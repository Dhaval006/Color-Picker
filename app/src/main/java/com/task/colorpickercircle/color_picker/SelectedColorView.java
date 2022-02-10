package com.task.colorpickercircle.color_picker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class SelectedColorView extends View {
    public int densityDpi;
    private Context context;
    private Path path;
    private Path path1;
    private Paint paint;
    private Paint paint1;
    private Integer selectedColor;

    public SelectedColorView(Context context) {
        this(context, null);
    }

    public SelectedColorView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SelectedColorView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.densityDpi = 10;
        this.selectedColor = Integer.valueOf(-1);
        this.context = context;
        selectedColor();
    }

    public void setSelectedColor(int i) {
        if (i == -1) {
            i = -1;
        }
        if (this.selectedColor.intValue() != i) {
            this.selectedColor = Integer.valueOf(i);
            if (this.paint != null) {
                this.paint.setColor(i);
            }
            invalidate();
        }
    }

    public int getSelectedColor() {
        return this.paint.getColor();
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.paint != null) {
            canvas.drawPath(this.path, this.paint);
            canvas.drawPath(this.path1, this.paint1);
        }
    }

    private void selectedColor() {
        setLayerType(1, null);
        this.paint = new Paint(1);
        this.paint.setStyle(Style.FILL);
        this.paint.setAntiAlias(true);
        if (this.selectedColor.intValue() == -1) {
            this.selectedColor = Integer.valueOf(-1);
        }
        this.paint.setColor(this.selectedColor.intValue());
        this.paint.setShadowLayer(ColorPicker.getDensityDpi(5.0f, getContext()), 0.0f, 0.0f, Color.parseColor("#40000000"));
        this.paint1 = new Paint(1);
        this.paint1.setStyle(Style.STROKE);
        this.paint1.setAntiAlias(true);
        this.paint1.setStrokeWidth(ColorPicker.getDensityDpi(3.0f, this.context));
        this.paint1.setColor(Color.parseColor("#ffffff"));
        this.densityDpi = (int) ColorPicker.getDensityDpi(5.0f, getContext());
    }

    private void sizeChanged() {
        Point point = new Point();
        int width = getWidth();
        if (getHeight() < width) {
            width = getHeight();
        }
        width = (width / 2) - this.densityDpi;
        point.set(getWidth() / 2, getHeight() / 2);
        RectF rectF = new RectF((float) this.densityDpi, (float) this.densityDpi, (float) (this.densityDpi + (width * 2)), (float) (this.densityDpi + (width * 2)));
        float a = ColorCircleView.getCosValue(ColorCircleView.cosAndSinValue, (float) width, point);
        float b = ColorCircleView.getSinValue(ColorCircleView.cosAndSinValue, (float) width, point);
        int a2 = (int) ColorPicker.getDensityDpi(30.0f, this.context);
        this.path = new Path();
        this.path.moveTo(a, b);
        this.path.arcTo(rectF, ColorCircleView.cosAndSinValue, ColorCircleView.fullPartOfValue);
        b = ColorCircleView.getCosValue(ColorCircleView.cosAndSinValue + ColorCircleView.fullPartOfValue, (float) a2, point);
        this.path.lineTo(b, ColorCircleView.getSinValue(ColorCircleView.cosAndSinValue + ColorCircleView.fullPartOfValue, (float) a2, point));
        float a3 = ColorCircleView.getCosValue(ColorCircleView.cosAndSinValue, (float) a2, point);
        float b2 = ColorCircleView.getSinValue(ColorCircleView.cosAndSinValue, (float) a2, point);
        this.path.lineTo(a3, b2);
        b -= a3;
        float a4 = getCosValue(b, ColorCircleView.halfPartOfValue);
        b = (float) Math.sqrt((double) ((a4 * a4) - ((b / 2.0f) * (b / 2.0f))));
        this.path.arcTo(new RectF(((float) (getWidth() / 2)) - a4, (b2 - b) - a4, ((float) (getWidth() / 2)) + a4, a4 + (b2 - b)), 165.0f, -150.0f);
        this.path.lineTo(a3, b2);
        this.path.close();
        this.path1 = new Path();
        this.path1.addCircle((float) (getWidth() / 2), (b2 - b) + ColorPicker.getDensityDpi(0.0f, this.context), ColorCircleView.getDensityDpi(3.3f, this.context), Direction.CCW);
        this.path1.close();
    }

    public static float getCosValue(float f, float f2) {
        return (float) (((double) (f / 2.0f)) / Math.cos(Math.toRadians((double) f2)));
    }

    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        sizeChanged();
    }
}
