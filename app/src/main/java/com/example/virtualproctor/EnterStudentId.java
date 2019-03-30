package com.example.virtualproctor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EnterStudentId extends AppCompatActivity {
    Button mSendIdBtn;
    EditText mSendIdText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_student_id);
        mSendIdBtn = findViewById(R.id.sendStudentIdBtn);
        mSendIdText = findViewById(R.id.sendStudentIdText);
        mSendIdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterStudentId.this, TeacherChatWindow.class);
                intent.putExtra("to_user", mSendIdText.getText().toString());
                startActivity(intent);
            }
        });
    }
}
