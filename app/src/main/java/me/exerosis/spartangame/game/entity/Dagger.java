package me.exerosis.spartangame.game.entity;

import android.graphics.Bitmap;

import me.exerosis.spartangame.game.player.Player;

/**
 * Created by student on 7/29/2015.
 */
public class Dagger extends Entity {

    public Dagger(Bitmap texture, int x, int y, Player player) {
        super(texture, x, y);
        setDirection(player);
    }

    public boolean isOffScreen(){
        if(getX() > 1900 || getX() < 0){
            return true;
        } else {
            return false;
        }
    }

    public void setDirection(Player player){
        if(player.getDirection() == 1){ //right
            setXVelocity(90);
        } else {
            setXVelocity(-90);
        }
    }

}
