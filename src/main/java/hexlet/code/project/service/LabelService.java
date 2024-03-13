package hexlet.code.project.service;

import hexlet.code.project.dto.label.LabelDto;
import hexlet.code.project.dto.label.acceptor.LabelAcceptor;
import hexlet.code.project.exception.ResourceNotFoundException;
import hexlet.code.project.exception.SqlException;
import hexlet.code.project.mapper.LabelMapper;
import hexlet.code.project.repository.LabelRepository;
import hexlet.code.project.utils.ErrorMessages;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LabelService {

    private LabelRepository repository;
    private LabelMapper mapper;

    private static final String ENTITY_NAME = "Label";

    /**
     * Retrieves all labels from the repository, maps them to LabelDto objects, and returns them as a list.
     *
     * @return a list of LabelDto objects representing all labels in the repository.
     */
    public List<LabelDto> getList() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    /**
     * Retrieves a label by its ID, maps it to a LabelDto object, and returns it.
     * If the label is not found, a ResourceNotFoundException is thrown.
     *
     * @param id the ID of the label to retrieve.
     * @return a LabelDto object representing the label with the given ID.
     * @throws ResourceNotFoundException if no label with the given ID is found.
     */
    public LabelDto getById(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        ErrorMessages.NOT_FOUND_BY_ID,
                        ENTITY_NAME,
                        id)));
    }

    /**
     * Creates a new label in the repository based on the provided LabelAcceptor object.
     * The created label is then mapped to a LabelDto object and returned.
     *
     * @param acceptor a LabelAcceptor object containing the data for the label to create.
     * @return a LabelDto object representing the created label.
     */
    public LabelDto create(LabelAcceptor acceptor) {
        var entity = mapper.toCreateEntity(acceptor);
        repository.save(entity);
        return repository.findById(entity.getId())
                .map(mapper::toDto)
                .orElse(null);
    }

    /**
     * Updates an existing label in the repository based on the provided ID and LabelAcceptor object.
     * The updated label is then mapped to a LabelDto object and returned.
     * If the label is not found, a ResourceNotFoundException is thrown.
     *
     * @param id the ID of the label to update.
     * @param acceptor a LabelAcceptor object containing the new data for the label.
     * @return a LabelDto object representing the updated label.
     * @throws ResourceNotFoundException if no label with the given ID is found.
     */
    public LabelDto update(long id, LabelAcceptor acceptor) {
        var label = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        ErrorMessages.NOT_FOUND_BY_ID,
                        ENTITY_NAME,
                        id)));
        var entity = mapper.toUpdateEntity(acceptor, label);
        repository.save(entity);
        return repository.findById(entity.getId())
                .map(mapper::toDto)
                .orElse(null);
    }

    /**
     * Deletes a label from the repository based on the provided ID.
     * If the label cannot be deleted due to a data integrity violation, a SqlException is thrown.
     *
     * @param id the ID of the label to delete.
     * @throws SqlException if the label cannot be deleted due to a data integrity violation.
     */
    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new SqlException(ErrorMessages.CONSTRAINT);
        }
    }
}
