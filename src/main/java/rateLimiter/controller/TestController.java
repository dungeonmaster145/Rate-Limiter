package rateLimiter.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rateLimiter.enums.UserTier;
import rateLimiter.service.RateLimiterService;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@Slf4j
@RequiredArgsConstructor
public class TestController {

    private final RateLimiterService rateLimiterService;

    @GetMapping("/limited")
    public ResponseEntity<?> limitedEndpoint(HttpServletRequest request, @RequestParam UserTier userTier) {
        String clientIp = getClientIpAddress(request);

        if (rateLimiterService.isRequestAllowed(clientIp, userTier)) {
            return ResponseEntity.ok(Map.of(
                    "message", "Request successful!",
                    "timestamp", Instant.now(),
                    "clientIp", clientIp
            ));
        } else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(Map.of(
                            "error", "Rate limit exceeded",
                            "message", "Too many requests from IP: " + clientIp,
                            "retryAfter", "60 seconds"
                    ));
        }
    }

    private String getClientIpAddress(HttpServletRequest request) {
        // Handle proxy headers
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
