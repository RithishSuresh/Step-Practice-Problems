# Problem 4: Plagiarism Detection System

## 1. Problem Understanding
- **Scenario:** Universities and publishers need to compare a new document against an existing database of documents to see if it was copied. A common approach is converting text into chunks called `N-Grams` (sequences of $N$ consecutive words) and checking for overlaps.
- **Inputs:** Two text documents (Strings) and an integer $N$ (the size of the N-Gram).
- **Outputs:** A similarity score (percentage) between 0% and 100%.
- **Constraints:** Needs to quickly compare thousands of N-Grams. Sequence generation must be sequential, but checking overlap must be $O(1)$.

## 2. Data Structures Used
- **HashMap (`HashMap<String, Integer>`):** We use a HashMap to store the N-Grams of the original document. The key is the exact N-Gram `String`, and the value is its `frequency`. This allows for extremely fast $O(1)$ lookups when validating the suspected document. Counting frequency helps handle repeated phrases accurately.

## 3. System Design Approach
1. **Text Preprocessing:** Both documents are converted to lowercase, and all punctuation is stripped away to ensure matching isn't broken by a comma or capital letter.
2. **N-Gram Generation:** A sliding window of size $N$ moves across the cleaned array of words to generate N-Grams.
3. **Store in HashMap:** Generate N-Grams for Document 1 (the original) and store them in the `HashMap<String, Integer>`, tracking how many times each N-Gram appears.
4. **Compare Document 2:** Generate N-Grams for Document 2 (the suspect). Check if each N-Gram exists in the HashMap. If it exists (and frequency > 0), increment a `matchCount` and decrement the HashMap frequency to avoid double-counting.
5. **Calculate Similarity Percentage:** We use the Sorensen-Dice coefficient formula:
   $Similarity = \frac{2 \times Matches}{Total N-Grams in Doc 1 + Total N-Grams in Doc 2} \times 100$

## 4. Java Implementation
See `PlagiarismDetector.java` for the cleanly structured logic including the sliding window algorithm.

## 5. Time and Space Complexity
- **Time Complexity:**
  - Generating N-Grams for Doc 1: $O(W1)$, where $W1$ is the number of words.
  - Generating N-Grams for Doc 2 and checking HashMap: $O(W2)$, where $W2$ is the number of words.
  - **Total Time Complexity:** $O(W1 + W2)$, linear time.
- **Space Complexity:**
  - $O(W1)$ to store the N-Grams in the HashMap. If Document 1 is extremely large, memory usage scales linearly.

## 6. Example Execution
**Input:**
- Document 1: "The quick brown fox jumps over the lazy dog."
- Document 2: "A quick brown fox jumps around the lazy dog."
- N: 3 (Trigrams)

**Output:**
```
Original Document N-Grams: 7
Suspect Document N-Grams: 7
Matched N-Grams: 2
- 'quick brown fox'
- 'brown fox jumps'
Similarity Score: 28.57%
```

## 7. Possible Improvements (Real-world systems)
- **MinHash and LSH (Locality Sensitive Hashing):** In systems like Turnitin, you are comparing 1 document against 10 billion documents. A HashMap is too memory-intensive. MinHash creates a tiny mathematical "signature" representing the document, and LSH groups similar signatures together to avoid $O(N^2)$ universal comparisons.
- **Rabin-Karp Rolling Hash:** Instead of storing actual string objects (which takes up a lot of heap space), store mathematical 64-bit integer hashes of the N-grams. A rolling hash allows generating the next sequence's hash in $O(1)$ time by subtracting the first character and adding the newest character.
- **Stop Words:** Filter out "the", "and", "a" before generating N-grams to focus on substantive vocabulary overlap.
