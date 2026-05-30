package com.example.quizbattle;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quizbattle.helper.SharedPrefManager;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash); // Cần tạo activity_splash.xml

        // Chờ 2 giây để tạo hiệu ứng mượt mà, sau đó kiểm tra xem user đã nhập tên chưa
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String username = SharedPrefManager.getUsername(SplashActivity.this);
                if (username != null && !username.isEmpty()) {
                    // Đã có tên -> Vào thẳng màn hình chính
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {
                    // Chưa có tên -> Vào màn hình nhập tên
                    startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
                }
                finish();
            }
        }, 2000);
    }
}