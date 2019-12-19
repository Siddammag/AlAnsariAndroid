package app.alansari.ipcheck;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by Parveen Dala on 05 June, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */
public class ApiManager {

    //private static final String ENDPOINT = "http://ip-api.com/json";
    //private static final String ENDPOINT = "https://ipapi.co/json";
 //   private static final String ENDPOINT = "https://mobileapp.eexchange.ae:9108";
    private static final String ENDPOINT = "https://mobileapp.eexchange.ae:9108/ProjectGateway/api/user/mobilenumberregistration";

    private RequestQueue mRequestQueue;

    public ApiManager(RequestQueue requestQueue) {
        mRequestQueue = requestQueue;
    }

    private static String toUrlParams(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        for (String key : params.keySet()) {
            try {
                sb.append("&").append(key).append("=").append(URLEncoder.encode(params.get(key), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString().substring(1);
    }

    public void getGeoIpInfo(Response.Listener<NewGeoResponseModel> listener, Response.ErrorListener errorListener) {
        GsonObjectRequest gsonReq = new GsonObjectRequest(Request.Method.GET, ENDPOINT, NewGeoResponseModel.class, null, listener, errorListener);
        gsonReq.setRetryPolicy(getRetryPolicy());
        mRequestQueue.add(gsonReq);

    }

    private RetryPolicy getRetryPolicy() {
        return new DefaultRetryPolicy(25000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }
}
