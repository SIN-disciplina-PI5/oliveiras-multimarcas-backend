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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImage(
            @RequestParam("file") MultipartFile file
    ) {
        try {
            String imageUrl = s3Service.uploadImage(file);
            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}