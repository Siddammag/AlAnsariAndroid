package app.alansari.modules.sendmoney.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.core.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.OnLoadMoreListener;
import app.alansari.Utils.Validation;
import app.alansari.models.TxnDetailsData;
import app.alansari.modules.sendmoney.TransactionCompletedActivity;
import app.alansari.textdrawable.ColorGenerator;
import app.alansari.textdrawable.TextDrawable;

/**
 * Created by Parveen Dala on 28 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class TransactionHistoryRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public ArrayList<TxnDetailsData> itemList;
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

    public TransactionHistoryRecyclerAdapter(Context context, ArrayList<TxnDetailsData> items, String transactionType, RecyclerView recyclerView) {
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

    public void addItem(TxnDetailsData data) {
        this.itemList.add(data);
        notifyItemInserted(itemList.size() - 1);
    }

    public void addItemAt(TxnDetailsData data, int position) {
        this.itemList.add(position, data);
        notifyItemInserted(position);
    }

    public void replaceItemAt(TxnDetailsData data, int position) {
        itemList.set(position, data);
        notifyItemChanged(position);
    }

    public void addItemAtCustomLLayout(TxnDetailsData data, int position) {
        this.itemList.add(position, data);
        notifyDataSetChanged();
    }

    public ArrayList<TxnDetailsData> getArrayList() {
        return itemList;
    }

    public TxnDetailsData getItemAt(int position) {
        return itemList.get(position);
    }

    public void addArrayList(ArrayList<TxnDetailsData> itemList) {
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

            TxnDetailsData current = itemList.get(position);

            if (position % 2 == 0) {
                ((TransactionViewHolder) holder).bgLayout.setBackgroundColor(ContextCompat.getColor(context, app.alansari.R.color.colorWhite));
            } else {
                ((TransactionViewHolder) holder).bgLayout.setBackgroundColor(ContextCompat.getColor(context, app.alansari.R.color.colorF8F9FA));
            }


            Log.e("jkcvshvdchsdvchs",""+transactionType);
            if (transactionType.equalsIgnoreCase("WU")) {
                if (current.getWuBeneficiaryName() != null && current.getWuBeneficiaryName().trim().length() > 0) {
                    ((TransactionViewHolder) holder).tvName.setText(current.getWuBeneficiaryName());
                    ((TransactionViewHolder) holder).ivProfilePic.setImageDrawable(TextDrawable
                            .builder()
                            .beginConfig()
                            .textColor(Color.WHITE)
                            .bold()
                            .useFont(Typeface.create("fonts/HelveticaNeue-Medium.ttf", Typeface.NORMAL))
                            .endConfig()
                            .buildRound(CommonUtils.getCharacters(current.getWuBeneficiaryName().trim()), ColorGenerator.MATERIAL.getRandomColor()));
                } else {
                    ((TransactionViewHolder) holder).tvName.setText("");
                }

                if (current.getWuBankName() != null && current.getWuBankName().trim().length() > 0) {
                    ((TransactionViewHolder) holder).tvBankName.setText(current.getWuBankName());
                } else {
                    ((TransactionViewHolder) holder).tvBankName.setText("");
                }

                if (current.getWuFcyAmt() != null && current.getWuFcyAmt().trim().length() > 0) {
                    ((TransactionViewHolder) holder).tvAmount.setText(CommonUtils.addCommaToString(current.getWuFcyAmt()));
                } else {
                    ((TransactionViewHolder) holder).tvAmount.setText("");
                }

                if (current.getWuCcyDesc() != null && current.getWuCcyDesc().trim().length() > 0) {
                    ((TransactionViewHolder) holder).tvCurrencyCode.setText(current.getWuCcyDesc());
                } else {
                    ((TransactionViewHolder) holder).tvCurrencyCode.setText("");
                }

                ((TransactionViewHolder) holder).tvServiceType.setText(current.getBranchCode());

                ((TransactionViewHolder) holder).tvTransferType.setText("");
            } else {


                if (current.getServiceType().equalsIgnoreCase(Constants.AREX_MAPPING) && current.getBeneficiaryData() == null && current.getTransactionData() == null && current.getTxnType().equalsIgnoreCase("TT")) {
                    current.setBeneficiaryData(current.getBeneficiaryDataTt());
                    current.setTransactionData(current.getTransactionDataTt());
                }

                if (current.getBeneficiaryData() != null && current.getBeneficiaryData().getBeneficiaryName() != null && current.getBeneficiaryData().getBeneficiaryName().trim().length() > 0) {
                    ((TransactionViewHolder) holder).tvName.setText(current.getBeneficiaryData().getBeneficiaryName());
                    ((TransactionViewHolder) holder).ivProfilePic.setImageDrawable(TextDrawable
                            .builder()
                            .beginConfig()
                            .textColor(Color.WHITE)
                            .bold()
                            .useFont(Typeface.create("fonts/HelveticaNeue-Medium.ttf", Typeface.NORMAL))
                            .endConfig()
                            .buildRound(CommonUtils.getCharacters(current.getBeneficiaryData().getBeneficiaryName().trim()), ColorGenerator.MATERIAL.getRandomColor()));
                } else if (current.getBeneficiaryData() != null && Validation.isValidString(current.getBeneficiaryData().getBeneficiaryNameAr())) {
                    ((TransactionViewHolder) holder).tvName.setText(current.getBeneficiaryData().getBeneficiaryNameAr().trim());
                    ((TransactionViewHolder) holder).ivProfilePic.setImageDrawable(TextDrawable
                            .builder()
                            .beginConfig()
                            .textColor(Color.WHITE)
                            .bold()
                            .useFont(Typeface.create("fonts/HelveticaNeue-Medium.ttf", Typeface.NORMAL))
                            .endConfig()
                            .buildRound(String.valueOf(""), ColorGenerator.MATERIAL.getRandomColor()));
                } else {
                    ((TransactionViewHolder) holder).tvName.setText("");
                }

                if (current.getBeneficiaryData() != null && current.getBeneficiaryData().getBankName() != null && current.getBeneficiaryData().getBankName().trim().length() > 0) {
                    ((TransactionViewHolder) holder).tvBankName.setText(current.getBeneficiaryData().getBankName());
                } else {
                    ((TransactionViewHolder) holder).tvBankName.setText("");
                }

                if (current.getTransactionData() != null && current.getTransactionData().getTxnAmount() != null && current.getTransactionData().getTxnAmount().trim().length() > 0) {
                    ((TransactionViewHolder) holder).tvAmount.setText(CommonUtils.addCommaToString(current.getTransactionData().getTxnAmount()));
                } else {
                    ((TransactionViewHolder) holder).tvAmount.setText("");
                }

                if (current.getTransactionData() != null && current.getTransactionData().getCcyName() != null && current.getTransactionData().getCcyName().trim().length() > 0) {
                    ((TransactionViewHolder) holder).tvCurrencyCode.setText(current.getTransactionData().getCcyName());
                } else {
                    ((TransactionViewHolder) holder).tvCurrencyCode.setText("");
                }

                if (current.getServiceType() != null && current.getServiceType().trim().length() > 0) {
                    if (transactionType.equalsIgnoreCase(Constants.TRANSACTION_TYPE_CREDIT_CARD_PAYMENT)) {
                        ((TransactionViewHolder) holder).tvServiceType.setText(current.getServiceType().equalsIgnoreCase(Constants.AREX_MAPPING) ? "Credit Card Bill Payment" : "Instant Transfer");
                    } else {
                        ((TransactionViewHolder) holder).tvServiceType.setText(current.getServiceType().equalsIgnoreCase(Constants.AREX_MAPPING) ? "Value Transfer" : "Instant Transfer");
                    }

                } else {
                    ((TransactionViewHolder) holder).tvServiceType.setText("");
                }

                if (current.getTxnRecType() != null && current.getTxnRecType().trim().length() > 0) {
                    if (transactionType.equalsIgnoreCase(Constants.TRANSACTION_TYPE_CREDIT_CARD_PAYMENT)) {
                        ((TransactionViewHolder) holder).tvTransferType.setText(current.getTxnRecType().equals(Constants.TRANSFER_TYPE_BANK_TRANSFER) ? "" : " Cash Pickup");
                    } else {
                        ((TransactionViewHolder) holder).tvTransferType.setText(current.getTxnRecType().equals(Constants.TRANSFER_TYPE_BANK_TRANSFER) ? " Bank Transfer" : " Cash Pickup");
                    }

                } else {
                    ((TransactionViewHolder) holder).tvTransferType.setText("");
                }
            }

            if (current.getCreatedDate() != null && current.getCreatedDate().trim().length() > 0) {
                try {
                    ((TransactionViewHolder) holder).tvDate.setText(CommonUtils.getDateFromMilliSec(Long.valueOf(current.getCreatedDate())));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    ((TransactionViewHolder) holder).tvDate.setText("");
                }
            } else {
                ((TransactionViewHolder) holder).tvDate.setText("");
            }

            if (current.getTxnStatus().equalsIgnoreCase(Constants.TRANSACTION_STATUS_COMPLETED) || current.getTxnStatus().equalsIgnoreCase(Constants.TRANSACTION_STATUS_PENDING)) {
                ((TransactionViewHolder) holder).ivTxnStatus.setImageResource(app.alansari.R.drawable.svg_success);
            } else if (current.getTxnStatus().equalsIgnoreCase(Constants.TRANSACTION_STATUS_EXPIRED)) {
                ((TransactionViewHolder) holder).ivTxnStatus.setImageResource(app.alansari.R.drawable.svg_rate_alert_close_icon);
            } else if (current.getTxnStatus().equalsIgnoreCase(Constants.TRANSACTION_STATUS_REJECTED)) {
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
                    Intent intent = new Intent(context, TransactionCompletedActivity.class);
                    intent.putExtra(Constants.OBJECT, itemList.get(getAdapterPosition()));
                    intent.putExtra(Constants.SOURCE, Constants.SOURCE_TRANSACTION_COMPLETED_LIST);
                    String name = itemList.get(getAdapterPosition()).getTxnType();
                    Log.e("owcjsbhb",""+name+" "+transactionType+" "+itemList.get(getAdapterPosition()));
//                    Log.e("owcjsbhb",""+name+" "+itemList.get(getAdapterPosition()).getTransactionData().getDoc_number());
                //    Log.e("owcjsbhb",""+name+" "+itemList.get(0).getTransactionData().getDoc_number());
                    if (transactionType.equalsIgnoreCase(Constants.TRANSACTION_TYPE_WU)) {
                        intent.putExtra(Constants.SOURCE_TYPE, Constants.TRANSACTION_TYPE_WU);
                    } else {

                        intent.putExtra(Constants.SOURCE_TYPE, transactionType.equalsIgnoreCase(Constants.TRANSACTION_TYPE_CREDIT_CARD_PAYMENT) ? Constants.TYPE_CREDIT_CARD : Constants.TYPE_SEND_MONEY);
                    }
                    context.startActivity(intent);


                   /* Intent intent = new Intent(context, Main2Activity.class);
                    intent.putExtra(Constants.OBJECT, itemList.get(getAdapterPosition()));
                    intent.putExtra(Constants.SOURCE, Constants.SOURCE_TRANSACTION_COMPLETED_LIST);
                    String name = itemList.get(getAdapterPosition()).getTxnType();
                    Log.e("owcjsbhb",""+name+" "+transactionType);
                    Log.e("acjsdjch",""+itemList.get(getAdapterPosition()).getTransactionData().getDoc_number());
                    if (transactionType.equalsIgnoreCase(Constants.TRANSACTION_TYPE_WU)) {
                        intent.putExtra(Constants.SOURCE_TYPE, Constants.TRANSACTION_TYPE_WU);
                    } else {

                        intent.putExtra(Constants.SOURCE_TYPE, transactionType.equalsIgnoreCase(Constants.TRANSACTION_TYPE_CREDIT_CARD_PAYMENT) ? Constants.TYPE_CREDIT_CARD : Constants.TYPE_SEND_MONEY);
                    }
                    context.startActivity(intent);*/



                }
            });
        }
    }
}