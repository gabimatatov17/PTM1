package test;

import java.util.BitSet;
import java.util.ArrayList;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
import java.util.Iterator;


public class BloomFilter {
    private final int bitSetSize;
    private final String[] hashNames;
    private BitSet bitSet;
    private ArrayList<MessageDigest> hashFunctions;

    public BloomFilter(int bitSetSize, String... hashNames){
        this.bitSetSize=bitSetSize;
        this.hashNames=hashNames.clone();

        // Initialize new BitSet
        this.bitSet = new BitSet(bitSetSize);

        // Initialize new Array List of instances of hashFunctions
        this.hashFunctions = new ArrayList<>();
        for(String hashName : hashNames){
            try {
                MessageDigest hashFunction = MessageDigest.getInstance(hashName);
                hashFunctions.add(hashFunction);
            } catch (NoSuchAlgorithmException e) {
                System.err.println(hashName + " algorithm not found.");
            }
        }
    }
    
    public void add(String wordToAdd){
        Iterator<MessageDigest> hashFunctionsIterator = hashFunctions.iterator();
        while (hashFunctionsIterator.hasNext()) {
            byte[] bts = hashFunctionsIterator.next().digest(wordToAdd.getBytes());
            int indexToMark = Math.abs(new BigInteger(bts).intValue()) % this.bitSetSize;
            // Change the index on the BitSet from off to on.
            this.bitSet.set(indexToMark);
        }
    }

    public boolean contains(String wordToSearch){
        Iterator<MessageDigest> hashFunctionsIterator = hashFunctions.iterator();
        while (hashFunctionsIterator.hasNext()) {
            byte[] bts = hashFunctionsIterator.next().digest(wordToSearch.getBytes());
            int indexToSearch = Math.abs(new BigInteger(bts).intValue()) % this.bitSetSize;
            if (!this.bitSet.get(indexToSearch))
                return false;
        }
        return true;
    }

    public String toString() {
        String bitSetStatus = "";
        for (int i=0; i < this.bitSet.length(); i++){
            if (this.bitSet.get(i))
                bitSetStatus+="1";
            else
                bitSetStatus+="0";
        }
        return bitSetStatus;
    }

    public void hashFunctionsUsed(){
        for (String hashName : this.hashNames)
            System.out.println(hashName);
    }
}
