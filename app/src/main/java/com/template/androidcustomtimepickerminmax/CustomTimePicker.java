package com.template.androidcustomtimepickerminmax;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.lang.reflect.Field;

public class CustomTimePicker extends TimePicker {

    int minHour, maxHour, minMinute, maxMinute;

    public CustomTimePicker(Context context) {
        super(context);
    }

    public CustomTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomTimePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTimePicker);
        minHour = typedArray.getInt(R.styleable.CustomTimePicker_min_hour, 0);
        maxHour = typedArray.getInt(R.styleable.CustomTimePicker_max_hour, 0);
        minMinute = typedArray.getInt(R.styleable.CustomTimePicker_min_minute, 0);
        maxMinute = typedArray.getInt(R.styleable.CustomTimePicker_min_minute, 0);
        typedArray.recycle();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        // The view id is retrieved from https://android.googlesource.com/platform/frameworks/base/+/ics-mr1-release/core/res/res/layout/time_picker.xml
        try {
            @SuppressLint("PrivateApi")
            Class<?> classForid = Class.forName("com.android.internal.R$id");
            Field fieldHour = classForid.getField("hour");
            Field fieldMinute = classForid.getField("minute");
            final NumberPicker mHourSpinner = findViewById(fieldHour.getInt(null));
            final NumberPicker mMinuteSpinner = findViewById(fieldMinute.getInt(null));
            mHourSpinner.setMaxValue(maxHour);
            mHourSpinner.setMinValue(minHour);
            mMinuteSpinner.setMaxValue(maxMinute);
            mMinuteSpinner.setMinValue(minMinute);
            Field amPm = classForid.getField("amPm");
            mHourSpinner.setMinValue(2);
            final NumberPicker numberPicker = findViewById(amPm.getInt(null));
            numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker np1, int oldVal, int newVal) {
                    if (newVal == 0) { // case AM
                        mHourSpinner.setMinValue(2);
                        mHourSpinner.setMaxValue(5);
                    } else { // case PM
                        mHourSpinner.setMinValue(1);
                        mHourSpinner.setMaxValue(8);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}


