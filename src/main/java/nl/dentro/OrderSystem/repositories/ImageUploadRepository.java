package nl.dentro.OrderSystem.repositories;

import nl.dentro.OrderSystem.models.ImageUploadResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface ImageUploadRepository extends JpaRepository<ImageUploadResponse, String> {
    void deleteByFileName(String fileName);

    Optional<ImageUploadResponse> findByFileName(String fileName);
}
