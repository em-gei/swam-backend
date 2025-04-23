package it.swam.backend.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(NOT_FOUND)
public class NotFoundException extends BaseRestException {

    public NotFoundException(String s) {
        super(s);
    }

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NotFoundException(ErrorCode errorCode, Object... params) {
        super(errorCode, params);
    }

    public NotFoundException(String errorMessage, Exception e) {
        super(errorMessage, e);
    }

    public NotFoundException(String errorMessage, Throwable e) {
        super(errorMessage, e);
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return NOT_FOUND;
    }
}
