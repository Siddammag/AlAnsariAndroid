package app.alansari;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONObject;

import java.util.ArrayList;

import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.Validation;
import app.alansari.adapters.LeftMenuAdapter;
import app.alansari.listeners.CustomClickListener;
import app.alansari.listeners.LogoutListeners;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.LeftMenu;
import app.alansari.modules.accountmanagement.AccountManagementActivity;
import app.alansari.modules.accountmanagement.BeneficiaryActivity;
import app.alansari.modules.branchlocator.BranchLocatorCityActivity;
import app.alansari.modules.currencycalculator.CurrencyConverterActivity;
import app.alansari.modules.currencycalculator.ForeignCurrencyActivity;
import app.alansari.modules.feedback.ContactUsActivity;
import app.alansari.modules.feedback.FaqActivity;
import app.alansari.modules.notification.NotificationActivity;
import app.alansari.modules.promotions.PromotionsActivity;
import app.alansari.modules.ratealert.RateAlertActivity;
import app.alansari.modules.sendmoney.PendingTransActivity;
import app.alansari.modules.sendmoney.TransactionHistoryActivity;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.newAdditions.Main2Activity;
import app.alansari.preferences.SharedPreferenceManger;
import app.alansari.residemenu.ResideMenu;
import app.alansari.TravelCardReloadActivity;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.LOGOUT;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.TRAVEL_CARD_RELOAD;

