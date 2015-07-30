/*
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
import me.exerosis.spartangame.game.entity.PicButton;
import me.exerosis.spartangame.game.player.Player;
import me.exerosis.spartangame.game.player.RemotePlayer;
import me.exerosis.spartangame.menu.SettingsActivity;

public class GameViewTEst extends SurfaceView implements SurfaceHolder.Callback {

    private final GameThread gameThread; // Threads

    private Bundle settings = new Bundle();

    public GameViewTEst(Context context, Bundle bundle) {
        super(context);

        settings.putAll(bundle);
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
        if (canvas == null)
            return;






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

        healthBar = new PicButton(BitmapFactory.decodeResource(getResources(), R.drawable.twenty), 50, 50, this);

        dpad[0] = new PicButton(BitmapFactory.decodeResource(getResources(), R.drawable.up), screenWidth - 355, screenHeight / 2 + 10, this) {
            @Override
            public void touched() {
                localPlayer.setYVelocity(-20);
            }
        };
        dpad[1] = new PicButton(BitmapFactory.decodeResource(getResources(), R.drawable.right), screenWidth - 250, screenHeight / 2 + 150, this) {
            @Override
            public void touched() {
                localPlayer.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bluerightidle));
                localPlayer.setDirection(1);
                localPlayer.setXVelocity(20);
            }
        };
        dpad[2] = new PicButton(BitmapFactory.decodeResource(getResources(), R.drawable.down), screenWidth - 355, screenHeight / 2 + 250, this) {
            @Override
            public void touched() {
                localPlayer.setYVelocity(20);
            }
        };
        dpad[3] = new PicButton(BitmapFactory.decodeResource(getResources(), R.drawable.left), screenWidth - 500, screenHeight / 2 + 150, this) {
            @Override
            public void touched() {
                localPlayer.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.blueleftidle));
                localPlayer.setDirection(0);
                localPlayer.setXVelocity(-20);
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
                localPlayer.die();
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
*/
