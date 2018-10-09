package main.java.com.peryomin.shootingballs;

import java.util.Random;

public class BallAI {
    Vector2[] avaliableDirection = new Vector2[]{
            new Vector2(-1, -1),
            new Vector2(-1, 0),
            new Vector2(0, -1),
            //new Vector2(0, 0),
            new Vector2(0, 1),
            new Vector2(1, 0),
            new Vector2(1, 1),
            new Vector2(1, -1),
            new Vector2(-1, 1),
    };

    public Vector2 expectedPos1;
    public Vector2 expectedPos2;
    public Vector2 expectedPos3;
    public Vector2 expectedPos4;
    public Vector2 expectedPosf1;
    public Vector2 expectedPosf2;
    public Vector2 expectedPosf3;
    public Vector2 expectedPosf4;
    public Vector2 pexpectedPos1;
    public Vector2 pexpectedPos2;
    public Vector2 pexpectedPos3;
    public Vector2 pexpectedPos4;
    public Vector2 pexpectedPosf1;
    public Vector2 pexpectedPosf2;
    public Vector2 pexpectedPosf3;
    public Vector2 pexpectedPosf4;

    final float INTER = 4.5f;
    float interpolation = 4.5f;

    public static class Action {
        boolean boost;
        Vector2 direction;

        public Action(boolean boost, Vector2 direction) {
            this.boost = boost;
            this.direction = direction;
        }


        @Override
        public String toString() {
            if (direction.y > 0) {
                return "DOWN";
            } else if (direction.y < 0) {
                return "UP";
            } else if (direction.x < 0) {
                return "LEFT";
            } else if (direction.x > 0) {
                return "RIGHT";
            }

            return "ERROR";
        }
    }

    Action getRandomAction() {
        Random r = new Random(System.currentTimeMillis());
        return new Action(r.nextBoolean(), avaliableDirection[r.nextInt(avaliableDirection.length)]);
    }

    private void emulate(Ball ball, Vector2 velocity, Vector2 position, Vector2 pVelocity, Vector2 pPosition,
                         float speed, Action action, Action pAction) {
        velocity.add(action.direction);
        pVelocity.add(pAction.direction);

        if (velocity.x > 0) {
            velocity.x = Math.min(velocity.x, ball.maxVelocity);
        }

        if (velocity.x < 0) {
            velocity.x = Math.max(velocity.x, ball.minVelocity);
        }

        if (velocity.y > 0) {
            velocity.y = Math.min(velocity.y, ball.maxVelocity);
        }

        if (velocity.y < 0) {
            velocity.y = Math.max(velocity.y, ball.minVelocity);
        }

        velocity.scl(0.98f);
        //pVelocity.scl(0.98f);
        //pVelocity.sub(position.cpy().nor());
        float r = ball.width / 2;
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

        // Speed does not change because no boost is allowed
        //speed = Math.max(ball.minSpeed, speed * (float) Math.pow(0.97f, interpolation * 2.0f));
        position.add(velocity.x * speed * interpolation, velocity.y * speed * interpolation);
        pPosition.add(pVelocity.x * 0.5f * interpolation, pVelocity.y * 0.5f * interpolation);
    }

    float evaluate(Vector2 pPos, Vector2 ballPos) {
        //return pPos.dst(ballPos);


        float eval = 0;

        Vector2 leftTop = new Vector2(0, 0);
        Vector2 leftBottom = new Vector2(0, Constants.WIN_HEIGHT);
        Vector2 rightTop = new Vector2(Constants.WIN_WIDTH, 0);
        Vector2 rightBottom = new Vector2(Constants.WIN_WIDTH, Constants.WIN_HEIGHT);

        if (ballPos.dst(leftTop) < 100 || ballPos.dst(leftBottom) < 100
                || ballPos.dst(rightBottom) < 100 || ballPos.dst(rightTop) < 100) {
            eval += -40;
        }

        eval += pPos.dst(ballPos);

        return eval;
    }

    boolean collide(Vector2 pos1, Vector2 pos2, float dist) {
        return pos1.dst(pos2) < dist;
    }

