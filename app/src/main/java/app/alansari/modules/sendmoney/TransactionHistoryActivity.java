package app.alansari.modules.sendmoney;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import androidx.core.app.Fragment;
import androidx.core.app.FragmentManager;
import androidx.core.app.FragmentStatePagerAdapter;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.alansari.NavigationBaseActivity;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.fragments.TransactionHistoryReloadFragment;
import app.alansari.fragments.TransactionHistoryRemittanceFragment;
import app.alansari.modules.sendmoney.fragments.TransactionHistoryFragment;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;

/**
 * Created by Parveen Dala on 01 November, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class TransactionHistoryActivity extends NavigationBaseActivity implements  LogOutTimerUtil.LogOutListener  {
    private Context context;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FragmentPagerAdapter adapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(app.alansari.R.layout.pending_transaction_activity);
        context = this;
        toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        ((TextView) findViewById(app.alansari.R.id.toolbar_title)).setText("Transaction History");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.ic_back_arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        findViewById(app.alansari.R.id.nav_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenuDrawer();
            }
        });

        //Init
        tabLayout = (TabLayout) findViewById(app.alansari.R.id.tab_layout);
        viewPager = (ViewPager) findViewById(app.alansari.R.id.pager);
        adapter = new FragmentPagerAdapter(getSupportFragmentManager(), new String[]{"Remittance", "Credit Card Payment", "Western Union","Travel Card Reload"});
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        setupTabLayout();
    }

    private void setupTabLayout() {
      /*  LinearLayout newTab = (LinearLayout) LayoutInflater.from(this).inflate(app.alansari.R.layout.pending_transaction_tab_view, null);
        ((AppCompatTextView) newTab.findViewById(app.alansari.R.id.tab_text)).setText("Remittance");
        ((AppCompatImageView) newTab.findViewById(app.alansari.R.id.tab_icon)).setImageResource(app.alansari.R.drawable.ic_svg_send_money__black);
        ((AppCompatImageView) newTab.findViewById(app.alansari.R.id.tab_icon)).setVisibility(View.GONE);
        tabLayout.getTabAt(0).setCustomView(newTab);*/

        LinearLayout newTab2 = (LinearLayout) LayoutInflater.from(this).inflate(app.alansari.R.layout.pending_transaction_tab_view, null);
        ((AppCompatTextView) newTab2.findViewById(app.alansari.R.id.tab_text)).setText("Remittance");
        CommonUtils.setLayoutFont(context, "Roboto-Light.ttf", ((AppCompatTextView) newTab2.findViewById(app.alansari.R.id.tab_text)));
        ((AppCompatImageView) newTab2.findViewById(app.alansari.R.id.tab_icon)).setImageResource(app.alansari.R.drawable.ic_svg_credit_card_pay_black);
        ((AppCompatImageView) newTab2.findViewById(app.alansari.R.id.tab_icon)).setColorFilter(ContextCompat.getColor(context, app.alansari.R.color.color3F3F3F));
        ((AppCompatImageView) newTab2.findViewById(app.alansari.R.id.tab_icon)).setVisibility(View.GONE);
        tabLayout.getTabAt(0).setCustomView(newTab2);


        LinearLayout newTab3 = (LinearLayout) LayoutInflater.from(this).inflate(app.alansari.R.layout.pending_transaction_tab_view, null);
        ((AppCompatTextView) newTab3.findViewById(app.alansari.R.id.tab_text)).setText("Credit Card Payment");
        CommonUtils.setLayoutFont(context, "Roboto-Light.ttf", ((AppCompatTextView) newTab3.findViewById(app.alansari.R.id.tab_text)));
        ((AppCompatImageView) newTab3.findViewById(app.alansari.R.id.tab_icon)).setImageResource(app.alansari.R.drawable.ic_svg_credit_card_pay_black);
        ((AppCompatImageView) newTab3.findViewById(app.alansari.R.id.tab_icon)).setColorFilter(ContextCompat.getColor(context, app.alansari.R.color.color3F3F3F));
        ((AppCompatImageView) newTab3.findViewById(app.alansari.R.id.tab_icon)).setVisibility(View.GONE);
        tabLayout.getTabAt(1).setCustomView(newTab3);

        LinearLayout newTab4 = (LinearLayout) LayoutInflater.from(this).inflate(app.alansari.R.layout.pending_transaction_tab_view, null);
        ((AppCompatTextView) newTab4.findViewById(app.alansari.R.id.tab_text)).setText("Western Union");
        CommonUtils.setLayoutFont(context, "Roboto-Light.ttf", ((AppCompatTextView) newTab4.findViewById(app.alansari.R.id.tab_text)));
        ((AppCompatImageView) newTab4.findViewById(app.alansari.R.id.tab_icon)).setImageResource(app.alansari.R.drawable.ic_svg_credit_card_pay_black);
        ((AppCompatImageView) newTab4.findViewById(app.alansari.R.id.tab_icon)).setColorFilter(ContextCompat.getColor(context, app.alansari.R.color.color3F3F3F));
        ((AppCompatImageView) newTab4.findViewById(app.alansari.R.id.tab_icon)).setVisibility(View.GONE);
        tabLayout.getTabAt(2).setCustomView(newTab4);



        LinearLayout newTab5 = (LinearLayout) LayoutInflater.from(this).inflate(app.alansari.R.layout.pending_transaction_tab_view, null);
        ((AppCompatTextView) newTab5.findViewById(app.alansari.R.id.tab_text)).setText("Travel Card Reload");
        CommonUtils.setLayoutFont(context, "Roboto-Light.ttf", ((AppCompatTextView) newTab5.findViewById(app.alansari.R.id.tab_text)));
        ((AppCompatImageView) newTab5.findViewById(app.alansari.R.id.tab_icon)).setImageResource(app.alansari.R.drawable.ic_svg_credit_card_pay_black);
        ((AppCompatImageView) newTab5.findViewById(app.alansari.R.id.tab_icon)).setColorFilter(ContextCompat.getColor(context, app.alansari.R.color.color3F3F3F));
        ((AppCompatImageView) newTab5.findViewById(app.alansari.R.id.tab_icon)).setVisibility(View.GONE);
        tabLayout.getTabAt(3).setCustomView(newTab5);



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                CommonUtils.setLayoutFont(context, "Roboto-Regular.ttf", (AppCompatTextView) tab.getCustomView().findViewById(app.alansari.R.id.tab_text));
                ((AppCompatImageView) tab.getCustomView().findViewById(app.alansari.R.id.tab_icon)).setColorFilter(ContextCompat.getColor(context, app.alansari.R.color.colorBlack));

                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    if (i != tabLayout.getSelectedTabPosition()) {
                        CommonUtils.setLayoutFont(context, "Roboto-Light.ttf", (AppCompatTextView) tabLayout.getTabAt(i).getCustomView().findViewById(app.alansari.R.id.tab_text));
                        ((AppCompatImageView) tabLayout.getTabAt(i).getCustomView().findViewById(app.alansari.R.id.tab_icon)).setColorFilter(ContextCompat.getColor(context, app.alansari.R.color.color3F3F3F));
                    }
                }
                ((AppCompatImageView) tab.getCustomView().findViewById(app.alansari.R.id.tab_icon)).setVisibility(View.GONE);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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

    private class FragmentPagerAdapter extends FragmentStatePagerAdapter {
        Bundle bundle;
        Fragment[] fragmentArray;
        String[] titleArray;


        public FragmentPagerAdapter(FragmentManager fm, String[] titleArray) {
            super(fm);
            fragmentArray = new Fragment[titleArray.length];
            this.titleArray = titleArray;
        }

        public Fragment getFragment(int position) {
            return fragmentArray[position];
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    fragmentArray[0] = new TransactionHistoryRemittanceFragment();
                    bundle = new Bundle();
                    bundle.putString(Constants.TYPE, Constants.TRANSACTION_TYPE_VALUE);
                    // bundle.putString("Type","VALUE");
                    Log.e("sdcnsdjbcsd1",Constants.TRANSACTION_TYPE_VALUE);
                    fragmentArray[0].setArguments(bundle);
                    return fragmentArray[0];
              /*  case 1:
                    fragmentArray[1] = new TransactionHistoryFragment();
                    bundle = new Bundle();
                    bundle.putString(Constants.TYPE, Constants.TRANSACTION_TYPE_INSTANT);
                    // bundle.putString("Type","INSTANT");
                    fragmentArray[1].setArguments(bundle);
                    return fragmentArray[1];*/
                case 1:
                    fragmentArray[1] = new TransactionHistoryFragment();
                    bundle = new Bundle();
                    bundle.putString(Constants.TYPE, Constants.TRANSACTION_TYPE_CREDIT_CARD_PAYMENT);
                    // bundle.putString("Type","CREDIT_CARD_PAYMENT");
                    Log.e("sdcnsdjbcsd1",""+ Constants.TRANSACTION_TYPE_CREDIT_CARD_PAYMENT);
                    fragmentArray[1].setArguments(bundle);
                    return fragmentArray[1];
                case 2:
                    fragmentArray[2] = new TransactionHistoryFragment();
                    bundle = new Bundle();
                    bundle.putString(Constants.TYPE, "WU");
                    Log.e("sdcnsdjbcsd1",""+"WU");
                    // bundle.putString("Type","WU");
                    fragmentArray[2].setArguments(bundle);
                    return fragmentArray[2];

                case 3:
                    fragmentArray[3] = new TransactionHistoryReloadFragment();
                    bundle = new Bundle();
                    bundle.putString(Constants.TYPE, Constants.TRANSACTION_TYPE_TRAVEL_CARD);
                    // bundle.putString("Type","WU");
                    Log.e("sdcnsdjbcsd1",Constants.TRANSACTION_TYPE_TRAVEL_CARD);
                    fragmentArray[3].setArguments(bundle);
                    return fragmentArray[3];

                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleArray[position];
        }

        @Override
        public int getCount() {
            return titleArray.length;
        }
    }

    public int getAdapterPosition() {
        return tabLayout.getSelectedTabPosition();
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