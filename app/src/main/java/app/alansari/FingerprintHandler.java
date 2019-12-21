package app.alansari;

import app.alansari.listeners.OnFingerprintAuthentication;
import android.annotation.TargetApi;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.CancellationSignal;
import androidx.core.app.ActivityCompat;

/**
 * Created by Parveen Dala on 22 November, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {
    private boolean mSelfCancelled;
    private CancellationSignal cancellationSignal;
    private Context appContext;
    private OnFingerprintAuthentication fingerprintAuthentication;

    public FingerprintHandler(Context context, OnFingerprintAuthentication fingerprintAuthentication) {
        appContext = context;
        this.fingerprintAuthentication = fingerprintAuthentication;
    }

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(appContext, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    public void stopListening() {
        if (cancellationSignal != null) {
            mSelfCancelled = true;
            cancellationSignal.cancel();
            cancellationSignal = null;
        }
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        //Toast.makeText(appContext, "Authentication e\n" + errString, Toast.LENGTH_LONG).show();
        if (fingerprintAuthentication != null)
            fingerprintAuthentication.onAuthenticationError(errMsgId, errString);
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        //Toast.makeText(appContext, "Authentication help\n" + helpString, Toast.LENGTH_LONG).show();
        if (fingerprintAuthentication != null)
            fingerprintAuthentication.onAuthenticationHelp(helpMsgId, helpString);
    }

    @Override
    public void onAuthenticationFailed() {
        //Toast.makeText(appContext, "Authentication failed.", Toast.LENGTH_LONG).show();
        if (fingerprintAuthentication != null)
            fingerprintAuthentication.onAuthenticationFailed();
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        //Toast.makeText(appContext, "Authentication succeeded.", Toast.LENGTH_LONG).show();
        if (fingerprintAuthentication != null)
            fingerprintAuthentication.onAuthenticationSucceeded(result);
    }
}
