package com.tp.tradexcelsior.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.tp.tradexcelsior.dto.response.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  // Handle UserNotFoundException
  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<CommonResponse> handleUserNotFoundException(UserNotFoundException ex) {
    CommonResponse commonResponse = new CommonResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    return new ResponseEntity<>(commonResponse, HttpStatus.NOT_FOUND);
  }

  // Handle UserAlreadyExistsException
  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<CommonResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
    CommonResponse commonResponse = new CommonResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    return new ResponseEntity<>(commonResponse, HttpStatus.BAD_REQUEST);
  }

  // Handle ValidationException
  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<CommonResponse> handleValidationException(ValidationException ex) {
    CommonResponse commonResponse = new CommonResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    return new ResponseEntity<>(commonResponse, HttpStatus.BAD_REQUEST);
  }

  // Handle malformed JSON (HttpMessageNotReadableException)
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<CommonResponse> handleMalformedJson(HttpMessageNotReadableException ex) {
    String errorMessage = "Malformed JSON request. Please check your syntax and ensure proper formatting.";
    CommonResponse commonResponse = new CommonResponse(HttpStatus.BAD_REQUEST.value(), errorMessage);
    return new ResponseEntity<>(commonResponse, HttpStatus.BAD_REQUEST);
  }

  // Handle JSON parse exception (e.g., invalid JSON format, trailing commas)
  @ExceptionHandler(JsonParseException.class)
  public ResponseEntity<CommonResponse> handleJsonParseException(JsonParseException ex) {
    String errorMessage = "Invalid JSON format: " + ex.getMessage();
    CommonResponse commonResponse = new CommonResponse(HttpStatus.BAD_REQUEST.value(), errorMessage);
    return new ResponseEntity<>(commonResponse, HttpStatus.BAD_REQUEST);
  }

  // Handle JSON mapping exception (e.g., invalid or missing fields for expected DTO)
  @ExceptionHandler(JsonMappingException.class)
  public ResponseEntity<CommonResponse> handleJsonMappingException(JsonMappingException ex) {
    String errorMessage = "Error mapping JSON: " + ex.getMessage();
    CommonResponse commonResponse = new CommonResponse(HttpStatus.BAD_REQUEST.value(), errorMessage);
    return new ResponseEntity<>(commonResponse, HttpStatus.BAD_REQUEST);
  }

  // Handle IllegalArgumentException (bad requests)
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<CommonResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
    CommonResponse commonResponse = new CommonResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    return new ResponseEntity<>(commonResponse, HttpStatus.BAD_REQUEST);
  }

  // Handle RuntimeException (generic exception handler)
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<CommonResponse> handleRuntimeException(RuntimeException ex) {
    CommonResponse commonResponse = new CommonResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    return new ResponseEntity<>(commonResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
