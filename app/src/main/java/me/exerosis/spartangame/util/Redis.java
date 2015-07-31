package me.exerosis.spartangame.util;

import android.os.Bundle;
import android.util.Log;

import me.exerosis.spartangame.menu.SettingsActivity;
import redis.clients.jedis.Jedis;

/**
 * Created by The Exerosis on 7/29/2015.
 */
public class Redis {
    public static int port = 40282;
    public static String host = "grappl.io";
    public static Jedis jedis = new Jedis(host, port);

    public static Jedis get(Bundle bundle) {
        String newHost = bundle.getString(SettingsActivity.ARGS_HOST);
        int newPort = bundle.getInt(SettingsActivity.ARGS_PORT);

        if (!newHost.equals(host) || port != newPort) {
            jedis = new Jedis(newHost, newPort);
            host = newHost;
            port = newPort;
        }
        if (!jedis.isConnected())
            try {
                jedis.connect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        return jedis;
    }
}
