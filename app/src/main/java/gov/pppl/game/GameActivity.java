package gov.pppl.game;

import android.app.Activity;
import android.os.Bundle;


public class GameActivity extends Activity {
    Bundle settings = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings.putAll(getIntent().getExtras());
        setContentView(R.layout.activity_game);
        //setContentView(new GameView(getApplicationContext(), getIntent().getExtras()));
    }
}