package com.task.colorpickercircle.color_picker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.task.colorpickercircle.R;

public class CenterCircleView extends View {
    public int shadowWidth;
    public boolean isTouchable;
    private Paint paint;

    public CenterCircleView(Context context) {
        this(context, null);
    }

    public CenterCircleView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CenterCircleView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.shadowWidth = 10;
        this.isTouchable = true;
        setView();
    }

    private void setView() {
        setLayerType(1, null);
        this.shadowWidth = (int) getResources().getDimension(R.dimen.no_shadow_width);
        this.paint = new Paint(1);
        this.paint.setColor(-1);
        this.paint.setStyle(Style.FILL);
        this.paint.setShadowLayer((float) this.shadowWidth, 0.0f, 0.0f, Color.parseColor("#40000000"));
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle((float) (getWidth() / 2), (float) (getHeight() / 2), (float) ((Math.min(getWidth(), getHeight()) / 2) - this.shadowWidth), this.paint);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.isTouchable) {
            return false;
        }
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        int a = (int) ColorPicker.getDensityDpi(20.0f, getContext());
        if (x <= ((float) this.shadowWidth) || x >= ((float) (getWidth() - this.shadowWidth)) || y <= ((float) this.shadowWidth) || y >= ((float) ((getHeight() - this.shadowWidth) - a))) {
            return false;
        }
        return true;
    }
}
