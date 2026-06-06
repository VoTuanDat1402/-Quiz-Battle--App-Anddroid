package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quizapp.FirebaseManager;
import com.example.quizapp.SharedPrefManager;

public class ResultActivity extends AppCompatActivity {
    private TextView tvResultTitle, tvFinalScore;
    private Button btnPlayAgain, btnMainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tvResultTitle = findViewById(R.id.tvResultTitle);
        tvFinalScore = findViewById(R.id.tvFinalScore);
        btnPlayAgain = findViewById(R.id.btnPlayAgain);
        btnMainMenu = findViewById(R.id.btnMainMenu);

        int finalScore = getIntent().getIntExtra("FINAL_SCORE", 0);
        String mode = getIntent().getStringExtra("MODE");

        tvFinalScore.setText("Điểm số của bạn: " + finalScore);

        if ("SOLO".equals(mode)) {
            tvResultTitle.setText("HOÀN THÀNH!");
            updateLeaderboard(finalScore);
        } else {
            tvResultTitle.setText("TRẬN ĐẤU KẾT THÚC!");
        }

        btnPlayAgain.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
        btnMainMenu.setOnClickListener(v -> finish());
    }

    private void updateLeaderboard(int score) {
        String myId = SharedPrefManager.getGuestId(this);
        String myName = SharedPrefManager.getUsername(this);
        if (myId != null && myName != null) {
            FirebaseManager.getDatabase().child("leaderboard").child(myId).child("username").setValue(myName);
            FirebaseManager.getDatabase().child("leaderboard").child(myId).child("score").setValue(score);
        }
    }
}
