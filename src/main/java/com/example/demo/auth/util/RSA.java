package com.example.demo.auth.util;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
public class RSA {
    private static final String ALGORITHM = "RSA";
    public static String RSA_ECB_PKCS1_PADDING = "RSA/ECB/PKCS1Padding";

    @Getter
    @Value("${RSA.public}")
    private String base64PublicKey;

    @Value("${RSA.private}")
    private String base64PrivateKey;

    private PrivateKey privateKey() {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(this.base64PrivateKey));
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to get private key!", e);
        }
    }

    public String decrypt(byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ECB_PKCS1_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, privateKey());
            return new String(cipher.doFinal(data));
        } catch (Exception e) {
            throw new IllegalArgumentException("Decrpyt failed!", e);
        }
    }

    public String decrypt(String cipherText) {
        return decrypt(Base64.decodeBase64(cipherText));
    }
}