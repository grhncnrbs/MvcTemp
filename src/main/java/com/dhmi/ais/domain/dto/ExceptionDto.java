package com.dhmi.ais.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ExceptionDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer httpStatusCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errorMessage;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String apiPath;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String httpMethod;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object errors;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;

    public ExceptionDto() {
        timestamp = LocalDateTime.now();
    }

    public ExceptionDto(Integer httpStatusCode, String errorMessage) {
        this();
        this.httpStatusCode = httpStatusCode;
        this.errorMessage = errorMessage;
    }

    public ExceptionDto(Integer httpStatusCode, String message, Object data) {
        this();
        this.httpStatusCode = httpStatusCode;
        this.message = message;
        this.data = data;
    }

    public ExceptionDto(Integer httpStatusCode, String message, String errorMessage, String httpMethod, String apiPath, Object data) {
        this();
        this.httpStatusCode = httpStatusCode;
        this.message = message;
        this.errorMessage = errorMessage;
        this.httpMethod = httpMethod;
        this.apiPath = apiPath;
        this.data = data;
    }

    public ExceptionDto(Integer httpStatusCode, String errorMessage, String httpMethod, String apiPath, Object errors) {
        this();
        this.httpStatusCode = httpStatusCode;
        this.message = message;
        this.errorMessage = errorMessage;
        this.httpMethod = httpMethod;
        this.apiPath = apiPath;
        this.errors = errors;
    }
}
