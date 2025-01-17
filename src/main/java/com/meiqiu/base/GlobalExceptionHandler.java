package com.meiqiu.base;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 全局异常处理
 * @Author sgh
 * @Date 2025/1/17
 * @Time 15:50
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    // 拦截所有异常类型
    @ExceptionHandler(
            {Exception.class})
    @ResponseBody
    public ResponseEntity<Object> handleException(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
//        if (ex instanceof ServiceException) {
//            ServiceException e = (ServiceException) ex;
//            errorResponse.setCode(e.getCode());
//            errorResponse.setMessage(e.getMessage());
//            return ResponseEntity.internalServerError().body(errorResponse);
//        } else
        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException e = (MethodArgumentNotValidException) ex;
            BindingResult bindingResult = e.getBindingResult();
            List<ValidationError> validationErrors = new ArrayList<>();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                validationErrors.add(new ValidationError(fieldError.getField(), fieldError.getDefaultMessage()));
            }
//            for (ObjectError objectError : bindingResult.getAllErrors()) {
//                // 处理非字段级别的错误（如全局约束）
//                validationErrors.add(new ValidationError(null, objectError.getDefaultMessage()));
//            }
            ValidationErrorResponse validationErrorResponse = new ValidationErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "Validation failed",
                    validationErrors
            );
            return new ResponseEntity<>(validationErrorResponse, HttpStatus.BAD_REQUEST);
        } else {
            errorResponse.setCode("500");
            errorResponse.setMessage(ex.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    // 错误信息的数据结构
    static class ErrorResponse {
        private String code;
        private String message;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    // ValidationError 类用于封装单个验证错误
    static class ValidationError {
        private String field;
        private String message;

        public ValidationError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        // Getters and Setters
        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }


    // ValidationErrorResponse 类用于封装整体的验证错误响应
    static class ValidationErrorResponse {
        private int status;
        private String message;
        private List<ValidationError> errors;

        public ValidationErrorResponse(int status, String message, List<ValidationError> errors) {
            this.status = status;
            this.message = message;
            this.errors = errors;
        }

        // Getters and Setters
        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<ValidationError> getErrors() {
            return errors;
        }

        public void setErrors(List<ValidationError> errors) {
            this.errors = errors;
        }
    }
}
