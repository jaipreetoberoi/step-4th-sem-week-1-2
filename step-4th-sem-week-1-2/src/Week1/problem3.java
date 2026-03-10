package Week1;

import java.util.*;

class DNSEntry {
    String domain;
    String ipAddress;
    long expiryTime;

    DNSEntry(String domain, String ipAddress, long ttlSeconds) {
        this.domain = domain;
        this.ipAddress = ipAddress;
        this.expiryTime = System.currentTimeMillis() + (ttlSeconds * 1000);
    }

    boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}

public class problem3 {

    private int capacity;
    private LinkedHashMap<String, DNSEntry> cache;
    private int hits = 0;
    private int misses = 0;

    public problem3(int capacity) {
        this.capacity = capacity;

        cache = new LinkedHashMap<String, DNSEntry>(capacity, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                return size() > problem3.this.capacity;
            }
        };
    }

    public synchronized String resolve(String domain) {

        DNSEntry entry = cache.get(domain);

        if (entry != null) {
            if (!entry.isExpired()) {
                hits++;
                return "Cache HIT → " + entry.ipAddress;
            } else {
                cache.remove(domain);
            }
        }

        misses++;
        String ip = queryUpstreamDNS(domain);
        cache.put(domain, new DNSEntry(domain, ip, 300));

        return "Cache MISS → " + ip;
    }

    private String queryUpstreamDNS(String domain) {
        Random r = new Random();
        return "172.217.14." + (r.nextInt(200) + 1);
    }

    public void cleanExpiredEntries() {
        Iterator<Map.Entry<String, DNSEntry>> it = cache.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, DNSEntry> entry = it.next();

            if (entry.getValue().isExpired()) {
                it.remove();
            }
        }
    }

    public void getCacheStats() {
        int total = hits + misses;
        double hitRate = total == 0 ? 0 : (hits * 100.0) / total;

        System.out.println("Cache Hits: " + hits);
        System.out.println("Cache Misses: " + misses);
        System.out.println("Hit Rate: " + hitRate + "%");
    }

    public static void main(String[] args) {

        problem3 dnsCache = new problem3(5);

        System.out.println(dnsCache.resolve("google.com"));
        System.out.println(dnsCache.resolve("google.com"));
        System.out.println(dnsCache.resolve("facebook.com"));
        System.out.println(dnsCache.resolve("google.com"));

        dnsCache.getCacheStats();
    }
}