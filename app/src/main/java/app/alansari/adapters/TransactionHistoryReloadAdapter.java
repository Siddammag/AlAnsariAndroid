package app.alansari.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.alansari.TransactionHistroyCompletedActivity;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.OnLoadMoreListener;


import app.alansari.models.transactionhistroy.TxnDetailsHistroyTravelCard;

import app.alansari.textdrawable.ColorGenerator;
import app.alansari.textdrawable.TextDrawable;

public class TransactionHistoryReloadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public ArrayList<TxnDetailsHistroyTravelCard> itemList;
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

    public TransactionHistoryReloadAdapter(Context context, ArrayList<TxnDetailsHistroyTravelCard> items, String transactionType, RecyclerView recyclerView) {
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

    public void addItem(TxnDetailsHistroyTravelCard data) {
        this.itemList.add(data);
        notifyItemInserted(itemList.size() - 1);
    }

    public void addItemAt(TxnDetailsHistroyTravelCard data, int position) {
        this.itemList.add(position, data);
        notifyItemInserted(position);
    }

    public void replaceItemAt(TxnDetailsHistroyTravelCard data, int position) {
        itemList.set(position, data);
        notifyItemChanged(position);
    }

    public void addItemAtCustomLLayout(TxnDetailsHistroyTravelCard data, int position) {
        this.itemList.add(position, data);
        notifyDataSetChanged();
    }

    public ArrayList<TxnDetailsHistroyTravelCard> getArrayList() {
        return itemList;
    }

    public TxnDetailsHistroyTravelCard getItemAt(int position) {
        return itemList.get(position);
    }

    public void addArrayList(ArrayList<TxnDetailsHistroyTravelCard> itemList) {
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
        return new TransactionViewHolder(inflater.inflate(app.alansari.R.layout.item_pending_transaction, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TransactionViewHolder) {

            TxnDetailsHistroyTravelCard current = itemList.get(position);

            if (position % 2 == 0) {
                ((TransactionViewHolder) holder).bgLayout.setBackgroundColor(ContextCompat.getColor(context, app.alansari.R.color.colorWhite));
            } else {
                ((TransactionViewHolder) holder).bgLayout.setBackgroundColor(ContextCompat.getColor(context, app.alansari.R.color.colorF8F9FA));
            }


            if (current.getCUSTOMERNAME() != null && current.getCUSTOMERNAME().trim().length() > 0) {
                ((TransactionViewHolder) holder).tvName.setText(current.getCUSTOMERNAME());
                ((TransactionViewHolder) holder).ivProfilePic.setImageDrawable(TextDrawable
                        .builder()
                        .beginConfig()
                        .textColor(Color.WHITE)
                        .bold()
                        .useFont(Typeface.create("fonts/HelveticaNeue-Medium.ttf", Typeface.NORMAL))
                        .endConfig()
                        .buildRound(CommonUtils.getCharacters(current.getCUSTOMERNAME().trim()), ColorGenerator.MATERIAL.getRandomColor()));
            } else {
                ((TransactionViewHolder) holder).tvName.setText("");
            }

            if (String.valueOf(current.getDOCNUMBER()) != null && String.valueOf(current.getDOCNUMBER()).trim().length() > 0) {
                ((TransactionViewHolder) holder).tvBankName.setText(String.valueOf(current.getDOCNUMBER()));
            } else {
                ((TransactionViewHolder) holder).tvBankName.setText("");
            }

                if (current.getTOTALAMOUNT() != null && current.getTOTALAMOUNT().trim().length() > 0) {
                    ((TransactionViewHolder) holder).tvAmount.setText(CommonUtils.addCommaToString(current.getTOTALAMOUNT()));
                } else {
                    ((TransactionViewHolder) holder).tvAmount.setText("");
                }

               /* if (current.getTransactionData() != null && current.getTransactionData().getCcyName() != null && current.getTransactionData().getCcyName().trim().length() > 0) {
                    ((TransactionHistoryReloadAdapter.TransactionViewHolder) holder).tvCurrencyCode.setText(current.getTransactionData().getCcyName());
                } else {*/
                    ((TransactionViewHolder) holder).tvCurrencyCode.setText("AED");
                //}

               /* if (current.getServiceType() != null && current.getServiceType().trim().length() > 0) {
                    if (transactionType.equalsIgnoreCase(Constants.TRANSACTION_TYPE_CREDIT_CARD_PAYMENT)) {
                        ((TransactionViewHolder) holder).tvServiceType.setText(current.getServiceType().equalsIgnoreCase(Constants.AREX_MAPPING) ? "Credit Card Bill Payment" : "Instant Transfer");
                    } else {
                        ((TransactionViewHolder) holder).tvServiceType.setText(current.getServiceType().equalsIgnoreCase(Constants.AREX_MAPPING) ? "Value Transfer" : "Instant Transfer");
                    }

                } else {*/
                    ((TransactionViewHolder) holder).tvServiceType.setText("Travel Card Reload Payment");
                //}

                    ((TransactionViewHolder) holder).tvTransferType.setVisibility(View.GONE);



            if (String.valueOf(current.getCREATEDDATE()) != null && String.valueOf(current.getCREATEDDATE()).length() > 0) {
                try {
                    ((TransactionViewHolder) holder).tvDate.setText(CommonUtils.getDateFromMilliSec(Long.valueOf(String.valueOf(current.getCREATEDDATE()))));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    ((TransactionHistoryReloadAdapter.TransactionViewHolder) holder).tvDate.setText("");
                }
            } else {
                ((TransactionHistoryReloadAdapter.TransactionViewHolder) holder).tvDate.setText("");
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
                    Intent intent = new Intent(context, TransactionHistroyCompletedActivity.class);
                    intent.putExtra(Constants.OBJECT, itemList.get(getAdapterPosition()));
                    //intent.putExtra(Constants.SOURCE, Constants.SOURCE_TRANSACTION_COMPLETED_LIST);
                    //intent.putExtra(Constants.SOURCE_TYPE, transactionType.equalsIgnoreCase(Constants.TRANSACTION_TYPE_CREDIT_CARD_PAYMENT) ? Constants.TYPE_CREDIT_CARD : Constants.TYPE_SEND_MONEY);
                    context.startActivity(intent);




                }
            });
        }
    }
}
