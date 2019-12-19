package app.alansari.preferences;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import app.alansari.Utils.LogUtils;

/**
 * Created by Parveen Dala on 14 August, 2017
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class SecureMe implements SharedPreferences {


    private static final String TAG = SecureMe.class.getName();
    public static String APP_PREF = "alansariPref";
    private static boolean sLoggingEnabled = false;
    //the backing pref file
    private SharedPreferences sharedPreferences;
    //secret keys used for enc and dec
    private AesCbcWithIntegrity.SecretKeys keys;
    //name of the currently loaded sharedPrefFile, can be null if default

    /**
     * @param context  should be ApplicationContext not Activity
     * @param password user password/code used to generate encryption key.
     */
    public SecureMe(Context context, final String password) {
        this(context, null, password);
    }

    private SecureMe(Context context, final AesCbcWithIntegrity.SecretKeys secretKey, final String password) {
        if (sharedPreferences == null) {
            sharedPreferences = getSharedPreferenceFile(context);
        }

        if (secretKey != null) {
            keys = secretKey;
        } else if (TextUtils.isEmpty(password)) {
            // Initialize or create encryption key
            try {
                final String key = generateAesKeyName(context);
                String keyAsString = sharedPreferences.getString(key, null);
                if (keyAsString == null) {
                    keys = AesCbcWithIntegrity.generateKey();
                    boolean committed = sharedPreferences.edit().putString(key, RSASecurityUtils.encryptData(keys.toString())).commit();
                    if (!committed) {
                        LogUtils.w(TAG, "Key not committed to prefs");
                    }
                } else {
                    keys = AesCbcWithIntegrity.keys(RSASecurityUtils.decryptData(keyAsString));
                }

                if (keys == null) {
                    throw new GeneralSecurityException("Problem generating Key");
                }

            } catch (GeneralSecurityException e) {
                if (sLoggingEnabled) {
                    LogUtils.e(TAG, "Error init:" + e.getMessage());
                }
                sharedPreferences.edit().clear().commit();
                throw new IllegalStateException(e);
            } catch (IOException e) {
                if (sLoggingEnabled) {
                    LogUtils.e(TAG, "Error init:" + e.getMessage());
                }
                throw new IllegalStateException(e);
            }
        } else {
            //use the password to generate the key
            try {
                final byte[] salt = getDeviceSerialNumber(context).getBytes();
                keys = AesCbcWithIntegrity.generateKeyFromPassword(password, salt);
                if (keys == null) {
                    throw new GeneralSecurityException("Problem generating Key From Password");
                }
            } catch (GeneralSecurityException e) {
                if (sLoggingEnabled) {
                    LogUtils.e(TAG, "Error init using user password:" + e.getMessage());
                }
                throw new IllegalStateException(e);
            }
        }
    }

    /**
     * Gets the hardware serial number of this device.
     *
     * @return serial number or Settings.Secure.ANDROID_ID if not available.
     */
    @SuppressLint("HardwareIds")
    private static String getDeviceSerialNumber(Context context) {
        // We're using the Reflection API because Build.SERIAL is only available
        // since API Level 9 (Gingerbread, Android 2.3).
        try {
            String deviceSerial = (String) Build.class.getField("SERIAL").get(
                    null);
            if (TextUtils.isEmpty(deviceSerial)) {
                return Settings.Secure.getString(
                        context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            } else {
                return deviceSerial;
            }
        } catch (Exception ignored) {
            // Fall back  to Android_ID
            return Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }
    }

    /**
     * The Pref keys must be same each time so we're using a hash to obscure the stored value
     *
     * @param prefKey
     * @return SHA-256 Hash of the preference key
     */
    public static String hashPrefKey(String prefKey) {
        final MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = prefKey.getBytes("UTF-8");
            digest.update(bytes, 0, bytes.length);

            return Base64.encodeToString(digest.digest(), AesCbcWithIntegrity.BASE64_FLAGS);

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            if (sLoggingEnabled) {
                LogUtils.w(TAG, "Problem generating hash", e);
            }
        }
        return null;
    }

    public static boolean isLoggingEnabled() {
        return sLoggingEnabled;
    }

    public static void setLoggingEnabled(boolean loggingEnabled) {
        sLoggingEnabled = loggingEnabled;
    }

    /**
     * if a prefFilename is not defined the getDefaultSharedPreferences is used.
     *
     * @param context should be ApplicationContext not Activity
     * @return
     */
    private SharedPreferences getSharedPreferenceFile(Context context) {
        return context.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
//        this.sharedPrefFilename = prefFilename;
//        if (TextUtils.isEmpty(prefFilename)) {
//            return PreferenceManager.getDefaultSharedPreferences(context);
//        } else {
//            return context.getSharedPreferences(prefFilename, Context.MODE_PRIVATE);
//        }
    }

    /**
     * nulls in memory keys
     */
    public void destroyKeys() {
        keys = null;
    }

    /**
     * Uses device and application values to generate the pref key for the encryption key
     *
     * @param context should be ApplicationContext not Activity
     * @return String to be used as the AESkey Pref key
     * @throws GeneralSecurityException if something goes wrong in generation
     */
    private String generateAesKeyName(Context context) throws GeneralSecurityException {
        final String password = context.getPackageName();
        final byte[] salt = getDeviceSerialNumber(context).getBytes();
        //AesCbcWithIntegrity.SecretKeys generatedKeyName = AesCbcWithIntegrity.generateKeyFromPassword(password, salt, iterationCount);
        AesCbcWithIntegrity.SecretKeys generatedKeyName = AesCbcWithIntegrity.generateKeyFromPassword(password, salt);
        return hashPrefKey(generatedKeyName.toString());
    }

    private String encrypt(String cleartext) {
        if (TextUtils.isEmpty(cleartext)) {
            return cleartext;
        }
        try {
            LogUtils.d("ok", "EncryptPrefs :" + cleartext);
            //return RSASecurityUtils.encryptData(cleartext);
            return AesCbcWithIntegrity.encrypt(cleartext, keys).toString();
        } catch (GeneralSecurityException | UnsupportedEncodingException e) {
            if (sLoggingEnabled) {
                LogUtils.w(TAG, "encrypt", e);
            }
        } catch (Exception e) {
            if (sLoggingEnabled) {
                LogUtils.w(TAG, "encrypt", e);
            }
        }
        return null;
    }

    /**
     * @param ciphertext
     * @return decrypted plain text, unless decryption fails, in which case null
     */
    private String decrypt(final String ciphertext) {
        if (TextUtils.isEmpty(ciphertext)) {
            return ciphertext;
        }
        try {
            LogUtils.d("ok", "EncryptPrefs :" + ciphertext);
            AesCbcWithIntegrity.CipherTextIvMac cipherTextIvMac = new AesCbcWithIntegrity.CipherTextIvMac(ciphertext);
            return AesCbcWithIntegrity.decryptString(cipherTextIvMac, keys);
        } catch (GeneralSecurityException | UnsupportedEncodingException e) {
            if (sLoggingEnabled) {
                LogUtils.w(TAG, "decrypt", e);
            }
        } catch (Exception e) {
            if (sLoggingEnabled) {
                LogUtils.w(TAG, "encrypt", e);
            }
        }
        return null;
    }

    /**
     * @return map ofd valu with decryptees (excluding the key if present)
     */
    @Override
    public Map<String, String> getAll() {
        //wont be null as per http://androidxref.com/5.1.0_r1/xref/frameworks/base/core/java/android/app/SharedPreferencesImpl.java
        final Map<String, ?> encryptedMap = sharedPreferences.getAll();
        final Map<String, String> decryptedMap = new HashMap<String, String>(
                encryptedMap.size());
        for (Entry<String, ?> entry : encryptedMap.entrySet()) {
            try {
                Object cipherText = entry.getValue();
                //don't include the key
                if (cipherText != null && !cipherText.equals(keys.toString())) {
                    //the prefs should all be strings
                    decryptedMap.put(entry.getKey(),
                            decrypt(cipherText.toString()));
                }
            } catch (Exception e) {
                if (sLoggingEnabled) {
                    LogUtils.w(TAG, "e during getAll", e);
                }
                // Ignore issues that unencrypted values and use instead raw cipher text string
                decryptedMap.put(entry.getKey(),
                        entry.getValue().toString());
            }
        }
        return decryptedMap;
    }

    @Override
    public String getString(String key, String defaultValue) {
        final String encryptedValue = sharedPreferences.getString(SecureMe.hashPrefKey(key), null);
        return (encryptedValue != null) ? decrypt(encryptedValue) : defaultValue;
    }

    /**
     * Added to get a values as as it can be useful to store values that are
     * already encrypted and encoded
     *
     * @param key          pref key
     * @param defaultValue
     * @return Encrypted value of the key or the defaultValue if no value exists
     */
    public String getEncryptedString(String key, String defaultValue) {
        final String encryptedValue = sharedPreferences.getString(
                SecureMe.hashPrefKey(key), null);
        return (encryptedValue != null) ? encryptedValue : defaultValue;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Set<String> getStringSet(String key, Set<String> defaultValues) {
        final Set<String> encryptedSet = sharedPreferences.getStringSet(
                SecureMe.hashPrefKey(key), null);
        if (encryptedSet == null) {
            return defaultValues;
        }
        final Set<String> decryptedSet = new HashSet<String>(
                encryptedSet.size());
        for (String encryptedValue : encryptedSet) {
            decryptedSet.add(decrypt(encryptedValue));
        }
        return decryptedSet;
    }

    @Override
    public int getInt(String key, int defaultValue) {
        final String encryptedValue = sharedPreferences.getString(SecureMe.hashPrefKey(key), null);
        if (encryptedValue == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(decrypt(encryptedValue));
        } catch (NumberFormatException e) {
            throw new ClassCastException(e.getMessage());
        }
    }

    @Override
    public long getLong(String key, long defaultValue) {
        final String encryptedValue = sharedPreferences.getString(SecureMe.hashPrefKey(key), null);
        if (encryptedValue == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(decrypt(encryptedValue));
        } catch (NumberFormatException e) {
            throw new ClassCastException(e.getMessage());
        }
    }

    @Override
    public float getFloat(String key, float defaultValue) {
        final String encryptedValue = sharedPreferences.getString(SecureMe.hashPrefKey(key), null);
        if (encryptedValue == null) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(decrypt(encryptedValue));
        } catch (NumberFormatException e) {
            throw new ClassCastException(e.getMessage());
        }
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        final String encryptedValue = sharedPreferences.getString(SecureMe.hashPrefKey(key), null);
        if (encryptedValue == null) {
            return defaultValue;
        }
        try {
            return Boolean.parseBoolean(decrypt(encryptedValue));
        } catch (NumberFormatException e) {
            throw new ClassCastException(e.getMessage());
        }
    }

    @Override
    public boolean contains(String key) {
        return sharedPreferences.contains(SecureMe.hashPrefKey(key));
    }

    /**
     * Cycle through the unencrypt all the current prefs to mem cache, clear, then encypt with key generated from new password.
     * This method can be used if switching from the generated key to a key derived from user password
     * <p>
     * Note: the pref keys will remain the same as they are SHA256 hashes.
     *
     * @param newPassword
     * @param context        should be ApplicationContext not Activity
     * @param iterationCount The iteration count for the keys generation
     */
    @SuppressLint("CommitPrefEdits")
    public void handlePasswordChange(String newPassword, Context context, int iterationCount) throws GeneralSecurityException {

        final byte[] salt = getDeviceSerialNumber(context).getBytes();
        AesCbcWithIntegrity.SecretKeys newKey = AesCbcWithIntegrity.generateKeyFromPassword(newPassword, salt);

        Map<String, ?> allOfThePrefs = sharedPreferences.getAll();
        Map<String, String> unencryptedPrefs = new HashMap<String, String>(allOfThePrefs.size());
        //iterate through the current prefs unencrypting each one
        for (String prefKey : allOfThePrefs.keySet()) {
            Object prefValue = allOfThePrefs.get(prefKey);
            if (prefValue instanceof String) {
                //all the encrypted values will be Strings
                final String prefValueString = (String) prefValue;
                final String plainTextPrefValue = decrypt(prefValueString);
                unencryptedPrefs.put(prefKey, plainTextPrefValue);
            }
        }

        //destroy and clear the current pref file
        destroyKeys();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

        //refresh the sharedPreferences object ref: I found it was retaining old ref/values
        sharedPreferences = null;
        sharedPreferences = getSharedPreferenceFile(context);

        //assign new key
        this.keys = newKey;

        SharedPreferences.Editor updatedEditor = sharedPreferences.edit();

        //iterate through the unencryptedPrefs encrypting each one with new key
        Iterator<String> unencryptedPrefsKeys = unencryptedPrefs.keySet().iterator();
        while (unencryptedPrefsKeys.hasNext()) {
            String prefKey = unencryptedPrefsKeys.next();
            String prefPlainText = unencryptedPrefs.get(prefKey);
            updatedEditor.putString(prefKey, encrypt(prefPlainText));
        }
        updatedEditor.commit();
    }

    public void handlePasswordChange(String newPassword, Context context) throws GeneralSecurityException {
        handlePasswordChange(newPassword, context);
    }

    @Override
    public Editor edit() {
        return new Editor();
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(
            final OnSharedPreferenceChangeListener listener) {
        sharedPreferences
                .registerOnSharedPreferenceChangeListener(listener);
    }

    /**
     * @param listener    OnSharedPreferenceChangeListener
     * @param decryptKeys Callbacks receive the "key" parameter decrypted
     */
    public void registerOnSharedPreferenceChangeListener(
            final OnSharedPreferenceChangeListener listener, boolean decryptKeys) {

        if (!decryptKeys) {
            registerOnSharedPreferenceChangeListener(listener);
        }
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(
            OnSharedPreferenceChangeListener listener) {
        sharedPreferences
                .unregisterOnSharedPreferenceChangeListener(listener);
    }

    /**
     * Wrapper for Android's {@link android.content.SharedPreferences.Editor}.
     * <p>
     * Used for modifying values in a {@link SecureMe} object. All
     * changes you make in an editor are batched, and not copied back to the
     * original {@link SecureMe} until you call {@link #commit()} or
     * {@link #apply()}.
     */
    public final class Editor implements SharedPreferences.Editor {
        private SharedPreferences.Editor mEditor;

        /**
         * Constructor.
         */
        private Editor() {
            mEditor = sharedPreferences.edit();
        }

        @Override
        public SharedPreferences.Editor putString(String key, String value) {
            mEditor.putString(SecureMe.hashPrefKey(key), encrypt(value));
            return this;
        }

        /**
         * This is useful for storing values that have be encrypted by something
         * else or for testing
         *
         * @param key   - encrypted as usual
         * @param value will not be encrypted
         * @return
         */
        public SharedPreferences.Editor putUnencryptedString(String key, String value) {
            mEditor.putString(SecureMe.hashPrefKey(key), value);
            return this;
        }

        @Override
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        public SharedPreferences.Editor putStringSet(String key, Set<String> values) {
            final Set<String> encryptedValues = new HashSet<String>(values.size());
            for (String value : values) {
                encryptedValues.add(encrypt(value));
            }
            mEditor.putStringSet(SecureMe.hashPrefKey(key), encryptedValues);
            return this;
        }

        @Override
        public SharedPreferences.Editor putInt(String key, int value) {
            mEditor.putString(SecureMe.hashPrefKey(key),
                    encrypt(Integer.toString(value)));
            return this;
        }

        @Override
        public SharedPreferences.Editor putLong(String key, long value) {
            mEditor.putString(SecureMe.hashPrefKey(key),
                    encrypt(Long.toString(value)));
            return this;
        }

        @Override
        public SharedPreferences.Editor putFloat(String key, float value) {
            mEditor.putString(SecureMe.hashPrefKey(key),
                    encrypt(Float.toString(value)));
            return this;
        }

        @Override
        public SharedPreferences.Editor putBoolean(String key, boolean value) {
            mEditor.putString(SecureMe.hashPrefKey(key), encrypt(Boolean.toString(value)));
            return this;
        }

        @Override
        public SharedPreferences.Editor remove(String key) {
            mEditor.remove(SecureMe.hashPrefKey(key));
            return this;
        }

        @Override
        public SharedPreferences.Editor clear() {
            mEditor.clear();
            return this;
        }

        @Override
        public boolean commit() {
            return mEditor.commit();
        }

        @Override
        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        public void apply() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                mEditor.apply();
            } else {
                commit();
            }
        }
    }
}
