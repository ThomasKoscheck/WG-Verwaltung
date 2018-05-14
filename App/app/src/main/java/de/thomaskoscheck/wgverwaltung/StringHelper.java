package de.thomaskoscheck.wgverwaltung;

public class StringHelper {
    public static String getStringWithZeros(int number, int amountOfCharacters) {
        String filledString = String.valueOf(number);
        while (filledString.length() < amountOfCharacters) {
            filledString = "0" + filledString;
        }
        return filledString;
    }
}
