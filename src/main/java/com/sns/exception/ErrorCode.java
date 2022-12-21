package com.sns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "유저이름이 중복되었습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "Not Found"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "유효하지 않은 비밀번호 입니다.");

    private HttpStatus status;
    private String message;
}
