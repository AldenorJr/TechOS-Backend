package br.com.tech.os.ostech.controller.advice;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import br.com.tech.os.ostech.exception.InvalidOrderServiceIdException;
import br.com.tech.os.ostech.exception.InvalidOrderServiceInformationException;

@ControllerAdvice
public class OrderServiceAdvice {
    
    @ExceptionHandler(InvalidOrderServiceInformationException.class)
    public ResponseEntity<Map<String, String>> InvalidSmartphoneInformationException(InvalidOrderServiceInformationException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

    @ExceptionHandler(InvalidOrderServiceIdException.class)
    public ResponseEntity<Map<String, String>> handleInvalidOrderServiceIdException(InvalidOrderServiceIdException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

}
