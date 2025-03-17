package com.henriquegustavo.flastro.exception;

import java.util.NoSuchElementException;

public class NoSuchUserPermissionsException extends NoSuchElementException {
    public NoSuchUserPermissionsException(String message) {
        super(message);
    }
}
