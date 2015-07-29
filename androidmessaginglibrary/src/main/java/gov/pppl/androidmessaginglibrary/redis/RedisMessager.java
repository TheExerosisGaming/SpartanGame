package gov.pppl.androidmessaginglibrary.redis;

import gov.pppl.androidmessaginglibrary.AndroidMessagingAPI;
import redis.clients.jedis.Jedis;

/**
 * Created by The Exerosis on 7/22/2015.
 */
public class RedisMessager {

    public static void sendMessage(final String channel, final String message){
        new Thread(new Runnable(){
            @Override
            public void run() {
                Jedis jedis = new Jedis(AndroidMessagingAPI.REDIS_IP);
                jedis.publish("android.".concat(channel), message);
                jedis.quit();
            }
        }).start();
    }
}
