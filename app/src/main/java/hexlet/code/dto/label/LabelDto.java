package hexlet.code.dto.label;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LabelDto {

    private Long id;
    private String name;
    private LocalDate createdAt;

}
