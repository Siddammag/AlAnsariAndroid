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
import app.alansari.models.CreditCardData;
import app.alansari.modules.accountmanagement.adapters.CreditCardRecyclerAdapter;
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
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
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

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.DELETE_CREDIT_CARDS;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_CREDIT_CARDS;
import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_EMPTY;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_ERROR;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_WRONG;

/**
 * Created by Parveen Dala on 19 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class CreditCardActivity extends AppCompatActivity implements View.OnClickListener, CustomClickListener, OnWebServiceResult , LogOutTimerUtil.LogOutListener  {

    private Intent intent;
    private Context context;
    private ImageView btnSearch;
    private String deletedCreditCardId;
    private Paint paint = new Paint();
    private AutoCompleteTextView etSearch;
    private TextView tvEmpty, tvError;
    private RecyclerView recyclerView;
    private MultiStateView multiStateView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private CreditCardRecyclerAdapter recyclerAdapter;
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
        multiStateView.findViewById(app.alansari.R.id.error_button).setOnClickListener(this);
        tvEmpty = ((TextView) multiStateView.findViewById(app.alansari.R.id.empty_textView));
        tvError = ((TextView) multiStateView.findViewById(app.alansari.R.id.error_textView));
        recyclerView = (RecyclerView) findViewById(app.alansari.R.id.recyclerView);
        emptyButton = multiStateView.findViewById(app.alansari.R.id.empty_button);
        emptyButton.setOnClickListener(this);
        recyclerView.setPadding(0, 0, 0, 60);
        findViewById(app.alansari.R.id.fab).setOnClickListener(this);
        btnSearch.setOnClickListener(this);

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
                    ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_title)).setText("Delete credit card?");
                    ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_text)).setText(context.getResources().getString(app.alansari.R.string.confirm_delete_credit_card));
                    myDialog.findViewById(app.alansari.R.id.dialog_btn_yes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deletedCreditCardId = recyclerAdapter.getItemAt(viewHolder.getAdapterPosition()).getId();
                            recyclerAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                            deleteCreditCard();
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
        setContentView(app.alansari.R.layout.credit_card_activity);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        ((TextView) findViewById(app.alansari.R.id.toolbar_title)).setText("Credit");
        ((TextView) findViewById(app.alansari.R.id.toolbar_title_2)).setText("Card");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.ic_back_arrow);
        findViewById(app.alansari.R.id.toolbar_layout).setBackground(ContextCompat.getDrawable(context, app.alansari.R.drawable.am_credit_card_header_bg));
        ((AppCompatImageView) findViewById(app.alansari.R.id.toolbar_right_icon)).setImageResource(app.alansari.R.drawable.svg_am_credit_card_icon);
        findViewById(app.alansari.R.id.nav_menu).setVisibility(View.GONE);
        CommonUtils.setStatusBarColor(getWindow(), ContextCompat.getColor(context, app.alansari.R.color.color1E6AB3));
        findViewById(app.alansari.R.id.toolbar_right_icon).setOnClickListener(this);
        init();

        recyclerAdapter = new CreditCardRecyclerAdapter(context, new ArrayList<CreditCardData>(), multiStateView, this);
        recyclerView.setAdapter(recyclerAdapter);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        initSwipe();
        fetchCreditCards();
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
                    fetchCreditCards();
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
                intent = new Intent(context, AddCreditCardActivity.class);
                startActivityForResult(intent, Constants.REQUEST_CODE_ADD_CREDIT_CARD);
                break;
            case app.alansari.R.id.empty_button:
            case app.alansari.R.id.error_button:
                fetchCreditCards();
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
        intent = new Intent(context, AddCreditCardActivity.class);
        intent.putExtra(Constants.OBJECT, (CreditCardData) dataItem);
        startActivityForResult(intent, Constants.REQUEST_CODE_ADD_CREDIT_CARD);
    }

    private void fetchCreditCards() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            if (!swipeRefreshLayout.isRefreshing()) {
                multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            }
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);

            etSearch.setEnabled(false);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchCreditCardList((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), (String) SharedPreferenceManger.getPrefVal(Constants.AREX_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), LogoutCalling.getDeviceID(context),sessionTime), Constants.FETCH_CREDIT_CARDS_URL, FETCH_CREDIT_CARDS, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(FETCH_CREDIT_CARDS.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, FETCH_CREDIT_CARDS.toString());
        } else {
            setViewState(VIEW_STATE_ERROR);
        }
    }

    private void deleteCreditCard() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            String userPkId=(String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().deleteCreditCard(deletedCreditCardId,userPkId,LogoutCalling.getDeviceID(context),sessionTime), Constants.DELETE_CREDIT_CARDS_URL, DELETE_CREDIT_CARDS, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(DELETE_CREDIT_CARDS.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, DELETE_CREDIT_CARDS.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), DELETE_CREDIT_CARDS.toString(), false);
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
            if (viewState == VIEW_STATE_EMPTY){
                emptyButton.setVisibility(View.GONE);
                CommonUtils.setViewState(multiStateView, viewState, tvEmpty, null, getString(app.alansari.R.string.empty_credit_card_list));}
            if (viewState == VIEW_STATE_WRONG)
                CommonUtils.setViewState(multiStateView, viewState, tvError, null, getString(app.alansari.R.string.error_credit_card_list));
            if (viewState == VIEW_STATE_ERROR)
                CommonUtils.setViewState(multiStateView, viewState, tvError, null, null);
        } else {
            CommonUtils.setResponseToast(context, viewState);
        }
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        switch (sType) {
            case FETCH_CREDIT_CARDS:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)){
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<CreditCardData> creditCardData = (ArrayList<CreditCardData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<CreditCardData>>() {}.getType());
                                if (creditCardData != null && creditCardData.size() > 0) {
                                    recyclerAdapter.addArrayList(creditCardData);
                                    multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                                    etSearch.setEnabled(true);
                                    return;
                                }
                            }
                            setViewState(VIEW_STATE_EMPTY);
                        } else {
                            setViewState(VIEW_STATE_WRONG);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        setViewState(VIEW_STATE_WRONG);
                    } finally {
                        onItemsLoadComplete();
                    }
                } else {
                    setViewState(VIEW_STATE_WRONG);
                }
                break;
            case DELETE_CREDIT_CARDS:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            for (int i = 0; i < recyclerAdapter.getItemCount(); i++) {
                                if (recyclerAdapter.getItemAt(i).getId().equalsIgnoreCase(deletedCreditCardId)) {
                                    recyclerAdapter.delete(i);
                                    if (isEmpty()) {
                                        setViewState(VIEW_STATE_EMPTY);
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
            if (requestCode == Constants.REQUEST_CODE_ADD_CREDIT_CARD && resultCode == Activity.RESULT_OK) {
                CreditCardData creditCardData = data.getParcelableExtra(Constants.OBJECT);
                etSearch.setText("");
                if (creditCardData != null) {
                    recyclerAdapter.addItemAt(creditCardData, 0);
                    linearLayoutManager.scrollToPosition(0);
                } else {
                    swipeRefreshLayout.setRefreshing(true);
                    fetchCreditCards();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  /*  private void addDummyData() {
        recyclerAdapter.clear();
        recyclerAdapter.addItem(new CreditCardData(0, ""));
        recyclerAdapter.addItem(new CreditCardData(1, ""));
        recyclerAdapter.addItem(new CreditCardData(2, ""));
        recyclerAdapter.addItem(new CreditCardData(3, ""));
        recyclerAdapter.addItem(new CreditCardData(4, ""));
        recyclerAdapter.addItem(new CreditCardData(5, ""));
        recyclerAdapter.addItem(new CreditCardData(6, ""));
        recyclerAdapter.addItem(new CreditCardData(7, ""));
        etSearch.setEnabled(true);
        multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        onItemsLoadComplete();
    }*/
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