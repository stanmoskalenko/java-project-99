package hexlet.code.dto.task.acceptor;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseTaskAcceptor implements Serializable {

    protected static final int MIN_LEN = 1;

    private Long index;
    private String content;
    @JsonProperty("assignee_id")
    private Long assigneeId;

}
