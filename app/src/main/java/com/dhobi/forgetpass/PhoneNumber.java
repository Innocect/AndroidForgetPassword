package com.dhobi.forgetpass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PhoneNumber extends AppCompatActivity {

    Button button;
    EditText editText;
    String phoneNumber;
    String url = "http://192.168.0.103:8000/apis/generate_otp/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNumber = editText.getText().toString();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("error").equals("false")) {
                                        startActivity(new Intent(PhoneNumber.this,MainActivity.class));
                                Toast.makeText(PhoneNumber.this, response.toString(), Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(PhoneNumber.this, response.toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(PhoneNumber.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    }
                }) {
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("phone_no",phoneNumber);

                        return params;
                    }
                };
                MySingleton.getInstance(PhoneNumber.this).addToRequestqueue(stringRequest);
            }
        });

    }
}

