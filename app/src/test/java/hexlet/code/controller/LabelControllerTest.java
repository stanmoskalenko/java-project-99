package hexlet.code.controller;

import hexlet.code.component.ModelGenerator;
import hexlet.code.dto.label.acceptor.LabelAcceptor;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.utils.TestUtils;
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
class LabelControllerTest extends TestUtils {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    LabelRepository repository;
    @Autowired
    ModelGenerator generator;
    @Autowired
    Faker faker;

    private static final String SLUG = "/api/labels";

    @Nested
    class GetListTest {

        @Test
        void getListTest() throws Exception {
            var testLabel = Instancio.of(generator.getLabelModel()).create();
            repository.save(testLabel);

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
            var testLabel = Instancio.of(generator.getLabelModel()).create();
            repository.save(testLabel);
            var endpoint = SLUG + "/" + testLabel.getId();

            var body = mockMvc.perform(MockMvcRequestBuilders.get(endpoint)
                            .with(token))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            assertThatJson(body).and(
                    v -> v.node("id").isEqualTo(testLabel.getId()),
                    v -> v.node("name").isEqualTo(testLabel.getName()),
                    v -> v.node("createdAt").isNotNull());
        }

        @Test
        void getUserWithoutTokenTest() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/labels/1"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void getNonExistentUserWithoutTokenTest() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/task_statuses/9999")
                            .with(token))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class CreateLabelTest {

        private LabelAcceptor getAcceptor() {
            var acceptor = new LabelAcceptor();
            acceptor.setName(faker.internet().domainName());

            return acceptor;
        }

        @Test
        void createLabelTest() throws Exception {
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
                    v -> v.node("name").isEqualTo(acceptor.getName()));
        }

        @Test
        void createLabelWithoutTokenTest() throws Exception {
            var acceptor = getAcceptor();
            mockMvc.perform(MockMvcRequestBuilders.get(SLUG)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(om.writeValueAsString(acceptor)))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class UpdateLabelTest {

        private Label testLabel;

        @BeforeEach
        public void setUp() {
            var testData = Instancio.of(generator.getLabelModel()).create();
            testLabel = repository.save(testData);
        }

        private LabelAcceptor getAcceptor() {
            var acceptor = new LabelAcceptor();
            acceptor.setName(faker.internet().domainName());

            return acceptor;
        }

        @Test
        void updateLabelTest() throws Exception {
            var acceptor = getAcceptor();

            var endpoint = SLUG + "/" + testLabel.getId();
            var body = mockMvc.perform(MockMvcRequestBuilders.put(endpoint)
                            .with(token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(om.writeValueAsString(acceptor)))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            var actual = repository.findById(testLabel.getId()).get();

            assertThatJson(body).and(
                    v -> v.node("createdAt").isNotNull(),
                    v -> v.node("id").isEqualTo(testLabel.getId()),
                    v -> v.node("name").isEqualTo(actual.getName()));
        }

        @Test
        void updateLabelWithoutTokenTest() throws Exception {
            var acceptor = getAcceptor();
            var endpoint = SLUG + "/" + testLabel.getId();
            mockMvc.perform(MockMvcRequestBuilders.get(endpoint)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(om.writeValueAsString(acceptor)))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class DeleteLabelTest {

        private Label testLabel;

        @BeforeEach
        public void setUp() {
            testLabel = Instancio.of(generator.getLabelModel()).create();
            repository.save(testLabel);
        }

        @Test
        void deleteLabelTest() throws Exception {
            var endpoint = SLUG + "/" + testLabel.getId();
            mockMvc.perform(MockMvcRequestBuilders.delete(endpoint)
                            .with(token))
                    .andExpect(status().isOk());

            assertTrue(repository.findById(testLabel.getId()).isEmpty());
        }

        @Test
        void deleteLabelWithoutTokenTest() throws Exception {
            var endpoint = SLUG + "/" + testLabel.getId();
            mockMvc.perform(MockMvcRequestBuilders.delete(endpoint))
                    .andExpect(status().isUnauthorized());

            assertTrue(repository.findById(testLabel.getId()).isPresent());
        }
    }

}
