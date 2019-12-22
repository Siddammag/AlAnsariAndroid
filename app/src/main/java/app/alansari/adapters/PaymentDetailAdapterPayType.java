package app.alansari.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;


import app.alansari.R;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.LogUtils;
import app.alansari.listeners.CustomClickListener;
import app.alansari.models.UserAccountData;

public class PaymentDetailAdapterPayType extends RecyclerView.Adapter<PaymentDetailAdapterPayType.PayTypeViewHolder> {
    private int selected = 0;
    private Context mContext;
    List<UserAccountData.PAYTYPEBTLISTItem> userAccountDataList,originalList;
    private LayoutInflater inflater;
    private CustomClickListener clickListener;
    private Dialog dialog;
    private String url;
    private UserAccountData selectedUserAccount;
    private WebView webView;
    private int adapterPosition=-1;


    public PaymentDetailAdapterPayType(Context context, ArrayList<UserAccountData.PAYTYPEBTLISTItem> userAccountData, UserAccountData selectedUserAccount, CustomClickListener clickListener) {
        this.mContext = context;
        this.userAccountDataList=userAccountData;
        this.originalList = new ArrayList<>();
        this.originalList.addAll(userAccountData);
        this.selectedUserAccount=selectedUserAccount;
        this.clickListener = clickListener;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public PaymentDetailAdapterPayType.PayTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payment_mode_adapter, parent, false);

        return new PaymentDetailAdapterPayType.PayTypeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PaymentDetailAdapterPayType.PayTypeViewHolder holder, int position) {
        UserAccountData.PAYTYPEBTLISTItem current = userAccountDataList.get(position);
        holder.title.setText(current.getDisplayValue());
        if (selected == position) {
            //updatePromoMessage(position);
            holder.imageView.setImageResource(R.drawable.ic_radio_button_checked_black_24dp);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_radio_button_unchecked_black_24dp);
        }

    }

    @Override
    public int getItemCount() {
        return userAccountDataList.size();
    }

    public void addArrayList(List<UserAccountData.PAYTYPEBTLISTItem> promoCodeList, UserAccountData selectedUserAccount) {
        this.userAccountDataList.clear();
        this.userAccountDataList = promoCodeList;
        this.selectedUserAccount=selectedUserAccount;
        notifyDataSetChanged();
    }
    public void clear() {
        this.userAccountDataList.clear();
        this.originalList.clear();
        notifyDataSetChanged();
    }



    public class PayTypeViewHolder extends RecyclerView.ViewHolder {
        public TextView title,tvLearnHow;
        public ImageView imageView;

        public PayTypeViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.name);
            imageView = (ImageView) view.findViewById(R.id.radio);
            tvLearnHow=(TextView)view.findViewById(R.id.learn_how);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected = getAdapterPosition();
                    updatePromoMessage(selected);
                    notifyDataSetChanged();
                }
            });
            tvLearnHow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterPosition=getAdapterPosition();
                    openDialogBox(adapterPosition);
                   /* if(userAccountDataList.get(adapterPosition).getPrimaryKeyValue().equalsIgnoreCase("OB"))
                        url=selectedUserAccount.getOBLEARNMOREURL();
                    else
                        url=selectedUserAccount.getFTLEARNMOREURL();

                    Log.e("cnashvcsgvc232",""+url);
                    mContext.startActivity(new Intent(mContext, MyWebviewDisplay.class).putExtra(Constants.URL,url));*/
                    dialog.show();
                }
            });
        }


    }

    private void updatePromoMessage(int adapterPosition) {
        clickListener.itemClicked(null, adapterPosition, userAccountDataList.get(adapterPosition));

    }
    private void openDialogBox(int adapterPosition) {

        dialog=new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_webview);
        dialog.setCanceledOnTouchOutside(false);
        webView=(WebView)dialog.findViewById(R.id.web_view);
        ImageView imageView=(ImageView)dialog.findViewById(R.id.close_btn);
//--------Adding Parameters-------------------------------------------------------------------------

        setWebView(webView);
       /* webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        webView.getSettings().setSupportMultipleWindows(false);
        webView.getSettings().setSupportZoom(false);

        webView.setVerticalScrollBarEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);

        webView.getSettings().setDomStorageEnabled(true);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyBrowser());*/


//--------------------------------------------------------------------------------------------------


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        try{
            if(userAccountDataList.get(adapterPosition).getPrimaryKeyValue().equalsIgnoreCase("OB"))
                url=selectedUserAccount.getOBLEARNMOREURL();
            else
                url=selectedUserAccount.getFTLEARNMOREURL();

            Log.e("cnashvcsgvc",""+url);

            webView.loadUrl(url);
            //webView.loadUrl("http://eexchange.ae/assets/img/Promo_Banner/HowtoFundTransfer.pdf");


            // dialog.show();



        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
    private void setWebView(WebView webView) {
        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        webView.getSettings().setSupportMultipleWindows(false);
        //webView.getSettings().setSupportZoom(false);

        webView.setVerticalScrollBarEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);

        webView.getSettings().setDomStorageEnabled(true);
        //webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);


    }
    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            LogUtils.d("DKG ok", "URL " + url+ " ");



        }

        @Override
        public void onPageFinished(WebView view, final String url) {
            try {
                CommonUtils.hideLoading();
            } catch (Exception e) {
                // Crashlytics.logException(new Exception("Web Error : " + e.getMessage().toString()));

            }
        }

        @Override
        public void onReceivedError(WebView webView, WebResourceRequest reques, WebResourceError error) {
            Crashlytics.logException(new Exception("Web Error : " + error.toString()));


        }

        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            try {
                CommonUtils.hideLoading();
            } catch (Exception e) {

            }

        }
    }

}



