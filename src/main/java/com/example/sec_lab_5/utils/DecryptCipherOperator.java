package com.example.sec_lab_5.utils;

import lombok.SneakyThrows;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.function.UnaryOperator;

public class DecryptCipherOperator implements UnaryOperator<String> {

    private Cipher cipher;

    private SecretKeySpec key;

    @SneakyThrows
    public DecryptCipherOperator(String password) {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.reset();
        md.update(new String(password).getBytes());

        key = new SecretKeySpec(md.digest(), "AES");
        cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
    }

    @SneakyThrows
    @Override
    public String apply(String s) {
        try {
            byte[] cipherText = cipher.doFinal(Base64.getDecoder().decode(s));
            return new String(cipherText);
        }catch (Exception e)
        {
            return "Ошибка дешифрования! Возможно введен неверный пароль.";
        }
    }
}
