package pi.oliveiras_multimarcas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pi.oliveiras_multimarcas.services.S3Service;

import java.io.IOException;

@RestController
@RequestMapping("/upload")
public class ImageController {

    @Autowired
    private S3Service s3Service;

    @PostMapping(value = "/cars", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadCarImage(
            @RequestParam("file") MultipartFile file
    ) {
        try {
            String imageUrl = s3Service.uploadImage(file, "cars");
            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(value = "/profiles", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadProfileImage(
            @RequestParam("file") MultipartFile file
    ) {
        try {
            String imageUrl = s3Service.uploadImage(file, "profiles");
            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}