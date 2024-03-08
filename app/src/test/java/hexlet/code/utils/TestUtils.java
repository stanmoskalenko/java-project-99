package hexlet.code.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.config.TestConfig;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

@SpringBootTest
@Import(TestConfig.class)
public class TestUtils {

    public static String testUserEmail = "hexlet@example.com";

    public static String testUserPassword = "qwerty";

    public static SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    public static SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor genJwt(String email) {
        return jwt().jwt(builder -> builder.subject(email));
    }

    @BeforeAll
    public static void setUp() {
        token = jwt().jwt(builder -> builder.subject(testUserEmail));
    }


    public static ObjectMapper om = new ObjectMapper();

}
