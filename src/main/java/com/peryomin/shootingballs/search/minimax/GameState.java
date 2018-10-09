package main.java.com.peryomin.shootingballs.search.minimax;

public interface GameState {
    void makeMove(GameMove move);
    boolean isTerminal();
    int getWinner();
    float evaluate();
    GameMove[] getLegalMoves();
    GameState deepCopy();
}
