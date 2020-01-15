package app.alansari.fragments.my_emirates;


import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import app.alansari.AppController;
import app.alansari.R;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogUtils;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.MyEmiratespersonalInfoData;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.LOAD_MY_ID_DETAILS;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyEmiratesPersonalInfo extends Fragment implements View.OnClickListener, OnWebServiceResult {

    View view;
    Context context;
    private EditText etEidNumber, etEidExpiry, etCardNumber;
    private String selectedExpiry, calExp;
    private TextInputLayout eidLayout, expiryLayout, cardLayout;
    private int count = 0;
    private int mYear, mMonth, mDay;
    private ImageView ivPersonalLoading,ivPersonalArrow;
    private LinearLayout personalExpandLayout;
    private String sessionTime;
    boolean hideDialog;

    public MyEmiratesPersonalInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_emirates_personal_info, container, false);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onStart() {
        super.onStart();
        fetchAllDetails();
    }

    private void fetchAllDetails() {
        hideDialog = getArguments().getBoolean("HideDialog");
        if (NetworkStatus.getInstance(getContext()).isOnline2(context)) {
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().getingDataInformation((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), (String) SharedPreferenceManger.getPrefVal(Constants.AREX_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), (String) SharedPreferenceManger.getPrefVal(Constants.CE_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), LogoutCalling.getDeviceID(context),sessionTime),
                    Constants.LOAD_MY_ID_DETAILS, LOAD_MY_ID_DETAILS, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.LOAD_MY_ID_DETAILS.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.LOAD_MY_ID_DETAILS.toString());
            CommonUtils.showLoading(context, getString(R.string.please_wait), LOAD_MY_ID_DETAILS.toString(), false);
        } else {
            Toast.makeText(context, getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }


    }

    private void init(View views) {
        context = getContext();
        sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);

        eidLayout = (TextInputLayout) view.findViewById(R.id.eid_number_layout);
        etEidNumber = (EditText) view.findViewById(R.id.eid_number);
        cardLayout = (TextInputLayout) view.findViewById(R.id.card_layout);
        etCardNumber = (EditText) view.findViewById(R.id.card_number);
        expiryLayout = (TextInputLayout) view.findViewById(R.id.eid_expiry_layout);
        etEidExpiry = (EditText) view.findViewById(R.id.eid_expiry);
        ivPersonalLoading = (ImageView) view.findViewById(R.id.personal_loading_image);
        ivPersonalArrow = (ImageView)view. findViewById(app.alansari.R.id.personal_arrow_image);
        view.findViewById(app.alansari.R.id.personal_title_layout).setOnClickListener(this);
        personalExpandLayout = (LinearLayout)view.findViewById(app.alansari.R.id.expiryLayout);
        ivPersonalArrow.setImageResource(R.drawable.ic_up_arrow);
       /* etEidExpiry.setOnClickListener(this);
        expiryLayout.setOnClickListener(this);*/

        etEidNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 1) {
                    eidLayout.setErrorEnabled(true);
                    eidLayout.setError(getString(R.string.error_invalid_eid_number));
                } else {
                    eidLayout.setError(null);
                    eidLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (count <= etEidNumber.getText().toString().length() && (etEidNumber.getText().toString().length() == 3 ||
                        etEidNumber.getText().toString().length() == 8 || etEidNumber.getText().toString().length() == 16)) {
                    etEidNumber.setText(etEidNumber.getText().toString() + "-");
                    int pos = etEidNumber.getText().length();
                    etEidNumber.setSelection(pos);
                } else if (count >= etEidNumber.getText().toString().length() && (etEidNumber.getText().toString().length() == 3
                        || etEidNumber.getText().toString().length() == 8 || etEidNumber.getText().toString().length() == 16)) {
                    etEidNumber.setText(etEidNumber.getText().toString().substring(0, etEidNumber.getText().toString().length() - 1));
                    int pos = etEidNumber.getText().length();
                    etEidNumber.setSelection(pos);
                }
                count = etEidNumber.getText().toString().length();
            }
        });

        ((Button) view.findViewById(R.id.btn_click_update_emirates_id)).setOnClickListener(this);





    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_click_update_emirates_id:
                ivPersonalLoading.setImageResource(app.alansari.R.drawable.ic_success);

               /* FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction().replace(R.id.fragment_layout, new MyEmiratesIdFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();*/

                /*if (TextUtils.isEmpty(etEidNumber.getText().toString().trim())) {
                    eidLayout.setErrorEnabled(true);
                    eidLayout.setError(getString(R.string.error_invalid_eid_number));
                } else if (selectedExpiry == null) {
                    expiryLayout.setErrorEnabled(true);
                    expiryLayout.setError(getString(R.string.error_invalid_expiry_date));
                } else if (TextUtils.isEmpty(etCardNumber.getText().toString().trim())) {
                    cardLayout.setErrorEnabled(true);
                    cardLayout.setError(getString(R.string.invalid_card_number));
                } else {
                    eidLayout.setErrorEnabled(false);
                    expiryLayout.setErrorEnabled(false);
                    cardLayout.setErrorEnabled(false);
                    submitDataNextScreen();

                }*/
                submitDataNextScreen();
                break;

            case R.id.eid_expiry:
            case R.id.eid_expiry_layout:
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                calExp = new StringBuilder().append(year).append("-").append(monthOfYear + 1).append("-").append(monthOfYear).toString();
                                selectedExpiry = calExp;
                                etEidExpiry.setText(dayOfMonth + " " + (CommonUtils.getMonthName(monthOfYear + 1)) + ", " + year);
                                expiryLayout.setError(null);
                                expiryLayout.setErrorEnabled(false);

                            }
                        }, mYear, mMonth, mDay);

                Calendar currentTime = Calendar.getInstance();
                datePickerDialog.getDatePicker().setMinDate(currentTime.getTimeInMillis() - 10000);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.setTitle("Set Expiry Date");
                datePickerDialog.show();
                break;
            case app.alansari.R.id.personal_title_layout:
                if (personalExpandLayout.getVisibility() == View.VISIBLE) {
                    ivPersonalArrow.setImageResource(app.alansari.R.drawable.ic_down_arrow);
                    personalExpandLayout.setVisibility(View.GONE);
                } else {
                    personalExpandLayout.setVisibility(View.VISIBLE);
                    ivPersonalArrow.setImageResource(app.alansari.R.drawable.ic_up_arrow);
                }
                break;
                }

    }

    private void submitDataNextScreen() {
        Bundle bundle=new Bundle();
        bundle.putString(Constants.EID_NUMBER,etEidNumber.getText().toString().trim());
        bundle.putString(Constants.CARD_NUMBER,etCardNumber.getText().toString().trim());
        bundle.putString(Constants.EXPIRY_DATE,etEidExpiry.getText().toString().trim());
        bundle.putBoolean("HideDialog", hideDialog);
        if(getArguments().getParcelable("data")!=null) bundle.putParcelable("data",getArguments().getParcelable("data"));
        MyEmiratesIdFragment myEmiratesIdFragment=new MyEmiratesIdFragment();
        myEmiratesIdFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction().replace(R.id.fragment_layout, myEmiratesIdFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        switch (sType) {
            case LOAD_MY_ID_DETAILS:
                try {
                    CommonUtils.hideLoading();
                    if (status == 1) {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            LogUtils.e("adcfjnasdcfjbsdResult", "" + response.getString("RESULT").toString());
                            ArrayList<MyEmiratespersonalInfoData> myEmiratespersonalInfoData = new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<MyEmiratespersonalInfoData>>() {
                            }.getType());
                            if (myEmiratespersonalInfoData != null && myEmiratespersonalInfoData.size() > 0) {
                                setDataViews(myEmiratespersonalInfoData.get(0).getEidNumber(), myEmiratespersonalInfoData.get(0).getEidCardnumber(), myEmiratespersonalInfoData.get(0).getIdExpirtyDate());
                                ivPersonalLoading.setImageResource(app.alansari.R.drawable.ic_success);
                                //ivPersonalLoading.setImageResource(app.alansari.R.drawable.ic_loading);

                            }
                        }
                    } else {
                        Toast.makeText(context, getString(app.alansari.R.string.empty_result), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    //Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void setDataViews(String eidNumber, String eidCardnumber, String idExpirtyDate) {
        etCardNumber.setText(eidCardnumber);
        etEidNumber.setText(convertEidFormat(eidNumber));
        etEidExpiry.setText(changeDateFormat(idExpirtyDate));
        selectedExpiry = etEidExpiry.getText().toString().trim();
    }

    private String changeDateFormat(String idExpirtyDate) {
        SimpleDateFormat sourceDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sourceDateFormat.parse(idExpirtyDate);
            SimpleDateFormat targetDateFormat = new SimpleDateFormat("dd MMM, yyyy");
            System.out.println(targetDateFormat.format(date));
            return String.valueOf(targetDateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String convertEidFormat(String string) {
        StringBuffer str = new StringBuffer(string);
        System.out.println("string = " + str);
        // insert boolean value at offset 8
        str.insert(3, '-');
        str.insert(8, '-');
        str.insert(16, '-');
        return str.toString();
    }

}



