package Week2;

import java.util.*;

class TrieNode {
    HashMap<Character, TrieNode> children = new HashMap<>();
    boolean isEnd = false;
}

public class Problem7 {

    private TrieNode root = new TrieNode();
    private HashMap<String, Integer> queryFrequency = new HashMap<>();

    public void insertQuery(String query) {

        queryFrequency.put(query, queryFrequency.getOrDefault(query, 0) + 1);

        TrieNode node = root;
        for (char c : query.toCharArray()) {
            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
        }
        node.isEnd = true;
    }

    public List<String> search(String prefix) {

        TrieNode node = root;

        for (char c : prefix.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return new ArrayList<>();
            }
            node = node.children.get(c);
        }

        List<String> results = new ArrayList<>();
        dfs(node, prefix, results);

        PriorityQueue<String> pq = new PriorityQueue<>(
                (a, b) -> queryFrequency.get(b) - queryFrequency.get(a)
        );

        pq.addAll(results);

        List<String> topResults = new ArrayList<>();
        int count = 0;

        while (!pq.isEmpty() && count < 10) {
            topResults.add(pq.poll());
            count++;
        }

        return topResults;
    }

    private void dfs(TrieNode node, String prefix, List<String> results) {

        if (node.isEnd) {
            results.add(prefix);
        }

        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            dfs(entry.getValue(), prefix + entry.getKey(), results);
        }
    }

    public void updateFrequency(String query) {
        queryFrequency.put(query, queryFrequency.getOrDefault(query, 0) + 1);
    }

    public static void main(String[] args) {

        Problem7 system = new Problem7();

        system.insertQuery("java tutorial");
        system.insertQuery("javascript");
        system.insertQuery("java download");
        system.insertQuery("java tutorial");

        System.out.println(system.search("jav"));

        system.updateFrequency("java 21 features");
        system.updateFrequency("java 21 features");
        system.updateFrequency("java 21 features");
    }
}