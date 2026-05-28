package aulasenora.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

@Component
public class FileServiceClient {

    private static final Logger log = LoggerFactory.getLogger(FileServiceClient.class);
    private final RestTemplate restTemplate;
    private final String baseUrl;

    public FileServiceClient(@Value("${app.file-service.url:http://localhost:8082}") String baseUrl) {
        this.restTemplate = new RestTemplate();
        this.baseUrl = baseUrl;
    }

    public Optional<String> upload(MultipartFile file, String tipo, String token) {
        Path tempFile = null;
        try {
            tempFile = Files.createTempFile("upload-", file.getOriginalFilename());
            file.transferTo(tempFile.toFile());

            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);

            MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
            parts.add("file", new FileSystemResource(tempFile.toFile()));
            parts.add("tipo", tipo);

            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(parts, headers);

            @SuppressWarnings("unchecked")
            Map<String, Object> result = restTemplate.postForObject(
                    baseUrl + "/api/files/upload", request, Map.class);

            if (result != null && result.containsKey("uuid")) {
                return Optional.of((String) result.get("uuid"));
            }
            return Optional.empty();
        } catch (Exception e) {
            log.error("FileServiceClient.upload failed: tipo={}, file={}", tipo, file.getOriginalFilename(), e);
            return Optional.empty();
        } finally {
            if (tempFile != null) {
                try { Files.deleteIfExists(tempFile); } catch (Exception ignored) {}
            }
        }
    }

    public Optional<Resource> download(String uuid) {
        try {
            Resource resource = restTemplate.getForObject(
                    baseUrl + "/api/files/{uuid}/download", Resource.class, uuid);
            return Optional.ofNullable(resource);
        } catch (Exception e) {
            log.error("FileServiceClient.download failed: uuid={}", uuid, e);
            return Optional.empty();
        }
    }

    public boolean delete(String uuid, String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
            HttpEntity<?> request = new HttpEntity<>(headers);

            restTemplate.exchange(
                    baseUrl + "/api/files/{uuid}",
                    org.springframework.http.HttpMethod.DELETE,
                    request, Void.class, uuid);
            return true;
        } catch (Exception e) {
            log.error("FileServiceClient.delete failed: uuid={}", uuid, e);
            return false;
        }
    }

    public Optional<Map> metadata(String uuid) {
        try {
            @SuppressWarnings("unchecked")
            Map result = restTemplate.getForObject(
                    baseUrl + "/api/files/{uuid}/meta", Map.class, uuid);
            return Optional.ofNullable(result);
        } catch (Exception e) {
            log.error("FileServiceClient.metadata failed: uuid={}", uuid, e);
            return Optional.empty();
        }
    }
}
