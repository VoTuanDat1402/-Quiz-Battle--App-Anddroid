package com.example.quizapp;

import androidx.annotation.NonNull;
import com.example.quizapp.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;

public class QuestionBank {

    // Tạo một interface để thông báo khi dữ liệu tải xong từ Internet
    public interface FirebaseCallback {
        void onQuestionsLoaded(ArrayList<Question> questions);
        void onError(String errorMessage);
    }

    public static void loadQuestionsFromFirebase(FirebaseCallback callback) {
        ArrayList<Question> list = new ArrayList<>();

        // Gọi đến nhánh "questions" mà bạn đã import dữ liệu AI trên Firebase
        FirebaseManager.getDatabase().child("questions")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        if (snapshot.exists()) {
                            for (DataSnapshot child : snapshot.getChildren()) {
                                // Tự động ép kiểu dữ liệu JSON từ Firebase thành Object Question trong Java
                                String qText = child.child("questionText").getValue(String.class);
                                String opA = child.child("optionA").getValue(String.class);
                                String opB = child.child("optionB").getValue(String.class);
                                String opC = child.child("optionC").getValue(String.class);
                                String opD = child.child("optionD").getValue(String.class);
                                String correct = child.child("correctAnswer").getValue(String.class);

                                Question question = new Question(qText, opA, opB, opC, opD, correct);
                                list.add(question);
                            }
                            // Trộn ngẫu nhiên thứ tự câu hỏi do AI tạo ra để tăng tính hấp dẫn
                            Collections.shuffle(list);
                            callback.onQuestionsLoaded(list);
                        } else {
                            callback.onError("Không tìm thấy dữ liệu câu hỏi trên Firebase!");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError(error.getMessage());
                    }
                });
    }
}
