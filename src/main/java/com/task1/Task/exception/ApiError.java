package com.task1.Task.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ApiError {

    private HttpStatus httpStatus;
    private String message;

}
