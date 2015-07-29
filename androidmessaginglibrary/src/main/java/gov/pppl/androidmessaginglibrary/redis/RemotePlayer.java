package gov.pppl.androidmessaginglibrary.redis;

import gov.pppl.androidmessaginglibrary.redis.listeners.RedisMessageListener;

/**
 * Created by The Exerosis on 7/22/2015.
 */
public class RemotePlayer {
    int x, y;

    public RemotePlayer(String name) {
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
}
