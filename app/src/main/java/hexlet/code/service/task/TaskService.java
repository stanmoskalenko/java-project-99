package hexlet.code.service.task;


import hexlet.code.dto.task.TaskDto;
import hexlet.code.dto.task.acceptor.CreateTaskAcceptor;
import hexlet.code.dto.task.acceptor.UpdateTaskAcceptor;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.repository.TaskRepository;
import hexlet.code.utils.ErrorMessages;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskService {

    private TaskRepository repository;
    private TaskMapper mapper;

    private static final String ENTITY_NAME = "Task";

    /**
     * Retrieves all tasks from the repository, converts them to DTOs, and returns them as a list.
     *
     * @return a list of TaskDto objects representing all tasks in the repository.
     */
    public List<TaskDto> getList() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    /**
     * Retrieves a task by its ID, converts it to a DTO, and returns it.
     * Throws a ResourceNotFoundException if no task with the given ID is found.
     *
     * @param id the ID of the task to retrieve.
     * @return a TaskDto object representing the task with the given ID.
     * @throws ResourceNotFoundException if no task with the given ID is found.
     */
    public TaskDto getById(long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        ErrorMessages.NOT_FOUND_BY_ID,
                        ENTITY_NAME,
                        id)));
    }

    /**
     * Creates a new task based on the provided CreateTaskAcceptor object, saves it in the repository,
     * and returns it as a DTO. If the task cannot be found after saving (which should not happen),
     * returns null.
     *
     * @param acceptor a CreateTaskAcceptor object containing the data for the new task.
     * @return a TaskDto object representing the newly created task, or null if the task cannot be found after saving.
     */
    public TaskDto create(CreateTaskAcceptor acceptor) {
        var entity = mapper.toCreateEntity(acceptor);
        repository.save(entity);

        return repository.findById(entity.getId())
                .map(mapper::toDto)
                .orElse(null);
    }

    /**
     * Updates an existing task with the given ID based on the provided UpdateTaskAcceptor object,
     * saves the updated task in the repository, and returns it as a DTO.
     * Throws a ResourceNotFoundException if no task with the given ID is found.
     * If the task cannot be found after saving (which should not happen), returns null.
     *
     * @param id       the ID of the task to update.
     * @param acceptor an UpdateTaskAcceptor object containing the new data for the task.
     * @return a TaskDto object representing the updated task, or null if the task cannot be found after saving.
     * @throws ResourceNotFoundException if no task with the given ID is found.
     */
    public TaskDto update(long id, UpdateTaskAcceptor acceptor) {
        var task = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        ErrorMessages.NOT_FOUND_BY_ID,
                        ENTITY_NAME,
                        id)));
        var entity = mapper.toUpdateEntity(acceptor, task);
        repository.save(entity);

        return repository.findById(entity.getId())
                .map(mapper::toDto)
                .orElse(null);
    }

    /**
     * Deletes the task with the given ID from the repository.
     *
     * @param id the ID of the task to delete.
     */
    public void delete(long id) {
        repository.deleteById(id);
    }

}
