package me.exerosis.spartangame.game;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import gov.pppl.androidmessaginglibrary.event.CustomEventManager;
import gov.pppl.androidmessaginglibrary.event.MessageReceivedEvent;
import gov.pppl.blah.R;
import me.exerosis.spartangame.game.entity.Dagger;
import me.exerosis.spartangame.game.entity.Entity;
import me.exerosis.spartangame.game.entity.HealthBar;
import me.exerosis.spartangame.game.entity.PicButton;
import me.exerosis.spartangame.game.player.Player;
import me.exerosis.spartangame.game.player.RemotePlayer;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private final GameThread gameThread; // Threads
    private Thread animationThread;

    private final Player localPlayer; // Players
    private final Entity otherPlayer;

    private final Entity[] dpad = new Entity[4]; // Icons
    private final Entity[] abilities = new Entity[4];
    private final Entity gameEnd;
    private final HealthBar healthBar;

    private Dagger dagger; // Weapons

    private static int screenWidth; // Screen dimensions
    private static int screenHeight;

    private Bundle settings = new Bundle();

    public GameView(Context context, Bundle bundle) {
        super(context);

        settings.putAll(bundle);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        //String playerName = bundle.getString(SettingsActivity.ARGS_PLAYER_NAME);//get Player Name
        //String otherPlayerName = bundle.getString(GameActivity.ARGS_OTHER_PLAYER); //Get Other Player Name

        String playerName = "asdasd";
        String otherPlayerName = "asdas";

        localPlayer = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.bluerightidle), screenWidth / 2, screenHeight / 2, playerName);
        otherPlayer = new RemotePlayer(BitmapFactory.decodeResource(getResources(), R.drawable.redleftidle), otherPlayerName);

        gameEnd = new PicButton(BitmapFactory.decodeResource(getResources(), R.drawable.victory), screenWidth / 2 - 350, screenHeight / 2 - 300, this);

        healthBar = new HealthBar(localPlayer.getHealth());

        initIcons(); //create all icons
        initThreads(); //create threads
        gameThread = new GameThread(getHolder(), this);

        getHolder().addCallback(this);
        setFocusable(true);
    }

    @CustomEventManager.CustomEventHandler
    public void onMessage(MessageReceivedEvent event) {
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        gameThread.setRunning(true);
        gameThread.start();
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

            canvas.drawColor(Color.BLACK);

            canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.background), 0, 0, null); //draw background

            localPlayer.draw(canvas); //draw players
            otherPlayer.draw(canvas);
            if (dagger != null) {
                Log.v("onDraw", "tracking a dagger");
                dagger.draw(canvas);
                if (dagger.isOffScreen()) {
                    dagger = null;
                    Log.v("onDraw", "dagger deleted");
                }
            }


            //canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.wall), 0, 500, null); //draw wall

            for (int i = 0; i < 4; i++) {
                dpad[i].draw(canvas); //draw dpad
            }
            for (int i = 0; i < 4; i++) {
                abilities[i].draw(canvas); //draw dpad
            }

            healthBar.setSize(localPlayer.getHealth());
            healthBar.draw(canvas);

            if (localPlayer.getHealth() == 0) {
                gameEnd.draw(canvas);
            }

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP) {
            localPlayer.setXVelocity(0);
            localPlayer.setYVelocity(0);
        }

        return true;
    }

    public void initIcons() {

        dpad[0] = new PicButton(BitmapFactory.decodeResource(getResources(), R.drawable.up), screenWidth - 355, screenHeight / 2 + 10, this) {
            @Override
            public void touched() {
                localPlayer.setYVelocity(-screenHeight/ 75);
            }
        };
        dpad[1] = new PicButton(BitmapFactory.decodeResource(getResources(), R.drawable.right), screenWidth - 250, screenHeight / 2 + 150, this) {
            @Override
            public void touched() {
                localPlayer.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bluerightidle));
                localPlayer.setDirection(1);
                localPlayer.setXVelocity(screenWidth / 75);
            }
        };
        dpad[2] = new PicButton(BitmapFactory.decodeResource(getResources(), R.drawable.down), screenWidth - 355, screenHeight / 2 + 250, this) {
            @Override
            public void touched() {
                localPlayer.setYVelocity(screenHeight / 75);
            }
        };
        dpad[3] = new PicButton(BitmapFactory.decodeResource(getResources(), R.drawable.left), screenWidth - 500, screenHeight / 2 + 150, this) {
            @Override
            public void touched() {
                localPlayer.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.blueleftidle));
                localPlayer.setDirection(0);
                localPlayer.setXVelocity(-screenWidth / 75);
            }
        };

        if (localPlayer.getTeam() == 0) { // blue
            abilities[0] = new PicButton(BitmapFactory.decodeResource(getResources(), R.drawable.blueattack), 25, 50, this) {
                @Override
                public void touched() {
                    //attack
                    if (localPlayer.getDirection() == 0) {
                        localPlayer.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.blueleftlunge));
                    } else {
                        localPlayer.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bluerightlunge));
                    }
                }
            };
            abilities[1] = new PicButton(BitmapFactory.decodeResource(getResources(), R.drawable.blueblock), 25, screenHeight / 2 - 250, this) {
                @Override
                public void touched() {
                    //block
                    if (localPlayer.getDirection() == 0) {
                        localPlayer.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.blueleftshield));
                    } else {
                        localPlayer.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bluerightshield));
                    }
                }
            };
            abilities[2] = new PicButton(BitmapFactory.decodeResource(getResources(), R.drawable.blueknife), 25, screenHeight / 2, this) {
                @Override
                public void touched() {
                    //throw dagger
                    if (dagger == null) {
                        if (localPlayer.getDirection() == 1) {
                            dagger = new Dagger(BitmapFactory.decodeResource(getResources(), R.drawable.bluedaggerright), localPlayer.getX() + 50, localPlayer.getY() + 50, localPlayer);
                        } else {
                            dagger = new Dagger(BitmapFactory.decodeResource(getResources(), R.drawable.bluedaggerleft), localPlayer.getX() + 50, localPlayer.getY() + 50, localPlayer);
                        }
                    }
                }
            };
        }
        abilities[3] = new PicButton(BitmapFactory.decodeResource(getResources(), R.drawable.poison), 25, screenHeight - 300, this) {
            @Override
            public void touched() {
                //unholy strike
                localPlayer.damage();
            }
        };
    }

    public void initThreads(){
        animationThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(250);
                        if (localPlayer.getXVelocity() != 0) { //moving
                            if (localPlayer.getDirection() == 1) {
                                localPlayer.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bluerightidle));
                                Thread.sleep(250);
                                localPlayer.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bluerightidle2));
                            } else {
                                localPlayer.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.blueleftidle));
                                Thread.sleep(250);
                                localPlayer.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.blueleftidle2));
                            }
                        } else { //not moving
                            if (localPlayer.getDirection() == 1) {
                                localPlayer.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bluerightidle));
                            } else {
                                localPlayer.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.blueleftidle));
                            }
                        }
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        });
        animationThread.start();
    }

    public static int getScreenWidth(){
        return screenWidth;
    }

    public static int getScreenHeight(){
        return screenHeight;
    }
}
