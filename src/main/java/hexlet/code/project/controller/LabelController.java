package hexlet.code.project.controller;

import hexlet.code.project.dto.label.LabelDto;
import hexlet.code.project.dto.label.acceptor.LabelAcceptor;
import hexlet.code.project.service.LabelService;
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
@RequestMapping("/api/labels")
@RequiredArgsConstructor
public class LabelController {

    private final LabelService service;

    /**
     * Retrieves a list of all labels from the service, and returns them in a ResponseEntity.
     * The ResponseEntity also includes a header "X-Total-Count" indicating the total number of labels.
     *
     * @return a ResponseEntity containing a list of all LabelDto objects and a header indicating the total count.
     */
    @GetMapping
    public ResponseEntity<List<LabelDto>> getList() {
        var result = service.getList();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(result.size()))
                .body(result);
    }

    /**
     * Retrieves a label with the specified ID from the service, and returns it.
     *
     * @param id the ID of the label to retrieve.
     * @return a LabelDto object representing the label with the specified ID.
     */
    @GetMapping("/{id}")
    public LabelDto get(@PathVariable Long id) {
        return service.getById(id);
    }

    /**
     * Creates a new label based on the provided LabelAcceptor object, and returns the created label.
     *
     * @param acceptor a LabelAcceptor object containing the data for the label to create.
     * @return a LabelDto object representing the created label.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LabelDto create(@Valid @RequestBody LabelAcceptor acceptor) {
        return service.create(acceptor);
    }

    /**
     * Updates an existing label with the specified ID based on the provided
     * LabelAcceptor object, and returns the updated label.
     *
     * @param id the ID of the label to update.
     * @param acceptor a LabelAcceptor object containing the new data for the label.
     * @return a LabelDto object representing the updated label.
     */
    @PutMapping("/{id}")
    public LabelDto update(@PathVariable Long id, @Valid @RequestBody LabelAcceptor acceptor) {
        return service.update(id, acceptor);
    }

    /**
     * Deletes the label with the specified ID.
     *
     * @param id the ID of the label to delete.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}
