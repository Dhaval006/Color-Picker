package com.task.colorpickercircle.color_picker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.task.colorpickercircle.R;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public class ColorPicker extends RelativeLayout {
    public static final String TAG = ColorPicker.class.getSimpleName();
    private boolean showSelectedColor = true;
    private boolean useShadow = true;
    private boolean isTouchable = true;
    private List<Integer> colorList;
    public ColorCircleView colorCircleView;
    public CenterCircleView centerCircleView;
    public SelectedColorView selectedColorView;
    public BackSelectedColorView backSelectedColorView;
    public final Handler handler = new Handler();
    private int centerCircleSize;
    public int currentIndex = 0;
    public final BehaviorSubject<Integer> behaviorSubject = BehaviorSubject.create();
    public final PublishSubject<String> publishSubject = PublishSubject.create();
    private ColorCircleTouchListener colorCircleTouchListener;

    public ColorPicker(Context context) {
        super(context);
        addView(LayoutInflater.from(context));
    }

    public ColorPicker(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setView(context, attributeSet);
        addView(LayoutInflater.from(context));
    }

    public ColorPicker(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setView(context, attributeSet);
        addView(LayoutInflater.from(context));
    }

    private void setView(Context context, AttributeSet attributeSet) {

        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ColorPickerView);

        try {
            this.showSelectedColor = typedArray.getBoolean(R.styleable.ColorPickerView_showSelectedColor, true);
            this.useShadow = typedArray.getBoolean(R.styleable.ColorPickerView_useShadow, true);
            this.isTouchable = typedArray.getBoolean(R.styleable.ColorPickerView_isTouchable, true);
            Log.d(TAG, "this.isTouchable = " + this.isTouchable);
            this.centerCircleSize = typedArray.getDimensionPixelSize(R.styleable.ColorPickerView_centerCircleSize, 0);
            Log.d(TAG, "this.centerSize = " + this.centerCircleSize);
        } finally {
            typedArray.recycle();
        }
    }

    private void addView(LayoutInflater layoutInflater) {
        this.colorCircleView = (ColorCircleView) layoutInflater.inflate(R.layout.color_picker_color_circle_view, this, false);
        this.centerCircleView = (CenterCircleView) layoutInflater.inflate(R.layout.color_picker_center_view, this, false);
        this.selectedColorView = (SelectedColorView) layoutInflater.inflate(R.layout.color_picker_selected_view, this, false);
        this.backSelectedColorView = (BackSelectedColorView) layoutInflater.inflate(R.layout.color_picker_back_selected_view, this, false);
        addView(this.backSelectedColorView);
        addView(this.colorCircleView);
        addView(this.centerCircleView);
        addView(this.selectedColorView);
        setShowSelectedColor(this.showSelectedColor);
        setShowShadow(this.useShadow);
        setTouchable(this.isTouchable);
        setColorList(colorCircleView.getTestColorsList());
        this.centerCircleView.getLayoutParams().height = this.centerCircleSize;
        this.centerCircleView.getLayoutParams().width = this.centerCircleSize;
        Log.d(TAG, "height : " + this.centerCircleView.getLayoutParams().height + " width : " + this.centerCircleView.getLayoutParams().width);
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        if (this.isTouchable) {
            this.colorCircleView.setOnTouchListener(new ColorCircleTouchListener());
        }
        int a = this.colorCircleView.getRoundValue(0.0f);
        this.behaviorSubject.onNext(Integer.valueOf(a));
        this.selectedColorView.setSelectedColor(a);
        this.backSelectedColorView.setSelectedColor(setDefaultSelectedColor(a, 0.7f));
    }

    class ColorCircleTouchListener implements OnTouchListener {
        boolean mIsDraggingWheel = false;
        private float getX = -1.0f;
        private float getY = -1.0f;
        private float totalRotation = 0.0f;
        private float angleInDegree = -1.0f;

        ColorCircleTouchListener() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            float x;
            float y;
            Point point;
            if (motionEvent.getAction() == 0) {
                Point centerPoint = ColorPicker.this.colorCircleView.getCenterPoint();
                this.getX = motionEvent.getX();
                this.getY = motionEvent.getY();
                mIsDraggingWheel = true;
                this.angleInDegree = (float) Math.atan2((double) (this.getY - ((float) centerPoint.y)), (double) (this.getX - ((float) centerPoint.x)));
                this.angleInDegree = (float) Math.toDegrees((double) this.angleInDegree);
            } else if (motionEvent.getAction() == 2) {
                x = motionEvent.getX();
                y = motionEvent.getY();
                float a = ColorPicker.getDensityDpi(10.0f, ColorPicker.this.getContext());
                if (Math.abs(y - this.getY) > a || Math.abs(x - this.getX) > a) {
                    mIsDraggingWheel = false;
                    point = new Point((int) x, (int) y);
                    Point centerPoint2 = ColorPicker.this.colorCircleView.getCenterPoint();
                    this.getX = x;
                    this.getY = y;
                    x = ColorCircleView.getAngleDifference(this.angleInDegree, point, centerPoint2);
                    setCurrentIndex(this.totalRotation + x);
                    Log.d(ColorPicker.TAG, "angle = " + x + " totalRotation = " + this.totalRotation);
                    if (x > 0.0f && this.totalRotation > 360.0f) {
                        setCurrentIndex(this.totalRotation - 360.0f);
                    } else if (x < 0.0f && this.totalRotation < -360.0f) {
                        setCurrentIndex(this.totalRotation + 360.0f);
                    }
                    Log.d(ColorPicker.TAG, "angle = " + x);
                    Log.d(ColorPicker.TAG, "totalRotation = " + this.totalRotation);
                    view.animate().rotation(this.totalRotation).setDuration(0).start();
                }
            } else if (motionEvent.getAction() == 1) {
                int a2;
                if (mIsDraggingWheel) {
                    x = motionEvent.getX();
                    y = motionEvent.getY();
                    point = ColorPicker.this.colorCircleView.getCenterPoint();
                    x = ColorPicker.this.colorCircleView.getAngle(this.totalRotation, new Point((int) x, (int) y), point);
                    if (x != -1.0f) {
                        setCurrentIndex(x);
                    }
                    a2 = ColorPicker.this.colorCircleView.getRoundValue(x);
                    if (a2 != -1) {
                        ColorPicker.this.behaviorSubject.onNext(Integer.valueOf(a2));
                        ColorPicker.this.selectedColorView.setSelectedColor(a2);
                        ColorPicker.this.backSelectedColorView.setSelectedColor(ColorPicker.setDefaultSelectedColor(a2, 0.7f));
                    } else {
                        ColorPicker.this.behaviorSubject.onNext(Integer.valueOf(-1));
                    }
                } else {
                    setCurrentIndex(ColorPicker.this.colorCircleView.setAngleAnimation(this.totalRotation, true));
                    a2 = ColorPicker.this.colorCircleView.getRoundValue((float) ((int) this.totalRotation));
                    if (a2 != -1) {
                        ColorPicker.this.behaviorSubject.onNext(Integer.valueOf(a2));
                        ColorPicker.this.selectedColorView.setSelectedColor(a2);
                        ColorPicker.this.backSelectedColorView.setSelectedColor(ColorPicker.setDefaultSelectedColor(a2, 0.7f));
                    } else {
                        ColorPicker.this.behaviorSubject.onNext(Integer.valueOf(-1));
                    }
                }
                this.getX = -1.0f;
                this.getY = -1.0f;
                mIsDraggingWheel = true;
            }
            return true;
        }

        public void setCurrentIndex(float f) {
            this.totalRotation = f;
            float f2 = this.totalRotation;
            if (f2 < 0.0f) {
                f2 = -f2;
            }
            ColorPicker.this.currentIndex = 10 - (((int) f2) / 36);
            if (ColorPicker.this.currentIndex == 10) {
                ColorPicker.this.currentIndex = 0;
            }
            Log.d(ColorPicker.TAG, "currentIndex = " + ColorPicker.this.currentIndex);
        }
    }

    public void setCurrentPartIndex(int color) {
        float a = this.colorCircleView.getCurrentIndex(color);
        this.behaviorSubject.onNext(Integer.valueOf(color));
        this.selectedColorView.setSelectedColor(color);
        this.backSelectedColorView.setSelectedColor(setDefaultSelectedColor(color, 0.7f));
        if (a != -1.0f) {
            this.colorCircleTouchListener.setCurrentIndex(a);
        }
    }

    public void resetCircle() {
        if (this.colorList != null && !this.colorList.isEmpty()) {
            this.selectedColorView.setAlpha(1.0f);
            int intValue = ((Integer) this.colorList.get(0)).intValue();
            this.selectedColorView.setSelectedColor(intValue);
            intValue = setDefaultSelectedColor(intValue, 0.7f);
            this.backSelectedColorView.setAlpha(1.0f);
            this.backSelectedColorView.setSelectedColor(intValue);
            invalidate();
        }
    }

    public boolean getShowSelectedColor() {
        return this.showSelectedColor;
    }

    public void setShowSelectedColor(boolean z) {
        this.showSelectedColor = z;
        if (this.showSelectedColor) {
            this.selectedColorView.setVisibility(VISIBLE);
            this.backSelectedColorView.setVisibility(VISIBLE);
            return;
        }
        this.selectedColorView.setVisibility(GONE);
        this.backSelectedColorView.setVisibility(GONE);
    }

    public boolean getShowShadow() {
        return this.useShadow;
    }

    public void setShowShadow(boolean z) {
        this.useShadow = z;
        this.colorCircleView.setShadow(this.useShadow);
    }

    public List<Integer> getColorList() {
        return this.colorList;
    }

    public void setColorList(List<Integer> list) {
        this.currentIndex = 0;
        this.colorList = list;
        this.colorCircleView.setColorList(this.colorList);
        resetCircle();
        if (this.isTouchable) {
            this.colorCircleView.setAngleAnimation(0.0f, false);
            this.colorCircleTouchListener = new ColorCircleTouchListener();
            this.colorCircleView.setOnTouchListener(this.colorCircleTouchListener);
        }
        this.behaviorSubject.onNext(this.colorList.get(0));
        forceLayout();
    }

    public void setPartColor(int color) {
        this.colorList.set(this.currentIndex, Integer.valueOf(color));
        this.colorCircleView.setCircleColor(this.currentIndex, color);
        this.selectedColorView.setSelectedColor(color);
        this.backSelectedColorView.setSelectedColor(setDefaultSelectedColor(color, 0.7f));
        invalidate();
    }

    public int getCurrentIndex() {
        return this.currentIndex;
    }

    public boolean getSelected() {
        return this.colorCircleView.isSelected();
    }

    public void setSelected(boolean z) {
        this.colorCircleView.setSelected(z);
    }

    public void setTouchable(boolean z) {
        this.centerCircleView.isTouchable = z;
    }

    public Observable<Integer> getBehaviorSubject() {
        return this.behaviorSubject;
    }

    public Observable<String> getPublishSubject() {
        return this.publishSubject;
    }

    protected static int setDefaultSelectedColor(int i, float f) {
        return Color.argb((-16777216 & i) >> 24, (int) (((float) ((16711680 & i) >> 16)) * f), (int) (((float) ((65280 & i) >> 8)) * f), (int) (((float) (i & 255)) * f));
    }

    public static float getDensityDpi(float f, Context context) {
        return (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f) * f;
    }
}
