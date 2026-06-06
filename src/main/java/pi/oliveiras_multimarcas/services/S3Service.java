package pi.oliveiras_multimarcas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
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

    // --- MÉTODOS DE UPLOAD EXISTENTES ---

    public String uploadImage(MultipartFile file, String dir) throws IOException {
        if (!Objects.requireNonNull(file.getContentType()).startsWith("image/")) {
            throw new RuntimeException("Arquivo inválido");
        }
        String originalName = file.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf("."));
        String fileName = UUID.randomUUID() + extension;
        String key = dir + "/" + fileName;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, key);
    }

    public String uploadPdf(MultipartFile file, String dir) throws IOException {
        if (!Objects.requireNonNull(file.getContentType()).equals("application/pdf")) {
            throw new RuntimeException("Arquivo inválido");
        }
        String originalName = file.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf("."));
        String fileName = UUID.randomUUID() + extension;
        String key = dir + "/" + fileName;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, key);
    }

    // --- NOVO MÉTODO PARA DELETAR ARQUIVO ---

    public void deleteFile(String fileUrl) {
        try {
            // A URL tem o formato: https://bucket.s3.regiao.amazonaws.com/dir/nome.pdf
            // Precisamos extrair apenas o "dir/nome.pdf" (a key)
            String key = fileUrl.substring(fileUrl.indexOf(".com/") + 5);

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar arquivo do S3: " + e.getMessage());
        }
    }
}