package com.example.virtualproctor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TeacherGroupActivity extends AppCompatActivity {
    Button mSendNotificationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_group);
        mSendNotificationBtn = findViewById(R.id.sendNotificationBtn);

        mSendNotificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code to broadcast notification to a group
            }
        });
    }
}
