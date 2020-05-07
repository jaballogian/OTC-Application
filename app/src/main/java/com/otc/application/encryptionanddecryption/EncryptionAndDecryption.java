package com.otc.application.encryptionanddecryption;

public class EncryptionAndDecryption {

    public static String encrypt(String inputText){
        String outputEncryptedText = "";
        try {
            outputEncryptedText = AESUtils.encrypt(inputText);
            return outputEncryptedText;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputEncryptedText;
    }

    public static String decrypt(String inputEncryptedText){
        String outputDecryptedText = "";
        try {
            outputDecryptedText = AESUtils.decrypt(inputEncryptedText);
            return outputDecryptedText;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputDecryptedText;
    }
}
