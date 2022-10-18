package nl.dentro.OrderSystem.controllers;

import nl.dentro.OrderSystem.dtos.AuthInputDto;
import nl.dentro.OrderSystem.security.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static nl.dentro.OrderSystem.util.UtilityMethods.getValidationErrorMessage;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("")
    public ResponseEntity<Object> signIn(@Valid @RequestBody AuthInputDto authInputDto, BindingResult br) {
        if (br.hasErrors()) {
            return new ResponseEntity<>(getValidationErrorMessage(br), HttpStatus.BAD_REQUEST);
        } else {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(authInputDto.getUserName(), authInputDto.getPassword());

            try {
                Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String token = jwtService.generateToken(userDetails);

                return ResponseEntity.ok()
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .body("Token generated");
            } catch (AuthenticationException ex) {
                return new ResponseEntity(ex.getMessage(), HttpStatus.UNAUTHORIZED);
            }
        }
    }
}
