package app.alansari.modules.sendmoney;

import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.models.BeneficiaryData;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;

/**
 * Created by Parveen Dala on 25 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class PaymentDetailsBranchPaymentActivity extends AppCompatActivity implements View.OnClickListener , LogOutTimerUtil.LogOutListener  {
    private Intent intent;
    private Button btnNext;
    private Context context;
    private Toolbar toolbar;
    private BeneficiaryData dataObject;
    private TextView tvAssistanceCall, tvTempTransactionNum;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void init() {
        tvAssistanceCall = (TextView) findViewById(app.alansari.R.id.assistance_call);
        tvTempTransactionNum = (TextView) findViewById(app.alansari.R.id.temp_transaction_num);
        tvAssistanceCall.setOnClickListener(this);
        btnNext = (Button) findViewById(app.alansari.R.id.next_btn);
        btnNext.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(app.alansari.R.layout.payment_details_branch_payment_activity);
        context = this;
        toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, app.alansari.R.color.colorWhite));
        ((TextView) findViewById(app.alansari.R.id.toolbar_title)).setText("Payment Details");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.ic_back_arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        init();
        if (getIntent().getExtras() != null) {
            dataObject = getIntent().getExtras().getParcelable(Constants.OBJECT);
            if (dataObject != null) {
                setInitialData();
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

    private void setInitialData() {
        if (dataObject != null) {
            tvTempTransactionNum.setText(dataObject.getTransactionNumber());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case app.alansari.R.id.next_btn:
                Toast.makeText(context, "Added To Wallet", Toast.LENGTH_SHORT).show();
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