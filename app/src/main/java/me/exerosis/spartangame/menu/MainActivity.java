package me.exerosis.spartangame.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import gov.pppl.blah.R;
import me.exerosis.spartangame.game.GameActivity;
import me.exerosis.spartangame.game.entity.NetworkEntity;
import me.exerosis.spartangame.game.entity.Player;
import me.exerosis.spartangame.util.ExActivity;
import me.exerosis.spartangame.util.Redis;
import me.exerosis.spartangame.util.redis.RedisMessageListener;
import redis.clients.jedis.Jedis;

public class MainActivity extends ExActivity {
    public static final int EDIT_SETTINGS_REQUEST = 1;
    public static final int HOST_MENU_REQUEST = 2;
    public static final int JOIN_MENU_REQUEST = 3;
    public static final int START_GAME_REQUEST = 4;

    private static Bundle settings = new Bundle();
    private TextView connectionView;
    private TextView playerNameView;
    private TextView ipView;

    private SharedPreferences sharedPref;

    public void intend(Class<? extends Activity> clazz, int id) {
        Intent intent = new Intent(this, clazz);
        intent.putExtras(settings);
        startActivityForResult(intent, id);
    }

    public void onClickJoinGameButton(View button) {
        intend(JoinActivity.class, JOIN_MENU_REQUEST);
    }

    public void onClickHostGameButton(View button) {
        intend(HostActivity.class, HOST_MENU_REQUEST);
    }

    public void onClickSettingsButton(View view) {
        intend(SettingsActivity.class, EDIT_SETTINGS_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            settings.putAll(data.getExtras());
            if (requestCode == HOST_MENU_REQUEST || requestCode == JOIN_MENU_REQUEST)
                intend(GameActivity.class, START_GAME_REQUEST);
        }
    }

    private ArrayList<String> damageMessages = new ArrayList<>();
    private ArrayList<String> endMessages = new ArrayList<>();
    private ArrayList<String> joinMessages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RedisMessageListener.setupListener();
        
        new RedisMessageListener("game.damage") {
            @Override
            public void onMessage(String message) {
                damageMessages.add(message);
            }
        };
        new RedisMessageListener("game.end") {
            @Override
            public void onMessage(String message) {
                endMessages.add(message);
            }
        };
        new RedisMessageListener("game.join") {
            @Override
            public void onMessage(String message) {
                joinMessages.add(message);
            }
        };
        new RedisMessageListener("game.spawn") {
            @Override
            public void onMessage(String message) {
                NetworkEntity.onSpawn(message);
            }
        };
        new RedisMessageListener("game.move"){
            @Override
            public void onMessage(String message) {
                NetworkEntity.onMove(message);
            }
        };

        new Thread() {
            @Override
            public void run() {
                while (true) {
                    int y = 0;
                    while (y < damageMessages.size()) {
                        if (Player.player != null)
                            Player.player.damage(damageMessages.remove(y));
                        else
                            y++;
                    }
                    int z = 0;
                    while (z < endMessages.size()) {
                        if (Player.player != null)
                            Player.player.end(endMessages.remove(z));
                        else
                            z++;
                    }
                    int a = 0;
                    while (a < joinMessages.size()) {
                        if (HostActivity.activity != null)
                            HostActivity.activity.join(joinMessages.remove(a));
                        else
                            a++;
                    }
                }
            }
        }.start();

        sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        connectionView = getByID(R.id.text_main_connection_default, TextView.class);
        playerNameView = getByID(R.id.text_main_player_default, TextView.class);
        ipView = getByID(R.id.text_main_ip_default, TextView.class);

        settings.putAll(SettingsActivity.getDefaultBundle(this));

        settings.putString(SettingsActivity.ARGS_HOST, sharedPref.getString(SettingsActivity.ARGS_HOST, settings.getString(SettingsActivity.ARGS_HOST)));
        settings.putInt(SettingsActivity.ARGS_PORT, sharedPref.getInt(SettingsActivity.ARGS_PORT, settings.getInt(SettingsActivity.ARGS_PORT)));
        settings.putString(SettingsActivity.ARGS_PLAYER_NAME, sharedPref.getString(SettingsActivity.ARGS_PLAYER_NAME, settings.getString(SettingsActivity.ARGS_PLAYER_NAME)));

        new Thread() {
            @Override
            public void run() {
                while (true) {
                    Jedis jedis = Redis.get(settings);
                    final String text = jedis.isConnected() ? "Online" : "Offline";

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!connectionView.getText().toString().equals(text))
                                connectionView.setText(text);

                            if (!ipView.getText().toString().equals(settings.getString(SettingsActivity.ARGS_HOST)))
                                ipView.setText(settings.getString(SettingsActivity.ARGS_HOST));

                            if (!playerNameView.getText().toString().equals(settings.getString(SettingsActivity.ARGS_PLAYER_NAME)))
                                playerNameView.setText(settings.getString(SettingsActivity.ARGS_PLAYER_NAME));
                        }
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

    }

    public static Bundle getSettings() {
        return settings;
    }

    public void onClickExitButton(View button) {
        finish();
    }
}
