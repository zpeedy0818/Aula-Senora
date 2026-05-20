package aulasenora.users.service;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class LoginAttemptService {

    private final int MAX_ATTEMPT = 5;
    private final int BLOCK_TIME_MINUTES = 15;
    
    private ConcurrentHashMap<String, Integer> attemptsCache = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, LocalDateTime> blockCache = new ConcurrentHashMap<>();

    public void loginSucceeded(String key) {
        attemptsCache.remove(key);
        blockCache.remove(key);
    }

    public void loginFailed(String key) {
        int attempts = attemptsCache.getOrDefault(key, 0);
        attempts++;
        attemptsCache.put(key, attempts);
        
        if (attempts >= MAX_ATTEMPT) {
            blockCache.put(key, LocalDateTime.now());
        }
    }

    public boolean isBlocked(String key) {
        if (!blockCache.containsKey(key)) {
            return false;
        }

        LocalDateTime blockTime = blockCache.get(key);
        if (blockTime.plusMinutes(BLOCK_TIME_MINUTES).isBefore(LocalDateTime.now())) {
            blockCache.remove(key);
            attemptsCache.remove(key);
            return false;
        }

        return true;
    }
}
