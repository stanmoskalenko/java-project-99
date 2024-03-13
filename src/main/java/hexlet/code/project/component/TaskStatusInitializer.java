package hexlet.code.project.component;

import hexlet.code.project.dto.taskstatus.acceptor.CreateTaskStatusAcceptor;
import hexlet.code.project.mapper.TaskStatusMapper;
import hexlet.code.project.repository.TaskStatusRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.text.CaseUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@AllArgsConstructor
public class TaskStatusInitializer implements ApplicationRunner {

    private final TaskStatusRepository repository;
    private final TaskStatusMapper mapper;
    @Value("${app.task-statuses}")
    private String[] taskStatuses;

    /**
     * This method is executed when the application starts. It initializes the task statuses in the repository.
     * It creates a list of task status names, converts each name to CamelCase to create a slug,
     * then creates a new CreateTaskStatusAcceptor with the name and slug.
     * It then maps the acceptor to an entity and saves the entity in the repository.
     *
     * @param args the arguments of the application
     * @throws Exception if any error occurs during the execution
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Arrays.stream(taskStatuses).forEach(name -> {
            var slug = CaseUtils.toCamelCase(name, true, '_');
            var acceptor = new CreateTaskStatusAcceptor(name, slug);
            var entity = mapper.toCreateEntity(acceptor);
            repository.save(entity);
        });
    }

}
