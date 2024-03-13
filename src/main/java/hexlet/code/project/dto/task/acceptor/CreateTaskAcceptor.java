package hexlet.code.project.dto.task.acceptor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
