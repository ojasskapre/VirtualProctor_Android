package com.example.virtualproctor;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class TeacherGroupMain extends AppCompatActivity {
    private static final String TAG = "TeacherGroupMain";

    SharedPreferences prefs;
    String to_group;
    String from_user;

    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_group_main);

        // get the targer_username
        Bundle extras = getIntent().getExtras();
        prefs = getSharedPreferences(getString(R.string.share_preference_filename), MODE_PRIVATE);

        from_user = prefs.getString(getString(R.string.login_username), null);
        to_group = extras.getString("to_group");
        String title = extras.getString("title_name");
        setTitle(title);

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        final EditText message_editTxt = findViewById(R.id.message);

        Button send_btn = findViewById(R.id.send_btn);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = message_editTxt.getText().toString();
                if(msg != null && !msg.equals("")) {
                    sendMessageToServer(msg);
                }
            }
        });

    }

    void sendMessageToServer(String message){
        String password = prefs.getString(getString(R.string.login_password), null);

        if(password == null){
            Log.e(TAG, "password not set");
            return;
        }

        String url = Config.URL+"broadcast_to_group";
        Log.d(TAG, url);

        try{
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("from_user", from_user);
            jsonBody.put("group_id", to_group);
            jsonBody.put("msg_body", message);
            jsonBody.put("password", password);
            final String requestBody = jsonBody.toString();
            Log.e(TAG, requestBody);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }){
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

            stringRequest.setRetryPolicy(mRetryPolicy);
            mRequestQueue.add(stringRequest);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
