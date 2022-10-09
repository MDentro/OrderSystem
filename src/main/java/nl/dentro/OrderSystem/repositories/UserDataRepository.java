package nl.dentro.OrderSystem.repositories;

import nl.dentro.OrderSystem.models.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDataRepository extends JpaRepository<UserData, Long> {
}
