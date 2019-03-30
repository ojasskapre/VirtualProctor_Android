package com.example.virtualproctor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TeacherNewChatActivity extends AppCompatActivity {
    Button mGroupButton, mParentButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_new_chat);

        mGroupButton = findViewById(R.id.group);
        mParentButton = findViewById(R.id.parent);

        mGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TeacherNewChatActivity.this, TeacherGroupActivity.class));
            }
        });

        mParentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start teacher parent chat
                startActivity(new Intent(TeacherNewChatActivity.this, EnterStudentId.class));
            }
        });
    }
}
