package me.exerosis.spartangame.util.redis;

import android.os.Bundle;
import android.util.Log;

import me.exerosis.spartangame.util.Redis;
import redis.clients.jedis.Jedis;

/**
 * Created by The Exerosis on 7/22/2015.
 */
public class RedisMessager {

    public static void sendMessage(final String channel, final String message, final Bundle bundle) {
        Log.e("MESSAGE_SENT!", "Channel: " + channel + " Message: " + message);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Jedis jedis = Redis.get(bundle);
                try {
                    jedis.publish("android." + channel, message);
                } catch (Exception ignored) {
                  //  jedis.quit();
                    jedis = Redis.get(bundle);
                    //jedis.publish("android." + channel, message);
                }
            }
        }).start();
    }
}
