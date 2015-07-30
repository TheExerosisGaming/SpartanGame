package gov.pppl.blah.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import gov.pppl.androidmessaginglibrary.AndroidMessagingAPI;
import gov.pppl.androidmessaginglibrary.redis.listeners.RedisMessageListener;
import gov.pppl.blah.R;
import gov.pppl.blah.util.ExActivity;
import gov.pppl.blah.util.Redis;
import redis.clients.jedis.Jedis;

public class HostActivity extends ExActivity {
    public static final String ARGS_OTHER_PLAYER = "other_player";
    public static final String ARGS_REDIS_SERVER_LIST = "gamesList";
    private EditText serverNameField;
    private Bundle settings = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        serverNameField = getByID(R.id.field_server_name, EditText.class);

        settings.putAll(getIntent().getExtras());
        String name = settings.getString(SettingsActivity.ARGS_SERVER_NAME);
        serverNameField.setText(name);

        addToDB(name);
        AndroidMessagingAPI.getEventManager().registerListener(this);


        new RedisMessageListener("game.join") {
            @Override
            public void onMessage(String message) {
                String[] components = message.split(":");

                if (!components[0].equals(settings.getString(SettingsActivity.ARGS_SERVER_NAME)))
                    return;

                removeFromDB(settings.getString(SettingsActivity.ARGS_SERVER_NAME));
                settings.putString(ARGS_OTHER_PLAYER, components[1]);
                intend(RESULT_OK);
            }
        };
    }

    private void addToDB(final String newName) {
        new Thread() {
            @Override
            public void run() {
                Jedis jedis = Redis.get(settings);
                jedis.sadd(ARGS_REDIS_SERVER_LIST, newName);
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
                if (jedis.sismember(ARGS_REDIS_SERVER_LIST, oldName))
                    jedis.srem(ARGS_REDIS_SERVER_LIST, oldName);
            }
        }.start();
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