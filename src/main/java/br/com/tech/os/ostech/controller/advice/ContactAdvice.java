package br.com.tech.os.ostech.controller.advice;

import br.com.tech.os.ostech.exception.InvalidClientIdException;
import br.com.tech.os.ostech.exception.InvalidContactIdException;
import br.com.tech.os.ostech.exception.InvalidContactInformationException;
import br.com.tech.os.ostech.exception.InvalidNameTechnicalAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ContactAdvice {

    @ExceptionHandler(InvalidContactInformationException.class)
    public ResponseEntity<Map<String, String>> InvalidContactInformationException(InvalidContactInformationException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

    @ExceptionHandler(InvalidClientIdException.class)
    public ResponseEntity<Map<String, String>> handleInvalidClientIdException(InvalidClientIdException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(InvalidContactIdException.class)
    public ResponseEntity<Map<String, String>> handleInvalidContactIdException(InvalidContactIdException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }


}
