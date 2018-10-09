package main.java.com.peryomin.shootingballs.search;

import main.java.com.peryomin.shootingballs.Vector2;
import main.java.com.peryomin.shootingballs.search.minimax.GameMove;

public class Move extends GameMove {
    public Vector2 direction;

    public Move(Vector2 direction) {
        this.direction = direction;
        this.score = 0;
    }
}
