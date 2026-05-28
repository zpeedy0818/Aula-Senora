package aulasenora.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;

@Component
public class CalendarServiceClient {

    private final RestClient restClient;

    public CalendarServiceClient(@Value("${app.calendar-service.url:http://localhost:8085}") String baseUrl) {
        this.restClient = RestClient.create(baseUrl);
    }

    public boolean isConfigured() {
        try {
            var result = restClient.get()
                    .uri("/api/calendar/health")
                    .retrieve()
                    .body(Map.class);
            return result != null && Boolean.TRUE.equals(result.get("configured"));
        } catch (Exception e) {
            System.err.println("CalendarServiceClient.isConfigured failed: " + e.getMessage());
            return false;
        }
    }

    public Optional<String> createEvent(String summary, String description, LocalDate date,
                                         LocalTime horaInicio, LocalTime horaFin,
                                         String calendarEmail, String token) {
        try {
            var result = restClient.post()
                    .uri(uriBuilder -> uriBuilder.path("/api/calendar/events")
                            .queryParam("summary", summary)
                            .queryParam("description", description)
                            .queryParam("date", date.toString())
                            .queryParam("horaInicio", horaInicio.toString())
                            .queryParam("horaFin", horaFin.toString())
                            .queryParam("calendarEmail", calendarEmail)
                            .build())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .retrieve()
                    .body(Map.class);
            if (result != null && result.containsKey("eventId")) {
                return Optional.of((String) result.get("eventId"));
            }
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("CalendarServiceClient.createEvent failed: " + e.getMessage());
            return Optional.empty();
        }
    }
}
