package hexlet.code.service.taskstatus;

import hexlet.code.dto.taskstatus.TaskStatusDto;
import hexlet.code.dto.taskstatus.acceptor.CreateTaskStatusAcceptor;
import hexlet.code.dto.taskstatus.acceptor.UpdateTaskStatusAcceptor;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.exception.SqlException;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.utils.ErrorMessages;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskStatusService {

    private TaskStatusRepository repository;
    private TaskStatusMapper mapper;

    private static final String ENTITY_NAME = "Task Status";

    /**
     * Retrieves all task statuses from the repository, converts them to DTOs, and returns them as a list.
     *
     * @return a list of TaskStatusDto objects.
     */
    public List<TaskStatusDto> getList() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    /**
     * Retrieves a task status by its ID, converts it to a DTO, and returns it.
     * If the task status is not found, it throws a ResourceNotFoundException.
     *
     * @param id the ID of the task status to retrieve.
     * @return the TaskStatusDto object corresponding to the given ID.
     * @throws ResourceNotFoundException if the task status is not found.
     */
    public TaskStatusDto getById(long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        ErrorMessages.NOT_FOUND_BY_ID,
                        ENTITY_NAME,
                        id)));
    }

    /**
     * Creates a new task status based on the provided acceptor, saves it in the repository, and returns it as a DTO.
     * If the newly created task status is not found, it returns null.
     *
     * @param acceptor the acceptor containing the data to create the new task status.
     * @return the TaskStatusDto object corresponding to the newly created task status, or null if not found.
     */
    public TaskStatusDto create(CreateTaskStatusAcceptor acceptor) {
        var entity = mapper.toCreateEntity(acceptor);
        repository.save(entity);
        return repository.findById(entity.getId())
                .map(mapper::toDto)
                .orElse(null);
    }

    /**
     * Updates an existing task status identified by the provided ID with the data from the provided acceptor,
     * saves the updated task status in the repository, and returns it as a DTO.
     * If the task status to update is not found, it throws a ResourceNotFoundException.
     * If the updated task status is not found, it returns null.
     *
     * @param id the ID of the task status to update.
     * @param acceptor the acceptor containing the data to update the task status.
     * @return the TaskStatusDto object corresponding to the updated task status, or null if not found.
     * @throws ResourceNotFoundException if the task status to update is not found.
     */
    public TaskStatusDto update(long id, UpdateTaskStatusAcceptor acceptor) {
        var user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        ErrorMessages.NOT_FOUND_BY_ID,
                        ENTITY_NAME,
                        id)));
        var entity = mapper.toUpdateEntity(acceptor, user);
        repository.save(entity);
        return repository.findById(entity.getId())
                .map(mapper::toDto)
                .orElse(null);
    }

    /**
     * Deletes a task status identified by the provided ID from the repository.
     * If the deletion violates data integrity, it throws a SqlException.
     *
     * @param id the ID of the task status to delete.
     * @throws SqlException if the deletion violates data integrity.
     */
    public void delete(long id) {
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new SqlException(ErrorMessages.CONSTRAINT);
        }
    }

}
