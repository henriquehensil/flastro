package codes.shawlas.jdata.core.exception.file;

import codes.shawlas.jdata.core.exception.NameAlreadyExistsException;

public class FileAlreadyExistsException extends NameAlreadyExistsException {
    public FileAlreadyExistsException(String message) {
        super(message);
    }
}
