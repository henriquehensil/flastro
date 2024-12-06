package ghostface.dev.exception.nest;

import ghostface.dev.exception.NameAlreadyExistsException;

public class NestAlreadyExistsException extends NameAlreadyExistsException {
    public NestAlreadyExistsException(String message) {
        super(message);
    }
}
