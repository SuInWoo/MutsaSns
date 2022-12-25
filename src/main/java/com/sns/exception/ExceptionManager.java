package com.sns.exception;

import com.sns.domain.dto.ErrorResponse;
import com.sns.domain.dto.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionManager {
    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> userAppExceptionHandler(AppException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode(), e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(Response.error("ERROR", errorResponse));
    }
}
