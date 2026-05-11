document.addEventListener('DOMContentLoaded', () => {
    const timerDisplay = document.getElementById('session-timer-display');
    
    // We don't return early anymore because we want the background ping to continue even if the UI is not present.

    let accumulatedSeconds = 0;
    
    // Format seconds into HH:MM:SS
    function formatTime(totalSeconds) {
        const hours = Math.floor(totalSeconds / 3600);
        const minutes = Math.floor((totalSeconds % 3600) / 60);
        const seconds = totalSeconds % 60;
        
        return [
            hours.toString().padStart(2, '0'),
            minutes.toString().padStart(2, '0'),
            seconds.toString().padStart(2, '0')
        ].join(':');
    }

    // Update the UI if present
    function updateTimerDisplay() {
        if (timerDisplay) {
            timerDisplay.textContent = formatTime(accumulatedSeconds);
        }
    }

    // Fetch initial time from server
    fetch('/api/time/current')
        .then(response => {
            if (response.ok) return response.json();
            throw new Error('Not authenticated or error fetching time');
        })
        .then(data => {
            accumulatedSeconds = data.tiempoAcumulado || 0;
            updateTimerDisplay();
            
            // Start local counter
            setInterval(() => {
                accumulatedSeconds++;
                updateTimerDisplay();
            }, 1000);
            
            // Start heartbeat sync with server (every 10 seconds)
            setInterval(() => {
                // Only ping if the tab is visible to avoid double counting across multiple background tabs
                if (document.visibilityState === 'visible') {
                    fetch('/api/time/ping', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    }).catch(err => console.error('Failed to sync time:', err));
                }
            }, 10000);
        })
        .catch(err => {
            console.error('Session time sync disabled:', err);
        });
});
