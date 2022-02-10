package com.task.colorpickercircle.color_picker;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.task.colorpickercircle.R;

import java.util.ArrayList;
import java.util.List;

public class ColorCircleView extends View {
    private static final String TAG = ColorCircleView.class.getSimpleName();
    public int shadowWidth;
    private boolean isShadow;
    private List<Path> pathList;
    private List<DrawPaint> drawPaintList;
    private List<Integer> colorList;
    private List<Integer> emptyList;
    private Paint paint;
    public static float halfPartOfValue = 15f;
    public static float fullPartOfValue = 30f;
    private int sizeOfPart = 12;
    public static float cosAndSinValue = -(90f + halfPartOfValue);

    private class DrawPaint {
        private boolean isPaint = false;
        private Paint paint;
        private Paint paint2;

        DrawPaint(Paint paint, Paint paint2, boolean z) {
            this.isPaint = z;
            this.paint = paint;
            this.paint2 = paint2;
        }

        public boolean getIsPaint() {
            return this.isPaint;
        }

        public Paint getPaint() {
            return this.paint;
        }

        public Paint getPaint2() {
            return this.paint2;
        }
    }

    public ColorCircleView(Context context) {
        this(context, null);
    }

    public ColorCircleView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ColorCircleView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.shadowWidth = 10;
        this.isShadow = true;
        this.pathList = new ArrayList(sizeOfPart);
        this.drawPaintList = new ArrayList(sizeOfPart);
        this.colorList = null;
        this.emptyList = new ArrayList(sizeOfPart);
        setDefaultList();
    }

    private void setDefaultList() {
        setTypeLayer();
        this.emptyList = getEmptyList();
        if (this.colorList == null) {
            setColorList(getTestColorsList());
        }
        this.shadowWidth = (int) getResources().getDimension(R.dimen.shadow_width);
    }

    public void setShadow(boolean z) {
        this.isShadow = z;
        fillShadow();
        setTypeLayer();
    }

    private void setTypeLayer() {
        int i = 1;
        if (!this.isShadow) {
            i = 0;
        }
        if (i != getLayerType()) {
            setLayerType(i, null);
        }
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.pathList != null) {
            canvas.drawCircle((float) (getWidth() / 2), (float) (getHeight() / 2), (float) (((Math.min(getWidth(), getHeight()) / 2) - this.shadowWidth) - 1), this.paint);
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 < sizeOfPart) {
                    Path path = (Path) this.pathList.get(i2);
                    DrawPaint drawPaint = (DrawPaint) this.drawPaintList.get(i2);
                    if (drawPaint.getIsPaint()) {
                        canvas.drawPath(path, drawPaint.getPaint());
                        canvas.drawPath(path, drawPaint.getPaint2());
                    } else {
                        canvas.drawPath(path, drawPaint.getPaint());
                    }
                    i = i2 + 1;
                } else {
                    return;
                }
            }
        }
    }

    public int getRoundValue(float f) {
        float abs = Math.abs(f / fullPartOfValue);
        int round = Math.round(abs);
        Log.d(TAG, "angle = " + f + " colorNo = " + abs + " adjColorNo = " + round);
        if (round == sizeOfPart || round == 0) {
            return ((Integer) this.colorList.get(0)).intValue();
        }
        if (f >= 0.0f) {
            return ((Integer) this.colorList.get(sizeOfPart - round)).intValue();
        }
        return ((Integer) this.colorList.get(round)).intValue();
    }

    public float setAngleAnimation(float f, boolean z) {
        float abs = Math.abs(f / fullPartOfValue);
        int round = Math.round(abs);
        Log.d(TAG, "angle = " + f + " colorNo = " + abs + " adjColorNo = " + round);
        if (round == sizeOfPart) {
            round = 0;
        }
        if (f <= 0.0f) {
            if (round != 0) {
                round = sizeOfPart - round;
            }
            abs = (((float) round) * fullPartOfValue) - 360.0f;
            if (z) {
                animate().rotation(abs).setDuration((long) 100).start();
            } else {
                setRotation(abs);
            }
            Log.d(TAG, "animationAngle = " + abs + " colorNo = " + round);
        } else {
            abs = ((float) round) * fullPartOfValue;
            if (z) {
                animate().rotation(abs).setDuration((long) 100).start();
            } else {
                setRotation(abs);
            }
            Log.d(TAG, "animationAngle = " + abs + " colorNo = " + round);
        }
        return abs;
    }

    public ObjectAnimator setPositionAnimation(float f) {
        float f2;
        int round = Math.round(Math.abs(f / fullPartOfValue));
        if (round == sizeOfPart) {
            round = 0;
        }
        if (f <= 0.0f) {
            if (round != 0) {
                round = sizeOfPart - round;
            }
            f2 = (((float) round) * fullPartOfValue) - 360.0f;
        } else {
            f2 = ((float) round) * fullPartOfValue;
        }
        return ObjectAnimator.ofFloat(this, "rotation", new float[]{f2});
    }

    public float getCurrentIndex(int i) {
        for (int i2 = 0; i2 < sizeOfPart; i2++) {
            int intValue = ((Integer) this.colorList.get(i2)).intValue();
            int intValue2 = ((Integer) this.emptyList.get(i2)).intValue();
            Log.d(TAG, "color = " + i + ", currentColor = " + intValue);
            if (intValue == i || intValue2 == i) {
                float f;
                if (i2 > 0) {
                    intValue2 = sizeOfPart - i2;
                } else {
                    intValue2 = 0;
                }
                float f2 = fullPartOfValue * ((float) intValue2);
                if (f2 == 0.0f) {
                    f2 = 360.0f;
                }
                if (f2 > 180.0f) {
                    f = f2 - 360.0f;
                } else if (f2 < -180.0f) {
                    f = f2 + 360.0f;
                } else {
                    f = f2;
                }
                Log.d(TAG, "goal = " + f2 + ", distance = " + f);
                setRotation(f);
                return f2;
            }
        }
        return -1.0f;
    }

    public float getIndexToRotation(int i) {
        float f = ((float) i) * fullPartOfValue;
        if (f == 0.0f) {
            f = 360.0f;
        }
        float f2 = f > 180.0f ? f - 360.0f : f < -180.0f ? 360.0f + f : f;
        Log.d(TAG, "goal = " + f + ", distance = " + f2);
        setRotation(f2);
        return f;
    }

    public void setCircleColor(int i, int i2) {
        if (this.colorList == null) {
            this.colorList = new ArrayList(sizeOfPart);
        }
        this.colorList.set(i, Integer.valueOf(i2));
        fillShadow();
        invalidate();
    }

    public List<Integer> getColorList() {
        return this.colorList;
    }

    public void setColorList(List<Integer> list) {
        if (this.colorList == null) {
            this.colorList = new ArrayList(sizeOfPart);
        }
        if (list != null && list.size() == sizeOfPart) {
            this.colorList = list;
            fillShadow();
        }
        invalidate();
    }

    public List<Integer> getTestColorsList() {
        ArrayList arrayList = new ArrayList(sizeOfPart);
        arrayList.add(Integer.valueOf(Color.parseColor("#f44336")));
        arrayList.add(Integer.valueOf(Color.parseColor("#e91e63")));
        arrayList.add(Integer.valueOf(Color.parseColor("#9c27b0")));
        arrayList.add(Integer.valueOf(Color.parseColor("#ffc107")));
        arrayList.add(Integer.valueOf(Color.parseColor("#3f51b5")));
        arrayList.add(Integer.valueOf(Color.parseColor("#2196f3")));
        arrayList.add(Integer.valueOf(Color.parseColor("#ff9800")));
        arrayList.add(Integer.valueOf(Color.parseColor("#607d8b")));
        arrayList.add(Integer.valueOf(Color.parseColor("#009688")));
        arrayList.add(Integer.valueOf(Color.parseColor("#4caf50")));
        arrayList.add(Integer.valueOf(Color.parseColor("#795548")));
        arrayList.add(Integer.valueOf(Color.parseColor("#cddc39")));
        return arrayList;
    }

    private List<Integer> getEmptyList() {
        ArrayList arrayList = new ArrayList(sizeOfPart);
        String format = String.format("#%02x%02x%02x", new Object[]{Integer.valueOf(255), Integer.valueOf(255), Integer.valueOf(255)});
        String format2 = String.format("#%02x%02x%02x", new Object[]{Integer.valueOf(255), Integer.valueOf(255), Integer.valueOf(255)});
        String format3 = String.format("#%02x%02x%02x", new Object[]{Integer.valueOf(255), Integer.valueOf(255), Integer.valueOf(255)});
        String format4 = String.format("#%02x%02x%02x", new Object[]{Integer.valueOf(255), Integer.valueOf(255), Integer.valueOf(255)});
        String format5 = String.format("#%02x%02x%02x", new Object[]{Integer.valueOf(255), Integer.valueOf(255), Integer.valueOf(255)});
        String format6 = String.format("#%02x%02x%02x", new Object[]{Integer.valueOf(255), Integer.valueOf(255), Integer.valueOf(255)});
        String format7 = String.format("#%02x%02x%02x", new Object[]{Integer.valueOf(255), Integer.valueOf(255), Integer.valueOf(255)});
        String format8 = String.format("#%02x%02x%02x", new Object[]{Integer.valueOf(255), Integer.valueOf(255), Integer.valueOf(255)});
        String format9 = String.format("#%02x%02x%02x", new Object[]{Integer.valueOf(255), Integer.valueOf(255), Integer.valueOf(255)});
        String format10 = String.format("#%02x%02x%02x", new Object[]{Integer.valueOf(255), Integer.valueOf(255), Integer.valueOf(255)});
        String format11 = String.format("#%02x%02x%02x", new Object[]{Integer.valueOf(255), Integer.valueOf(255), Integer.valueOf(255)});
        String format12 = String.format("#%02x%02x%02x", new Object[]{Integer.valueOf(255), Integer.valueOf(255), Integer.valueOf(255)});
        arrayList.add(Integer.valueOf(Color.parseColor(format)));
        arrayList.add(Integer.valueOf(Color.parseColor(format2)));
        arrayList.add(Integer.valueOf(Color.parseColor(format3)));
        arrayList.add(Integer.valueOf(Color.parseColor(format4)));
        arrayList.add(Integer.valueOf(Color.parseColor(format5)));
        arrayList.add(Integer.valueOf(Color.parseColor(format6)));
        arrayList.add(Integer.valueOf(Color.parseColor(format7)));
        arrayList.add(Integer.valueOf(Color.parseColor(format8)));
        arrayList.add(Integer.valueOf(Color.parseColor(format9)));
        arrayList.add(Integer.valueOf(Color.parseColor(format10)));
        arrayList.add(Integer.valueOf(Color.parseColor(format11)));
        arrayList.add(Integer.valueOf(Color.parseColor(format12)));
        return arrayList;
    }

    private void fillShadow() {
        this.paint = new Paint(1);
        this.paint.setColor(-1);
        this.paint.setStyle(Style.FILL);
        if (this.isShadow) {
            this.paint.setShadowLayer((float) this.shadowWidth, 0.0f, 0.0f, Color.parseColor("#40000000"));
        }
        this.drawPaintList.clear();
        for (int i = 0; i < sizeOfPart; i++) {
            Paint paint;
            if (((Integer) this.colorList.get(i)).intValue() != -1) {
                paint = new Paint(1);
                paint.setStyle(Style.FILL);
                paint.setAntiAlias(true);
                paint.setColor(((Integer) this.colorList.get(i)).intValue());
                this.drawPaintList.add(new DrawPaint(paint, null, false));
            } else {
                Paint paint2 = new Paint(1);
                paint2.setStyle(Style.FILL);
                paint2.setAntiAlias(true);
                paint2.setColor(-1);
                paint = new Paint(1);
                paint.setStyle(Style.STROKE);
                paint.setAntiAlias(true);
                paint.setColor(Color.parseColor("#bcbbc2"));
                paint.setPathEffect(new DashPathEffect(new float[]{7.0f, 7.0f}, 0.0f));
                paint.setStrokeWidth(3.0f);
                this.drawPaintList.add(new DrawPaint(paint2, paint, true));
            }
        }
    }

    private void sizeChanged() {
        this.pathList.clear();
        Point point = new Point();
        int width = getWidth();
        if (getHeight() < width) {
            width = getHeight();
        }
        int i = (width / 2) - this.shadowWidth;
        point.set(getWidth() / 2, getHeight() / 2);
        RectF rectF = new RectF((float) this.shadowWidth, (float) this.shadowWidth, (float) (this.shadowWidth + (i * 2)), (float) (this.shadowWidth + (i * 2)));
        float f = cosAndSinValue; // set degree of first position
        for (int i2 = 0; i2 < sizeOfPart; i2++) {
            Path path = new Path();
            float a = getCosValue(f, (float) i, point);
            float b = getSinValue(f, (float) i, point);
            path.moveTo((float) point.x, (float) point.y);
            path.lineTo(a, b);
            path.arcTo(rectF, f, fullPartOfValue);
            f += fullPartOfValue;
            if (f > 360.0f) {
                f = halfPartOfValue;
            }
            path.lineTo((float) point.x, (float) point.y);
            path.close();
            this.pathList.add(path);
        }
    }

    public Point getCenterPoint() {
        return new Point(getWidth() / 2, getHeight() / 2);
    }

    public static float getCosValue(float f, float f2, Point point) {
        return (float) ((((double) f2) * Math.cos(Math.toRadians((double) f))) + ((double) point.x));
    }

    public static float getSinValue(float f, float f2, Point point) {
        return (float) ((((double) f2) * Math.sin(Math.toRadians((double) f))) + ((double) point.y));
    }

    public static float getAngleDifference(float f, Point point, Point point2) {
        return ((float) Math.toDegrees((double) ((float) Math.atan2((double) ((float) (point.y - point2.y)), (double) ((float) (point.x - point2.x)))))) - f;
    }

    public float getAngle(float f, Point point, Point point2) {
        int i;
        int round = (int) Math.round(((double) ((float) Math.toDegrees((double) (((float) Math.atan2((double) ((float) (point.y - point2.y)), (double) ((float) (point.x - point2.x)))) - ((float) Math.atan2((double) ((float) (0 - point2.y)), (double) ((float) (point2.x - point2.x)))))))) / fullPartOfValue);
        Log.d(TAG, "angleFromTwoPoints colorNo = " + round);
        if (round < 0) {
            i = round + sizeOfPart;
        } else {
            i = round;
        }
        round = 0;
        if (i > 0) {
            round = sizeOfPart - i;
        }
        float f2 = ((float) round) * fullPartOfValue;
        if (f2 == 0.0f) {
            f2 = 360.0f;
        }
        if (f == 360.0f) {
            f = 0.0f;
        }
        float f3 = f2 - f;
        if (f3 > 180.0f) {
            f3 -= 360.0f;
        } else if (f3 < -180.0f) {
            f3 += 360.0f;
        }
        Log.d(TAG, "goal = " + f2 + ", angle = " + f + " distance = " + f3);
        animate().rotationBy(f3).setDuration(100).start();
        return f2;
    }

    public static float getDensityDpi(float f, Context context) {
        return (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f) * f;
    }

    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        sizeChanged();
    }
}