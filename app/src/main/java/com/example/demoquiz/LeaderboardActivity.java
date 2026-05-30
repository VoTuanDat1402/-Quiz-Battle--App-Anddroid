package com.example.quizbattle;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quizbattle.helper.FirebaseManager;
import com.example.quizbattle.helper.LeaderboardAdapter;
import com.example.quizbattle.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;

public class LeaderboardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LeaderboardAdapter adapter;
    private ArrayList<User> leaderboardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard); // Cần tạo activity_leaderboard.xml

        recyclerView = findViewById(R.id.recyclerViewLeaderboard);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        leaderboardList = new ArrayList<>();
        adapter = new LeaderboardAdapter(leaderboardList);
        recyclerView.setAdapter(adapter);

        loadLeaderboardData();
    }

    private void loadLeaderboardData() {
        // Lấy danh sách từ nhánh "leaderboard", sắp xếp theo thuộc tính "score" cao nhất (giới hạn lấy top 20)
        Query query = FirebaseManager.getDatabase().child("leaderboard").orderByChild("score").limitToLast(20);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                leaderboardList.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    User user = child.getValue(User.class);
                    leaderboardList.add(user);
                }
                // Do Firebase trả về sắp xếp tăng dần, cần đảo ngược mảng để điểm cao nhất lên đầu
                Collections.reverse(leaderboardList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}