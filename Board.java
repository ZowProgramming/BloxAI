// Created by YoAn Zhao Dec 2024

public class Board {

    public int blocks[][];
    public int pieceX;
    public int pieceY;

    public int fallingBlocks[][];
    public int stableBlocks[][];

    public int score;
    public int lines;
    public int LVL = 1;

    final private int LINE_CAP = 150;


    @SuppressWarnings("OverridableMethodCallInConstructor")
    Board() {
        blocks = new int[20][10];
        stableBlocks = new int[20][10];

        score = 0;
        lines = 0;
        generateNewPiece();
    }

    public void generateNewPiece() {

        String pieces[] = {"O","J","L","Line","S","Z","T"};
        int pieceSelect = (int)(Math.random()*(pieces.length));
        //System.out.println("NUMBER: " + pieceSelect);

        switch(pieceSelect) {
            case 0 -> {
                fallingBlocks = new int[2][2];
                fallingBlocks[0][0] = 1;
                fallingBlocks[0][1] = 1;
                fallingBlocks[1][0] = 1;
                fallingBlocks[1][1] = 1;

                pieceX = 5;
                pieceY = 0;
            }
            case 1 -> {
                fallingBlocks = new int[3][3];
                fallingBlocks[0][0] = 1;
                fallingBlocks[1][0] = 1;
                fallingBlocks[1][1] = 1;
                fallingBlocks[1][2] = 1;

                pieceX = 4;
                pieceY = 0;
            }
            case 2 -> {
                fallingBlocks = new int[3][3];
                fallingBlocks[0][2] = 1;
                fallingBlocks[1][0] = 1;
                fallingBlocks[1][1] = 1;
                fallingBlocks[1][2] = 1;

                pieceX = 4;
                pieceY = 0;
            }
            case 3 -> {
                fallingBlocks = new int[4][4];
                fallingBlocks[1][0] = 1;
                fallingBlocks[1][1] = 1;
                fallingBlocks[1][2] = 1;
                fallingBlocks[1][3] = 1;

                pieceX = 3;
                pieceY = 0;
            }
            case 4 -> {
                fallingBlocks = new int[3][3];
                fallingBlocks[0][1] = 1;
                fallingBlocks[0][2] = 1;
                fallingBlocks[1][0] = 1;
                fallingBlocks[1][1] = 1;

                pieceX = 4;
                pieceY = 0;
            }
            case 5 -> {
                fallingBlocks = new int[3][3];
                fallingBlocks[0][0] = 1;
                fallingBlocks[0][1] = 1;
                fallingBlocks[1][1] = 1;
                fallingBlocks[1][2] = 1;

                pieceX = 4;
                pieceY = 0;
            }
            default -> {             
                fallingBlocks = new int[3][3];
                fallingBlocks[0][0] = 1;
                fallingBlocks[0][1] = 1;
                fallingBlocks[0][2] = 1;
                fallingBlocks[1][1] = 1;

                pieceX = 4;
                pieceY = 0;
            }
        }
        //O-piece
        //J-piece
        //L-piece
        //Line-piece
        //S-piece
        //Z-piece
        //T-Piece
            }

    @SuppressWarnings("StringEquality")
    public void userAction(String action, boolean isPaused){
        if(action == "LEFT"){
            moveLeft();
        }
        if(action == "RIGHT"){
            moveRight();
        }
        if(action == "DOWN"){
            updateFallingBlocks();
        }
        if(action == "HARD_DROP" && !isPaused){
            while(!updateFallingBlocks()){}
        }
        if(action == "CW"){
            tryRotation(rotateCW());
        }
        if(action == "CCW"){
            tryRotation(rotateCCW());
        }
    }

    private void moveLeft(){
        int size = fallingBlocks.length;
        for(int row = 0; row < size; row++){
            for(int col = 0; col < size; col++){
                if(fallingBlocks[row][col] != 0){
                    if(pieceX + col <= 0){
                        //System.out.println("can't go further");
                        return;
                    }
                    if(stableBlocks[pieceY + row][pieceX + col - 1] != 0){
                        //System.out.println("stable points there");
                        return;
                    }
                }
            }
        }
        //System.out.println("moved left");
        pieceX--;

    }

    private void moveRight(){
        int size = fallingBlocks.length;
        for(int row = 0; row < size; row++){
            for(int col = 0; col < size; col++){
                if(fallingBlocks[row][col] != 0){
                    if(pieceX + col >= 9){
                        //System.out.println("can't go further");
                        return;
                    }
                    if(stableBlocks[pieceY + row][pieceX + col + 1] != 0){
                        //System.out.println("stable points there");
                        return;
                    }
                }
            }
        }
        pieceX++;

    }

