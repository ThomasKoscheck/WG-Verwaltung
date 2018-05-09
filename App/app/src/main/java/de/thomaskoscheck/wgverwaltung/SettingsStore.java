package de.thomaskoscheck.wgverwaltung;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsStore {

    private static SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences(context.getString(R.string.settingsKey), Context.MODE_PRIVATE);
    }

    public static void addValue(String key, String value, Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static Settings load(Context context){
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        String password = sharedPreferences.getString(context.getString(R.string.passwordKey), null);
        String requester = sharedPreferences.getString(context.getString(R.string.requesterKey), null);
        return new Settings(requester, password);
    }

}