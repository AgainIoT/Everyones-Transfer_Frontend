package com.example.sksunny_subway;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;
import java.util.ArrayList;

public class DifActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dif);

        TextView btn_modify = findViewById(R.id.txtEleven);
        TextView btn_complete = findViewById(R.id.txtTwelve);

        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(String.format("TEXT %d", i));
        }

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView upperScroll = findViewById(R.id.upperScroll);
        RecyclerView lowerScroll = findViewById(R.id.lowerScroll);
        upperScroll.setLayoutManager(new LinearLayoutManager((this)));
        lowerScroll.setLayoutManager(new LinearLayoutManager((this)));

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        RecyclerAdapter upperAdapter = new RecyclerAdapter(list);
        upperScroll.setAdapter(upperAdapter);
        RecyclerAdapter lowerAdapter = new RecyclerAdapter(list);
        lowerScroll.setAdapter(lowerAdapter);
    }
}