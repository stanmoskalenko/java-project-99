package hexlet.code.project.controller;

import hexlet.code.project.dto.task.TaskDto;
import hexlet.code.project.dto.task.acceptor.CreateTaskAcceptor;
import hexlet.code.project.dto.task.acceptor.GetListTaskAcceptor;
import hexlet.code.project.dto.task.acceptor.UpdateTaskAcceptor;
import hexlet.code.project.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;

    /**
     * Retrieves a list of tasks based on the specified criteria.
     * <p>
     * This method accepts a {@link GetListTaskAcceptor} object that contains various
     * filtering criteria such as title content, assignee ID, status, and label ID. It then uses
     * these criteria to fetch a list of tasks from the {@link TaskService}.
     * The resulting list of {@link TaskDto} objects is returned along with a custom header `X-Total-Count`
     * indicating the total number of tasks found.
     * </p>
     *
     * @param acceptor An instance of {@link GetListTaskAcceptor} containing the search criteria for tasks.
     * @return A {@link ResponseEntity} containing the list of {@link TaskDto} objects that match the given
     * criteria and the total count of these tasks in the `X-Total-Count` header.
     */
    @GetMapping
    public ResponseEntity<List<TaskDto>> getList(@ModelAttribute GetListTaskAcceptor acceptor) {
        var result = service.getList(acceptor);
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(result.size()))
                .body(result);
    }

    /**
     * Retrieves a specific task by its ID.
     *
     * @param id the ID of the task to retrieve.
     * @return the TaskDto object corresponding to the provided ID.
     */
    @GetMapping("/{id}")
    public TaskDto get(@PathVariable Long id) {
        return service.getById(id);
    }

    /**
     * Creates a new task.
     *
     * @param acceptor a CreateTaskAcceptor object containing the details of the task to be created.
     * @return the created TaskDto object.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto create(@Valid @RequestBody CreateTaskAcceptor acceptor) {
        return service.create(acceptor);
    }

    /**
     * Updates an existing task.
     *
     * @param id       the ID of the task to update.
     * @param acceptor an UpdateTaskAcceptor object containing the new details of the task.
     * @return the updated TaskDto object.
     */
    @PutMapping("/{id}")
    public TaskDto update(@Valid @PathVariable Long id, @RequestBody UpdateTaskAcceptor acceptor) {
        return service.update(id, acceptor);
    }

    /**
     * Deletes a specific task by its ID.
     *
     * @param id the ID of the task to delete.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}
