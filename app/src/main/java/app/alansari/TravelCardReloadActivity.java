package app.alansari;


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

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;

import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.Utils.Validation;
import app.alansari.adapters.TravelCardReloadAdapter;
import app.alansari.customviews.MultiStateView;
import app.alansari.customviews.flatbutton.ButtonFlat;
import app.alansari.listeners.OnCustomClickListenerBoth;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.TravelCardReloadModel.TravelCardInfo;
import app.alansari.models.travalcardvalidateflag.TravelCardFlag;
import app.alansari.modules.travelcardreload.AddTravelCardReload;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.DELETE_TRAVEL_CARD;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.TRAVEL_CARD_RELOAD;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.VALIDATE_SELECT_WIRECARD;
import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_EMPTY;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_ERROR;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_WRONG;


public class TravelCardReloadActivity extends NavigationBaseActivity implements View.OnClickListener, OnCustomClickListenerBoth, OnWebServiceResult, LogOutTimerUtil.LogOutListener  {

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
    private ButtonFlat emptyButton;
    private TravelCardReloadAdapter travelCardReloadAdapter;
    private String responseProfileUpdateFlag;
    private String cardNumber,cardName;
    private String requestMode="delete";
    private String wcPkId;
    private TravelCardInfo travelCardInfo;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void init(){
        context=this;
        etSearch = (AutoCompleteTextView) findViewById(R.id.search_view);
        btnSearch = (ImageView) findViewById(R.id.search_btn);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(app.alansari.R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(CommonUtils.getPrimaryColor(context));
       // swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout();
        multiStateView = (MultiStateView) findViewById(app.alansari.R.id.multiState_rview);
        multiStateView.findViewById(app.alansari.R.id.empty_button).setOnClickListener(this);
        multiStateView.findViewById(app.alansari.R.id.error_button).setOnClickListener(this);
        tvEmpty = ((TextView) multiStateView.findViewById(app.alansari.R.id.empty_textView));
        tvError = ((TextView) multiStateView.findViewById(app.alansari.R.id.error_textView));
        recyclerView = (RecyclerView) findViewById(app.alansari.R.id.recyclerView);
        emptyButton = multiStateView.findViewById(app.alansari.R.id.empty_button);
        emptyButton.setOnClickListener(this);
        recyclerView.setPadding(0, 0, 0, 60);



        findViewById(R.id.fab).setOnClickListener(this);
        findViewById(R.id.nav_menu).setOnClickListener(this);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(app.alansari.R.layout.activity_travel_card_reload);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Toolbar toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        ((TextView) findViewById(R.id.toolbar_title)).setText("Travel Card Reload");
        findViewById(app.alansari.R.id.nav_menu).setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.ic_back_arrow);

        init();
        travelCardReloadAdapter = new TravelCardReloadAdapter(context, new ArrayList<TravelCardInfo>(), multiStateView, this);
        recyclerView.setAdapter(travelCardReloadAdapter);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        initSwipe();
        fetchTravelCardReload();


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
                    ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_title)).setText("Delete travel card?");
                    ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_text)).setText(context.getResources().getString(app.alansari.R.string.confirm_delete_travel_card));
                     myDialog.findViewById(app.alansari.R.id.dialog_btn_yes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cardNumber=travelCardReloadAdapter.getItemAt(viewHolder.getAdapterPosition()).getCARDNUMBER();
                            cardName=travelCardReloadAdapter.getItemAt(viewHolder.getAdapterPosition()).getCARDHOLDERNAME();
                            deletedCreditCardId = travelCardReloadAdapter.getItemAt(viewHolder.getAdapterPosition()).getWCPKID();
                            travelCardReloadAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                            deleteTravelCardReload();
                            myDialog.dismiss();
                        }
                    });

                    myDialog.findViewById(app.alansari.R.id.dialog_btn_no).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            travelCardReloadAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void searchAccount(String keyword) {
        travelCardReloadAdapter.getFilter().filter(keyword);
    }



    private void fetchTravelCardReload() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            if (!swipeRefreshLayout.isRefreshing()) {
                multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            }
            etSearch.setEnabled(false);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchTravelCardList(CommonUtils.getUserId(), (String) SharedPreferenceManger.getPrefVal(Constants.AREX_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), LogoutCalling.getDeviceID(context),sessionTime), Constants.TRAVEL_CARD_RELOAD_URL, TRAVEL_CARD_RELOAD, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(TRAVEL_CARD_RELOAD.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, TRAVEL_CARD_RELOAD.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), TRAVEL_CARD_RELOAD.toString(), false);

        } else {
            setViewState(VIEW_STATE_ERROR,"");
            //4000010010932110
            //4000010014855374
        }

    }

    private void deleteTravelCardReload() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            String userPkId=(String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);

            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().deleteTravelCardReload(userPkId,cardNumber,cardName,(String) SharedPreferenceManger.getPrefVal(Constants.AREX_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING),requestMode,deletedCreditCardId,LogoutCalling.getDeviceID(context),sessionTime), Constants.DELETE_TRAVEL_CARD_URL, DELETE_TRAVEL_CARD, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(DELETE_TRAVEL_CARD.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, DELETE_TRAVEL_CARD.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), DELETE_TRAVEL_CARD.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }
    }


    private void validateSelectWireCard(){
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            String userPkId=(String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);

            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().validateWireCard(userPkId,wcPkId,LogoutCalling.getDeviceID(context),sessionTime), Constants.VALIDATE_SELECT_WIRECARD_URL, VALIDATE_SELECT_WIRECARD, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(VALIDATE_SELECT_WIRECARD.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, VALIDATE_SELECT_WIRECARD.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), VALIDATE_SELECT_WIRECARD.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }
    }


    private void swipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkStatus.getInstance(context).isOnline2(context)) {
                    fetchTravelCardReload();
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
            case R.id.nav_menu:
                openMenuDrawer();
                break;
            case app.alansari.R.id.fab:
                intent = new Intent(context, AddTravelCardReload.class);
                intent.putExtra(Constants.PROFILE_UPDATE_FLAG,responseProfileUpdateFlag);
                startActivityForResult(intent, Constants.REQUEST_CODE_ADD_CREDIT_CARD);
                break;
            case app.alansari.R.id.empty_button:
            case app.alansari.R.id.error_button:
                fetchTravelCardReload();
                break;
            case app.alansari.R.id.search_btn:
                if (!TextUtils.isEmpty(etSearch.getText().toString().trim()) && etSearch.getText().toString().trim().length() > 0)
                    searchAccount(etSearch.getText().toString().trim());
                CommonUtils.hideKeyboard(context);
                break;
        }

    }

