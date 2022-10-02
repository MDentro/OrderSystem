package nl.dentro.OrderSystem.controllers;

import nl.dentro.OrderSystem.dtos.ImageUploadResponseDto;
import nl.dentro.OrderSystem.services.ImageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@RestController
@CrossOrigin
@RequestMapping("images")
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    ResponseEntity<Object> singleFileUpload(@RequestParam("file") MultipartFile file){
        ImageUploadResponseDto ImageUploadResponseDto = imageService.saveFile(file);
        return ResponseEntity.ok().body(ImageUploadResponseDto);
    }

    @GetMapping("/download/{fileName}")
    ResponseEntity<Resource> downLoadSingleFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = imageService.downLoadFile(fileName);
        String mimeType;
        try{
            mimeType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType)).header(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName=" + resource.getFilename()).body(resource);
    }

    @DeleteMapping( "/{fileName}")
    public ResponseEntity<Object> deletePicture(@PathVariable String fileName) {
        imageService.deleteImage(fileName);
        return ResponseEntity.noContent().build();
    }

}