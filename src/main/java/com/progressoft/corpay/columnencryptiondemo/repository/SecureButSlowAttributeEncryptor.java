package com.progressoft.corpay.columnencryptiondemo.repository;

import jakarta.persistence.AttributeConverter;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class SecureButSlowAttributeEncryptor implements AttributeConverter<String, String> {

    private static final String AES = "AES";
    private static final String PASSWORD = "super-duper-password";
    public static final int SALT_LENGTH = 24;
    public static final int BASE64_SALT_LENGTH = 32;

    private final Cipher cipher;

    public SecureButSlowAttributeEncryptor() throws Exception {
        cipher = Cipher.getInstance(AES);
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        try {
            byte[] salt = new byte[SALT_LENGTH];
            new SecureRandom().nextBytes(salt);
            String saltInBase64 = Base64.getEncoder().encodeToString(salt);

            cipher.init(Cipher.ENCRYPT_MODE, getKeyFromPassword(PASSWORD, salt));
            String encryptedText = Base64.getEncoder().encodeToString(cipher.doFinal(attribute.getBytes()));

            return saltInBase64.concat(encryptedText);
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchAlgorithmException |
                 InvalidKeySpecException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            String saltInBase64 = dbData.substring(0, BASE64_SALT_LENGTH);
            byte[] salt = Base64.getDecoder().decode(saltInBase64);
            String encryptedData = dbData.substring(BASE64_SALT_LENGTH);

            cipher.init(Cipher.DECRYPT_MODE, getKeyFromPassword(PASSWORD, salt));
            return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedData)));
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException |
                 InvalidKeySpecException e) {
            throw new IllegalStateException(e);
        }
    }

    public static SecretKey getKeyFromPassword(String password, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
        return new SecretKeySpec(factory.generateSecret(spec)
                .getEncoded(), "AES");
    }
}
