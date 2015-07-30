package me.exerosis.spartangame.game.player;

import android.graphics.Bitmap;

import gov.pppl.androidmessaginglibrary.redis.listeners.RedisMessageListener;
import me.exerosis.spartangame.game.entity.Entity;

public class RemotePlayer extends Entity {
    private int health = 100;

    public RemotePlayer(Bitmap bitmap, final String name) {
        super(bitmap, 0, 0);
        new RedisMessageListener("move") {
            @Override
            public void onMessage(String message) {
                String[] components = message.split(":");
                if (components[0].equals(name))
                    return;
                setX(Integer.valueOf(components[1]));
                setY(Integer.valueOf(components[2]));
            }
        };
        new RedisMessageListener("health") {
            @Override
            public void onMessage(String message) {
                String[] components = message.split(":");
                if (components[0].equals(name))
                    return;
                setHealth(Integer.valueOf(components[1]));
            }
        };
    }

    public int getHealth() {
        return health;
    }
    public void setHealth(int health) {
        this.health = health;
    }
}