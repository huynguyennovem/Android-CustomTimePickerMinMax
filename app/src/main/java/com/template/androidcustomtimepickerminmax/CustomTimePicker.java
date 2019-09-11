package com.template.androidcustomtimepickerminmax;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomTimePicker extends TimePicker {

    int minHour_am, maxHour_am, minMinute_am, maxMinute_am;
    int minHour_pm, maxHour_pm, minMinute_pm, maxMinute_pm;
    int minuteInterval;

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
        minHour_am = typedArray.getInt(R.styleable.CustomTimePicker_min_hour_am, 0);
        maxHour_am = typedArray.getInt(R.styleable.CustomTimePicker_max_hour_am, 0);
        minMinute_am = typedArray.getInt(R.styleable.CustomTimePicker_min_minute_am, 0);
        maxMinute_am = typedArray.getInt(R.styleable.CustomTimePicker_max_minute_am, 0);
        minHour_pm = typedArray.getInt(R.styleable.CustomTimePicker_min_hour_pm, 0);
        maxHour_pm = typedArray.getInt(R.styleable.CustomTimePicker_max_hour_pm, 0);
        minMinute_pm = typedArray.getInt(R.styleable.CustomTimePicker_min_minute_pm, 0);
        maxMinute_pm = typedArray.getInt(R.styleable.CustomTimePicker_max_minute_pm, 0);
        minuteInterval = typedArray.getInt(R.styleable.CustomTimePicker_minute_interval, 0);
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
            Field amPm = classForid.getField("amPm");
            final NumberPicker hourSpinner = findViewById(fieldHour.getInt(null));
            final NumberPicker minuteSpinner = findViewById(fieldMinute.getInt(null));
            final NumberPicker ampmPicker = findViewById(amPm.getInt(null));

            boolean isAM = ampmPicker.getValue() == 0;
            if (isAM) {
                hourSpinner.setMinValue(minHour_am);
                hourSpinner.setMaxValue(maxHour_am);
                minuteSpinner.setMinValue(minMinute_am);
                minuteSpinner.setMaxValue(maxMinute_am / minuteInterval);
                List<String> list = new ArrayList<>();
                for (int i = minMinute_am; i <= maxMinute_am; i += minuteInterval) {
                    list.add(String.format(Locale.getDefault(), "%02d", i));
                }
                String[] mStringArray = new String[list.size()];
                mStringArray = list.toArray(mStringArray);
                minuteSpinner.setDisplayedValues(mStringArray);
            } else {
                hourSpinner.setMinValue(minHour_pm);
                hourSpinner.setMaxValue(maxHour_pm);
                minuteSpinner.setMinValue(minMinute_pm);
                minuteSpinner.setMaxValue(maxMinute_pm / minuteInterval);
                List<String> list = new ArrayList<>();
                for (int i = minMinute_pm; i <= maxMinute_pm; i += minuteInterval) {
                    list.add(String.format(Locale.getDefault(), "%02d", i));
                }
                String[] mStringArray = new String[list.size()];
                mStringArray = list.toArray(mStringArray);
                minuteSpinner.setDisplayedValues(mStringArray);
            }

            ampmPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker np1, int oldVal, int newVal) {
                    if (newVal == 0) { // case AM
                        hourSpinner.setMinValue(minHour_am);
                        hourSpinner.setMaxValue(maxHour_am);
                        minuteSpinner.setMinValue(minMinute_am);
                        minuteSpinner.setMaxValue(maxMinute_am / minuteInterval);
                    } else { // case PM
                        hourSpinner.setMinValue(minHour_pm);
                        hourSpinner.setMaxValue(maxHour_pm);
                        minuteSpinner.setMinValue(minMinute_pm);
                        minuteSpinner.setMaxValue(maxMinute_pm / minuteInterval);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}


