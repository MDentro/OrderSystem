package nl.dentro.OrderSystem.repositories;

import nl.dentro.OrderSystem.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface ImageRepository extends JpaRepository<Image, String> {
    void deleteByFileName(String fileName);

    Optional<Image> findByFileName(String fileName);
}
