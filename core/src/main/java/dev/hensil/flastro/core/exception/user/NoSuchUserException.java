package dev.hensil.flastro.core.exception.user;

import java.util.NoSuchElementException;

public class NoSuchUserException extends NoSuchElementException {
    public NoSuchUserException(String message) {
        super(message);
    }
}
