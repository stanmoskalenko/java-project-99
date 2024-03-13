package hexlet.code.mapper;

import hexlet.code.dto.label.LabelDto;
import hexlet.code.dto.label.acceptor.LabelAcceptor;
import hexlet.code.model.Label;
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
public interface LabelMapper {

    LabelDto toDto(Label entity);

    Label toCreateEntity(LabelAcceptor acceptor);

    Label toUpdateEntity(LabelAcceptor acceptor, @MappingTarget Label status);

}
