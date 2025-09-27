package rateLimiter.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rateLimiter.algorithm.TokenBucket;
import rateLimiter.enums.UserTier;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class RateLimiterService {

    private final Map<String, TokenBucket> buckets = new ConcurrentHashMap<>();

    public boolean isRequestAllowed(String ip, UserTier tier){
        // we will create a token bucket for each user
        String bucketKey = ip + "_" + tier.name();
        int capacity = tier.getCapacity();
        int refillRate = tier.getRefillRate();
        TokenBucket bucket = buckets.computeIfAbsent(bucketKey,
                key -> new TokenBucket(capacity,
                        refillRate));
        return bucket.allowRequest();
    }
}
