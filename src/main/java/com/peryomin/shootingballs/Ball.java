package main.java.com.peryomin.shootingballs;

import main.java.com.peryomin.shootingballs.search.minimax.Minimax;
import main.java.com.peryomin.shootingballs.search.Move;
import main.java.com.peryomin.shootingballs.search.State;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Set;

public class Ball extends Unit {

    BallAI ai = new BallAI();
    BallAI.Action nextAction;
    Minimax minimax = new Minimax();
    Game.GameMode mode;

    public Ball(Game.GameMode mode) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(getClass().getResourceAsStream(Constants.BALL_IMG));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageIcon ii = new ImageIcon(img);
        setImage(ii.getImage());
        position = new Vector2(Constants.WIN_WIDTH / 4, Constants.WIN_HEIGHT / 4);
        velocity = new Vector2();
        left = new Vector2();
        right = new Vector2();
        up = new Vector2();
        down = new Vector2();
        lastPosition = new Vector2();
        width = Constants.BALL_WIDTH;
        height = Constants.BALL_HEIGHT;
        this.mode = mode;
        speed = 0.85f;
        minSpeed = 0.85f;
    }

    public void chooseNextAction(Player player, int depth) {
        if (mode == Game.GameMode.AGAINST_MINIMAX_AI) {
            State state = new State(player, this);
            Move move = (Move) minimax.search(state, depth);
            if (move == null) {
                move = new Move(new Vector2(0 ,0));
            }
            nextAction = new BallAI.Action(false, move.direction);

            if (nextAction.boost) {
                speed = 1;
            }
        } else {
            nextAction = ai.getAction(player, this);
        }
    }

    @Override
    void updateVelocity() {
        velocity.add(nextAction.direction);
    }

    public void keyPressed(Set<Integer> keys) {
        for (Integer keyCode: keys) {
            if (keyCode == KeyEvent.VK_SPACE)
                speed = 1;
            if (keyCode == KeyEvent.VK_UP) {
                up.set(0, -1);
            } else if (keyCode == KeyEvent.VK_DOWN) {
                down.set(0, 1);
            } else if (keyCode == KeyEvent.VK_LEFT) {
                left.set(-1, 0);
            } else if (keyCode == KeyEvent.VK_RIGHT) {
                right.set(1, 0);
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        System.out.println("released = " + keyCode);
        if (keyCode == KeyEvent.VK_UP) {
            up.set(0, 0);
        } else if (keyCode == KeyEvent.VK_DOWN) {
            down.set(0, 0);
        } else if (keyCode == KeyEvent.VK_LEFT) {
            left.set(0, 0);
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            right.set(0, 0);
        }
    }
}