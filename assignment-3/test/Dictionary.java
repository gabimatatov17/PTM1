package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Dictionary {
    private String[] fileNames;
    private CacheManager existWordsCrp;
    private CacheManager nonExistWordsCrp;
    private BloomFilter bloomFilter;

    public Dictionary(String... fileNames){
        this.fileNames=fileNames;
        this.existWordsCrp = new CacheManager(400, new LRU());
        this.nonExistWordsCrp = new CacheManager(100, new LFU());
        this.bloomFilter = new BloomFilter(256, "MD5", "SHA1");
        try {
            initializeBloomFilter();
        } catch (Exception e) {
            System.err.println("Something went wrong with initializing BloomFilter");
        }
    }

    private void initializeBloomFilter() throws Exception{
        for (String file : fileNames){
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    String[] wordsInLine = line.split(" ");
                    for(String word : wordsInLine){
                        // System.out.println(word);
                        this.bloomFilter.add(word);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean query(String wordToQuery){
        if (this.existWordsCrp.query(wordToQuery))
            return true;
        else if(this.nonExistWordsCrp.query(wordToQuery))
            return false;
        else {
            if (this.bloomFilter.contains(wordToQuery)){
                this.existWordsCrp.add(wordToQuery);
                return true;
            }
            else {
                this.nonExistWordsCrp.add(wordToQuery);
                return false;
            }
        }
    }

    public boolean challenge(String wordToChallenge){
        Boolean result;
        try {
            result = IOSearcher.search(wordToChallenge, fileNames);
            if(result){
                this.existWordsCrp.add(wordToChallenge);
                return true;
            } else {
                this.nonExistWordsCrp.add(wordToChallenge);
                return false;
            }
        } catch (Exception e){
            return false;
        }
    }
}
