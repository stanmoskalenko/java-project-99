package hexlet.code.component;

import hexlet.code.exception.UserNotFoundException;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {

    private final UserRepository repository;
    private final String testUserEmail;

    public UserUtils(
            UserRepository repository,
            @Value("${app.user.email}") String testUserEmail
    ) {
        this.repository = repository;
        this.testUserEmail = testUserEmail;
    }

    /**
     * Retrieves the current authenticated user from the security context.
     * If no user is authenticated, this method returns null.
     * If the user is authenticated but not found in the repository, a UserNotFoundException is thrown.
     *
     * @return the authenticated User, or null if no user is authenticated
     * @throws UserNotFoundException if the authenticated user is not found in the repository
     */
    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        var email = authentication.getName();
        return repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    /**
     * Retrieves the test user from the repository using the email provided in the application properties.
     * If the test user is not found in the repository, a UserNotFoundException is thrown.
     *
     * @return the test User
     * @throws UserNotFoundException if the test user is not found in the repository
     */
    public User getTestUser() {
        return repository.findByEmail(testUserEmail)
                .orElseThrow(() -> new UserNotFoundException("Test user not found"));
    }
}
