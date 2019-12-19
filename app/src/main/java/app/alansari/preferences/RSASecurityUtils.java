package app.alansari.preferences;


import android.content.Context;
import android.content.res.AssetManager;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.crypto.Cipher;

import app.alansari.Utils.LogUtils;

import static org.kobjects.base64.Base64.encode;

/**
 * Created by Parveen Dala on 22 August, 2017
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */

public class RSASecurityUtils {
    //static Configuration config = Configuration.getInstance();

    static String KEY_STORE_TYPE = "PKCS12";
    //static String KEY_STORE_NAME = "alansari_key.pfx";
    static String KEY_STORE_NAME = "aae_rsakey.pfx";
    static String KEY_STORE_PASS = "abcd1234";
    static String KEY_ALIAS = "91181a7d-6a49-11e3-bcea-089e0138d0f8";
    static String PRIVATE_KEY_PASS = "abcd1234";
    static PrivateKey privateKey = null;
    static PublicKey publicKey = null;

    /**
     * Encrypt Data
     *
     * @param data
     * @throws IOException
     */
    public static String encryptData(String data) throws IOException {
        LogUtils.printString("\n----------------ENCRYPTION STARTED------------");
        LogUtils.d("ok", "Data Before Encryption :" + data);
        LogUtils.printString("Data Before Encryption :" + data);
        byte[] dataToEncrypt = data.getBytes("UTF-8");
        byte[] encryptedData = null;
        String base64Str = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            encryptedData = cipher.doFinal(dataToEncrypt);

            base64Str = encode(encryptedData);
            LogUtils.printString("Encryted Data: " + base64Str);

        } catch (Exception e) {
            e.printStackTrace();
        }

        LogUtils.printString("----------------ENCRYPTION COMPLETED------------");
        return base64Str;
    }

    /**
     * Encrypt Data
     *
     * @throws IOException
     */
    public static String decryptData(String encrypted) throws IOException {
        LogUtils.printString("\n----------------DECRYPTION STARTED------------");
        LogUtils.d("ok", "Data Before Decryption :" + encrypted);
        byte[] descryptedData = null;
        String decrypted = null;
        try {
            byte[] data = org.kobjects.base64.Base64.decode(encrypted);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            descryptedData = cipher.doFinal(data);
            decrypted = new String(descryptedData, "UTF-8");
            LogUtils.printString("Decrypted Data: " + decrypted);

        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtils.printString("----------------DECRYPTION COMPLETED------------");
        return decrypted;
    }

    public void setSecurityKeys(Context context) {
        try {
            KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE);
            AssetManager assManager = context.getAssets();
            InputStream is = null;
            try {
                is = assManager.open(KEY_STORE_NAME);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            keyStore.load(is, KEY_STORE_PASS.toCharArray());
            privateKey = (PrivateKey) keyStore.getKey(KEY_ALIAS, PRIVATE_KEY_PASS.toCharArray());
            X509Certificate cert = (X509Certificate) keyStore.getCertificate(KEY_ALIAS);
            publicKey = cert.getPublicKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (CertificateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}