    Vector2 choosePlayerAction(Vector2 ballPos, Vector2 playerPos, Vector2 playerVel) {
        int best = 0;
        float bestValue = Float.MAX_VALUE;
        for (int i = 0; i < avaliableDirection.length; i++) {
            Vector2 curVel = playerVel.cpy();
            Vector2 curPos = playerPos.cpy();
            curVel.add(avaliableDirection[i]);
            curPos.add(curVel.x * 0.5f * interpolation, curVel.y * 0.5f * interpolation);

            if (ballPos.dst2(curPos) < bestValue) {
                bestValue = ballPos.dst2(curPos);
                best = i;
            }
        }
        return avaliableDirection[best];
    }

    Action getAction(Player player, Ball ball) {
        float bestValue = Float.MIN_VALUE;
        //float bestValue = Float.MAX_VALUE;
        Action bestAction = new Action(false, avaliableDirection[3]);
        for (int i = 0; i < avaliableDirection.length; i++) {
            for (int j = 0; j < avaliableDirection.length; j++) {
                for (int k = 0; k < avaliableDirection.length; k++) {
                    for (int h = 0; h < avaliableDirection.length; h++) {

                        Vector2 velocity = new Vector2(ball.velocity);
                        Vector2 position = new Vector2(ball.position);
                        Float speed = ball.speed;

                        Vector2 pVelocity = new Vector2(player.velocity);
                        Vector2 pPosition = new Vector2(player.position);

                        Action action1 = new Action(false, avaliableDirection[i]);
                        Action action2 = new Action(false, avaliableDirection[j]);
                        Action action3 = new Action(false, avaliableDirection[k]);
                        Action action4 = new Action(false, avaliableDirection[h]);

                        interpolation = INTER;

                        Action playerAction1 = new Action(false, choosePlayerAction(position, pPosition, pVelocity));
                        emulate(ball, velocity, position, pVelocity, pPosition, speed, action1, playerAction1);
                        expectedPos1 = position.cpy();
                        pexpectedPos1 = pPosition.cpy();
                        if (collide(position, pPosition, ball.width / 1.8f + player.width / 1.8f)) continue;
                        //interpolation *= 1.5;

                        Action playerAction2 = new Action(false, choosePlayerAction(position, pPosition, pVelocity));
                        emulate(ball, velocity, position, pVelocity, pPosition, speed, action2, playerAction2);
                        expectedPos2 = position.cpy();
                        pexpectedPos2 = pPosition.cpy();
                        if (collide(position, pPosition, ball.width / 1.8f + player.width / 1.8f)) continue;
                        //interpolation *= 1.5;

                        Action playerAction3 = new Action(false, choosePlayerAction(position, pPosition, pVelocity));
                        emulate(ball, velocity, position, pVelocity, pPosition, speed, action3, playerAction3);
                        expectedPos3 = position.cpy();
                        pexpectedPos3 = pPosition.cpy();
                        if (collide(position, pPosition, ball.width / 1.8f + player.width / 1.8f)) continue;
                        //interpolation *= 1.5;

                        Action playerAction4 = new Action(false, choosePlayerAction(position, pPosition, pVelocity));
                        emulate(ball, velocity, position, pVelocity, pPosition, speed, action4, playerAction4);
                        expectedPos4 = position.cpy();
                        pexpectedPos4 = pPosition.cpy();
                        if (collide(position, pPosition, ball.width / 1.8f + player.width / 1.8f)) continue;

                        float value = evaluate(pPosition, position);

                        if (value > bestValue) {
                            bestAction = action1;
                            bestValue = value;
                            expectedPosf1 = expectedPos1.cpy();
                            expectedPosf2 = expectedPos2.cpy();
                            expectedPosf3 = expectedPos3.cpy();
                            expectedPosf4 = expectedPos4.cpy();
                            pexpectedPosf1 = pexpectedPos1.cpy();
                            pexpectedPosf2 = pexpectedPos2.cpy();
                            pexpectedPosf3 = pexpectedPos3.cpy();
                            pexpectedPosf4 = pexpectedPos4.cpy();
                        }

                    }
                }
            }
        }
        return bestAction;
    }
}