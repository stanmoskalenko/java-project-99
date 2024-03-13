package hexlet.code.project.mapper;

import hexlet.code.project.dto.taskstatus.TaskStatusDto;
import hexlet.code.project.dto.taskstatus.acceptor.CreateTaskStatusAcceptor;
import hexlet.code.project.dto.taskstatus.acceptor.UpdateTaskStatusAcceptor;
import hexlet.code.project.model.TaskStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TaskStatusMapper {

    TaskStatusDto toDto(TaskStatus entity);

    TaskStatus toCreateEntity(CreateTaskStatusAcceptor acceptor);

    TaskStatus toUpdateEntity(UpdateTaskStatusAcceptor acceptor, @MappingTarget TaskStatus status);

}
