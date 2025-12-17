package com.apiorbit.lovableclone.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;

public record ApiError(
        HttpStatus status,
        String message,
        Instant timestamp,
        @JsonInclude(JsonInclude.Include.NON_NULL) List<SubError> subErrorList
) {
    ApiError(
            HttpStatus status,
            String message) {
        this(status, message, Instant.now(), null);
    }
    ApiError(HttpStatus status, String message, List < SubError > subErrorList){
        this(status, message, Instant.now(), subErrorList);
    }
}

record SubError(
        String field,
        String message
) {
}
