package br.com.tech.os.ostech.controller.advice;

import br.com.tech.os.ostech.exception.InvalidUserEmailAlreadyExistsException;
import br.com.tech.os.ostech.exception.InvalidUserFieldException;
import br.com.tech.os.ostech.exception.InvalidUserIdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class UserAdvice {

    @ExceptionHandler(InvalidUserEmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleInvalidUserEmailAlreadyExists(InvalidUserEmailAlreadyExistsException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

    @ExceptionHandler(InvalidUserIdException.class)
    public ResponseEntity<Map<String, String>> handleInvalidUserIdException(InvalidUserIdException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(InvalidUserFieldException.class)
    public ResponseEntity<Map<String, String>> handleInvalidUserFieldException(InvalidUserFieldException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

}
