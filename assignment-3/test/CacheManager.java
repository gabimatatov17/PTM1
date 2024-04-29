package test;

import java.util.HashSet;

public class CacheManager {
	private int size;
    private CacheReplacementPolicy crp;
    private HashSet<String> wordsCache;

	public CacheManager(int size, CacheReplacementPolicy crp){
        this.size=size;
        this.crp=crp;
        this.wordsCache = new HashSet<>();
    }

    public boolean query(String searchValue){
        if (wordsCache.contains(searchValue))
            return true;
        return false;
        }

    public void add(String addValue){
        int wordsCacheSize = wordsCache.size();
        if (this.size == wordsCacheSize)
            wordsCache.remove(this.crp.remove());
        this.wordsCache.add(addValue);
        this.crp.add(addValue);
    }
}


