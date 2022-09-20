package nl.dentro.OrderSystem.repositories;

import nl.dentro.OrderSystem.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
