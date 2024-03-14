package hexlet.code.project.controller;

import hexlet.code.project.component.ModelGenerator;
import hexlet.code.project.dto.task.acceptor.CreateTaskAcceptor;
import hexlet.code.project.dto.task.acceptor.UpdateTaskAcceptor;
import hexlet.code.project.model.Label;
import hexlet.code.project.model.Task;
import hexlet.code.project.model.TaskStatus;
import hexlet.code.project.model.User;
import hexlet.code.project.repository.LabelRepository;
import hexlet.code.project.repository.TaskRepository;
import hexlet.code.project.repository.TaskStatusRepository;
import hexlet.code.project.repository.UserRepository;
import hexlet.code.project.utils.ErrorMessages;
import hexlet.code.project.utils.TestUtils;
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
    private MockMvc mockMvc;
    @Autowired
    private TaskRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private ModelGenerator generator;
    @Autowired
    private Faker faker;

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
        void getListTest() throws Exception {
            var body = mockMvc.perform(MockMvcRequestBuilders.get(SLUG)
                            .with(getToken()))
                    .andExpect(status().isOk())
                    .andExpect(header().exists("X-Total-Count"))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            assertThatJson(body).isArray();
        }

        @Test
        void getListWithFilterTest() throws Exception {
            var labelId = testTask.getLabels().get(0).getId();
            var endpoint = SLUG
                    + "?titleCont=" + testTask.getName().toUpperCase()
                    + "&assigneeId=" + testTask.getAssignee().getId()
                    + "&status=" + testTask.getStatus().getName()
                    + "&labelId=" + labelId;

            var body = mockMvc.perform(MockMvcRequestBuilders.get(endpoint)
                            .with(getToken()))
                    .andExpect(status().isOk())
                    .andExpect(header().exists("X-Total-Count"))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            assertThatJson(body)
                    .isArray().first().and(
                            t -> t.node("id").isEqualTo(testTask.getId()),
                            t -> t.node("status").isEqualTo(testTask.getStatus().getSlug()),
                            t -> t.node("content").isEqualTo(testTask.getDescription()),
                            t -> t.node("index").isEqualTo(testTask.getIndex()),
                            t -> t.node("taskLabelIds").isArray().singleElement().isEqualTo(labelId),
                            t -> t.node("assignee_id").isEqualTo(testTask.getAssignee().getId()));

            var noMatchEndpoint = SLUG + "?titleCont=create&assigneeId=99999&status=to_be_fixed&labelId=9999";
            var emptyBody = mockMvc.perform(MockMvcRequestBuilders.get(noMatchEndpoint)
                            .with(getToken()))
                    .andExpect(status().isOk())
                    .andExpect(header().exists("X-Total-Count"))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            assertThatJson(emptyBody).isArray().isEmpty();
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
        void getUserTest() throws Exception {
            var endpoint = SLUG + "/" + testTask.getId();

            var body = mockMvc.perform(MockMvcRequestBuilders.get(endpoint)
                            .with(getToken()))
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
        void createTaskTest() throws Exception {
            var acceptor = getAcceptor();
            var body = mockMvc.perform(MockMvcRequestBuilders.post(SLUG)
                            .with(getToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(OM.writeValueAsString(acceptor)))
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

            var task = OM.readValue(body, Map.class);
            repository.deleteById(Long.parseLong(task.get("id").toString()));
        }

        @Test
        void createTaskWithoutTokenTest() throws Exception {
            var acceptor = getAcceptor();
            mockMvc.perform(MockMvcRequestBuilders.get(SLUG)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(OM.writeValueAsString(acceptor)))
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
        void updateTaskTest() throws Exception {
            var acceptor = getAcceptor();

            var endpoint = SLUG + "/" + testTask.getId();
            var body = mockMvc.perform(MockMvcRequestBuilders.put(endpoint)
                            .with(getToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(OM.writeValueAsString(acceptor)))
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
                            .content(OM.writeValueAsString(acceptor)))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class DeleteTaskTest {

        @Test
        void deleteTaskTest() throws Exception {
            var endpoint = SLUG + "/" + testTask.getId();
            mockMvc.perform(MockMvcRequestBuilders.delete(endpoint)
                            .with(getToken()))
                    .andExpect(status().isNoContent());

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
        void deleteTaskStatusTest() throws Exception {
            var endpoint = "/api/task_statuses/" + testStatus.getId();
            var body = mockMvc.perform(MockMvcRequestBuilders.delete(endpoint)
                            .with(getToken()))
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
                            .with(getToken()))
                    .andExpect(status().isBadRequest())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            assertEquals(ErrorMessages.CONSTRAINT, body);
            assertTrue(labelRepository.findById(testLabel.getId()).isPresent());
        }
    }

}
