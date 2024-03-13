package hexlet.code.project.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
