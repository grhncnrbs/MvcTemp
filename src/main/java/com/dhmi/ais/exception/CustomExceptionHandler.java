package com.dhmi.ais.exception;

import com.dhmi.ais.domain.dto.ExceptionDto;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.rmi.ServerException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomExceptionHandler.class);
    int httpStatusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
    String errorMessage = null;
    String apiPath = null;
    String httpMethod = null;
    Object data = null;
    Object errors = null;

    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleDataIntegrityViolationException(Exception exception, WebRequest request) {
        httpStatusCode = HttpStatus.CONFLICT.value();
        errorMessage = exception.getMessage();
        String message = "Komut çalıştırılamadı.";
        apiPath = ((ServletWebRequest) request).getRequest().getRequestURI();
        httpMethod = ((ServletWebRequest) request).getRequest().getMethod();
        ExceptionDto exceptionResponseDTO = new ExceptionDto(httpStatusCode, message, errorMessage, httpMethod, apiPath, data);
        return new ResponseEntity<>(exceptionResponseDTO, HttpStatusCode.valueOf(httpStatusCode));
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<Object> handleIllegalArgumentException(Exception exception, WebRequest request) {
        httpStatusCode = HttpStatus.BAD_REQUEST.value();
        errorMessage = exception.getMessage();
        apiPath = ((ServletWebRequest) request).getRequest().getRequestURI();
        httpMethod = ((ServletWebRequest) request).getRequest().getMethod();
        ExceptionDto exceptionResponseDTO = new ExceptionDto(httpStatusCode, null, errorMessage, httpMethod, apiPath, data);
        return new ResponseEntity<>(exceptionResponseDTO, HttpStatusCode.valueOf(httpStatusCode));
    }

    @ExceptionHandler(value = {NullPointerException.class})
    public ResponseEntity<Object> handleNullPointerException(Exception exception, WebRequest request) {
        httpStatusCode = HttpStatus.NOT_FOUND.value();
        errorMessage = exception.getMessage();
        apiPath = ((ServletWebRequest) request).getRequest().getRequestURI();
        httpMethod = ((ServletWebRequest) request).getRequest().getMethod();
        ExceptionDto exceptionResponseDTO = new ExceptionDto(httpStatusCode, null, errorMessage, httpMethod, apiPath, data);
        return new ResponseEntity<>(exceptionResponseDTO, HttpStatusCode.valueOf(httpStatusCode));
    }

    @ExceptionHandler(value = {BadCredentialsException.class})
    public ResponseEntity<Object> handleBadCredentialsException(Exception exception, WebRequest request) {
        httpStatusCode = HttpStatus.UNAUTHORIZED.value();
        errorMessage = exception.getMessage();
        apiPath = ((ServletWebRequest) request).getRequest().getRequestURI();
        httpMethod = ((ServletWebRequest) request).getRequest().getMethod();
        ExceptionDto exceptionResponseDTO = new ExceptionDto(httpStatusCode, null, errorMessage, httpMethod, apiPath, data);
        return new ResponseEntity<>(exceptionResponseDTO, HttpStatusCode.valueOf(httpStatusCode));
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(Exception exception, WebRequest request) {
        httpStatusCode = HttpStatus.FORBIDDEN.value();
        errorMessage = exception.getMessage();
        apiPath = ((ServletWebRequest) request).getRequest().getRequestURI();
        httpMethod = ((ServletWebRequest) request).getRequest().getMethod();
        ExceptionDto exceptionResponseDTO = new ExceptionDto(httpStatusCode, null, errorMessage, httpMethod, apiPath, data);
        return new ResponseEntity<>(exceptionResponseDTO, HttpStatusCode.valueOf(httpStatusCode));
    }

    @ExceptionHandler(value = {CustomException.class})
    public ResponseEntity<Object> handleCustomException(CustomException exception, WebRequest request) {
        httpStatusCode = exception.getErrorCode();
        errorMessage = exception.getMessage();
        apiPath = ((ServletWebRequest) request).getRequest().getRequestURI();
        httpMethod = ((ServletWebRequest) request).getRequest().getMethod();
        ExceptionDto exceptionResponseDTO = new ExceptionDto(httpStatusCode, null, errorMessage, httpMethod, apiPath, data);
        return new ResponseEntity<>(exceptionResponseDTO, HttpStatusCode.valueOf(httpStatusCode));
    }

    @ExceptionHandler(value = {InsufficientAuthenticationException.class})
    public ResponseEntity<Object> handleInsufficientAuthenticationException(Exception exception, WebRequest request) {
        httpStatusCode = HttpStatus.BAD_REQUEST.value();
        errorMessage = exception.getMessage();
        apiPath = ((ServletWebRequest) request).getRequest().getRequestURI();
        httpMethod = ((ServletWebRequest) request).getRequest().getMethod();
        ExceptionDto exceptionResponseDTO = new ExceptionDto(httpStatusCode, null, errorMessage, httpMethod, apiPath, data);
        return new ResponseEntity<>(exceptionResponseDTO, HttpStatusCode.valueOf(httpStatusCode));
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public ResponseEntity<Object> handleHttpMessageNotReadableException(Exception exception, WebRequest request) {
        httpStatusCode = HttpStatus.BAD_REQUEST.value();
        errorMessage = exception.getMessage();
        apiPath = ((ServletWebRequest) request).getRequest().getRequestURI();
        httpMethod = ((ServletWebRequest) request).getRequest().getMethod();
        ExceptionDto exceptionResponseDTO = new ExceptionDto(httpStatusCode, null, errorMessage, httpMethod, apiPath, data);
        return new ResponseEntity<>(exceptionResponseDTO, HttpStatusCode.valueOf(httpStatusCode));
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, WebRequest request) {
        httpStatusCode = HttpStatus.BAD_REQUEST.value();
        Map<String, String> errorsMap = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errorsMap.put(fieldName, errorMessage);
        });
        errorMessage = exception.getMessage().substring(0, 30);
        errors = errorsMap;
        apiPath = ((ServletWebRequest) request).getRequest().getRequestURI();
        httpMethod = ((ServletWebRequest) request).getRequest().getMethod();
        ExceptionDto exceptionResponseDTO = new ExceptionDto(httpStatusCode, errorMessage, httpMethod, apiPath, errors);
        return new ResponseEntity<>(exceptionResponseDTO, HttpStatusCode.valueOf(httpStatusCode));
    }

    @ExceptionHandler(value = {ServerException.class})
    public ResponseEntity<Object> handleServerException(Exception exception, WebRequest request) {
        httpStatusCode = HttpStatus.BAD_REQUEST.value();
        errorMessage = exception.getMessage();
        apiPath = ((ServletWebRequest) request).getRequest().getRequestURI();
        httpMethod = ((ServletWebRequest) request).getRequest().getMethod();
        ExceptionDto exceptionResponseDTO = new ExceptionDto(httpStatusCode, null, errorMessage, httpMethod, apiPath, data);
        return new ResponseEntity<>(exceptionResponseDTO, HttpStatusCode.valueOf(httpStatusCode));
    }

    @ExceptionHandler(value = {SignatureException.class})
    public ResponseEntity<Object> handleSignatureException(Exception exception, WebRequest request) {
        httpStatusCode = HttpStatus.FORBIDDEN.value();
        errorMessage = exception.getMessage();
        apiPath = ((ServletWebRequest) request).getRequest().getRequestURI();
        httpMethod = ((ServletWebRequest) request).getRequest().getMethod();
        ExceptionDto exceptionResponseDTO = new ExceptionDto(httpStatusCode, null, errorMessage, httpMethod, apiPath, data);
        return new ResponseEntity<>(exceptionResponseDTO, HttpStatusCode.valueOf(httpStatusCode));
    }

    @ExceptionHandler(value = {ExpiredJwtException.class})
    public ResponseEntity<Object> handleExpiredJwtException(Exception exception, WebRequest request) {
        httpStatusCode = HttpStatus.FORBIDDEN.value();
        errorMessage = exception.getMessage();
        apiPath = ((ServletWebRequest) request).getRequest().getRequestURI();
        httpMethod = ((ServletWebRequest) request).getRequest().getMethod();
        ExceptionDto exceptionResponseDTO = new ExceptionDto(httpStatusCode, null, errorMessage, httpMethod, apiPath, data);
        return new ResponseEntity<>(exceptionResponseDTO, HttpStatusCode.valueOf(httpStatusCode));
    }

    @ExceptionHandler(value = {MalformedJwtException.class})
    public ResponseEntity<Object> handleMalformedJwtException(Exception exception, WebRequest request) {
        httpStatusCode = HttpStatus.FORBIDDEN.value();
        errorMessage = exception.getMessage();
        apiPath = ((ServletWebRequest) request).getRequest().getRequestURI();
        httpMethod = ((ServletWebRequest) request).getRequest().getMethod();
        ExceptionDto exceptionResponseDTO = new ExceptionDto(httpStatusCode, null, errorMessage, httpMethod, apiPath, data);
        return new ResponseEntity<>(exceptionResponseDTO, HttpStatusCode.valueOf(httpStatusCode));
    }

    @ExceptionHandler(value = {UnsupportedJwtException.class})
    public ResponseEntity<Object> handleUnsupportedJwtException(Exception exception, WebRequest request) {
        httpStatusCode = HttpStatus.FORBIDDEN.value();
        errorMessage = exception.getMessage();
        apiPath = ((ServletWebRequest) request).getRequest().getRequestURI();
        httpMethod = ((ServletWebRequest) request).getRequest().getMethod();
        ExceptionDto exceptionResponseDTO = new ExceptionDto(httpStatusCode, null, errorMessage, httpMethod, apiPath, data);
        return new ResponseEntity<>(exceptionResponseDTO, HttpStatusCode.valueOf(httpStatusCode));
    }

    @ExceptionHandler(value = {UsernameNotFoundException.class})
    public ResponseEntity<Object> handleUsernameNotFoundException(Exception exception, WebRequest request) {
        httpStatusCode = HttpStatus.FORBIDDEN.value();
        errorMessage = exception.getMessage();
        apiPath = ((ServletWebRequest) request).getRequest().getRequestURI();
        httpMethod = ((ServletWebRequest) request).getRequest().getMethod();
        ExceptionDto exceptionResponseDTO = new ExceptionDto(httpStatusCode, null, errorMessage, httpMethod, apiPath, data);
        return new ResponseEntity<>(exceptionResponseDTO, HttpStatusCode.valueOf(httpStatusCode));
    }
}
