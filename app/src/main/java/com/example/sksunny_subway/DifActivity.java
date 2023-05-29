package com.example.sksunny_subway;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.AppCompatButton;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;
import java.util.ArrayList;

public class DifActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dif);

        AppCompatButton maintainBtn = findViewById(R.id.maintainBtn);
        AppCompatButton completeBtn = findViewById(R.id.completeBtn);

        maintainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "기존 내용을 유지합니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "수정된 내용이 저장되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });



        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        TextView upperScroll = findViewById(R.id.upperScroll);
        RecyclerView lowerScroll = findViewById(R.id.lowerScroll);
        lowerScroll.setLayoutManager(new LinearLayoutManager((this)));

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        RecyclerAdapter lowerAdapter = new RecyclerAdapter();
        lowerScroll.setAdapter(lowerAdapter);

        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(lowerAdapter));
        mItemTouchHelper.attachToRecyclerView(lowerScroll);

        ArrayList<ListItem> list = new ArrayList<>();
        lowerAdapter.setItems(list);

        //radiogroup 추가
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.navbar);

        RadioButton elevator = (RadioButton) findViewById(R.id.elevator);
        RadioButton walk = (RadioButton) findViewById(R.id.walk);
        RadioButton pass = (RadioButton) findViewById(R.id.pass);
        RadioButton getOff = (RadioButton) findViewById(R.id.getOff);

        elevator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListItem item = new ListItem("elevator",0);
                list.add(item);
                lowerAdapter.notifyItemInserted(list.size());
            }
        });

        walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListItem item = new ListItem("walk",0);
                list.add(item);
                lowerAdapter.notifyItemInserted(list.size());
            }
        });

        //얘는 확정! 더 손 안 대도 됨!
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListItem item = new ListItem("pass",0);
                list.add(item);
                lowerAdapter.notifyItemInserted(list.size());
            }
        });

        getOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListItem item = new ListItem("getOff",0);
                list.add(item);
                lowerAdapter.notifyItemInserted(list.size());
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}