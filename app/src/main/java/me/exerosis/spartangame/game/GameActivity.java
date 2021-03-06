package me.exerosis.spartangame.game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import gov.pppl.blah.R;
import me.exerosis.spartangame.menu.MainActivity;
import me.exerosis.spartangame.util.redis.RedisMessager;
import me.exerosis.spartangame.util.redis.RedisMessageListener;
import me.exerosis.spartangame.menu.SettingsActivity;


public class GameActivity extends Activity {
    public static final String ARGS_OTHER_PLAYER = "other_player";
    private Bundle settings = new Bundle();
    public static GameActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(new GameView(getApplicationContext(), getIntent().getExtras()));
        settings.putAll(getIntent().getExtras());

    }

    public static GameActivity getActivity() {
        return activity;
    }

    public void intend() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtras(MainActivity.getSettings());
        startActivity(intent);
    }
}