package app.alansari.modules.accountmanagement;

import app.alansari.AppController;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.Utils.Validation;
import app.alansari.customviews.MultiStateView;
import app.alansari.customviews.flatbutton.ButtonFlat;
import app.alansari.listeners.CustomClickListener;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.UserAccountData;
import app.alansari.modules.accountmanagement.adapters.BankAccountRecyclerAdapter;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;

/**
 * Created by Parveen Dala on 13 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class BankAccountActivity extends AppCompatActivity implements View.OnClickListener, CustomClickListener, OnWebServiceResult , LogOutTimerUtil.LogOutListener  {

    private Intent intent;
    private Context context;
    private ImageView btnSearch;
    private String deletedBankAccountId;
    private Paint paint = new Paint();
    private AutoCompleteTextView etSearch;
    private TextView tvEmpty, tvError;
    private RecyclerView recyclerView;
    private MultiStateView multiStateView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private BankAccountRecyclerAdapter recyclerAdapter;
    private ButtonFlat emptyButton;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void init() {
        etSearch = (AutoCompleteTextView) findViewById(app.alansari.R.id.search_view);
        btnSearch = (ImageView) findViewById(app.alansari.R.id.search_btn);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(app.alansari.R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(CommonUtils.getPrimaryColor(context));
        swipeRefreshLayout();
        multiStateView = (MultiStateView) findViewById(app.alansari.R.id.multiState_rview);
        emptyButton = multiStateView.findViewById(app.alansari.R.id.empty_button);
        multiStateView.findViewById(app.alansari.R.id.error_button).setOnClickListener(this);
        tvEmpty = ((TextView) multiStateView.findViewById(app.alansari.R.id.empty_textView));
        tvError = ((TextView) multiStateView.findViewById(app.alansari.R.id.error_textView));
        recyclerView = (RecyclerView) findViewById(app.alansari.R.id.recyclerView);
        recyclerView.setPadding(0, 0, 0, 60);

        findViewById(app.alansari.R.id.fab).setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        emptyButton.setOnClickListener(this);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Validation.isValidAutoCompleteTextValue(etSearch))
                    searchAccount(etSearch.getText().toString().trim());
                else
                    searchAccount("");
            }
        });

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (Validation.isValidAutoCompleteTextValue(etSearch))
                        searchAccount(etSearch.getText().toString().trim());
                    CommonUtils.hideKeyboard(context);
                    return true;
                }
                return false;
            }
        });
    }

    private void initSwipe() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT) {
                    final Dialog myDialog = new Dialog(context, app.alansari.R.style.CustomDialogThemeLightBg);
                    myDialog.setCanceledOnTouchOutside(true);
                    myDialog.setContentView(app.alansari.R.layout.confirm_delete_item_generic_dialog);
                    ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_title)).setText("Delete bank account?");
                    ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_text)).setText(context.getResources().getString(app.alansari.R.string.confirm_delete_bank_account));
                    myDialog.findViewById(app.alansari.R.id.dialog_btn_yes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deletedBankAccountId = recyclerAdapter.getItemAt(viewHolder.getAdapterPosition()).getId();
                            recyclerAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                            deleteBankAccount();
                            myDialog.dismiss();
                        }
                    });

                    myDialog.findViewById(app.alansari.R.id.dialog_btn_no).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            recyclerAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                            myDialog.dismiss();
                        }
                    });
                    myDialog.show();
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;
                    if (dX < 0) {
                        paint.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, paint);
                        Drawable drawable = getResources().getDrawable(app.alansari.R.drawable.svg_btn_delete);
                        icon = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(icon);
                        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                        drawable.draw(canvas);

                        // icon = BitmapFactory.decodeResource(getResources(), R.drawable.svg_btn_delete);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, paint);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(app.alansari.R.layout.bank_account_activity);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        ((TextView) findViewById(app.alansari.R.id.toolbar_title)).setText("Bank");
        ((TextView) findViewById(app.alansari.R.id.toolbar_title_2)).setText("Account");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.ic_back_arrow);
        findViewById(app.alansari.R.id.toolbar_layout).setBackground(ContextCompat.getDrawable(context, app.alansari.R.drawable.am_bank_account_header_bg));
        ((AppCompatImageView) findViewById(app.alansari.R.id.toolbar_right_icon)).setImageResource(app.alansari.R.drawable.svg_am_bank_account_icon);
        findViewById(app.alansari.R.id.nav_menu).setVisibility(View.GONE);
        CommonUtils.setStatusBarColor(getWindow(), ContextCompat.getColor(context, app.alansari.R.color.color024B54));
        //((AppCompatImageView) findViewById(R.id.toolbar_right_icon)).setImageResource(R.drawable.svg_am_bank_account_icon);
        findViewById(app.alansari.R.id.toolbar_right_icon).setOnClickListener(this);
        init();

        recyclerAdapter = new BankAccountRecyclerAdapter(context, new ArrayList<UserAccountData>(), multiStateView, this);
        recyclerView.setAdapter(recyclerAdapter);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        initSwipe();
        fetchBankAccounts();
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

    private void swipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkStatus.getInstance(context).isOnline2(context)) {
                    fetchBankAccounts();
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
            case app.alansari.R.id.fab:
                intent = new Intent(context, AddBankAccountActivity.class);
                startActivityForResult(intent, Constants.REQUEST_CODE_ADD_USER_ACCOUNT);
                break;
            case app.alansari.R.id.empty_button:
            case app.alansari.R.id.error_button:
                fetchBankAccounts();
                break;
            case app.alansari.R.id.search_btn:
                if (!TextUtils.isEmpty(etSearch.getText().toString().trim()) && etSearch.getText().toString().trim().length() > 0)
                    searchAccount(etSearch.getText().toString().trim());
                CommonUtils.hideKeyboard(context);
                break;
        }
    }

    @Override
    public void itemClicked(View view, int position, Object dataItem) {
        intent = new Intent(context, AddBankAccountActivity.class);
        intent.putExtra(Constants.OBJECT, (UserAccountData) dataItem);
        startActivityForResult(intent, Constants.REQUEST_CODE_ADD_USER_ACCOUNT);
    }

    private void fetchBankAccounts() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            if (!swipeRefreshLayout.isRefreshing()) {
                multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            }
            etSearch.setEnabled(false);
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchUserAccountList((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), (String) SharedPreferenceManger.getPrefVal(Constants.AREX_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), LogoutCalling.getDeviceID(context),sessionTime), Constants.FETCH_USER_ACCOUNTS_URL, CommonUtils.SERVICE_TYPE.FETCH_USER_ACCOUNTS, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.FETCH_USER_ACCOUNTS.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.FETCH_USER_ACCOUNTS.toString());
        } else {
            setViewState(MultiStateView.VIEW_STATE_ERROR);
        }
    }

    private void deleteBankAccount() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            String userPkId=(String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().deleteUserBankAccount(deletedBankAccountId,userPkId,LogoutCalling.getDeviceID(context),sessionTime), Constants.DELETE_USER_ACCOUNTS_URL, CommonUtils.SERVICE_TYPE.DELETE_USER_ACCOUNTS, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.DELETE_USER_ACCOUNTS.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.DELETE_USER_ACCOUNTS.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.DELETE_USER_ACCOUNTS.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    public void searchAccount(String keyword) {
        recyclerAdapter.getFilter().filter(keyword);
    }

    private Boolean isEmpty() {
        return recyclerAdapter.getItemCount() == 0;
    }

    private void setViewState(int viewState) {
        if (isEmpty()) {
            if (viewState == MultiStateView.VIEW_STATE_EMPTY) {
                emptyButton.setVisibility(View.GONE);
                CommonUtils.setViewState(multiStateView, viewState, tvEmpty, null, getString(app.alansari.R.string.empty_bank_account_list));
            }
            if (viewState == MultiStateView.VIEW_STATE_WRONG)
                CommonUtils.setViewState(multiStateView, viewState, tvError, null, getString(app.alansari.R.string.error_bank_account_list));
            if (viewState == MultiStateView.VIEW_STATE_ERROR)
                CommonUtils.setViewState(multiStateView, viewState, tvError, null, null);
        } else {
            CommonUtils.setResponseToast(context, viewState);
        }
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        switch (sType) {
            case FETCH_USER_ACCOUNTS:
                if (status == Constants.RESPONSE_DUMMY) {
                    Toast.makeText(context, getString(app.alansari.R.string.dummy_data), Toast.LENGTH_SHORT).show();
                    status = 1;
                    try {
                        addDummyData();
                        return;
                    } catch (Exception e) {
                        setViewState(MultiStateView.VIEW_STATE_WRONG);
                        e.printStackTrace();
                    }
                }
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<UserAccountData> userAccountData = (ArrayList<UserAccountData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<UserAccountData>>() {
                                }.getType());
                                if (userAccountData != null && userAccountData.size() > 0) {
                                    recyclerAdapter.addArrayList(userAccountData);
                                    multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                                    etSearch.setEnabled(true);
                                    return;
                                }
                            }
                            setViewState(MultiStateView.VIEW_STATE_EMPTY);
                        } else {
                            setViewState(MultiStateView.VIEW_STATE_WRONG);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        setViewState(MultiStateView.VIEW_STATE_WRONG);
                    } finally {
                        onItemsLoadComplete();
                    }
                } else {
                    setViewState(MultiStateView.VIEW_STATE_WRONG);
                }
                break;
            case DELETE_USER_ACCOUNTS:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            for (int i = 0; i < recyclerAdapter.getItemCount(); i++) {
                                if (recyclerAdapter.getItemAt(i).getId().equalsIgnoreCase(deletedBankAccountId)) {
                                    recyclerAdapter.delete(i);
                                    if (isEmpty()) {
                                        setViewState(MultiStateView.VIEW_STATE_EMPTY);
                                    }
                                    break;
                                }
                            }
                        } else {
                            Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == Constants.REQUEST_CODE_ADD_USER_ACCOUNT && resultCode == Activity.RESULT_OK) {
                UserAccountData userAccountData = data.getParcelableExtra(Constants.OBJECT);
                etSearch.setText("");
                if (userAccountData != null) {
                    recyclerAdapter.addItemAt(userAccountData, 0);
                    linearLayoutManager.scrollToPosition(0);
                } else {
                    swipeRefreshLayout.setRefreshing(true);
                    fetchBankAccounts();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addDummyData() {
        recyclerAdapter.clear();
        recyclerAdapter.addItem(new UserAccountData(0, ""));
        recyclerAdapter.addItem(new UserAccountData(1, ""));
        recyclerAdapter.addItem(new UserAccountData(2, ""));
        recyclerAdapter.addItem(new UserAccountData(0, ""));
        recyclerAdapter.addItem(new UserAccountData(1, ""));
        recyclerAdapter.addItem(new UserAccountData(2, ""));
        recyclerAdapter.addItem(new UserAccountData(0, ""));
        recyclerAdapter.addItem(new UserAccountData(1, ""));
        recyclerAdapter.addItem(new UserAccountData(2, ""));
        recyclerAdapter.addItem(new UserAccountData(0, ""));
        recyclerAdapter.addItem(new UserAccountData(1, ""));
        etSearch.setEnabled(true);
        multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        onItemsLoadComplete();
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