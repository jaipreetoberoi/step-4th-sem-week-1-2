package Week2;

import java.util.*;

class Transaction {
    int id;
    int amount;
    String merchant;
    String account;
    long time;

    Transaction(int id, int amount, String merchant, String account, long time) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.account = account;
        this.time = time;
    }
}

public class Problem9 {

    // Classic Two-Sum
    public static List<int[]> findTwoSum(List<Transaction> transactions, int target) {
        HashMap<Integer, Transaction> map = new HashMap<>();
        List<int[]> result = new ArrayList<>();

        for (Transaction t : transactions) {
            int complement = target - t.amount;

            if (map.containsKey(complement)) {
                result.add(new int[]{map.get(complement).id, t.id});
            }

            map.put(t.amount, t);
        }

        return result;
    }

    // Two-Sum within 1 hour window
    public static List<int[]> findTwoSumTimeWindow(List<Transaction> transactions, int target) {
        HashMap<Integer, Transaction> map = new HashMap<>();
        List<int[]> result = new ArrayList<>();

        for (Transaction t : transactions) {
            int complement = target - t.amount;

            if (map.containsKey(complement)) {
                Transaction other = map.get(complement);

                if (Math.abs(t.time - other.time) <= 3600) {
                    result.add(new int[]{other.id, t.id});
                }
            }

            map.put(t.amount, t);
        }

        return result;
    }

    // Duplicate detection
    public static List<String> detectDuplicates(List<Transaction> transactions) {
        HashMap<String, List<String>> map = new HashMap<>();
        List<String> result = new ArrayList<>();

        for (Transaction t : transactions) {
            String key = t.amount + "_" + t.merchant;

            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(t.account);
        }

        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            if (entry.getValue().size() > 1) {
                result.add("Duplicate: " + entry.getKey() + " Accounts: " + entry.getValue());
            }
        }

        return result;
    }

    // K-Sum
    public static void findKSum(List<Transaction> transactions, int k, int target,
                                int start, List<Transaction> current, List<List<Integer>> result) {

        if (k == 0 && target == 0) {
            List<Integer> ids = new ArrayList<>();
            for (Transaction t : current) ids.add(t.id);
            result.add(ids);
            return;
        }

        if (k == 0 || target < 0) return;

        for (int i = start; i < transactions.size(); i++) {
            current.add(transactions.get(i));
            findKSum(transactions, k - 1, target - transactions.get(i).amount,
                    i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }

    public static void main(String[] args) {

        List<Transaction> transactions = new ArrayList<>();

        transactions.add(new Transaction(1, 500, "StoreA", "acc1", 36000));
        transactions.add(new Transaction(2, 300, "StoreB", "acc2", 36900));
        transactions.add(new Transaction(3, 200, "StoreC", "acc3", 37800));
        transactions.add(new Transaction(4, 500, "StoreA", "acc4", 38000));

        System.out.println("Two-Sum:");
        for (int[] pair : findTwoSum(transactions, 500)) {
            System.out.println(pair[0] + " + " + pair[1]);
        }

        System.out.println("\nDuplicates:");
        for (String d : detectDuplicates(transactions)) {
            System.out.println(d);
        }

        System.out.println("\nK-Sum:");
        List<List<Integer>> result = new ArrayList<>();
        findKSum(transactions, 3, 1000, 0, new ArrayList<>(), result);
        System.out.println(result);
    }
}