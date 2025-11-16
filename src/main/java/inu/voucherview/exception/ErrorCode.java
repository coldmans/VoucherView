package inu.voucherview.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 400 BAD_REQUEST
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "C400", "잘못된 요청입니다."),

    // 404 NOT_FOUND
    FACILITY_NOT_FOUND(HttpStatus.NOT_FOUND, "F404", "시설을 찾을 수 없습니다."),
    COURSE_NOT_FOUND(HttpStatus.NOT_FOUND, "C404", "강좌를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
