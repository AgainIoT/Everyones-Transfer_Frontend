package com.example.sksunny_subway;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //백엔드로 받아온 라인 정보
        ArrayList<String> lines = new ArrayList<>();
        AdapterSpinner adapterlines;

        Button register_btn = (Button) findViewById(R.id.register_btn);
        ImageView Image_Search = findViewById(R.id.Image_Search);
        EditText Et_Search = (EditText) findViewById(R.id.Et_Search);

        EditText start_nextst = (EditText) findViewById(R.id.start_nextst);
        EditText arrive_nextst = (EditText) findViewById(R.id.arrive_nextst);
        start_nextst.setInputType(InputType.TYPE_NULL);
        arrive_nextst.setInputType(InputType.TYPE_NULL);

        sendRequest();

        Image_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String station = Et_Search.getText().toString();
                //백엔드로 받아오는데 response가 true이면 입력 아니면 확인해주세요 메세지 띄우기
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

    public void sendRequest() {

        final String API_KEY = BuildConfig.API_KEY;

        try {
            // request body
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("stationName", "디지털미디어시티");
            jsonParams.put("APP_KEY", API_KEY);
            Log.i("request body", jsonParams.toString());

            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://3.39.25.196:8000/station/getStationList";

            // Request a jsonObject response from the provided URL.
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonParams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String responseString = "=======================================";
                                responseString += "\n" + "stationName : " + response.getString("stationName");
                                responseString += "\n" + "collectionID : " +  response.getString("collectionID");
                                responseString += "\n" + "LineCnt : " + response.getString("LineCnt");
                                responseString += "\n" + "Line : " + response.getString("Line");
                                responseString += "\n" + "returnValue : " + response.getString("returnValue");
                                responseString += "\n=======================================";
                                Log.i("response", responseString);
                            } catch (JSONException ex) {
                                Log.e("Ex", ex.toString());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("error", error.getMessage());
                        }
                    });

            // By adding the request to the RequestQueue, make the request.
            queue.add(jsonObjectRequest);
            // Instantiate the RequestQueue.
        } catch (JSONException ex) {
            Log.e("exception", ex.toString());
        }
    }
}