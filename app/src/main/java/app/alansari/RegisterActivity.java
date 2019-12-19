package app.alansari;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.fragments.RegisterEmailFragment;
import app.alansari.fragments.RegisterMobileFragment;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;


/**
 * Created by Parveen Dala on 11 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener , LogOutTimerUtil.LogOutListener  {

    private Intent intent;
    private Context context;
    private Toolbar toolbar;
    private ProgressDialog pDialog;
    private View mobileCLayout, emailCLayout;
    private ObjectAnimator objectAnimator, objectAnimator2;
    private View mainLayout, fragmentParentLayout;
    private TextView tvMobile, tvMobileDetails, tvId, tvIdDetails;
    private ImageView ivMobile, ivId;
    private RelativeLayout home;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void init() {
        mobileCLayout = findViewById(R.id.reg_mobile_layout);
        emailCLayout = findViewById(R.id.reg_id_layout);

        mobileCLayout.setOnClickListener(this);
        emailCLayout.setOnClickListener(this);
        mobileCLayout.setOnTouchListener(this);
        emailCLayout.setOnTouchListener(this);

        mainLayout = findViewById(R.id.reg_main_layout);
        fragmentParentLayout = findViewById(R.id.fragment_parent_layout);

        tvMobile = ((TextView) findViewById(R.id.reg_mobile));
        tvMobileDetails = ((TextView) findViewById(R.id.reg_mobile_details));
        tvId = ((TextView) findViewById(R.id.reg_id));
        tvIdDetails = ((TextView) findViewById(R.id.reg_id_details));
        ivMobile = ((ImageView) findViewById(R.id.reg_mobile_image));
        ivMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Crashlytics.getInstance().crash();
            }
        });
        ivId = ((ImageView) findViewById(R.id.reg_id_image));
        home = ((RelativeLayout) findViewById(R.id.home));
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        context = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(ContextCompat.getColor(context, R.color.colorTransparent));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        ((TextView) findViewById(R.id.toolbar_title)).setText("REGISTER");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setVisibility(View.GONE);
        init();
        if (((boolean) SharedPreferenceManger.getPrefVal(Constants.IS_LOGGED_IN, false, SharedPreferenceManger.VALUE_TYPE.BOOLEAN)) && ((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING) != null) && ((String) SharedPreferenceManger.getPrefVal(Constants.MOBILE_NUM, null, SharedPreferenceManger.VALUE_TYPE.STRING) != null) && ((String) SharedPreferenceManger.getPrefVal(Constants.PIN, null, SharedPreferenceManger.VALUE_TYPE.STRING) != null)) {
            ((TextView) findViewById(R.id.login_label)).setTextColor(ContextCompat.getColor(context, R.color.color3F3F3F));
            ((TextView) findViewById(R.id.login_link)).setTextColor(ContextCompat.getColor(context, R.color.color3F3F3F));
            findViewById(R.id.login_link).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent = new Intent(context, LandingActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            });
        } else {
            ((TextView) findViewById(R.id.login_label)).setTextColor(ContextCompat.getColor(context, R.color.color9E9E9E));
            ((TextView) findViewById(R.id.login_link)).setTextColor(ContextCompat.getColor(context, R.color.color9E9E9E));
            findViewById(R.id.reg_login_link_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopUp("For security reasons, kindly register again using your mobile number or eExchange credentials.");
                }
            });
        }

        if (getIntent() != null) {
            if (getIntent().getStringExtra("goto") != null && getIntent().getStringExtra("goto").equalsIgnoreCase("registration")) {
                hideWithHandler(View.VISIBLE, toolbar);
                findViewById(R.id.fragment_back_view).getLayoutParams().height = (int) getResources().getDimension(R.dimen.dimens_320);
                fragmentParentLayout.setVisibility(View.VISIBLE);
                addFragment(1);
                objectAnimator = ObjectAnimator.ofFloat(fragmentParentLayout, "alpha", 0f, 1f);
                objectAnimator.setDuration(300);
                objectAnimator.start();
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.reg_mobile_layout:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        findViewById(R.id.reg_mobile_layout).setBackgroundColor(ContextCompat.getColor(context, R.color.color1E6AB3));
                        tvMobile.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                        tvMobileDetails.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                        ivMobile.setColorFilter(ContextCompat.getColor(context, R.color.colorWhite));
                        return true;
                    case MotionEvent.ACTION_UP:
                        onActionUp(v);
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        resetColorState();
                        return true;
                }
                return false;
            case R.id.reg_id_layout:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        findViewById(R.id.reg_id_layout).setBackgroundColor(ContextCompat.getColor(context, R.color.color1E6AB3));
                        tvId.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                        tvIdDetails.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                        ivId.setColorFilter(ContextCompat.getColor(context, R.color.colorWhite));
                        return true;
                    case MotionEvent.ACTION_UP:
                        onActionUp(v);
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        resetColorState();
                        return true;
                }
                return false;
        }
        return false;
    }

    private void onActionUp(final View v) {
        switch (v.getId()) {
            case R.id.reg_mobile_layout:
                hideWithHandler(View.VISIBLE, toolbar);
                findViewById(R.id.fragment_back_view).getLayoutParams().height = (int) getResources().getDimension(R.dimen.dimens_320);
                fragmentParentLayout.setVisibility(View.VISIBLE);
                addFragment(1);
                objectAnimator = ObjectAnimator.ofFloat(fragmentParentLayout, "alpha", 0f, 1f);
                objectAnimator.setDuration(300);
                objectAnimator.start();
                break;
            case R.id.reg_id_layout:
                hideWithHandler(View.VISIBLE, toolbar);
                findViewById(R.id.fragment_back_view).getLayoutParams().height = (int) getResources().getDimension(R.dimen.dimens_200);
                fragmentParentLayout.setVisibility(View.VISIBLE);
                addFragment(2);
                objectAnimator = ObjectAnimator.ofFloat(fragmentParentLayout, "alpha", 0f, 1f);
                objectAnimator.setDuration(300);
                objectAnimator.start();
                break;
        }
    }

    private void addFragment(int type) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (type == 1)
            fragmentTransaction.replace(R.id.fragment_layout, new RegisterMobileFragment());
        else
            fragmentTransaction.replace(R.id.fragment_layout, new RegisterEmailFragment());
        fragmentTransaction.commit();
    }

    private void resetColorState() {
        findViewById(R.id.reg_mobile_layout).setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
        tvMobile.setTextColor(ContextCompat.getColor(context, R.color.color3F3F3F));
        tvMobileDetails.setTextColor(ContextCompat.getColor(context, R.color.color7F8486));
        ivMobile.setColorFilter(ContextCompat.getColor(context, R.color.color1E6AB3));

        findViewById(R.id.reg_id_layout).setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
        tvId.setTextColor(ContextCompat.getColor(context, R.color.color3F3F3F));
        tvIdDetails.setTextColor(ContextCompat.getColor(context, R.color.color7F8486));
        ivId.setColorFilter(ContextCompat.getColor(context, R.color.color1E6AB3));
    }

    @Override
    public void onBackPressed() {
        if (fragmentParentLayout.getVisibility() == View.VISIBLE) {
            CommonUtils.hideKeyboard(context);
            objectAnimator = ObjectAnimator.ofFloat(fragmentParentLayout, "alpha", 1f, 0f);
            objectAnimator.setDuration(300);
            objectAnimator.start();
            toolbar.setVisibility(View.GONE);
            hideWithHandler(View.GONE, fragmentParentLayout);
            resetColorState();
        } else {
            super.onBackPressed();
        }
    }

    private void hideWithHandler(final int visibility, final View... v) {
        Runnable lObjRunnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < v.length; i++) {
                    v[i].setVisibility(visibility);
                }
            }
        };
        Handler lObjHandler = new Handler();
        lObjHandler.postDelayed(lObjRunnable, 300);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showPopUp(String message) {
        final Dialog myDialog = new Dialog(context, app.alansari.R.style.CustomDialogThemeLightBg);
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.setContentView(app.alansari.R.layout.sample_msg_dialog);

        ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_title)).setVisibility(View.GONE);
        ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_text)).setText(message);

        myDialog.findViewById(app.alansari.R.id.btn_layout).setVisibility(View.GONE);
        myDialog.findViewById(app.alansari.R.id.single_btn).setVisibility(View.VISIBLE);
        ((TextView) myDialog.findViewById(app.alansari.R.id.single_btn)).setText(getString(app.alansari.R.string.btn_continue));

        myDialog.findViewById(app.alansari.R.id.single_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.show();
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

}