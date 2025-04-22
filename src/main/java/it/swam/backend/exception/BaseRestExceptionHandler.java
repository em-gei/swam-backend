package it.swam.backend.exception;

import static org.springframework.http.HttpHeaders.EMPTY;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class BaseRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ConstraintViolationException.class, DuplicateKeyException.class, IllegalArgumentException.class})
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<Object> handleConstraintViolationException(Exception e, WebRequest request) {
        var body = getProblemDetail(e, BAD_REQUEST, request);

        return handleExceptionInternal(e, body, EMPTY, BAD_REQUEST, request);
    }

    @NotNull
    protected ProblemDetail getProblemDetail(Exception e, HttpStatus status, WebRequest request) {
        var problemDetail = createProblemDetail(e, status, e.getMessage(), null, null, request);
        if (e instanceof BaseRestException baseRestException && baseRestException.getBody() != null) {
            problemDetail.setType(baseRestException.getBody().getType());
        }
        return problemDetail;
    }
}
