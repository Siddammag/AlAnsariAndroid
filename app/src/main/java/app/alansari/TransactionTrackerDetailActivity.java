package app.alansari;

import android.content.Context;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.models.transactiontracker.TrackerResult;

public class TransactionTrackerDetailActivity extends NavigationBaseActivity implements View.OnClickListener{

    private ArrayList<TrackerResult> trackerResults;
    //88023000006654
    private View vTransCreated, vTransVerified, vTransProcessed;
    private ImageView ivTransCreated, ivTransVerified, ivTransProcessed, ivTransRecieved;
    private TextView tvTransCreated, tvTransVerified, tvTransProcessed, tvTransRecieved;
    private LinearLayout linLayTxtName, linLayLayout;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_tracker_detail);
        Toolbar toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        ((TextView) findViewById(app.alansari.R.id.toolbar_title)).setText("Transaction");
        ((TextView) findViewById(app.alansari.R.id.toolbar_title_2)).setText("Tracker");
        ((TextView) findViewById(app.alansari.R.id.toolbar_title_2)).setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(R.dimen.dimens_5sp));
        findViewById(app.alansari.R.id.nav_menu).setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        init();
        findViewById(app.alansari.R.id.toolbar_layout).setBackground(ContextCompat.getDrawable(context, app.alansari.R.drawable.am_bank_account_header_bg));
        ((AppCompatImageView) findViewById(app.alansari.R.id.toolbar_right_icon)).setImageResource(app.alansari.R.drawable.svg_am_bank_account_icon);
        findViewById(app.alansari.R.id.toolbar_right_icon).setOnClickListener(this);
        CommonUtils.setStatusBarColor(getWindow(), ContextCompat.getColor(context, app.alansari.R.color.color024B54));

        trackerResults = getIntent().getExtras().getParcelableArrayList(Constants.LIST);

        ((TextView) findViewById(R.id.trans_number)).setText(getIntent().getStringExtra(Constants.TRANSACTION_TYPE_VALUE));
        for (int i = 0; i < trackerResults.size(); i++) {
            if (trackerResults.get(i).getACTIVEIND().equalsIgnoreCase("Y")) {
                Log.e("vkbdzhvchd", "" + trackerResults.get(i).getMESSAGE());
                ((TextView) findViewById(R.id.trans_status)).setText(trackerResults.get(i).getMESSAGE());

            }
        }


        switch (trackerResults.size()) {
            case 1:
                linLayTxtName.setVisibility(View.GONE);
                linLayLayout.setVisibility(View.GONE);
                return;
            case 2:
                tvTransCreated.setVisibility(View.VISIBLE);
                tvTransVerified.setVisibility(View.VISIBLE);
                tvTransProcessed.setVisibility(View.GONE);
                tvTransRecieved.setVisibility(View.GONE);
                ivTransCreated.setVisibility(View.VISIBLE);
                ivTransVerified.setVisibility(View.VISIBLE);
                ivTransProcessed.setVisibility(View.GONE);
                ivTransRecieved.setVisibility(View.GONE);
                vTransCreated.setVisibility(View.VISIBLE);
                vTransVerified.setVisibility(View.GONE);
                vTransProcessed.setVisibility(View.GONE);


                if (trackerResults.get(0).getACTIVEIND().equalsIgnoreCase("Y")) {
                    ivTransCreated.setImageDrawable(getResources().getDrawable(R.drawable.circle_fill_with_strike));
                } else {
                    ivTransCreated.setImageDrawable(getResources().getDrawable(R.drawable.circle_empty_with_strike));

                }
                if (trackerResults.get(1).getACTIVEIND().equalsIgnoreCase("Y")) {
                    ivTransVerified.setImageDrawable(getResources().getDrawable(R.drawable.circle_fill_with_strike));
                } else {
                    ivTransVerified.setImageDrawable(getResources().getDrawable(R.drawable.circle_empty_with_strike));

                }
                return;
            case 3:
                tvTransCreated.setVisibility(View.VISIBLE);
                tvTransVerified.setVisibility(View.VISIBLE);
                tvTransProcessed.setVisibility(View.VISIBLE);
                tvTransRecieved.setVisibility(View.GONE);
                ivTransCreated.setVisibility(View.VISIBLE);
                ivTransVerified.setVisibility(View.VISIBLE);
                ivTransProcessed.setVisibility(View.VISIBLE);
                ivTransRecieved.setVisibility(View.GONE);
                vTransCreated.setVisibility(View.VISIBLE);
                vTransVerified.setVisibility(View.VISIBLE);
                vTransProcessed.setVisibility(View.GONE);


                if (trackerResults.get(0).getACTIVEIND().equalsIgnoreCase("Y")) {
                    ivTransCreated.setImageDrawable(getResources().getDrawable(R.drawable.circle_fill_with_strike));
                } else {
                    ivTransCreated.setImageDrawable(getResources().getDrawable(R.drawable.circle_empty_with_strike));

                }
                if (trackerResults.get(1).getACTIVEIND().equalsIgnoreCase("Y")) {
                    ivTransVerified.setImageDrawable(getResources().getDrawable(R.drawable.circle_fill_with_strike));
                } else {
                    ivTransVerified.setImageDrawable(getResources().getDrawable(R.drawable.circle_empty_with_strike));

                }
                if (trackerResults.get(2).getACTIVEIND().equalsIgnoreCase("Y")) {
                    ivTransProcessed.setImageDrawable(getResources().getDrawable(R.drawable.circle_fill_with_strike));
                } else {
                    ivTransProcessed.setImageDrawable(getResources().getDrawable(R.drawable.circle_empty_with_strike));

                }

                return;
            case 4:

                tvTransCreated.setVisibility(View.VISIBLE);
                tvTransVerified.setVisibility(View.VISIBLE);
                tvTransProcessed.setVisibility(View.VISIBLE);
                tvTransRecieved.setVisibility(View.VISIBLE);
                ivTransCreated.setVisibility(View.VISIBLE);
                ivTransVerified.setVisibility(View.VISIBLE);
                ivTransProcessed.setVisibility(View.VISIBLE);
                ivTransRecieved.setVisibility(View.VISIBLE);
                vTransCreated.setVisibility(View.VISIBLE);
                vTransVerified.setVisibility(View.VISIBLE);
                vTransProcessed.setVisibility(View.VISIBLE);


                if (trackerResults.get(0).getACTIVEIND().equalsIgnoreCase("Y")) {
                    ivTransCreated.setImageDrawable(getResources().getDrawable(R.drawable.circle_fill_with_strike));
                } else {
                    ivTransCreated.setImageDrawable(getResources().getDrawable(R.drawable.circle_empty_with_strike));

                }
                if (trackerResults.get(1).getACTIVEIND().equalsIgnoreCase("Y")) {
                    ivTransVerified.setImageDrawable(getResources().getDrawable(R.drawable.circle_fill_with_strike));
                } else {
                    ivTransVerified.setImageDrawable(getResources().getDrawable(R.drawable.circle_empty_with_strike));

                }
                if (trackerResults.get(2).getACTIVEIND().equalsIgnoreCase("Y")) {
                    ivTransProcessed.setImageDrawable(getResources().getDrawable(R.drawable.circle_fill_with_strike));
                } else {
                    ivTransProcessed.setImageDrawable(getResources().getDrawable(R.drawable.circle_empty_with_strike));

                }

                if (trackerResults.get(3).getACTIVEIND().equalsIgnoreCase("Y")) {
                    ivTransRecieved.setImageDrawable(getResources().getDrawable(R.drawable.circle_fill_with_strike));
                } else {
                    ivTransRecieved.setImageDrawable(getResources().getDrawable(R.drawable.circle_empty_with_strike));

                }
                return;
            default:
                return;

        }


    }

    public void init() {
        context=this;
        linLayTxtName = (LinearLayout) findViewById(R.id.lin_lay_txt_name);
        linLayLayout = (LinearLayout) findViewById(R.id.lin_lay_layout);

        tvTransCreated = (TextView) findViewById(R.id.trans_created);
        tvTransVerified = (TextView) findViewById(R.id.trans_verified);
        tvTransProcessed = (TextView) findViewById(R.id.trans_processed);
        tvTransRecieved = (TextView) findViewById(R.id.trans_recieved);

        ivTransCreated = (ImageView) findViewById(R.id.iv_trans_created);
        ivTransVerified = (ImageView) findViewById(R.id.iv_trans_verified);
        ivTransProcessed = (ImageView) findViewById(R.id.iv_trans_processed);
        ivTransRecieved = (ImageView) findViewById(R.id.iv_trans_recieved);

        vTransCreated = (View) findViewById(R.id.v_trans_created);
        vTransVerified = (View) findViewById(R.id.v_trans_verified);
        vTransProcessed = (View) findViewById(R.id.v_trans_processed);
        ((Button)findViewById(R.id.track_transfer)).setOnClickListener(this);


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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case app.alansari.R.id.nav_menu:
                openMenuDrawer();
                break;
            case R.id.track_transfer:
                onBackPressed();
                break;
        }
    }
}
