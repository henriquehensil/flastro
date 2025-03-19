package dev.hensil.flastro.api.exception;

import java.util.NoSuchElementException;

public class NoSuchUserException extends NoSuchElementException {
    public NoSuchUserException(String message) {
        super(message);
    }
}
