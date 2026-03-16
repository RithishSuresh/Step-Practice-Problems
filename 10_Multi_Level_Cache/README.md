# Problem 10: Multi-Level Cache System

## 1. Problem Understanding
- **Scenario:** Modern computer architectures use multiple layers of caching ($L1$ CPU Cache, $L2$, $L3$, Main RAM, SSD/Disk). Caches closer to the CPU are blazing fast but hold very little data. Deeper caches are slower but hold much more. When data is requested, we check L1 first. If it's a "Miss", we check L2. Over time, frequently accessed data naturally bubbles up to L1, while unused data gets evicted downwards, eventually resting in the final permanent Database.
- **Inputs:** A key/value to store or fetch.
- **Outputs:** Value retrieved, plus system logs showing which Cache Level fulfilled the request.
- **Constraints:** O(1) reads/writes. Automatic cascading eviction downward.

## 2. Data Structures Used
- **LinkedHashMap (`LinkedHashMap<String, String>`):** We use this structure configured with `accessOrder = true` to serve as our dynamic LRU caches (Both L1 and L2). `LinkedHashMap` allows an elegant architectural pattern: by overriding `removeEldestEntry`, we can automatically trigger a "Cascade" where data evicted from L1 immediately falls into L2, and data evicted from L2 falls into L3.
- **HashMap (`HashMap<String, String>`):** Used to mock the static L3 Database, featuring unlimited size.

## 3. System Design Approach
1. **Cache Initialization (The Cascade):** Set up three structures: `L1Cache`, `L2Cache`, and `L3Database`.
   - `L1Cache` has extremely small capacity. When it exceeds capacity, its `removeEldestEntry` method pushes the evicted entry into `L2Cache` before deleting itself.
   - `L2Cache` is slightly larger. When it exceeds capacity, its `removeEldestEntry` method pushes the evicted entry into the permanent `L3Database`.
2. **Put Operation:** Writes immediately target the `L1Cache`. If this forces an eviction, the cascade described above happens automatically.
3. **Get Operation (The Bubble-Up Effect):**
   - Check `L1Cache`. If found, hit! Return value.
   - If missed, check `L2Cache`. If found, **Promote it**: copy the value up into `L1Cache` and remove it from `L2Cache`. Return value.
   - If missed, check `L3Database`. If found, promote it into `L1Cache`. Return value.

## 4. Java Implementation
See `MultiLevelCache.java` for an elegant recursive-eviction model utilizing Anonymous Inner Classes for the `LinkedHashMap`.

## 5. Time and Space Complexity
- **Time Complexity:** 
  - $O(1)$ for both Reads and Writes, regardless of whether a Write triggers a cascade of evictions, as Hash table operations remain strictly constant.
- **Space Complexity:**
  - $O(L1_{Cap} + L2_{Cap} + V)$, where $L1_{Cap}$ and $L2_{Cap}$ are statically bounded constants, and $V$ is the limitless volume of the L3 Database.

## 6. Example Execution
**Input:**
L1 Cap: 2, L2 Cap: 3.
- Put "A","B","C"
- Get "B"
- Put "D","E"
- Get "A"

**Output:**
```
[PUT L1] A
[PUT L1] B
[PUT L1] C -> L1 Full! Cascading 'A' down to L2.
[FETCH] B found in L1 Cache.
[PUT L1] D -> L1 Full! Cascading 'C' down to L2.
[PUT L1] E -> L1 Full! Cascading 'B' down to L2.
           -> L2 Full! Cascading 'A' down to L3 (Database).
[FETCH] A missed L1, missed L2, found in L3. Promoting to L1!
           -> Promoting 'A' to L1 causes 'D' to fall to L2!
```

## 7. Possible Improvements (Real-world systems)
- **Concurrent Maps / Thread Safety:** Because the eviction of L1 writes to L2, locking strategies must be extremely careful to avoid Deadlocks. Granular lock striping (like ConcurrentHashMap uses internally) is mandated.
- **Write-Through vs Write-Back vs Write-Around:** Our example writes only to L1 exactly, acting like "Write-Back". Hardware usually employs "Write-Through" where saving data writes it safely to L1 and L3 simultaneously so it survives a power outage.
- **Frequency Caching (LFU):** Sometimes LRU kicks out data accessed million times just because a user didn't request it in the last 10 minutes. Real systems augment LRU with LFU (Least Frequently Used) algorithms, notably TinyLFU (Window-TinyLFU) utilized by Caffeine cache.
