package hexlet.code.project.dto.label.acceptor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LabelAcceptor implements Serializable {

    private static final int MIN_LEN = 1;
    private static final int MAX_LEN = 1000;

    @Size(
            min = MIN_LEN,
            max = MAX_LEN,
            message = "The name must be at least 1 character long and no more than 1000 characters long!"
    )
    @NotNull
    private String name;
}
