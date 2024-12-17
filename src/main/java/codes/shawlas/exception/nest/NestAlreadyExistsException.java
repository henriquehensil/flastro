package codes.shawlas.exception.nest;

import codes.shawlas.exception.NameAlreadyExistsException;

public class NestAlreadyExistsException extends NameAlreadyExistsException {
    public NestAlreadyExistsException(String message) {
        super(message);
    }
}
