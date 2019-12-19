package app.alansari.modules.sendmoney.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;

import app.alansari.AppController;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogUtils;
import app.alansari.Utils.OnLoadMoreListener;
import app.alansari.customviews.MultiStateView;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.TxnDetailsCeCashPayout;
import app.alansari.models.TxnDetailsCreditCardData;
import app.alansari.models.TxnDetailsData;
import app.alansari.models.transactionhistroy.TxnDetailsHistroyTravelCard;
import app.alansari.modules.sendmoney.TransactionHistoryActivity;
import app.alansari.modules.sendmoney.adapters.TransactionHistoryRecyclerAdapter;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import app.alansari.preferences.SharedPreferenceManger.VALUE_TYPE;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_TRANSACTIONS_HISTORY_REMITTANCE_API;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_TRANSACTIONS_HISTORY_REMITTANCE_CREDIT_CARD_API;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_EMPTY;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_ERROR;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_WRONG;

/**
 * Created by Parveen Dala on 14 October, 2016
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */
public class TransactionHistoryFragment extends Fragment implements View.OnClickListener, OnWebServiceResult {

    private View view;
    private Intent intent;
    private String memberId;
    private String startCount = "1";
    private String arexUserId;
    private Context context;
    private String transactionType;
    private TextView tvEmpty, tvError;
    private ImageView ivError, ivEmpty;
    private MultiStateView multiStateView;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private TransactionHistoryRecyclerAdapter recyclerAdapter;
    private String CePaginationNo="1";
    String sessionTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(app.alansari.R.layout.recycler_view_full, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(app.alansari.R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(CommonUtils.getPrimaryColor(context));
        swipeRefreshLayout.setEnabled(false);
//        swipeRefreshLayout();
        multiStateView = (MultiStateView) view.findViewById(app.alansari.R.id.multiState_rview);
        multiStateView.findViewById(app.alansari.R.id.empty_button).setOnClickListener(this);
        multiStateView.findViewById(app.alansari.R.id.error_button).setOnClickListener(this);

        multiStateView.findViewById(app.alansari.R.id.empty_button).setVisibility(View.GONE);

        tvEmpty = ((TextView) multiStateView.findViewById(app.alansari.R.id.empty_textView));
        tvError = ((TextView) multiStateView.findViewById(app.alansari.R.id.error_textView));
        ivEmpty = ((ImageView) multiStateView.findViewById(app.alansari.R.id.empty_imageView));
        ivError = ((ImageView) multiStateView.findViewById(app.alansari.R.id.error_imageView));
        recyclerView = (RecyclerView) view.findViewById(app.alansari.R.id.recyclerView);
        memberId = (String) SharedPreferenceManger.getPrefVal(Constants.MEMBER_ID, null, VALUE_TYPE.STRING);

        if (getArguments() != null) {
            // transactionType = getArguments().getString(Constants.TYPE, Constants.TRANSACTION_TYPE_VALUE);
            transactionType = getArguments().getString(Constants.TYPE);

        } else {
            transactionType = Constants.TRANSACTION_TYPE_VALUE;
        }
        LogUtils.v("transactionType", "transactionType : " + transactionType);
        Log.e("fwhfuihwujfgb",""+transactionType);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerAdapter = new TransactionHistoryRecyclerAdapter(context, new ArrayList<TxnDetailsData>(), transactionType, recyclerView);
        recyclerAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (Integer.parseInt(startCount) >= recyclerAdapter.getItemCount()) {
                    fetchTransactions();
                }
            }
        });
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (shouldLoad()) {
            startCount = "1";
            fetchTransactions();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        try {
            if (isVisibleToUser && context != null) {
                startCount = "1";
                fetchTransactions();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean shouldLoad() {
       /* if (transactionType.equalsIgnoreCase("VALUE") && ((TransactionHistoryActivity) context).getAdapterPosition() == 0) {
            return true;
        } else if (transactionType.equalsIgnoreCase("INSTANT") && ((TransactionHistoryActivity) context).getAdapterPosition() == 1) {
            return true;
        } else if (transactionType.equalsIgnoreCase("CREDIT_CARD_PAYMENT") && ((TransactionHistoryActivity) context).getAdapterPosition() == 2) {
            return true;
        }
        */
        if (transactionType.equalsIgnoreCase("VALUE") && ((TransactionHistoryActivity) context).getAdapterPosition() == 0) {
            return true;
        } else if (transactionType.equalsIgnoreCase("CREDIT_CARD_PAYMENT") && ((TransactionHistoryActivity) context).getAdapterPosition() == 1) {
            return true;
        } else if (transactionType.equalsIgnoreCase("WU") && ((TransactionHistoryActivity) context).getAdapterPosition() == 2) {
            return true;
        }



        /*else if(transactionType.equalsIgnoreCase("TRAVEL_CARD") && ((TransactionHistoryActivity) context).getAdapterPosition() == 4) {
            return true;
        }*//*else if(transactionType.equalsIgnoreCase("TRAVEL_CARD") && ((TransactionHistoryActivity) context).getAdapterPosition() == 4)
            return true;*/
        return false;
    }

    private void swipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkStatus.getInstance(context).isOnline2(context)) {
                    startCount = "1";
                    fetchTransactions();
                } else
                    onItemsLoadComplete();
            }
        });
    }

    void onItemsLoadComplete() {
        swipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case app.alansari.R.id.empty_button:
            case app.alansari.R.id.error_button:
                fetchTransactions();
        }
    }

    private void fetchTransactions() {
        if (transactionType.equalsIgnoreCase(Constants.TRANSACTION_TYPE_CREDIT_CARD_PAYMENT)) {
            fetchPendingTransactionsCreditCard();
            return;
        } else if (transactionType.equalsIgnoreCase(Constants.TRANSACTION_TYPE_WU)) {
            fetchWU();
            return;
        }else if(transactionType.equalsIgnoreCase(Constants.TRANSACTION_TYPE_TRAVEL_CARD)){
            //Toast.makeText(context, "Here", Toast.LENGTH_SHORT).show();
            fetchTransactionsTravelCard();
            return;
        }
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            if (!swipeRefreshLayout.isRefreshing() && startCount.equalsIgnoreCase("1")) {
                multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            } else {
                CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), null, false);
            }
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchTransactionsRemittanceApi2((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), Constants.TRANSACTION_STATUS_COMPLETED, Constants.AREX_MAPPING, startCount,CePaginationNo, LogoutCalling.getDeviceID(context),sessionTime), Constants.FETCH_TRANSACTIONS_REMITTANCE_API_URL, FETCH_TRANSACTIONS_HISTORY_REMITTANCE_API, Request.Method.PUT, this);
            AppController.getInstance().addToRequestQueue(jsonObjReq, FETCH_TRANSACTIONS_HISTORY_REMITTANCE_API.toString());
        } else {
            CommonUtils.setViewState(multiStateView, VIEW_STATE_ERROR, tvError, ivError, null);
        }
    }

    private void fetchTransactionsTravelCard() {
        arexUserId = CommonUtils.getMemPkId(Constants.AREX_MAPPING);
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), null, false);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().loadTravelcard((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), arexUserId,LogoutCalling.getDeviceID(context),sessionTime), Constants.LOAD_WC_TXN_HISTORY_URL, CommonUtils.SERVICE_TYPE.LOAD_WC_TXN_HISTORY, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.LOAD_WC_TXN_HISTORY.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.LOAD_WC_TXN_HISTORY.toString());
        } else {
            CommonUtils.setViewState(multiStateView, VIEW_STATE_ERROR, tvError, null, null);
        }

    }

    void fetchWU() {
        arexUserId = CommonUtils.getMemPkId(Constants.AREX_MAPPING);
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), null, false);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().loadWuNumber((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), arexUserId, "",LogoutCalling.getDeviceID(context),sessionTime), Constants.FETCH_WU_SEND_MONEY_URL, CommonUtils.SERVICE_TYPE.FETCH_WU_SEND_MONEY, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.FETCH_WU_SEND_MONEY.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.FETCH_WU_SEND_MONEY.toString());
        } else {
            CommonUtils.setViewState(multiStateView, VIEW_STATE_ERROR, tvError, null, null);
        }
    }

    private void fetchWuTransactions() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            if (!swipeRefreshLayout.isRefreshing() && startCount.equalsIgnoreCase("1")) {
                multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            } else {
                CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), null, false);
            }
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchTransactionsRemittanceApi((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), Constants.TRANSACTION_STATUS_COMPLETED, transactionType, startCount, LogoutCalling.getDeviceID(context),sessionTime), Constants.FETCH_WU_TRANSACTIONS_URL, FETCH_TRANSACTIONS_HISTORY_REMITTANCE_API, Request.Method.PUT, this);
            AppController.getInstance().addToRequestQueue(jsonObjReq, FETCH_TRANSACTIONS_HISTORY_REMITTANCE_API.toString());
        } else {
            CommonUtils.setViewState(multiStateView, VIEW_STATE_ERROR, tvError, ivError, null);
        }
    }

    private void fetchPendingTransactionsCreditCard() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            if (!swipeRefreshLayout.isRefreshing() && startCount.equalsIgnoreCase("1")) {
                multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            } else {
                CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), null, false);
            }
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchTransactionsRemittanceApi((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), Constants.TRANSACTION_STATUS_COMPLETED, transactionType, startCount, LogoutCalling.getDeviceID(context),sessionTime), Constants.FETCH_TRANSACTIONS_REMITTANCE_CREDIT_CARD_API_URL, FETCH_TRANSACTIONS_HISTORY_REMITTANCE_CREDIT_CARD_API, Request.Method.PUT, this);
            AppController.getInstance().addToRequestQueue(jsonObjReq, FETCH_TRANSACTIONS_HISTORY_REMITTANCE_CREDIT_CARD_API.toString());
        } else {
            CommonUtils.setViewState(multiStateView, VIEW_STATE_ERROR, tvError, ivError, null);
        }
    }

    private Boolean isEmpty() {
        return recyclerAdapter.getItemCount() == 0;
    }

    private void setViewState(int viewState) {
        if (isEmpty()) {
            if (viewState == VIEW_STATE_EMPTY)
                if (transactionType == Constants.TRANSACTION_TYPE_WU) {
                    CommonUtils.setViewState(multiStateView, viewState, tvEmpty, null, "No transactions yet. Send money using Western Union");
                } else {
                    CommonUtils.setViewState(multiStateView, viewState, tvEmpty, null, getString(transactionType == Constants.TRANSACTION_TYPE_VALUE ? app.alansari.R.string.empty_completed_arex : transactionType.equalsIgnoreCase(Constants.TRANSACTION_TYPE_INSTANT) ? app.alansari.R.string.empty_completed_ce : app.alansari.R.string.empty_completed_credit));
                }
            if (viewState == VIEW_STATE_WRONG)
                CommonUtils.setViewState(multiStateView, viewState, tvError, null, getString(app.alansari.R.string.error_pending));
            if (viewState == VIEW_STATE_ERROR)
                CommonUtils.setViewState(multiStateView, viewState, tvError, null, null);
        } else {
            CommonUtils.setResponseToast(context, viewState);
        }
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        if (context != null && isAdded())
            switch (sType) {
                case FETCH_TRANSACTIONS_HISTORY_REMITTANCE_CREDIT_CARD_API:
                case FETCH_TRANSACTIONS_HISTORY_REMITTANCE_API:
                    try {
                        if (status == 1) {
                            if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                                CommonUtils.hideLoading();
                                if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                    if (startCount.equalsIgnoreCase("1")) {
                                        recyclerAdapter.clear();
                                    }
                                    startCount = response.getString(Constants.NEXT_RECORD);
                                    ArrayList<TxnDetailsData> txnDetailsData = null;
                                    switch (sType) {
                                        case FETCH_TRANSACTIONS_HISTORY_REMITTANCE_CREDIT_CARD_API:
                                            ArrayList<TxnDetailsCreditCardData> txnDetailsCreditCardData = (ArrayList<TxnDetailsCreditCardData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<TxnDetailsCreditCardData>>() {
                                            }.getType());
                                            txnDetailsData = CommonUtils.getTxnDetailsDataFromCreditCard(txnDetailsCreditCardData);
                                            break;
                                        case FETCH_TRANSACTIONS_HISTORY_REMITTANCE_API:

                                            Log.e("sdcnsdjbcsd1",""+transactionType);

                                            if (transactionType == Constants.TRANSACTION_TYPE_VALUE) {
                                                startCount=response.getJSONArray(Constants.RESULT).getJSONObject(0).getString(Constants.AREX_PAGINATION_NO);
                                                CePaginationNo=response.getJSONArray(Constants.RESULT).getJSONObject(0).getString(Constants.CE_PAGINATION_NO);
                                                txnDetailsData = (ArrayList<TxnDetailsData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<TxnDetailsData>>() {
                                                }.getType());
                                            } else if (transactionType == Constants.TRANSACTION_TYPE_WU) {
                                                txnDetailsData = (ArrayList<TxnDetailsData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<TxnDetailsData>>() {
                                                }.getType());
                                            } else {
                                                ArrayList<TxnDetailsCeCashPayout> txnDetailsCeCashPayout = (ArrayList<TxnDetailsCeCashPayout>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<TxnDetailsCeCashPayout>>() {
                                                }.getType());
                                                txnDetailsData = CommonUtils.getTxnDetailsData(txnDetailsCeCashPayout);
                                            }
                                            break;
                                    }

                                    if (txnDetailsData != null && txnDetailsData.size() > 0) {
                                        recyclerAdapter.addArrayList(txnDetailsData);
                                        multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                                    } else {
                                        setViewState(VIEW_STATE_EMPTY);
                                    }
                                } else {
                                    setViewState(VIEW_STATE_EMPTY);
                                }
                            }else if(response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE)){
                                CommonUtils.showLimitDialog(context, response.getString(Constants.MESSAGE));

                            } else {
                                setViewState(VIEW_STATE_WRONG);
                            }
                        } else {
                            setViewState(VIEW_STATE_WRONG);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        setViewState(VIEW_STATE_EMPTY);
                    } finally {
                        onItemsLoadComplete();
                    }
                    break;
                case FETCH_WU_SEND_MONEY:
                    CommonUtils.hideLoading();
                    if (status == 1) {
                        try {
                            if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                                fetchWuTransactions();
                            } else {
                                CommonUtils.setViewState(multiStateView, VIEW_STATE_EMPTY, tvEmpty, null, response.getString(Constants.MESSAGE));
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            CommonUtils.hideLoading();
                        }
                    } else {
                        CommonUtils.hideLoading();
                    }
                    break;

                case LOAD_WC_TXN_HISTORY:
                    CommonUtils.hideLoading();
                    try {
                        if (status == 1) {
                            if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                                CommonUtils.hideLoading();
                                if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                    /*if (startCount.equalsIgnoreCase("1")) {
                                        recyclerAdapter.clear();
                                    }
                                    startCount = response.getString(Constants.NEXT_RECORD);*/
                                    ArrayList<TxnDetailsData> txnDetailsData = null;

                                    ArrayList<TxnDetailsHistroyTravelCard> txnDetailsTravelCardReload = (ArrayList<TxnDetailsHistroyTravelCard>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<TxnDetailsHistroyTravelCard>>() {}.getType());
                                    //txnDetailsData = CommonUtils.getTxnDetailsDataTravelCard(txnDetailsTravelCardReload);


                                 /*   if (txnDetailsTravelCardReload != null && txnDetailsTravelCardReload.size() > 0) {
                                        recyclerAdapter.addArrayList(txnDetailsTravelCardReload);
                                        multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                                    } else {*/
                                        setViewState(VIEW_STATE_EMPTY);
                                    //}
                                } else {
                                    setViewState(VIEW_STATE_EMPTY);
                                }
                            }else if(response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE)){
                                CommonUtils.showLimitDialog(context, response.getString(Constants.MESSAGE));

                            } else {
                                setViewState(VIEW_STATE_WRONG);
                            }
                        } else {
                            setViewState(VIEW_STATE_WRONG);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        setViewState(VIEW_STATE_EMPTY);
                    } finally {
                        onItemsLoadComplete();
                    }
                    break;


                default:
                    break;
            }
    }
}