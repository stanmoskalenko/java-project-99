package hexlet.code.dto.taskstatus.acceptor;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
public class UpdateTaskStatusAcceptor implements Serializable {

    private static final int MIN_LEN = 1;

    @Size(min = MIN_LEN, message = "Name must be at least 1 character long!")
    private String name;
    @Size(min = MIN_LEN, message = "Slug must be at least 1 character long!")
    private String slug;

}
