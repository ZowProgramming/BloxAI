import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class gameCanvas extends JPanel{
    final int FPS = 60;
    int LVL = 1;

    int count = 0;

    final int BLOCK_SIZE = 30;

    final int GAME_X = 150;
    final int GAME_Y = 50;

    final int GAME_WIDTH = 10 * BLOCK_SIZE;
    final int GAME_HEIGHT = 20 * BLOCK_SIZE;

    Board gameBoard;

    boolean Paused = false;

    boolean needNewBoard = true;

    gameCanvas(){
        //KEYBINDS

        //Close Application when escape pressed
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "escapePressed");
        getActionMap().put("escapePressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Paused){
                    Paused = false;
                }
                else{
                    Paused = true;
                }
            }
        });

        //Move Piece Left;
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("LEFT"), "leftPressed");
        getActionMap().put("leftPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameBoard.userAction("LEFT");
            }
        });

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("RIGHT"), "rightPressed");
        getActionMap().put("rightPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameBoard.userAction("RIGHT");
            }
        });

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"), "downPressed");
        getActionMap().put("downPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameBoard.userAction("DOWN");
            }
        });

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("SPACE"), "spacePressed");
        getActionMap().put("spacePressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameBoard.userAction("HARD_DROP");
            }
        });

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("Z"), "zPressed");
        getActionMap().put("zPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameBoard.userAction("CCW");
            }
        });

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("C"), "cPressed");
        getActionMap().put("cPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameBoard.userAction("CW");
            }
        });

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("R"), "rPressed");
        getActionMap().put("rPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(gameBoard.gameOver()){
                    needNewBoard = true;
                }
            }
        });
    }

    @Override
    public void paint(Graphics g) {

        super.paint(g);

        // Set the background color
        setBackground(Color.WHITE);

        setSize(1000,1000);       

        for(int row = 0; row < 20; row++){
            for(int col = 0; col < 10; col++){
                if(gameBoard.blocks[row][col] == 0){
                    g.setColor(Color.BLACK);

                }
                else if(gameBoard.blocks[row][col] == 1){
                    g.setColor(Color.BLUE);
                }
                else if(gameBoard.blocks[row][col] == 2){
                    g.setColor(Color.RED);
                }
                else if(gameBoard.blocks[row][col] == 3){
                    g.setColor(Color.MAGENTA);
                }
                else if(gameBoard.blocks[row][col] == 4){
                    g.setColor(Color.GREEN);
                }
                else if(gameBoard.blocks[row][col] == 5){
                    g.setColor(Color.YELLOW);
                }
                else if(gameBoard.blocks[row][col] == 6){
                    g.setColor(Color.PINK);
                }
                else if(gameBoard.blocks[row][col] == 7){
                    g.setColor(Color.CYAN);
                }
                int blockX = GAME_X + BLOCK_SIZE * col;
                int blockY = GAME_Y + BLOCK_SIZE * row;

                g.fillRect(blockX, blockY, BLOCK_SIZE, BLOCK_SIZE);

            }
        }
        

        g.setColor(Color.GRAY);
        for(int i = 0; i < 10; i++){
            g.drawLine(GAME_X + BLOCK_SIZE*i, GAME_Y, GAME_X + BLOCK_SIZE*i, GAME_Y + GAME_HEIGHT);
        }
        for(int i = 0; i < 20; i++){
            g.drawLine(GAME_X, GAME_Y + BLOCK_SIZE*i, GAME_X + GAME_WIDTH, GAME_Y + BLOCK_SIZE*i);
        }
        //draw the boundary lines for the game

        g.drawLine(GAME_X, GAME_Y, GAME_X, GAME_Y + GAME_HEIGHT);
        g.drawLine(GAME_X,GAME_Y + GAME_HEIGHT, GAME_X + GAME_WIDTH, GAME_Y + GAME_HEIGHT);
        g.drawLine(GAME_X + GAME_WIDTH, GAME_Y, GAME_X + GAME_WIDTH, GAME_Y + GAME_HEIGHT);

        g.drawString("SCORE: "+gameBoard.score,GAME_X,GAME_Y - 30);
        g.drawString("LINES: "+gameBoard.lines,GAME_X,GAME_Y - 20);
        g.drawString("LEVEL: "+ LVL,GAME_X,GAME_Y - 10);



    }

    public void updateGame(){
        new Thread(() -> { 
            while(true){
                if(needNewBoard){
                    gameBoard = new Board();
                    Paused = false;
                }

                if(!Paused){
                    needNewBoard = false;
                    int speedDivider = 60/LVL;

                    if(count % speedDivider == 0){
                        gameBoard.updateFallingBlocks();  
                    }
                    gameBoard.updateBoard();
                    if(LVL < 60 && LVL > 0){
                        LVL = (gameBoard.lines / 30) + 1;
                    }

                    if(gameBoard.gameOver()){
                        Paused = true;
                    }
    

                    //gameBoard.printBoard();
                    repaint();
                    count++; 
                }
                try{
                    Thread.sleep(1000 / FPS);
                    //Thread.sleep(100);
                }
                catch(InterruptedException e) {
                    e.printStackTrace();
                } 
            }

        }).start();
    }
        
}