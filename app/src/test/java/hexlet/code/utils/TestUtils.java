package hexlet.code.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.config.TestConfig;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

@SpringBootTest
@Import(TestConfig.class)
public class TestUtils {

    public static String testUserEmail = "dev-user@tm.io";

    public static String testUserPassword = "secret";

    public static SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    @BeforeAll
    public static void setUp() {
        token = jwt().jwt(builder -> builder.subject(testUserEmail));
    }

    public static ObjectMapper om = new ObjectMapper();


    public static String readFixtureFile(String filePath) throws IOException {
        var pathToFile = Path.of("src", "test", "resources", "fixtures", filePath)
                .toAbsolutePath()
                .normalize();
        return Files.readString(pathToFile);
    }

}
