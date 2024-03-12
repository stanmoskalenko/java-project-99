package hexlet.code.service;

import hexlet.code.dto.user.UserDto;
import hexlet.code.dto.user.acceptor.CreateUserAcceptor;
import hexlet.code.dto.user.acceptor.UpdateUserAcceptor;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.exception.SqlException;
import hexlet.code.mapper.UserMapper;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.ErrorMessages;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private UserRepository repository;
    private UserMapper mapper;
    private static final String ENTITY_NAME = "User";

    /**
     * Loads a user by the given username.
     *
     * @param username the username of the user to load
     * @return the UserDetails of the loaded user
     * @throws UsernameNotFoundException if no user is found with the given username
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Retrieves a list of all users.
     *
     * @return a list of UserDto objects representing all users
     */
    public List<UserDto> getList() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    /**
     * Retrieves a user by the given ID.
     *
     * @param id the ID of the user to retrieve
     * @return a UserDto object representing the user with the given ID
     * @throws ResourceNotFoundException if no user is found with the given ID
     */
    public UserDto getById(long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        ErrorMessages.NOT_FOUND_BY_ID,
                        ENTITY_NAME,
                        id)));
    }

    /**
     * Creates a new user with the details from the given CreateUserAcceptor.
     *
     * @param acceptor a CreateUserAcceptor object containing the details of the user to create
     * @return a UserDto object representing the newly created user
     */
    public UserDto create(CreateUserAcceptor acceptor) {
        var entity = mapper.toCreateEntity(acceptor);
        repository.save(entity);
        return repository.findById(entity.getId())
                .map(mapper::toDto)
                .orElse(null);
    }

    /**
     * Updates the user with the given ID using the details from the given UpdateUserAcceptor.
     *
     * @param id the ID of the user to update
     * @param acceptor an UpdateUserAcceptor object containing the new details of the user
     * @return a UserDto object representing the updated user
     * @throws ResourceNotFoundException if no user is found with the given ID
     */
    public UserDto update(long id, UpdateUserAcceptor acceptor) {
        var user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        ErrorMessages.NOT_FOUND_BY_ID,
                        ENTITY_NAME,
                        id)));
        var entity = mapper.toUpdateEntity(acceptor, user);
        entity.setUpdatedAt(LocalDate.now());
        repository.save(entity);
        return repository.findById(entity.getId())
                .map(mapper::toDto)
                .orElse(null);
    }

    /**
     * Deletes the user with the given ID.
     *
     * @param id the ID of the user to delete
     * @throws SqlException if there is a SQL constraint violation when attempting to delete the user
     */
    public void delete(long id) {
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new SqlException(ErrorMessages.CONSTRAINT);
        }
    }

}
