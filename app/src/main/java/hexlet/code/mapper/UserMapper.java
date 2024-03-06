package hexlet.code.mapper;

import hexlet.code.dto.user.UserDto;
import hexlet.code.dto.user.acceptor.BaseUserAcceptor;
import hexlet.code.dto.user.acceptor.CreateUserAcceptor;
import hexlet.code.dto.user.acceptor.UpdateUserAcceptor;
import hexlet.code.model.User;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;


@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {

    @Autowired
    PasswordEncoder passwordEncoder;

    public abstract UserDto toDto(User entity);

    public abstract User toCreateEntity(CreateUserAcceptor acceptor);

    public abstract User toUpdateEntity(UpdateUserAcceptor acceptor, @MappingTarget User user);

    /**
     * This method encrypts the password of a BaseUserAcceptor object before mapping.
     * If the password is not null, it will be encoded using the PasswordEncoder
     * and then set back to the acceptor.
     *
     * @param acceptor The BaseUserAcceptor object whose password is to be encrypted.
     */
    @BeforeMapping
    public void encryptPassword(BaseUserAcceptor acceptor) {
        var password = acceptor.getPassword();
        if (password != null) {
            var encodedPassword = passwordEncoder.encode(password);
            acceptor.setPassword(encodedPassword);
        }
    }

}
