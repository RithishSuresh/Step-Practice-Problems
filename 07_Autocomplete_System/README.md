# Problem 7: Search Engine Autocomplete System

## 1. Problem Understanding
- **Scenario:** When a user begins typing a query into a search bar (e.g., "app"), the system must instantly recommend popular completed queries (e.g., "apple", "app store", "apply"). These recommendations must be ranked by their historical frequency (how often other people searched for them).
- **Inputs:** A prefix string (what the user typed so far).
- **Outputs:** A list of the Top 10 complete search queries ranked by frequency.
- **Constraints:** Retrieving suggestions must be lighting-fast because it executes continuously on every keystroke. 

## 2. Data Structures Used
- **Trie (Prefix Tree):** A specialized tree data structure where each node represents a single character of a string. This is heavily optimized for prefix matching. Unlike a HashMap where "apple" is totally separate from "app", in a Trie, "apple" is stored directly as a branch extending from "app". Finding a prefix takes $O(L)$ where $L$ is the prefix length.
- **HashMap (`HashMap<Character, TrieNode>`):** Embedded inside each TrieNode. Instead of an array of 26 letters (which wastes memory if Unicode/special characters are used), the HashMap efficiently maps precisely the child characters that exist.
- **PriorityQueue (Max-Heap):** Used dynamically during retrieval to rank the discovered vocabulary words by their `frequency` and return exactly the top 10.

## 3. System Design Approach
1. **Building the TrieNode:**
   - Contains a `HashMap<Character, TrieNode> children`.
   - A `boolean isEndOfWord` flag to denote a valid completed word.
   - An `int frequency` variable denoting how many times this full word was requested.
2. **Inserting/Updating a Query:**
   - Iterate through the characters of the query. Navigate down the Trie via the `children` HashMap, creating new nodes if edges don't exist yet.
   - Upon the final character node, set `isEndOfWord = true` and increment `frequency`.
3. **Retrieving Autocomplete Suggestions (The Prefix Search):**
   - **Step 1:** Trace the given prefix down the Trie. If the path abruptly ends, return an empty list (no words share this prefix).
   - **Step 2:** From the prefix's ending node, perform a recursive **Depth First Search (DFS)** to explore every possible branch beneath it.
   - **Step 3:** Every time a node with `isEndOfWord == true` is encountered, add the assembled word and its `frequency` to a list.
   - **Step 4:** Insert the list into a `PriorityQueue` (Max-Heap) sorted by frequency, and pop off the Top 10 results.

## 4. Java Implementation
See `Autocomplete.java` for the integrated implementation of the Trie, HashMap, and DFS logic.

## 5. Time and Space Complexity
- **Time Complexity:**
  - **Insertion:** $O(L)$, where $L$ is the length of the string.
  - **Autocomplete Retrieval:** $O(L + V \log V)$, where $L$ is traversing down the prefix, and $V$ is traversing all descendant Nodes in the DFS. Sorting the results takes $V \log V$ time.
- **Space Complexity:**
  - $O(W \cdot L)$ worst case, where $W$ is the number of words, and $L$ is the average length of the words. Nodes sharing the same prefix effectively share memory, making it highly space-efficient for large lexicons.

## 6. Example Execution
**Input:**
- User searches "apple" 5 times
- User searches "app store" 10 times
- User searches "apply" 2 times
- User searches "ape" 1 time
- Suggest for: "app"

**Output:**
```
1. app store (10 searches)
2. apple (5 searches)
3. apply (2 searches)
```
*(Notice "ape" is omitted since it doesn't match the prefix "app".)*

## 7. Possible Improvements (Real-world systems)
- **Node Caching (The Google Approach):** A production search engine doesn't perform $O(V)$ DFS traversals continuously. Instead, *every node* in the Trie caches its top 10 most frequent descendant words. Caching changes memory from $O(W \cdot L)$ to $O(W \cdot L \cdot 10)$, but makes retrieval absolutely constant $O(L)$, eliminating DFS.
- **Redis / Distributed Tries:** Memory limitations mean a global Trie cannot fit in RAM. The logic is partitioned across distributed servers. Redis has plugins like `RediSearch` that natively support Autocomplete indexing using tries.
