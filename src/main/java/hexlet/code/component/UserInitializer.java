package hexlet.code.component;

import hexlet.code.dto.user.acceptor.CreateUserAcceptor;
import hexlet.code.mapper.UserMapper;
import hexlet.code.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserInitializer implements ApplicationRunner {

    private final UserRepository repository;
    private final UserMapper mapper;
    @Value("${app.user.email}")
    private String defaultUserEmail;
    @Value("${app.user.password}")
    private String defaultUserPassword;

    /**
     * This method is invoked at the startup of the application.
     * It creates a new user with default email and password,
     * maps it to a User entity, and saves it to the repository if the user is not null.
     *
     * @param args the arguments of the application
     */
    @Override
    public void run(ApplicationArguments args) {
        var acceptor = new CreateUserAcceptor();
        acceptor.setEmail(defaultUserEmail);
        acceptor.setPassword(defaultUserPassword);

        var user = mapper.toCreateEntity(acceptor);
        if (user != null) {
            repository.save(user);
        }
    }
}
