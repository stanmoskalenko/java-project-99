package hexlet.code.dto.taskstatus;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskStatusDto {

    private Long id;
    private String name;
    private String slug;
    private LocalDate createdAt;

}
