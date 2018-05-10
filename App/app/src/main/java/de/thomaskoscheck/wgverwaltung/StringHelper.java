package de.thomaskoscheck.wgverwaltung;

import android.util.Log;

public class StringHelper {
    static String getStringWithZeros(int number, int amountOfCharacters) {
        String filledString = String.valueOf(number);
        while (filledString.length() < amountOfCharacters) {
            filledString = "0"+filledString;
        }
        Log.d("TK", "filledString: "+ filledString);
        return filledString;
    }
}
