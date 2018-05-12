package de.thomaskoscheck.wgverwaltung;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

public class Cryptographics {
    static String encryptString(String rawString, String key, String initVector){
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(rawString.getBytes());
            //System.out.println("encrypted string: " + Base64.encodeBase64String(encrypted));


            //return StringUtils.newStringUsAscii(encodeBase64(binaryData, false));
            return StringUtils.newStringUsAscii(Base64.encodeBase64(encrypted));
            // return Base64.encodeToString(encrypted, Base64.NO_PADDING);
            // return Base64.encode(encrypted, Base64.NO_PADDING);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    static String decryptString(String encryptedString, String key, String initVector){
                try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(encryptedString));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
