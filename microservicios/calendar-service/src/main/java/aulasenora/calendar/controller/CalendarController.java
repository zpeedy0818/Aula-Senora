package aulasenora.calendar.controller;

import aulasenora.calendar.service.GoogleCalendarService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController {

    private final GoogleCalendarService googleCalendarService;

    public CalendarController(GoogleCalendarService googleCalendarService) {
        this.googleCalendarService = googleCalendarService;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Boolean>> health() {
        return ResponseEntity.ok(Map.of("configured", googleCalendarService.isConfigured()));
    }

    @PostMapping("/events")
    public ResponseEntity<Map<String, String>> createEvent(
            @RequestParam String summary,
            @RequestParam String description,
            @RequestParam String date,
            @RequestParam String horaInicio,
            @RequestParam String horaFin,
            @RequestParam String calendarEmail,
            Authentication auth) {
        if (auth == null) return ResponseEntity.status(401).build();
        try {
            String eventId = googleCalendarService.createEvent(
                    summary, description,
                    LocalDate.parse(date),
                    LocalTime.parse(horaInicio),
                    LocalTime.parse(horaFin),
                    calendarEmail);
            if (eventId == null) {
                return ResponseEntity.ok(Map.of("status", "not_configured"));
            }
            return ResponseEntity.ok(Map.of("eventId", eventId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
