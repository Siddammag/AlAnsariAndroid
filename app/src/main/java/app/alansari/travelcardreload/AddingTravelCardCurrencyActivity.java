package app.alansari.travelcardreload;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import app.alansari.R;
import app.alansari.SelectCurrencyFlagTravelCardActivity;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.Validation;
import app.alansari.customviews.progressbar.CircleProgressBar;
import app.alansari.models.travalcardvalidateflag.TravelCardFlag;
import app.alansari.network.NetworkStatus;
import app.alansari.preferences.SharedPreferenceManger;

public class AddingTravelCardCurrencyActivity extends AppCompatActivity implements View.OnClickListener{

    private ArrayList<TravelCardFlag> travelCardFlags, travelCardFlags2;
    private Context context;
    private TravelCardFlag selectedCurrency = null;
    private TextView tvFromCode, tvToCode, tvRate, tvAssistanceCall, tvSelectCurrency, tvBuy, tvSell, tvCurrency;
    private AppCompatImageView ivFromFlag, ivToFlag, btnSwap;
    private EditText etFrom, etTo;
    private TextWatcher textWatcher;
    private boolean isCalculatingCurrency = false;
    private CircleProgressBar progressBar;
    private int currentDirection = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_travel_card_currency);
        init();
        if (getIntent() != null) {
            travelCardFlags = getIntent().getExtras().getParcelableArrayList(Constants.OBJECT);
            openTravelCardCurrencyList();
        } else {
            onBackPressed();
        }

    }

    private void openTravelCardCurrencyList() {
        Intent intent = new Intent(context, SelectCurrencyFlagTravelCardActivity.class);
        try {
            for (int i = 0; i < travelCardFlags.size(); i++) {
                Log.e("bcjhbcjhb", "" + travelCardFlags.get(i).getISOCCYCODE());
                Log.e("bcjhbcjhbb", "" + travelCardFlags2.toString());
                for (int j = 0; j < travelCardFlags2.size(); j++) {
                    if (travelCardFlags.get(i).getISOCCYCODE().equalsIgnoreCase(travelCardFlags2.get(j).getISOCCYCODE())) {
                        travelCardFlags.remove(i);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        intent.putExtra(Constants.OBJECT, travelCardFlags);
        startActivityForResult(intent, Constants.SELECT_ITEM_CURRENCY);
        overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);
    }
    

    public void init() {
        travelCardFlags2 = new ArrayList<>();
        context=this;
        etFrom = (EditText) findViewById(app.alansari.R.id.from);
        etTo = (EditText) findViewById(app.alansari.R.id.to);
        tvFromCode = (TextView) findViewById(app.alansari.R.id.from_code);
        tvToCode = (TextView) findViewById(app.alansari.R.id.to_code);
        ivFromFlag = (AppCompatImageView) findViewById(app.alansari.R.id.from_flag);
        ivToFlag = (AppCompatImageView) findViewById(app.alansari.R.id.to_flag);
        tvRate = (TextView) findViewById(app.alansari.R.id.rate);
        tvSelectCurrency = (TextView) findViewById(app.alansari.R.id.select_currency);
        tvSelectCurrency.setOnClickListener(this);

        progressBar = (CircleProgressBar) findViewById(app.alansari.R.id.progress_bar);
        btnSwap = (AppCompatImageView) findViewById(app.alansari.R.id.swap_btn);
        tvSelectCurrency.setOnClickListener(this);
        progressBar.setVisibility(View.GONE);
        btnSwap.setVisibility(View.GONE);

        ivFromFlag.setImageResource(0);
        ivToFlag.setImageResource(0);
        tvFromCode.setText(null);
        tvToCode.setText(null);

        ivFromFlag.setVisibility(View.INVISIBLE);
        ivToFlag.setVisibility(View.INVISIBLE);
        tvFromCode.setVisibility(View.INVISIBLE);
        tvToCode.setVisibility(View.INVISIBLE);

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s == etFrom.getEditableText()) {
                    if (s.length() > 5) {
                        etFrom.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(app.alansari.R.dimen.dimens_22sp));
                    } else {
                        etFrom.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(app.alansari.R.dimen.dimens_28sp));
                    }
                    if (!isCalculatingCurrency && Validation.isValidEditTextValue(etFrom)) {
                        etTo.setText("");
                        progressBar.setVisibility(View.GONE);
                        btnSwap.setVisibility(View.GONE);
                    }
                    if (Validation.isValidString(s.toString()))
                        CommonUtils.addCommaAfterThousand(etFrom, textWatcher, s.toString());
                } else if (s == etTo.getEditableText()) {
                    if (s.length() > 5) {
                        etTo.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(app.alansari.R.dimen.dimens_22sp));
                    } else {
                        etTo.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(app.alansari.R.dimen.dimens_28sp));
                    }
                    if (!isCalculatingCurrency && Validation.isValidEditTextValue(etTo)) {
                        etFrom.setText("");
                        progressBar.setVisibility(View.GONE);
                        btnSwap.setVisibility(View.GONE);
                    }
                    if (Validation.isValidString(s.toString()))
                        CommonUtils.addCommaAfterThousand(etTo, textWatcher, s.toString());
                }
            }
        };

        EditText.OnEditorActionListener editiorListener = new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    CommonUtils.hideKeyboard(context);
                    if (v.getId() == etFrom.getId()) {
                        if (Validation.isValidEditTextValue(etFrom)) {
                            currentDirection = 1;
                            calculateCurrency();
                        }
                        return true;
                    } else if (v.getId() == etTo.getId()) {
                        if (Validation.isValidEditTextValue(etTo)) {
                            currentDirection = 2;
                            calculateCurrency();
                        }
                        return true;
                    }
                }
                return false;
            }
        };

        etFrom.addTextChangedListener(textWatcher);
        etTo.addTextChangedListener(textWatcher);
        etFrom.setOnEditorActionListener(editiorListener);
        etTo.setOnEditorActionListener(editiorListener);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == Constants.SELECT_ITEM_CURRENCY) {
                if (resultCode == RESULT_OK) {
                    // regCurrencyLayout.setVisibility(View.VISIBLE);
                    Log.e("fsvjbjhvbsfj", "" + data.getParcelableExtra(Constants.OBJECT));
                    selectedCurrency = data.getParcelableExtra(Constants.OBJECT);

                    selectedCurrency = data.getParcelableExtra(Constants.OBJECT);
                    tvSelectCurrency.setText(selectedCurrency.getCCYDESC());
                    CommonUtils.setCountryFlagImage(context, ivFromFlag, selectedCurrency.getFLAG());
                    ivToFlag.setImageResource(R.drawable.svg_flag_aed);
                    tvFromCode.setText(selectedCurrency.getISOCCYCODE());
                    tvToCode.setText("AED");
                    ivFromFlag.setVisibility(View.VISIBLE);
                    ivToFlag.setVisibility(View.VISIBLE);
                    tvToCode.setVisibility(View.VISIBLE);
                    tvFromCode.setVisibility(View.VISIBLE);

                   // travelCardFlags2.add(selectedCurrency);

                }else{
                    onBackPressed();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        onFinish();
        super.onBackPressed();

    }

    private void onFinish() {
        Intent intent=new Intent();
        Bundle bundle=new Bundle();
        bundle.putParcelable(Constants.OBJECT, selectedCurrency);
        intent.putExtras(bundle);
        intent.putExtra("test","DKG");
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }

    private void calculateCurrency() {
        try {
            if ((currentDirection == 1 && Validation.isValidEditTextValue(etFrom)) || (currentDirection == 2 && Validation.isValidEditTextValue(etTo))) {
                if (NetworkStatus.getInstance(context).isOnline2(context)) {
                    JSONObject requestParams;
                    String userPkId = (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
                    String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
                    // if (currentDirection == 1)
                    // requestParams = new APIRequestParams().foreignCurrency(selectedCurrency.getCURRENCYCODE(), "91", FCY_TYP, CommonUtils.getTextFromEditText(etFrom), userPkId, LogoutCalling.getDeviceID(context), sessionTime);
                    //  else
                    //   requestParams = new APIRequestParams().foreignCurrency("91", selectedCurrency.getCURRENCYCODE(), FCY_TYP, CommonUtils.getTextFromEditText(etTo), userPkId, LogoutCalling.getDeviceID(context), sessionTime);
                    // JsonObjectRequest jsonObjReq = new CallAddr().executeApi(requestParams, Constants.FOREIGN_CURRENCY_URL, CommonUtils.SERVICE_TYPE.FOREIGN_CURRENCY, Request.Method.PUT, this);
                    //cancelPendingRequests();
                    isCalculatingCurrency = true;
                    // setTransparentCoverVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    btnSwap.setVisibility(View.GONE);
                    // AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.FOREIGN_CURRENCY.toString());
                } else {
                    Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
                }
            } else {
                progressBar.setVisibility(View.GONE);
                // setTransparentCoverVisibility(View.GONE);
            }
        } catch (Exception ex) {
            Toast.makeText(context, getString(app.alansari.R.string.please_try_again), Toast.LENGTH_SHORT).show();
        }
    }
}
