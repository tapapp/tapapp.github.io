package com.rran.tapapp;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.PictureDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class TouchView extends View {

    float x;
    float y;
    boolean checkDraw;

    Paint paint;

    public TouchView(Context context) {
        super(context);

        x = 0;
        y = 0;

        checkDraw = false;


        paint = new Paint();
        paint.setColor(Color.rgb(2,102,201));

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        x = event.getX();
        y = event.getY();

        if(event.getAction() == KeyEvent.ACTION_UP)
            checkDraw = false;
        else if (event.getAction() == KeyEvent.ACTION_DOWN)
            checkDraw = true;

        return true;

    }



    @Override
    public void onDraw(Canvas canvas) {

        if(checkDraw) {
            canvas.drawCircle(x, y, 50, paint);
        }

        /*Bitmap cursor = BitmapFactory.decodeResource(getResources(),R.drawable.pointer);

        if(checkDraw) {
            canvas.drawColor(Color.BLACK);
            canvas.drawBitmap(cursor, x, y, null);
        }*/

        invalidate();


    }

}
