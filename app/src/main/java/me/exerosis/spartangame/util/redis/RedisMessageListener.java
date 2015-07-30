package me.exerosis.spartangame.util.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gov.pppl.androidmessaginglibrary.AndroidMessagingAPI;
import gov.pppl.androidmessaginglibrary.event.redis.RedisMessageReceivedEvent;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/**
 * Created by The Exerosis on 7/20/2015.
 */
public class RedisMessageListener {
    public static Map<List<String>, RedisMessageListener> messageListeners = new HashMap<>();

    static {
        setupListener();
    }

    public RedisMessageListener(final String... channels) {
        List<String> channelList = new ArrayList<>();

        for (String channel : channels)
            channelList.add("android.".concat(channel));

        messageListeners.put(channelList, this);
    }

    public void onMessage(String message) {
    }

    public void onMessage(String channel, String message) {
    }

    public void onSubscribe(String channel) {
    }

    public void onUnsubscribe(String channel) {
    }

    private static JedisPubSub getNewListener() {
        return new JedisPubSub() {
            @Override
            public void onUnsubscribe(String channel, int subscribedChannels) {
                for (Map.Entry<List<String>, RedisMessageListener> entry : messageListeners.entrySet())
                    if (entry.getKey().contains(channel))
                        entry.getValue().onUnsubscribe(channel);
            }

            @Override
            public void onSubscribe(String channel, int subscribedChannels) {
                for (Map.Entry<List<String>, RedisMessageListener> entry : messageListeners.entrySet())
                    if (entry.getKey().contains(channel))
                        entry.getValue().onSubscribe(channel);
            }

            @Override
            public void onMessage(String channel, String message) {
                AndroidMessagingAPI.getEventManager().callEvent(new RedisMessageReceivedEvent(message, channel));
            }

            @Override
            public void onPUnsubscribe(String pattern, int subscribedChannels) {
            }

            @Override
            public void onPMessage(String pattern, String channel, String message) {
                AndroidMessagingAPI.getEventManager().callEvent(new RedisMessageReceivedEvent(message, channel));
                for (Map.Entry<List<String>, RedisMessageListener> entry : messageListeners.entrySet())
                    if (entry.getKey().contains(channel)) {
                        if (entry.getKey().size() > 1)
                            entry.getValue().onMessage(channel, message);
                        else
                            entry.getValue().onMessage(message);
                    }
            }

            @Override
            public void onPSubscribe(String pattern, int subscribedChannels) {
            }
        };
    }

    private static void setupListener() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    Jedis jedis = new Jedis(AndroidMessagingAPI.REDIS_IP);
                    jedis.psubscribe(getNewListener(), "android.*");
                    jedis.quit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
}
