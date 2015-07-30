package me.exerosis.spartangame.util;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.view.View;

import gov.pppl.blah.R;
import me.exerosis.spartangame.game.GameView;
import me.exerosis.spartangame.game.entity.PicButton;
import me.exerosis.spartangame.game.player.Player;

/**
 * Created by Exerosis on 7/30/2015.
 */
public class EntityStorage {
    public static void initIcons(final Player player, final int screenHeight, final int screenWidth, View view) {
        new PicButton(BitmapFactory.decodeResource(GameView.getGameResources(), R.drawable.up), screenWidth - 355, screenHeight / 2 + 10, view, 5) {
            @Override
            public void touched() {
                player.setYVelocity(-screenHeight / 75);
            }
        };
        new PicButton(BitmapFactory.decodeResource(GameView.getGameResources(), R.drawable.right), screenWidth - 250, screenHeight / 2 + 150, view, 5) {
            @Override
            public void touched() {
                player.setBitmap(BitmapFactory.decodeResource(GameView.getGameResources(), R.drawable.bluerightidle));
                player.setDirection(1);
                player.setXVelocity(screenWidth / 75);
            }
        };
        new PicButton(BitmapFactory.decodeResource(GameView.getGameResources(), R.drawable.down), screenWidth - 355, screenHeight / 2 + 250, view, 5) {
            @Override
            public void touched() {
                player.setYVelocity(screenHeight / 75);
            }
        };
        new PicButton(BitmapFactory.decodeResource(GameView.getGameResources(), R.drawable.left), screenWidth - 500, screenHeight / 2 + 150, view, 5) {
            @Override
            public void touched() {
                player.setBitmap(BitmapFactory.decodeResource(GameView.getGameResources(), R.drawable.blueleftidle));
                player.setDirection(0);
                player.setXVelocity(-screenWidth / 75);
            }
        };

        if (player.getTeam() == 0) { // blue
            new PicButton(BitmapFactory.decodeResource(GameView.getGameResources(), R.drawable.blueattack), 25, 50, view, 5) {
                @Override
                public void touched() {
                    //attack
                    if (player.getDirection() == 0) {
                        player.setBitmap(BitmapFactory.decodeResource(GameView.getGameResources(), R.drawable.blueleftlunge));
                    } else {
                        player.setBitmap(BitmapFactory.decodeResource(GameView.getGameResources(), R.drawable.bluerightlunge));
                    }
                }
            };
            new PicButton(BitmapFactory.decodeResource(GameView.getGameResources(), R.drawable.blueblock), 25, screenHeight / 2 - 250, view, 5) {
                @Override
                public void touched() {
                    //block
                    if (player.getDirection() == 0) {
                        player.setBitmap(BitmapFactory.decodeResource(GameView.getGameResources(), R.drawable.blueleftshield));
                    } else {
                        player.setBitmap(BitmapFactory.decodeResource(GameView.getGameResources(), R.drawable.bluerightshield));
                    }
                }
            };
        }
        new PicButton(BitmapFactory.decodeResource(GameView.getGameResources(), R.drawable.poison), 25, screenHeight - 300, view, 5) {
            @Override
            public void touched() {
                //unholy strike
                player.damage();
            }
        };
    }
}
