package aulasenora.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class AdminServiceClient {

    private final RestClient restClient;

    public AdminServiceClient(@Value("${app.admin-service.url:http://localhost:8086}") String baseUrl) {
        this.restClient = RestClient.create(baseUrl);
    }

    public Optional<Map<String, Object>> getStats(String token) {
        try {
            Map<String, Object> result = restClient.get()
                    .uri("/api/admin/stats")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
            return Optional.ofNullable(result);
        } catch (Exception e) {
            System.err.println("AdminServiceClient.getStats failed: " + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<List<Map<String, Object>>> listUsers(String token) {
        try {
            List<Map<String, Object>> result = restClient.get()
                    .uri("/api/admin/users")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
            return Optional.ofNullable(result);
        } catch (Exception e) {
            System.err.println("AdminServiceClient.listUsers failed: " + e.getMessage());
            return Optional.empty();
        }
    }

    public boolean toggleUserStatus(Long id, String token) {
        try {
            restClient.put()
                    .uri("/api/admin/users/{id}/toggle-status", id)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .retrieve()
                    .toBodilessEntity();
            return true;
        } catch (Exception e) {
            System.err.println("AdminServiceClient.toggleUserStatus failed: " + e.getMessage());
            return false;
        }
    }

    public boolean verifyVolunteer(Long id, String token) {
        try {
            restClient.put()
                    .uri("/api/admin/users/{id}/verify", id)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .retrieve()
                    .toBodilessEntity();
            return true;
        } catch (Exception e) {
            System.err.println("AdminServiceClient.verifyVolunteer failed: " + e.getMessage());
            return false;
        }
    }
}
