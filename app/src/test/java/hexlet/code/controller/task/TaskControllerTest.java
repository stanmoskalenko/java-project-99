package hexlet.code.controller.task;

import hexlet.code.component.ModelGenerator;
import hexlet.code.dto.task.acceptor.CreateTaskAcceptor;
import hexlet.code.dto.task.acceptor.UpdateTaskAcceptor;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.ErrorMessages;
import hexlet.code.utils.TestUtils;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Map;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class TaskControllerTest extends TestUtils {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    TaskRepository repository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TaskStatusRepository taskStatusRepository;
    @Autowired
    LabelRepository labelRepository;
    @Autowired
    ModelGenerator generator;
    @Autowired
    Faker faker;

    private static final String SLUG = "/api/tasks";
    private Task testTask;
    private User testUser;
    private TaskStatus testStatus;
    private Label testLabel;

    @BeforeEach
    public void prepareDb() {
        testUser = Instancio.of(generator.getUserModel()).create();
        testStatus = Instancio.of(generator.getTaskStatusModel()).create();
        testTask = Instancio.of(generator.getTaskModel()).create();
        testLabel = Instancio.of(generator.getLabelModel()).create();
        labelRepository.save(testLabel);
        taskStatusRepository.save(testStatus);
        userRepository.save(testUser);
        testTask.setAssignee(testUser);
        testTask.setStatus(testStatus);
        testTask.setLabels(List.of(testLabel));
        repository.save(testTask);
    }

    @AfterEach
    public void clear() {
        repository.delete(testTask);
        userRepository.delete(testUser);
        taskStatusRepository.delete(testStatus);
        labelRepository.delete(testLabel);
    }

    @Nested
    class GetListTest {

        @Test
        void getListWithTokenTest() throws Exception {
            var body = mockMvc.perform(MockMvcRequestBuilders.get(SLUG)
                            .with(token))
                    .andExpect(status().isOk())
                    .andExpect(header().exists("X-Total-Count"))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            assertThatJson(body).isArray();
        }

        @Test
        void getListWithoutTokenTest() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get(SLUG))
                    .andExpect(status().isUnauthorized());
        }
    }


    @Nested
    class GetByIdTest {

        @Test
        void getUserWithTokenTest() throws Exception {
            var endpoint = SLUG + "/" + testTask.getId();

            var body = mockMvc.perform(MockMvcRequestBuilders.get(endpoint)
                            .with(token))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            assertThatJson(body).and(
                    v -> v.node("id").isEqualTo(testTask.getId()),
                    v -> v.node("content").isEqualTo(testTask.getDescription()),
                    v -> v.node("title").isEqualTo(testTask.getName()),
                    v -> v.node("status").isEqualTo(testTask.getStatus().getSlug()),
                    v -> v.node("assignee_id").isEqualTo(testTask.getAssignee().getId()),
                    v -> v.node("createdAt").isNotNull());
        }

        @Test
        void getUserWithoutTokenTest() throws Exception {
            var endpoint = SLUG + "/" + testTask.getId();
            mockMvc.perform(MockMvcRequestBuilders.get(endpoint))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class CreateTaskTest {

        private CreateTaskAcceptor getAcceptor() {
            var acceptor = new CreateTaskAcceptor();
            acceptor.setTitle(faker.internet().domainName());
            acceptor.setStatus(testStatus.getSlug());
            acceptor.setAssigneeId(testUser.getId());
            acceptor.setContent(faker.elderScrolls().city());
            acceptor.setTaskLabelIds(List.of(testLabel.getId()));
            return acceptor;
        }

        @Test
        void createTaskWithTokenTest() throws Exception {
            var acceptor = getAcceptor();
            var body = mockMvc.perform(MockMvcRequestBuilders.post(SLUG)
                            .with(token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(om.writeValueAsString(acceptor)))
                    .andExpect(status().isCreated())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            assertThatJson(body).and(
                    v -> v.node("id").isNotNull(),
                    v -> v.node("createdAt").isNotNull(),
                    v -> v.node("index").isAbsent(),
                    v -> v.node("title").isEqualTo(acceptor.getTitle()),
                    v -> v.node("status").isEqualTo(acceptor.getStatus()),
                    v -> v.node("content").isEqualTo(acceptor.getContent()),
                    v -> v.node("assignee_id").isEqualTo(acceptor.getAssigneeId()));

            var task = om.readValue(body, Map.class);
            repository.deleteById(Long.parseLong(task.get("id").toString()));
        }

        @Test
        void createTaskWithoutTokenTest() throws Exception {
            var acceptor = getAcceptor();
            mockMvc.perform(MockMvcRequestBuilders.get(SLUG)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(om.writeValueAsString(acceptor)))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class UpdateTaskTest {

        private UpdateTaskAcceptor getAcceptor() {
            var acceptor = new UpdateTaskAcceptor();
            acceptor.setTitle(faker.internet().domainName());

            return acceptor;
        }

        @Test
        void updateTaskWithTokenTest() throws Exception {
            var acceptor = getAcceptor();

            var endpoint = SLUG + "/" + testTask.getId();
            var body = mockMvc.perform(MockMvcRequestBuilders.put(endpoint)
                            .with(token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(om.writeValueAsString(acceptor)))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            assertThatJson(body).and(
                    v -> v.node("id").isNotNull(),
                    v -> v.node("createdAt").isNotNull(),
                    v -> v.node("index").isEqualTo(testTask.getIndex()),
                    v -> v.node("title").isEqualTo(acceptor.getTitle()),
                    v -> v.node("status").isEqualTo(testTask.getStatus().getSlug()),
                    v -> v.node("content").isEqualTo(testTask.getDescription()),
                    v -> v.node("assignee_id").isEqualTo(testTask.getAssignee().getId()));
        }

        @Test
        void updateTaskWithoutTokenTest() throws Exception {
            var acceptor = getAcceptor();
            var endpoint = SLUG + "/" + testTask.getId();
            mockMvc.perform(MockMvcRequestBuilders.get(endpoint)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(om.writeValueAsString(acceptor)))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class DeleteTaskTest {

        @Test
        void deleteTaskWithTokenTest() throws Exception {
            var endpoint = SLUG + "/" + testTask.getId();
            mockMvc.perform(MockMvcRequestBuilders.delete(endpoint)
                            .with(token))
                    .andExpect(status().isOk());

            assertTrue(repository.findById(testTask.getId()).isEmpty());

        }

        @Test
        void deleteTaskWithoutTokenTest() throws Exception {
            var endpoint = SLUG + "/" + testTask.getId();
            mockMvc.perform(MockMvcRequestBuilders.delete(endpoint))
                    .andExpect(status().isUnauthorized());

            assertTrue(repository.findById(testTask.getId()).isPresent());
        }

        @Test
        void deleteUserTest() throws Exception {
            var endpoint = "/api/users/" + testUser.getId();
            var body = mockMvc.perform(MockMvcRequestBuilders.delete(endpoint)
                            .with(token))
                    .andExpect(status().isBadRequest())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            assertEquals(ErrorMessages.CONSTRAINT, body);
            assertTrue(userRepository.findById(testUser.getId()).isPresent());
        }

        @Test
        void deleteTaskStatusTest() throws Exception {
            var endpoint = "/api/task_statuses/" + testStatus.getId();
            var body = mockMvc.perform(MockMvcRequestBuilders.delete(endpoint)
                            .with(token))
                    .andExpect(status().isBadRequest())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            assertEquals(ErrorMessages.CONSTRAINT, body);
            assertTrue(taskStatusRepository.findById(testStatus.getId()).isPresent());
        }

        @Test
        void deleteLabelTest() throws Exception {
            var endpoint = "/api/labels/" + testLabel.getId();
            var body = mockMvc.perform(MockMvcRequestBuilders.delete(endpoint)
                            .with(token))
                    .andExpect(status().isBadRequest())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            assertEquals(ErrorMessages.CONSTRAINT, body);
            assertTrue(labelRepository.findById(testLabel.getId()).isPresent());
        }
    }

}
