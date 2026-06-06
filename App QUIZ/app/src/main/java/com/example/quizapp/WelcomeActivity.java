package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quizapp.SharedPrefManager;
import java.util.UUID;

public class WelcomeActivity extends AppCompatActivity {
    private EditText edtNickname;
    private Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        edtNickname = findViewById(R.id.edtNickname);
        btnStart = findViewById(R.id.btnStart);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtNickname.getText().toString().trim();
                if (name.isEmpty()) {
                    Toast.makeText(WelcomeActivity.this, "Vui lòng nhập biệt danh!", Toast.LENGTH_SHORT).show();
                } else {
                    String guestId = UUID.randomUUID().toString();
                    SharedPrefManager.saveUserData(WelcomeActivity.this, name, guestId);
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    finish();
                }
            }
        });
    }
}