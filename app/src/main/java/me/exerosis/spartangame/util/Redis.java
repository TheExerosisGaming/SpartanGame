package me.exerosis.spartangame.util;

import android.os.Bundle;
import android.util.Log;

import me.exerosis.spartangame.menu.SettingsActivity;
import redis.clients.jedis.Jedis;

/**
 * Created by The Exerosis on 7/29/2015.
 */
public class Redis {
    public static Jedis jedis;
    public static int port = SettingsActivity.DEFAULT_PORT;
    public static String host = SettingsActivity.DEFAULT_HOST;

    public static Jedis get(Bundle bundle) {
        String newHost = bundle.getString(SettingsActivity.ARGS_HOST);
        int newPort = bundle.getInt(SettingsActivity.ARGS_PORT);
        if(!newHost.equals(host) || port != newPort) {
            jedis = new Jedis(newHost, newPort);
            host = newHost;
            port = newPort;
            try {
                jedis.connect();
            } catch (Exception e) {
            }
        }

        return jedis;
    }
}
