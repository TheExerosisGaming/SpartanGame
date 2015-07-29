package gov.pppl.game.player;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import gov.pppl.androidmessaginglibrary.redis.RedisMessager;

/**
 * Created by The Exerosis on 7/26/2015.
 */
public class Player extends Entity {
    private String playerName;

    public Player(Bitmap bitmap, int x, int y, String name){
        super(bitmap, x, y);
        playerName = name;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(getBitmap(), getX(), getY(), null);
        setXVelocity(getXVelocity());
    }

    private void updateLocation(){
        RedisMessager.sendMessage("android." + playerName + "Move", getX() + ":" + getY());
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        updateLocation();
    }

    @Override
    public void setY(int y) {
        super.setY(y);
        updateLocation();
    }

    public String getPlayerName() {
        return playerName;
    }
}