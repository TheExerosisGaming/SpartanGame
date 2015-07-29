package gov.pppl.game.player;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by student on 7/21/2015.
 */
public abstract class Entity {
    private Bitmap bitmap;
    private int y, x;
    private int xVel = 0, yVel = 0;

    public Entity(Bitmap texture, int x, int y){
        bitmap = texture;
        this.x = x;
        this.y = y;
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

    public Bitmap getBitmap(){
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(bitmap, x, y, null);
    }

}
