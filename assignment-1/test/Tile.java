package test;
import java.util.Random;
import java.util.Objects;

public class Tile {
    final char letter;
    final int score;
    
    private Tile(char letter, int score) {
        this.letter=letter;
        this.score=score;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {return true;}
        if (object == null || getClass() != object.getClass() ) { return false;}
        Tile tile = (Tile) object;
        // Compare the data members and return accordingly 
        return (tile.score == this.score) && (tile.letter == this.letter);
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.score, this.letter);
    }

    public static class Bag {
        // Random
        private Random random;
        
        // Bag attribiutes
        private static Bag bagInstance;
        private Tile[] tileArray;
        private int[] tileAmount;
        private int[] initialTileAmount;

        public Bag(){
            this.tileArray = new Tile[]{
                new Tile('A', 1),
                new Tile('B', 3),
                new Tile('C', 3),
                new Tile('D', 2),
                new Tile('E', 1),
                new Tile('F', 4),
                new Tile('G', 2),
                new Tile('H', 4),
                new Tile('I', 1),
                new Tile('J', 8),
                new Tile('K', 5),
                new Tile('L', 1),
                new Tile('M', 3),
                new Tile('N', 1),
                new Tile('O', 1),
                new Tile('P', 3),
                new Tile('Q', 10),
                new Tile('R', 1),
                new Tile('S', 1),
                new Tile('T', 1),
                new Tile('U', 1),
                new Tile('V', 4),
                new Tile('W', 4),
                new Tile('X', 4),
                new Tile('Y', 8),
                new Tile('Z', 10)};

            this.tileAmount = new int[]{
                9,2,2,4,12,2,3,2,9,1,1,4,2,6,8,2,1,6,4,6,4,2,2,1,2,1
            };
            
            this.random = new Random();

            this.initialTileAmount = new int[]{
                9,2,2,4,12,2,3,2,9,1,1,4,2,6,8,2,1,6,4,6,4,2,2,1,2,1
            };
        }
        
        public Tile getRand(){
            // Generate a random number between 0 and 25
            int randomNumber = random.nextInt(26);

            if(!BagEmpty()){
                while (!BagEmpty()) {
                    if(tileAmount[randomNumber] != 0) 
                    {
                        tileAmount[randomNumber]-=1;
                        return tileArray[randomNumber];
                    } 
                    else 
                    {
                        randomNumber = random.nextInt(26);
                    }
                }
            }
            return null;
        }

        public Tile getTile(char tile){
            // Get the Char index
            if(!BagEmpty()){
                int index = getTileIndex(tile);
                if(index != -1){
                    if(tileAmount[index] != 0){
                        tileAmount[index]-=1;
                        return tileArray[index];
                    }
                }
            }
            return null;
        }

        public void put(Tile tile){
            int index = getTileIndex(tile.letter);
            if(index != -1){
                if(tileAmount[index] < initialTileAmount[index])
                    tileAmount[index]+=1;
            }
        }

        public int size(){
            int size=0;
            for(int i=0; i < tileAmount.length; i++){
                size+=tileAmount[i];
            }
            return size;
        }

        public static Bag getBag() {
            if (bagInstance == null) {
                bagInstance = new Bag();
            }
            return bagInstance;
        }

        public int[] getQuantities(){

            int[] copyTileAmount = new int[tileAmount.length];
            for(int i=0; i < copyTileAmount.length; i++){
                    copyTileAmount[i] = tileAmount[i];
                }
            return copyTileAmount;
        }
    
        // Get index of Char Method.
        public int getTileIndex(char tile){
            for(int i=0; i < tileAmount.length; i++){
                if(tileArray[i].letter == tile){
                    return i;
                }
            }
            return -1;
        }

        public boolean BagEmpty(){
            for(int i=0; i < tileAmount.length; i++){
                if(tileAmount[i] != 0 ){
                    return false;
                }
            }
            return true;
        }
    }
    
}
