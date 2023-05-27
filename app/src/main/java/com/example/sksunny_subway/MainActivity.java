package com.example.sksunny_subway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //백엔드로 받아온 라인 정보
        ArrayList<String> lines = new ArrayList<>();
        AdapterSpinner adapterlines;

        Button register_btn = (Button)findViewById(R.id.register_btn);
        ImageView Image_Search = findViewById(R.id.Image_Search);
        EditText Et_Search = (EditText)findViewById(R.id.Et_Search);

        Image_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String station = Et_Search.getText().toString();
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}