package com.example.aplikacja;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@SuppressLint("ViewConstructor")
class GameView extends View {

    private int mXPos = 0, mYPos = 0;
    protected int cCordX = 720, cCordY = 1400, cSize = 50, cDefSize = 50;
    protected int cCenterX = 700, cCenterY = 2500;
    private final int ocDiameter, icDiameter;
    Point target;
    private final Handler handler = new Handler();
    private final long delayMillis = 16; // Około 60 klatek na sekundę

    @SuppressLint("ClickableViewAccessibility")
    public GameView(Context context, int ocDiameter, int icDiameter){
        super(context);

        this.ocDiameter = ocDiameter;
        this.icDiameter = icDiameter;

        target = new Point(new Random().nextInt(1340), new Random().nextInt(2768));

        setOnTouchListener((v, event) -> {
            mXPos = (int) event.getX();
            mYPos = (int) event.getY();
            return true;
        });

        Runnable drawRunnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
                handler.postDelayed(this, delayMillis);
            }
        };
        handler.postDelayed(drawRunnable, delayMillis);
    }

    @SuppressLint("DrawAllocation")
    protected void onDraw(Canvas canvas) {

        @SuppressLint("DrawAllocation")
        Paint paint = new Paint();

        paint.setColor(Color.BLUE);
        canvas.drawCircle(cCenterX,cCenterY,ocDiameter,paint);

        int jXPos = mXPos - cCenterX;
        int jYPos = mYPos - cCenterY;

        double dToMouse = Math.sqrt(Math.pow(cCenterX - mXPos, 2) + Math.pow(cCenterY - mYPos, 2));
        double ratio = ocDiameter / dToMouse;

        if (dToMouse > ocDiameter) {
            jXPos = (int) ((mXPos - cCenterX) * ratio);
            jYPos = (int) ((mYPos - cCenterY) * ratio);
        }

        if ( (cCordX + (jXPos / 10) ) > 0 && (cCordX + (jXPos / 10) ) < getWidth())
            cCordX += jXPos / 10;

        if ( (cCordY + (jYPos / 10) ) > 0 && (cCordY + (jYPos / 10) ) < getHeight())
            cCordY += jYPos / 10;

        @SuppressLint("DrawAllocation")
        Rect x = new Rect(cCordX - (cSize / 2), cCordY - (cSize / 2), cCordX + cSize,cCordY + cSize);
        Rect y = new Rect(target.x - (cDefSize / 2), target.y - (cDefSize / 2), target.x + cDefSize, target.y + cDefSize);

        canvas.drawRect(y,paint);
        canvas.drawCircle(cCordX, cCordY, cSize, paint);

        if (x.intersect(y)) {
            target = new Point(new Random().nextInt(1440), new Random().nextInt(2868));
            cSize+=5;
        }
        
        paint.setColor(Color.MAGENTA);

        if (dToMouse > ocDiameter)
            canvas.drawCircle((int) (cCenterX + (mXPos - cCenterX) * ratio), (int) (cCenterY + (mYPos - cCenterY) * ratio), icDiameter, paint);

        else
            canvas.drawCircle(mXPos, mYPos, icDiameter,paint);


        paint.setColor(Color.BLACK);
        paint.setTextSize(120);
        canvas.drawText("Punkty: " + (cSize - 50), 0, 100, paint);


    }
}
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GameView(this,200,100));
    }
}