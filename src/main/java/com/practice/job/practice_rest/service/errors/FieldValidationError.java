package com.practice.job.practice_rest.service.errors;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FieldValidationError {

    private String object;
    private String field;
    private Object rejectedValue;
    private String message;

    public FieldValidationError() {
    }

    public FieldValidationError(String object, String message) {
        this.object = object;
        this.message = message;
    }
}