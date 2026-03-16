package apiratelimiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Problem 6: Distributed API Rate Limiter
 * Utilizes a ConcurrentHashMap and the Token Bucket Algorithm to restrict requests.
 */
public class RateLimiter {

    /**
     * Internal state object simulating a Token Bucket for a single client.
     */
    private static class TokenBucket {
        private final long maxBucketSize;
        private final double refillRatePerMillisecond;
        
        private double currentTokens;
        private long lastRefillTimestamp;

        public TokenBucket(long maxBucketSize, long refillRate, long refillDecayWindowMs) {
            this.maxBucketSize = maxBucketSize;
            this.currentTokens = maxBucketSize; // Start completely full
            this.refillRatePerMillisecond = (double) refillRate / refillDecayWindowMs;
            this.lastRefillTimestamp = System.currentTimeMillis();
        }

        /**
         * Lazily calculates how many tokens should have been regenerated since the last interaction.
         */
        private void lazyRefill() {
            long now = System.currentTimeMillis();
            long elapsedTime = now - lastRefillTimestamp;

            // Calculate tokens generated during elapsed time
            double tokensToAdd = elapsedTime * refillRatePerMillisecond;

            // Only update if time naturally passed
            if (tokensToAdd > 0) {
                currentTokens = Math.min(maxBucketSize, currentTokens + tokensToAdd);
                lastRefillTimestamp = now;
            }
        }

        /**
         * Checks if 1 request token is available.
         */
        public synchronized boolean allowRequest() {
            lazyRefill();
            if (currentTokens >= 1.0) {
                currentTokens -= 1.0;
                return true;
            }
            return false;
        }

        public double getTokens() {
            return currentTokens;
        }
    }


    // HashMap to map Client IDs to their specific Token Buckets
    // ConcurrentHashMap provides thread-safety required for web servers handling simultaneous requests
    private final Map<String, TokenBucket> clientBuckets;

    // Global settings applied to new clients dynamically
    private final long capacity;
    private final long refillTokens;
    private final long refillWindowMs;

    /**
     * Constructs the Master Rate Limiter.
     * @param capacity The maximum burst capacity a user can have
     * @param refillTokens How many tokens generate per time window
     * @param refillWindowMs The time window in milliseconds (e.g. 1 hour = 3,600,000)
     */
    public RateLimiter(long capacity, long refillTokens, long refillWindowMs) {
        this.clientBuckets = new ConcurrentHashMap<>();
        this.capacity = capacity;
        this.refillTokens = refillTokens;
        this.refillWindowMs = refillWindowMs;
    }

    /**
     * Primary API gateway method. O(1) time complexity.
     * @param clientId The ID/IP of the caller.
     * @return true if HTTP 200 OK, false if HTTP 429 Too Many Requests
     */
    public boolean allowRequest(String clientId) {
        // Retrieve or dynamically create a bucket for the client via O(1) Hash Map
        clientBuckets.putIfAbsent(clientId, new TokenBucket(capacity, refillTokens, refillWindowMs));
        TokenBucket bucket = clientBuckets.get(clientId);

        boolean allowed = bucket.allowRequest();
        
        if (allowed) {
            System.out.println("[ALLOWED] Client " + clientId + " request granted. Tokens remaining: " 
                + String.format("%.2f", bucket.getTokens()));
        } else {
            System.out.println("[BLOCKED] Client " + clientId + " hit the Rate Limit! HTTP 429 Too Many Requests.");
        }

        return allowed;
    }


    public static void main(String[] args) throws InterruptedException {
        // Setup: Max 5 tokens. Regenerates 5 tokens every 10,000 ms (10 seconds)
        // Mathematically: 0.5 tokens generated per second.
        RateLimiter api = new RateLimiter(5, 5, 10000);

        String ip = "192.168.1.100";

        System.out.println("--- Rapid Burst (Using predefined capacity) ---");
        for (int i = 1; i <= 5; i++) {
            api.allowRequest(ip);
        }

        System.out.println("\n--- Exceeding the Limit ---");
        // This 6th rapid request will fail, because capacity was 5.
        api.allowRequest(ip);

        System.out.println("\n--- Waiting for Refill (4 seconds) ---");
        // We wait 4 seconds. Rate is 0.5/sec. 4 * 0.5 = 2.0 new tokens should generate.
        Thread.sleep(4000);

        // This will succeed, using 1 of the 2 regenerated tokens.
        api.allowRequest(ip);
        
        // This will succeed, using the last regenerated token.
        api.allowRequest(ip);

        // This should fail, as bucket is empty again.
        api.allowRequest(ip);
    }
}
