package hexlet.code.dto.taskstatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatusDto {

    private Long id;
    private String name;
    private String slug;
    private LocalDate createdAt;

}
