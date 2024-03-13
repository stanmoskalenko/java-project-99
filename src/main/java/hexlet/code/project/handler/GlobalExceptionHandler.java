package hexlet.code.project.handler;

import hexlet.code.project.exception.ResourceNotFoundException;
import hexlet.code.project.exception.SqlException;
import hexlet.code.project.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles the ResourceNotFoundException.
     *
     * @param exception the ResourceNotFoundException instance.
     * @return a ResponseEntity with the HTTP status code NOT_FOUND and the exception message as the body.
     */
    @ExceptionHandler
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    /**
     * Handles the UserNotFoundException.
     *
     * @param exception the UserNotFoundException instance.
     * @return a ResponseEntity with the HTTP status code NOT_FOUND and the exception message as the body.
     */
    @ExceptionHandler
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    /**
     * Handles the SqlException.
     *
     * @param exception the SqlException instance.
     * @return a ResponseEntity with the HTTP status code BAD_REQUEST and the exception message as the body.
     */
    @ExceptionHandler
    public ResponseEntity<String> handleSqlException(SqlException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}
