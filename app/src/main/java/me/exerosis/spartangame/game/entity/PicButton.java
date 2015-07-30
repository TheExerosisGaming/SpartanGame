package me.exerosis.spartangame.game.entity;

/**
 * Created by student on 7/28/2015.
 */

import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;

public class PicButton extends Entity {
    private static HashMap<View, View.OnTouchListener> views = new HashMap<View, View.OnTouchListener>();
    private static View.OnTouchListener listener;

    public PicButton(Bitmap texture, int x, int y, View view, int layer) {
        super(texture, x, y, layer);
        setGravity(false);
        if (!views.containsKey(view)) {
            View.OnTouchListener listener = getOnNewTouchListener();
            views.put(view, listener);
            view.setOnTouchListener(listener);
        }
    }

    private View.OnTouchListener getOnNewTouchListener() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                synchronized (getInstances()) {
                    for (Entity entity : getInstances()) {
                        if (!(entity instanceof PicButton))
                            continue;
                        PicButton button = (PicButton) entity;

                        if (button.contains((int) event.getX(), (int) event.getY()))
                            button.touched();
                    }
                    return false;
                }
            }
        };
    }

    public void touched() {
    }
}