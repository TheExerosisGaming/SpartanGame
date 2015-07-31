package me.exerosis.spartangame.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import gov.pppl.blah.R;
import me.exerosis.spartangame.util.ExActivity;
import me.exerosis.spartangame.util.Redis;
import redis.clients.jedis.Jedis;

public class SettingsActivity extends ExActivity {
    public static final String ARGS_IS_IP = "is_ip";
    public static final String ARGS_HOST = "host";
    public static final String ARGS_PORT = "port";
    public static final String ARGS_PLAYER_NAME = "player_name";
    public static final String ARGS_SERVER_NAME = "server_name";

    public static final String DEFAULT_HOST = "grappl.io";
    public static final int DEFAULT_PORT = 40186;

    private EditText serverIPField;
    private EditText playerNameField;

    private Bundle settings = new Bundle();

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (getIntent().getExtras() != null)
            settings.putAll(getIntent().getExtras());

        serverIPField = getByID(R.id.field_settings_ip, EditText.class);
        playerNameField = getByID(R.id.field_settings_player_name, EditText.class);

        serverIPField.setText(settings.getString(ARGS_HOST) + ":" + settings.getInt(ARGS_PORT));
        playerNameField.setText(settings.getString(ARGS_PLAYER_NAME));

        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }


    public void onClickSaveButton(View button) {

        isValidIP(serverIPField.getText().toString());
    }

    private void respond() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), settings.getString(ARGS_HOST) + ":" + settings.getInt(ARGS_PORT), Toast.LENGTH_LONG).show();

                if (!settings.getBoolean(ARGS_IS_IP)) {
                    serverIPField.setError("Unable to connect to that IP!");
                    return;
                }

                settings.remove(ARGS_IS_IP);
                settings.putString(ARGS_PLAYER_NAME, playerNameField.getText().toString());

                editor.putString(ARGS_HOST, settings.getString(ARGS_HOST));
                editor.putInt(ARGS_PORT, settings.getInt(ARGS_PORT));
                editor.putString(ARGS_SERVER_NAME, settings.getString(ARGS_PLAYER_NAME));
                editor.apply();

                intend(settings, RESULT_OK);
            }
        });
    }


    private void isValidIP(String input) {
        if (input.equals("")) {
            respond();
            return;
        }

        String[] parts = input.split(":");

        if (parts.length > 0)
            settings.putString(ARGS_HOST, parts[0]);
        if (parts.length == 2)
            settings.putInt(ARGS_PORT, Integer.valueOf(parts[1]));

        new Thread() {
            @Override
            public void run() {
                Jedis jedis = Redis.get(settings);
                settings.putBoolean(ARGS_IS_IP, jedis.isConnected());
                respond();
            }
        }.start();
    }

    public static Bundle getDefaultBundle(Activity activity) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ARGS_IS_IP, false);
        bundle.putInt(ARGS_PORT, DEFAULT_PORT);
        bundle.putString(ARGS_HOST, DEFAULT_HOST);
        bundle.putString(ARGS_SERVER_NAME, activity.getString(R.string.player_name_default));
        bundle.putString(ARGS_PLAYER_NAME, activity.getString(R.string.player_name_default));
        return bundle;
    }

    private void intend(Bundle bundle, int id) {
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(id, intent);
        finish();
    }

    public void onClickCancelButton(View button) {
        serverIPField.setText("");
        playerNameField.setText("");
        intend(getDefaultBundle(this), RESULT_CANCELED);
    }
}