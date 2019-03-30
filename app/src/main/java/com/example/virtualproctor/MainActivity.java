package com.example.virtualproctor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    EditText mUsernameText, mPasswordText;
    Button mLoginButton;
    String mUsername = "", mPassword = "";
    SharedPreferences prefs;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private static String user_role = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences(getString(R.string.share_preference_filename), MODE_PRIVATE);

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

                            user_role = role;
                            Log.e("RESPONSE", role);
                            Log.e("RESPONSE", flag);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("RESPONSE", response);
                        if (flag.equals("true")) {
                            // start new activity
                            final SharedPreferences.Editor editor = prefs.edit();
                            editor.putString(getString(R.string.login_username), mUsername);
                            editor.putString(getString(R.string.login_password), mPassword);
                            editor.putString(getString(R.string.role), role);
                            editor.apply();



                            String register_key = prefs.getString(getString(R.string.firebase_register_key), null);
                            if(register_key == null){
                                // key not set, so save key in shared prefs and send a copy to server

                                // Get token
                                // [START retrieve_current_token]
                                FirebaseInstanceId.getInstance().getInstanceId()
                                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                if (!task.isSuccessful()) {
                                                    Log.e(TAG, "getInstanceId failed", task.getException());
                                                    return;
                                                }

                                                // Get new Instance ID token
                                                String token = task.getResult().getToken();

                                                editor.putString(getString(R.string.firebase_register_key), token);
                                                editor.apply();

                                                sendRegisterKeyToServer(token);

                                                // Log and toast
                                                String msg = getString(R.string.msg_token_fmt, token);
                                                Log.d(TAG, msg);


                                            }
                                        });
                                // [END retrieve_current_token]
                            }
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
//                Log.e(TAG, error.getMessage());
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

    void sendRegisterKeyToServer(final String register_key){
        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        //setup the url
        String ssn = prefs.getString(getString(R.string.login_username), null);
        if(ssn == null){
            Log.e(TAG, "SSN NOT SET");
            return;
        }

        String url = Config.URL+"register?username="+ssn+"&fcm_id="+register_key;
        Log.d(TAG, url);

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response from server "+response.toString());
                if(user_role == null){
                    Log.e(TAG, "user_role not set");
                    return;
                }else if( user_role.equals("parent")){
                    startActivity(new Intent(MainActivity.this, ChatWindow.class));
                }else if(user_role.equals("teacher")){
                    startActivity(new Intent(MainActivity.this, TeacherUserChat.class));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,"Error :" + error.toString());
            }
        });

        mRequestQueue.add(mStringRequest);
    }
}
