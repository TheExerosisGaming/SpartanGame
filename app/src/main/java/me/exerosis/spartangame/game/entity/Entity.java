package me.exerosis.spartangame.game.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.TreeSet;

import me.exerosis.spartangame.game.GameView;

/**
 * Created by student on 7/21/2015.
 */
public abstract class Entity implements Comparable<Entity> {

    private static TreeSet<Entity> instances = new TreeSet<>();

    private Bitmap bitmap;
    private int y, x;
    private int xVelocity;
    private int yVelocity;
    private Rect rectangle;
    private int layer;

    private static final int JUMP_LIMIT = GameView.getScreenHeight() / 4;
    private static final int TERMINAL_VELOCITY = GameView.getScreenHeight() / 20;
    private static final int GRAVITY = GameView.getScreenHeight() / 25;
    private static final int JUMP_FORCE = GameView.getScreenHeight() / 200;
    private static boolean jumping = false;

    static {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    for (Entity entity : instances) {
                        if (jumping) {
                            if (entity.y > GameView.getScreenHeight() - entity.bitmap.getHeight() - JUMP_LIMIT && entity.yVelocity > -TERMINAL_VELOCITY) {
                                entity.yVelocity -= JUMP_FORCE;
                            }

                            if(entity.y <= GameView.getScreenHeight()- entity.bitmap.getHeight() - JUMP_LIMIT){
                                jumping = false;
                            }

                        } else {
                            if (entity.y < GameView.getScreenHeight() - entity.bitmap.getHeight() && entity.yVelocity < TERMINAL_VELOCITY) {
                                entity.yVelocity += GRAVITY;
                            } else if (entity.y > GameView.getScreenHeight() - entity.bitmap.getHeight()) {
                                entity.y = GameView.getScreenHeight() - entity.bitmap.getHeight();
                                entity.yVelocity = 0;
                            }
                        }
                    }
                }
            }
        }.start();
    }

    public static TreeSet<Entity> getInstances() {
        return instances;
    }

    public Entity(Bitmap bitmap, int x, int y, int layer) {
        this.layer = layer;
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;

        setRectangle(x, y, bitmap.getHeight(), bitmap.getWidth());
        instances.add(this);
    }

    public Rect getRectangle() {
        return rectangle;
    }

    public void setRectangle(int x, int y, int height, int width) {
        rectangle = new Rect(x, y + height, x + width, y);
    }

    public boolean contains(int x, int y) {
        return x > getRectangle().left && x < getRectangle().right && y > getRectangle().bottom && y < getRectangle().top;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public int getLayer() {
        return layer;
    }

    public int getXVelocity() {
        return xVelocity;
    }

    public int getYVelocity() {
        return yVelocity;
    }

    public void setXVelocity(int xVel) {
        this.xVelocity = xVel;
    }

    public void setYVelocity(int yVel) {
        this.yVelocity = yVel;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void activateGravity(boolean activate) {
        if (activate) {
            instances.add(this);
        } else {
            instances.remove(this);
        }
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y, null);
        x += xVelocity;
        y += yVelocity;
    }

    @Override
    public int compareTo(Entity another) {
        return Integer.compare(layer, another.getLayer());
    }

    public void jump() {
        if(y >= GameView.getScreenHeight() - bitmap.getHeight()) {
            jumping = true;
        }
    }
}
