package rateLimiter.algorithm;

import lombok.Getter;
import rateLimiter.enums.UserTier;

import java.time.Duration;
import java.time.Instant;

@Getter
public class TokenBucket {

    private final Integer capacity;
    private final Integer refillRate;
    private int availableTokens;
    private Instant lastRefillTime;

    public TokenBucket(int capacity, int refillRate){
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.availableTokens = capacity;
        this.lastRefillTime = Instant.now();
    }

    public synchronized boolean allowRequest(){
       this.refillTokens();
       if(availableTokens>0){
           availableTokens--;
           return true;
       }

       return false;

    }

    private void refillTokens(){
        Instant now = Instant.now();
        //calculate the time elapsed since last refill
        long hoursElapsed =
                Duration.between(lastRefillTime, now).toSeconds();
        if(hoursElapsed>0){
            //calculate how many new token to add
            int newTokens = (int)(hoursElapsed*refillRate);
            if(newTokens>0){
                //add the tokens, but not more than the capacity
                availableTokens = Math.min(capacity, availableTokens+newTokens);
                lastRefillTime = now;
            }
        }
    }




}
