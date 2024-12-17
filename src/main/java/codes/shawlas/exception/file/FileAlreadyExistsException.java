package codes.shawlas.exception.file;

import codes.shawlas.exception.NameAlreadyExistsException;

public class FileAlreadyExistsException extends NameAlreadyExistsException {
    public FileAlreadyExistsException(String message) {
        super(message);
    }
}
