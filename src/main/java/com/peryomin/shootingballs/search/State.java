package main.java.com.peryomin.shootingballs.search;

import main.java.com.peryomin.shootingballs.*;
import main.java.com.peryomin.shootingballs.search.minimax.GameMove;
import main.java.com.peryomin.shootingballs.search.minimax.GameState;

public class State implements GameState {

    private final float INTERPOLATION = 4.5f;

    public Vector2 playerPos;
    private Vector2 playerVel;
    private float playerSpeed;

    public Vector2 ballPos;
    private Vector2 ballVel;
    private float ballSpeed;

    public boolean playerTurn = false;

    public State(Player player, Ball ball) {
        this.playerPos = player.getPosition().cpy();
        this.playerVel = player.getVelocity().cpy();
        this.playerSpeed = player.getMinSpeed();
        this.ballPos = ball.getPosition().cpy();
        this.ballVel = ball.getVelocity().cpy();
        this.ballSpeed = ball.getMinSpeed();
    }

    State(State state) {
        this.playerPos = state.playerPos.cpy();
        this.playerVel = state.playerVel.cpy();
        this.playerSpeed = state.playerSpeed;
        this.ballPos = state.ballPos.cpy();
        this.ballVel = state.ballVel.cpy();
        this.ballSpeed = state.ballSpeed;
        this.playerTurn = state.playerTurn;
    }

    @Override
    public void makeMove(GameMove gameMove) {
        Move move = (Move) gameMove;

        Vector2 velocity;
        Vector2 position;
        float speed;
        float r;
        if (playerTurn) {
            velocity = playerVel;
            position = playerPos;
            speed = playerSpeed;
            r = Constants.PLAYER_WIDTH / 2;
        } else {
            velocity = ballVel;
            position = ballPos;
            speed = ballSpeed;
            r = Constants.BALL_WIDTH / 2;
        }
        playerTurn = !playerTurn;

        velocity.add(move.direction);

        if (velocity.x > 0) {
            velocity.x = Math.min(velocity.x, Unit.maxVelocity);
        }

        if (velocity.x < 0) {
            velocity.x = Math.max(velocity.x, Unit.minVelocity);
        }

        if (velocity.y > 0) {
            velocity.y = Math.min(velocity.y, Unit.maxVelocity);
        }

        if (velocity.y < 0) {
            velocity.y = Math.max(velocity.y, Unit.minVelocity);
        }

        velocity.scl(0.98f);

        if (position.x + r >= Constants.WIN_WIDTH) {
            velocity.x *= -1;
        } else if (position.x - r <= 0) {
            velocity.x *= -1;
        }

        if (position.y + r / 2 >= Constants.WIN_HEIGHT) {
            velocity.y *= -1;
        } else if (position.y - r / 2 <= 0) {
            velocity.y *= -1;
        }

        position.add(velocity.x * speed * INTERPOLATION, velocity.y * speed * INTERPOLATION);
    }

    @Override
    public boolean isTerminal() {
        //return playerPos.dst(ballPos) + (Constants.BALL_WIDTH / 2) * 0.1
        //        < Constants.PLAYER_WIDTH / 2 + Constants.BALL_WIDTH / 2;
        return playerPos.dst(ballPos)
                < Constants.PLAYER_WIDTH / 2 + Constants.BALL_WIDTH / 2;
    }

    @Override
    public int getWinner() {
        return 0;
    }

    @Override
    public float evaluate() {
        float eval = 0;

        if (isTerminal()) {
            return playerTurn ? Float.MIN_VALUE / 2 : Float.MAX_VALUE / 2;
        }

        Vector2 leftTop = new Vector2(0, 0);
        Vector2 leftBottom = new Vector2(0, Constants.WIN_HEIGHT);
        Vector2 rightTop = new Vector2(Constants.WIN_WIDTH, 0);
        Vector2 rightBottom = new Vector2(Constants.WIN_WIDTH, Constants.WIN_HEIGHT);

        if (ballPos.dst(leftTop) < 100 || ballPos.dst(leftBottom) < 100
                || ballPos.dst(rightBottom) < 100 || ballPos.dst(rightTop) < 100) {
            eval += -40;
        }

        eval += playerPos.dst(ballPos);

        return playerTurn ? eval : -eval;

        //System.out.println(ballPos);
        //return -ballPos.dst2(Constants.WIN_WIDTH / 2, Constants.WIN_HEIGHT / 2);

    }

    @Override
    public GameMove[] getLegalMoves() {
        Move[] avaliableDirection = new Move[]{
                new Move(new Vector2(-1, 0)),
                new Move(new Vector2(0, -1)),
                new Move(new Vector2(0, 1)),
                new Move(new Vector2(1, 0))
        };

        Move[] avaliableDirectionPlayer = new Move[]{
                new Move(new Vector2(-1, -1)),
                new Move(new Vector2(1, 1)),
                new Move(new Vector2(-1, 1)),
                new Move(new Vector2(1, -1)),
                new Move(new Vector2(-1, 0)),
                new Move(new Vector2(0, -1)),
                new Move(new Vector2(0, 1)),
                new Move(new Vector2(1, 0))
        };

        return playerTurn ? avaliableDirectionPlayer : avaliableDirection;
    }

    @Override
    public GameState deepCopy() {
        return new State(this);
    }
}
