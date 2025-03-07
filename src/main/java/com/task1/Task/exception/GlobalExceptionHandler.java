//package com.task1.Task.exception;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//
//
//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<ApiResponse> handleResourceNotFound(ResourceNotFoundException exception){
//        ApiError apiError=ApiError.builder()
//                .httpStatus(HttpStatus.NOT_FOUND)
//                .message(exception.getMessage())
//                .build();
//
//        return buildErrorResponseEntity(apiError);
//    }
//
//    public ResponseEntity<ApiResponse> handleInternalServerError(Exception exception){
//        ApiError apiError=ApiError.builder()
//                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//                .message(exception.getMessage())
//                .build();
//
//        return buildErrorResponseEntity(apiError);
//    }
//
//
//    private ResponseEntity<ApiResponse> buildErrorResponseEntity(ApiError apiError){
//        return new ResponseEntity<>(new ApiResponse<>(apiError),apiError.getHttpStatus());
//    }
//
//}