//diagAedAmount = response.getString(Constants.AED_AMOUNT);
//                                diagCharges = response.getString(Constants.TOTAL_PRIORITY_PAY_CHARGES_PP);
//                                diagTotalPay = response.getString(Constants.TOTAL_AMOUNT);
    private void setViewState(int viewState, String msg) {
        if (isEmpty()) {
            if (viewState == VIEW_STATE_EMPTY) {
                emptyButton.setVisibility(View.GONE);
                if(!msg.equalsIgnoreCase("" )|| msg.equalsIgnoreCase("null"))
                   CommonUtils.setViewState(multiStateView, viewState, tvEmpty, null, msg);
                else
                    CommonUtils.setViewState(multiStateView, viewState, tvEmpty, null, getString(R.string.empty_travel_card_list));

            }
            if (viewState == VIEW_STATE_WRONG)
                CommonUtils.setViewState(multiStateView, viewState, tvError, null, getString(R.string.error_travel_card_list));
            if (viewState == VIEW_STATE_ERROR)
                CommonUtils.setViewState(multiStateView, viewState, tvError, null, null);
        } else {
            CommonUtils.setResponseToast(context, viewState);
        }
    }
    private Boolean isEmpty() {
        return travelCardReloadAdapter.getItemCount() == 0;
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        super.onResponse(status, response, sType);
        switch (sType){
            case TRAVEL_CARD_RELOAD:
                CommonUtils.hideLoading();
                if(status==1){
                    try{
                        if(response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)){
                        if(response.getJSONArray(Constants.RESULT)!=null && response.getJSONArray(Constants.RESULT).length()>0){
                            responseProfileUpdateFlag=response.getString(Constants.PROFILE_UPDATE_FLAG);
                            ArrayList<TravelCardInfo> travelCardInfo= (ArrayList<TravelCardInfo>)new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(),new TypeToken<ArrayList<TravelCardInfo>>(){}.getType());
                            if(travelCardInfo.size()>0 && travelCardInfo !=null){
                                travelCardReloadAdapter.addArrayList(travelCardInfo);
                                multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                                etSearch.setEnabled(true);
                                return;
                            }
                        }
                        setViewState(VIEW_STATE_EMPTY,"");
                        }else if(response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE)){
                            setViewState(VIEW_STATE_EMPTY,response.getString(Constants.MESSAGE));
                        }
                        else {
                            setViewState(VIEW_STATE_WRONG,"");
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                        setViewState(VIEW_STATE_WRONG,"");
                    }
                    finally {
                        onItemsLoadComplete();
                    }
                }else{
                    setViewState(VIEW_STATE_WRONG,"");
                }
                break;
            case DELETE_TRAVEL_CARD:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            for (int i = 0; i < travelCardReloadAdapter.getItemCount(); i++) {
                                if (travelCardReloadAdapter.getItemAt(i).getWCPKID().equalsIgnoreCase(deletedCreditCardId)) {
                                    travelCardReloadAdapter.delete(i);
                                    if (isEmpty()) {
                                        //setViewState(VIEW_STATE_EMPTY,"");
                                        fetchTravelCardReload();
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
            case VALIDATE_SELECT_WIRECARD:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            ArrayList<TravelCardFlag> travelCardFlags= (ArrayList<TravelCardFlag>)new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(),new TypeToken<ArrayList<TravelCardFlag>>(){}.getType());
                            if(travelCardFlags.size()>0 && travelCardFlags !=null){

                                mFirebaseAnalytics.logEvent("WC_Initate", null);
                                Log.i("WC_Initate", "Success in WC_TravelCard");
                                //Siddu 123

                                Intent intent = new Intent(context, TravelCardReloadCurrencyActivity.class);
                                intent.putExtra(Constants.OBJECT, travelCardInfo);
                                intent.putExtra(Constants.PROFILE_UPDATE_FLAG,travelCardFlags);
                                context.startActivity(intent);
                                return;
                            }

                        } else if(response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE)) {
                            Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                        }else{
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
            if (requestCode == Constants.REQUEST_CODE_ADD_CREDIT_CARD && resultCode == RESULT_OK) {
                etSearch.setText("");
                fetchTravelCardReload();
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    @Override
    public void itemClickedListerners(View view, int position, Object dataItem) {
       // Log.e("gcghc",""+dataItem+" "+((TravelCardInfo) dataItem).getWCPKID());
        wcPkId=((TravelCardInfo) dataItem).getWCPKID();
        validateSelectWireCard();
        travelCardInfo= ((TravelCardInfo) dataItem);
        //TravelCardInfo  travelCardInfo=(TravelCardInfo) dataItem;
        //Log.e("jsfjkbsjkbvf",""+travelCardInfo.getWCPKID()+travelCardInfo.getCARDHOLDERNAME());

    }


}
