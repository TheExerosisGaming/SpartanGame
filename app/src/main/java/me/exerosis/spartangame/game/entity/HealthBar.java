package me.exerosis.spartangame.game.entity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import me.exerosis.spartangame.game.GameView;

/**
 * Created by student on 7/30/2015.
 */
public class HealthBar {

    Rect bar = new Rect();
    Paint paint = new Paint(0);
    Point point = new Point(GameView.getScreenWidth() - 625, 25);
    int width;

    public HealthBar(int health){
        width = health * 25;
        bar.set(point.x, point.y, point.x + width, 100); //left, top, right, bottom
        paint.setColor(Color.GREEN);
    }

    public void setSize(int health){
        if(health > 0) {
            width = health * 30;
        } else {
            width = 0;
        }
        bar.set(point.x, point.y, point.x + width, 250); //left, top, right, bottom
    }

    public void draw(Canvas canvas){
        canvas.drawRect(bar, paint);
    }
}
