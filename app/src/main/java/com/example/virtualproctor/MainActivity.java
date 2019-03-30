package com.example.virtualproctor;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText mUsernameText, mPasswordText;
    Button mLoginButton;
    String mUsername = "", mPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsernameText = findViewById(R.id.username);
        mPasswordText = findViewById(R.id.password);
        mLoginButton = findViewById(R.id.login);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        mUsername = mUsernameText.getText().toString();
        mPassword = mPasswordText.getText().toString();

        if (!mUsername.equals("") && !mPassword.equals("")) {
            // Send request to flask
            validateCredentials();
        } else if (mUsername.equals("") && mPassword.equals("")) {
            Toast.makeText(getApplicationContext(), "Enter both fields", Toast.LENGTH_SHORT).show();
        } else if (mUsername.equals("")) {
            Toast.makeText(getApplicationContext(), "Enter username", Toast.LENGTH_SHORT).show();
        } else if (mPassword.equals("")) {
            Toast.makeText(getApplicationContext(), "Enter password", Toast.LENGTH_SHORT).show();
        }
    }

    private void validateCredentials() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Config.URL + "android_login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String flag="", role="";
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            role = jsonObject.getString("role");
                            flag = jsonObject.getString("flag");
                            Log.e("RESPONSE", role);
                            Log.e("RESPONSE", flag);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("RESPONSE", response);
                        if (flag.equals("true")) {
                            // start new activity
                            SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.share_preference_filename), MODE_PRIVATE).edit();
                            editor.putString(getString(R.string.login_username), mUsername);
                            editor.putString(getString(R.string.login_password), mPassword);
                            editor.putString(getString(R.string.role), role);
                            editor.apply();
                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                        } else if (response.equals("both_false")) {
                            Toast.makeText(getApplicationContext(), "Both credentials wrong", Toast.LENGTH_SHORT).show();
                        } else if (response.equals("username_false")) {
                            Toast.makeText(getApplicationContext(), "Username wrong", Toast.LENGTH_SHORT).show();
                        } else if (response.equals("password_false")) {
                            Toast.makeText(getApplicationContext(), "Password wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                params.put("username", mUsername);
                params.put("password", mPassword);
                return params;
            }};
        queue.add(stringRequest);
    }
}
