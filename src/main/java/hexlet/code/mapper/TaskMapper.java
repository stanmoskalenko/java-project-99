package hexlet.code.mapper;

import hexlet.code.dto.task.TaskDto;
import hexlet.code.dto.task.acceptor.CreateTaskAcceptor;
import hexlet.code.dto.task.acceptor.UpdateTaskAcceptor;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {

    @Autowired
    TaskStatusRepository taskStatusRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    LabelRepository labelRepository;

    @Mapping(target = "content", source = "description")
    @Mapping(target = "status", source = "status.slug")
    @Mapping(target = "title", source = "name")
    @Mapping(target = "assigneeId", source = "assignee.id")
    @Mapping(target = "taskLabelIds", source = "labels", qualifiedByName = "resolveLabelsByEntity")
    public abstract TaskDto toDto(Task entity);

    @Mapping(target = "description", source = "content")
    @Mapping(target = "name", source = "title")
    @Mapping(target = "status", source = "status", qualifiedByName = "resolveStatus")
    @Mapping(target = "assignee", source = "assigneeId", qualifiedByName = "resolveUser")
    @Mapping(target = "labels", source = "taskLabelIds", qualifiedByName = "resolveLabelsByIds")
    public abstract Task toCreateEntity(CreateTaskAcceptor acceptor);

    @Mapping(target = "description", source = "content")
    @Mapping(target = "name", source = "title")
    @Mapping(target = "status", source = "status", qualifiedByName = "resolveStatus")
    @Mapping(target = "assignee", source = "assigneeId", qualifiedByName = "resolveUser")
    @Mapping(target = "labels", source = "taskLabelIds", qualifiedByName = "resolveLabelsByIds")
    public abstract Task toUpdateEntity(UpdateTaskAcceptor acceptor, @MappingTarget Task task);

    /**
     * Fetches a TaskStatus entity by its slug.
     *
     * @param name the slug of the TaskStatus entity
     * @return the found TaskStatus entity
     * @throws ResourceNotFoundException if no TaskStatus entity with the given slug is found
     */
    @Named("resolveStatus")
    public TaskStatus getStatusByName(String name) {
        return taskStatusRepository.findBySlug(name)
                .orElseThrow(() -> new ResourceNotFoundException("TaskStatus = " + name + " not found!"));
    }

    /**
     * Fetches a User entity by its ID.
     *
     * @param id the ID of the User entity
     * @return the found User entity
     * @throws ResourceNotFoundException if no User entity with the given ID is found
     */
    @Named("resolveUser")
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id = " + id + " not found!"));
    }

    /**
     * Fetches a list of Label entities by their IDs.
     *
     * @param ids the IDs of the Label entities
     * @return the found Label entities, or null if the input list is empty
     */
    @Named("resolveLabelsByIds")
    public List<Label> getLabelById(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }

        return labelRepository.findAllById(ids);
    }

    /**
     * Converts a list of Label entities to a list of their IDs.
     *
     * @param labels the Label entities
     * @return a list of IDs of the given Label entities
     */
    @Named("resolveLabelsByEntity")
    public List<Long> getLabelIds(List<Label> labels) {
        return labels.stream()
                .map(Label::getId)
                .toList();
    }
}
