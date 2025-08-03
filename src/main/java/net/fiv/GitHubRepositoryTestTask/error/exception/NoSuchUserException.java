package net.fiv.GitHubRepositoryTestTask.error.exception;

public class NoSuchUserException extends RuntimeException {
    public NoSuchUserException(String message) {
        super(message);
    }
}
