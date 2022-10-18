package nl.dentro.OrderSystem.repositories;

import nl.dentro.OrderSystem.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
}
