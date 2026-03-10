package Week2;

import java.util.concurrent.ConcurrentHashMap;

class TokenBucket {
    int tokens;
    int maxTokens;
    double refillRate; // tokens per second
    long lastRefillTime;

    public TokenBucket(int maxTokens, double refillRate) {
        this.maxTokens = maxTokens;
        this.refillRate = refillRate;
        this.tokens = maxTokens;
        this.lastRefillTime = System.currentTimeMillis();
    }

    public synchronized boolean allowRequest() {
        refill();

        if (tokens > 0) {
            tokens--;
            return true;
        }
        return false;
    }

    private void refill() {
        long now = System.currentTimeMillis();
        double seconds = (now - lastRefillTime) / 1000.0;

        int tokensToAdd = (int) (seconds * refillRate);
        if (tokensToAdd > 0) {
            tokens = Math.min(maxTokens, tokens + tokensToAdd);
            lastRefillTime = now;
        }
    }

    public int getRemainingTokens() {
        return tokens;
    }
}

public class problem6 {

    private ConcurrentHashMap<String, TokenBucket> clientBuckets = new ConcurrentHashMap<>();

    private int LIMIT = 1000;
    private double REFILL_RATE = 1000.0 / 3600.0; // 1000 requests per hour

    public String checkRateLimit(String clientId) {

        clientBuckets.putIfAbsent(clientId, new TokenBucket(LIMIT, REFILL_RATE));
        TokenBucket bucket = clientBuckets.get(clientId);

        if (bucket.allowRequest()) {
            return "Allowed (" + bucket.getRemainingTokens() + " requests remaining)";
        } else {
            return "Denied (0 requests remaining, try later)";
        }
    }

    public String getRateLimitStatus(String clientId) {

        TokenBucket bucket = clientBuckets.get(clientId);

        if (bucket == null) {
            return "No requests yet.";
        }

        int used = LIMIT - bucket.getRemainingTokens();

        return "{used: " + used + ", limit: " + LIMIT + "}";
    }

    public static void main(String[] args) {

        problem6 limiter = new problem6();

        System.out.println(limiter.checkRateLimit("abc123"));
        System.out.println(limiter.checkRateLimit("abc123"));
        System.out.println(limiter.checkRateLimit("abc123"));

        System.out.println(limiter.getRateLimitStatus("abc123"));
    }
}