package app.alansari.modules.sendmoney.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;

import app.alansari.AppController;
import app.alansari.R;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.OnLoadMoreListener;
import app.alansari.customviews.MultiStateView;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.TxnDetailsData;
import app.alansari.modules.sendmoney.adapters.PendingTransactionRecyclerAdapter;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.ALL_PENDING_TRANSACTION_API;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_EMPTY;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_ERROR;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_WRONG;

public class PendingTransFragment extends Fragment implements View.OnClickListener, OnWebServiceResult {

    private View view;
    private Intent intent;
    private String memberId;
    private String arexUserId;
    private String startCount = "1";
    private Context context;
    private String transactionType;
    private TextView tvEmpty, tvError;
    private ImageView ivError, ivEmpty;
    private MultiStateView multiStateView;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private PendingTransactionRecyclerAdapter recyclerAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(app.alansari.R.layout.recycler_view_full, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context = getActivity();
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


        memberId = (String) SharedPreferenceManger.getPrefVal(Constants.MEMBER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);

        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerAdapter = new PendingTransactionRecyclerAdapter(context, new ArrayList<TxnDetailsData>(), transactionType, recyclerView);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (Integer.parseInt(startCount) >= recyclerAdapter.getItemCount()) {
                    fetchAllPendingTransation();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        startCount = "1";
        fetchAllPendingTransation();
    }

    private void fetchAllPendingTransation() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);

            multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().pendingTransaction((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), startCount, LogoutCalling.getDeviceID(context),sessionTime), Constants.FETCH_ALL_PENDING_TXN_API_URL, ALL_PENDING_TRANSACTION_API, Request.Method.PUT, this);
            AppController.getInstance().addToRequestQueue(jsonObjReq, ALL_PENDING_TRANSACTION_API.toString());
        } else {
            CommonUtils.setViewState(multiStateView, VIEW_STATE_ERROR, tvError, ivError, null);
        }
    }

    void onItemsLoadComplete() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case app.alansari.R.id.empty_button:
            case app.alansari.R.id.error_button:
                fetchAllPendingTransation();
        }
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        switch (sType) {
            case ALL_PENDING_TRANSACTION_API:
                multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                try {
                    if (status == 1) {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                if (startCount.equalsIgnoreCase("1")) {
                                    recyclerAdapter.clear();
                                }
                                startCount = response.getString(Constants.NEXT_RECORD);
                                ArrayList<TxnDetailsData> txnDetailsData = null;
                                txnDetailsData = new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<TxnDetailsData>>() {}.getType());


                               // Log.e("vbjkfbjsbv",""+txnD);
                                if (txnDetailsData != null && txnDetailsData.size() > 0) {
                                    recyclerAdapter.addArrayList(txnDetailsData);
                                    multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                                } else {
                                    setViewState(VIEW_STATE_EMPTY);
                                }
                                CommonUtils.setTotalPendingTransactions(response.getString(Constants.ID));
                            } else {
                                CommonUtils.setTotalPendingTransactions(null);
                                setViewState(VIEW_STATE_EMPTY);
                            }
                        } else
                            setViewState(VIEW_STATE_EMPTY);

                    } else {
                        setViewState(VIEW_STATE_WRONG);
                    }
                } catch (Exception exp) {
                    exp.printStackTrace();
                    setViewState(VIEW_STATE_ERROR);
                }
                break;
        }
    }

    private void setViewState(int viewState) {
        if (isEmpty()) {
            if (viewState == VIEW_STATE_EMPTY)
                CommonUtils.setViewState(multiStateView, viewState, tvEmpty, null, "No transactions pending");
            CommonUtils.setTotalPendingTransactions("0");
            if (viewState == VIEW_STATE_ERROR)
                CommonUtils.setViewState(multiStateView, viewState, tvError, null, null);
            if (viewState == VIEW_STATE_WRONG)
                CommonUtils.setViewState(multiStateView, viewState, tvError, null, getString(R.string.error_something_wrong));
        } else {
            CommonUtils.setResponseToast(context, viewState);
        }
    }

    private Boolean isEmpty() {
        return recyclerAdapter.getItemCount() == 0;
    }
}
