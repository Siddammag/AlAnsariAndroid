package app.alansari.listeners;


import android.hardware.fingerprint.FingerprintManager;

/**
 * Created by Parveen Dala on 22 November, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public interface OnFingerprintAuthentication {

    void onAuthenticationError(int errMsgId, CharSequence errString);

    void onAuthenticationHelp(int helpMsgId, CharSequence helpString);

    void onAuthenticationFailed();

    void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result);

}
