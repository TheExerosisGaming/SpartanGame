package me.exerosis.spartangame.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import gov.pppl.androidmessaginglibrary.AndroidMessagingAPI;
import gov.pppl.blah.R;
import me.exerosis.spartangame.game.GameActivity;
import me.exerosis.spartangame.util.ExActivity;
import me.exerosis.spartangame.util.Redis;
import me.exerosis.spartangame.util.redis.RedisMessageListener;
import redis.clients.jedis.Jedis;

public class HostActivity extends ExActivity {
    public static final String ARGS_REDIS_SERVER_LIST = "gamesList";
    private EditText serverNameField;
    public static HostActivity activity;
    private Bundle settings = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        activity = this;
        serverNameField = getByID(R.id.field_server_name, EditText.class);

        settings.putAll(getIntent().getExtras());
        settings.putString(SettingsActivity.ARGS_SERVER_NAME, settings.getString(SettingsActivity.ARGS_PLAYER_NAME));
        String name = settings.getString(SettingsActivity.ARGS_SERVER_NAME);
        serverNameField.setText(name);

        addToDB(name);
    }

    private void addToDB(final String newName) {
        new Thread() {
            @Override
            public void run() {
                Jedis jedis = Redis.get(settings);
                try {
                  //  jedis.sadd(ARGS_REDIS_SERVER_LIST, newName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void replaceInDB(final String oldName, final String newName) {
        settings.putString(SettingsActivity.ARGS_SERVER_NAME, newName);
        removeFromDB(oldName);
        addToDB(newName);
    }


    private void removeFromDB(final String oldName) {
        new Thread() {
            @Override
            public void run() {
                Jedis jedis = Redis.get(settings);
                try {
//                    jedis.srem(ARGS_REDIS_SERVER_LIST, oldName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void join(String message) {
        if (!message.equals(settings.getString(SettingsActivity.ARGS_SERVER_NAME)))
            return;

        removeFromDB(settings.getString(SettingsActivity.ARGS_SERVER_NAME));
        intend(RESULT_OK);
    }

    @Override
    protected void onDestroy() {
        removeFromDB(settings.getString(SettingsActivity.ARGS_SERVER_NAME));
        super.onDestroy();
    }

    public void onClickSaveButton(View button) {
        replaceInDB(settings.getString(SettingsActivity.ARGS_SERVER_NAME), serverNameField.getText().toString());
    }

    public void onClickCancelButton(View button) {
        removeFromDB(settings.getString(SettingsActivity.ARGS_SERVER_NAME));
        intend(RESULT_CANCELED);
    }

    private void intend(int id) {
        Intent intent = new Intent();
        intent.putExtras(settings);
        setResult(id, intent);
        finish();
    }
}