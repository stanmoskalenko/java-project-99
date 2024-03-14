package hexlet.code.project.component;

import hexlet.code.project.dto.label.acceptor.LabelAcceptor;
import hexlet.code.project.mapper.LabelMapper;
import hexlet.code.project.repository.LabelRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@AllArgsConstructor
public class LabelsInitializer implements ApplicationRunner {

    private final LabelRepository repository;
    private final LabelMapper mapper;
    @Value("${app.labels:feature,bug}")
    private String[] labels;

    /**
     * Initializes labels in the application upon startup. This method reads predefined label names from the application
     * configuration, creates a {@link LabelAcceptor} for each label, maps these acceptors to label entities using
     * {@link LabelMapper}, and finally persists these entities into the database using {@link LabelRepository}.
     * <p>
     * This process ensures that the application starts with a set of predefined labels available for use.
     *
     * @param args the application arguments. This parameter is not directly used in the method but is required by the
     *             {@link ApplicationRunner} interface.
     * @throws Exception if an error occurs during the label initialization process. This could be due to issues such
     *                   as database connectivity problems or data mapping errors.
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        var entities = Arrays.stream(labels)
                .map(LabelAcceptor::new)
                .map(mapper::toCreateEntity)
                .toList();
        repository.saveAll(entities);
    }

}
