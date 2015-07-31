package me.exerosis.spartangame.game.entity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

import java.util.UUID;

import gov.pppl.blah.R;
import me.exerosis.spartangame.game.GameActivity;
import me.exerosis.spartangame.game.GameView;
import me.exerosis.spartangame.game.entity.NetworkEntity;
import me.exerosis.spartangame.menu.MainActivity;
import me.exerosis.spartangame.util.redis.RedisMessageListener;
import me.exerosis.spartangame.util.redis.RedisMessager;

/**
 * Created by Exerosis on 7/26/2015.
 */
public class Player {
    private NetworkEntity entity;
    private int health = 20;
    private int team = 0;
    private int direction = 1;
    private boolean blocking = false;


    public Player(int texture, int x, int y, int layer) {
        entity = NetworkEntity.newInstance(texture, x, y, layer);
        entity.setGravity(false);

        new RedisMessageListener("game.damage") {
            @Override
            public void onMessage(String message) {
                String[] parts = message.split(":");
                if (UUID.fromString(parts[0]).equals(entity.getUUID().toString()))
                    if(blocking == false) {
                        setHealth(getHealth() - Integer.valueOf(parts[1]));
                    } else {
                        Log.v("Player", "Attack blocked");
                    }
            }
        };
        new RedisMessageListener("game.end") {
            @Override
            public void onMessage(String message) {
                if (!UUID.fromString(message).equals(entity.getUUID()))
                    return;

                showEndGameDrawable(R.drawable.victory);
            }
        };
    }

    public void setGravity(boolean activate) {
      entity.setGravity(activate);
    }

    public Rect getRectangle() {
        return entity.getRectangle();
    }

    public void setRectangle(int x, int y, int height, int width) {
        entity.setRectangle(x, y, height, width);
    }

    public boolean contains(int x, int y) {
        return entity.contains(x, y);
    }

    public int getX() {
        return entity.getX();
    }

    public void setX(int x) {
        entity.setX(x);
    }

    public void setY(int y) {
        entity.setY(y);
    }

    public int getY() {
        return entity.getY();
    }

    public int getLayer() {
        return entity.getLayer();
    }

    public int getXVelocity() {
        return entity.getXVelocity();
    }

    public int getYVelocity() {
        return entity.getYVelocity();
    }

    public void setXVelocity(int xVel) {
        entity.setXVelocity(xVel);
    }

    public void setYVelocity(int yVel) {
        entity.setYVelocity(yVel);
    }

    public Bitmap getBitmap() {
        return entity.getBitmap();
    }

    public UUID getUUID() {
        return entity.getUUID();
    }

    public UUID getPairUUID() {
        return entity.getPairUUID();
    }

    public void setBitmap(Bitmap bitmap) {
        entity.setBitmap(bitmap);
    }

    public int getHealth() {
        return health;
    }

    public NetworkEntity getPairEntity() {
        return entity.getPairEntity();
    }

    public void setHealth(int health) {
        this.health = health;

        if (health < 1) {
            showEndGameDrawable(R.drawable.defeat);
            RedisMessager.sendMessage("game.end", entity.getPairUUID().toString(), MainActivity.getSettings());
        }
    }

    public void damage() {
        damage(1);
    }

    public void damage(int amount) {
        setHealth(getHealth() - amount);
    }

    private void showEndGameDrawable(int id){
        int screenWidth = GameView.getScreenHeight();
        int screenHeight = GameView.getScreenHeight();
        PicButton gameEnd = new PicButton(BitmapFactory.decodeResource(GameView.getGameResources(), id), screenWidth / 2 - 350, screenHeight / 2 - 300, GameView.getView(), 5);
        gameEnd.setX(screenHeight / 2 - gameEnd.getBitmap().getWidth() / 2);
        gameEnd.setY(screenWidth / 2 - gameEnd.getBitmap().getHeight() / 2);
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                GameActivity.getActivity().intend();
            }
        }.start();
    }

    public int getTeam() {
        return team;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setBlocking(boolean blocking){
        this.blocking = blocking;
    }
}