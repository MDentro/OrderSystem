package nl.dentro.OrderSystem.services;

import nl.dentro.OrderSystem.dtos.UserDto;
import nl.dentro.OrderSystem.dtos.UserInputDto;
import nl.dentro.OrderSystem.models.User;

public interface UserService {
    UserDto createUser(UserInputDto userInputDto);

    void checkDuplicateUserRole(User user);

    UserDto fromUser(User user);

    User toUser(UserInputDto dto);

    void userRoleExists(UserInputDto dto);
}
