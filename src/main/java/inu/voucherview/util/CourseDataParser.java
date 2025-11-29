package inu.voucherview.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
public class CourseDataParser {

    public static void main(String[] args) {
        String csvFilePath = "/Users/coldmans/Downloads/KS_SVCH_UTILIIZA_CRSTAT_INFO_202507.csv";
        String jdbcUrl = "jdbc:mysql://34.47.83.48:3306/voucherview?useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC";
        String dbUser = "root";
        String dbPassword = "";

        try {
            parseAndInsertCourses(csvFilePath, jdbcUrl, dbUser, dbPassword);
        } catch (Exception e) {
            log.error("데이터 저장 실패", e);
        }
    }

    public static void parseAndInsertCourses(String csvFilePath, String jdbcUrl, String dbUser, String dbPassword)
            throws Exception {

        // facility_name + zip_no -> facility_id 매핑 캐시
        Map<String, Long> facilityCache = new HashMap<>();

        int totalLines = 0;
        int insertedCount = 0;
        int skippedNoFacility = 0;
        int skippedDuplicate = 0;

        Set<String> processedCourses = new HashSet<>();

        try (Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
             PreparedStatement selectStmt = conn.prepareStatement(
                     "SELECT facility_id FROM facilities WHERE name = ? AND zip_no = ?");
             PreparedStatement insertStmt = conn.prepareStatement(
                     "INSERT INTO courses (facility_id, course_no, course_name, sport_cd, sport_name, " +
                             "start_date, end_date, establishment_year, establishment_month, request_count, price) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
             BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {

            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // 헤더 스킵
                }

                totalLines++;

                try {
                    String[] fields = parseCsvLine(line);

                    if (fields.length < 20) {
                        continue;
                    }

                    // CSV 컬럼 인덱스:
                    // 0: BSNS_NO, 1: FCLTY_NM, 2: ITEM_CD, 3: ITEM_NM
                    // 4: CTPRVN_CD, 5: CTPRVN_NM, 6: SIGNGU_CD, 7: SIGNGU_NM
                    // 8: FCLTY_ADDR, 9: FCLTY_DETAIL_ADDR, 10: ZIP_NO, 11: TEL_NO
                    // 12: COURSE_NM, 13: COURSE_NO, 14: COURSE_ESTBL_YEAR, 15: COURSE_ESTBL_MT
                    // 16: COURSE_BEGIN_DE, 17: COURSE_END_DE, 18: COURSE_REQST_NMPR_CO, 19: COURSE_PRC

                    String facilityName = fields[1].trim();
                    String zipNo = fields[10].trim();
                    String courseNo = fields[13].trim();
                    String courseName = fields[12].trim();

                    if (zipNo.isEmpty() || courseNo.isEmpty()) {
                        skippedNoFacility++;
                        continue;
                    }

                    // 중복 체크 (course_no + 시작일)
                    String courseKey = courseNo + "|" + fields[16].trim();
                    if (processedCourses.contains(courseKey)) {
                        skippedDuplicate++;
                        continue;
                    }

                    // facility_id 조회 (캐시 사용)
                    String facilityKey = facilityName + "|" + zipNo;
                    Long facilityId = facilityCache.get(facilityKey);

                    if (facilityId == null) {
                        selectStmt.setString(1, facilityName);
                        selectStmt.setString(2, zipNo);
                        try (ResultSet rs = selectStmt.executeQuery()) {
                            if (rs.next()) {
                                facilityId = rs.getLong("facility_id");
                                facilityCache.put(facilityKey, facilityId);
                            } else {
                                skippedNoFacility++;
                                continue;
                            }
                        }
                    }

                    // 날짜 파싱
                    LocalDate startDate = parseDate(fields[16].trim());
                    LocalDate endDate = parseDate(fields[17].trim());

                    // DB 삽입
                    insertStmt.setLong(1, facilityId);
                    insertStmt.setString(2, courseNo);
                    insertStmt.setString(3, courseName);
                    insertStmt.setString(4, fields[2].trim()); // ITEM_CD
                    insertStmt.setString(5, fields[3].trim()); // ITEM_NM
                    insertStmt.setDate(6, startDate != null ? java.sql.Date.valueOf(startDate) : null);
                    insertStmt.setDate(7, endDate != null ? java.sql.Date.valueOf(endDate) : null);
                    insertStmt.setString(8, fields[14].trim()); // COURSE_ESTBL_YEAR
                    insertStmt.setString(9, fields[15].trim()); // COURSE_ESTBL_MT
                    insertStmt.setInt(10, fields[18].isEmpty() ? 0 : Integer.parseInt(fields[18])); // COURSE_REQST_NMPR_CO
                    insertStmt.setBigDecimal(11, fields[19].isEmpty() ? null : new java.math.BigDecimal(fields[19])); // COURSE_PRC

                    insertStmt.addBatch();
                    processedCourses.add(courseKey);
                    insertedCount++;

                    // 100개마다 배치 실행
                    if (insertedCount % 100 == 0) {
                        insertStmt.executeBatch();
                        log.info("현재까지 실행 된 것: {}", insertedCount);
                    }

                } catch (Exception e) {
                    log.error("에러 line {}: {}", totalLines, e.getMessage());
                }
            }

            // 마지막 배치 실행
            insertStmt.executeBatch();

            log.info("파싱 끝");
            log.info("모든 라인: {}", totalLines);
            log.info("저장 수: {}", insertedCount);
            log.info("시설 없는 튜플: {}", skippedNoFacility);
            log.info("중복 튜플: {}", skippedDuplicate);
        }
    }

    private static LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty() || dateStr.length() != 8) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyyMMdd"));
        } catch (Exception e) {
            return null;
        }
    }

    private static String[] parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentField = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(currentField.toString());
                currentField = new StringBuilder();
            } else {
                currentField.append(c);
            }
        }

        result.add(currentField.toString());
        return result.toArray(new String[0]);
    }
}