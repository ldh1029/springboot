package com.xhcoding.helper;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

/**
 * 对称加密
 * Created by Administrator on 2017/6/20 0020.
 */
public class SymmetricalCoderHelper {

    public static final String ALGORITHM_DES = "DES";
    public static final String ALGORITHM_DESede = "DESede";
    public static final String ALGORITHM_AES = "AES";
    public static final String ALGORITHM_Blowfish = "Blowfish";
    public static final String ALGORITHM_RC2 = "RC2";
    public static final String ALGORITHM_RC4 = "RC4";

    private Key toKey(String algorithm, byte[] key) throws InvalidKeyException,
            InvalidKeySpecException, NoSuchAlgorithmException
    {
        if (ALGORITHM_DES.equals(algorithm))
        {
            DESKeySpec dks = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
            SecretKey secretKey = keyFactory.generateSecret(dks);
            return secretKey;
        }

        return new SecretKeySpec(key, algorithm);
    }

    /**
     * 解密
     *
     * @param algorithm
     * @param data
     * @param key
     * @return
     * @throws InvalidKeyException
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public byte[] decrypt(String algorithm, byte[] data, byte[] key)
            throws InvalidKeyException, InvalidKeySpecException,
            NoSuchAlgorithmException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException
    {
        Key k = toKey(algorithm, key);

        Cipher cipher = Cipher.getInstance(algorithm.toString());
        cipher.init(2, k);

        return cipher.doFinal(data);
    }

    /**
     * 加密
     *
     * @param algorithm
     * @param data
     * @param key
     * @return
     * @throws InvalidKeyException
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws GeneralSecurityException
     */
    public byte[] encrypt(String algorithm, byte[] data, byte[] key)
            throws InvalidKeyException, InvalidKeySpecException,
            NoSuchAlgorithmException, NoSuchPaddingException,
            IllegalBlockSizeException, GeneralSecurityException
    {
        Key k = toKey(algorithm, key);
        Cipher cipher = Cipher.getInstance(algorithm.toString());
        cipher.init(1, k);

        return cipher.doFinal(data);
    }

    /**
     * 初始化密钥
     *
     * @param algorithm
     * @return
     * @throws NoSuchAlgorithmException
     */
    public byte[] initKey(String algorithm) throws NoSuchAlgorithmException
    {
        return initKey(algorithm, null);
    }

    /**
     * 初始化密钥
     *
     * @param algorithm
     * @param seed
     * @return
     * @throws NoSuchAlgorithmException
     */
    public byte[] initKey(String algorithm, byte[] seed)
            throws NoSuchAlgorithmException
    {
        SecureRandom secureRandom = null;

        if (seed != null)
        {
            secureRandom = new SecureRandom(seed);
        }
        else
        {
            secureRandom = new SecureRandom();
        }

        KeyGenerator kg = KeyGenerator.getInstance(algorithm.toString());
        kg.init(secureRandom);

        return kg.generateKey().getEncoded();
    }

    public static void main(String[] args) throws Exception
    {
        String algorithm = ALGORITHM_RC4;
        byte[] data = "abcd".getBytes();
        SymmetricalCoderHelper coder1 = new SymmetricalCoderHelper();
        byte[] key = coder1.initKey(algorithm);
        byte[] result = coder1.encrypt(algorithm, data, key);

        SymmetricalCoderHelper coder2 = new SymmetricalCoderHelper();
        byte[] key1 = coder1.initKey(algorithm);

        System.out.println(new String(coder2.decrypt(algorithm, result, key1)));
    }
}
