package rateLimiter.enums;

public enum UserTier {
    FREE(10, 1),      // 10 capacity, 1 token per second
    PREMIUM(50, 5),   // 50 capacity, 5 tokens per second
    ADMIN(200, 10);   // 200 capacity, 10 tokens per second

    private final int capacity;
    private final int refillRate;

    UserTier(int capacity, int refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
    }

    public int getCapacity() { return capacity; }
    public int getRefillRate() { return refillRate; }

}
