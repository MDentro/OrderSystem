package nl.dentro.OrderSystem.services;

import nl.dentro.OrderSystem.dtos.UserDto;
import nl.dentro.OrderSystem.dtos.UserInputDto;
import nl.dentro.OrderSystem.exceptions.DuplicateFoundException;
import nl.dentro.OrderSystem.models.Role;
import nl.dentro.OrderSystem.models.User;
import nl.dentro.OrderSystem.repositories.RoleRepository;
import nl.dentro.OrderSystem.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    @Override
    public UserDto createUser(UserInputDto userInputDto) {
        User user = toUser(userInputDto);
        Optional<User> foundUser = (userRepository.findById(user.getUserName()));
        if (foundUser.isPresent()) {
            throw new DuplicateFoundException("The username " + userInputDto.getUserName() + " already exists.");
        } else {
            User savedUser = userRepository.save(user);
            return fromUser(savedUser);
        }
    }

    @Override
    public UserDto fromUser(User user) {
        var dto = new UserDto();
        dto.setUserName(user.getUserName());
        dto.setPassword(user.getPassword());
        for (Role rolename : user.getRoles()) {
            dto.getRoles().add(rolename.getRoleName());
        }
        return dto;
    }

    @Override
    public User toUser(UserInputDto dto) {
        var user = new User();
        user.setUserName(dto.getUserName());
        user.setPassword(encoder.encode(dto.getPassword()));
        List<Role> userRoles = new ArrayList<>();
        for (String rolename : dto.getRoles()) {
            String name = rolename.toUpperCase();
            Optional<Role> databaseRole = roleRepository.findById(name);
            userRoles.add(databaseRole.get());
        }
        user.setRoles(userRoles);
        return user;
    }
}
