package gov.pppl.blah.GUI;

import android.graphics.Bitmap;

/**
 * Created by The Exerosis on 7/26/2015.
 */
public class Player extends Entity {
    private String playerName;
    private int health = 100;
    private int team = 0;
    private int direction = 0;

    public Player(Bitmap bitmap, int x, int y, String name){
        super(bitmap, x, y);
        playerName = name;
        activateGravity(true);
    }

    private void update(){
        //RedisMessager.sendMessage("android." + playerName + "Move", getX() + ":" + getY());
        //RedisMessager.sendMessage("android." + playerName + "Health", getHealth());
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        update();
    }

    @Override
    public void setY(int y) {
        super.setY(y);
        update();
    }

    public int getHealth(){ return health; }

    public void die(){
        health = 0;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getTeam(){
        return team;
    }

    public int getDirection(){
        return direction;
    }

    public void setDirection(int direc){
        direction = direc;
    }
}