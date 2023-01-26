package MainPackageForTaskManager.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class HandlerException {
    //will handle my not found exception
    @ExceptionHandler(value = {ExceptionNotFound.class})
    public ResponseEntity<Object> handleExceptionNotFound(ExceptionNotFound notfound) {
        ErrorResponse errorResponse = new ErrorResponse(notfound.getMessage(),
                notfound.getCause(),
                HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleBadRequests(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(),
                exception.getCause(),
                HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(value = {NotAllowedException.class})
    public ResponseEntity<Object> handleUnauthorized(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(),
                exception.getCause(),
                HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        //http status created 201
        //response entity
        //control http status
    }

}