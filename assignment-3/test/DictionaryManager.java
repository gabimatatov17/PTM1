package test;

import java.util.HashMap;
import java.util.Map;

public class DictionaryManager {

    private Map<String, Dictionary> dictionaryMap;
    private static DictionaryManager dictionaryManager; 


    public DictionaryManager(){
        this.dictionaryMap = new HashMap<>();
    }

    public boolean query(String... books){
        String wordToQuery = books[books.length - 1];
        Boolean isFound = false;

        for(int i = 0; i < books.length -1; i++){
            if(!this.dictionaryMap.containsKey(books[i]))
                this.dictionaryMap.put(books[i], new Dictionary(books[i]));
        }

        for(int i = 0; i < books.length -1; i++){
            if(this.dictionaryMap.get(books[i]).query(wordToQuery))
                isFound = true;
        }
        return isFound;
    }

    public boolean challenge(String... books){
        String wordToChallenge = books[books.length - 1];
        Boolean isFound = false;

        for(int i = 0; i < books.length -1; i++){
            if(!this.dictionaryMap.containsKey(books[i]))
                this.dictionaryMap.put(books[i], new Dictionary(books[i]));
        }

        for(int i = 0; i < books.length -1; i++){
            if(this.dictionaryMap.get(books[i]).challenge(wordToChallenge))
                isFound = true;
        }
        return isFound;
    }

    public int getSize(){
        return this.dictionaryMap.size();
    }

    // Return singleton
    public static DictionaryManager get(){
        if(dictionaryManager == null)
            dictionaryManager = new DictionaryManager();
        return dictionaryManager;
    }
}
