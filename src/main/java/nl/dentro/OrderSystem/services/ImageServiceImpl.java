package nl.dentro.OrderSystem.services;

import nl.dentro.OrderSystem.dtos.ImageUploadResponseDto;
import nl.dentro.OrderSystem.exceptions.RecordNotFoundException;
import nl.dentro.OrderSystem.models.ImageUploadResponse;
import nl.dentro.OrderSystem.repositories.ImageUploadRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class ImageServiceImpl implements ImageService {
    @Value("${my.upload_location}")
    private Path fileStoragePath;
    private final String fileStorageLocation;

    private final ImageUploadRepository imageUploadRepository;

    public ImageServiceImpl(@Value("${my.upload_location}") String fileStorageLocation, ImageUploadRepository imageUploadRepository) {
        fileStoragePath = Paths.get(fileStorageLocation).toAbsolutePath().normalize();

        this.fileStorageLocation = fileStorageLocation;
        this.imageUploadRepository = imageUploadRepository;


        try {
            Files.createDirectories(fileStoragePath);
        } catch (IOException e) {
            throw new RuntimeException("Issue in creating file directory");
        }

    }

    @Override
    public ImageUploadResponseDto saveFile(MultipartFile file) {
        String url = ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/").path(Objects.requireNonNull(file.getOriginalFilename())).toUriString();
        String contentType = file.getContentType();
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        Path filePath = Paths.get(fileStoragePath + "\\" + fileName);

        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Issue in storing the file", e);
        }
        imageUploadRepository.save(new ImageUploadResponse(fileName, file.getContentType(), url));
        return toImageResponseDTO(fileName, contentType, url);
    }

    @Override
    public Resource downLoadFile(String fileName) {

        Path path = Paths.get(fileStorageLocation).toAbsolutePath().resolve(fileName);

        Resource resource;

        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Issue in reading the file", e);
        }

        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new RecordNotFoundException("Could not find the image with the name: " + fileName + ". Or the image isn't readable.");
        }
    }

    @Override
    public void deleteImage(String fileName) {
        imageUploadRepository.deleteByFileName(fileName);
    }

    @Override
    public ImageUploadResponseDto toImageResponseDTO(String fileName, String contentType, String url) {
        ImageUploadResponseDto imageUploadResponseDto = new ImageUploadResponseDto(fileName, contentType, url);
        return imageUploadResponseDto;
    }

    @Override
    public String toImageResponseFileName(String name) {
        ImageUploadResponse imageUploadResponse = new ImageUploadResponse();
        imageUploadResponse.setFileName(name);
        return imageUploadResponse.getFileName();
    }

}
