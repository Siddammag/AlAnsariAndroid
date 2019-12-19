package app.alansari.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopeer.cardstack.CardStackView;
import com.loopeer.cardstack.StackAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.alansari.AppController;
import app.alansari.CreditCardPaymentActivity;
import app.alansari.R;
import app.alansari.Utils.CardType;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.Validation;
import app.alansari.customviews.MultiStateView;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.BeneficiaryData;
import app.alansari.models.CountryData;
import app.alansari.models.CreditCardData;
import app.alansari.models.PromoCodeData;
import app.alansari.models.TxnAmountData;
import app.alansari.modules.sendmoney.PaymentModeActivity;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.CALCULATE_CURRENCY_CREDIT_CARD;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.CHECK_TXN_LIMITS;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_EMPTY;

/**
 * Created by Parveen Dala on 20 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class CreditCardPaymentStackAdapter extends StackAdapter<CreditCardData> implements Filterable {

    private Context context;
    private View visibleLayout;
    private boolean isSearching;
    private LayoutInflater inflater;
    private MultiStateView multiStateView;
    private ArrayList<CreditCardData> creditCardList;
    private String vat = "";
    private String rounding = "";
    private String discount = "";
    private String promoCode = "";

    public CreditCardPaymentStackAdapter(Context context, ArrayList<CreditCardData> creditCardList) {
        super(context);
        this.context = context;
        this.creditCardList = creditCardList;
        inflater = LayoutInflater.from(context);
        //super.addList(this.originalList);
    }

    public CreditCardPaymentStackAdapter(Context context, MultiStateView multiStateView) {
        super(context);
        this.context = context;
        this.multiStateView = multiStateView;
        inflater = LayoutInflater.from(context);
    }

    public void setArrayData(ArrayList<CreditCardData> list) {
        this.creditCardList = list;
    }

    @Override
    public void updateData(List<CreditCardData> data) {
        if (data != null && data.size() > 0) {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            multiStateView.findViewById(R.id.empty_button).setVisibility(View.VISIBLE);
            multiStateView.findViewById(R.id.error_button).setVisibility(View.VISIBLE);
        } else {
            CommonUtils.setViewState(multiStateView, VIEW_STATE_EMPTY, (TextView) multiStateView.findViewById(R.id.empty_textView), null, null);
            if (isSearching) {
                multiStateView.findViewById(R.id.empty_button).setVisibility(View.GONE);
                multiStateView.findViewById(R.id.error_button).setVisibility(View.GONE);
            }
        }
        super.updateData(data);
    }

    @Override
    public void bindView(CreditCardData data, int position, CardStackView.ViewHolder holder) {
        CreditCardData current = data;
        if ((position + 1) % 4 == 0) {
            ((CreditCardViewHolder) holder).bgLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.credit_card_grey_bg));
        } else if ((position + 1) % 3 == 0) {
            ((CreditCardViewHolder) holder).bgLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.credit_card_blue_dark_bg));
        } else if ((position + 1) % 2 == 0) {
            ((CreditCardViewHolder) holder).bgLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.credit_card_blue_light_bg));
        } else {
            ((CreditCardViewHolder) holder).bgLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.credit_card_black_bg));
        }

        if (current.getName() != null && current.getName().trim().length() > 0) {
            ((CreditCardViewHolder) holder).tvName.setText(current.getName());
        } else {
            ((CreditCardViewHolder) holder).tvName.setText("");
        }

        if (current.getId() != null && current.getId().trim().length() > 0) {
            ((CreditCardViewHolder) holder).tvCcId.setText(current.getId());
        } else {
            ((CreditCardViewHolder) holder).tvCcId.setText("0");
        }

        if (current.getBankName() != null && current.getBankName().trim().length() > 0) {
            ((CreditCardViewHolder) holder).tvBankName.setText(current.getBankName());
        } else {
            ((CreditCardViewHolder) holder).tvBankName.setText("");
        }

        try {
            if (current.getCardNumber() != null && current.getCardNumber().trim().length() > 0) {
                ((CreditCardViewHolder) holder).tvCardNum1.setText(String.valueOf(current.getCardNumber().substring(0, 4)));
                ((CreditCardViewHolder) holder).cardNumLayout.setVisibility(View.VISIBLE);
                ((CreditCardViewHolder) holder).tvCardNum2.setText("****");
                ((CreditCardViewHolder) holder).tvCardNum3.setText("****");
                ((CreditCardViewHolder) holder).tvCardNum4.setText(String.valueOf(current.getCardNumber().substring(12)));
            } else {
                ((CreditCardViewHolder) holder).cardNumLayout.setVisibility(View.INVISIBLE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            ((CreditCardViewHolder) holder).cardNumLayout.setVisibility(View.INVISIBLE);
        }
        if (current.getCardTypeName() != null && current.getCardTypeName().trim().length() > 0) {
            /*if (current.getCardTypeName().equalsIgnoreCase("MASTER")) {
                ((CreditCardViewHolder) holder).ivCardType.setImageResource(R.drawable.svg_card_master);
                ((CreditCardViewHolder) holder).tvCardTypeName.setVisibility(View.VISIBLE);
                ((CreditCardViewHolder) holder).tvCardTypeName.setText("mastercard");
            } else if (current.getCardTypeName().equalsIgnoreCase("VISA")) {
                ((CreditCardViewHolder) holder).ivCardType.setImageResource(R.drawable.icn_card_visa);
                ((CreditCardViewHolder) holder).tvCardTypeName.setVisibility(View.GONE);
            } else if (current.getCardTypeName().equalsIgnoreCase("MAESTRO")) {
                ((CreditCardViewHolder) holder).ivCardType.setImageResource(R.drawable.svg_card_maestro);
                ((CreditCardViewHolder) holder).tvCardTypeName.setVisibility(View.VISIBLE);
                ((CreditCardViewHolder) holder).tvCardTypeName.setText("maestro");
            } else if (current.getCardTypeName().equalsIgnoreCase("JCB")) {
                ((CreditCardViewHolder) holder).ivCardType.setImageResource(R.drawable.icn_card_jcb);
                ((CreditCardViewHolder) holder).tvCardTypeName.setVisibility(View.GONE);
            }*/
            ((CreditCardViewHolder) holder).tvCardTypeName.setText(current.getCardTypeName());
            String cardType = CardType.getCardType(String.valueOf(current.getCardNumber()));
            if (cardType != null) {
                ((CreditCardViewHolder) holder).ivCardType.setImageResource(CardType.getCardLogo(cardType));
            }
        } else {
            ((CreditCardViewHolder) holder).ivCardType.setImageResource(R.drawable.svg_card_master);
            ((CreditCardViewHolder) holder).tvCardTypeName.setVisibility(View.GONE);
        }
    }

    public ArrayList<CreditCardData> getArrayList() {
        return creditCardList;
    }

    public CreditCardData getItemAt(int position) {
        return creditCardList.get(position);
    }

    public void clear() {
        this.creditCardList.clear();
        notifyDataSetChanged();
    }

    @Override
    public CardStackView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
        return new CreditCardViewHolder(inflater.inflate(R.layout.item_credit_card_payment, parent, false));
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                isSearching = true;
                updateData((ArrayList<CreditCardData>) results.values);
                isSearching = false;
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<CreditCardData> filteredResults = null;
                if (constraint.length() == 0) {
                    filteredResults = creditCardList;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }
                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }

    protected List<CreditCardData> getFilteredResults(String constraint) {
        List<CreditCardData> results = new ArrayList<>();
        for (CreditCardData item : creditCardList) {
            if (item.getName() != null && item.getName().toLowerCase().contains(constraint)) {
                results.add(item);
            } else if (item.getBankName() != null && item.getBankName().toLowerCase().contains(constraint)) {
                results.add(item);
            } else if (item.getCardNumber() != null && item.getCardNumber().toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }
        return results;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    class CreditCardViewHolder extends CardStackView.ViewHolder implements OnWebServiceResult {
        View headerLayout;
        NestedScrollView detailsLayout;
        EditText etAmount, etPromoCode;
        View bgLayout, cardNumLayout;
        ImageView ivCardType, acceptImage;
        Button btnConfirm;
        RelativeLayout promoLayout;
        TextView tvName, tvBankName, tvCardNum1, tvCardNum2, tvCardNum3, tvCardNum4, tvCardTypeName, tvAEDFee, tvTotalAmount, tvCcId, tvDisclaimer, tvVat, tvDiscount, tvRoundOff, acceptText;
        boolean isPromoListActive;
        private boolean isAgreed, isAmountFeeValid;
        private Dialog priorityConditionsDialog;


        public CreditCardViewHolder(View view) {
            super(view);
            headerLayout = view.findViewById(R.id.header_layout);
            detailsLayout = view.findViewById(R.id.details_layout);
            bgLayout = itemView.findViewById(R.id.bg_layout);
            ivCardType = (ImageView) itemView.findViewById(R.id.card_type);
            tvName = (TextView) itemView.findViewById(R.id.name);
            tvBankName = (TextView) itemView.findViewById(R.id.bank_name);

            cardNumLayout = itemView.findViewById(R.id.card_num_layout);
            tvCardNum1 = (TextView) itemView.findViewById(R.id.card_num_1);
            tvCardNum2 = (TextView) itemView.findViewById(R.id.card_num_2);
            tvCardNum3 = (TextView) itemView.findViewById(R.id.card_num_3);
            tvCardNum4 = (TextView) itemView.findViewById(R.id.card_num_4);
            tvCardTypeName = (TextView) itemView.findViewById(R.id.card_type_name);
            tvAEDFee = (TextView) itemView.findViewById(R.id.fee);
            tvVat = (TextView) itemView.findViewById(R.id.vat);
            tvDiscount = (TextView) itemView.findViewById(R.id.discount);
            tvRoundOff = (TextView) itemView.findViewById(R.id.roundoff);
            tvDisclaimer = (TextView) itemView.findViewById(R.id.disclaimer);
            tvTotalAmount = (TextView) itemView.findViewById(R.id.total_amount);
            tvCcId = (TextView) itemView.findViewById(R.id.cc_id);
            etAmount = (EditText) itemView.findViewById(R.id.amount);
            btnConfirm = (Button) itemView.findViewById(R.id.confirm_btn);
            promoLayout = (RelativeLayout) itemView.findViewById(R.id.promocode_layout);
            etPromoCode = (EditText) itemView.findViewById(app.alansari.R.id.promocode);
            acceptImage = (ImageView) itemView.findViewById(app.alansari.R.id.accept_image);
            acceptText = (TextView) itemView.findViewById(app.alansari.R.id.accept_text);

            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Validation.isValidEditTextValue(etAmount) && !tvCcId.getText().toString().equalsIgnoreCase("0")) {
                        checkTxnLimits(tvCcId.getText().toString());
                    } else {
                        etAmount.setError("Please enter a valid amount");
                        etAmount.requestFocus();
                    }
                }
            });

            acceptImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isAgreed) {
                        acceptImage.setImageResource(0);
                        isAgreed = false;
                    } else {
                        acceptImage.setImageResource(R.drawable.svg_success);
                        isAgreed = true;
                    }
                    enableConfirmBtn();
                }
            });

            acceptText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createConditionDialog();
                }
            });


            etAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    isAmountFeeValid = false;
                    btnConfirm.setEnabled(false);
                    tvAEDFee.setText(null);
                    tvDisclaimer.setText(null);
                    tvDiscount.setText(null);
                    tvRoundOff.setText(null);
                    tvTotalAmount.setText(null);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (Validation.isValidString(s.toString()))
                        CommonUtils.addCommaAfterThousand(etAmount, this, s.toString());
                }
            });

            etAmount.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        CommonUtils.hideKeyboard(context);
                        if (!TextUtils.isEmpty(etAmount.getText().toString().trim()) && etAmount.getText().toString().trim().length() > 0) {
                            calculateTotalToPay(tvCcId.getText().toString(), CommonUtils.getTextFromEditText(etAmount));
                        }
                        return false;
                    }
                    return true;
                }
            });
        }

        @Override
        public void onItemExpand(boolean b) {
            detailsLayout.setVisibility(b ? View.VISIBLE : View.GONE);
        }

        private void onSuccess() {
            TxnAmountData currencyData = new TxnAmountData();
            currencyData.setYouSend(etAmount.getText().toString().trim());
            currencyData.setYouGet(etAmount.getText().toString().trim());
            currencyData.setTotalToPay(tvTotalAmount.getText().toString());
            currencyData.setFee(tvAEDFee.getText().toString());
            currencyData.setRate("");
            currencyData.setYouSendCurrencyData(new CountryData.CurrencyData().getUAECurrencyData());
            currencyData.setYouGetCurrencyData(new CountryData.CurrencyData().getUAECurrencyData());
            BeneficiaryData dataObject = new BeneficiaryData();
            dataObject.setBeneficiaryId(tvCcId.getText().toString());
            dataObject.setTxnAmountData(currencyData);
            dataObject.setVat(vat);
            dataObject.setDiscount(discount);
            dataObject.setRoundoff(rounding);
            dataObject.setPromoCode(promoCode);
            Intent intent = new Intent(context, PaymentModeActivity.class);
            intent.putExtra(Constants.OBJECT, dataObject);
            intent.putExtra(Constants.SOURCE_TYPE, Constants.TYPE_CREDIT_CARD);
            context.startActivity(intent);
        }


        private void calculateTotalToPay(String creditCardId, String amount) {
            try {
                if (NetworkStatus.getInstance(context).isOnline2(context)) {
                    String userPkId=(String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
                    String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
                    JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().calculateCurrencyCreditCard(creditCardId, amount,userPkId, LogoutCalling.getDeviceID(context),sessionTime), Constants.CALCULATE_CURRENCY_CREDIT_CARD_URL, CALCULATE_CURRENCY_CREDIT_CARD, Request.Method.PUT, this);
                    cancelPendingRequests();
                    AppController.getInstance().addToRequestQueue(jsonObjReq, CALCULATE_CURRENCY_CREDIT_CARD.toString());
                    CommonUtils.showLoading(context, context.getString(R.string.please_wait), CALCULATE_CURRENCY_CREDIT_CARD.toString(), false);
                } else {
                    Toast.makeText(context, context.getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                Toast.makeText(context, context.getString(R.string.please_try_again), Toast.LENGTH_SHORT).show();
            }
        }

        void checkTxnLimits(String creditCardId) {
            if (NetworkStatus.getInstance(context).isOnline2(context)) {
                String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
                JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().checkTxnLimits(CommonUtils.getUserId(), creditCardId, Constants.AREX_MAPPING, Constants.TRANSFER_TYPE_CREDIT_CARD, CommonUtils.getTextFromTextView(tvTotalAmount),LogoutCalling.getDeviceID(context),sessionTime), Constants.CHECK_TXN_LIMITS_URL, CHECK_TXN_LIMITS, Request.Method.PUT, this);
                AppController.getInstance().getRequestQueue().cancelAll(CHECK_TXN_LIMITS.toString());
                AppController.getInstance().addToRequestQueue(jsonObjReq, CHECK_TXN_LIMITS.toString());
                CommonUtils.showLoading(context, context.getString(R.string.please_wait), CommonUtils.SERVICE_TYPE.CHECK_TXN_LIMITS.toString(), false);
            } else {
                Toast.makeText(context, context.getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show();
            }
        }

        void fetchPromoCode() {
            if (NetworkStatus.getInstance(context).isOnline2(context)) {
                String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
                JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchPromoCode((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING),LogoutCalling.getDeviceID(context),sessionTime), Constants.PROMO_CODE_URL, CommonUtils.SERVICE_TYPE.PROMO_CODE, Request.Method.POST, this);
                AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.PROMO_CODE.toString());
                AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.PROMO_CODE.toString());
                CommonUtils.showLoading(context, context.getString(R.string.please_wait), CommonUtils.SERVICE_TYPE.PROMO_CODE.toString(), false);
            } else {
                Toast.makeText(context, context.getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show();
            }
        }

        private void cancelPendingRequests() {
            AppController.getInstance().cancelPendingRequests(CALCULATE_CURRENCY_CREDIT_CARD.toString());
            AppController.getInstance().cancelPendingRequests(CHECK_TXN_LIMITS.toString());
        }

        private void onErrorInCalculation() {
        }

        @Override
        public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
            CommonUtils.hideLoading();
            switch (sType) {
                case CALCULATE_CURRENCY_CREDIT_CARD:
                    if (status == 1) {
                        try {
                            if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                                detailsLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                     //   detailsLayout.smoothScrollTo(0, btnConfirm.getTop());
                                    }
                                });
                                String amount = response.getString(Constants.AMOUNT);
                                String fee = response.getString(Constants.CHARGES_ON_US);
                                String disclaimer = response.getString(Constants.DISCLAIMER);
                                String vatLable = response.getString(Constants.VAT_CHARGES_CODE);
                                String roundingLable = response.getString(Constants.VAT_ROUNDINGOFF_CODE);
                                String discountLable = response.getString(Constants.VAT_DISCOUNT_CODE);

                                vat = response.getString(Constants.VAT_CHARGES);
                                rounding = response.getString(Constants.VAT_ROUNDINGOFF);
                                discount = response.getString(Constants.VAT_DISCOUNT);

                                if (fee != null)
                                    fee = CommonUtils.getDecimalFormattedString(Double.parseDouble(fee));
                                if (amount != null)
                                    amount = CommonUtils.getDecimalFormattedString(Double.parseDouble(amount));
                                if (amount != null && fee != null) {
                                    tvTotalAmount.setText(CommonUtils.addCommaToString(String.valueOf(response.getString(Constants.AMOUNT))));
                                    tvAEDFee.setText(" " + fee);
                                    if (vat != null && !vat.equalsIgnoreCase("0.0")) {
                                        vat = CommonUtils.getDecimalFormattedString(Double.parseDouble(vat));
                                        tvVat.setText(vatLable + " : AED " + vat);
                                    } else {
                                        tvVat.setText(null);
                                        tvVat.setVisibility(View.GONE);
                                    }
                                    if (discount != null && !discount.equalsIgnoreCase("0.0")) {
                                        discount = CommonUtils.getDecimalFormattedString(Double.parseDouble(discount));
                                        tvDiscount.setText(discountLable + " : AED " + discount);
                                    } else {
                                        tvDiscount.setText(null);
                                        tvDiscount.setVisibility(View.GONE);
                                    }
                                    if (rounding != null && !rounding.equalsIgnoreCase("0.0")) {
                                        rounding = CommonUtils.getDecimalFormattedString(Double.parseDouble(rounding));
                                        tvRoundOff.setText(roundingLable + " : AED " + rounding);
                                    } else {
                                        tvRoundOff.setText(null);
                                        tvRoundOff.setVisibility(View.GONE);
                                    }
                                    tvDisclaimer.setText(disclaimer);

                                    if (response.has(Constants.PROMO_ACTIVE) && response.getString(Constants.PROMO_ACTIVE).equalsIgnoreCase("Y")) {
                                        promoLayout.setVisibility(View.VISIBLE);
                                        if (response.getString(Constants.PROMO_LIST_ACTIVE).equalsIgnoreCase("Y")) {
                                            isPromoListActive = true;
                                        } else {
                                            isPromoListActive = false;
                                        }
                                        ((CreditCardPaymentActivity) context).setListners(etPromoCode, isPromoListActive);
                                        fetchPromoCode();
                                    } else {
                                        isPromoListActive = false;
                                        promoLayout.setVisibility(View.GONE);
                                    }
                                    isAmountFeeValid = true;
                                } else {
                                    Toast.makeText(context, context.getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE) || response.getString(Constants.STATUS_MSG).equals(Constants.REJECTED)) {
                                Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_LONG).show();
                                onErrorInCalculation();
                            } else {
                                Toast.makeText(context, context.getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                                onErrorInCalculation();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Toast.makeText(context, context.getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                            onErrorInCalculation();
                        }
                    } else {
                        Toast.makeText(context, context.getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                        onErrorInCalculation();
                    }
                    break;
                case CHECK_TXN_LIMITS:
                    if (status == 1) {
                        try {
                            if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                                onSuccess();
                            } else if (response.getString(Constants.STATUS_MSG).equals(Constants.REJECTED)) {
                                CommonUtils.showLimitDialog(context, response.getString(Constants.MESSAGE));
                            } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE)) {
                                //Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_LONG).show();
                                CommonUtils.showLimitDialog(context, response.getString(Constants.MESSAGE));

                            } else {
                                Toast.makeText(context, context.getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Toast.makeText(context, context.getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, context.getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case PROMO_CODE:
                    if (status == 1) {
                        try {
                            if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                                if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                    ArrayList<PromoCodeData> promoCodeData = (ArrayList<PromoCodeData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<PromoCodeData>>() {
                                    }.getType());
                                    ((CreditCardPaymentActivity) context).setPromoData(promoCodeData);
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Toast.makeText(context, context.getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, context.getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }

        void enableConfirmBtn() {
            if (isAgreed && isAmountFeeValid)
                btnConfirm.setEnabled(true);
            else
                btnConfirm.setEnabled(false);
        }

        private void createConditionDialog() {
            priorityConditionsDialog = new Dialog(context, app.alansari.R.style.CustomDialogThemeLightBg);
            priorityConditionsDialog.setCanceledOnTouchOutside(false);
            priorityConditionsDialog.setContentView(R.layout.wu_terms_dialog);
            ((TextView) priorityConditionsDialog.findViewById(app.alansari.R.id.toolbar_title)).setText("TERMS AND CONDITIONS");

            CommonUtils.setLayoutFont(context, "HelveticaNeue-Medium.ttf",priorityConditionsDialog.findViewById(R.id.toolbar_title));
            CommonUtils.setLayoutFont(context, "Roboto-Regular.ttf", priorityConditionsDialog.findViewById(app.alansari.R.id.dialog_title), priorityConditionsDialog.findViewById(R.id.accept_text));
            // tvText.setText(Html.fromHtml(context.getString(R.string.indemnity_form_text), null, new IndemnityActivity.UlTagHandler()));

            ((TextView) priorityConditionsDialog.findViewById(app.alansari.R.id.dialog_title)).setText(Html.fromHtml(context.getString(R.string.indemnity_form_text)));
            ((ImageView) priorityConditionsDialog.findViewById(app.alansari.R.id.close_btn)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    priorityConditionsDialog.dismiss();
                }
            });
            priorityConditionsDialog.show();
        }
    }

}
