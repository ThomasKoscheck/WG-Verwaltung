package de.thomaskoscheck.wgverwaltung;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Cryptographics {
    public static String encryptString(String rawString, byte[] key, String initVector) throws InvalidAlgorithmParameterException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

        byte[] paddedBytes = generatedPaddedString(rawString.getBytes());

        byte[] encrypted = cipher.doFinal(paddedBytes);

        return Base64.encodeToString(encrypted, Base64.DEFAULT);
    }

    private static byte[] generatedPaddedString(byte[] input) {
        int targetLength = input.length;
        while (targetLength % 16 != 0) {
            targetLength++;
        }
        byte[] paddedArray = new byte[targetLength];
        System.arraycopy(input, 0, paddedArray, 0, input.length);
        for (int i = input.length; i < targetLength; i++) {
            paddedArray[i] = 0x3f;
        }
        return paddedArray;
    }


    public static byte[] generateHexPassphrase(String passphrase) {
        StringBuilder stringBuilder = new StringBuilder(passphrase);
        int passphraseLength = passphrase.length();
        if (passphraseLength < 16) {
            while (passphraseLength != 16) {
                stringBuilder.append("?");
                passphraseLength++;
            }
        } else if (passphraseLength < 24) {
            while (passphraseLength != 24) {
                stringBuilder.append("?");
                passphraseLength++;
            }
        } else if (passphraseLength < 32) {
            while (passphraseLength != 32) {
                stringBuilder.append("?");
                passphraseLength++;
            }
        }
        return stringBuilder.toString().getBytes();
    }

    public static String decryptString(String encryptedString, byte[] key, String initVector) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decode(encryptedString, Base64.DEFAULT));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
