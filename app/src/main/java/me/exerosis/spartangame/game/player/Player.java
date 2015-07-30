package me.exerosis.spartangame.game.player;

import android.graphics.Bitmap;

import me.exerosis.spartangame.menu.MainActivity;
import me.exerosis.spartangame.util.redis.RedisMessager;
import me.exerosis.spartangame.game.entity.Entity;

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

    private void updateLocation(){
        RedisMessager.sendMessage("android.move", getPlayerName() + ":" + getX() + ":" + getY(), MainActivity.getSettings());
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

    public int getHealth(){ return health; }

    public void setHealth(int health) {
        this.health = health;
        RedisMessager.sendMessage("android.health", getPlayerName() + ":" + getHealth(), MainActivity.getSettings());
    }

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

    public void setDirection(int direction){
        this.direction = direction;
    }
}