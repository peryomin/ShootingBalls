package main.java.com.peryomin.shootingballs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

class Game extends JPanel {

    public enum GameMode {AGAINST_SIMPLE_AI, AGAINST_MINIMAX_AI, AI_VS_AI}
    private GameMode mode = GameMode.AGAINST_SIMPLE_AI;

    private Player player;
    private Ball ball;
    long counter = 0;

    private boolean running = true;
    private int fps = 30;
    private int frameCount = 0;
    private float timeLeft = Constants.WIN_WIDTH;

    Game() {
        setBackground(Color.black);
        setDoubleBuffered(true);
        player = new Player(mode);
        ball = new Ball(mode);
        addKeyListener(new KeyboardAdapter());
        setFocusable(true);

        System.out.println("player pos = " + player.position);
        System.out.println("ball pos = " + ball.position);
        startGameLoop();
    }

    public void startGameLoop() {
        player.position = new Vector2(Constants.WIN_WIDTH / 2, Constants.WIN_HEIGHT / 2);
        player.velocity = new Vector2(0, 0);
        player.mode = mode;
        ball.position = new Vector2(Constants.WIN_WIDTH / 4, Constants.WIN_HEIGHT / 4);
        ball.velocity = new Vector2(0, 0);
        ball.mode = mode;
        timeLeft = Constants.WIN_WIDTH;
        running = true;
        Thread loop = new Thread(this::gameLoop);
        loop.start();
    }

    private void updateGame() {
        update();
    }

    private void drawGame(float interpolation) {
        player.draw(interpolation);
        ball.draw(interpolation);
        repaint();
    }

    public void update() {
        counter++;
        ball.chooseNextAction(player, 6);

        if (mode == GameMode.AI_VS_AI) {
            player.chooseNextAction(ball, 8);
        }
        ball.update(getWidth(), getHeight());
        player.update(getWidth(), getHeight());
        timeLeft -= 1;
    }

    private boolean gameIsOver() {
        return timeLeft <= 0 || player.position.dst(ball.position)
                < Constants.PLAYER_WIDTH / 2 + Constants.BALL_WIDTH / 2;
    }

    @Override
    public void paintComponent(Graphics g) {
        //BS way of clearing out the old rectangle to save CPU.
        g.setColor(getBackground());
        /*g.fillRect((int)player.lastPosition.x - 1,
                (int)player.lastPosition.y - 1,
                player.ballWidth + 2,
                player.ballHeight + 2);
        g.fillRect(5, 0, 75, 30);*/
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.RED);
        /*g.fillOval((int) player.position.x - player.getWidth() / 2,
                (int) player.position.y - player.getHeight() / 2,
                player.getWidth(), player.getHeight());*/
        g.setColor(Color.WHITE);
        g.drawString("FPS: " + fps, 5, 10);

        g.fillRect(0,
                Constants.WIN_HEIGHT - 40,
                Constants.WIN_WIDTH - (int) timeLeft,
                10);


        //System.out.println(player.position);

        g.drawImage(player.getImage(), (int)player.position.x - player.getWidth() / 2,
                (int)player.position.y - player.getHeight() / 2,
                player.getWidth(), player.getHeight(), this);

        g.drawImage(ball.getImage(), (int)ball.position.x - ball.getWidth() / 2,
                (int)ball.position.y - ball.getHeight() / 2,
                ball.getWidth(), ball.getHeight(), this);

/*
        g.setColor(Color.CYAN);
        if (ball.ai.expectedPosf1 != null)
        g.fillOval((int)ball.ai.expectedPosf1.x - ball.getWidth() / 2,
                (int)ball.ai.expectedPosf1.y - ball.getHeight() / 2,
                ball.getWidth(), ball.getHeight());
        g.fillOval((int)ball.ai.pexpectedPosf1.x - player.getWidth() / 2,
                (int)ball.ai.pexpectedPosf1.y - player.getHeight() / 2,
                player.getWidth(), player.getHeight());


        g.setColor(Color.WHITE);
        g.fillOval((int)ball.ai.expectedPosf2.x - ball.getWidth() / 2,
                (int)ball.ai.expectedPosf2.y - ball.getHeight() / 2,
                ball.getWidth(), ball.getHeight());
        g.fillOval((int)ball.ai.pexpectedPosf2.x - player.getWidth() / 2,
                (int)ball.ai.pexpectedPosf2.y - player.getHeight() / 2,
                player.getWidth(), player.getHeight());

        g.setColor(Color.BLUE);
        g.fillOval((int)ball.ai.expectedPosf3.x - ball.getWidth() / 2,
                (int)ball.ai.expectedPosf3.y - ball.getHeight() / 2,
                ball.getWidth(), ball.getHeight());
        g.fillOval((int)ball.ai.pexpectedPosf3.x - player.getWidth() / 2,
                (int)ball.ai.pexpectedPosf3.y - player.getHeight() / 2,
                player.getWidth(), player.getHeight());

        g.setColor(Color.green);
        g.fillOval((int)ball.ai.expectedPosf4.x - ball.getWidth() / 2,
                (int)ball.ai.expectedPosf4.y - ball.getHeight() / 2,
                ball.getWidth(), ball.getHeight());
        g.fillOval((int)ball.ai.pexpectedPosf4.x - player.getWidth() / 2,
                (int)ball.ai.pexpectedPosf4.y - player.getHeight() / 2,
                player.getWidth(), player.getHeight());

        g.setColor(Color.WHITE);
*/
        /*g.drawLine((int) ball.position.x,
                (int) ball.position.y,
                (int) (ball.getEcpectedPos().x),
                (int) (ball.getEcpectedPos().y ));*/

