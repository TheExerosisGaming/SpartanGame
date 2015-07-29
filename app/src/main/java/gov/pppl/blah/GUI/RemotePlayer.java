package gov.pppl.blah.GUI;

import android.graphics.Bitmap;

import gov.pppl.androidmessaginglibrary.redis.listeners.RedisMessageListener;

public class RemotePlayer extends Entity {
    int x = 0, y = 0;
    String name;

    public RemotePlayer(Bitmap bitmap, String name) {
        super(bitmap, 0, 0);

        this.name = name;
        new RedisMessageListener(name.concat("Move")) {
            @Override
            public void onMessage(String message) {
                String[] xy = message.split(":");
                x = Integer.valueOf(xy[0]);
                y = Integer.valueOf(xy[1]);
            }
        };
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public String getName(){return name;}
}