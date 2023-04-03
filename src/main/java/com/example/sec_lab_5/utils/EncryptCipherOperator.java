package com.example.sec_lab_5.utils;

import lombok.SneakyThrows;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.function.UnaryOperator;

public class EncryptCipherOperator implements UnaryOperator<String> {

    private Cipher cipher;

    private SecretKeySpec key;

    @SneakyThrows
    public EncryptCipherOperator(String password) {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.reset();
        md.update(new String(password).getBytes());

        key = new SecretKeySpec(md.digest(), "AES");
        cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
    }

    @SneakyThrows
    @Override
    public String apply(String s) {
        byte[] cipherText = cipher.doFinal(s.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(cipherText);
    }
}
