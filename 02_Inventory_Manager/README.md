# Problem 2: E-commerce Flash Sale Inventory Manager

## 1. Problem Understanding
- **Scenario:** During a flash sale, thousands of users rush to buy limited-stock items simultaneously. The system needs to accurately manage inventory to prevent overselling (selling more items than available). When an item goes out of stock, users who attempt to buy it should be placed on a waiting list in a First-In-First-Out (FIFO) manner. If a buyer cancels their order, the item should automatically go to the first person on the waitlist.
- **Inputs:** Product ID, User ID, Action (Buy / Cancel).
- **Outputs:** Purchase success status, Waitlist position.
- **Constraints:** Heavy concurrency requires fast lookups ($O(1)$) and structured waitlists.

## 2. Data Structures Used
- **HashMap (`HashMap<String, Integer>`):** Manages the core inventory stock. The key is the `productId` and the value is the `stockCount`. Providing $O(1)$ inventory lookups and updates.
- **HashMap of Queues (`HashMap<String, Queue<String>>`):** Manages the waitlists per product. The key is the `productId`, and the value is a Queue (`LinkedList` functioning as a Queue) of `userId`s. A Queue natively provides the FIFO behavior required ($O(1)$ Enqueue, $O(1)$ Dequeue).

## 3. System Design Approach
1. **Initialize Inventory:** Populate the inventory `HashMap` with product IDs and their initial stock levels.
2. **Purchase Item (Buy):**
   - Lookup stock in $O(1)$.
   - If `stock > 0`, decrement stock in the `HashMap`, process purchase immediately.
   - If `stock == 0` (or absent depending on setup), add the User ID to the Waitlist `Queue` for that product.
3. **Cancel Item (Restock):**
   - When an item is restocked, immediately check the Waitlist `Queue`.
   - If the waitlist for that product is **not empty**, `poll()` the first User ID from the Queue and award them the item.
   - If the waitlist is **empty**, simply increment the stock counter in the inventory map.

*Note: In a true Java backend, we would use `ConcurrentHashMap` and thread synchronization (e.g., `synchronized` blocks or `ReentrantLock`s) to prevent race conditions during high concurrency.*

## 4. Java Implementation
See `InventoryManager.java` for the implementation. It includes basic synchronization for Thread-Safety, mimicking real-world constraints.

## 5. Time and Space Complexity
- **Time Complexity:**
  - **Best/Average Case:** $O(1)$ for buying an item, adding to the waitlist, and canceling/restocking an item, because Maps and Queues both offer constant-time primary operations.
  - **Worst Case:** $O(1)$ ignoring hashing collisions. If Hash collisions occur (rare), it might degrade to $O(\log N)$ or $O(N)$ for map retrieval.
- **Space Complexity:**
  - **Best/Average/Worst Case:** $O(P + U)$ where $P$ is the number of distinct products in the inventory and $U$ is the total numbers of users globally waiting in the queues.

## 6. Example Execution
**Input:**
- Add "PhoneX" with stock 2.
- User A buys "PhoneX" (Success)
- User B buys "PhoneX" (Success)
- User C buys "PhoneX" (Waitlist)
- User D buys "PhoneX" (Waitlist)
- User A cancels "PhoneX" (User C automatically gets it from Waitlist)

**Output:**
```
Stock added for PhoneX: 2
User A successfully purchased PhoneX!
User B successfully purchased PhoneX!
PhoneX is out of stock! User C added to waitlist.
PhoneX is out of stock! User D added to waitlist.
User A canceled their order for PhoneX.
Waitlist processed! User C automatically received PhoneX.
```

## 7. Possible Improvements (Real-world systems)
- **Redis & Lua Scripting:** In modern microservices, inventory decrement shouldn't occur exclusively in JVM memory. Commands like Redis' `DECRBY` with Lua scripts guarantee atomic decrement and prevent overselling across distributed servers.
- **Message Brokers:** The Waitlist is better managed via a message broker like **Apache Kafka** or **RabbitMQ**. Users enter a queue topic, and a consumer picks them up in FIFO order if the transaction is pending.
- **Expiration / TTL on Carts:** In a real flash sale, buying means "reserving in cart". The reservation should have a TTL (e.g., 10 minutes to pay). If they don't fulfill payment, the stock is released.
