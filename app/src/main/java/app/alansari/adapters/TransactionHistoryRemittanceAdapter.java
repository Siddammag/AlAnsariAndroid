package app.alansari.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


import app.alansari.TransactionCompletedRemittanceActivity;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.OnLoadMoreListener;
import app.alansari.models.transactionhistroy.TxnDetailsHistroyTravelCard;
import app.alansari.modules.remittance.TXNHISTORYDATAItem;
import app.alansari.modules.remittance.TxnRemittanceModel;
import app.alansari.modules.sendmoney.TransactionCompletedActivity;
import app.alansari.textdrawable.ColorGenerator;
import app.alansari.textdrawable.TextDrawable;

public class TransactionHistoryRemittanceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public ArrayList<TXNHISTORYDATAItem> itemList;
    private Context context;
    private String transactionType;
    private LayoutInflater inflater;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean isLoading;
    private OnLoadMoreListener onLoadMoreListener;

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    public TransactionHistoryRemittanceAdapter(Context context, ArrayList<TXNHISTORYDATAItem> items, String transactionType, RecyclerView recyclerView) {
        this.context = context;
        this.itemList = items;

        this.transactionType = transactionType;
        inflater = LayoutInflater.from(context);

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (itemList.size() > 0) {
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        isLoading = true;
                    }
                }

            }
        });
    }

    public void delete(int position) {
        itemList.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(TXNHISTORYDATAItem data) {
        this.itemList.add(data);
        notifyItemInserted(itemList.size() - 1);
    }

    public void addItemAt(TXNHISTORYDATAItem data, int position) {
        this.itemList.add(position, data);
        notifyItemInserted(position);
    }

    public void replaceItemAt(TXNHISTORYDATAItem data, int position) {
        itemList.set(position, data);
        notifyItemChanged(position);
    }

    public void addItemAtCustomLLayout(TXNHISTORYDATAItem data, int position) {
        this.itemList.add(position, data);
        notifyDataSetChanged();
    }

    public ArrayList<TXNHISTORYDATAItem> getArrayList() {
        return itemList;
    }

    public TXNHISTORYDATAItem getItemAt(int position) {
        return itemList.get(position);
    }

    public void addArrayList(ArrayList<TXNHISTORYDATAItem> itemList) {
        isLoading = false;
        this.itemList.addAll(itemList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.itemList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TransactionViewHolder(inflater.inflate(app.alansari.R.layout.item_pending_transaction_remittance, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TransactionViewHolder) {

            TXNHISTORYDATAItem current = itemList.get(position);

            if (position % 2 == 0) {
                ((TransactionViewHolder) holder).bgLayout.setBackgroundColor(ContextCompat.getColor(context, app.alansari.R.color.colorWhite));
            } else {
                ((TransactionViewHolder) holder).bgLayout.setBackgroundColor(ContextCompat.getColor(context, app.alansari.R.color.colorF8F9FA));
            }


            if (current.getBENFNAME() != null && current.getBENFNAME().trim().length() > 0) {
                ((TransactionViewHolder) holder).tvName.setText(current.getBENFNAME());
                ((TransactionViewHolder) holder).ivProfilePic.setImageDrawable(TextDrawable
                        .builder()
                        .beginConfig()
                        .textColor(Color.WHITE)
                        .bold()
                        .useFont(Typeface.create("fonts/HelveticaNeue-Medium.ttf", Typeface.NORMAL))
                        .endConfig()
                        .buildRound(CommonUtils.getCharacters(current.getBENFNAME().trim()), ColorGenerator.MATERIAL.getRandomColor()));
            } else {
                ((TransactionViewHolder) holder).tvName.setText("");
            }

            if (String.valueOf(current.getBENEFBANK()) != null && String.valueOf(current.getBENEFBANK()).trim().length() > 0) {
                ((TransactionViewHolder) holder).tvBankName.setText(String.valueOf(current.getBENEFBANK()));
            } else {
                ((TransactionViewHolder) holder).tvBankName.setText("");
            }

            if (current.getTOTALTXNAMT() != null && current.getTOTALTXNAMT().trim().length() > 0) {
                ((TransactionViewHolder) holder).tvAmount.setText(CommonUtils.addCommaToString(current.getFCYAMT()));
            } else {
                ((TransactionViewHolder) holder).tvAmount.setText("");
            }

            if (current.getCCYDESC() != null && current.getCCYDESC().trim().length() > 0) {
                ((TransactionViewHolder) holder).tvCurrencyCode.setText(current.getCCYDESC());
            } else {
                ((TransactionViewHolder) holder).tvCurrencyCode.setText("");
            }
           /* if(current.get){
                ((TransactionViewHolder) holder).tvServiceType.setText("Travel Card Reload Payment");
            }else{
                ((TransactionViewHolder) holder).tvServiceType.setText("");

            }
            if(){
                ((TransactionViewHolder) holder).tvTransferType.setVisibility(View.VISIBLE);
            }else{
                ((TransactionViewHolder) holder).tvTransferType.setText("");
            }*/


            if (current.getTXNRECTYPE()!=null) {
                if(current.getTXNRECTYPE().equalsIgnoreCase("BT")){
                    if(current.getSERVICETYPE().equalsIgnoreCase("CE")){
                        ((TransactionViewHolder) holder).tvServiceType.setText("Instant Transfer");
                        ((TransactionViewHolder) holder).tvTransferType.setText("Bank Transfer");
                    }else{
                        ((TransactionViewHolder) holder).tvServiceType.setText("Value Transfer");
                        ((TransactionViewHolder) holder).tvTransferType.setText("Bank Transfer");
                    }

                }else{
                    if(current.getSERVICETYPE().equalsIgnoreCase("CE")){
                        ((TransactionViewHolder) holder).tvServiceType.setText("Instant Transfer");
                        ((TransactionViewHolder) holder).tvTransferType.setText("Cash Pickup");
                    }else{
                        ((TransactionViewHolder) holder).tvServiceType.setText("Value Transfer");
                        ((TransactionViewHolder) holder).tvTransferType.setText("Cash Pickup");
                    }
                }

            }


            if (String.valueOf(current.getCREATEDDATE()) != null && String.valueOf(current.getCREATEDDATE()).length() > 0) {
                try {
                    ((TransactionViewHolder) holder).tvDate.setText(CommonUtils.getDateFromMilliSec(Long.valueOf(String.valueOf(current.getCREATEDDATE()))));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    ((TransactionViewHolder) holder).tvDate.setText("");
                }
            } else {
                ((TransactionViewHolder) holder).tvDate.setText("");
            }

            if (current.getTXNSTATUS().equalsIgnoreCase(Constants.TRANSACTION_STATUS_COMPLETED) || current.getTXNSTATUS().equalsIgnoreCase(Constants.TRANSACTION_STATUS_PENDING)) {
                ((TransactionViewHolder) holder).ivTxnStatus.setImageResource(app.alansari.R.drawable.svg_success);
            } else if (current.getTXNSTATUS().equalsIgnoreCase(Constants.TRANSACTION_STATUS_EXPIRED)) {
                ((TransactionViewHolder) holder).ivTxnStatus.setImageResource(app.alansari.R.drawable.svg_rate_alert_close_icon);
            } else if (current.getTXNSTATUS().equalsIgnoreCase(Constants.TRANSACTION_STATUS_REJECTED)) {
                ((TransactionViewHolder) holder).ivTxnStatus.setImageResource(app.alansari.R.drawable.svg_rate_alert_close_icon);
            }
        }

    }

    class TransactionViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfilePic;
        ImageView ivTxnStatus;
        View bgLayout;
        TextView tvName, tvDate, tvBankName, tvServiceType, tvTransferType, tvAmount, tvCurrencyCode;

        public TransactionViewHolder(final View itemView) {
            super(itemView);
            bgLayout = itemView.findViewById(app.alansari.R.id.bg_layout);
            ivProfilePic = (ImageView) itemView.findViewById(app.alansari.R.id.profile_pic);
            ivTxnStatus = (ImageView) itemView.findViewById(app.alansari.R.id.status);
            tvName = (TextView) itemView.findViewById(app.alansari.R.id.name);
            tvDate = (TextView) itemView.findViewById(app.alansari.R.id.date);
            tvBankName = (TextView) itemView.findViewById(app.alansari.R.id.bank_name);
            tvServiceType = (TextView) itemView.findViewById(app.alansari.R.id.service_type);
            tvTransferType = (TextView) itemView.findViewById(app.alansari.R.id.transfer_type);
            tvAmount = (TextView) itemView.findViewById(app.alansari.R.id.amount);
            tvCurrencyCode = (TextView) itemView.findViewById(app.alansari.R.id.currency_code);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, TransactionCompletedRemittanceActivity.class);
                    intent.putExtra(Constants.OBJECT, itemList.get(getAdapterPosition()));
                    intent.putExtra(Constants.SOURCE, Constants.SOURCE_TRANSACTION_COMPLETED_LIST);
                    Log.e("ncjkbdcvsgc",""+itemList.get(getAdapterPosition()).getREJECTIONMESSAGE());
                    //intent.putExtra(Constants.SOURCE_TYPE, transactionType.equalsIgnoreCase(Constants.TRANSACTION_TYPE_CREDIT_CARD_PAYMENT) ? Constants.TYPE_CREDIT_CARD : Constants.TYPE_SEND_MONEY);
                    context.startActivity(intent);




                }
            });
        }
    }
}
