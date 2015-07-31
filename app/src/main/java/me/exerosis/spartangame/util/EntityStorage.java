package me.exerosis.spartangame.util;

import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;

import gov.pppl.blah.R;
import me.exerosis.spartangame.game.GameView;
import me.exerosis.spartangame.game.entity.PicButton;
import me.exerosis.spartangame.game.entity.Player;
import me.exerosis.spartangame.menu.MainActivity;
import me.exerosis.spartangame.util.redis.RedisMessager;

/**
 * Created by Exerosis on 7/30/2015.
 */
public class EntityStorage {

    public static boolean touchingIcon = false;

    public static void initIcons(final Player player, final int screenHeight, final int screenWidth, View view) {
        new PicButton(BitmapFactory.decodeResource(GameView.getGameResources(), R.drawable.up), screenWidth - 355, screenHeight / 2 + 10, view, 5) {
            @Override
            public void touched() {
                player.setYVelocity(-screenHeight / 150);
            }
        };
        new PicButton(BitmapFactory.decodeResource(GameView.getGameResources(), R.drawable.right), screenWidth - 250, screenHeight / 2 + 150, view, 5) {
            @Override
            public void touched() {
                touchingIcon = true;
                player.setBitmap(BitmapFactory.decodeResource(GameView.getGameResources(), R.drawable.bluerightidle));
                player.setDirection(1);
                player.setXVelocity(screenWidth / 150);
            }
        };
        new PicButton(BitmapFactory.decodeResource(GameView.getGameResources(), R.drawable.down), screenWidth - 355, screenHeight / 2 + 250, view, 5) {
            @Override
            public void touched() {
                player.setYVelocity(screenHeight / 150);
                touchingIcon = true;
            }
        };
        new PicButton(BitmapFactory.decodeResource(GameView.getGameResources(), R.drawable.left), screenWidth - 500, screenHeight / 2 + 150, view, 5) {
            @Override
            public void touched() {
                player.setBitmap(BitmapFactory.decodeResource(GameView.getGameResources(), R.drawable.blueleftidle));
                player.setDirection(0);
                player.setXVelocity(-screenWidth / 150);
                touchingIcon = true;
            }
        };

        if (player.getTeam() == 0) { // blue
            new PicButton(BitmapFactory.decodeResource(GameView.getGameResources(), R.drawable.blueattack), 25, screenHeight / 2 - 500, view, 5) {
                @Override
                public void touched() {
                    //attack
                    if (player.getDirection() == 0) {
                        player.setBitmap(BitmapFactory.decodeResource(GameView.getGameResources(), R.drawable.blueleftlunge));
                        if (player.intersects(player.getPairEntity().getRectangle())) {

                            Log.v("Players", "Intersecting");
                            if (player.getDirection() == 0 && player.getPairEntity().getX() <= player.getX()) {
                                Log.v("Players", "Intersecting facing left");
                                RedisMessager.sendMessage("game.damage", player.getPairUUID() + ":" + 2, MainActivity.getSettings());
                            }
                        }
                    } else {
                        player.setBitmap(BitmapFactory.decodeResource(GameView.getGameResources(), R.drawable.bluerightlunge));
                        if (player.getPairEntity().getX() >= player.getX()) {
                            RedisMessager.sendMessage("game.damage", player.getPairUUID() + ":" + 2, MainActivity.getSettings());
                            Log.v("Players", "Intersecting facing right");
                        }
                    }
                    touchingIcon = true;
                }
            };
            new PicButton(BitmapFactory.decodeResource(GameView.getGameResources(), R.drawable.blueblock), 25, screenHeight / 2 - 250, view, 5) {
                @Override
                public void touched() {
                    //block
                    player.setBlocking(true);
                    if (player.getDirection() == 0) {
                        player.setBitmap(BitmapFactory.decodeResource(GameView.getGameResources(), R.drawable.blueleftshield));
                    } else {
                        player.setBitmap(BitmapFactory.decodeResource(GameView.getGameResources(), R.drawable.bluerightshield));
                    }
                    touchingIcon = true;
                }
            };
        }
        new PicButton(BitmapFactory.decodeResource(GameView.getGameResources(), R.drawable.poison), 25, screenHeight / 2 + 250, view, 5) {
            @Override
            public void touched() {
                //unholy strike
                if (player.getHealth() > 0) {
                    player.damage();
                }
                touchingIcon = true;
            }
        };
    }
}
