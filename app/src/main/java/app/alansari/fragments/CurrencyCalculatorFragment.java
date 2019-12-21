package app.alansari.fragments;

import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.customviews.progressbar.CircleProgressBar;
import app.alansari.listeners.OnWebServiceResult;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import androidx.core.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

/**
 * Created by Parveen Dala on 14 October, 2016
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */
public class CurrencyCalculatorFragment extends Fragment implements View.OnClickListener, OnWebServiceResult {

    private View view;
    private String memberId;
    private Intent intent;
    private Context context;
    private int beneficiaryType;
    private String countryCode = "11";
    private EditText etSend, etGet;
    private CircleProgressBar progressBar;
    private ImageView ivSuccess;
    private TextView tvSendCode, tvGetCode, tvRate, tvTimeMessage, tvMessageBottom;


    private void init() {
        etSend = (EditText) view.findViewById(app.alansari.R.id.send);
        etGet = (EditText) view.findViewById(app.alansari.R.id.get);
        tvSendCode = (TextView) view.findViewById(app.alansari.R.id.send_code);
        tvGetCode = (TextView) view.findViewById(app.alansari.R.id.get_code);
        tvRate = (TextView) view.findViewById(app.alansari.R.id.rate);
        tvTimeMessage = (TextView) view.findViewById(app.alansari.R.id.time_message);
        tvMessageBottom = (TextView) view.findViewById(app.alansari.R.id.message);
        progressBar = (CircleProgressBar) view.findViewById(app.alansari.R.id.progress_bar);
        ivSuccess = (ImageView) view.findViewById(app.alansari.R.id.success_image);
        progressBar.setVisibility(View.GONE);
        ivSuccess.setVisibility(View.GONE);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                progressBar.setVisibility(View.GONE);
                ivSuccess.setVisibility(View.GONE);
                if (s == etSend.getEditableText()) {
                    if (s.length() > 5) {
                        etSend.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(app.alansari.R.dimen.dimens_30sp));
                    } else {
                        etSend.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(app.alansari.R.dimen.dimens_35sp));
                    }
                } else if (s == etGet.getEditableText()) {
                    if (s.length() > 5) {
                        etGet.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(app.alansari.R.dimen.dimens_30sp));
                    } else {
                        etGet.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(app.alansari.R.dimen.dimens_35sp));
                    }
                }
            }
        };

        EditText.OnEditorActionListener editiorListener = new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    CommonUtils.hideKeyboard(context);
                    if (v.getId() == etSend.getId()) {
                        if (!TextUtils.isEmpty(etSend.getText().toString().trim()) && etSend.getText().toString().trim().length() > 0) {
                            calculateCurrency(1);
                        }
                        return true;
                    } else if (v.getId() == etGet.getId()) {
                        if (!TextUtils.isEmpty(etGet.getText().toString().trim()) && etGet.getText().toString().trim().length() > 0) {
                            calculateCurrency(2);
                        }
                        return true;
                    }
                }
                return false;
            }
        };

        etSend.addTextChangedListener(textWatcher);
        etGet.addTextChangedListener(textWatcher);
        etSend.setOnEditorActionListener(editiorListener);
        etGet.setOnEditorActionListener(editiorListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        view = mInflater.inflate(app.alansari.R.layout.currency_converter_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        if (getArguments() != null) {
            beneficiaryType = getArguments().getInt(Constants.TYPE, Constants.BANK_TRANSFER);
        } else {
            beneficiaryType = Constants.BANK_TRANSFER;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    private void calculateCurrency(final int type) {
        progressBar.setVisibility(View.VISIBLE);
        ivSuccess.setVisibility(View.GONE);

        Runnable lObjRunnable = new Runnable() {
            @Override
            public void run() {
                if (type == 1) {
                    etGet.setText(etSend.getText().toString());
                } else {
                    etSend.setText(etGet.getText().toString());
                }
                progressBar.setVisibility(View.GONE);
                ivSuccess.setVisibility(View.VISIBLE);
            }
        };
        new Handler().postDelayed(lObjRunnable, 2500);

//        if (NetworkStatus.getInstance(context).isOnline2(context)) {
//            JsonObjectRequest jsonObjReq = CallAddr.fetchBeneficiary(countryCode, memberId, beneficiaryType == Constants.BENEFICIARY_VALUE_TRANSFER ? "A" : "C", CommonUtils.SERVICE_TYPE.FETCH_BENEFICIARY_VALUE, this);
//            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.FETCH_BENEFICIARY_VALUE.toString());
//        } else {
//            CommonUtils.showSnack(context, false, tvRate, R.id.rate);
//        }
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        if (status == 1) {
            switch (sType) {
            }
        }
    }
}