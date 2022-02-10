package com.task.colorpickercircle.color_picker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

public class BackSelectedColorView extends View {
    private Path path;
    private Paint paint;

    public BackSelectedColorView(Context context) {
        this(context, null);
    }

    public BackSelectedColorView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public BackSelectedColorView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        onPaint();
    }

    public void setSelectedColor(int i) {
        if (i == -1) {
            i = -1;
        }
        if (this.paint.getColor() != i) {
            this.paint.setColor(i);
            invalidate();
        }
    }

    public int getSelectedColor() {
        return this.paint.getColor();
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.path != null) {
            canvas.drawPath(this.path, this.paint);
        }
    }

    private void onPaint() {
        this.paint = new Paint(1);
        this.paint.setStyle(Style.FILL);
        this.paint.setAntiAlias(true);
        this.paint.setColor(-1);
    }

    private void sizeChanged() {
        int a = (int) ColorPicker.getDensityDpi(5.0f, getContext());
        Point point = new Point();
        int width = getWidth();
        if (getHeight() < width) {
            width = getHeight();
        }
        width = (width / 2) - a;
        point.set(getWidth() / 2, getHeight() / 2);
        float a2 = ColorCircleView.getCosValue(ColorCircleView.cosAndSinValue, (float) width, point);
        float b = ColorCircleView.getSinValue(ColorCircleView.cosAndSinValue, (float) width, point);
        float a3 = ColorCircleView.getCosValue(ColorCircleView.cosAndSinValue + ColorCircleView.fullPartOfValue, (float) width, point);
        float b2 = ColorCircleView.getSinValue(ColorCircleView.cosAndSinValue + ColorCircleView.fullPartOfValue, (float) width, point);
        this.path = new Path();
        this.path.moveTo(a2, (float) (getHeight() / 4));
        this.path.lineTo(a2, b);
        this.path.lineTo(a3, b2);
        this.path.lineTo(a3, (float) (getHeight() / 4));
        this.path.moveTo(a2, (float) (getHeight() / 4));
        this.path.close();
    }

    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        sizeChanged();
    }
}
