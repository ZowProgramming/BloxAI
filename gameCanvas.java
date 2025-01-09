// Created by YoAn Zhao Dec 2024

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class gameCanvas extends JPanel{
    final int FPS = 60;
    //int LVL = 5;

    int count = 0;

    final int BLOCK_SIZE = 20;

    final int GAME_X = 150;
    final int GAME_Y = 50;

    final int GAME_WIDTH = 10 * BLOCK_SIZE;
    final int GAME_HEIGHT = 20 * BLOCK_SIZE;

    final int NETWORK_X = 20;
    final int NETWORK_Y = 500;
    final int NODE_SIZE = 15;
    final int NN_GRID_SIZE = 10;

    final int SCORE_WEIGHT = 50;
    final int SURVIVAL_WEIGHT = 1;

    int gen = 0;
    int genome = 0;

    Board gameBoard = new Board();
    NeuralNetwork network = new NeuralNetwork(gameBoard);

    boolean Paused = false;

    boolean needNewBoard = true;

    int action = 0;

    gameCanvas(){
        //KEYBINDS

        //Close Application when escape pressed
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "escapePressed");
        getActionMap().put("escapePressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Paused = !Paused;
            }
        });

        //Move Piece Left;
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("LEFT"), "leftPressed");
        getActionMap().put("leftPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameBoard.userAction("LEFT",Paused);
            }
        });

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("RIGHT"), "rightPressed");
        getActionMap().put("rightPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameBoard.userAction("RIGHT",Paused);
            }
        });

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"), "downPressed");
        getActionMap().put("downPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameBoard.userAction("DOWN",Paused);
            }
        });

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("SPACE"), "spacePressed");
        getActionMap().put("spacePressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameBoard.userAction("HARD_DROP",Paused);
            }
        });

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("Z"), "zPressed");
        getActionMap().put("zPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameBoard.userAction("CCW",Paused);
            }
        });

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("C"), "cPressed");
        getActionMap().put("cPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameBoard.userAction("CW",Paused);
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
        setBackground(new Color(200,230,255));

        setSize(1000,1000);       

        // DRAW TETRIS GAME
        for(int row = 0; row < 20; row++){
            for(int col = 0; col < 10; col++){
                switch (gameBoard.blocks[row][col]) {
                    case 0 -> g.setColor(Color.BLACK);
                    case 1 -> g.setColor(Color.RED);
                    case 2 -> g.setColor(Color.WHITE);
                    default -> {
                    }
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
        g.drawString("LEVEL: "+ gameBoard.LVL,GAME_X,GAME_Y - 10);

        //DRAW NEURAL NETWORK
        
        // gameBoard AKA input layer
        for(int row = 0; row < 20; row++){
            for(int col = 0; col < 10; col++){
                double nodeVal = network.inputLayer.neurons[row*10 + col].neuronValue;
                int colorVal = (int)(nodeVal * 255);
                g.setColor(new Color(colorVal,colorVal,colorVal));
                g.fillRect(NETWORK_X + col * NN_GRID_SIZE, 80 + NETWORK_Y + row * NN_GRID_SIZE, NN_GRID_SIZE, NN_GRID_SIZE);
            }
        }

        // hidden layer 1
        for(int i = 0; i < network.hiddenLayer1.neurons.length; i++){
            double nodeVal = network.hiddenLayer1.neurons[i].neuronValue;
            int colorVal = (int)((nodeVal) * 255);
            g.setColor(new Color(colorVal,colorVal,colorVal));

            int nodeX = NETWORK_X + 150;
            int nodeY = NETWORK_Y + (int)(i*NODE_SIZE*1.5);
            g.fillOval(nodeX, nodeY, NODE_SIZE, NODE_SIZE);

            /*

            for(int j = 0; j < network.hiddenLayer1.neurons.length; j++){
                for(int row = 0; row < 20; row++){
                    for(int col = 0; col < 10; col++){
                        if(network.hiddenLayer1.neurons[j].weights[row*10 + col] > 40){
                            g.setColor(Color.GREEN);
                            g.drawLine(NETWORK_X + col * NN_GRID_SIZE, 80 + NETWORK_Y + row * NN_GRID_SIZE,nodeX,nodeY);
                        }
                        else if(network.hiddenLayer1.neurons[j].weights[row*10 + col] < -40){
                            g.setColor(Color.RED );
                            g.drawLine(NETWORK_X + col * NN_GRID_SIZE, 80 + NETWORK_Y + row * NN_GRID_SIZE,nodeX,nodeY);
                        }
                        
                    }
                }
            }

            */
        }

        // hidden layer 2
        for(int i = 0; i < network.hiddenLayer2.neurons.length; i++){
            double nodeVal = network.hiddenLayer2.neurons[i].neuronValue;
            int colorVal = (int)(nodeVal * 255);
            g.setColor(new Color(colorVal,colorVal,colorVal));
            g.fillOval(NETWORK_X + 300, NETWORK_Y + (int)(i*NODE_SIZE*1.5), NODE_SIZE, NODE_SIZE);


        }

        // hidden layer 3
        for(int i = 0; i < network.hiddenLayer3.neurons.length; i++){
            double nodeVal = network.hiddenLayer3.neurons[i].neuronValue;
            int colorVal = (int)(nodeVal * 255);
            g.setColor(new Color(colorVal,colorVal,colorVal));
            g.fillOval(NETWORK_X + 450, NETWORK_Y + (int)(i*NODE_SIZE*1.5), NODE_SIZE, NODE_SIZE);

        }

        // outputLayer
        for(int i = 0; i < network.outputLayer.neurons.length; i++){
            double nodeVal = network.outputLayer.neurons[i].neuronValue;
            int colorVal = (int)(nodeVal * 255);
            g.setColor(new Color(colorVal,colorVal,colorVal));
            g.fillOval(NETWORK_X + 600, 100 + NETWORK_Y + (int)(i*NODE_SIZE*1.5), NODE_SIZE, NODE_SIZE);

        }
        g.setColor(Color.BLACK);
        g.drawString("LEFT", NETWORK_X + 640, 110 + NETWORK_Y + (int)(0*NODE_SIZE*1.5));
        g.drawString("RIGHT", NETWORK_X + 640, 110 + NETWORK_Y + (int)(1*NODE_SIZE*1.5));
        g.drawString("CW", NETWORK_X + 640, 110 + NETWORK_Y + (int)(2*NODE_SIZE*1.5));
        g.drawString("CCW", NETWORK_X + 640, 110 + NETWORK_Y + (int)(3*NODE_SIZE*1.5));
        g.drawString("HARD DROP", NETWORK_X + 640, 110 + NETWORK_Y + (int)(4*NODE_SIZE*1.5));
        g.drawString("DOWN", NETWORK_X + 640, 110 + NETWORK_Y + (int)(5*NODE_SIZE*1.5));

        Font font = new Font("Arial", Font.PLAIN, 24);
        g.setFont(font);

        g.drawString("GEN: " + gen + " GENOME: " + genome, 400,100);
        
    }

    public void updateGame(){
        

        new Thread(() -> { 
            while(true){
                if(needNewBoard){
                    gameBoard = new Board();
                    count = 0;
                    Paused = false;
                }
                if(gameBoard.gameOver()){
                    network.writeToFile("storedNetworks.csv");
                    network = new NeuralNetwork(gameBoard);
                    needNewBoard = true;
                }

                if(!Paused){
                    needNewBoard = false;
                    //int speedDivider = 60/gameBoard.LVL;
                    int speedDivider = 5;

                    if(count % speedDivider == 0){
                        gameBoard.updateFallingBlocks();  
                    }
                    gameBoard.updateBoard();
                    network.inputBoard(gameBoard);
                    network.update();
                    

                    action = network.outputNode();
                    switch(action){
                        case 0 -> gameBoard.userAction("LEFT", Paused);
                        case 1 -> gameBoard.userAction("RIGHT", Paused);
                        case 2 -> gameBoard.userAction("CW", Paused);
                        case 3 -> gameBoard.userAction("CCW", Paused);
                        case 4 -> gameBoard.userAction("HARD DROP", Paused);
                        case 5 -> gameBoard.userAction("DOWN", Paused);
                        default -> {
                        }
                    }

                    if(gameBoard.LVL < 60 && gameBoard.LVL > 0){
                        gameBoard.LVL = (gameBoard.lines / 30) + 1;
                    }

                    if(gameBoard.gameOver()){
                        network.setFitness(SCORE_WEIGHT*gameBoard.score + SURVIVAL_WEIGHT*count);
                        System.out.println(network.getFitness());
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