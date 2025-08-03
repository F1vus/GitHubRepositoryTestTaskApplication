package net.fiv.GitHubRepositoryTestTask.error;

import lombok.extern.slf4j.Slf4j;
import net.fiv.GitHubRepositoryTestTask.error.exception.ExternalApiFormatException;
import net.fiv.GitHubRepositoryTestTask.error.exception.NoSuchUserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@ControllerAdvice
@Slf4j
public class RestClientGlobalExceptionHandler {

    @ExceptionHandler(NoSuchUserException.class)
    public ResponseEntity<ErrorResponse> noSuchExistsException(NoSuchUserException exception) {
        var body = new ErrorResponse(404, exception.getMessage());
        log.warn(body.message());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(ExternalApiFormatException.class)
    public ResponseEntity<ErrorResponse> externalApiFormatException(ExternalApiFormatException exception) {
        var body = new ErrorResponse(502, "Invalid response from GitHub: "+exception.getMessage());
        log.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(body);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponse> handleClientError(HttpClientErrorException exception) {
        var body = new ErrorResponse(exception.getStatusCode().value(), exception.getResponseBodyAsString());
        log.error("HttpClientErrorException: {} http status code: {}", body.message(), exception.getStatusCode());
        return ResponseEntity.status(exception.getStatusCode()).body(body);
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<ErrorResponse>handleServerError(HttpServerErrorException exception) {
        var body = new ErrorResponse(exception.getStatusCode().value(), exception.getResponseBodyAsString());
        log.error("HttpServerErrorException: {} http status code: {}", body.message(), exception.getStatusCode());
        return ResponseEntity.status(exception.getStatusCode()).body(body);
    }

}
