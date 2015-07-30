package me.exerosis.spartangame.util;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.view.View;

import gov.pppl.blah.R;
import me.exerosis.spartangame.game.entity.Dagger;
import me.exerosis.spartangame.game.entity.EntityTest;
import me.exerosis.spartangame.game.entity.PicButton;
import me.exerosis.spartangame.game.player.Player;

/**
 * Created by Exerosis on 7/30/2015.
 */
public class EntityStorage {
    public static void initIcons(final Player player, final int screenHeight, final int screenWidth, View view) {
        new PicButton(BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.up), screenWidth - 355, screenHeight / 2 + 10, view) {
            @Override
            public void touched() {
                player.setYVelocity(-screenHeight / 75);
            }
        };
       new PicButton(BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.right), screenWidth - 250, screenHeight / 2 + 150, view) {
            @Override
            public void touched() {
                player.setBitmap(BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.bluerightidle));
                player.setDirection(1);
                player.setXVelocity(screenWidth / 75);
            }
        };
        new PicButton(BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.down), screenWidth - 355, screenHeight / 2 + 250, view) {
            @Override
            public void touched() {
                player.setYVelocity(screenHeight / 75);
            }
        };
        new PicButton(BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.left), screenWidth - 500, screenHeight / 2 + 150, view) {
            @Override
            public void touched() {
                player.setBitmap(BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.blueleftidle));
                player.setDirection(0);
                player.setXVelocity(-screenWidth / 75);
            }
        };

        if (player.getTeam() == 0) { // blue
            new PicButton(BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.blueattack), 25, 50, view) {
                @Override
                public void touched() {
                    //attack
                    if (player.getDirection() == 0) {
                        player.setBitmap(BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.blueleftlunge));
                    } else {
                        player.setBitmap(BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.bluerightlunge));
                    }
                }
            };
            new PicButton(BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.blueblock), 25, screenHeight / 2 - 250, view) {
                @Override
                public void touched() {
                    //block
                    if (player.getDirection() == 0) {
                        player.setBitmap(BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.blueleftshield));
                    } else {
                        player.setBitmap(BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.bluerightshield));
                    }
                }
            };
        }
       new PicButton(BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.poison), 25, screenHeight - 300, view) {
            @Override
            public void touched() {
                //unholy strike
                player.damage();
            }
        };
    }
}
