package hexlet.code.project.dto.user.acceptor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseUserAcceptor implements Serializable {

    protected static final int MIN_LEN = 3;

    private String firstName;
    private String lastName;

    public abstract String getPassword();
    public abstract void setPassword(String password);

}