/**
 * Created by Parveen Dala on 21 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class NavigationBaseActivity extends AppCompatActivity implements CustomClickListener, OnWebServiceResult, LogoutListeners {
    private Intent intent;
    private Context context;
    private ResideMenu resideMenu;
    private ImageView ivLogo;
    private RecyclerView recyclerView;
    private LeftMenuAdapter leftMenuAdapter;
    private ArrayList<LeftMenu> leftMenuArrayList;
    private ResideMenu.OnMenuListener menuListener;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_base_activity);
        context = this;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        setResideMenu();
        // CommonUtils.checkConnection(context, resideMenu);
        //-------------------------------------DKG--------------------------------------------------


    }

    /*@Override
    public void onUserInteraction() {
        super.onUserInteraction();
        AppController.getInstance().userInteraction();
    }*/

    @Override
    protected void onStop() {
        super.onStop();
       /* AppController.getInstance().startUserSession();
        AppController.getInstance().registerSessionListeners(this);*/
    }





    private void setResideMenu() {
        leftMenuArrayList = new ArrayList<>();
        leftMenuArrayList.add(new LeftMenu("Send Money", R.drawable.ic_nav_money));
        leftMenuArrayList.add(new LeftMenu("Credit Card Payment", R.drawable.ic_nav_credit_card));
        leftMenuArrayList.add(new LeftMenu("Travel Card Reload", R.drawable.ic_nav_credit_card));
        leftMenuArrayList.add(new LeftMenu("Transaction Tracker", R.drawable.ic_nav_search));
        //leftMenuArrayList.add(new LeftMenu("My Emirates Id", R.drawable.ic_nav_credit_card));
        leftMenuArrayList.add(new LeftMenu("My Profile", R.drawable.ic_my_profile_icon));
        leftMenuArrayList.add(new LeftMenu("Beneficiary Management", R.drawable.ic_nav_user));
        leftMenuArrayList.add(new LeftMenu("Rate Alert", R.drawable.ic_nav_alarm));
        leftMenuArrayList.add(new LeftMenu("Pending Transactions", R.drawable.ic_nav_clock));
        leftMenuArrayList.add(new LeftMenu("Transaction History", R.drawable.ic_nav_list));
        leftMenuArrayList.add(new LeftMenu("Rate Calculator", R.drawable.ic_nav_currency_calculator));
        leftMenuArrayList.add(new LeftMenu("Foreign Currency", R.drawable.ic_nav_foreign_currency_exchange));
        leftMenuArrayList.add(new LeftMenu("Promotions", R.drawable.ic_nav_alarm));
        leftMenuArrayList.add(new LeftMenu("Notifications", R.drawable.ic_nav_notification));
        leftMenuArrayList.add(new LeftMenu("Contact Us", R.drawable.ic_nav_speech_bubble_with_text_lines));
        leftMenuArrayList.add(new LeftMenu("Branch Locator", R.drawable.nav_branch));
        leftMenuArrayList.add(new LeftMenu("FAQ", R.drawable.ic_nav_question));
        //leftMenuArrayList.add(new LeftMenu("My Profile", R.drawable.ic_my_profile_icon));
        leftMenuArrayList.add(new LeftMenu("Logout", R.drawable.ic_nav_logout));
        leftMenuArrayList.add(new LeftMenu(" App Version " + BuildConfig.VERSION_NAME, 0));


        resideMenu = new ResideMenu(context, -1, R.layout.residemenu_custom_right_scrollview);
        ivLogo = (ImageView) resideMenu.findViewById(R.id.logo);
        ivLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView) resideMenu.findViewById(R.id.recycler_view_right);
        leftMenuAdapter = new LeftMenuAdapter(context, leftMenuArrayList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(leftMenuAdapter);

        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);
        resideMenu.attachToActivity(this);
        resideMenu.setScaleValue(0.5f);

        menuListener = new ResideMenu.OnMenuListener() {
            @Override
            public void openMenu() {
            }

            @Override
            public void closeMenu() {
            }
        };
        resideMenu.setMenuListener(menuListener);

    }

    public void openMenuDrawer() {
        resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
    }

    @Override
    public void itemClicked(View view, int position, Object dataItem) {
        switch (position) {
            case 0:
                if (!(context instanceof BeneficiaryActivity)) {
                    intent = new Intent(context, BeneficiaryActivity.class);
                    intent.putExtra(Constants.INTENT_TYPE, Constants.ACTIVITY_FOR_SELECTION);
                    startActivity(intent);
                } else {
                    resideMenu.closeMenu();
                    return;
                }
                break;
            case 1:
                if (!(context instanceof CreditCardPaymentActivity))
                    startActivity(new Intent(context, CreditCardPaymentActivity.class));
                else {
                    resideMenu.closeMenu();
                    return;
                }


                break;
            case 2:
                if (!(context instanceof TravelCardReloadActivity))
                    //startActivity(new Intent(context, TravelCardReloadActivity.class));
                    checkApiToOpen();
                else {
                    resideMenu.closeMenu();
                    return;
                }

                break;
            case 3:
                if (!(context instanceof TransactionTrackerActivity)) {
                    mFirebaseAnalytics.logEvent("Prelogin_TransactionTracker", null);
                    Log.i("Prelogin_TransactionTracker", "Success in Naviga Screen");
                    startActivity(new Intent(context, TransactionTrackerActivity.class));
                }else {
                    resideMenu.closeMenu();
                    return;
                }

                break;
            case 4:
                /*if (!(context instanceof MyEmiratesIdActivity))
                    startActivity(new Intent(context, MyEmiratesIdActivity.class));
                else {
                    resideMenu.closeMenu();
                    return;
                }*/
                if (!(context instanceof MyProfileActivity))
                startActivity(new Intent(context, MyProfileActivity.class));
                else {
                resideMenu.closeMenu();
                return;
                }
                break;
            case 5:
                if (!(context instanceof AccountManagementActivity))
                    startActivity(new Intent(context, AccountManagementActivity.class));
                else {
                    resideMenu.closeMenu();
                    return;
                }
                break;

            case 6:
                if (!(context instanceof RateAlertActivity)) {

                    mFirebaseAnalytics.logEvent("Prelogin_RateCalculator", null);
                    Log.i("Prelogin_RateCalculator", "Success in Navigation Screen");
                    startActivity(new Intent(context, RateAlertActivity.class));
                }else {
                    resideMenu.closeMenu();
                    return;
                }
                break;

            case 7:
                if (!(context instanceof PendingTransActivity))
                    startActivity(new Intent(context, PendingTransActivity.class));
                else {
                    resideMenu.closeMenu();
                    return;
                }
                break;
            case 8:
                if (!(context instanceof TransactionHistoryActivity))
                    startActivity(new Intent(context, TransactionHistoryActivity.class));
                else {
                    resideMenu.closeMenu();
                    return;
                }
                break;
            case 9:
                if (!(context instanceof CurrencyConverterActivity))
                    startActivity(new Intent(context, CurrencyConverterActivity.class));
                else {
                    resideMenu.closeMenu();
                    return;
                }


                break;
            case 10:
                if (!(context instanceof ForeignCurrencyActivity))
                    startActivity(new Intent(context, ForeignCurrencyActivity.class));
                else {
                    resideMenu.closeMenu();
                    return;
                }

                break;
            case 11:
                if (!(context instanceof PromotionsActivity))
                    startActivity(new Intent(context, PromotionsActivity.class));
                else {
                    resideMenu.closeMenu();
                    return;
                }

                break;
            case 12:
                if (!(context instanceof NotificationActivity))
                    startActivity(new Intent(context, NotificationActivity.class));
                else {
                    resideMenu.closeMenu();
                    return;
                }

                break;
            case 13:
                if (!(context instanceof ContactUsActivity))
                    startActivity(new Intent(context, ContactUsActivity.class));
                else {
                    resideMenu.closeMenu();
                    return;
                }


                break;
            case 14:
                if (!(context instanceof BranchLocatorCityActivity))
                    startActivity(new Intent(context, BranchLocatorCityActivity.class));
                else {
                    resideMenu.closeMenu();
                    return;
                }

                break;
            case 15:
                if (!(context instanceof FaqActivity))
                    startActivity(new Intent(context, FaqActivity.class));
                else {
                    resideMenu.closeMenu();
                    return;
                }

                break;

            case 16:
               /* if (!(context instanceof MyProfileActivity))
                    startActivity(new Intent(context, MyProfileActivity.class));
                else {
                    resideMenu.closeMenu();
                    return;
                }*/
                new LogoutCalling().SessionTimeOutAPI(context);
                break;
            /*case 17:
                //logoutApi();
                //CommonUtils.registerAgain(context);
                new LogoutCalling().SessionTimeOutAPI(context);
                break;*/

            default:
                break;
        }
        if (!(context instanceof DashboardActivity) && !isTaskRoot()) {
            ((AppCompatActivity) context).finish();
        }

        resideMenu.closeMenu();
    }

    private void logoutApi() {
        String userId = (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        if (Validation.isValidString(userId)) {
            if (NetworkStatus.getInstance(context).isOnline2(context)) {
                JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().userPkIdSessionOut(userId, sessionTime, CommonUtils.getDeviceID(context)), Constants.LOGOUT, LOGOUT, Request.Method.PUT, this);
                AppController.getInstance().getRequestQueue().cancelAll(LOGOUT);
                AppController.getInstance().addToRequestQueue(jsonObjReq, LOGOUT.toString());
                CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), LOGOUT.toString(), false);
            } else {
                Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (resideMenu.isOpened()) {
            resideMenu.closeMenu();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //  AppController.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onSessionLogout() {
       /* finish();
        startActivity(new Intent(this, LandingActivity.class));*/
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        switch (sType) {
            case LOGOUT:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            CommonUtils.registerAgain(context);
                            Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Toast.makeText(context, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void checkApiToOpen() {
        String userId = (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchTravelCardList(CommonUtils.getUserId(), (String) SharedPreferenceManger.getPrefVal(Constants.AREX_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), LogoutCalling.getDeviceID(context), sessionTime), Constants.TRAVEL_CARD_RELOAD_URL, TRAVEL_CARD_RELOAD, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(TRAVEL_CARD_RELOAD.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, TRAVEL_CARD_RELOAD.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), TRAVEL_CARD_RELOAD.toString(), false);

        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }

    }
}
