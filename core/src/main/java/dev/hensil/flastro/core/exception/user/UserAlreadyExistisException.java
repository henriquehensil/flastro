package dev.hensil.flastro.core.exception.user;

public class UserAlreadyExistisException extends RuntimeException {
    public UserAlreadyExistisException(String message) {
        super(message);
    }
}
