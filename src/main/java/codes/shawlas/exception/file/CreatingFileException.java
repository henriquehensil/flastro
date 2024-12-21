package codes.shawlas.exception.file;

import java.io.IOException;

public class CreatingFileException extends IOException {
    public CreatingFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
