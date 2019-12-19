package app.alansari.network;

import android.os.AsyncTask;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.HashMap;
import java.util.Map;

import app.alansari.Utils.CommonUtils.SERVICE_TYPE;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogUtils;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.preferences.AesCbcWithIntegrity;
import app.alansari.preferences.RSASecurityUtils;

/**
 * Created by Parveen Dala on 12 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */

public class CallAddr {
    JsonObjectRequest jsonObjectRequest;
    String TAG = "CallAddr";

    public static void callWSThreadSoapPrimitive(final String apiUrl, final String strSoapAction, final SoapObject request, final SERVICE_TYPE serviceType, final OnWebServiceResult listener) {

        new AsyncTask<String, Void, SoapObject>() {
            @Override
            protected SoapObject doInBackground(String... params) {

                try {
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);
                    HttpTransportSE ht = new HttpTransportSE(apiUrl);
                    ht.debug = true;
                    ht.call(strSoapAction, envelope);
                    SoapObject response = (SoapObject) envelope.getResponse();
                    return response;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(SoapObject response) {
                super.onPostExecute(response);
                try {
                    if (response != null) {
                        if (response.getPrimitivePropertyAsString("responseCode").equalsIgnoreCase("1000")) {
                            listener.onResponse(1000, new JSONObject("{\"" + Constants.STATUS_MSG + "\":\"" + response.getPrimitivePropertyAsString("mobileNumber") + "\"}"), serviceType);
                        } else if (response.getPrimitivePropertyAsString("responseCode").equalsIgnoreCase("1001")) {
                            listener.onResponse(1001, new JSONObject("{\"" + Constants.STATUS_MSG + "\":\"EID expired\"}"), serviceType);
                        } else if (response.getPrimitivePropertyAsString("responseCode").equalsIgnoreCase("1002")) {
                            listener.onResponse(1002, new JSONObject("{\"" + Constants.STATUS_MSG + "\":\"User account locked\"}"), serviceType);
                        } else if (response.getPrimitivePropertyAsString("responseCode").equalsIgnoreCase("1003")) {
                            listener.onResponse(1003, new JSONObject("{\"" + Constants.STATUS_MSG + "\":\"Invalid user credentials\"}"), serviceType);
                        }
                        LogUtils.e(Constants.TAG, response.toString());
                    } else {
                        listener.onResponse(0, null, serviceType);
                        LogUtils.e(Constants.TAG, "Error: ");
                    }
                } catch (Exception e) {
                    listener.onResponse(0, null, serviceType);
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private JSONObject getEJson2(JSONObject jsonObject) {
        JSONObject jsonObjectNew = new JSONObject();
        try {
            if (jsonObject != null) {
                String jsonString = jsonObject.toString();
                AesCbcWithIntegrity.SecretKeys secretKeys = AesCbcWithIntegrity.generateKey();
                jsonObjectNew.put(Constants.API_KEY, RSASecurityUtils.encryptData(secretKeys.toString()));
                jsonObjectNew.put(Constants.API_REQUEST, AesCbcWithIntegrity.encrypt(jsonString, secretKeys).toString());
            }
            jsonObjectNew.put(Constants.API_SOURCE, Constants.SOURCE_ANDROID);
            LogUtils.e("ok", "NewParameters:- " + jsonObjectNew.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonObjectNew = null;
        }
        return jsonObjectNew;
    }

    private JSONObject getDJson2(JSONObject jsonObject) {
        try {
            AesCbcWithIntegrity.CipherTextIvMac cipherTextIvMac = new AesCbcWithIntegrity.CipherTextIvMac(jsonObject.getString(Constants.API_REQUEST));
            AesCbcWithIntegrity.SecretKeys secretKeys = AesCbcWithIntegrity.keys(RSASecurityUtils.decryptData(jsonObject.getString(Constants.API_KEY)));
            LogUtils.e("ok", "NewResponseSecretKey:- " + secretKeys.toString());
            String decrypt = new String(AesCbcWithIntegrity.decrypt(cipherTextIvMac, secretKeys), "UTF-8");
            LogUtils.e("ok", "NewResponse1:- " + decrypt);
            jsonObject = new JSONObject(decrypt);
            LogUtils.e("ok", "NewResponse:- " + jsonObject.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject;
    }

    public JsonObjectRequest executeApi(final JSONObject parameters, String apiUrl, final SERVICE_TYPE serviceType, int requestMethod, final OnWebServiceResult listener) {
        LogUtils.d(TAG, "url:" + apiUrl);
        if (parameters != null)
            LogUtils.d(TAG, "payload:" + parameters.toString());
        jsonObjectRequest = new JsonObjectRequest(requestMethod, apiUrl, getEJson2(parameters),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        LogUtils.d(Constants.TAG, response.toString());
                        response = getDJson2(response);
                        LogUtils.d(TAG + "_Decrypted ", response.toString());
                        listener.onResponse(1, response, serviceType);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    LogUtils.e(Constants.TAG, "TimeoutError " + error.getNetworkTimeMs());
                } else if (error instanceof NoConnectionError) {
                    LogUtils.e(Constants.TAG, "NoConnectionError");
                } else if (error instanceof AuthFailureError) {
                    LogUtils.e(Constants.TAG, "AuthFailureError");
                } else if (error instanceof ServerError) {
                    LogUtils.e(Constants.TAG, "ServerError");
                } else if (error instanceof NetworkError) {
                    LogUtils.e(Constants.TAG, "NetworkError");
                } else if (error instanceof ParseError) {
                    LogUtils.e(Constants.TAG, "ParseError");
                }
                LogUtils.e(Constants.TAG, "Error: " + error.getMessage());
                listener.onResponse(0, null, serviceType);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        jsonObjectRequest.setRetryPolicy(getRetryPolicy());
        LogUtils.e("ok", "" + apiUrl);
        return jsonObjectRequest;
    }

    private RetryPolicy getRetryPolicy() {
        return new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }

}
