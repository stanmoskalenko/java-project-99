package hexlet.code.controller.user;

import hexlet.code.component.UserUtils;
import hexlet.code.dto.user.UserDto;
import hexlet.code.dto.user.acceptor.CreateUserAcceptor;
import hexlet.code.dto.user.acceptor.UpdateUserAcceptor;
import hexlet.code.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private UserUtils userUtils;

    /**
     * Fetches a list of all users.
     *
     * @return A ResponseEntity containing a list of UserDto objects and a header indicating the total count of users.
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> getList() {
        var result = service.getList();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(result.size()))
                .body(result);
    }

    /**
     * Fetches a specific user by their ID.
     *
     * @param id The ID of the user to fetch.
     * @return A UserDto object representing the fetched user.
     */
    @GetMapping("/{id}")
    public UserDto get(@PathVariable long id) {
        return service.getById(id);
    }

    /**
     * Creates a new user.
     *
     * @param acceptor A CreateUserAcceptor object containing the data for the new user.
     * @return A UserDto object representing the created user.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody CreateUserAcceptor acceptor) {
        return service.create(acceptor);
    }

    /**
     * Updates a specific user.
     *
     * @param id The ID of the user to update.
     * @param acceptor An UpdateUserAcceptor object containing the new data for the user.
     * @return A UserDto object representing the updated user.
     */
    @PutMapping("/{id}")
    @PreAuthorize("@userUtils.isCurrentUser(#id)")
    public UserDto update(@PathVariable long id, @Valid @RequestBody UpdateUserAcceptor acceptor) {
        return service.update(id, acceptor);
    }

    /**
     * Deletes a specific user.
     *
     * @param id The ID of the user to delete.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@userUtils.isCurrentUser(#id)")
    public void delete(@PathVariable long id) {
        service.delete(id);
    }

}
