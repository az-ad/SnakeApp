package com.example.snake;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chaos.view.PinView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class EmailVerificationActivity extends AppCompatActivity {

    TextInputEditText editText;
    Button button, confirmbtn;
    int code;
    PinView pinView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        webView.loadData("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/VIDEO_ID\" frameborder=\"0\" allowfullscreen></iframe>", "text/html", "utf-8");
        webView.loadUrl("https://youtu.be/bZYPI4mYwhw");




//        editText = findViewById(R.id.signup_Email);
//        button = findViewById(R.id.signup_btn);
//        pinView = findViewById(R.id.firstPinView);
//        confirmbtn = findViewById(R.id.signup_confirm);

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Random random = new Random();
//                code = random.nextInt(8999) + 1000;
//                String url = "https://foysaltech.000webhostapp.com/sendEmail.php";
//                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//                StringRequest stringRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Toast.makeText(EmailVerificationActivity.this, "" + response, Toast.LENGTH_SHORT).show();
//                        findViewById(R.id.LinearLayout_Email).setVisibility(View.GONE);
//                        findViewById(R.id.LinearLayout_PinView).setVisibility(View.VISIBLE);
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(EmailVerificationActivity.this, "" + error, Toast.LENGTH_SHORT).show();
//                    }
//                }) {
//                    @Nullable
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        Map<String, String> mydata = new HashMap<>();
//                        mydata.put("email", editText.getText().toString());
//                        mydata.put("code", String.valueOf(code));
//                        return mydata;
//                    }
//                };
//                requestQueue.add(stringRequest);
//            }
//        });

//        confirmbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String inputCode = pinView.getText().toString();
//                if (inputCode.equals(String.valueOf(code))) {
//                    Intent intent = new Intent(EmailVerificationActivity.this,SnakeClassificationActivity.class);
//                    startActivity(intent);
//                    Toast.makeText(EmailVerificationActivity.this, "Success", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(EmailVerificationActivity.this, "failed", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

    }
}