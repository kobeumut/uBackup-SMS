package com.grisoft.umut.uBackup.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Umut on 2.04.2016.
 */
public class OvalTextView  extends TextView {
    private static final String YOUR_TEXT = "something cool";
    private Path _arc;

    private Paint _paintText;

    public OvalTextView(Context context) {
        super(context);

        _arc = new Path();
        RectF oval = new RectF(50,100,200,250);;
        _arc.addArc(oval, -180, 200);
        _paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        _paintText.setStyle(Paint.Style.FILL_AND_STROKE);
        _paintText.setColor(Color.WHITE);
        _paintText.setTextSize(20f);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawTextOnPath(YOUR_TEXT, _arc, 0, 20, _paintText);
        invalidate();
    }
}