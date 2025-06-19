package br.com.tech.os.ostech.controller.advice;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import br.com.tech.os.ostech.exception.InvalidBudgetFieldException;
import br.com.tech.os.ostech.exception.InvalidBudgetIdException;

@ControllerAdvice
public class BudgetAdvice {

    @ExceptionHandler(InvalidBudgetIdException.class)
    public ResponseEntity<Map<String, String>> InvalidBudgetIdException(InvalidBudgetIdException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(InvalidBudgetFieldException.class)
    public ResponseEntity<Map<String, String>> InvalidBudgetFieldException(InvalidBudgetFieldException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

}
