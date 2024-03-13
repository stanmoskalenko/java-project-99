package hexlet.code.dto.user.acceptor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UpdateUserAcceptor extends BaseUserAcceptor {

    @Email
    private String email;
    @Size(min = MIN_LEN, message = "The password must be more than 2 characters!")
    private String password;

}
