package me.exerosis.spartangame.util;

import android.os.Bundle;
import android.util.Log;

import me.exerosis.spartangame.menu.SettingsActivity;
import redis.clients.jedis.Jedis;

/**
 * Created by The Exerosis on 7/29/2015.
 */
public class Redis {
    public static Jedis get(Bundle bundle) {
        Log.e("CONNECTING", bundle.getString(SettingsActivity.ARGS_HOST) + ":" + bundle.getInt(SettingsActivity.ARGS_PORT));
        Jedis jedis = new Jedis(bundle.getString(SettingsActivity.ARGS_HOST), bundle.getInt(SettingsActivity.ARGS_PORT));
        try {
            jedis.connect();
        } catch (Exception e) {
        }
        return jedis;
    }
}
