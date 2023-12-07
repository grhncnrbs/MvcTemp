package com.dhmi.ais.domain.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class ResponseDto {

    public static ResponseEntity<ExceptionDto> errorResponse(String message) {
        return new ResponseEntity<>(new ExceptionDto(HttpStatus.OK.value(), message), HttpStatusCode.valueOf(HttpStatus.OK.value()));
    }

    public static ResponseEntity<ExceptionDto> successResponse(String message, Object data) {
        return new ResponseEntity<>(new ExceptionDto(HttpStatus.OK.value(), message, data), HttpStatusCode.valueOf(HttpStatus.OK.value()));
    }

    public static ResponseEntity<ExceptionDto> successResponse(String message) {
        return new ResponseEntity<>(new ExceptionDto(HttpStatus.OK.value(), message), HttpStatusCode.valueOf(HttpStatus.OK.value()));
    }
}
