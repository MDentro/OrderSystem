package nl.dentro.OrderSystem.services;

import nl.dentro.OrderSystem.dtos.ImageUploadResponseDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    ImageUploadResponseDto saveFile(MultipartFile file);

    Resource downLoadFile(String fileName);

    void deleteImage(String fileName);
}
