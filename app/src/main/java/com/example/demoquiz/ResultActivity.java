package com.example.quizbattle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quizbattle.helper.FirebaseManager;
import com.example.quizbattle.helper.SharedPrefManager;

public class ResultActivity extends AppCompatActivity {
    private TextView tvResultTitle, tvFinalScore;
    private Button btnPlayAgain, btnMainMenu;

    @Override
    protected void Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result); // Cần tạo activity_result.xml

        tvResultTitle = findViewById(R.id.tvResultTitle);
        tvFinalScore = findViewById(R.id.tvFinalScore);
        btnPlayAgain = findViewById(R.id.btnPlayAgain);
        btnMainMenu = findViewById(R.id.btnMainMenu);

        int finalScore = getIntent().getIntExtra("FINAL_SCORE", 0);
        String mode = getIntent().getStringExtra("MODE");

        tvFinalScore.setText("Điểm số của bạn: " + finalScore);

        if ("SOLO".equals(mode)) {
            tvResultTitle.setText("HOÀN THÀNH!");
            // Cập nhật điểm số này lên bảng xếp hạng chung toàn cầu
            updateLeaderboard(finalScore);
        } else {
            tvResultTitle.setText("TRẬN ĐẤU KẾT THÚC!");
        }

        btnPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quay lại màn hình chọn chế độ
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        btnMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updateLeaderboard(int score) {
        String myId = SharedPrefManager.getGuestId(this);
        String myName = SharedPrefManager.getUsername(this);

        if (myId != null && myName != null) {
            // Đẩy trực tiếp bản ghi điểm lên nhánh "leaderboard" trên Firebase
            FirebaseManager.getDatabase().child("leaderboard").child(myId).child("username").setValue(myName);
            FirebaseManager.getDatabase().child("leaderboard").child(myId).child("score").setValue(score);
        }
    }
}