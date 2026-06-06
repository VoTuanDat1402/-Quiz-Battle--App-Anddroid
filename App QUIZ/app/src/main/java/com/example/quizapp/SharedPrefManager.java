package com.example.quizapp;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "QuizBattlePrefs";
    private static final String KEY_USERNAME = "key_username";
    private static final String KEY_GUEST_ID = "key_guest_id";

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void saveUserData(Context context, String username, String guestId) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_GUEST_ID, guestId);
        editor.apply();
    }

    public static String getUsername(Context context) {
        return getPrefs(context).getString(KEY_USERNAME, null);
    }

    public static String getGuestId(Context context) {
        return getPrefs(context).getString(KEY_GUEST_ID, null);
    }

    public static void clearData(Context context) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.clear();
        editor.apply();
    }
}
