package aulasenora.service;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class LoginAttemptService {

    private final int MAX_ATTEMPT = 5;
    private final int BLOCK_TIME_MINUTES = 15;
    
    // Almacena la IP y el número de intentos fallidos
    private ConcurrentHashMap<String, Integer> attemptsCache = new ConcurrentHashMap<>();
    
    // Almacena la IP y la hora en la que fue bloqueada
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
            // El tiempo de bloqueo ya expiró
            blockCache.remove(key);
            attemptsCache.remove(key);
            return false;
        }

        return true;
    }
}
