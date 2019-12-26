package app.alansari;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;

import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.Utils.LogUtils;
import app.alansari.modules.branchlocator.BranchLocatorCityActivity;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;

/**
 * Created by Parveen Dala on 14 December, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class GoToBranchActivity extends AppCompatActivity implements LogOutTimerUtil.LogOutListener {
    private Context context;
    private TextView tvReferenceNum;
    private TextView tvMessage;
    private String message;
    private RelativeLayout home;

    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (CommonUtils.getLogoutStatus()) {
            CommonUtils.registerAgainOpen(getApplicationContext());
        }
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        LogOutTimerUtil.startLogoutTimer(this, this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.go_to_branch_activity);
        context = this;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        tvReferenceNum = (TextView) findViewById(R.id.reference_num);
        tvMessage = (TextView) findViewById(R.id.message);
        tvMessage.setText((String) SharedPreferenceManger.getPrefVal(Constants.MESSAGE, "", SharedPreferenceManger.VALUE_TYPE.STRING));

        tvReferenceNum.setText((String) SharedPreferenceManger.getPrefVal(Constants.GO_TO_BRANCH_REF_NUM, "-----", SharedPreferenceManger.VALUE_TYPE.STRING));
        LogUtils.d("ok", "RefNumber1 " + (String) SharedPreferenceManger.getPrefVal(Constants.GO_TO_BRANCH_REF_NUM, "-----", SharedPreferenceManger.VALUE_TYPE.STRING));
        try {
            if (getIntent() != null && getIntent().getStringExtra(Constants.PROFILE_UPDATE_FLAG).equalsIgnoreCase("N")) {
                ((TextView) findViewById(R.id.branch_locator_btn)).setText("Emirates ID upload");
                findViewById(R.id.branch_locator_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addFragment();

                    }
                });


            } else {
                ((TextView) findViewById(R.id.branch_locator_btn)).setText("Branch Locator");
                findViewById(R.id.branch_locator_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(context, BranchLocatorCityActivity.class).putExtra(Constants.HIDE_BURGER_MENU, true));
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            ((TextView) findViewById(R.id.branch_locator_btn)).setText("Branch Locator");
            findViewById(R.id.branch_locator_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(context, BranchLocatorCityActivity.class).putExtra(Constants.HIDE_BURGER_MENU, true));
                }
            });
        }
        home = ((RelativeLayout) findViewById(R.id.home));
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseAnalytics.logEvent("App_Loading", null);
                startActivity(new Intent(context, LandingActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogOutTimerUtil.startLogoutTimer(this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLogoutTimer();
    }

    private void addFragment() {
        Intent intent = new Intent(getApplicationContext(), MyEmiratesIdActivity.class);
        intent.putExtra(Constants.HIDE_BURGER_MENU, true);
        intent.putExtra("HideDialog", false);
        startActivity(intent);
    }

    @Override
    public void doLogout() {
        boolean mlogout = CommonUtils.isAppOnForeground(context);
        if (mlogout) {
            CommonUtils.registerAgainOpen(getApplicationContext());
        }
    }

}