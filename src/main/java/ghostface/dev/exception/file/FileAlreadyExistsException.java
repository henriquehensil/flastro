package ghostface.dev.exception.file;

import ghostface.dev.exception.NameAlreadyExistsException;

public class FileAlreadyExistsException extends NameAlreadyExistsException {
    public FileAlreadyExistsException(String message) {
        super(message);
    }
}
