package com.microsoft.band.service.crypto;

import android.annotation.SuppressLint;
import android.content.Context;
import com.microsoft.band.internal.util.KDKLog;
import com.microsoft.band.service.CargoServiceException;
import com.microsoft.band.service.util.FileHelper;
import java.io.File;
import java.math.BigInteger;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
/* loaded from: classes.dex */
public class CryptoProviderImpl implements CryptoProvider {
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String CREDENTIAL_KEY_FILE = "credentials.key";
    private static final int CRYPTO_V1 = 0;
    private static final int IV_LENGTH = 16;
    private static final int OUTPUT_KEY_LENGTH = 256;
    private static final String PASSPHRASE = "12uhd8349129d93489udu934";
    private static final String PBE_ALGORITHM = "PBKDF2WithHmacSHA1";
    public static final int PBE_ITERATION_COUNT = 1000;
    private static final String PROVIDER = "BC";
    private static final String RANDOM_ALGORITHM = "SHA1PRNG";
    private File mDirectory;
    private static final String TAG = CryptoProvider.class.getSimpleName();
    private static final Object CREDENTIALS_KEY_LOCK = new Object();

    public CryptoProviderImpl(Context context) {
        this.mDirectory = FileHelper.directoryAtPath(context.getFilesDir().getAbsolutePath());
    }

    public CryptoProviderImpl(File directory) {
        this.mDirectory = directory;
    }

    @SuppressLint({"TrulyRandom"})
    private byte[] getSalt(boolean isEncrypt) throws NoSuchAlgorithmException, CryptoException {
        byte[] key = getGeneratedCryptoKey();
        if (key == null || key.length == 0) {
            if (isEncrypt) {
                SecureRandom secureRandom = new SecureRandom();
                KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
                keyGenerator.init(256, secureRandom);
                SecretKey generatedKey = keyGenerator.generateKey();
                setGeneratedCryptoKey(generatedKey.getEncoded());
                return generatedKey.getEncoded();
            }
            throw new CryptoException("Stored decryption key not found");
        }
        return key;
    }

    private SecretKey generateKey(byte[] salt, char[] passphrase) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(PBE_ALGORITHM);
        KeySpec keySpec = new PBEKeySpec(passphrase, salt, 1000, 256);
        SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
        return secretKey;
    }

    @SuppressLint({"TrulyRandom"})
    private byte[] generateIv() throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstance(RANDOM_ALGORITHM);
        byte[] iv = new byte[16];
        random.nextBytes(iv);
        return iv;
    }

    private String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        return paddingLength > 0 ? String.format("%0" + paddingLength + "d", 0) + hex : hex;
    }

    private byte[] fromHex(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(i * 2, (i * 2) + 2), 16);
        }
        return bytes;
    }

    @Override // com.microsoft.band.service.crypto.CryptoProvider
    public String encrypt(String clearText, int cryptoVersion) throws CryptoException {
        if (cryptoVersion != 0) {
            throw new CryptoException("Unable to encrypt for cryptoVersion = " + cryptoVersion);
        }
        try {
            byte[] salt = getSalt(true);
            SecretKey secret = generateKey(salt, PASSPHRASE.toCharArray());
            Key encryptionKey = new SecretKeySpec(secret.getEncoded(), "AES");
            byte[] iv = generateIv();
            String ivHex = toHex(iv);
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            Cipher encryptionCipher = Cipher.getInstance(CIPHER_ALGORITHM, PROVIDER);
            encryptionCipher.init(1, encryptionKey, ivspec);
            byte[] encryptedText = encryptionCipher.doFinal(clearText.getBytes("UTF-8"));
            String encryptedHex = toHex(encryptedText);
            return ivHex + encryptedHex;
        } catch (Exception e) {
            throw new CryptoException("Unable to encrypt", e);
        }
    }

    @Override // com.microsoft.band.service.crypto.CryptoProvider
    public String decrypt(String encryptedText, int cryptoVersion) throws CryptoException {
        if (cryptoVersion != 0) {
            throw new CryptoException("Unable to decrypt for cryptoVersion = " + cryptoVersion);
        }
        try {
            byte[] salt = getSalt(false);
            SecretKey secret = generateKey(salt, PASSPHRASE.toCharArray());
            Key encryptionKey = new SecretKeySpec(secret.getEncoded(), "AES");
            Cipher decryptionCipher = Cipher.getInstance(CIPHER_ALGORITHM, PROVIDER);
            String ivHex = encryptedText.substring(0, 32);
            String encryptedHex = encryptedText.substring(32);
            IvParameterSpec ivspec = new IvParameterSpec(fromHex(ivHex));
            decryptionCipher.init(2, encryptionKey, ivspec);
            byte[] decryptedText = decryptionCipher.doFinal(fromHex(encryptedHex));
            String decrypted = new String(decryptedText, "UTF-8");
            return decrypted;
        } catch (Exception e) {
            throw new CryptoException("Unable to decrypt", e);
        }
    }

    private byte[] getGeneratedCryptoKey() {
        byte[] readDataFromFile;
        synchronized (CREDENTIALS_KEY_LOCK) {
            File file = new File(this.mDirectory + File.separator + CREDENTIAL_KEY_FILE);
            try {
                readDataFromFile = FileHelper.readDataFromFile(file);
            } catch (CargoServiceException e) {
                KDKLog.e(TAG, "unable to get key", e);
                return null;
            }
        }
        return readDataFromFile;
    }

    private void setGeneratedCryptoKey(byte[] key) throws CryptoException {
        synchronized (CREDENTIALS_KEY_LOCK) {
            File file = new File(this.mDirectory + File.separator + CREDENTIAL_KEY_FILE);
            try {
                FileHelper.writeDataToFile(key, file);
            } catch (CargoServiceException e) {
                KDKLog.e(TAG, "unable to set key", e);
                throw new CryptoException("Unable to set key: " + e.getMessage());
            }
        }
    }
}
