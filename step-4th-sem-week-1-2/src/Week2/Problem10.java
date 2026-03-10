package Week2;

import java.util.*;

class VideoData {
    String videoId;
    String content;

    VideoData(String videoId, String content) {
        this.videoId = videoId;
        this.content = content;
    }
}

public class Problem10 {

    private final int L1_CAPACITY = 10000;
    private final int L2_CAPACITY = 100000;
    private final int PROMOTION_THRESHOLD = 3;

    private LinkedHashMap<String, VideoData> L1;
    private LinkedHashMap<String, VideoData> L2;
    private HashMap<String, VideoData> L3Database;
    private HashMap<String, Integer> accessCount;

    private int L1Hits = 0;
    private int L2Hits = 0;
    private int L3Hits = 0;

    public Problem10() {

        L1 = new LinkedHashMap<String, VideoData>(L1_CAPACITY, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
                return size() > L1_CAPACITY;
            }
        };

        L2 = new LinkedHashMap<String, VideoData>(L2_CAPACITY, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
                return size() > L2_CAPACITY;
            }
        };

        L3Database = new HashMap<>();
        accessCount = new HashMap<>();
    }

    public VideoData getVideo(String videoId) {

        if (L1.containsKey(videoId)) {
            L1Hits++;
            return L1.get(videoId);
        }

        if (L2.containsKey(videoId)) {
            L2Hits++;
            VideoData video = L2.get(videoId);

            int count = accessCount.getOrDefault(videoId, 0) + 1;
            accessCount.put(videoId, count);

            if (count >= PROMOTION_THRESHOLD) {
                L1.put(videoId, video);
            }

            return video;
        }

        if (L3Database.containsKey(videoId)) {
            L3Hits++;
            VideoData video = L3Database.get(videoId);

            L2.put(videoId, video);
            accessCount.put(videoId, 1);

            return video;
        }

        return null;
    }

    public void addVideoToDatabase(String videoId, String content) {
        L3Database.put(videoId, new VideoData(videoId, content));
    }

    public void invalidate(String videoId) {
        L1.remove(videoId);
        L2.remove(videoId);
        L3Database.remove(videoId);
        accessCount.remove(videoId);
    }

    public void getStatistics() {

        int total = L1Hits + L2Hits + L3Hits;

        double l1Rate = total == 0 ? 0 : (L1Hits * 100.0) / total;
        double l2Rate = total == 0 ? 0 : (L2Hits * 100.0) / total;
        double l3Rate = total == 0 ? 0 : (L3Hits * 100.0) / total;

        System.out.println("L1 Hit Rate: " + l1Rate + "%");
        System.out.println("L2 Hit Rate: " + l2Rate + "%");
        System.out.println("L3 Hit Rate: " + l3Rate + "%");
        System.out.println("Overall Requests: " + total);
    }

    public static void main(String[] args) {

        Problem10 cache = new Problem10();

        cache.addVideoToDatabase("video_123", "Movie Data A");
        cache.addVideoToDatabase("video_999", "Movie Data B");

        System.out.println(cache.getVideo("video_123"));
        System.out.println(cache.getVideo("video_123"));
        System.out.println(cache.getVideo("video_123"));

        System.out.println(cache.getVideo("video_999"));

        cache.getStatistics();
    }
}