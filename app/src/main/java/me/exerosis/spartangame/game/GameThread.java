package me.exerosis.spartangame.game;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by The Exerosis on 7/26/2015.
 */
public class GameThread extends Thread {
    private SurfaceHolder surfaceHolder;
    private GameView gameView;

    private boolean running;

    public void setRunning(boolean running) {
        this.running = running;
    }

    public GameThread(SurfaceHolder surfaceHolder, GameView gameView) {
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
    }


    @Override
    @SuppressLint("WrongCall")
    public void run() {
        Canvas canvas;
        while (running) {
            canvas = null;
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.gameView.onDraw(canvas);
                }
            }
            finally {
               if(canvas != null)
                   surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

}