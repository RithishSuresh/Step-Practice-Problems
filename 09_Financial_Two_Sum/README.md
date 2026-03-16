# Problem 9: Financial Transactions Two-Sum Variants

## 1. Problem Understanding
- **Scenario:** A financial security system needs algorithms to analyze logs of bank transactions. First, it must find two specific transactions that perfectly sum up to a target amount (e.g., to reconcile an accounting error). Next, it needs a K-Sum variant to find 3 (or $K$) transactions summing to a target. Finally, it must detect identical duplicate transactions (fraud/anomalies) in real-time.
- **Inputs:** Array of transaction amounts, target sum amount.
- **Outputs:** Indices of the transactions, or detection alerts.
- **Constraints:** Two-Sum must be $O(N)$. Duplicate detection must be $O(1)$. 

## 2. Data Structures Used
- **HashMap (`HashMap<Integer, Integer>`):** Used for Classic Two-Sum. We map the transaction `Amount` to its original Array `Index`. This allows calculating the missing math complement and looking it up in $O(1)$ time. 
- **HashSet (`HashSet<String>`):** Used for Duplicate Anomaly detection. We convert complex transaction objects into a unique composite String "signature" and check if the signature already exists in the set.
- **Pointers & Sorting (Arrays):** For K-Sum (like 3-Sum), HashMaps become mathematically convoluted and memory heavy. Instead, we use an in-place Array Sorting algorithm ($O(N \log N)$) combined with a Two-Pointer algorithmic pattern.

## 3. System Design Approach
1. **Classic Two-Sum (The HashMap Pattern):**
   - Iterate through the transaction array.
   - For each amount, calculate what amount is needed to reach the target: `Complement = Target - Current_Amount`.
   - Check if the `Complement` exists in the HashMap. If Yes, we found our two transactions!
   - If No, add the `Current_Amount` and its array `Index` to the HashMap and continue iterating.
2. **K-Sum (The Sorting + Two-Pointer Pattern):**
   - Sort the array ($O(N \log N)$ time).
   - Iterate through the array with pointer `i`.
   - Setup a `Left` pointer at `i + 1`, and a `Right` pointer at the end of the array `N - 1`.
   - Sum the three pointers. If the sum is too small, move `Left` right. If too big, move `Right` left. If perfect, record it!
3. **Duplicate Detection (The HashSet Pattern):**
   - Given a Transaction object (AccountID, Timestamp, Amount).
   - Generate a deterministic string key: `"ACC123_1640000_500"`.
   - `set.add()` returns `false` if the exact same signature was already processed. Flag as fraud immediately!

## 4. Java Implementation
See `FinancialAnalysis.java` for the three distinct algorithms implemented cleanly.

## 5. Time and Space Complexity
- **Time Complexity:**
  - **Two-Sum:** $O(N)$ single pass through the array.
  - **Three-Sum:** $O(N^2)$ due to the nested Two-Pointer search.
  - **Duplicate Detection:** $O(1)$ lookup per incoming transaction.
- **Space Complexity:**
  - **Two-Sum:** $O(N)$ for the HashMap.
  - **Three-Sum:** $O(1)$ (or $O(\log N)$ depending on the language's sorting algorithm). We don't allocate extra memory for data structures here.
  - **Duplicate Detection:** $O(N)$ to store the historical HashSet.

## 6. Example Execution
**Input:**
- Transactions: `[100, -500, 250, 400, 150]`
- Reconcile Target: `350`

**Output:**
```
--- Classic Two-Sum (Target: 350) ---
Found error! Transaction index 0 (100) + Transaction index 2 (250) = 350

--- Duplicate Detection ---
Processing: ACC99_$50.00
Processing: ACC99_$50.00
[ALERT] Fraud/Duplicate detected for signature: ACC99_$50.00
```

## 7. Possible Improvements (Real-world systems)
- **Time-Windowed Deduplication:** A HashSet tracking duplicates will eventually consume all RAM and crash. In real finance, we only care about exact duplicates occurring within the same 5-minute window. We would use Redis with a 5-minute TTL (Time To Live), or a `LinkedHashMap` that naturally evicts entries older than 5 minutes.
- **Fuzzy Math:** Financial floats differ (e.g., $100.0001$ vs $100.00$). Real algorithms round or use `BigDecimal` before hashing.
