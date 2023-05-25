package com.example.sksunny_subway;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    String [] floors = {"B5", "B4", "B3", "B2", "B1", "1", "2", "3", "4", "5"};
    String [] locations = {"승강장", "대합실", "외부"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 층 선택 Dropdown
        Spinner spinnerThree = findViewById(R.id.spinnerThree);
        Spinner spinnerThirteen = findViewById(R.id.spinnerThirteen);
        ArrayAdapter<String> adapterT = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, floors);
        adapterT.setDropDownViewResource(
                android.R.layout.select_dialog_singlechoice);
        spinnerThree.setAdapter(adapterT);
        spinnerThirteen.setAdapter(adapterT);


        // 장소 선택 Dropdown
        Spinner spinnerFive = findViewById(R.id.spinnerFive);
        Spinner spinnerFifteen = findViewById(R.id.spinnerFifteen);
        ArrayAdapter<String> adapterF = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, locations);
        adapterF.setDropDownViewResource(
                android.R.layout.select_dialog_singlechoice);
        spinnerFive .setAdapter(adapterF);
        spinnerFifteen.setAdapter(adapterF);

    }
}