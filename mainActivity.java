// Created by YoAn Zhao Dec 2024

import javax.swing.*;

public class mainActivity {
    public static void main(String[] args) {

        int WINDOW_WIDTH = 600;
        int WINDOW_HEIGHT = 800;
        JFrame frame = new JFrame("Game");

        gameCanvas canvas = new gameCanvas();

        frame.add(canvas);

        frame.setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
        frame.setVisible(true);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
            }
        });

        //update Canvas
        canvas.updateGame();
    }
}