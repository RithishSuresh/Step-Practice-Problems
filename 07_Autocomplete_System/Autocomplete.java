package autocomplete;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Problem 7: Search Engine Autocomplete System
 * Demonstrates a Trie data structure, utilizing a HashMap for character branching.
 */
public class Autocomplete {

    /**
     * Internal structure representing a single Character node in the Trie.
     */
    private static class TrieNode {
        // O(1) character lookup and dynamic memory allocation using HashMap
        Map<Character, TrieNode> children = new HashMap<>();
        
        boolean isEndOfWord = false;
        int frequency = 0; // Tracks historical search volume
    }

    private final TrieNode root;

    public Autocomplete() {
        this.root = new TrieNode();
    }

    /**
     * Simulated feature: The user officially hits "Enter" to search a word.
     * We add the word to the Trie and increment its global frequency.
     * @param word The complete query
     */
    public void registerSearch(String word) {
        TrieNode current = root;
        // Traverse character by character
        for (char c : word.toCharArray()) {
            current.children.putIfAbsent(c, new TrieNode());
            current = current.children.get(c); // move pointer down
        }
        
        current.isEndOfWord = true;
        current.frequency++;
    }

    /**
     * Simulated feature: The user is currently typing.
     * We fetch the Top 10 queries that complete their current prefix.
     * @param prefix What the user has typed so far
     * @return Ordered list of top queries
     */
    public List<String> getSuggestions(String prefix) {
        TrieNode current = root;

        // Step 1: Navigate to the end of the prefix
        for (char c : prefix.toCharArray()) {
            if (!current.children.containsKey(c)) {
                return new ArrayList<>(); // Prefix doesn't exist
            }
            current = current.children.get(c);
        }

        // Step 2: DFS to gather all descendants
        Map<String, Integer> foundWords = new HashMap<>();
        dfsGatherWords(current, prefix, foundWords);

        // Step 3: Sort by frequency using a Max-Heap PriorityQueue
        PriorityQueue<Map.Entry<String, Integer>> maxHeap = new PriorityQueue<>(
            (a, b) -> b.getValue().compareTo(a.getValue()) // Descending order
        );
        maxHeap.addAll(foundWords.entrySet());

        // Step 4: Extract the top 10
        List<String> top10 = new ArrayList<>();
        int count = 0;
        while (!maxHeap.isEmpty() && count < 10) {
            top10.add(maxHeap.poll().getKey());
            count++;
        }

        return top10;
    }

    /**
     * Recursive Depth-First Search helper that traverses down from the prefix node,
     * assembling string combinations and tracking valid endpoints.
     */
    private void dfsGatherWords(TrieNode node, String currentPrefix, Map<String, Integer> collector) {
        if (node.isEndOfWord) {
            collector.put(currentPrefix, node.frequency);
        }

        for (Map.Entry<Character, TrieNode> child : node.children.entrySet()) {
            dfsGatherWords(child.getValue(), currentPrefix + child.getKey(), collector);
        }
    }

    public static void main(String[] args) {
        Autocomplete engine = new Autocomplete();

        // Simulating millions of historical user searches 
        // (adding 'query' multiple times simulates popularity)
        for (int i = 0; i < 50; i++) engine.registerSearch("apple");
        for (int i = 0; i < 200; i++) engine.registerSearch("amazon");
        for (int i = 0; i < 150; i++) engine.registerSearch("amazon prime");
        for (int i = 0; i < 100; i++) engine.registerSearch("application");
        for (int i = 0; i < 80; i++) engine.registerSearch("apple watch");
        for (int i = 0; i < 15; i++) engine.registerSearch("apply");
        for (int i = 0; i < 2; i++) engine.registerSearch("apologize");

        System.out.println("--- User types 'a' ---");
        System.out.println(engine.getSuggestions("a"));

        System.out.println("\n--- User types 'app' ---");
        System.out.println(engine.getSuggestions("app"));

        System.out.println("\n--- User types 'appl' ---");
        System.out.println(engine.getSuggestions("appl"));
    }
}
