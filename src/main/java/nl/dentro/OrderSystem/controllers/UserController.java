package nl.dentro.OrderSystem.controllers;

import nl.dentro.OrderSystem.dtos.UserDto;
import nl.dentro.OrderSystem.dtos.UserInputDto;
import nl.dentro.OrderSystem.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static nl.dentro.OrderSystem.util.UtilityMethods.getValidationErrorMessage;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {
    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserInputDto userInputDto, BindingResult br) {
        if (br.hasErrors()) {
            return new ResponseEntity<>(getValidationErrorMessage(br), HttpStatus.BAD_REQUEST);
        } else {
            UserDto userDto = userService.createUser(userInputDto);
            return new ResponseEntity<>(userDto, HttpStatus.CREATED);
        }
    }
}
