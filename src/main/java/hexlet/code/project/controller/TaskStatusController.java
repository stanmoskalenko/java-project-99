package hexlet.code.project.controller;

import hexlet.code.project.dto.taskstatus.TaskStatusDto;
import hexlet.code.project.dto.taskstatus.acceptor.CreateTaskStatusAcceptor;
import hexlet.code.project.dto.taskstatus.acceptor.UpdateTaskStatusAcceptor;
import hexlet.code.project.service.TaskStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/task_statuses")
@RequiredArgsConstructor
public class TaskStatusController {

    private final TaskStatusService service;

    /**
     * This method is used to get a list of all TaskStatusDto objects.
     * It uses the service to fetch the list and then returns it as a ResponseEntity.
     * It also sets a custom header "X-Total-Count" with the size of the list.
     *
     * @return ResponseEntity containing a list of TaskStatusDto objects and a custom header.
     */
    @GetMapping
    public ResponseEntity<List<TaskStatusDto>> getList() {
        var result = service.getList();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(result.size()))
                .body(result);
    }

    /**
     * This method is used to get a specific TaskStatusDto object by its id.
     * It uses the service to fetch the TaskStatusDto object.
     *
     * @param id The id of the TaskStatusDto object to fetch.
     * @return The TaskStatusDto object with the given id.
     */
    @GetMapping("/{id}")
    public TaskStatusDto get(@PathVariable long id) {
        return service.getById(id);
    }

    /**
     * This method is used to create a new TaskStatusDto object.
     * It takes a CreateTaskStatusAcceptor object as input and uses the service to create the TaskStatusDto object.
     *
     * @param acceptor The CreateTaskStatusAcceptor object containing the data to create the TaskStatusDto object.
     * @return The newly created TaskStatusDto object.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskStatusDto create(@Valid @RequestBody CreateTaskStatusAcceptor acceptor) {
        return service.create(acceptor);
    }

    /**
     * This method is used to update a specific TaskStatusDto object.
     * It takes an id and an UpdateTaskStatusAcceptor object as input
     * and uses the service to update the TaskStatusDto object.
     *
     * @param id       The id of the TaskStatusDto object to update.
     * @param acceptor The UpdateTaskStatusAcceptor object containing the data to update the TaskStatusDto object.
     * @return The updated TaskStatusDto object.
     */
    @PutMapping("/{id}")
    public TaskStatusDto update(@PathVariable long id, @Valid @RequestBody UpdateTaskStatusAcceptor acceptor) {
        return service.update(id, acceptor);
    }

    /**
     * This method is used to delete a specific TaskStatusDto object by its id.
     * It uses the service to delete the TaskStatusDto object.
     *
     * @param id The id of the TaskStatusDto object to delete.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        service.delete(id);
    }

}
