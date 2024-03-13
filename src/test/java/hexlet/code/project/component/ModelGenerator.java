package hexlet.code.project.component;

import hexlet.code.project.model.Label;
import hexlet.code.project.model.Task;
import hexlet.code.project.model.TaskStatus;
import hexlet.code.project.model.User;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ModelGenerator {

    @Autowired
    private Faker faker;

    private Model<User> userModel;
    private Model<TaskStatus> taskStatusModel;
    private Model<Task> taskModel;
    private Model<Label> labelModel;

    @PostConstruct
    private void init() {
        userModel = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .supply(Select.field(User::getFirstName), () -> faker.name().lastName())
                .supply(Select.field(User::getFirstName), () -> faker.name().firstName())
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .toModel();

        taskStatusModel = Instancio.of(TaskStatus.class)
                .ignore(Select.field(TaskStatus::getId))
                .supply(Select.field(TaskStatus::getName), () -> faker.eldenRing().npc())
                .supply(Select.field(TaskStatus::getSlug), () -> faker.internet().slug())
                .toModel();

        taskModel = Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .ignore(Select.field(Task::getStatus))
                .ignore(Select.field(Task::getAssignee))
                .ignore(Select.field(Task::getLabels))
                .supply(Select.field(Task::getName), () -> faker.eldenRing().npc())
                .supply(Select.field(Task::getIndex), () -> faker.number().randomNumber())
                .supply(Select.field(Task::getDescription), () -> faker.eldenRing().spell())
                .toModel();

        labelModel = Instancio.of(Label.class)
                .ignore(Select.field(Label::getId))
                .supply(Select.field(Label::getName), () -> faker.company().name())
                .toModel();

    }

}
