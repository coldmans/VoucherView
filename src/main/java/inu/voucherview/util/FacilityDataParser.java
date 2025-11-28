package inu.voucherview.util;

import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class FacilityDataParser {

    private static final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    public static void main(String[] args){
        String csvFilePath = "/Users/coldmans/Downloads/KS_SPORTS_VOUCH_FCLTY_INFO_202507.csv";
        String jdbcUrl = "jdbc:mysql://34.47.83.48:3306/voucherview?useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC";
        String dbUser = "root";
        String dbPassword = "";

        try{
            parseAndInsertFacilities(csvFilePath, jdbcUrl, dbUser, dbPassword);
        } catch (Exception e){
            log.error("데이터 저장 실패", e);
        }
    }

    public static void parseAndInsertFacilities(String csvFilePath, String jdbcUrl, String dbUser, String dbPassword) throws Exception{
        Set<String> processedFacilities = new HashSet<>();
        int totalLines = 0;
        int insertedCount = 0;
        int skippedNoCoord = 0;
        int skippedDuplicate = 0;

        try (Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO facilities(name, zip_no, province_cd, province_name, city_cd, city_name, "+
                    "address, detail_address, tel_no, main_sport_id, main_sport, location) "+
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ST_GeomFromText(?, 4326))");
             BufferedReader br = new BufferedReader(new FileReader(csvFilePath))){
            String line;
            boolean isFirstLine = true;


            while((line = br.readLine()) != null){
                //컬럼명 건너뛰기
                if(isFirstLine){
                    isFirstLine = false;
                    continue;
                }

                totalLines++;

                try{
                    String[] fields = parseCsvLine(line);
                    if(fields.length < 14){
                        continue;
                    }

                    String facilityName = fields[4].trim();
                    String zipNo = fields[7].trim();
                    String fullAddress = fields[8].trim();
                    String xCoord = fields[12].trim();
                    String yCoord = fields[13].trim();

                    // 좌표 없으면 스킵
                    if(xCoord.isEmpty() || yCoord.isEmpty()){
                        skippedNoCoord++;
                        continue;
                    }

                    if(zipNo.isEmpty()){
                        skippedNoCoord++;
                        continue;
                    }

                    // 중복 시에 스킵
                    String facilityKey = facilityName + "|" + zipNo;
                    if(processedFacilities.contains(facilityKey)){
                        skippedDuplicate++;
                        continue;
                    }

                    double longitude = Double.parseDouble(xCoord);
                    double latitude = Double.parseDouble(yCoord);
                    Point location = geometryFactory.createPoint(new Coordinate(longitude, latitude));

                    pstmt.setString(1, facilityName);
                    pstmt.setString(2, zipNo);
                    pstmt.setString(3, fields[0]);
                    pstmt.setString(4, fields[1]);
                    pstmt.setString(5, fields[2]);
                    pstmt.setString(6, fields[3]);
                    pstmt.setString(7, fullAddress);
                    pstmt.setString(8, fields[9]);
                    pstmt.setString(9, fields[6]);
                    pstmt.setLong(10, fields[10].isEmpty() ? 0 : Long.parseLong(fields[10]));
                    pstmt.setString(11, fields[11]);
                    // MySQL SRID 4326에서는 POINT(위도 경도) 순서
                    pstmt.setString(12, String.format("POINT(%f %f)", latitude, longitude));

                    pstmt.addBatch();;
                    processedFacilities.add(facilityKey);
                    insertedCount++;

                    // 100개씩 넣음
                    if(insertedCount % 100 == 0){
                        pstmt.executeBatch();
                    }
                }
                catch (Exception e){
                    log.error("에러 line {}: {}", totalLines, e.getMessage());
                }
            }
            //나머지 데이터 넣음
            pstmt.executeBatch();
            log.info("파싱 끝");
            log.info("모든 라인: {}", totalLines);
            log.info("저장 수: {}", insertedCount);
            log.info("좌표 없는 튜플: {}", skippedNoCoord);
            log.info("중복 튜플: {}", skippedDuplicate);

        }
    }
    private static String[] parseCsvLine(String line){
        List<String> result = new ArrayList<>();
        boolean inQ = false;
        StringBuilder currentFiled = new StringBuilder();

        for(int i = 0; i < line.length(); i++){
            char c = line.charAt(i);

            if(c == '"'){
                inQ = !inQ;
            }
            else if(c == ',' && !inQ){
                result.add(currentFiled.toString());
                currentFiled = new StringBuilder();
            }
            else{
                currentFiled.append(c);
            }
        }
        result.add(currentFiled.toString());
        return result.toArray(new String[0]);
    }
}