package gov.pppl.blah.GUI;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

import gov.pppl.blah.GameView;

/**
 * Created by student on 7/21/2015.
 */
public abstract class Entity {

    private static List<Entity> instances = new ArrayList<>();

    private Bitmap bitmap;
    private int y, x;
    private int xVel = 0, yVel = 0;
    private Rect rectangle;
    private int height, width;

    static {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    for (Entity entity : instances) {
                        if (entity.getY() < GameView.getScreenHeight() - 450) {
                            entity.setYVelocity(entity.getYVelocity() + 1);
                        } else {
                            entity.setY(GameView.getScreenHeight() - 450);
                        }
                    }
                }
            }
        }.start();
    }

    public Entity(Bitmap texture, int x, int y) {
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
        return xVel;
    }

    public int getYVelocity() {
        return yVel;
    }

    public void setXVelocity(int xVel) {
        this.xVel = xVel;
    }

    public void setYVelocity(int yVel) {
        this.yVel = yVel;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y, null);
        x += xVel;
        y += yVel;
    }

    public void activateGravity(boolean activate) {
        if (activate) {
            instances.add(this);
        } else {
            instances.remove(this);
        }
    }

}
