package ar.edu.itba.hci.profitapp;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {
    private final String AUTH_TOKEN = "auth_token";
    private final String ROUTINE_DISPLAY = "routine_display"; //0 simple, 1 detallada

    private final SharedPreferences sharedPreferences;

    public AppPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
    }

    public void setAuthToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AUTH_TOKEN, token);
        editor.apply();
    }

    public String getAuthToken() {
        return sharedPreferences.getString(AUTH_TOKEN, null);
    }

    public int getRoutineDisplayMode() {
        return  sharedPreferences.getInt(ROUTINE_DISPLAY, 0);
    }

    public void setRoutineDisplayMode(Integer mode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(ROUTINE_DISPLAY, mode);
        editor.apply();
    }
}