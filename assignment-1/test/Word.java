package test;

import java.util.Objects;

public class Word {
    private Tile[] tiles;
    private int row;
    private int col;
    private boolean vertical;
    
    // false --> ABC
    //
    //            A
    // true -->   B
    //            C

    public Word(Tile[] tiles, int row, int col, boolean vertical) {
        this.tiles=tiles;
        this.row=row;
        this.col=col;
        this.vertical=vertical;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {return true;}
        if (object == null || getClass() != object.getClass()) {return false;}
        Word word = (Word) object;

        // Compare the Tiles array and return true or false
        boolean tilesCompare = isTilesEqual(word.getTiles());

        return tilesCompare && 
               word.getRow() == this.row &&
               word.getCol() == this.col &&
               word.isVertical() == this.vertical;
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.tiles, this.row, this.col, this.vertical);
    }

    // Getters
    public Tile[] getTiles(){
        return tiles;
    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }

    public boolean isVertical(){
        return vertical;
    }

    // Help Methods
    public boolean isTilesEqual(Tile[] tilesToCompare){
        int thisWordLength      = this.tiles.length;
        int wordToCompareLength = tilesToCompare.length;
        if(thisWordLength != wordToCompareLength) 
            return false;
        else { 
            for(int i=0; i < this.tiles.length; i++){
                if(!this.tiles[i].equals(tilesToCompare[i])) 
                    return false;
            }
        }
        return true;
    }
	
}
