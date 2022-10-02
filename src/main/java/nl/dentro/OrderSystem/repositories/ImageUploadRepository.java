package nl.dentro.OrderSystem.repositories;

import nl.dentro.OrderSystem.models.ImageUploadResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface ImageUploadRepository extends JpaRepository<ImageUploadResponse, String> {
    void deleteByFileName(String fileName);
}
