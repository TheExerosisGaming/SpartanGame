package gov.pppl.blah;

import android.app.Activity;
import android.os.Bundle;

import gov.pppl.androidmessaginglibrary.redis.RedisMessager;
import gov.pppl.androidmessaginglibrary.redis.listeners.RedisMessageListener;
import gov.pppl.blah.menu.SettingsActivity;


public class GameActivity extends Activity {
    public static final String ARGS_OTHER_PLAYER = "other_player";
    Bundle settings = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings.putAll(getIntent().getExtras());
        RedisMessager.sendMessage("game.sendname", settings.getString(SettingsActivity.ARGS_PLAYER_NAME));

        new RedisMessageListener("game.sendname"){
            @Override
            public void onMessage(String message) {
                if(!message.equals(settings.getString(SettingsActivity.ARGS_PLAYER_NAME)))
                    settings.putString(ARGS_OTHER_PLAYER, message);
            }
        };
        setContentView(new GameView(getApplicationContext(), getIntent().getExtras()));
    }
}