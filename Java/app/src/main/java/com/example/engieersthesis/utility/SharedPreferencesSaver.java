package com.example.engieersthesis.utility;

import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferencesSaver {
    public static void saveTokenToSharedPreferences(String token, SharedPreferences sharedPreferences) {
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        if (token != null) {
            sharedPreferencesEditor.putString(Consts.TOKEN_KEY, token);
            sharedPreferencesEditor.commit();
        }
    }

    public static void deleteTokenFromSharedPreferences(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        String token = sharedPreferences.getString(Consts.TOKEN_KEY, "");
        if (token.length() != 0) {
            sharedPreferencesEditor.remove(Consts.TOKEN_KEY);
            Log.d("SharedPreferencesSaver", "Token removed " + token + " from device memory");
            sharedPreferencesEditor.commit();
        }
    }
}
