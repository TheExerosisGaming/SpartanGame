package me.exerosis.spartangame.game.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

import me.exerosis.spartangame.game.GameView;

/**
 * Created by student on 7/21/2015.
 */
public abstract class EntityTest {
    private static List<EntityTest> instances = new ArrayList<>();
    public static final int GRAVITY = 10;
    public static final int TERMINAL_VELOCITY = 300;
    private Bitmap bitmap;
    private int y, x;
    private int xVelocity;
    private int yVelocity;
    private Rect rectangle;
    private int height, width;

    static {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    for (EntityTest entity : instances) {
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

    public EntityTest(Bitmap texture, int x, int y) {
        bitmap = texture;
        height = bitmap.getHeight();
        width = bitmap.getWidth();
        this.x = x;
        this.y = y;
        setRectangle(x, y, height, width);
    }

    public Rect getRectangle() {
        return rectangle;
    }

    public void setRectangle(int x, int y, int height, int width) {
        rectangle = new Rect(x, y + height, x + width, y);
    }

    public boolean contains(int x, int y) {
        if (x > getRectangle().left && x < getRectangle().right &&
                y > getRectangle().bottom && y < getRectangle().top) {
            return true;
        } else {
            return false;
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y, null);
        x += xVelocity;
        y += yVelocity;
    }

    public void activateGravity(boolean activate) {
        if (activate) {
            instances.add(this);
        } else {
            instances.remove(this);
        }
    }

}
