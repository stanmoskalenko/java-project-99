package hexlet.code.component;

import hexlet.code.dto.taskstatus.acceptor.CreateTaskStatusAcceptor;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.repository.TaskStatusRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.text.CaseUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class TaskStatusInitializer implements ApplicationRunner {

    private final TaskStatusRepository repository;
    private final TaskStatusMapper mapper;

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
        var taskStatuses2 = List.of(
                "draft",
                "to_review",
                "to_be_fixed",
                "to_publish",
                "published",
                "feature",
                "bug");
        taskStatuses2.forEach(name -> {
            var slug = CaseUtils.toCamelCase(name, true, '_');
            var acceptor = new CreateTaskStatusAcceptor(name, slug);
            var entity = mapper.toCreateEntity(acceptor);
            repository.save(entity);
        });
    }
}