        g.drawLine((int) player.position.x,
                (int) player.position.y,
                (int) (player.velocity.x * 5 + player.position.x),
                (int) (player.velocity.y * 5 + player.position.y));

        g.drawLine((int) ball.position.x,
                (int) ball.position.y,
                (int) (ball.velocity.x * 3 + ball.position.x),
                (int) (ball.velocity.y * 3 + ball.position.y));

        if (gameIsOver()) {
            System.out.println("counter " + counter);
            int line = 20;
            int left = 150;
            int top = 50;
            g.drawString("THE GAME IS OVER!\n YOUR SCORE IS " + (timeLeft >= 0 ? (int) timeLeft : 0),
                    Constants.WIN_WIDTH / 2 - left, Constants.WIN_HEIGHT / 2 - line - top);
            g.drawString("press \"space\" to try again",
                    Constants.WIN_WIDTH / 2 - left, Constants.WIN_HEIGHT / 2 - top);
            g.drawString("press \"1\" to play against simple AI",
                    Constants.WIN_WIDTH / 2 - left, Constants.WIN_HEIGHT / 2 + line - top);
            g.drawString("press \"2\" to play against a bit different AI",
                    Constants.WIN_WIDTH / 2 - left, Constants.WIN_HEIGHT / 2 + line * 2 - top);
            g.drawString("press \"3\" if you wanna see this stupid AI plays against itself :)",
                    Constants.WIN_WIDTH / 2 - left, Constants.WIN_HEIGHT / 2 + line * 3 - top);
        }

        frameCount++;
    }

    private void gameLoop() {
        final int NANO_SEC = 1000000000;
        final int MAX_UPDATES_BEFORE_RENDER = 5;
        final double GAME_HERTZ = 30.0;
        final double TIME_BETWEEN_UPDATES = NANO_SEC / GAME_HERTZ;

        double lastUpdateTime = System.nanoTime();
        double lastRenderTime;

        final double TARGET_FPS = 60;
        final double TARGET_TIME_BETWEEN_RENDERS = NANO_SEC / TARGET_FPS;

        int lastSecondTime = (int) (lastUpdateTime / NANO_SEC);

        while (running) {
            double now = System.nanoTime();

            if (gameIsOver()) {
                running = false;
            }

            int updateCount = 0;

            while (now - lastUpdateTime > TIME_BETWEEN_UPDATES
                    && updateCount < MAX_UPDATES_BEFORE_RENDER) {
                updateGame();
                lastUpdateTime += TIME_BETWEEN_UPDATES;
                updateCount++;
            }

            if (now - lastUpdateTime > TIME_BETWEEN_UPDATES) {
                lastUpdateTime = now - TIME_BETWEEN_UPDATES;
            }

            float interpolation = Math.min(1.0f, (float) ((now - lastUpdateTime) / TIME_BETWEEN_UPDATES));
            drawGame(interpolation);
            lastRenderTime = now;

            int thisSecond = (int) (lastUpdateTime / NANO_SEC);
            if (thisSecond > lastSecondTime) {
                fps = frameCount;
                frameCount = 0;
                lastSecondTime = thisSecond;
            }

            // This saves the CPU from hogging.
            while (now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS
                    && now - lastUpdateTime < TIME_BETWEEN_UPDATES) {
                Thread.yield();
                try {Thread.sleep(1);} catch(Exception ignored) {}
                now = System.nanoTime();
            }
        }
    }

    private class KeyboardAdapter extends KeyAdapter {
        private Set<Integer> pressedKeys = new HashSet<>();

        @Override
        public void keyPressed(KeyEvent e) {

            if (e.getKeyCode() == KeyEvent.VK_SPACE && gameIsOver()) {
                startGameLoop();
            } else if (e.getKeyCode() == KeyEvent.VK_1 && gameIsOver()) {
                mode = GameMode.AGAINST_SIMPLE_AI;
                startGameLoop();
            } else if (e.getKeyCode() == KeyEvent.VK_2 && gameIsOver()){
                mode = GameMode.AGAINST_MINIMAX_AI;
                startGameLoop();
            } else if (e.getKeyCode() == KeyEvent.VK_3 && gameIsOver()){
                mode = GameMode.AI_VS_AI;
                startGameLoop();
            }

            pressedKeys.add(e.getKeyCode());
            player.keyPressed(pressedKeys);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            pressedKeys.remove(e.getKeyCode());
            player.keyReleased(e);
        }
    }

}
