package gov.pppl.game.game.networking;

import android.graphics.Bitmap;

import gov.pppl.androidmessaginglibrary.redis.listeners.RedisMessageListener;

public class GameListener {
    public GameListener(final Bitmap bitmap) {
        new RedisMessageListener("game") {
            @Override
            public void onMessage(String message) {
                String[] contents = message.split(":");
                if (contents[0].equals("start"))
                    gameStart(bitmap, contents[1]);
                else if (contents[0].equals("join"))
                    gameJoin(bitmap, contents[1]);
                else
                    return;
            }
        };
    }

    public void gameStart(Bitmap map, String player) {
    }

    public void gameJoin(Bitmap map, String player) {
    }
}