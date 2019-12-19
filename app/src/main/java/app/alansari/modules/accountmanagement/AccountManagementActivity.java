package app.alansari.modules.accountmanagement;

import app.alansari.NavigationBaseActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;

/**
 * Created by Parveen Dala on 17 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class AccountManagementActivity extends NavigationBaseActivity implements View.OnClickListener , LogOutTimerUtil.LogOutListener  {
    private Context context;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void init() {
        findViewById(app.alansari.R.id.beneficiary_continue_btn).setOnClickListener(this);
        findViewById(app.alansari.R.id.credit_card_continue_btn).setOnClickListener(this);
        findViewById(app.alansari.R.id.bank_account_continue_btn).setOnClickListener(this);
        findViewById(app.alansari.R.id.nav_menu).setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(app.alansari.R.layout.account_management_activity);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        ((TextView) findViewById(app.alansari.R.id.toolbar_title)).setText("Beneficiary");
        ((TextView) findViewById(app.alansari.R.id.toolbar_title_2)).setText("Management");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.ic_back_arrow);
        init();
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
        switch (v.getId()) {
            case app.alansari.R.id.nav_menu:
                openMenuDrawer();
                break;
            case app.alansari.R.id.beneficiary_continue_btn:
                startActivity(new Intent(context, BeneficiaryActivity.class).putExtra(Constants.INTENT_TYPE, Constants.ACTIVITY_FOR_DELETE));
                break;
            case app.alansari.R.id.credit_card_continue_btn:
                startActivity(new Intent(context, CreditCardActivity.class));
                break;
            case app.alansari.R.id.bank_account_continue_btn:
                startActivity(new Intent(context, BankAccountActivity.class));
                break;
        }
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
