package nl.dentro.OrderSystem.services;

import nl.dentro.OrderSystem.dtos.UserDto;
import nl.dentro.OrderSystem.dtos.UserInputDto;
import nl.dentro.OrderSystem.models.User;

public interface UserService {
    UserDto getUserById(Long id);

    User createUser(UserInputDto userInputDto);

    User fromUserDto(UserInputDto userInputDto);

    UserDto toUserDto(User user);

    boolean availableUserId(Long id);
}
