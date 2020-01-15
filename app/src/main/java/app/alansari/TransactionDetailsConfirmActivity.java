package app.alansari;

import app.alansari.Utils.Constants;
import app.alansari.models.BeneficiaryData;
import app.alansari.modules.sendmoney.PaymentModeActivity;
import android.content.Context;
import android.content.Intent;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Parveen Dala on 24 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class TransactionDetailsConfirmActivity extends AppCompatActivity implements View.OnClickListener {
    private Intent intent;
    private Toolbar toolbar;
    private Context context;
    private Button btnNext;
    private ImageView ivFlag;
    private BeneficiaryData dataObject;
    private TextView tvName, tvBankName, tvBranchName, tvAccountNumber, tvCurrencyCode, tvAmount, tvAmountToPay, tvCurrencyCodeToPay;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void init() {
        //ivFlag = (ImageView) findViewById(R.id.flag);
        tvName = (TextView) findViewById(R.id.name);
        tvBankName = (TextView) findViewById(R.id.bank_name);
        tvBranchName = (TextView) findViewById(R.id.branch_name);
        tvAccountNumber = (TextView) findViewById(R.id.account_number);

        tvCurrencyCode = (TextView) findViewById(R.id.currency_code);
        tvAmount = (TextView) findViewById(R.id.total_amount);
        tvCurrencyCodeToPay = (TextView) findViewById(R.id.currency_code_to_pay);
        tvAmountToPay = (TextView) findViewById(R.id.total_amount_to_pay);

        btnNext = (Button) findViewById(R.id.next_btn);
        btnNext.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_details_confirm_activity);
        context = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        ((TextView) findViewById(R.id.toolbar_title)).setText("Transaction");
        ((TextView) findViewById(R.id.toolbar_title_2)).setText("Details");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        init();
        if (getIntent().getExtras() != null) {
            dataObject = getIntent().getExtras().getParcelable(Constants.OBJECT);
            if (dataObject != null) {
                setInitialData();
            }
        }
    }

    private void setInitialData() {
        if (dataObject != null) {
            tvName.setText(dataObject.getName());
            tvBankName.setText(dataObject.getBankData().getBankName());
            tvBranchName.setText(dataObject.getBranchData().getBranchName());
            tvAccountNumber.setText(dataObject.getAccountNumber());
            tvCurrencyCode.setText("Receive Amount (" + dataObject.getTxnAmountData().getYouGetCurrencyData().getName() + ")");
            tvAmount.setText(dataObject.getTxnAmountData().getYouGet());
            tvCurrencyCodeToPay.setText("Total To Pay (" + dataObject.getTxnAmountData().getYouSendCurrencyData().getName() + ")");
            tvAmountToPay.setText(dataObject.getTxnAmountData().getTotalToPay());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_btn:
                intent = new Intent(context, PaymentModeActivity.class);
                intent.putExtra(Constants.OBJECT, dataObject);
                startActivity(intent);
                break;
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
}
