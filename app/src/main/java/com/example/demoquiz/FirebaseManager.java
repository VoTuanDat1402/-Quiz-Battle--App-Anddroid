package com.example.quizbattle.helper;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseManager {
    private static DatabaseReference databaseReference;

    public static DatabaseReference getDatabase() {
        if (databaseReference == null) {
            // Kết nối đến nút gốc của Firebase Database
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }
        return databaseReference;
    }
}