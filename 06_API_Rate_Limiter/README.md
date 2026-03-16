# Problem 6: Distributed API Rate Limiter (Token Bucket)

## 1. Problem Understanding
- **Scenario:** Public APIs must protect themselves from being overwhelmed by a single client (accidental loops, malicious DDoS). A Rate Limiter restricts the number of requests a client can make within a specific time window.
- **Inputs:** Client ID (IP Address or API Key).
- **Outputs:** True (Request Allowed) or False (Request Blocked/HTTP 429 Too Many Requests).
- **Constraints:** Needs to support millions of dynamic clients, requiring $O(1)$ lookups. The token state must smoothly reset over time (e.g., limits resetting hourly).

## 2. Data Structures Used
- **HashMap (`ConcurrentHashMap<String, TokenBucket>`):** The primary data structure maps a specific `clientId` to their distinct `TokenBucket` state object. We use a Hash table for immediate $O(1)$ client state retrieval. (Note: standard `HashMap` is not thread-safe, so `ConcurrentHashMap` is preferred for modern web servers).

## 3. System Design Approach (Token Bucket Algorithm)
The Token Bucket algorithm is an industry standard approach for rate limiting. 
1. **The Bucket Concept:** Imagine each user has a literal bucket. The bucket can hold up to $C$ tokens (capacity).
2. **Tokens Refilling:** Every second/minute/hour, tokens are continuously trickled into the bucket at a steady rate $R$. The bucket can never overflow past $C$.
3. **Lazy Refilling (The Trick):** We don't run a global background thread that constantly updates millions of buckets. Instead, we use **Lazy Assessment**. When a user makes a request, we calculate how much time has passed since their *last* request, and mathematically calculate how many tokens they *should* have gained.
4. **Processing the Request:**
   - Client makes a request -> Fetch their `TokenBucket` from the HashMap.
   - Refill bucket based on elapsed time.
   - If `tokens >= 1`: Subtract 1 token and allow the request in $O(1)$.
   - If `tokens < 1`: Reject the request ($O(1)$).

## 4. Java Implementation
See `RateLimiter.java` for a complete, thread-safe implementation of the Token Bucket algorithm using a ConcurrentHashMap.

## 5. Time and Space Complexity
- **Time Complexity:** 
  - $O(1)$ for every `allowRequest()` check. The hash map retrieval and mathematical refill calculation happen in constant time.
- **Space Complexity:** 
  - $O(N)$ where $N$ is the number of distinct clients tracked by the API.

## 6. Example Execution
**Input (Limit 5 requests per 10 seconds):**
- Client A makes 5 rapid requests.
- Client A makes a 6th request immediately.
- Client A waits 4 seconds and attempts a request.

**Output:**
```
[1] Allowed - Tokens remaining: 4.0
[2] Allowed - Tokens remaining: 3.0
[3] Allowed - Tokens remaining: 2.0
[4] Allowed - Tokens remaining: 1.0
[5] Allowed - Tokens remaining: 0.0
[6] Blocked - HTTP 429 Too Many Requests.
... Client A waits 4 seconds ...
[7] Allowed - Tokens remaining: 1.0 (Refilled 2 tokens over 4 seconds, used 1)
```

## 7. Possible Improvements (Real-world systems)
- **Redis Integration:** In a distributed architecture (10 backend servers), an in-memory Java HashMap only tracks rate limits per server. A user balancing across 10 servers would get 10x the allowed rate limit. Real rate limiters store the Token Bucket states in Redis so all backend servers share the exact same truth.
- **Leaky Bucket Algorithm:** Instead of a Token Bucket (which allows sudden bursts of traffic up to the capacity), Leaky Bucket enforces a strictly smooth, uniform rate of requests.
- **Sliding Window Log:** Another algorithm that tracks exact millisecond timestamps of requests to prevent edge-case abuse found in Fixed Window counters.
