package aulasenora.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;

@Service
public class GoogleCalendarService {

    private static final String APPLICATION_NAME = "Aula Señora";
    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
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
                    JSON_FACTORY,
                    new HttpCredentialsAdapter(credentials))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
            
            isConfigured = true;
            System.out.println("Google Calendar Service initialized successfully.");
        } catch (Exception e) {
            System.err.println("Google Calendar Service not initialized. Missing or invalid " + CREDENTIALS_FILE_PATH);
        }
    }

    public boolean isConfigured() {
        return isConfigured;
    }

    /**
     * Creates a Google Calendar event with a Meet link and invites attendees.
     */
    public EventResult createEventWithMeet(String summary, String description, LocalDate date, LocalTime startTime, LocalTime endTime, String volunteerEmail, java.util.List<String> attendeeEmails) {
        if (!isConfigured) return null;

        try {
            Event event = new Event()
                .setSummary(summary)
                .setDescription(description);

            ZoneId zoneId = ZoneId.systemDefault();
            
            Date startDateTime = Date.from(LocalDateTime.of(date, startTime).atZone(zoneId).toInstant());
            EventDateTime start = new EventDateTime()
                .setDateTime(new com.google.api.client.util.DateTime(startDateTime))
                .setTimeZone(zoneId.getId());
            event.setStart(start);

            Date endDateTime = Date.from(LocalDateTime.of(date, endTime).atZone(zoneId).toInstant());
            EventDateTime end = new EventDateTime()
                .setDateTime(new com.google.api.client.util.DateTime(endDateTime))
                .setTimeZone(zoneId.getId());
            event.setEnd(end);

            // Execute request directly on the volunteer's calendar
            Event createdEvent = calendarService.events().insert(volunteerEmail, event)
                .execute();

            return new EventResult(createdEvent.getId(), getMeetLink(createdEvent));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Google Calendar Error: Asegúrate de haber compartido tu calendario con la cuenta de servicio y otorgado permisos de 'Realizar cambios en eventos'. Detalle técnico: " + e.getMessage(), e);
        }
    }

    /**
     * Adds an attendee to an existing event on the given calendar.
     */
    public void addAttendeeToEvent(String calendarId, String eventId, String email) {
        // No-op: Service accounts without Domain-Wide Delegation cannot invite attendees.
        // The student will use the Meet link from the platform directly.
        System.out.println("Skipping Google Calendar invitation for " + email + " due to API restrictions.");
    }

    private String getMeetLink(Event event) {
        if (event.getConferenceData() != null && event.getConferenceData().getEntryPoints() != null) {
            for (EntryPoint ep : event.getConferenceData().getEntryPoints()) {
                if ("video".equals(ep.getEntryPointType())) {
                    return ep.getUri();
                }
            }
        }
        return null;
    }

    public static class EventResult {
        public final String eventId;
        public final String meetLink;

        public EventResult(String eventId, String meetLink) {
            this.eventId = eventId;
            this.meetLink = meetLink;
        }
    }
}
