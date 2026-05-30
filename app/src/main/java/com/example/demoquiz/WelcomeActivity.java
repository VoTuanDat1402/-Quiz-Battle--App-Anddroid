package com.example.quizbattle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quizbattle.helper.SharedPrefManager;
import java.util.UUID;

public class WelcomeActivity extends AppCompatActivity {
    private EditText edtNickname;
    private Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome); // Cần tạo activity_welcome.xml

        edtNickname = findViewById(R.id.edtNickname);
        btnStart = findViewById(R.id.btnStart);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtNickname.getText().toString().trim();
                if (name.isEmpty()) {
                    Toast.makeText(WelcomeActivity.this, "Vui lòng nhập biệt danh của bạn!", Toast.LENGTH_SHORT).show();
                } else {
                    // Tạo một mã định danh ngẫu nhiên (Guest ID) không trùng lặp
                    String guestId = UUID.randomUUID().toString();

                    // Lưu dữ liệu vào máy
                    SharedPrefManager.saveUserData(WelcomeActivity.this, name, guestId);

                    // Chuyển sang Menu chính
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}