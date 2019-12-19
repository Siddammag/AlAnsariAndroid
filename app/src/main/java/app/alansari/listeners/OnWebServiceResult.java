package app.alansari.listeners;


import app.alansari.Utils.CommonUtils;

import org.json.JSONObject;

/**
 * Created by Parveen Dala on 12 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public interface OnWebServiceResult {

    void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType);

}
