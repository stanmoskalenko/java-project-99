package hexlet.code.project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import hexlet.code.project.dto.AuthAcceptor;
import hexlet.code.project.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class AuthControllerTest extends TestUtils {

    @Autowired
    private MockMvc mockMvc;

    private String getAuthRq(String login, String password) throws JsonProcessingException {
        var acceptor = new AuthAcceptor(login, password);

        return OM.writeValueAsString(acceptor);
    }

    @Test
    void shouldBeOk() throws Exception {
        var rq = getAuthRq(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(rq))
                .andExpect(status().isOk());
    }

    @Test
    void shouldBeUnauthorized() throws Exception {
        var rq = getAuthRq("not_exists@example.com", "secret");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(rq))
                .andExpect(status().isUnauthorized());
    }

}
