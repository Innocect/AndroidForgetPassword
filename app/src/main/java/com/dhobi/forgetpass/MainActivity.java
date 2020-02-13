package com.dhobi.forgetpass;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button button;
    EditText Password, ConfirmPassword,Otp;
    String password, confirmPassword,otp;
    AlertDialog.Builder builder;
    String restUrl = "http://192.168.0.103:8000/apis/change_password/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.rectangle_4);
        Password = findViewById(R.id.old_passwor);
        ConfirmPassword = findViewById(R.id.new_passwor);
        Otp = findViewById(R.id.otp);
        builder = new AlertDialog.Builder(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(MainActivity.this,PseudoActivity.class));
                password = Password.getText().toString();
                confirmPassword = ConfirmPassword.getText().toString();
                otp = Otp.getText().toString();
                if (password.equals("") && confirmPassword.equals(""))
                {
                    builder.setTitle("Cannot be Empty");
                    builder.setMessage("Please fill all the fields....");
                    displaAlert("input_error");
                }
                else{
                    if(!(password.equals(confirmPassword)))
                    {
                        builder.setTitle("Cannot be Empty");
                        builder.setMessage("Please fill all the fields....");
                        displaAlert("input_error");
                    }
                    else
                    {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, restUrl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {

                                    JSONObject jsonObject = new JSONObject(response);
                                    if(jsonObject.getString("error").equals("false")){
                                        startActivity(new Intent(MainActivity.this,PseudoActivity.class));
                                        Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();

                                    }
                                    else{
                                        Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }){
                          protected Map<String , String > getParams() throws AuthFailureError{
                              Map<String, String> params = new HashMap<>();
                              params.put("otp",otp);
                              params.put("new_password",password);
                              params.put("re_password",confirmPassword);

                              return params;
                          }
                        };
                        MySingleton.getInstance(MainActivity.this).addToRequestqueue(stringRequest);
                    }
                }

            }

        });
    }
    public void displaAlert(final String code)
    {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(code.equals("input_error"))
                {
                    Password.setText("");
                    ConfirmPassword.setText("");
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
