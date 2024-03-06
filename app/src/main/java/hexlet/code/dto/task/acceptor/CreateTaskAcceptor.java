package hexlet.code.dto.task.acceptor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateTaskAcceptor extends BaseTaskAcceptor {

    @NotBlank
    @Size(min = MIN_LEN, message = "Name must be at least 1 character long!")
    private String title;
    @NotBlank
    private String status;
    @NotEmpty
    private List<Long> taskLabelIds;

}
