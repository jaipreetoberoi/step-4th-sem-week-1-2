package Week1;

import java.util.*;

public class Problem2 {

    private HashMap<String, Integer> stockMap = new HashMap<>();
    private HashMap<String, LinkedHashMap<Integer, Boolean>> waitingList = new HashMap<>();

    public void addProduct(String productId, int stock) {
        stockMap.put(productId, stock);
        waitingList.put(productId, new LinkedHashMap<>());
    }

    public int checkStock(String productId) {
        return stockMap.getOrDefault(productId, 0);
    }

    public synchronized String purchaseItem(String productId, int userId) {

        int stock = stockMap.getOrDefault(productId, 0);

        if (stock > 0) {
            stockMap.put(productId, stock - 1);
            return "Success, " + (stock - 1) + " units remaining";
        } else {
            LinkedHashMap<Integer, Boolean> queue = waitingList.get(productId);
            queue.put(userId, true);
            int position = queue.size();
            return "Added to waiting list, position #" + position;
        }
    }

    public void showWaitingList(String productId) {
        LinkedHashMap<Integer, Boolean> queue = waitingList.get(productId);
        System.out.println("Waiting List: " + queue.keySet());
    }

    public static void main(String[] args) {

        Problem2 manager = new Problem2();

        manager.addProduct("IPHONE15_256GB", 100);

        System.out.println("Stock: " + manager.checkStock("IPHONE15_256GB"));

        System.out.println(manager.purchaseItem("IPHONE15_256GB", 12345));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 67890));

        for (int i = 0; i < 100; i++) {
            manager.purchaseItem("IPHONE15_256GB", 20000 + i);
        }

        System.out.println(manager.purchaseItem("IPHONE15_256GB", 99999));

        manager.showWaitingList("IPHONE15_256GB");
    }
}
