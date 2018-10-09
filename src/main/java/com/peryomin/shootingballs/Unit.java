package main.java.com.peryomin.shootingballs;

import java.awt.*;

public abstract class Unit {
    protected Image image;

    protected int width;
    protected int height;

    protected Vector2 velocity;
    protected Vector2 position;
    protected Vector2 lastPosition;
    protected Vector2 left;
    protected Vector2 right;
    protected Vector2 up;
    protected Vector2 down;

    protected float speed = .5f;
    protected float minSpeed = .5f;
    public static final float minVelocity = -15f;
    public static final float maxVelocity =  15f;

    public Unit() {
    }

    abstract void updateVelocity();

    public void update(float winWidth, float winHeight) {

        updateVelocity();

        if (velocity.x > 0) {
            velocity.x = Math.min(velocity.x, maxVelocity);
        }

        if (velocity.x < 0) {
            velocity.x = Math.max(velocity.x, minVelocity);
        }

        if (velocity.y > 0) {
            velocity.y = Math.min(velocity.y, maxVelocity);
        }

        if (velocity.y < 0) {
            velocity.y = Math.max(velocity.y, minVelocity);
        }

        velocity.scl(0.98f);

        if (position.x + this.width / 2 >= winWidth) {
            velocity.x *= -1;
            position.x = winWidth - this.width / 2;
        } else if (position.x - this.width / 2 <= 0) {
            velocity.x *= -1;
            position.x = this.width / 2;
        }

        if (position.y + this.height / 2 >= winHeight) {
            velocity.y *= -1;
            position.y = winHeight - this.height / 2;
        } else if (position.y - this.height / 2 <= 0) {
            position.y = this.width / 2;
            velocity.y *= -1;
        }
    }

    public void draw(float interpolation) {
        speed = Math.max(minSpeed, speed * (float) Math.pow(0.97f, interpolation * 2.0f));
        lastPosition.set(position);
        position.add(velocity.x * speed * interpolation, velocity.y * speed * interpolation);
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    public void setPosition(Vector2 pos) {
        this.position = pos;
    }

    public Vector2 getPosition () {
        return position;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public Vector2 getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(Vector2 lastPosition) {
        this.lastPosition = lastPosition;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getMinSpeed() {
        return minSpeed;
    }

    public void setMinSpeed(float minSpeed) {
        this.minSpeed = minSpeed;
    }
}