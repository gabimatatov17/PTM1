package test;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRU implements CacheReplacementPolicy{

    private LinkedHashMap<String, String> lruCache;

    public LRU(){
        this.lruCache = new LinkedHashMap<String, String>(){
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest){
                return false;
            }
        };
    }

    @Override
    public String remove() {
        // Get the first entry in the map (least recently used)
        Map.Entry<String, String> eldest = lruCache.entrySet().iterator().next();
        // Remove the entry from the cache
        String removedValue = lruCache.remove(eldest.getKey());
        // Return the value that was removed
        return removedValue;
    }

    @Override
    public void add(String word){
        // If the word already exists in the cache, remove it to re-insert it at the front
        if (this.lruCache.containsKey(word)) {
            this.lruCache.remove(word);
        }
        // Put the word in the cache (moves it to the front if it already existed)
        this.lruCache.put(word, word);
    }
}
