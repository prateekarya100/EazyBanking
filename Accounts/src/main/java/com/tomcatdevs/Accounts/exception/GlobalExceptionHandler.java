package com.tomcatdevs.Accounts.exception;

import com.tomcatdevs.Accounts.dto.ErrorResponseDto;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.security.auth.login.AccountNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // HANDLE JAKARTA BEAN ERROR RESPONSE
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        HashMap<String, String> validationErrorResponse = new HashMap<>();
        List<ObjectError> errorList = ex.getBindingResult().getAllErrors();
        errorList.forEach(error -> {
            String errorField= ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrorResponse.put(errorField, errorMessage);
        });
        return new ResponseEntity<>(validationErrorResponse, HttpStatus.BAD_REQUEST);
    }

    // HANDLE GLOBAL RUNTIME EXCEPTION
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalErrorException(Exception ex, WebRequest webRequest) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDto(
                        webRequest.getDescription(false),
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }


    @ExceptionHandler(CustomerAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleCustomerAlreadyExistsException(CustomerAlreadyExistsException ex, WebRequest webRequest) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setApiPath(webRequest.getDescription(false));
        errorResponseDto.setErrorCode(HttpStatus.BAD_REQUEST);
        errorResponseDto.setServerResponse(ex.getMessage());
        errorResponseDto.setErrorTime(LocalDateTime.now());
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest webRequest) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto(webRequest.getDescription(false),
                        HttpStatus.NOT_FOUND,
                        ex.getMessage(),
                        LocalDateTime.now()));
    }
}
