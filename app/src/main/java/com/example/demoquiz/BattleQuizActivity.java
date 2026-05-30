package com.example.quizbattle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quizbattle.helper.FirebaseManager;
import com.example.quizbattle.helper.SharedPrefManager;
import com.example.quizbattle.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class BattleQuizActivity extends AppCompatActivity {
    private TextView tvPlayerMe, tvPlayerOpponent, tvScoreMe, tvScoreOpponent, tvBattleStatus;
    private Button btnOptionA, btnOptionB, btnOptionC, btnOptionD;

    private DatabaseReference dbRef;
    private String myId, myName, opponentId = "";
    private String roomId = "";
    private int myScore = 0, opponentScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle); // Cần tạo activity_battle.xml

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
        // Khởi tạo các Button đáp án tương tự như SoloQuizActivity...
    }

    private void findMatch() {
        tvBattleStatus.setText("Đang tìm đối thủ...");

        // Đẩy thông tin lên hàng chờ "waiting_room"
        User me = new User(myId, myName, 0);
        dbRef.child("waiting_room").child(myId).setValue(me);

        // Lắng nghe xem có ai khác trong phòng chờ không
        dbRef.child("waiting_room").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    String key = child.getKey();
                    if (!key.equals(myId)) {
                        // Tìm thấy đối thủ! Tạo một phòng đấu ngẫu nhiên
                        opponentId = key;
                        User opponent = child.getValue(User.class);
                        tvPlayerOpponent.setText(opponent.getUsername());

                        roomId = myId + "_vs_" + opponentId;

                        // Đưa cả 2 vào phòng đấu chung và xóa khỏi hàng chờ
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

        // Liên tục lắng nghe biến động điểm số của đối thủ từ phòng chung trên Firebase
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

    // Khi người chơi bấm chọn đáp án đúng ở giao diện
    public void onAnswerCorrect() {
        myScore += 10;
        tvScoreMe.setText("Điểm: " + myScore);
        // Đẩy điểm số mới lên Firebase ngay lập tức để đồng bộ sang máy đối thủ
        if (!roomId.isEmpty()) {
            dbRef.child("rooms").child(roomId).child(myId).setValue(myScore);
        }
    }

    // Xử lý kết thúc trận và so điểm tương tự Solo Mode, chuyển qua ResultActivity
}