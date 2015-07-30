package me.exerosis.spartangame.game.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.UUID;

import me.exerosis.spartangame.game.GameView;

/**
 * Created by student on 7/21/2015.
 */
public abstract class Entity implements Comparable<Entity> {
    private static TreeSet<Entity> instances = new TreeSet<>();

    public static final int GRAVITY = 10;
    public static final int TERMINAL_VELOCITY = 300;
    private Bitmap bitmap;
    private int y, x;
    private int xVelocity;
    private int yVelocity;
    private Rect rectangle;
    private int layer;

    static {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    for (Entity entity : instances) {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (entity.y + entity.bitmap.getHeight() < GameView.getScreenHeight())
                            entity.yVelocity += GRAVITY;
                        else


                        if (entity.yVelocity < TERMINAL_VELOCITY)
                            entity.y -= entity.yVelocity;
                        else
                            entity.y = 0;
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
}
