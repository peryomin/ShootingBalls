package main.java.com.peryomin.shootingballs;

import javax.swing.*;
import java.awt.*;

public class ShootingBalls extends JFrame {
    ShootingBalls() {
        add(new Game());
        setTitle("Catch the ball!");
        setBackground(Color.black);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(Constants.WIN_WIDTH, Constants.WIN_HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            ShootingBalls game = new ShootingBalls();
            game.setVisible(true);
        });
    }
}
