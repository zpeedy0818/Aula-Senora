package aulasenora.calendar.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;

@Service
public class GoogleCalendarService {

    private static final String CREDENTIALS_FILE_PATH = "google-credentials.json";

    private Calendar calendarService;
    private boolean isConfigured = false;

    @PostConstruct
    public void init() {
        try {
            InputStream in = new FileInputStream(CREDENTIALS_FILE_PATH);
            GoogleCredentials credentials = GoogleCredentials.fromStream(in)
                    .createScoped(Collections.singleton(CalendarScopes.CALENDAR));
            calendarService = new Calendar.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    new HttpCredentialsAdapter(credentials))
                    .setApplicationName("Aula Senora")
                    .build();
            isConfigured = true;
        } catch (Exception e) {
            System.err.println("Google Calendar no configurado: " + e.getMessage());
        }
    }

    public boolean isConfigured() { return isConfigured; }

    public String createEvent(String summary, String description, LocalDate date,
                               LocalTime startTime, LocalTime endTime, String calendarEmail) {
        if (!isConfigured) return null;
        try {
            ZoneId zoneId = ZoneId.systemDefault();
            Event event = new Event().setSummary(summary).setDescription(description);
            event.setStart(new EventDateTime()
                    .setDateTime(new com.google.api.client.util.DateTime(
                            Date.from(LocalDateTime.of(date, startTime).atZone(zoneId).toInstant())))
                    .setTimeZone(zoneId.getId()));
            event.setEnd(new EventDateTime()
                    .setDateTime(new com.google.api.client.util.DateTime(
                            Date.from(LocalDateTime.of(date, endTime).atZone(zoneId).toInstant())))
                    .setTimeZone(zoneId.getId()));

            Event created = calendarService.events().insert(calendarEmail, event).execute();
            return created.getId();
        } catch (Exception e) {
            throw new RuntimeException("Error al crear evento: " + e.getMessage(), e);
        }
    }
}
