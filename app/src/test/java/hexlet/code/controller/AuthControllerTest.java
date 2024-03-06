package hexlet.code.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import hexlet.code.dto.AuthAcceptor;
import hexlet.code.utils.TestUtils;
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

        return om.writeValueAsString(acceptor);
    }

    @Test
    void shouldBeOk() throws Exception {
        var rq = getAuthRq(testUserEmail, testUserPassword);
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
