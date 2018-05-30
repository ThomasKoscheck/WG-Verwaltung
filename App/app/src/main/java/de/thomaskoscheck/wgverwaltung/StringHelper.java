package de.thomaskoscheck.wgverwaltung;

public class StringHelper {
    public static String getStringWithZeros(int number, int amountOfCharacters) {
        StringBuilder filledString = new StringBuilder(String.valueOf(number));
        while (filledString.length() < amountOfCharacters) {
            filledString.insert(0, "0");
        }
        return filledString.toString();
    }
}
