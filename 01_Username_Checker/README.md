# Problem 1: Social Media Username Availability Checker

## 1. Problem Understanding
- **Scenario:** When a user signs up on a social media platform, they must pick a unique username. We need a system to check if their desired username is available in $O(1)$ time. If it is already taken, we must suggest alternative usernames. We also need to track which taken usernames are attempted the most for analytics.
- **Inputs:** A string representing the desired username.
- **Outputs:** A boolean indicating availability. If taken, a list of suggested alternatives.
- **Constraints:** The availability check must be extremely fast ($O(1)$ time complexity). Memory is limited, but we assume it fits in RAM for this exercise.

## 2. Data Structures Used
- **HashSet (Java's `HashSet<String>`):** Used to store all registered usernames. A HashSet allows us to check for the existence of a username (the availability check) in $O(1)$ average time complexity.
- **HashMap (Java's `HashMap<String, Integer>`):** Used to track the number of times a taken username has been attempted. The key is the taken username, and the value is the attempt count. This allows $O(1)$ updates and lookups.

## 3. System Design Approach
1. **Registration/Check:** When a username is requested, check if it exists in the `HashSet`.
   - If **not present**, no collision occurs. The username is added to the `HashSet`, indicating successful registration.
   - If **present**, we have a collision (username taken). We increment the attempt count for this username in the `HashMap`.
2. **Alternative Suggestions:** If the username is taken, we generate alternatives by appending numbers (e.g., `username1`, `username2`) and check their availability using the `HashSet` until we find available ones.
3. **Tracking Most Attempted:** We can iterate through the `HashMap` entries to find the usernames with the highest attempt counts or maintain a `PriorityQueue` if we need top-k attempts dynamically.

## 4. Java Implementation
See `UsernameChecker.java` for the complete documented implementation.

## 5. Time and Space Complexity
- **Time Complexity:**
  - **Best Case:** $O(1)$ for checking availability and registering an available username.
  - **Average Case:** $O(1)$ due to the constant time performance of HashSet operations (assuming a good hash function with minimal collisions).
  - **Worst Case:** $O(N)$ if the hash table faces numerous collisions and degenerates into a Linked List (though Java 8+ handles this using balanced trees for bins, making it $O(\log N)$). Generating suggestions takes $O(K)$, where $K$ is the number of suggestions needed.
- **Space Complexity:**
  - **Worst Case:** $O(N + M)$ where $N$ is the number of registered users stored in the HashSet, and $M$ is the number of unique taken usernames tracked in the HashMap.

## 6. Example Execution
**Input:**
1. Register "john_doe"
2. Register "jane"
3. Register "john_doe" (taken)
4. Register "john_doe" (taken)

**Output:**
1. "john_doe" registered successfully.
2. "jane" registered successfully.
3. "john_doe" is taken! Suggestions: [john_doe1, john_doe2, john_doe3]
4. "john_doe" is taken! Suggestions: [john_doe1, john_doe2, john_doe3]
Most attempted taken username: "john_doe" (Attempted 2 times)

## 7. Possible Improvements (Real-world systems)
- **Bloom Filters:** In a real distributed system with billions of users, checking a database or a massive Hash Table is expensive. A Bloom Filter (probabilistic data structure) is deployed in front to instantly answer "definitely not taken" or "possibly taken". Only if it says "possibly taken" do we check the actual database.
- **Distributed Caching:** Using Redis or Memcached clusters to store the HashSet in memory across multiple servers, ensuring sub-millisecond lookups.
- **Sharding:** Partition the usernames alphabetically or by consistent hashing across multiple database shards to distribute the load across multiple nodes.
