package hexlet.code.project.dto.task.acceptor;

import lombok.Data;

import java.io.Serializable;

@Data
public class GetListTaskAcceptor implements Serializable {

    private String titleCont;
    private String status;
    private Long assigneeId;
    private Long labelId;

}
