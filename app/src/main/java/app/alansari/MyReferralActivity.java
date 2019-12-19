package app.alansari;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;

import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.customviews.MultiStateView;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.referral.ReferralData;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_BENEFICIARY_INSTANT;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.REFERRAL_POP_UP_CONTENT;
import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_EMPTY;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_ERROR;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_WRONG;


public class MyReferralActivity extends NavigationBaseActivity implements View.OnClickListener, OnWebServiceResult, LogOutTimerUtil.LogOutListener  {

    private Context context;
    private TextView tvEmpty, tvError;
    private MultiStateView multiStateView;
    private ImageView ivImageTop,ivImageMiddle;
    private TextView tvMessage1,tvMessage2,tvMessage3,tvMessage4,tvTermsAndConditions,tvMessage6;
    private String dataReferralMessage;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void init() {
        multiStateView = findViewById(app.alansari.R.id.multiState_rview);
        multiStateView.findViewById(app.alansari.R.id.empty_button).setOnClickListener(this);
        multiStateView.findViewById(app.alansari.R.id.error_button).setOnClickListener(this);
        tvEmpty = (multiStateView.findViewById(app.alansari.R.id.empty_textView));
        tvError = ( multiStateView.findViewById(app.alansari.R.id.error_textView));


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int height2 = displayMetrics.heightPixels;
        height= (height/2)+10;
        height2= (int) (0.15*height2);

        ivImageTop=findViewById(R.id.iv_image_top);
        ivImageMiddle=findViewById(R.id.iv_image_middle);
        tvMessage1=findViewById(R.id.tv_message1);
        tvMessage2=findViewById(R.id.tv_message2);
        tvMessage3=findViewById(R.id.tv_message3);
        tvMessage4=findViewById(R.id.tv_message4);
        tvTermsAndConditions=findViewById(R.id.tv_terms_and_conditions);
        tvMessage6=findViewById(R.id.tv_message6);
       //findViewById(app.alansari.R.id.nav_menu).setOnClickListener(this);
        findViewById(R.id.close_btn).setOnClickListener(this);
        findViewById(R.id.iv_share_whatsapp).setOnClickListener(this);
        findViewById(R.id.iv_share_other).setOnClickListener(this);

        ivImageTop.getLayoutParams().height = height; // OR
        ivImageMiddle.getLayoutParams().height = height2;



    }
    private void setViewState(CommonUtils.SERVICE_TYPE sType, int viewState) {
        if (viewState == VIEW_STATE_EMPTY) {
            findViewById(app.alansari.R.id.empty_button).setVisibility(View.GONE);
            CommonUtils.setViewState(multiStateView, viewState, tvEmpty, null, sType == FETCH_BENEFICIARY_INSTANT ? getString(R.string.empty_beneficiary_arex) : getString(R.string.empty_result));

        }
        if (viewState == VIEW_STATE_WRONG)
            CommonUtils.setViewState(multiStateView, viewState, tvError, null, getString(R.string.error_beneficiary));
        if (viewState == VIEW_STATE_ERROR)
            CommonUtils.setViewState(multiStateView, viewState, tvError, null, null);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_referral);
        context = this;
        init();
        fetchReferralPopup();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case app.alansari.R.id.empty_button:
            case app.alansari.R.id.error_button:
                fetchReferralPopup();
                break;
            case app.alansari.R.id.nav_menu:
                openMenuDrawer();
                break;
            case R.id.close_btn:
                onBackPressed();
                break;
            case R.id.iv_share_whatsapp:
                onClickWhatsApp(v);
                break;
            case R.id.iv_share_other:
                onClickSendRefer(v);
                break;
        }
    }



    private void fetchReferralPopup() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().referralPopup((String) SharedPreferenceManger.getPrefVal(Constants.MOBILE_NUM, null, SharedPreferenceManger.VALUE_TYPE.STRING),(String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), LogoutCalling.getDeviceID(context),sessionTime), Constants.REFERRAL_POP_UP_CONTENT_URL, REFERRAL_POP_UP_CONTENT, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(REFERRAL_POP_UP_CONTENT.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, REFERRAL_POP_UP_CONTENT.toString());


        } else {
            setViewState(VIEW_STATE_ERROR);

        }
    }



    private void setViewState(int viewState) {
        //if (isEmpty())
            CommonUtils.setViewState(multiStateView, viewState, (viewState == VIEW_STATE_EMPTY ? tvEmpty : tvError), null, null);
       // else {
         //   CommonUtils.setResponseToast(context, viewState);
        //}
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        if (sType == REFERRAL_POP_UP_CONTENT) {
            if (status == 1) {
                try {
                    if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                        if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                            ArrayList<ReferralData> faqData = (ArrayList<ReferralData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<ReferralData>>() {
                            }.getType());
                            if (faqData != null && faqData.size() > 0) {
                                updateData(faqData);
                                multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                                return;
                            }
                        }
                        setViewState(VIEW_STATE_EMPTY);
                    } else {
                        setViewState(VIEW_STATE_WRONG);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    setViewState(VIEW_STATE_WRONG);
                }
            } else {
                setViewState(VIEW_STATE_WRONG);
            }
        }
    }

    private void updateData(ArrayList<ReferralData> faqData) {
        tvMessage1.setText(faqData.get(0).getMESSAGECONTENT1());
        tvMessage2.setText(faqData.get(0).getMESSAGECONTENT2());
        if(!faqData.get(0).getMESSAGECONTENT3().equalsIgnoreCase(" ")){
            tvMessage3.setVisibility(View.VISIBLE);
            tvMessage3.setText(faqData.get(0).getMESSAGECONTENT3());
        }else
            tvMessage3.setVisibility(View.GONE);

        if(!faqData.get(0).getMESSAGECONTENT4().equalsIgnoreCase(" ")){
            tvMessage4.setVisibility(View.VISIBLE);
            tvMessage4.setText(faqData.get(0).getMESSAGECONTENT4());
        }else
            tvMessage4.setVisibility(View.GONE);

        tvTermsAndConditions.setText(Html.fromHtml("<u>Terms and Conditions</u>"));
        tvMessage6.setText(faqData.get(0).getTERMSANDCONDITIONSSTR());
        dataReferralMessage=faqData.get(0).getMESSAGECONTENT5()+faqData.get(0).getSUBURL()+((String) SharedPreferenceManger.getPrefVal(Constants.REFERRAL_CODE, null, SharedPreferenceManger.VALUE_TYPE.STRING));

        Glide.with(context)
                .load(faqData.get(0).getIMGURL())
                .into(ivImageTop);

        Glide.with(context)
                .load(faqData.get(0).getIMGURL1())
                .into(ivImageMiddle);

    }


    @Override
    protected void onStart() {
        super.onStart();
        LogOutTimerUtil.startLogoutTimer(this, this);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        LogOutTimerUtil.startLogoutTimer(this, this);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(CommonUtils.getLogoutStatus()) {
            CommonUtils.registerAgainOpen(getApplicationContext());
        }
    }

    @Override
    public void doLogout() {
        boolean mlogout=CommonUtils.isAppOnForeground(context);
        if(mlogout) {
            CommonUtils.registerAgainOpen(getApplicationContext());
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLogoutTimer();
    }
    public void onClickWhatsApp(View v) {
        PackageManager pm=getPackageManager();
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            intent.setPackage("com.whatsapp");
            intent.putExtra(Intent.EXTRA_TEXT, dataReferralMessage);
            startActivity(Intent.createChooser(intent, "Share with"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
        }

    }
    private void onClickSendRefer(View v) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, dataReferralMessage);
        startActivity(Intent.createChooser(sharingIntent, "Share app via"));
    }
}

