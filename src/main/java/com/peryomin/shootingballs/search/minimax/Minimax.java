package main.java.com.peryomin.shootingballs.search.minimax;

public class Minimax {

    private final float INF = Float.MAX_VALUE / 2;

    private float alphaBeta(float alpha, float beta, int depth, GameState state) {
        if (depth <= 0 || state.isTerminal()) {
            return state.evaluate();
        }

        GameMove[] moves =  state.getLegalMoves();

        float score;
        float bestScore = -INF;
        GameMove bestMove;

        for (GameMove nextMove : moves) {
            GameState nextState = state.deepCopy();
            nextState.makeMove(nextMove);
            score = -alphaBeta(-beta, -alpha, depth - 1, nextState);

            if (score > bestScore) {
                bestScore = score;
                bestMove = nextMove;
                bestMove.score = score;

                if (score > alpha) {
                    if (score >= beta) {
                        return beta;
                    }
                    alpha = score;
                }
            }
        }

        return alpha;
    }

    public GameMove search(GameState state, int depth) {
        assert (!state.isTerminal());
        assert (depth > 0);

        GameMove[] moves =  state.getLegalMoves();

        float score;
        float bestScore = -INF;
        GameMove bestMove = null;

        for (GameMove nextMove : moves) {
            GameState nextState = state.deepCopy();
            nextState.makeMove(nextMove);
            score = alphaBeta(-INF, INF, depth - 1, nextState);

            if (score > bestScore) {
                bestScore = score;
                bestMove = nextMove;
                bestMove.score = score;
            }
        }

        return bestMove;
    }

}
