/*
Copyright (c) 2019 Inverse Palindrome
SensorBox - ThermometerView.java
https://inversepalindrome.com/
*/


package com.inversepalindrome.sensorbox;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


public class ThermometerView extends View {

    private float outerCircleRadius, outerRectRadius;
    private Paint outerPaint;
    private float middleCircleRadius, middleRectRadius;
    private Paint middlePaint;
    private float innerCircleRadius, innerRectRadius;
    private Paint innerPaint;
    private Paint degreePaint, graduationPaint;

    private static final int GRADUATION_TEXT_SIZE = 16; // in sp
    private static float DEGREE_WIDTH = 30;
    private static final int NB_GRADUATIONS = 8;
    public static final float MAX_TEMP = 50, MIN_TEMP = -30;
    private static final float RANGE_TEMP = 80;
    private static final int NB_GRADUATIONS_F = 8;
    private static final float MAX_TEMP_F = 120, MIN_TEMP_F = -30;
    private static final float RANGE_TEMP_F = 150;
    private int nbGraduations = NB_GRADUATIONS;
    private float maxTemp = MAX_TEMP;
    private float minTemp = MIN_TEMP;
    private float rangeTemp = RANGE_TEMP;
    private float currentTemp = MIN_TEMP;
    private Rect rect = new Rect();

    public ThermometerView(Context context) {
        super(context);
        init(context, null);
    }

    public ThermometerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public ThermometerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super (context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setCurrentTemp(float currentTemp) {
        if (currentTemp > maxTemp) {
            this.currentTemp = maxTemp;
        } else if (currentTemp < minTemp) {
            this.currentTemp = minTemp;
        } else {
            this.currentTemp = currentTemp;
        }

        invalidate();
    }

    public float getMinTemp() {
        return minTemp;
    }

    public void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ThermometerView);
        outerCircleRadius = typedArray.getDimension(R.styleable.ThermometerView_radius, 20f);
        int outerColor = typedArray.getColor(R.styleable.ThermometerView_outerColor, Color.GRAY);
        int middleColor = typedArray.getColor(R.styleable.ThermometerView_middleColor, Color.WHITE);
        int innerColor = typedArray.getColor(R.styleable.ThermometerView_innerColor, Color.RED);

        typedArray.recycle();

        outerRectRadius = outerCircleRadius / 2;
        outerPaint = new Paint();

        outerPaint.setColor(outerColor);
        outerPaint.setStyle(Paint.Style.FILL);

        middleCircleRadius = outerCircleRadius - 5;
        middleRectRadius = outerRectRadius - 5;
        middlePaint = new Paint();
        middlePaint.setColor(middleColor);
        middlePaint.setStyle(Paint.Style.FILL);

        innerCircleRadius = middleCircleRadius - middleCircleRadius / 6;
        innerRectRadius = middleRectRadius - middleRectRadius / 6;
        innerPaint = new Paint();
        innerPaint.setColor(innerColor);
        innerPaint.setStyle(Paint.Style.FILL);

        DEGREE_WIDTH = middleCircleRadius / 8;

        degreePaint = new Paint();
        degreePaint.setStrokeWidth(middleCircleRadius / 16);
        degreePaint.setColor(outerColor);
        degreePaint.setStyle(Paint.Style.FILL);

        graduationPaint = new Paint();
        graduationPaint.setColor(outerColor);
        graduationPaint.setStyle(Paint.Style.FILL);
        graduationPaint.setAntiAlias(true);
        graduationPaint.setTextSize(Utils.dpToPx(GRADUATION_TEXT_SIZE));
    }

    public void changeUnit(boolean isCelsius) {
        if (isCelsius) {
            nbGraduations = NB_GRADUATIONS;
            maxTemp = MAX_TEMP;
            minTemp = MIN_TEMP;
            rangeTemp = RANGE_TEMP;
        } else {
            nbGraduations = NB_GRADUATIONS_F;
            maxTemp = MAX_TEMP_F;
            minTemp = MIN_TEMP_F;
            rangeTemp = RANGE_TEMP_F;
        }

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int height = getHeight();
        int width = getWidth();

        int circleCenterX = width / 2;
        float circleCenterY = height - outerCircleRadius;
        float outerStartY = 0;
        float middleStartY = outerStartY + 5;


        float innerEffectStartY = middleStartY + middleRectRadius + 10;
        float innerEffectEndY = circleCenterY - outerCircleRadius - 10;
        float innerRectHeight = innerEffectEndY - innerEffectStartY;
        float innerStartY = innerEffectStartY + (maxTemp - currentTemp) / rangeTemp * innerRectHeight;

        RectF outerRect = new RectF();
        outerRect.left = circleCenterX - outerRectRadius;
        outerRect.top = outerStartY;
        outerRect.right = circleCenterX + outerRectRadius;
        outerRect.bottom = circleCenterY;

        canvas.drawRoundRect(outerRect, outerRectRadius, outerRectRadius, outerPaint);
        canvas.drawCircle(circleCenterX, circleCenterY, outerCircleRadius, outerPaint);

        RectF middleRect = new RectF();
        middleRect.left = circleCenterX - middleRectRadius;
        middleRect.top = middleStartY;
        middleRect.right = circleCenterX + middleRectRadius;
        middleRect.bottom = circleCenterY;

        canvas.drawRoundRect(middleRect, middleRectRadius, middleRectRadius, middlePaint);
        canvas.drawCircle(circleCenterX, circleCenterY, middleCircleRadius, middlePaint);

        canvas.drawRect(circleCenterX - innerRectRadius, innerStartY, circleCenterX + innerRectRadius, circleCenterY, innerPaint);
        canvas.drawCircle(circleCenterX, circleCenterY, innerCircleRadius, innerPaint);

        float tmp = innerEffectStartY;
        float startGraduation = maxTemp;
        float inc = rangeTemp / nbGraduations;

        while (tmp <= innerEffectEndY) {
            canvas.drawLine(circleCenterX - outerRectRadius - DEGREE_WIDTH, tmp, circleCenterX - outerRectRadius, tmp, degreePaint);
            String txt = ((int) startGraduation) + "°";
            graduationPaint.getTextBounds(txt, 0, txt.length(), rect);
            float textWidth = rect.width();
            float textHeight = rect.height();

            canvas.drawText(((int) startGraduation) + "°", circleCenterX - outerRectRadius - DEGREE_WIDTH - textWidth - DEGREE_WIDTH * 1.5f,
                    tmp + textHeight / 2, graduationPaint);
            tmp += (innerEffectEndY - innerEffectStartY) / nbGraduations;
            startGraduation -= inc;
        }
    }
}