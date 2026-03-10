package Week1;

import java.util.*;

public class problem4 {

    private HashMap<String, Set<String>> ngramIndex = new HashMap<>();
    private int N = 5;

    public List<String> generateNGrams(String text) {
        List<String> ngrams = new ArrayList<>();
        String[] words = text.toLowerCase().split("\\s+");

        for (int i = 0; i <= words.length - N; i++) {
            StringBuilder gram = new StringBuilder();
            for (int j = 0; j < N; j++) {
                gram.append(words[i + j]).append(" ");
            }
            ngrams.add(gram.toString().trim());
        }

        return ngrams;
    }

    public void addDocument(String documentId, String text) {
        List<String> ngrams = generateNGrams(text);

        for (String gram : ngrams) {
            ngramIndex.putIfAbsent(gram, new HashSet<>());
            ngramIndex.get(gram).add(documentId);
        }
    }

    public void analyzeDocument(String documentId, String text) {

        List<String> ngrams = generateNGrams(text);
        HashMap<String, Integer> matchCount = new HashMap<>();

        for (String gram : ngrams) {
            if (ngramIndex.containsKey(gram)) {

                for (String doc : ngramIndex.get(gram)) {
                    matchCount.put(doc, matchCount.getOrDefault(doc, 0) + 1);
                }
            }
        }

        System.out.println("Extracted " + ngrams.size() + " n-grams");

        for (Map.Entry<String, Integer> entry : matchCount.entrySet()) {

            String docId = entry.getKey();
            int matches = entry.getValue();

            double similarity = (matches * 100.0) / ngrams.size();

            System.out.println("Found " + matches + " matching n-grams with \"" + docId + "\"");
            System.out.println("Similarity: " + String.format("%.2f", similarity) + "%");

            if (similarity > 60) {
                System.out.println("PLAGIARISM DETECTED");
            }
        }
    }

    public static void main(String[] args) {

        problem4 detector = new problem4();

        String doc1 = "machine learning is a field of artificial intelligence that focuses on data";
        String doc2 = "machine learning is a field that studies algorithms and data patterns";

        detector.addDocument("essay_089.txt", doc1);

        detector.analyzeDocument("essay_123.txt", doc2);
    }
}