package test;
import test.Tile.Bag;

public class TestTile {
    public static void testEqual() {
        Bag b = Tile.Bag.getBag();
        Tile t1 = b.getTile('A');
        Tile t2 = b.getRand();

        // TestTile
        // if(t1.equals(t2)){
        //     System.out.println("Same Tile");
        // } else {
        //     System.out.println("Not the same tile");
        // }

        // System.out.println(t1.hashCode());
        // System.out.println(t2.hashCode());

        Tile[] tilesToWord1 = new Tile[]{t1,t2};
        Tile[] tilesToWord2 = new Tile[]{t2,t1};

        Word word1 = new Word(tilesToWord1, 4, 5, false);
        Word word2 = new Word(tilesToWord2, 4, 5, false);

        // WordTest
        // if(word1.equals(word2)){
        //     System.out.println("Same Word");
        // } else {
        //     System.out.println("Not the same word");
        // }
        
        // System.out.println(word1.hashCode());
        // System.out.println(word2.hashCode());  
    }

    public static void main(String[] args) {
        testEqual(); // 30 points
        System.out.println("done");
    }
}