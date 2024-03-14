package hexlet.code.project.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.project.config.TestConfig;
import lombok.Getter;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

@Getter
@SpringBootTest
@Import(TestConfig.class)
public class TestUtils {

    public static final String TEST_USER_EMAIL = "hexlet@example.com";

    public static final String TEST_USER_PASSWORD = "qwerty";

    public static final ObjectMapper OM = new ObjectMapper();

    protected static SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor getToken(String email) {
        return jwt().jwt(builder -> builder.subject(email));
    }

    protected static SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor getToken() {
        return getToken(TEST_USER_EMAIL);
    }

}
