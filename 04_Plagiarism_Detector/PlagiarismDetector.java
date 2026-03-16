package plagiarismdetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Problem 4: Plagiarism Detection System
 * Breaks text into N-Grams and uses a HashMap to efficiently detect overlap percentage.
 */
public class PlagiarismDetector {

    /**
     * Cleans text and generates a list of N-Grams (sliding window of N words)
     */
    private static List<String> generateNGrams(String text, int n) {
        List<String> nGrams = new ArrayList<>();
        
        // Remove punctuation and convert to lower case for normalization
        String cleanedText = text.replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase();
        String[] words = cleanedText.split("\\s+");

        // Generate sliding window of N words
        for (int i = 0; i <= words.length - n; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < n; j++) {
                sb.append(words[i + j]);
                if (j < n - 1) sb.append(" ");
            }
            nGrams.add(sb.toString().trim());
        }

        return nGrams;
    }

    /**
     * Detects the plagiarism percentage between an original document and a suspected one.
     * Uses Sorensen-Dice coefficient formula.
     * 
     * @param originalText The source material
     * @param suspectText  The text being checked for plagiarism
     * @param n            The N-Gram size (e.g., 3 for Trigram)
     * @return A percentage double between 0.0 and 100.0
     */
    public static double checkPlagiarism(String originalText, String suspectText, int n) {
        List<String> origNGrams = generateNGrams(originalText, n);
        List<String> suspNGrams = generateNGrams(suspectText, n);

        int totalOrig = origNGrams.size();
        int totalSusp = suspNGrams.size();

        if (totalOrig == 0 || totalSusp == 0) return 0.0;

        // Populate HashMap for O(1) matching. Map stores N-Gram -> Frequency 
        Map<String, Integer> origMap = new HashMap<>();
        for (String gram : origNGrams) {
            origMap.put(gram, origMap.getOrDefault(gram, 0) + 1);
        }

        int matches = 0;
        List<String> matchedPhrases = new ArrayList<>();

        // Check Suspect array against the O(1) HashMap
        for (String gram : suspNGrams) {
            if (origMap.containsKey(gram) && origMap.get(gram) > 0) {
                matches++;
                matchedPhrases.add(gram);
                
                // Decrement to prevent duplicate counting if Suspect repeats the phrase
                origMap.put(gram, origMap.get(gram) - 1);
            }
        }

        System.out.println("Original Document N-Grams: " + totalOrig);
        System.out.println("Suspect Document N-Grams: " + totalSusp);
        System.out.println("Matched N-Grams (" + matches + "):");
        
        // Print up to 3 matches for brevity
        for (int i = 0; i < Math.min(3, matchedPhrases.size()); i++) {
            System.out.println(" - '" + matchedPhrases.get(i) + "'");
        }
        if (matchedPhrases.size() > 3) {
            System.out.println(" ... and " + (matchedPhrases.size() - 3) + " more.");
        }

        // Calculate Sorensen-Dice index as a percentage
        double similarity = (2.0 * matches) / (totalOrig + totalSusp) * 100.0;
        
        return similarity;
    }

    public static void main(String[] args) {
        String originalDoc = "The quick brown fox jumps over the lazy dog. Programming is a fun and analytical process.";
        String copiedDoc = "A quick brown fox jumps around the lazy dog. Writing code is a fun and analytical process.";

        System.out.println("--- Trigram (3-word) Plagiarism Analysis ---");
        double score = checkPlagiarism(originalDoc, copiedDoc, 3);
        System.out.printf("Similarity Score: %.2f%%\n", score);
        
        if (score > 30.0) {
            System.out.println("[ALERT] High likelihood of plagiarism detected.");
        }
    }
}