    public int[][] rotateCW(){
        int size = fallingBlocks.length;
        int rotated[][] = new int[size][size];

        for(int row = 0; row < size; row++){
            for(int col = 0; col < size; col++){
                rotated[row][col] = fallingBlocks[size - 1 - col][row];
            }
        }

        return rotated;
    }

    public int[][] rotateCCW(){
        int size = fallingBlocks.length;
        int rotated[][] = new int[size][size];

        for(int row = 0; row < size; row++){
            for(int col = 0; col < size; col++){
                rotated[row][col] = fallingBlocks[col][size - 1 - row];
            }
        }

        return rotated;
    }    

    private void tryRotation(int[][] rotated){
        int size = fallingBlocks.length;

        for(int row = 0; row < size; row++){
            for(int col = 0; col < size; col++){
                if(pieceY + row >= 20 || pieceX + col >= 10 || pieceX < 0){
                    //System.out.println("can't rotate");
                    return;
                }
                else if(rotated[row][col] != 0 && stableBlocks[pieceY + row][pieceX + col] != 0){
                    //System.out.println("can't rotate");
                    return;
                }
            }
        }

        //System.out.println("ROTATION");
        fallingBlocks = rotated;
    }

    public boolean updateFallingBlocks(){
        boolean convertToStable = false;

        for(int row = 0; row < fallingBlocks.length; row ++){
            for(int col = 0; col < fallingBlocks.length; col ++){
                if(!convertToStable && fallingBlocks[row][col] != 0){
                    if(pieceY + row == 19){
                        convertToStable = true;
                        //System.out.println("Hit Bottom");
                        break;   
                    }
                    else if(convertToStable){
                        break;
                    }
                    else if(stableBlocks[pieceY + row + 1][pieceX + col] != 0){
                        convertToStable = true;
                        break;
                    }
                }
                if(convertToStable){
                    break;
                }   
            }
            if(convertToStable){
                break;
            } 
        }

        if(convertToStable){
            for(int row = 0; row < fallingBlocks.length; row ++){
                for(int col = 0; col < fallingBlocks.length; col ++){
                    if(fallingBlocks[row][col] != 0){
                        stableBlocks[pieceY + row][pieceX + col] = 2;
                    }
                }
            }

            updateScore();
            generateNewPiece();
            return true;
        }
        else{
            pieceY++;
            return false;
        }   
    }

    public void updateBoard(){
        //superimpose both fallingBlocks and stableBLocks onto blocks;
        blocks = new int[20][10];

        for(int row = 0; row < fallingBlocks.length; row ++){
            for(int col = 0; col < fallingBlocks.length; col ++){
                if(fallingBlocks[row][col] != 0){
                    blocks[pieceY + row][pieceX + col] = fallingBlocks[row][col];
                }
            }
        }

        for(int row = 0; row < 20; row++){
            for(int col = 0; col < 10; col++){
                if(stableBlocks[row][col] != 0){
                    blocks[row][col] = stableBlocks[row][col];
                }
            }
        }
    }

    @SuppressWarnings("ManualArrayToCollectionCopy")
    private void updateScore(){
        boolean rowFull = true;
        int linesCleared = 0;

        for(int row = 0; row < 20; row++){
            for(int col = 0; col < 10; col++){
                if(stableBlocks[row][col] == 0){
                    rowFull = false;
                }
            }
            if(rowFull){
                linesCleared++;

                for(int clearRow = row; clearRow > 0; clearRow--){
                    for(int clearCol = 0; clearCol < 10; clearCol++){
                        stableBlocks[clearRow][clearCol] = stableBlocks[clearRow - 1][clearCol];
                    }
                }
            }
            else{
                rowFull = true;  
            }

            
        }
        if(linesCleared != 0){
            score += 50*linesCleared*linesCleared*LVL;
            lines += linesCleared;
        }

    }

    public boolean gameOver(){
        if(lines >= LINE_CAP){
            return true;
        }
        for(int i = 0; i < 10; i++){
            if(stableBlocks[0][i] != 0){
                return true;
            }
        }
        return false;
    }

    public void printBoard(){
        System.out.println("- - - - - - - - - -");
        for(int row = 0; row < 20; row++){
            for(int col = 0; col < 10; col++){
                System.out.print(blocks[row][col] + " ");
            }
            System.out.println();
        }
        System.out.println("- - - - - - - - - -");
    }

    public int[][] getBoard(){
        return blocks;
    }
    
}
