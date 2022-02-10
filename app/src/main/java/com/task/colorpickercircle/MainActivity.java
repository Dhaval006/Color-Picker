package com.task.colorpickercircle;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.task.colorpickercircle.color_picker.ColorPicker;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ColorPicker colorPicker;
    TextView hello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        colorPicker = findViewById(R.id.color_picker_layout);
        colorPicker.setColorList(getTestColorsList());
//        colorPicker.setPartColor(getTestColorsList().get(3));

        hello = findViewById(R.id.hello);

        hello.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                hello.setBackgroundColor(colorPicker.selectedColorView.getSelectedColor());
                return false;
            }
        });

        hello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hello.setTextColor(colorPicker.selectedColorView.getSelectedColor());
            }
        });


    }

    private List<Integer> getTestColorsList() {
        ArrayList arrayList = new ArrayList(12);
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
}
