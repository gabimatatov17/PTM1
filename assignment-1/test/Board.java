package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Board {
    private static Board boardInstance;
    private Tile[][] boardArray;
    private int boardSize;
    private boolean isFirstWord;

    private HashMap<String[], Integer> specialSlots;

    public Board(){
        this.boardSize = 15;
        this.boardArray = new Tile[this.boardSize][this.boardSize];
        this.isFirstWord = true;
        this.specialSlots = new HashMap<>();

        initializeSpecialSlots(this.specialSlots);
    }

    public Tile[][] getTiles(){
        Tile[][] copyBoardArray = new Tile[this.boardSize][this.boardSize];
        for(int i=0; i<this.boardArray.length; i++)
            for(int j=0; j<this.boardArray[i].length; j++)
                copyBoardArray[i][j] = this.boardArray[i][j];
        return copyBoardArray;
    }

    public boolean boardLegal(Word word){
        int wordLength = word.getTiles().length;
        Tile[] wordTiles = word.getTiles();

        if(isFirstWord){
            // Check if first word located on the star grid (7,7)
            boolean valid = isSutibaleFirstWord(wordLength, word);
            if(valid){
                if (word.isVertical()){
                    if ((word.getRow() + wordLength > 14)){return false;}
                }   
                else {
                    if ((word.getCol() + wordLength > 14)){return false;}
                }
                return true;
            } 
            else {return false;}
        } 
        // Not first word on the board, Check if that 0<= row, col <= 14
        else 
        {
            if ((word.getCol() < 0 || word.getRow() < 0) || (word.getCol() > 14 || word.getRow() > 14)) 
                return false;

            if (word.isVertical()){
                if ((word.getRow() + wordLength > 14)){return false;}
                // Check if word is laying near a tile or on the same tile
                else { 
                    boolean isLinkedToOtherTile = this.isTileLinkedToOther(word);
                    int overlappingTilesAmount = 0;
                    for(int i=word.getRow(); i < wordLength + word.getRow(); i++){
                        if(wordTiles[i-word.getRow()] != null){
                            if(wordTiles[i-word.getRow()].equals(boardArray[i][word.getCol()])){
                                overlappingTilesAmount++;
                            }
                        }   
                    }
                    if(isLinkedToOtherTile || overlappingTilesAmount!=0){return true;}
                } 
            } else {
                if ((word.getCol() + wordLength > 14)){return false;}
                // Check if word is laying near a tile or on the same tile
                else {
                    boolean isLinkedToOtherTile = this.isTileLinkedToOther(word);
                    int overlappingTilesAmount = 0;
                    for(int i=word.getCol(); i < wordLength + word.getCol(); i++){
                        if(wordTiles[i-word.getCol()] != null){
                            if(wordTiles[i-word.getCol()].equals(boardArray[word.getRow()][i])){
                                overlappingTilesAmount++;
                            }
                        }          
                    }
                    if(isLinkedToOtherTile || overlappingTilesAmount!=0){return true;}
                }
            }
        }
        return false;
    }

    public boolean dictionaryLegal(Word word){return true;}

    public ArrayList<Word> getWords(Word word){
        ArrayList<Word> wordList = new ArrayList<>();
        wordList.add(word);
        if(this.isFirstWord)
            return wordList;
        else 
            // Do if isVertical
            if(!word.isVertical()){
                int up, down;
                for(int i=word.getCol(); i < word.getTiles().length + word.getCol(); i++){
                    if(word.getTiles()[i - word.getCol()] == null) continue;
                    if(word.getTiles()[i - word.getCol()].equals(this.boardArray[word.getRow()][i])) continue;
                    up = word.getRow()-1;
                    down = word.getRow()+1;
                    if(0 <= up && up <= this.boardSize-1)
                        if(this.boardArray[up][i] != null && this.boardArray[down][i] == null) {
                            Word newWord = extractWord(word.getRow(), i, word.isVertical(), word.getTiles()[i-word.getCol()], "end");
                            wordList.add(newWord);
                            continue;
                        }
                        else if(this.boardArray[up][i] == null && this.boardArray[down][i] != null) {
                            Word newWord = extractWord(word.getRow(), i, word.isVertical(), word.getTiles()[i-word.getCol()], "beginning");
                            wordList.add(newWord);
                            continue;
                        }
                        else if(this.boardArray[up][i] != null && this.boardArray[down][i] != null) {
                            Word newWord = extractWord(word.getRow(), i, word.isVertical(), word.getTiles()[i-word.getCol()], "middle");
                            wordList.add(newWord);
                            continue;    
                        }
                    // Todo: Chcek for down
                }
            } else {
                int right, left;
                for(int i=word.getRow(); i < word.getTiles().length + word.getRow(); i++){
                    if(word.getTiles()[i - word.getRow()] == null) continue;
                    left = word.getRow()-1;
                    right = word.getRow()+1;
                    // ToDo: Continue to check left and right;
                }
            }
        return wordList;
    }

    public static Board getBoard() {
        if (boardInstance == null) {
            boardInstance = new Board();
        }
        return boardInstance;
    }

    public int getScore(Word word){
        Tile[] tiles = word.getTiles();
        int option, multiplyFactor = 1, score = 0;

        if(!word.isVertical()){
            for(int i=word.getCol(); i < word.getCol() + tiles.length; i++){
                option = checkSpecialSlot(String.valueOf(word.getRow()) + "," + String.valueOf(i), tiles[i - word.getCol()]);
                switch(option) {
                    case 0:
                        if(this.isFirstWord)
                            multiplyFactor*=2;
                        score += tiles[i - word.getCol()].score;
                        break;
                    case 1: 
                        multiplyFactor*=3;
                        score += tiles[i - word.getCol()].score;
                        break;
                    case 2: 
                        multiplyFactor*=2;
                        score += tiles[i - word.getCol()].score; 
                        break;
                    case 3:
                        score += (2 * tiles[i - word.getCol()].score);
                        break;
                    case 4:
                        score += (3 * tiles[i - word.getCol()].score);
                        break;
                    default:
                        if (this.boardArray[word.getRow()][i] == null && tiles[i - word.getCol()] != null)
                            score += tiles[i - word.getCol()].score;
                        else
                            score += this.boardArray[word.getRow()][i].score;
                        break;
                  }
            } 
        } else {
            for(int i=word.getRow(); i < word.getRow() + tiles.length; i++){
                option = checkSpecialSlot(String.valueOf(String.valueOf(i) + "," + word.getCol()), tiles[i - word.getRow()]);
                switch(option) {
                    case 0:
                        if(this.isFirstWord)
                            multiplyFactor*=2;
                        score += tiles[i - word.getRow()].score;
                        break;
                    case 1: 
                        multiplyFactor*=3;
                        score += tiles[i - word.getRow()].score;
                        break;
                    case 2: 
                        multiplyFactor*=2;
                        score += tiles[i - word.getRow()].score; 
                        break;
                    case 3:
                        score += (2 * tiles[i - word.getRow()].score);
                        break;
                    case 4:
                        score += (3 * tiles[i - word.getRow()].score);
                        break;
                    default:
                        if (this.boardArray[i][word.getCol()] == null && tiles[i - word.getRow()] != null)
                            score += tiles[i - word.getRow()].score;
                        else
                            score += this.boardArray[i][word.getCol()].score;
                        break;
                  }
            } 
        }           
        return score * multiplyFactor;
    }

    public int tryPlaceWord(Word word){
        if(!boardLegal(word)){
            return 0;
        } 

        if(!this.dictionaryLegal(word)) return 0;

        ArrayList<Word> newWords = this.getWords(word);
        // System.out.println("Word List Size:" + newWords.size());
        int newWordsScore = 0;

        for (Word newWord : newWords){
            if(!this.boardLegal(newWord)){
                return 0;
            }
            // for(int i=0; i < newWord.getTiles().length; i++){
            //     if(newWord.getTiles()[i] != null)
            //         System.out.println(newWord.getTiles()[i].letter);
            // }
            // System.out.println(this.getScore(newWord));
            newWordsScore += this.getScore(newWord);
        }

        // Calculate the score
        //int score = this.getScore(word);

        // Put Word On The Board
        if(word.isVertical()){
            int i = word.getRow();
            for (Tile tile : word.getTiles()){
                if(tile != null)
                    this.boardArray[i][word.getCol()] = tile;
                i++;
            }
        } else {
            int i = word.getCol();
            for (Tile tile : word.getTiles()){
                if(tile != null)
                    this.boardArray[word.getRow()][i] = tile;
                i++;
            }
        }
        this.isFirstWord = false;
        // System.out.println("==========================");
        // System.out.println(newWordsScore);
        return newWordsScore;
    }

    ////////////////////////////////////
    /////////// Help Methods ///////////
    ////////////////////////////////////
    public boolean isSutibaleFirstWord(int wordLength, Word word){
        if((word.isVertical() && (word.getCol() != 7)) || (!word.isVertical() && (word.getRow() != 7))) 
            return false;
        if ((word.getCol() < 0 || word.getRow() < 0) || (word.getCol() > 14 || word.getRow() > 14)) 
            return false; 
        else {
            if(!word.isVertical()){
                if((word.getRow() + wordLength -1 < 7) || (word.getRow() > 7))
                    return false;
            }
            else {
                if((word.getCol() + wordLength -1 < 7) || (word.getCol() > 7))
                    return false;
            }
        }
        return true;
    }

    public boolean isTileLinkedToOther(Word word){
        if(word.isVertical()){
            int left, right, up, down;
            for(int i=word.getRow(); i < word.getTiles().length + word.getRow(); i++){
                if(i==word.getRow()){
                    up = i-1;
                    if(0 <= up && up <= this.boardSize-1)
                        if(this.boardArray[up][word.getCol()] != null) {return true;}
                } else if(i==(word.getTiles().length + word.getRow())){
                    down = i+1;
                    if(0 <= down && down <= this.boardSize-1)
                        if(this.boardArray[down][word.getCol()] != null) {return true;}
                } else {
                    right = word.getCol()+1;
                    left = word.getCol()-1;
                    if(0 <= right && right <= this.boardSize-1)
                        if(this.boardArray[i][right] != null) {return true;}
                    if(0 <= left && left <= this.boardSize-1)
                        if(this.boardArray[i][left] != null) {return true;}
                }
            }   
        } else {
            int left, right, up, down;
            for(int i=word.getCol(); i < word.getTiles().length + word.getCol(); i++){
                if(i==word.getCol()){
                    left = i-1;
                    if(0 <= left && left <= this.boardSize-1)
                        if(this.boardArray[word.getRow()][left] != null) {return true;}
                } else if (i==(word.getTiles().length + word.getCol())){
                    right = i-1;
                    if(0 <= right && right <= this.boardSize-1)
                        if(this.boardArray[word.getRow()][right] != null) {return true;}
                } else {
                    up = word.getRow()-1;
                    down = word.getRow()+1;
                    if(0 <= up && up <= this.boardSize-1)
                        if(this.boardArray[up][i] != null) {return true;}
                    if(0 <= down && down <= this.boardSize-1)
                        if(this.boardArray[down][i] != null) {return true;}
                }
            }
        }
        return false;
    }

    public void initializeSpecialSlots(HashMap specialSlots){
        specialSlots.put(new String[]{"7,7"}, 0);
        specialSlots.put(new String[]{"0,0", "0,7", "0,14", "7,0", "7,14", "14,0", "14,7", "14,14"}, 1);
        specialSlots.put(new String[]{"1,1", "2,2", "3,3", "4,4", "10,10", "11,11", "12,12", "13,13", "1,13", "2,12", "3,11", "4,10", "10,4", "11,3", "12,2", "13,1"}, 2);
        specialSlots.put(new String[]{"0,3", "0,11", "2,6", "2,8", "3,0", "3,7", "3,14", "6,2", "6,6", "6,8", "6,12", "7,3", "7,11", "8,2", "8,6", "8,8", "8,16", "11,0", "11,7", "11,14", "12,6", "12,8", "14,3", "14,11"}, 3);
        specialSlots.put(new String[]{"1,5", "1,9", "5,1", "5,5", "5,9", "5,13", "9,1", "9,5", "9,9", "9,13", "13,5", "13,9"}, 4);
    }

    public int checkSpecialSlot(String boardIndex, Tile tileToCheck){
        // Iterate through the map keys to find the key that contains the search string
        // String[] indexes = boardIndex.split(",");
        if(tileToCheck == null)
            return -1;

        for (Map.Entry<String[], Integer> entry : specialSlots.entrySet()) {
            String[] keys = entry.getKey();
            for (String key : keys) {
                if (key.equals(boardIndex)){
                        //this.boardArray[Integer.parseInt(indexes[0])][Integer.parseInt(indexes[1])] == null){
                        int value = entry.getValue();
                        return value;
                    }
                }
            }
        return -1;
    }

    public Word extractWord(int row, int col, boolean isVertical, Tile tileToAdd, String position){
        ArrayList<Tile> tiles = new ArrayList<>();
        int newWordRow = 0;
        if(!isVertical){
            if(position.equals("end")){
                while (this.boardArray[row-1][col] != null) {
                    row--;
                }
                newWordRow = row;
                while (this.boardArray[row][col] != null){
                    tiles.add(this.boardArray[row][col]);
                    row++;
                }
                if(this.boardArray[row][col] == null){
                    tiles.add(tileToAdd);
                    row++;
                }
            } else if(position.equals("beginning")) {
                newWordRow = row;
                if(this.boardArray[row][col] == null){
                    tiles.add(tileToAdd);
                    row++;
                }
                while (this.boardArray[row-1][col] != null) {
                    row--;
                }
                while (this.boardArray[row][col] !=null){
                    tiles.add(this.boardArray[row][col]);
                    row++;
                }
            } else {
                while (this.boardArray[row-1][col] != null) {
                    row--;
                }
                newWordRow = row;
                while (this.boardArray[row][col] !=null){
                    tiles.add(this.boardArray[row][col]);
                    row++;
                }
                if(this.boardArray[row][col] == null){
                    tiles.add(tileToAdd);
                    row++;
                }
                while (this.boardArray[row][col] != null) {

                    tiles.add(this.boardArray[row][col]);
                    row++;
                }
            }
            // Convert ArrayList To Array
            Tile[] newWordTiles = tiles.toArray(new Tile[tiles.size()]);
            return new Word(newWordTiles, newWordRow, col, true);
        } else {
            return null;
            // ToDo: Create the same logic for col
        }
    }
}