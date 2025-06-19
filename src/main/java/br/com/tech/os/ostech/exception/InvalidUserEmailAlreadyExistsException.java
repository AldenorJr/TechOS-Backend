package br.com.tech.os.ostech.exception;

public class InvalidUserEmailAlreadyExistsException extends RuntimeException {

    public InvalidUserEmailAlreadyExistsException(String message) {
        super(message);
    }

}
