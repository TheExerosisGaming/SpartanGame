package me.exerosis.spartangame.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import gov.pppl.blah.R;
import me.exerosis.spartangame.game.GameActivity;
import me.exerosis.spartangame.util.ExActivity;
import me.exerosis.spartangame.util.Redis;
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
    private SharedPreferences.Editor editor;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();

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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
