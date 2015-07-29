package gov.pppl.blah;

import android.app.Activity;
import android.os.Bundle;


public class GameActivity extends Activity {
    Bundle settings = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings.putAll(getIntent().getExtras());
        setContentView(new GameView(getApplicationContext(), getIntent().getExtras()));
    }
}