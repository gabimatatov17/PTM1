package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class IOSearcher {

    public static boolean search(String wordToSearch, String... fileNames) throws Exception{
        for (String file : fileNames){
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    String[] wordsInLine = line.split(" ");
                    for(String word : wordsInLine){
                    // System.out.println(word);
                        if(wordToSearch.equals(word)) 
                            return true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
