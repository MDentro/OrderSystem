package nl.dentro.OrderSystem.services;

import nl.dentro.OrderSystem.dtos.ImageDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    ImageDto saveFile(MultipartFile file);

    Resource downLoadFile(String fileName);

    void deleteImage(String fileName);

    ImageDto toImageDTO(String fileName, String contentType, String url);

    String createDownloadUrl(String url);

    boolean allowFileExtension(String fileName);

    String extension(String fileName);

    boolean availableImageId(String fileName);
}
