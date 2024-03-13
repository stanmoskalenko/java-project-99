package hexlet.code.project.controller;

import hexlet.code.project.component.ModelGenerator;
import hexlet.code.project.dto.taskstatus.acceptor.CreateTaskStatusAcceptor;
import hexlet.code.project.dto.taskstatus.acceptor.UpdateTaskStatusAcceptor;
import hexlet.code.project.model.TaskStatus;
import hexlet.code.project.repository.TaskStatusRepository;
import hexlet.code.project.utils.TestUtils;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class TaskStatusControllerTest extends TestUtils {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    TaskStatusRepository repository;
    @Autowired
    ModelGenerator generator;
    @Autowired
    Faker faker;

    private static final String SLUG = "/api/task_statuses";

    @Nested
    class GetListTest {

        @Test
        void getListTest() throws Exception {
            var testTaskStatus = Instancio.of(generator.getTaskStatusModel()).create();
            repository.save(testTaskStatus);

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
        void getUserTest() throws Exception {
            var testTaskStatus = Instancio.of(generator.getTaskStatusModel()).create();
            repository.save(testTaskStatus);
            var endpoint = SLUG + "/" + testTaskStatus.getId();

            var body = mockMvc.perform(MockMvcRequestBuilders.get(endpoint)
                            .with(token))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            assertThatJson(body).and(
                    v -> v.node("id").isEqualTo(testTaskStatus.getId()),
                    v -> v.node("name").isEqualTo(testTaskStatus.getName()),
                    v -> v.node("slug").isEqualTo(testTaskStatus.getSlug()),
                    v -> v.node("createdAt").isNotNull());
        }

        @Test
        void getUserWithoutTokenTest() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/task_statuses/1"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class CreateTaskStatusTest {

        private CreateTaskStatusAcceptor getAcceptor() {
            var acceptor = new CreateTaskStatusAcceptor();
            acceptor.setName(faker.internet().domainName());
            acceptor.setSlug(faker.internet().slug());

            return acceptor;
        }

        @Test
        void createTaskStatusTest() throws Exception {
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
                    v -> v.node("name").isEqualTo(acceptor.getName()),
                    v -> v.node("slug").isEqualTo(acceptor.getSlug()));
        }

        @Test
        void createTaskStatusWithoutTokenTest() throws Exception {
            var acceptor = getAcceptor();
            mockMvc.perform(MockMvcRequestBuilders.get(SLUG)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(om.writeValueAsString(acceptor)))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class UpdateTaskStatusTest {

        private TaskStatus testTaskStatus;

        @BeforeEach
        public void setUp() {
            var testData = Instancio.of(generator.getTaskStatusModel()).create();
            testTaskStatus = repository.save(testData);
        }

        private UpdateTaskStatusAcceptor getAcceptor() {
            var acceptor = new UpdateTaskStatusAcceptor();
            acceptor.setName(faker.internet().domainName());

            return acceptor;
        }

        @Test
        void updateTaskStatusTest() throws Exception {
            var acceptor = getAcceptor();

            var endpoint = SLUG + "/" + testTaskStatus.getId();
            var body = mockMvc.perform(MockMvcRequestBuilders.put(endpoint)
                            .with(token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(om.writeValueAsString(acceptor)))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            var actual = repository.findById(testTaskStatus.getId()).get();

            assertThatJson(body).and(
                    v -> v.node("createdAt").isNotNull(),
                    v -> v.node("id").isEqualTo(testTaskStatus.getId()),
                    v -> v.node("name").isEqualTo(actual.getName()),
                    v -> v.node("slug").isEqualTo(testTaskStatus.getSlug()));
        }

        @Test
        void updateTaskStatusWithoutTokenTest() throws Exception {
            var acceptor = getAcceptor();
            var endpoint = SLUG + "/" + testTaskStatus.getId();
            mockMvc.perform(MockMvcRequestBuilders.get(endpoint)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(om.writeValueAsString(acceptor)))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class DeleteTaskStatusTest {

        private TaskStatus testTaskStatus;

        @BeforeEach
        public void setUp() {
            testTaskStatus = Instancio.of(generator.getTaskStatusModel()).create();
            repository.save(testTaskStatus);
        }

        @Test
        void deleteTaskStatusTest() throws Exception {
            var endpoint = SLUG + "/" + testTaskStatus.getId();
            mockMvc.perform(MockMvcRequestBuilders.delete(endpoint)
                            .with(token))
                    .andExpect(status().isOk());

            assertTrue(repository.findById(testTaskStatus.getId()).isEmpty());
        }

        @Test
        void deleteTaskStatusWithoutTokenTest() throws Exception {
            var endpoint = SLUG + "/" + testTaskStatus.getId();
            mockMvc.perform(MockMvcRequestBuilders.delete(endpoint))
                    .andExpect(status().isUnauthorized());

            assertTrue(repository.findById(testTaskStatus.getId()).isPresent());
        }
    }

}
