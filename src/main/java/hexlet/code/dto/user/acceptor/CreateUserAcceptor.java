package hexlet.code.dto.user.acceptor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreateUserAcceptor extends BaseUserAcceptor {

    @Email
    @NotBlank(message = "Required field: email is empty!")
    private String email;
    @NotNull(message = "Required field: password is empty!")
    @Size(min = MIN_LEN, message = "The password must be more than 2 characters!")
    private String password;

}
