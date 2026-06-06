package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quizapp.FirebaseManager;
import com.example.quizapp.SharedPrefManager;
import com.example.quizapp.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class BattleQuizActivity extends AppCompatActivity {
    private TextView tvPlayerMe, tvPlayerOpponent, tvScoreMe, tvScoreOpponent, tvBattleStatus;
    private DatabaseReference dbRef;
    private String myId, myName, opponentId = "";
    private String roomId = "";
    private int myScore = 0, opponentScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);
        initViews();
        dbRef = FirebaseManager.getDatabase();
        myId = SharedPrefManager.getGuestId(this);
        myName = SharedPrefManager.getUsername(this);
        tvPlayerMe.setText(myName);
        findMatch();
    }

    private void initViews() {
        tvPlayerMe = findViewById(R.id.tvPlayerMe);
        tvPlayerOpponent = findViewById(R.id.tvPlayerOpponent);
        tvScoreMe = findViewById(R.id.tvScoreMe);
        tvScoreOpponent = findViewById(R.id.tvScoreOpponent);
        tvBattleStatus = findViewById(R.id.tvBattleStatus);
    }

    private void findMatch() {
        tvBattleStatus.setText("Đang tìm đối thủ...");
        User me = new User(myId, myName, 0);
        dbRef.child("waiting_room").child(myId).setValue(me);

        dbRef.child("waiting_room").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    String key = child.getKey();
                    if (!key.equals(myId)) {
                        opponentId = key;
                        User opponent = child.getValue(User.class);
                        if (opponent != null) tvPlayerOpponent.setText(opponent.getUsername());
                        roomId = myId + "_vs_" + opponentId;
                        dbRef.child("rooms").child(roomId).child(myId).setValue(0);
                        dbRef.child("rooms").child(roomId).child(opponentId).setValue(0);
                        dbRef.child("waiting_room").child(myId).removeValue();
                        dbRef.child("waiting_room").child(opponentId).removeValue();
                        listenToRoomProgress();
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void listenToRoomProgress() {
        tvBattleStatus.setText("Trận đấu bắt đầu!");
        dbRef.child("rooms").child(roomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(opponentId)) {
                    opponentScore = snapshot.child(opponentId).getValue(Integer.class);
                    tvScoreOpponent.setText("Điểm: " + opponentScore);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void onAnswerCorrect() {
        myScore += 10;
        tvScoreMe.setText("Điểm: " + myScore);
        if (!roomId.isEmpty()) {
            dbRef.child("rooms").child(roomId).child(myId).setValue(myScore);
        }
    }
}
