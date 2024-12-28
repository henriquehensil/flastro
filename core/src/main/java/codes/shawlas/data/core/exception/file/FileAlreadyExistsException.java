package codes.shawlas.data.core.exception.file;

import codes.shawlas.data.core.exception.NameAlreadyExistsException;

public class FileAlreadyExistsException extends NameAlreadyExistsException {
    public FileAlreadyExistsException(String message) {
        super(message);
    }
}
