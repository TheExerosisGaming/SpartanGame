package me.exerosis.spartangame.game;

import android.os.Bundle;

import me.exerosis.spartangame.menu.JoinActivity;

/**
 * Created by The Exerosis on 7/30/2015.
 */
public class PlayerConnection {
    private String otherPlayer;

    public PlayerConnection(Bundle bundle){
        otherPlayer = bundle.getString(GameActivity.ARGS_OTHER_PLAYER);
    }


}
