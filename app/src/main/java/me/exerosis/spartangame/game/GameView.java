package me.exerosis.spartangame.game;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import java.util.Collections;

import gov.pppl.blah.R;
import me.exerosis.spartangame.game.entity.Dagger;
import me.exerosis.spartangame.game.entity.Entity;
import me.exerosis.spartangame.game.entity.HealthBar;
import me.exerosis.spartangame.game.entity.PicButton;
import me.exerosis.spartangame.game.entity.Player;
import me.exerosis.spartangame.menu.MainActivity;
import me.exerosis.spartangame.util.EntityStorage;
import me.exerosis.spartangame.util.redis.RedisMessager;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private final GameThread gameThread; // Threads
    private Thread animationThread;

    private final Player player; // Players

    private final HealthBar healthBar;

    private Dagger dagger; // Weapons

    private static GameView view; // Weapons

    private static int screenWidth; // Screen dimensions
    private static int screenHeight;

    private Bundle settings = new Bundle();

    private static Resources resources;

    public GameView(Context context, Bundle bundle) {
        super(context);
        view = this;
        resources = getResources();

        settings.putAll(bundle);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        player = new Player(R.drawable.bluerightidle, screenWidth / 2, screenHeight / 2, 4);

        healthBar = new HealthBar(player.getHealth());

        EntityStorage.initIcons(player, screenHeight, screenWidth, this); //create all icons

        //TODO move this to the EntityStorage
        new PicButton(BitmapFactory.decodeResource(getResources(), R.drawable.blueknife), 25, screenHeight / 2, this, 5) {
            @Override
            public void touched() {
                //throw dagger
                if (dagger == null) {
                    if (player.getDirection() == 1) {
                        dagger = new Dagger(BitmapFactory.decodeResource(getResources(), R.drawable.bluedaggerright), player.getX() + 50, player.getY() + 50, player);
                    } else {
                        dagger = new Dagger(BitmapFactory.decodeResource(getResources(), R.drawable.bluedaggerleft), player.getX() + 50, player.getY() + 50, player);
                    }
                }
            }
        };
        initThreads(); //create threads
        gameThread = new GameThread(getHolder(), this);

        getHolder().addCallback(this);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        gameThread.setRunning(true);
        gameThread.start();
        RedisMessager.sendMessage("game.damage", player.getPairUUID() + ":" + 2, MainActivity.getSettings());
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                gameThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (canvas != null) {
            canvas.drawColor(Color.CYAN);

            for (Entity entity : Entity.getSyncInstances())
                if (entity != null && entity.isVisible())
                    entity.draw(canvas);

            if (dagger != null)
                if (dagger.isOffScreen())
                    dagger = null;

            healthBar.setSize(player.getHealth());
            healthBar.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP) {
            player.setXVelocity(0);
            player.setYVelocity(0);
            EntityStorage.touchingIcon = false;
        }

        return true;
    }

    public static Resources getGameResources() {
        return resources;
    }

    public void initThreads() {
        animationThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (!EntityStorage.touchingIcon) {
                        try {
                            Thread.sleep(250);
                            if (player.getXVelocity() != 0) { //moving
                                if (player.getDirection() == 1) {
                                    player.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bluerightidle));
                                    Thread.sleep(250);
                                    player.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bluerightidle2));
                                } else {
                                    player.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.blueleftidle));
                                    Thread.sleep(250);
                                    player.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.blueleftidle2));
                                }
                            } else { //not moving
                                if (player.getDirection() == 1) {
                                    player.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bluerightidle));
                                } else {
                                    player.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.blueleftidle));
                                }
                            }
                        } catch (InterruptedException ignored) {
                        }
                    }
                }
            }
        });
        animationThread.start();
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    public static View getView() {
        return view;
    }
}
