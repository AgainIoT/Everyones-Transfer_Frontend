package com.example.sksunny_subway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DifActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dif);

        TextView btn_modify = findViewById(R.id.txtEleven);
        TextView btn_complete = findViewById(R.id.txtTwelve);

        EditText btn_elevator = findViewById(R.id.etSix);
        TextView btn_move = findViewById(R.id.txtOptionOne);
        EditText btn_turnstile = findViewById(R.id.etOptionTwo);
        TextView btn_getoff = findViewById(R.id.txtOptionThree);


    }
}