package com.example.quizbattle;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quizbattle.helper.QuestionBank;
import com.example.quizbattle.model.Question;
import java.util.ArrayList;

public class SoloQuizActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvQuestion, tvScore, tvTimer;
    private ProgressBar progressBarTimer;
    private Button btnA, btnB, btnC, btnD;

    private ArrayList<Question> questionList;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private CountDownTimer countDownTimer;
    private final long TOTAL_TIME = 15000; // 15 giây cho mỗi câu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz); // Cần tạo activity_quiz.xml

        initViews();
        questionList = QuestionBank.getQuestions(); // Lấy bộ câu hỏi từ File số 7
        displayQuestion();
    }

    private void initViews() {
        tvQuestion = findViewById(R.id.tvQuestion);
        tvScore = findViewById(R.id.tvScore);
        tvTimer = findViewById(R.id.tvTimer);
        progressBarTimer = findViewById(R.id.progressBarTimer);
        btnA = findViewById(R.id.btnOptionA);
        btnB = findViewById(R.id.btnOptionB);
        btnC = findViewById(R.id.btnOptionC);
        btnD = findViewById(R.id.btnOptionD);

        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);
    }

    private void displayQuestion() {
        resetButtonColors();
        if (currentQuestionIndex < questionList.size()) {
            Question currentQuestion = questionList.get(currentQuestionIndex);
            tvQuestion.setText(currentQuestion.getQuestionText());
            btnA.setText(currentQuestion.getOptionA());
            btnB.setText(currentQuestion.getOptionB());
            btnC.setText(currentQuestion.getOptionC());
            btnD.setText(currentQuestion.getOptionD());
            tvScore.setText("Điểm: " + score);

            startTimer();
        } else {
            // Hết câu hỏi -> Chuyển sang màn hình kết quả
            Intent intent = new Intent(SoloQuizActivity.this, ResultActivity.class);
            intent.putExtra("FINAL_SCORE", score);
            intent.putExtra("MODE", "SOLO");
            startActivity(intent);
            finish();
        }
    }

    private void startTimer() {
        if (countDownTimer != null) countDownTimer.cancel();

        countDownTimer = new CountDownTimer(TOTAL_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsLeft = (int) (millisUntilFinished / 1000);
                tvTimer.setText(String.valueOf(secondsLeft));
                progressBarTimer.setProgress(secondsLeft * 100 / 15);
            }

            @Override
            public void onFinish() {
                // Hết thời gian coi như trả lời sai, tự động chuyển câu
                currentQuestionIndex++;
                displayQuestion();
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        if (countDownTimer != null) countDownTimer.cancel();

        Button clickedButton = (Button) v;
        String selectedAnswer = "";
        if (clickedButton.getId() == R.id.btnOptionA) selectedAnswer = "A";
        else if (clickedButton.getId() == R.id.btnOptionB) selectedAnswer = "B";
        else if (clickedButton.getId() == R.id.btnOptionC) selectedAnswer = "C";
        else if (clickedButton.getId() == R.id.btnOptionD) selectedAnswer = "D";

        Question currentQuestion = questionList.get(currentQuestionIndex);
        if (selectedAnswer.equals(currentQuestion.getCorrectAnswer())) {
            clickedButton.setBackgroundColor(Color.GREEN);
            score += 10; // Đúng cộng 10 điểm
        } else {
            clickedButton.setBackgroundColor(Color.RED);
            highlightCorrectAnswer(currentQuestion.getCorrectAnswer());
        }

        // Chờ 1.5 giây để người chơi nhìn đáp án trước khi đổi câu mới
        v.postDelayed(new Runnable() {
            @Override
            public void run() {
                currentQuestionIndex++;
                displayQuestion();
            }
        }, 1500);
    }

    private void highlightCorrectAnswer(String correct) {
        if (correct.equals("A")) btnA.setBackgroundColor(Color.GREEN);
        else if (correct.equals("B")) btnB.setBackgroundColor(Color.GREEN);
        else if (correct.equals("C")) btnC.setBackgroundColor(Color.GREEN);
        else if (correct.equals("D")) btnD.setBackgroundColor(Color.GREEN);
    }

    private void resetButtonColors() {
        int defaultColor = Color.parseColor("#E0E0E0");
        btnA.setBackgroundColor(defaultColor);
        btnB.setBackgroundColor(defaultColor);
        btnC.setBackgroundColor(defaultColor);
        btnD.setBackgroundColor(defaultColor);
    }

    @Override
    protected void onDestroy() {
        if (countDownTimer != null) countDownTimer.cancel();
        super.onDestroy();
    }
}