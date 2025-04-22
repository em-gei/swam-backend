package it.swam.backend.exception;

import java.net.URI;
import lombok.Getter;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

@Getter
public abstract class BaseRestException extends RuntimeException implements ErrorResponse {

    private final ProblemDetail body = ProblemDetail.forStatus(this.getStatusCode());

    protected BaseRestException(String s) {
        super(s);
    }

    protected BaseRestException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        body.setType(URI.create(errorCode.getType()));
    }

    protected BaseRestException(ErrorCode errorCode, Object... params) {
        super(String.format(errorCode.getMessage(), params));
        body.setType(URI.create(errorCode.getType()));
    }

    protected BaseRestException(String errorMessage, Exception e) {
        super(errorMessage, e);
    }

    protected BaseRestException(String errorMessage, Throwable e) {
        super(errorMessage, e);
    }

}
