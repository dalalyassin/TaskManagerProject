package MainPackageForTaskManager.Exception;

import org.springframework.http.HttpStatus;

public class ErrorResponse {
    private final String message;
    private final Throwable throwable;
    private final HttpStatus httpstatus;

    public ErrorResponse(String message, Throwable throwable, HttpStatus httpstatus) {
        this.message = message;
        this.throwable = throwable;
        this.httpstatus = httpstatus;
    }

    public String getMessage() {

        return message;
    }

    public Throwable getThrowable() {

        return throwable;
    }

    public HttpStatus getHttpstatus() {

        return httpstatus;
    }
}
