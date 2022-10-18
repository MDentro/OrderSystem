package nl.dentro.OrderSystem.security;

import nl.dentro.OrderSystem.models.User;
import nl.dentro.OrderSystem.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> foundUser = userRepository.findById(username);
        if (foundUser.isPresent()) {
            User user = foundUser.get();
            return new MyUserDetails(user);
        }
        else {
            throw new UsernameNotFoundException(username);
        }
    }
}
