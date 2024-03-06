package hexlet.code.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {

    /**
     * Creates and configures a Jackson2ObjectMapperBuilder.
     *
     * <p>This method sets up a Jackson2ObjectMapperBuilder with specific configurations:
     * <ul>
     *   <li>Serialization inclusion set to non-null, meaning it will
     *   ignore all null fields during serialization.</li>
     *   <li>Installs a module to handle JsonNullable types.</li>
     *   <li>Registers a serializer for LocalDate objects, using a specific date format ("yyyy-MM-dd").</li>
     * </ul>
     *
     * @return a Jackson2ObjectMapperBuilder with the specified configurations.
     */

    @Bean
    Jackson2ObjectMapperBuilder objectMapperBuilder() {
        var builder = new Jackson2ObjectMapperBuilder();
        builder.serializationInclusion(JsonInclude.Include.NON_NULL)
                .modulesToInstall(new JsonNullableModule());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        builder.serializers(new LocalDateSerializer(formatter));

        return builder;
    }

}
