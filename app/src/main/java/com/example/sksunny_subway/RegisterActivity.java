package com.example.sksunny_subway;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        LinearLayout btn_next = findViewById(R.id.linearRoweleven);
        LinearLayout btn_finish = findViewById(R.id.linearRowtwelve);

    }
}