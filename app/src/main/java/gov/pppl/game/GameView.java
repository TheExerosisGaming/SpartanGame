package gov.pppl.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import gov.pppl.androidmessaginglibrary.event.CustomEventManager;
import gov.pppl.androidmessaginglibrary.event.MessageReceivedEvent;
import gov.pppl.game.menu.SettingsActivity;
import gov.pppl.game.player.Entity;
import gov.pppl.game.player.Player;
import gov.pppl.game.player.RemotePlayer;

/**
 * Created by The Exerosis on 7/26/2015.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private final GameThread thread;
    private final Entity player;
    private final Entity otherPlayer;

    public GameView(Context context, Bundle bundle) {
        super(context);

        String playerName = bundle.getString(SettingsActivity.ARGS_PLAYER_NAME);
        String otherPlayerName = bundle.getString(SettingsActivity.ARGS_SERVER_NAME);

        player = new Player(null, 1, 0, playerName);
        otherPlayer = new RemotePlayer(null, otherPlayerName);

        getHolder().addCallback(this);

        thread = new GameThread(getHolder(), this);
        setFocusable(true);
    }
    @CustomEventManager.CustomEventHandler
    public void onMessage(MessageReceivedEvent event) {
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        player.draw(canvas);
        otherPlayer.draw(canvas);
    }
}
