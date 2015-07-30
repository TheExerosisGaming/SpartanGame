package me.exerosis.spartangame.game.entity;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by student on 7/30/2015.
 */
public class HealthBar {

    Rect bar = new Rect();
    Paint paint = new Paint(0);
    Point point = new Point(50, 50);
    int width;

    public HealthBar(int health){
        width = health * 25;
        bar.set(point.x, point.y, point.x + width, 250); //left, top, right, bottom
        paint.setColor(0x00FF00);
    }

    public void setSize(int health){
        width = health * 25;
        bar.set(point.x, point.y, point.x + width, 250); //left, top, right, bottom
    }

    public void draw(Canvas canvas){
        canvas.drawRect(bar, paint);
    }
}
