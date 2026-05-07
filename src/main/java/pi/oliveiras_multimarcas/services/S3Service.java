package pi.oliveiras_multimarcas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
public class S3Service {

    @Autowired
    private S3Client s3Client;

    @Value("${aws.bucketName}")
    private String bucketName;

    @Value("${aws.region}")
    private String region;

    public String uploadImage(MultipartFile file) throws IOException {

        // valida se é imagem
        if (!Objects.requireNonNull(file.getContentType())
                .startsWith("image/")) {

            throw new RuntimeException("Arquivo inválido");
        }

        // pega extensão
        String originalName = file.getOriginalFilename();

        String extension = originalName.substring(
                originalName.lastIndexOf(".")
        );

        // gera nome único
        String fileName = UUID.randomUUID() + extension;

        // caminho no bucket
        String key = "cars/" + fileName;

        // request upload
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

        // upload
        s3Client.putObject(
                putObjectRequest,
                RequestBody.fromBytes(file.getBytes())
        );

        // retorna URL pública
        return String.format(
                "https://%s.s3.%s.amazonaws.com/%s",
                bucketName,
                region,
                key
        );
    }
}