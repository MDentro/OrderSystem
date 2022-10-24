package nl.dentro.OrderSystem.services;

import nl.dentro.OrderSystem.dtos.ImageDto;
import nl.dentro.OrderSystem.exceptions.DeniedFileExtensionException;
import nl.dentro.OrderSystem.exceptions.RecordNotFoundException;
import nl.dentro.OrderSystem.models.Image;
import nl.dentro.OrderSystem.repositories.ImageRepository;
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

    private final ImageRepository imageRepository;

    public ImageServiceImpl(@Value("${my.upload_location}") String fileStorageLocation, ImageRepository imageRepository) {
        fileStoragePath = Paths.get(fileStorageLocation).toAbsolutePath().normalize();

        this.fileStorageLocation = fileStorageLocation;
        this.imageRepository = imageRepository;

        try {
            Files.createDirectories(fileStoragePath);
        } catch (IOException e) {
            throw new RuntimeException("Issue in creating file directory");
        }

    }

    @Override
    public ImageDto saveFile(MultipartFile file) {
        String url = ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/").path(Objects.requireNonNull(file.getOriginalFilename())).toUriString();
        String contentType = file.getContentType();
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        if (allowFileExtension(fileName)) {
            throw new DeniedFileExtensionException("Only file extensions: jpg / JPG / png / PNG are accepted, now your extension is: " + extension(fileName));
        }

        Path filePath = Paths.get(fileStoragePath + "\\" + fileName);

        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Issue in storing the file", e);
        }
        imageRepository.save(new Image(fileName, file.getContentType(), url));
        return toImageDTO(fileName, contentType, url);
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
        if (availableImageId(fileName)) {
            Path filePath = Paths.get(fileStoragePath + "\\" + fileName);
            try {
                Files.delete(filePath);
                imageRepository.deleteByFileName(fileName);
            } catch (IOException e) {
                throw new RuntimeException("Issue with deleting the file", e);
            }
        }
    }

    @Override
    public ImageDto toImageDTO(String fileName, String contentType, String url) {
        ImageDto imageDto = new ImageDto(fileName, contentType, createDownloadUrl(url));
        return imageDto;
    }

    @Override
    public String createDownloadUrl(String url) {
        String downloadUrl = url.substring(0, 21) + "/images/" + url.substring(22);
        return downloadUrl;
    }

    @Override
    public boolean allowFileExtension(String fileName) {
        return !extension(fileName).equalsIgnoreCase("png") && !extension(fileName).equalsIgnoreCase("jpg");
    }

    @Override
    public String extension(String fileName) {
        String reverseFileName = new StringBuilder(fileName).reverse().toString();
        int foundDotIndex = reverseFileName.indexOf(".");
        String foundExtensionReverse = reverseFileName.substring(0, foundDotIndex);
        return new StringBuilder(foundExtensionReverse).reverse().toString();
    }

    @Override
    public boolean availableImageId(String fileName) {
        return imageRepository.findByFileName(fileName).isPresent();
    }
}
