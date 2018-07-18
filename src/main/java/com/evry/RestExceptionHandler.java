package com.evry;

import com.evry.dto.error.ErrorDetail;
import com.evry.dto.error.ValidationError;
import com.evry.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                               HttpHeaders headers,
                                                               HttpStatus status,
                                                               WebRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimeStamp(new Date().getTime());
        errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDetail.setTitle("Validation Failed");
        errorDetail.setDetail("Input validation failed");
        errorDetail.setDeveloperMessage(exception.getClass().getName());

        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            List<ValidationError> validationErrorList = errorDetail.getErrors().get(fieldError.getField());
            if (validationErrorList == null) {
                validationErrorList = new ArrayList<>();
                errorDetail.getErrors().put(fieldError.getField(), validationErrorList);
            }
            ValidationError validationError = new ValidationError();
            validationError.setCode(fieldError.getCode());
            validationError.setMessage(fieldError.getField());
            validationErrorList.add(validationError);
        }
        return handleExceptionInternal(exception, errorDetail, headers, status, request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException exception, HttpServletRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimeStamp(new Date().getTime());
        errorDetail.setStatus(HttpStatus.NOT_FOUND.value());
        errorDetail.setTitle("Resource not found");
        errorDetail.setDetail(exception.getMessage());
        errorDetail.setDeveloperMessage(exception.getClass().getSimpleName());

        return new ResponseEntity<>(errorDetail, null, HttpStatus.NOT_FOUND);
    }

}
