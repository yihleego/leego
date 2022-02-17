package io.leego.commons.standard.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author Leego Yih
 */
public final class RSAUtils {
    private static final String RSA_ALGORITHM = "RSA";
    private static final String SHA1_WITH_RSA_ALGORITHMS = "SHA1WithRSA";
    private static final String SHA256_WITH_RSA_ALGORITHMS = "SHA256WithRSA";
    private static final int KEY_SIZE = 2048;
    private static final int BLOCK_SIZE = KEY_SIZE / 8;
    private static final int ENCRYPT_BLOCK_SIZE = KEY_SIZE / 8 - 11;
    private static final int DECRYPT_BLOCK_SIZE = KEY_SIZE / 8;

    private RSAUtils() {
    }

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        return generateKeyPair(KEY_SIZE);
    }

    public static KeyPair generateKeyPair(int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.generateKeyPair();
    }

    public static byte[] encryptByPrivateKey(PrivateKey privateKey, byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, ShortBufferException, InvalidKeyException {
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return doFinal(cipher, data, ENCRYPT_BLOCK_SIZE);
    }

    public static byte[] encryptByPrivateKey(byte[] privateKey, byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, ShortBufferException, InvalidKeyException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKey)));
        return doFinal(cipher, data, ENCRYPT_BLOCK_SIZE);
    }

    public static byte[] encryptByPublicKey(PublicKey publicKey, byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, ShortBufferException, InvalidKeyException {
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return doFinal(cipher, data, ENCRYPT_BLOCK_SIZE);
    }

    public static byte[] encryptByPublicKey(byte[] publicKey, byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, ShortBufferException, InvalidKeyException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, keyFactory.generatePublic(new X509EncodedKeySpec(publicKey)));
        return doFinal(cipher, data, ENCRYPT_BLOCK_SIZE);
    }

    public static byte[] decryptByPrivateKey(PrivateKey privateKey, byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, ShortBufferException, InvalidKeyException {
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return doFinal(cipher, data, DECRYPT_BLOCK_SIZE);
    }

    public static byte[] decryptByPrivateKey(byte[] privateKey, byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, ShortBufferException, InvalidKeyException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKey)));
        return doFinal(cipher, data, DECRYPT_BLOCK_SIZE);
    }

    public static byte[] decryptByPublicKey(PublicKey publicKey, byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, ShortBufferException, InvalidKeyException {
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return doFinal(cipher, data, DECRYPT_BLOCK_SIZE);
    }

    public static byte[] decryptByPublicKey(byte[] publicKey, byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, ShortBufferException, InvalidKeyException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, keyFactory.generatePublic(new X509EncodedKeySpec(publicKey)));
        return doFinal(cipher, data, DECRYPT_BLOCK_SIZE);
    }

    private static byte[] doFinal(Cipher cipher, byte[] input, int maxSize) throws IllegalBlockSizeException, BadPaddingException, ShortBufferException {
        int inputSize = input.length;
        if (inputSize <= maxSize) {
            return cipher.doFinal(input);
        }
        boolean aligned = inputSize % maxSize == 0;
        int quotient = inputSize / maxSize;
        byte[] output = new byte[(aligned ? quotient : quotient + 1) * BLOCK_SIZE];
        int i;
        for (i = 0; i < quotient; i++) {
            cipher.doFinal(input, i * maxSize, maxSize, output, i * BLOCK_SIZE);
        }
        if (!aligned) {
            cipher.doFinal(input, i * maxSize, inputSize - i * maxSize, output, i * BLOCK_SIZE);
        }
        return output;
    }


    public static byte[] signSHA1(PrivateKey privateKey, byte[] data) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        return sign(SHA1_WITH_RSA_ALGORITHMS, privateKey, data);
    }

    public static byte[] signSHA1(byte[] privateKey, byte[] data) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, InvalidKeySpecException {
        return sign(SHA1_WITH_RSA_ALGORITHMS, privateKey, data);
    }

    public static byte[] signSHA256(PrivateKey privateKey, byte[] data) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        return sign(SHA256_WITH_RSA_ALGORITHMS, privateKey, data);
    }

    public static byte[] signSHA256(byte[] privateKey, byte[] data) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, InvalidKeySpecException {
        return sign(SHA256_WITH_RSA_ALGORITHMS, privateKey, data);
    }

    private static byte[] sign(String algorithm, PrivateKey privateKey, byte[] data) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Signature sig = Signature.getInstance(algorithm);
        sig.initSign(privateKey);
        sig.update(data);
        return sig.sign();
    }

    private static byte[] sign(String algorithm, byte[] privateKey, byte[] data) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        return sign(algorithm, keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKey)), data);
    }


    public static boolean verifySHA1(PublicKey publicKey, byte[] data, byte[] signature) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        return verify(SHA1_WITH_RSA_ALGORITHMS, publicKey, data, signature);
    }

    public static boolean verifySHA1(byte[] publicKey, byte[] data, byte[] signature) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, InvalidKeySpecException {
        return verify(SHA1_WITH_RSA_ALGORITHMS, publicKey, data, signature);
    }

    public static boolean verifySHA256(PublicKey publicKey, byte[] data, byte[] signature) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        return verify(SHA256_WITH_RSA_ALGORITHMS, publicKey, data, signature);
    }

    public static boolean verifySHA256(byte[] publicKey, byte[] data, byte[] signature) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, InvalidKeySpecException {
        return verify(SHA256_WITH_RSA_ALGORITHMS, publicKey, data, signature);
    }

    private static boolean verify(String algorithm, PublicKey publicKey, byte[] data, byte[] signature) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Signature sig = Signature.getInstance(algorithm);
        sig.initVerify(publicKey);
        sig.update(data);
        return sig.verify(signature);
    }

    private static boolean verify(String algorithm, byte[] publicKey, byte[] data, byte[] signature) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        return verify(algorithm, keyFactory.generatePublic(new X509EncodedKeySpec(publicKey)), data, signature);
    }

}
