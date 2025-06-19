package br.com.tech.os.ostech.controller.advice;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import br.com.tech.os.ostech.exception.InvalidSmartphoneIdException;
import br.com.tech.os.ostech.exception.InvalidSmartphoneInformationException;

@ControllerAdvice
public class SmartphoneAdvice {

    @ExceptionHandler(InvalidSmartphoneInformationException.class)
    public ResponseEntity<Map<String, String>> InvalidSmartphoneInformationException(InvalidSmartphoneInformationException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

    @ExceptionHandler(InvalidSmartphoneIdException.class)
    public ResponseEntity<Map<String, String>> handleInvalidClientIdException(InvalidSmartphoneIdException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }


}
