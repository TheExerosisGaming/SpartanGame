package gov.pppl.blah.GUI;

/**
 * Created by student on 7/28/2015.
 */

import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PicButton extends Entity {

    private static HashMap<View, View.OnTouchListener> views = new HashMap<View, View.OnTouchListener>();
    private static List<PicButton> instances = new ArrayList<>();
    private static View.OnTouchListener listener;

    public PicButton(Bitmap texture, int x, int y, View view) {
        super(texture, x, y);
        if(!views.containsKey(view)){
            View.OnTouchListener listener = getOnNewTouchListener();
            views.put(view, listener);
            view.setOnTouchListener(listener);
        }

        instances.add(this);
    }

    private View.OnTouchListener getOnNewTouchListener(){
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                for(PicButton button : instances){
                    if(button.contains((int)event.getX(), (int)event.getY())){
                        button.touched();
                    }
                }
                return false;
            }
        };
    }

    public void touched(){}

}
