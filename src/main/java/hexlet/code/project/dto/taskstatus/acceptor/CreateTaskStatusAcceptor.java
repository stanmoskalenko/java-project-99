package hexlet.code.project.dto.taskstatus.acceptor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTaskStatusAcceptor implements Serializable {

    private static final int MIN_LEN = 1;

    @NotBlank
    @Size(min = MIN_LEN, message = "Name must be at least 1 character long!")
    private String name;
    @NotBlank
    @Size(min = MIN_LEN, message = "Slug must be at least 1 character long!")
    private String slug;

}
