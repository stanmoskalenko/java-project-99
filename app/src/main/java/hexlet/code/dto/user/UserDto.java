package hexlet.code.dto.user;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate createdAt;

}
