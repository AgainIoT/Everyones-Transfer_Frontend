package com.example.sksunny_subway;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;

public class RegisterActivity extends AppCompatActivity {

    ArrayList<String> floors = new ArrayList<>(Arrays.asList("B5층", "B4층", "B3층", "B2층", "B1층", "1층", "2층", "3층", "4층", "5층"));
    ArrayList<String> locations = new ArrayList<>(Arrays.asList("승강장", "대합실", "외부"));
    AdapterSpinner adapterfloors;
    AdapterSpinner adapterlocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        LinearLayout btn_next = findViewById(R.id.layout_next);
        LinearLayout btn_finish = findViewById(R.id.layout_finish);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DifActivity.class);
                startActivity(intent);
            }
        });

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DifActivity.class);
                startActivity(intent);
            }
        });

        // 층 선택 Dropdown
        Spinner spinner_stfloors= findViewById(R.id.spinner_stfloors);
        Spinner spinner_arfloors = findViewById(R.id.spinner_arfloors);
        adapterfloors = new AdapterSpinner(this, floors); //그 값을 넣어줌
        spinner_stfloors.setAdapter(adapterfloors);
        spinner_arfloors.setAdapter(adapterfloors);

        // 장소 선택 Dropdown
        Spinner spinner_stlocations = findViewById(R.id.spinner_stlocations);
        Spinner spinner_arlocations = findViewById(R.id.spinner_arlocations);
        adapterlocations= new AdapterSpinner(this, locations); //그 값을 넣어줌
        spinner_stlocations.setAdapter(adapterlocations);
        spinner_arlocations.setAdapter(adapterlocations);

        //arrayList
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            list.add(String.format("TEXT %d", i));
        }

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView upperScroll = findViewById(R.id.upperScroll);
        upperScroll.setLayoutManager(new LinearLayoutManager((this)));

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        RecyclerAdapter upperAdapter = new RecyclerAdapter(list);
        upperScroll.setAdapter(upperAdapter);

        //radiogroup 추가
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.navbar);

        RadioButton elevator = (RadioButton)findViewById(R.id.elevator);
        RadioButton walk = (RadioButton)findViewById(R.id.walk);
        RadioButton pass = (RadioButton)findViewById(R.id.pass);
        RadioButton getOff = (RadioButton)findViewById(R.id.getOff);

        elevator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.add("elevator");
                upperAdapter.notifyItemInserted(list.size());
            }
        });

        walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.add("walk");
                upperAdapter.notifyItemInserted(list.size());
            }
        });

        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.add("pass");
                upperAdapter.notifyItemInserted(list.size());
            }
        });

        getOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.add("getOff");
                upperAdapter.notifyItemInserted(list.size());
            }
        });
    }
}