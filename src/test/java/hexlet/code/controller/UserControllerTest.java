package hexlet.code.controller;

import hexlet.code.component.ModelGenerator;
import hexlet.code.dto.user.acceptor.CreateUserAcceptor;
import hexlet.code.dto.user.acceptor.UpdateUserAcceptor;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
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
class UserControllerTest extends TestUtils {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository repository;
    @Autowired
    ModelGenerator generator;
    @Autowired
    Faker faker;

    private static final String SLUG = "/api/users";

    @Nested
    class GetListTest {

        @Test
        void getListTest() throws Exception {
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
            var testUser = Instancio.of(generator.getUserModel()).create();
            repository.save(testUser);
            var endpoint = SLUG + "/" + testUser.getId();

            var body = mockMvc.perform(MockMvcRequestBuilders.get(endpoint)
                            .with(token))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            assertThatJson(body).and(
                    v -> v.node("id").isEqualTo(testUser.getId()),
                    v -> v.node("firstName").isEqualTo(testUser.getFirstName()),
                    v -> v.node("lastName").isEqualTo(testUser.getLastName()),
                    v -> v.node("email").isEqualTo(testUser.getEmail()));
        }

        @Test
        void getUserWithoutTokenTest() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/users/2"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class CreateUserTest {

        private CreateUserAcceptor getAcceptor() {
            var acceptor = new CreateUserAcceptor();
            acceptor.setEmail(faker.internet().emailAddress());
            acceptor.setPassword(faker.internet().password());
            acceptor.setFirstName(faker.name().firstName());
            acceptor.setLastName(faker.name().lastName());

            return acceptor;
        }

        @Test
        void createUserTest() throws Exception {
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
                    v -> v.node("password").isAbsent(),
                    v -> v.node("firstName").isEqualTo(acceptor.getFirstName()),
                    v -> v.node("lastName").isEqualTo(acceptor.getLastName()),
                    v -> v.node("email").isEqualTo(acceptor.getEmail()));
        }

        @Test
        void createUserWithoutTokenTest() throws Exception {
            var acceptor = getAcceptor();
            mockMvc.perform(MockMvcRequestBuilders.get(SLUG)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(om.writeValueAsString(acceptor)))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class UpdateUserTest {

        private User getUser() {
            return repository.findByEmail(testUserEmail).get();
        }

        private UpdateUserAcceptor getAcceptor() {
            var acceptor = new UpdateUserAcceptor();
            acceptor.setFirstName(faker.name().firstName());
            acceptor.setLastName(faker.name().lastName());

            return acceptor;
        }

        @Test
        void updateUserTest() throws Exception {
            var acceptor = getAcceptor();
            var userId = getUser().getId();
            var endpoint = SLUG + "/" + userId;
            var body = mockMvc.perform(MockMvcRequestBuilders.put(endpoint)
                            .with(token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(om.writeValueAsString(acceptor)))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            var actual = repository.findById(userId).get();
            System.out.println("bodu => " + body);
            assertThatJson(body).and(
                    v -> v.node("id").isNotNull(),
                    v -> v.node("createdAt").isNotNull(),
                    v -> v.node("password").isAbsent(),
                    v -> v.node("firstName").isEqualTo(actual.getFirstName()),
                    v -> v.node("lastName").isEqualTo(actual.getLastName()),
                    v -> v.node("email").isEqualTo(actual.getEmail()));
        }

        @Test
        void updateUserWithoutTokenTest() throws Exception {
            var userId = getUser().getId();
            var acceptor = getAcceptor();

            var endpoint = SLUG + "/" + userId;
            mockMvc.perform(MockMvcRequestBuilders.put(endpoint)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(om.writeValueAsString(acceptor)))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void updateAnotherUserTest() throws Exception {
            var anotherUser = Instancio.of(generator.getUserModel()).create();
            var acceptor = new UpdateUserAcceptor();
            acceptor.setFirstName(faker.name().firstName());

            var endpoint = SLUG + "/" + anotherUser.getId();
            mockMvc.perform(MockMvcRequestBuilders.put(endpoint)
                            .with(token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(om.writeValueAsString(acceptor)))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    class DeleteUserTest {

        private User testUser;

        @BeforeEach
        public void setUp() {
            testUser = Instancio.of(generator.getUserModel()).create();
            repository.save(testUser);
        }

        @Test
        void deleteUserTest() throws Exception {
            var endpoint = SLUG + "/" + testUser.getId();
            var jwt = genJwt(testUser.getEmail());

            mockMvc.perform(MockMvcRequestBuilders.delete(endpoint)
                            .with(jwt))
                    .andExpect(status().isNoContent());

            assertTrue(repository.findById(testUser.getId()).isEmpty());
        }

        @Test
        void deleteUserWithoutTokenTest() throws Exception {
            var endpoint = SLUG + "/" + testUser.getId();
            mockMvc.perform(MockMvcRequestBuilders.delete(endpoint))
                    .andExpect(status().isUnauthorized());

            assertTrue(repository.findById(testUser.getId()).isPresent());
        }

        @Test
        void deleteAnotherUserTest() throws Exception {
            var anotherUser = Instancio.of(generator.getUserModel()).create();
            var endpoint = SLUG + "/" + anotherUser.getId();
            mockMvc.perform(MockMvcRequestBuilders.delete(endpoint)
                            .with(token))
                    .andExpect(status().isForbidden());
        }
    }

}
