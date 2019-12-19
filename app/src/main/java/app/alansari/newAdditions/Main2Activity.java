package app.alansari.newAdditions;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import app.alansari.NavigationBaseActivity;
import app.alansari.R;
import app.alansari.listeners.OnWebServiceResult;


public class Main2Activity extends NavigationBaseActivity implements View.OnClickListener,OnWebServiceResult {

   /* private TxnDetailsData txnDetailsData;
    private String sourceType;
    private JSONObject jsonObject;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toolbar toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        ((TextView) findViewById(R.id.toolbar_title)).setText("Card Reload");
        findViewById(app.alansari.R.id.nav_menu).setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.ic_back_arrow);


        /*
        jsonObject=new JSONObject();

        String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);

        jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.AREX_MEM_FK_ID, "2728045");
            jsonObject.put(Constants.USER_PK_ID, CommonUtils.getUserId());
            jsonObject.put(Constants.DEVICE_ID, CommonUtils.getDeviceID(this));
            jsonObject.put(Constants.SESSION_ID, sessionTime);

        }catch (JSONException e){
            e.printStackTrace();
        }

        callingWebSebervice();
        */

    }

    /*private void callingWebSebervice() {
        if (NetworkStatus.getInstance(this).isOnline2(this)) {
                //CommonUtils.showLoading(this, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.LOAD_WIRE_CARD_RELOAD.toString(), false);
                JsonObjectRequest jsonObjReq = new CallAddr().executeApi(jsonObject, Constants.LOAD_WIRE_CARD_RELOAD, LOAD_WIRE_CARD_RELOAD, Request.Method.PUT, this);
                AppController.getInstance().getRequestQueue().cancelAll(LOAD_WIRE_CARD_RELOAD.toString());
                AppController.getInstance().addToRequestQueue(jsonObjReq, LOAD_WIRE_CARD_RELOAD.toString());
            } else {
            Toast.makeText(this, "No network", Toast.LENGTH_SHORT).show();
            }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(Constants.MESSAGE, "");
        intent.putExtra(Constants.RESULT, 1);
        setResult(Activity.RESULT_OK, intent);
        finish();
        super.onBackPressed();



    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        switch (sType) {
            case POPUP:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<PopUpData> popupData = (ArrayList<PopUpData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<PopUpData>>() {
                                }.getType());


                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }


                }
                break;

        }


    }*/

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_menu:
                openMenuDrawer();
                break;

        }
    }
}



// <Button tile='Press' icon={{namr: 'code'}}/>