package com.comkub.flashcardbackend.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
public class ErrorResponse {
    private  Timestamp timestamp;
    private  int status;
    private  String error;
    private  String message;
    private  String path;
    private  String instance;
    private List<FieldError> errors;

    public ErrorResponse(Timestamp timestamp, int status, String error, String message, List<FieldError> errors) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.errors = errors;
    }

    public ErrorResponse(Timestamp timestamp, int status, String error, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    @Data
    public static class FieldError {
        private String field;
        private String message;

        public FieldError(String field, String message) {
            this.field = field;
            this.message = message;
        }

    }
}
