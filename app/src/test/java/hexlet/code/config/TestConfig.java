package hexlet.code.config;

import net.datafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    /**
     * This method is used to create and return a new instance of the Faker class.
     * Faker is a library that generates fake data for testing or other purposes.
     *
     * @return Faker - a new instance of the Faker class
     */
    @Bean
    Faker getFaker() {
        return new Faker();
    }

}
