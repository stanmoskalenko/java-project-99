package hexlet.code.project.dto.task.acceptor;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UpdateTaskAcceptor extends BaseTaskAcceptor {

    @Size(min = MIN_LEN, message = "Name must be at least 1 character long!")
    private String title;
    private String status;
    private JsonNullable<List<Long>> taskLabelIds;

}
