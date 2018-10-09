package main.java.com.peryomin.shootingballs;

import main.java.com.peryomin.shootingballs.search.minimax.Minimax;
import main.java.com.peryomin.shootingballs.search.Move;
import main.java.com.peryomin.shootingballs.search.State;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Set;

public class Player extends Unit {

    BallAI.Action nextAction;
    Minimax minimax = new Minimax();
    Game.GameMode mode;

    public Player(Game.GameMode mode) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMG));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageIcon ii = new ImageIcon(img);
        setImage(ii.getImage());
        position = new Vector2(Constants.WIN_WIDTH / 2, Constants.WIN_HEIGHT / 2);
        System.out.println(position);
        velocity = new Vector2();
        left = new Vector2();
        right = new Vector2();
        up = new Vector2();
        down = new Vector2();
        lastPosition = new Vector2();
        width = Constants.PLAYER_WIDTH;
        height = Constants.PLAYER_HEIGHT;
        this.mode = mode;
    }

    public void chooseNextAction(Ball ball, int depth) {
        //nextAction = ai.getAction(player, this, mapWidth, mapHeight);
        //BallAI.Action action = ai.getRandomAction();
        State state = new State(this, ball);
        state.playerTurn = true;
        Move move = (Move) minimax.search(state, depth);
        if (move == null) {
            move = new Move(new Vector2(0 ,0));
        }
        nextAction = new BallAI.Action(false, move.direction);

        if (nextAction.boost) {
            speed = 1;
        }
    }

    @Override
    void updateVelocity() {
        if (mode == Game.GameMode.AI_VS_AI) {
            velocity.add(nextAction.direction);
        } else {
            velocity.add(left).add(right).add(up).add(down);
        }
    }

    public void keyPressed(Set<Integer> keys) {
        for (Integer keyCode: keys) {
            if (keyCode == KeyEvent.VK_SPACE) {
                //speed = 1;
            }

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
