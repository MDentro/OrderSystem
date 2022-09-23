package nl.dentro.OrderSystem.services;

import nl.dentro.OrderSystem.dtos.UserDto;
import nl.dentro.OrderSystem.dtos.UserInputDto;
import nl.dentro.OrderSystem.exceptions.RecordNotFoundException;
import nl.dentro.OrderSystem.models.User;
import nl.dentro.OrderSystem.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto getUserById(Long id) {
        if (availableUserId(id)) {
            User user = userRepository.findById(id).get();
            UserDto userDto = toUserDto(user);
            return userDto;
        } else {
            throw new RecordNotFoundException("Could not find user with id: " + id + ".");
        }
    }

    @Override
    public User createUser(UserInputDto userInputDto) {
        User user = fromUserDto(userInputDto);
        User savedUser = userRepository.save(user);

        return savedUser;
    }

    @Override
    public User fromUserDto(UserInputDto userInputDto) {
        var user = new User();
        user.setFirstName(userInputDto.getFirstName());
        user.setLastName(userInputDto.getLastName());
        user.setEmail(userInputDto.getEmail());
        user.setPhoneNumber(userInputDto.getPhoneNumber());
        return user;
    }

    @Override
    public UserDto toUserDto(User user) {
        var dto = new UserDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        return dto;
    }

    @Override
    public boolean availableUserId(Long id) {
        return userRepository.findById(id).isPresent();
    }


}
