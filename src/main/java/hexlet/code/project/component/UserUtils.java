package hexlet.code.project.component;

import hexlet.code.project.exception.UserNotFoundException;
import hexlet.code.project.model.User;
import hexlet.code.project.repository.UserRepository;
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
     * Checks if the user with the specified ID is the current authenticated user.
     * This method retrieves the email of the current authenticated user from the security context
     * and compares it with the email of the user identified by the given ID.
     * If the emails match, it returns true, indicating that the specified user is the current user.
     * If there is no user with the given ID, or the emails do not match, it returns false.
     *
     * @param id the ID of the user to check against the current authenticated user
     * @return true if the user with the given ID is the current authenticated user, false otherwise
     */
    public boolean isCurrentUser(Long id) {
        var currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        var userEmail = repository.findById(id).map(User::getEmail).orElse(null);
        System.out.println("currentUserEmail => " + currentUserEmail);
        System.out.println("userEmail => " + userEmail);
        return currentUserEmail.equals(userEmail);
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
