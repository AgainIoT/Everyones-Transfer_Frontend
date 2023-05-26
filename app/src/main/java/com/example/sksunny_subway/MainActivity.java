package com.example.sksunny_subway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
public class MainActivity extends AppCompatActivity {

//    Spinner departSelector = findViewById(R.id.spinnerTwoTwo);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.stations, android.R.layout.simple_spinner_item);
//        departSelector.setAdapter(adapter);

        Button btn = (Button)findViewById(R.id.btn);
        ImageButton btn_search = (ImageButton)findViewById(R.id.imageSearch);
        EditText Et_Search = (EditText)findViewById(R.id.Et_Search);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}