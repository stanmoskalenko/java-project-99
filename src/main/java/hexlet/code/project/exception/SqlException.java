package hexlet.code.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SqlException extends RuntimeException {

    public SqlException(String message) {
        super(message);
    }

}
