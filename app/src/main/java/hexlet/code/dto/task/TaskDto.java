package hexlet.code.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class TaskDto {

    private Long id;
    private String title;
    private String status;
    private String content;
    private Long index;
    @JsonProperty("assignee_id")
    private Long assigneeId;
    private LocalDate createdAt;
    private List<Long> taskLabelIds;

}
