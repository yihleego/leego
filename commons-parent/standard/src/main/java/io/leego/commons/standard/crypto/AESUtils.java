package io.leego.commons.standard.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author Leego Yih
 */
public final class AESUtils {
    private static final String AES_ALGORITHM = "AES";
    private static final String SHA1PRNG_ALGORITHM = "SHA1PRNG";
    private static final String STR_ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final int KEY_SIZE = 128;

    private AESUtils() {
    }

    public static byte[] encrypt(byte[] seed, byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        return doFinal(Cipher.ENCRYPT_MODE, seed, data, KEY_SIZE);
    }

    public static byte[] decrypt(byte[] seed, byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        return doFinal(Cipher.DECRYPT_MODE, seed, data, KEY_SIZE);
    }

    public static byte[] encrypt(byte[] seed, byte[] data, int keySize) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        return doFinal(Cipher.ENCRYPT_MODE, seed, data, keySize);
    }

    public static byte[] decrypt(byte[] seed, byte[] data, int keySize) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        return doFinal(Cipher.DECRYPT_MODE, seed, data, keySize);
    }

    private static byte[] doFinal(int mode, byte[] seed, byte[] data, int keySize) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        SecureRandom random = SecureRandom.getInstance(SHA1PRNG_ALGORITHM);
        random.setSeed(seed);
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_ALGORITHM);
        keyGenerator.init(keySize, random);
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] keyBytes = secretKey.getEncoded();
        Key key = new SecretKeySpec(keyBytes, AES_ALGORITHM);
        Cipher cipher = Cipher.getInstance(STR_ALGORITHM);
        cipher.init(mode, key);
        return cipher.doFinal(data);
    }

}
