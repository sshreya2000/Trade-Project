package com.tp.tradexcelsior.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.tp.tradexcelsior.dto.response.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle Book Not Found Exception
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleBookNotFoundException(BookNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

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
    
  // Handle SuccessStoryAlreadyExistsException
  @ExceptionHandler(SuccessStoryAlreadyExistsException.class)
  public ResponseEntity<CommonResponse> handleSuccessStoryAlreadyExistsException(SuccessStoryAlreadyExistsException ex) {
    // You can customize the error message further, if needed
    CommonResponse commonResponse = new CommonResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    return new ResponseEntity<>(commonResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<CommonResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
    StringBuilder message = new StringBuilder();
    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      message.append(error.getField())
          .append(": ")
          .append(error.getDefaultMessage())
          .append("; ");
    }

    CommonResponse commonResponse = new CommonResponse(HttpStatus.BAD_REQUEST.value(), message.toString());
    return new ResponseEntity<>(commonResponse, HttpStatus.BAD_REQUEST);
  }

    // Handle ValidationException
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<CommonResponse> handleValidationException(ValidationException ex) {
        CommonResponse commonResponse = new CommonResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(commonResponse, HttpStatus.BAD_REQUEST);
    }

    // Handle Invalid Book Data Exception
    @ExceptionHandler(InvalidBookException.class)
    public ResponseEntity<Map<String, String>> handleInvalidBookException(InvalidBookException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Handle malformed JSON (HttpMessageNotReadableException)
//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    public ResponseEntity<CommonResponse> handleMalformedJson(HttpMessageNotReadableException ex) {
//        String errorMessage = "Malformed JSON request. Please check your syntax and ensure proper formatting.";
//        CommonResponse commonResponse = new CommonResponse(HttpStatus.BAD_REQUEST.value(), errorMessage);
//        return new ResponseEntity<>(commonResponse, HttpStatus.BAD_REQUEST);
//    }

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

    @ExceptionHandler(DuplicateBookException.class)
    public ResponseEntity<String> handleDuplicateBookException(DuplicateBookException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    // Handle RuntimeException (generic exception handler)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CommonResponse> handleRuntimeException(RuntimeException ex) {
        CommonResponse commonResponse = new CommonResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        return new ResponseEntity<>(commonResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle Checklist Not Found Exception
    @ExceptionHandler(ChecklistNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleChecklistNotFoundException(ChecklistNotFoundException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Checklist Not Found");
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // Handle Generic Exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle Invalid Checklist Exception
    @ExceptionHandler(InvalidChecklistException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidChecklistException(InvalidChecklistException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Handle Duplicate Checklist Exception
    @ExceptionHandler(DuplicateChecklistException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateChecklistException(DuplicateChecklistException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<CommonResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        StringBuilder message = new StringBuilder();
//        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
//            message.append(error.getField())
//                    .append(": ")
//                    .append(error.getDefaultMessage())
//                    .append("; ");
//        }
//
//        CommonResponse commonResponse = new CommonResponse(HttpStatus.BAD_REQUEST.value(), message.toString());
//        return new ResponseEntity<>(commonResponse, HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(ReferenceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleReferenceNotFoundException(ReferenceNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidReferenceException.class)
    public ResponseEntity<Map<String, String>> handleInvalidReferenceException(InvalidReferenceException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateReferenceException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateReferenceException(DuplicateReferenceException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

}
