package test;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class LFU implements CacheReplacementPolicy{
    private final Map<String, CacheEntry> cache;
    private final PriorityQueue<CacheEntry> priorityQueue;

    public LFU() {
        this.cache = new HashMap<>();
        this.priorityQueue = new PriorityQueue<>((entry1, entry2) -> {
            int frequencyComparison = Integer.compare(entry1.frequency, entry2.frequency);
            return frequencyComparison != 0 ? frequencyComparison : Long.compare(entry1.timestamp, entry2.timestamp);
        });
    }

    public void add(String key) {
        if (cache.containsKey(key)) {
            CacheEntry entry = cache.get(key);
            entry.frequency++;
            priorityQueue.remove(entry);
            priorityQueue.offer(entry);
        } else {
            CacheEntry newEntry = new CacheEntry(key);
            cache.put(key, newEntry);
            priorityQueue.offer(newEntry);
        }
    }

    public String remove() {
        if (priorityQueue.isEmpty()) {
            return null;
        }
        CacheEntry entryToRemove = priorityQueue.poll();
        cache.remove(entryToRemove.key);
        return entryToRemove.key;
    }


    private static class CacheEntry {
        private final String key;
        private int frequency;
        private final long timestamp;

        public CacheEntry(String key) {
            this.key = key;
            this.frequency = 1;
            this.timestamp = System.nanoTime();
        }
    }
}
