package com.example.sksunny_subway;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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


        LinearLayout btn_next = findViewById(R.id.linearRoweleven);
        LinearLayout btn_finish = findViewById(R.id.linearRowtwelve);
        // 층 선택 Dropdown
        Spinner spinnerThree = findViewById(R.id.spinnerThree);
        Spinner spinnerThirteen = findViewById(R.id.spinnerThirteen);
        adapterfloors = new AdapterSpinner(this, floors); //그 값을 넣어줌
        spinnerThree.setAdapter(adapterfloors);
        spinnerThirteen.setAdapter(adapterfloors);

        // 장소 선택 Dropdown
        Spinner spinnerFive = findViewById(R.id.spinnerFive);
        Spinner spinnerFifteen = findViewById(R.id.spinnerFifteen);
        adapterlocations= new AdapterSpinner(this, locations); //그 값을 넣어줌
        spinnerFive.setAdapter(adapterlocations);
        spinnerFifteen.setAdapter(adapterlocations);

    }
}