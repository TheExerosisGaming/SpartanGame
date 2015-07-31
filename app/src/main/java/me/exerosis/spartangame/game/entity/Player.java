package me.exerosis.spartangame.game.entity;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.UUID;

import me.exerosis.spartangame.game.entity.NetworkEntity;
import me.exerosis.spartangame.menu.MainActivity;
import me.exerosis.spartangame.util.redis.RedisMessageListener;
import me.exerosis.spartangame.util.redis.RedisMessager;

/**
 * Created by Exerosis on 7/26/2015.
 */
public class Player {
    private NetworkEntity entity;
    private int health = 20;
    private int team = 0;
    private int direction = 1;


    public Player(int texture, int x, int y, int layer) {
        entity = NetworkEntity.newInstance(texture, x, y, layer);
        entity.setGravity(false);

        new RedisMessageListener("game.damaged") {
            @Override
            public void onMessage(String message) {
                String[] parts = message.split(":");
                if (UUID.fromString(parts[0]).equals(entity.getUUID().toString()))
                    setHealth(getHealth() - Integer.valueOf(parts[1]));
            }
        };
    }

    public void setGravity(boolean activate) {
        entity.setGravity(activate);
    }

    public Rect getRectangle() {
        return entity.getRectangle();
    }

    public void setRectangle(int x, int y, int height, int width) {
        entity.setRectangle(x, y, height, width);
    }

    public boolean contains(int x, int y) {
        return entity.contains(x, y);
    }

    public int getX() {
        return entity.getX();
    }

    public void setX(int x) {
        entity.setX(x);
    }

    public void setY(int y) {
        entity.setY(y);
    }

    public int getY() {
        return entity.getY();
    }

    public int getLayer() {
        return entity.getLayer();
    }

    public int getXVelocity() {
        return entity.getXVelocity();
    }

    public int getYVelocity() {
        return entity.getYVelocity();
    }

    public void setXVelocity(int xVel) {
        entity.setXVelocity(xVel);
    }

    public void setYVelocity(int yVel) {
        entity.setYVelocity(yVel);
    }

    public Bitmap getBitmap() {
        return entity.getBitmap();
    }

    public UUID getUUID() {
        return entity.getUUID();
    }

    public UUID getPairUUID() {
        return entity.getPairUUID();
    }

    public void setBitmap(Bitmap bitmap) {
        entity.setBitmap(bitmap);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void damage() {
        damage(1);
    }

    public void damage(int amount) {
        setHealth(getHealth() - amount);
    }

    public int getTeam() {
        return team;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}