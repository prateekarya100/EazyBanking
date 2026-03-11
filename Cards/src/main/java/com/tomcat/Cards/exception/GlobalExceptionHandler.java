package com.tomcat.Cards.exception;

import com.tomcat.Cards.dto.ErrorResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        HashMap<String, String> validationErrorResponse = new HashMap<>();
        List<ObjectError> errorList = ex.getBindingResult().getAllErrors();
        errorList.forEach(error -> {
            String errorField = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrorResponse.put(errorField, errorMessage);
        });
        return new ResponseEntity<>(validationErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> exception(Exception ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponseDto(
                request.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                LocalDateTime.now()
        ), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(CreditCardAlreadyExists.class)
    public ResponseEntity<ErrorResponseDto> handleCreditCardAlreadyExists(CreditCardAlreadyExists card, WebRequest request) {
        return new ResponseEntity<>(
                new ErrorResponseDto(request.getDescription(false),
                        HttpStatus.CONFLICT,
                        card.getMessage(),
                        LocalDateTime.now()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException ex,WebRequest request){
        return new ResponseEntity<>(
                new ErrorResponseDto(request.getDescription(false),
                        HttpStatus.NOT_FOUND,
                        ex.getMessage(),
                        LocalDateTime.now()),
                HttpStatus.NOT_FOUND);
    }
}
