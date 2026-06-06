package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quizapp.SharedPrefManager;

public class MainActivity extends AppCompatActivity {
    private TextView tvWelcome;
    private Button btnSolo, btnBattle, btnLeaderboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvWelcome = findViewById(R.id.tvWelcome);
        btnSolo = findViewById(R.id.btnSolo);
        btnBattle = findViewById(R.id.btnBattle);
        btnLeaderboard = findViewById(R.id.btnLeaderboard);

        String username = SharedPrefManager.getUsername(this);
        tvWelcome.setText("Xin chào, " + username + "!");

        btnSolo.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SoloQuizActivity.class)));
        btnBattle.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, BattleQuizActivity.class)));
        btnLeaderboard.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LeaderboardActivity.class)));
    }
}