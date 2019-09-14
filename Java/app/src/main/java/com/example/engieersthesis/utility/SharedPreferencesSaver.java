package com.example.engieersthesis.utility;

import android.content.SharedPreferences;

public class SharedPreferencesSaver {
    public static void saveTokenToSharedPreferences(String token, SharedPreferences sharedPreferences) {
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        if (token != null) {
            sharedPreferencesEditor.putString("token", token);
            sharedPreferencesEditor.commit();
        }
    }
}
