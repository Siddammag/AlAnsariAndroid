package app.alansari.modules.sendmoney.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.alansari.TransactionTravelDetailsActivity;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.OnLoadMoreListener;
import app.alansari.Utils.Validation;
import app.alansari.models.TxnDetailsData;
import app.alansari.modules.sendmoney.TransactionDetailsActivity;
import app.alansari.textdrawable.ColorGenerator;
import app.alansari.textdrawable.TextDrawable;

/**
 * Created by Parveen Dala on 28 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class PendingTransactionRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public ArrayList<TxnDetailsData> itemList;
    private Context context;
    private String transactionType;
    private LayoutInflater inflater;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean isLoading;
    private OnLoadMoreListener onLoadMoreListener;

    public PendingTransactionRecyclerAdapter(Context context, ArrayList<TxnDetailsData> itemList, String transactionType, RecyclerView recyclerView) {
        this.context = context;
        this.itemList = itemList;
        this.transactionType = transactionType;
        inflater = LayoutInflater.from(context);

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });

    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TransactionViewHolder(inflater.inflate(app.alansari.R.layout.item_pending_transaction, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TransactionViewHolder) {
            TxnDetailsData current = itemList.get(position);
            transactionType = current.getTxnType();
            Log.e("njkabxjab", "" + transactionType);
            if (position % 2 == 0) {
                ((TransactionViewHolder) holder).bgLayout.setBackgroundColor(ContextCompat.getColor(context, app.alansari.R.color.colorWhite));
            } else {
                ((TransactionViewHolder) holder).bgLayout.setBackgroundColor(ContextCompat.getColor(context, app.alansari.R.color.colorF8F9FA));
            }
            switch (transactionType) {
                case Constants.TXN_TYPE_VALUE:
                    current.setBeneficiaryData(current.getBeneficiaryDataTt());
                    current.setTransactionData(current.getTransactionDataTt());
                    break;
                case Constants.TXN_TYPE_INSTANT:
                    current.setBeneficiaryData(current.getBeneficiaryDataTCE());
                    current.setTransactionData(current.getTransactionDataCE());
                    break;
                case Constants.TXN_TYPE_CREDIT_CARD:
                    current.setBeneficiaryData(current.getBeneficiaryDataTCC());
                    current.setTransactionData(current.getTransactionDataCC());
                    break;
                case Constants.TXN_REC_TYPE_VALUE:
                    current.setBeneficiaryData(current.getBeneficiaryDataBTCE());
                    current.setTransactionData(current.getTransactionDataBTCE());
                    break;

                case Constants.TXN_TYPE_TRAVEL_CARD:
                    current.setTransactionHistroyData(current.getTransactionHistroyData());
                    break;
                case Constants.TXN_TYPE_WESTERN:
                    String name = "";
                    if (current.getBeneficiaryDataWu() != null && current.getBeneficiaryDataWu().getReceiverNameType() != null && current.getBeneficiaryDataWu().getReceiverFirstName() != null) {
                        if (current.getBeneficiaryDataWu().getReceiverNameType().equalsIgnoreCase("D")) {
                            name = (CommonUtils.getValidString(current.getBeneficiaryDataWu().getReceiverFirstName()) + " " + CommonUtils.getValidString(current.getBeneficiaryDataWu().getReceiverMiddleName()) + " " + CommonUtils.getValidString(current.getBeneficiaryDataWu().getReceiverLastName()));
                        } else {
                            name = (CommonUtils.getValidString(current.getBeneficiaryDataWu().getReceiverFirstName()) + " " + CommonUtils.getValidString(current.getBeneficiaryDataWu().getReceiverLastName()) + " " + CommonUtils.getValidString(current.getBeneficiaryDataWu().getReceiverMiddleName()));
                        }
                    }
                    if (Validation.isValidString(name)) {
                        ((TransactionViewHolder) holder).tvName.setText(name);

                        ((TransactionViewHolder) holder).ivProfilePic.setImageDrawable(TextDrawable
                                .builder()
                                .beginConfig()
                                .textColor(Color.WHITE)
                                .bold()
                                .useFont(Typeface.create("fonts/HelveticaNeue-Medium.ttf", Typeface.NORMAL))
                                .endConfig()
                                .buildRound(CommonUtils.getCharacters(name), ColorGenerator.MATERIAL.getRandomColor()));
                    } else {
                        ((TransactionViewHolder) holder).tvName.setText("");
                        ((TransactionViewHolder) holder).ivProfilePic.setImageDrawable(TextDrawable
                                .builder()
                                .beginConfig()
                                .textColor(Color.WHITE)
                                .bold()
                                .useFont(Typeface.create("fonts/HelveticaNeue-Medium.ttf", Typeface.NORMAL))
                                .endConfig()
                                .buildRound("", ColorGenerator.MATERIAL.getRandomColor()));
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

                    ((TransactionViewHolder) holder).tvBankName.setText("WESTERN UNION");

                    if (current.getTransactionDataWu() != null && current.getTransactionDataWu().getTotalTxnAmount() != null && current.getTransactionDataWu().getTotalTxnAmount().trim().length() > 0) {
                        ((TransactionViewHolder) holder).tvAmount.setText(CommonUtils.addCommaToString(current.getTransactionDataWu().getSendAmount()));
                    } else {
                        ((TransactionViewHolder) holder).tvAmount.setText("");
                    }

                    if (current.getTransactionDataWu() != null && current.getTransactionDataWu().getCurrencyDesc() != null && current.getTransactionDataWu().getCurrencyDesc().trim().length() > 0) {
                        ((TransactionViewHolder) holder).tvCurrencyCode.setText(current.getTransactionDataWu().getCurrencyDesc());
                    } else {
                        ((TransactionViewHolder) holder).tvCurrencyCode.setText("");
                    }

                    ((TransactionViewHolder) holder).tvServiceType.setText("Payment by ");
                    break;
            }

            //Log.e("fhsjfvhdu", "" + transactionType);
            if (current.getBeneficiaryData() != null && Validation.isValidString(current.getBeneficiaryData().getBeneficiaryName())) {
                ((TransactionViewHolder) holder).tvName.setText(current.getBeneficiaryData().getBeneficiaryName());
                ((TransactionViewHolder) holder).ivProfilePic.setImageDrawable(TextDrawable
                        .builder()
                        .beginConfig()
                        .textColor(Color.WHITE)
                        .bold()
                        .useFont(Typeface.create("fonts/HelveticaNeue-Medium.ttf", Typeface.NORMAL))
                        .endConfig()
                        .buildRound(String.valueOf(current.getBeneficiaryData().getBeneficiaryName().split(" ")[0].charAt(0) + "" + (current.getBeneficiaryData().getBeneficiaryName().split(" ").length > 1 ? current.getBeneficiaryData().getBeneficiaryName().split(" ")[1].charAt(0) : "")), ColorGenerator.MATERIAL.getRandomColor()));
            } else if (current.getBeneficiaryData() != null && Validation.isValidString(current.getBeneficiaryData().getBeneficiaryNameAr())) {
                ((TransactionViewHolder) holder).tvName.setText(current.getBeneficiaryData().getBeneficiaryNameAr().trim());
                ((TransactionViewHolder) holder).ivProfilePic.setImageDrawable(TextDrawable
                        .builder()
                        .beginConfig()
                        .textColor(Color.WHITE)
                        .bold()
                        .useFont(Typeface.create("fonts/HelveticaNeue-Medium.ttf", Typeface.NORMAL))
                        .endConfig()
                        .buildRound("", ColorGenerator.MATERIAL.getRandomColor()));


            } else if (current.getBeneficiaryData() != null && Validation.isValidString(current.getBeneficiaryData().getFull_name())) {
                ((TransactionViewHolder) holder).tvName.setText(current.getBeneficiaryData().getFull_name());
                ((TransactionViewHolder) holder).ivProfilePic.setImageDrawable(TextDrawable
                        .builder()
                        .beginConfig()
                        .textColor(Color.WHITE)
                        .bold()
                        .useFont(Typeface.create("fonts/HelveticaNeue-Medium.ttf", Typeface.NORMAL))
                        .endConfig()
                        .buildRound(String.valueOf(current.getBeneficiaryData().getFull_name().split(" ")[0].charAt(0) + "" + (current.getBeneficiaryData().getFull_name().split(" ").length > 1 ? current.getBeneficiaryData().getFull_name().split(" ")[1].charAt(0) : "")), ColorGenerator.MATERIAL.getRandomColor()));
            }
//------------------------------------DKG_----------------------------------------------------------

            else if (current.getBeneficiaryData() != null && Validation.isValidString(current.getBeneficiaryData().getCard_holder_name())) {
                ((TransactionViewHolder) holder).tvName.setText(current.getBeneficiaryData().getCard_holder_name());
                ((TransactionViewHolder) holder).ivProfilePic.setImageDrawable(TextDrawable
                        .builder()
                        .beginConfig()
                        .textColor(Color.WHITE)
                        .bold()
                        .useFont(Typeface.create("fonts/HelveticaNeue-Medium.ttf", Typeface.NORMAL))
                        .endConfig()
                        .buildRound(String.valueOf(current.getBeneficiaryData().getCard_holder_name().split(" ")[0].charAt(0) + "" + (current.getBeneficiaryData().getCard_holder_name().split(" ").length > 1 ? current.getBeneficiaryData().getCard_holder_name().split(" ")[1].charAt(0) : "")), ColorGenerator.MATERIAL.getRandomColor()));
            }
            //-----------------------------------------------WU-------------------------------------------
            else if (transactionType.equalsIgnoreCase("WU")) {
                String name = "";
                if (current.getBeneficiaryDataWu() != null && current.getBeneficiaryDataWu().getReceiverNameType() != null && current.getBeneficiaryDataWu().getReceiverFirstName() != null) {
                    if (current.getBeneficiaryDataWu().getReceiverNameType().equalsIgnoreCase("D")) {
                        name = (CommonUtils.getValidString(current.getBeneficiaryDataWu().getReceiverFirstName()) + " " + CommonUtils.getValidString(current.getBeneficiaryDataWu().getReceiverMiddleName()) + " " + CommonUtils.getValidString(current.getBeneficiaryDataWu().getReceiverLastName()));
                    } else {
                        name = (CommonUtils.getValidString(current.getBeneficiaryDataWu().getReceiverFirstName()) + " " + CommonUtils.getValidString(current.getBeneficiaryDataWu().getReceiverLastName()) + " " + CommonUtils.getValidString(current.getBeneficiaryDataWu().getReceiverMiddleName()));
                    }
                }
                if (Validation.isValidString(name)) {
                    ((TransactionViewHolder) holder).tvName.setText(name);

                    ((TransactionViewHolder) holder).ivProfilePic.setImageDrawable(TextDrawable
                            .builder()
                            .beginConfig()
                            .textColor(Color.WHITE)
                            .bold()
                            .useFont(Typeface.create("fonts/HelveticaNeue-Medium.ttf", Typeface.NORMAL))
                            .endConfig()
                            .buildRound(CommonUtils.getCharacters(name), ColorGenerator.MATERIAL.getRandomColor()));
                } else {
                    ((TransactionViewHolder) holder).tvName.setText("");
                    ((TransactionViewHolder) holder).ivProfilePic.setImageDrawable(TextDrawable
                            .builder()
                            .beginConfig()
                            .textColor(Color.WHITE)
                            .bold()
                            .useFont(Typeface.create("fonts/HelveticaNeue-Medium.ttf", Typeface.NORMAL))
                            .endConfig()
                            .buildRound("", ColorGenerator.MATERIAL.getRandomColor()));
                }

//--------------------------------------------------------BT----------------------------------------
            } /*else if (transactionType.equalsIgnoreCase("BT") && Validation.isValidString(current.getBeneficiaryData().getFull_name())) {
                ((TransactionViewHolder) holder).tvName.setText(current.getBeneficiaryData().getFull_name());
                ((TransactionViewHolder) holder).ivProfilePic.setImageDrawable(TextDrawable
                        .builder()
                        .beginConfig()
                        .textColor(Color.WHITE)
                        .bold()
                        .useFont(Typeface.create("fonts/HelveticaNeue-Medium.ttf", Typeface.NORMAL))
                        .endConfig()
                        .buildRound(String.valueOf(current.getBeneficiaryData().getFull_name().split(" ")[0].charAt(0) + "" + (current.getBeneficiaryData().getFull_name().split(" ").length > 1 ? current.getBeneficiaryData().getFull_name().split(" ")[1].charAt(0) : "")), ColorGenerator.MATERIAL.getRandomColor()));
            }*/


//--------------------------------------------------------------------------------------------------

            else if (transactionType.equalsIgnoreCase("WC")) {
                String name = "";
                if (current.getTransactionHistroyData() != null && current.getTransactionHistroyData().getCUSTOMERNAME() != null && current.getTransactionHistroyData().getCUSTOMERNAME() != null) {
                    name = CommonUtils.getValidString(current.getTransactionHistroyData().getCUSTOMERNAME());
                }

                if (Validation.isValidString(name)) {
                    ((TransactionViewHolder) holder).tvName.setText(name);

                    ((TransactionViewHolder) holder).ivProfilePic.setImageDrawable(TextDrawable
                            .builder()
                            .beginConfig()
                            .textColor(Color.WHITE)
                            .bold()
                            .useFont(Typeface.create("fonts/HelveticaNeue-Medium.ttf", Typeface.NORMAL))
                            .endConfig()
                            .buildRound(CommonUtils.getCharacters(name), ColorGenerator.MATERIAL.getRandomColor()));
                } else {
                    ((TransactionViewHolder) holder).tvName.setText("");
                    ((TransactionViewHolder) holder).ivProfilePic.setImageDrawable(TextDrawable
                            .builder()
                            .beginConfig()
                            .textColor(Color.WHITE)
                            .bold()
                            .useFont(Typeface.create("fonts/HelveticaNeue-Medium.ttf", Typeface.NORMAL))
                            .endConfig()
                            .buildRound("", ColorGenerator.MATERIAL.getRandomColor()));
                }
            } else {
                ((TransactionViewHolder) holder).tvName.setText("");
                ((TransactionViewHolder) holder).ivProfilePic.setImageDrawable(TextDrawable
                        .builder()
                        .beginConfig()
                        .textColor(Color.WHITE)
                        .bold()
                        .useFont(Typeface.create("fonts/HelveticaNeue-Medium.ttf", Typeface.NORMAL))
                        .endConfig()
                        .buildRound("", ColorGenerator.MATERIAL.getRandomColor()));
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
            if (current.getTxnType().equalsIgnoreCase("BT")) {
                if (current.getTransactionData().getBenBankName() != null) {
                    ((TransactionViewHolder) holder).tvBankName.setText(current.getTransactionData().getBenBankName());
                } else {
                    ((TransactionViewHolder) holder).tvBankName.setText(current.getTransactionData().getRoutine_bank_name());

                }
            } else {
                if (current.getBeneficiaryData() != null && current.getBeneficiaryData().getBankName() != null && current.getBeneficiaryData().getBankName().trim().length() > 0) {
                    ((TransactionViewHolder) holder).tvBankName.setText(current.getBeneficiaryData().getBankName().trim());
                } else if (current.getTransactionDataCE() != null && current.getTransactionDataCE().getAgent_name() != null && current.getTransactionDataCE().getAgent_name().trim().length() > 0) {
                    ((TransactionViewHolder) holder).tvBankName.setText(current.getTransactionDataCE().getAgent_name().trim());

                } else if (current.getTransactionDataBTCE() != null && current.getTransactionDataBTCE().getRoutine_bank_name() != null && current.getTransactionDataBTCE().getRoutine_bank_name().trim().length() > 0) {
                    ((TransactionViewHolder) holder).tvBankName.setText(current.getTransactionDataBTCE().getRoutine_bank_name().trim());

                } else if (transactionType.equalsIgnoreCase("WU")) {
                    ((TransactionViewHolder) holder).tvBankName.setText("WESTERN UNION");

                } else if (transactionType.equalsIgnoreCase("WC")) {
                    ((TransactionViewHolder) holder).tvBankName.setText("Travel Card Reload");
                } else {
                    ((TransactionViewHolder) holder).tvBankName.setText("");
                }
            }
//-------------------------New Modification---------------------------------------------------------
           /* if (Validation.isValidString(current.getBeneficiaryData().getBankName())) {
                ((TransactionViewHolder) holder).tvBankName.setText(current.getBeneficiaryData().getBankName());
            } else if (Validation.isValidString(current.getBeneficiaryData().getCard_holder_name())) {
                ((TransactionViewHolder) holder).tvBankName.setText(current.getBeneficiaryData().getBankName());
            } else if (current.getTxnType().equalsIgnoreCase("CP")||current.getTransactionDataCE().getAgent_name() != null) {
                ((TransactionViewHolder) holder).tvBankName.setText(current.getTransactionDataCE().getAgent_name());
            } else if (current.getTxnType().equalsIgnoreCase("BT")) {
                //rashid mpdification
                if(current.getTransactionData().getBenBankName()!=null){
                    ((TransactionViewHolder) holder).tvBankName.setText(current.getTransactionData().getBenBankName());
                }else{
                    ((TransactionViewHolder) holder).tvBankName.setText(current.getTransactionData().getRoutine_bank_name());
                }
            } else if (transactionType.equalsIgnoreCase("WU")) {
                ((TransactionViewHolder) holder).tvBankName.setText("WESTERN UNION");
            } else if (transactionType.equalsIgnoreCase("WC")) {
                ((TransactionViewHolder) holder).tvBankName.setText("Travel Card Reload");
            } else {
                ((TransactionViewHolder) holder).tvBankName.setText("");
            }*/


//--------------------------------------------------------------------------------------------------


            if (current.getTransactionData() != null && current.getTransactionData().getTxnAmount() != null && current.getTransactionData().getTxnAmount().trim().length() > 0) {
                ((TransactionViewHolder) holder).tvAmount.setText(CommonUtils.addCommaToString(current.getTransactionData().getTxnAmount()));
            } else if (current.getTransactionDataCE() != null && current.getTransactionDataCE().getSend_amount() != null && current.getTransactionDataCE().getSend_amount().trim().length() > 0) {
                ((TransactionViewHolder) holder).tvAmount.setText(CommonUtils.addCommaToString(current.getTransactionDataCE().getSend_amount()));
            } else if (current.getTransactionDataBTCE() != null && current.getTransactionDataBTCE().getSend_amount() != null && current.getTransactionDataBTCE().getSend_amount().trim().length() > 0) {
                ((TransactionViewHolder) holder).tvAmount.setText(CommonUtils.addCommaToString(current.getTransactionDataBTCE().getSend_amount()));
            } else if (transactionType.equalsIgnoreCase("WU")) {
                ((TransactionViewHolder) holder).tvAmount.setText(CommonUtils.addCommaToString(current.getTransactionDataWu().getSendAmount()));

            } else if (transactionType.equalsIgnoreCase("WC")) {
                ((TransactionViewHolder) holder).tvAmount.setText(CommonUtils.addCommaToString(String.valueOf(current.getTransactionHistroyData().getNETTOTAL())));

            } else {
                ((TransactionViewHolder) holder).tvAmount.setText("");
            }

            if (current.getTransactionData() != null && current.getTransactionData().getCcyName() != null && current.getTransactionData().getCcyName().trim().length() > 0) {
                ((TransactionViewHolder) holder).tvCurrencyCode.setText(current.getTransactionData().getCcyName());
            }
//-------------------------------------------------DKG_---------------------------------------------
            else if (current.getBeneficiaryData() != null && current.getBeneficiaryData().getCcy_desc() != null && current.getBeneficiaryData().getCcy_desc().trim().length() > 0) {
                ((TransactionViewHolder) holder).tvCurrencyCode.setText(current.getBeneficiaryData().getCcy_desc());
            }

//--------------------------------------------------------------------------------------------------
            else if (transactionType.equalsIgnoreCase("WU")) {
                if (current.getTransactionDataWu() != null && current.getTransactionDataWu().getCurrencyDesc() != null && current.getTransactionDataWu().getCurrencyDesc().trim().length() > 0) {
                    ((TransactionViewHolder) holder).tvCurrencyCode.setText(current.getTransactionDataWu().getCurrencyDesc());
                }
            } else if (transactionType.equalsIgnoreCase("WU")) {
                ((TransactionViewHolder) holder).tvCurrencyCode.setText("AED");

                /*else {
                    ((TransactionViewHolder) holder).tvCurrencyCode.setText("");
                }*/
            }

//--------------------------------------------------------------------------------------------------
            else {
                ((TransactionViewHolder) holder).tvCurrencyCode.setText("");
            }


            if (current.getServiceType() != null && current.getServiceType().trim().length() > 0) {
//                ((TransactionViewHolder) holder).tvServiceType.setText(current.getServiceType().equalsIgnoreCase(Constants.AREX_MAPPING) ? "Value Transfer" : "Instant Transfer");
                // ((TransactionViewHolder) holder).tvServiceType.setText(current.getServiceType().equalsIgnoreCase(Constants.AREX_MAPPING) ? "Payment by " : "Payment by ");
                ((TransactionViewHolder) holder).tvServiceType.setText("");

            } else {
                ((TransactionViewHolder) holder).tvServiceType.setText("");
            }

            Log.e("ewfbhejwvfhwf", "" + current.getTxnPayType() + " " + current.getTxnRecType());
            /*if (current.getTxnRecType() != null && current.getTxnRecType().trim().length() > 0) {
                if (current.getTxnPayType().equalsIgnoreCase(Constants.TRANSFER_TYPE_BANK_TRANSFER)) {
                    ((TransactionViewHolder) holder).tvTransferType.setText("Payment by Bank Transfer to Al Ansari a/c");
                } else if (current.getTxnPayType().equalsIgnoreCase(Constants.TRANSFER_TYPE_CASH_PICK_UP)) {
                    ((TransactionViewHolder) holder).tvTransferType.setText("Payment by Cash at Al Ansari branch");
                } else if (current.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_PRIORITY_PAY)) {
                    ((TransactionViewHolder) holder).tvTransferType.setText("Payment by Debit/Credit Card");
                } else if (current.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_BRANCH_PAY)) {
                    ((TransactionViewHolder) holder).tvTransferType.setText("Payment by Cash at Al Ansari branch");
                } else if (current.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_BANK_TRANSFER)) {
                        ((TransactionViewHolder) holder).tvTransferType.setText("Payment by Bank Transfer to Al Ansari a/c");


                }
            } else if (current.getTxnType().equalsIgnoreCase(Constants.TRANSFER_TYPE_CASH_PICK_UP)) {
                ((TransactionViewHolder) holder).tvTransferType.setText("Payment by Cash at Al Ansari branch");

            } else if (current.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_BRANCH_PAY)) {
                ((TransactionViewHolder) holder).tvTransferType.setText("Payment by Cash at Al Ansari branch");
            } else if (current.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_BANK_TRANSFER)) {
                ((TransactionViewHolder) holder).tvTransferType.setText("Payment by Bank Transfer to Al Ansari a/c");

            } else {
                ((TransactionViewHolder) holder).tvTransferType.setText("");
            }*/
            if(current.getTxnPayType()!=null) {
                if (current.getTxnPayType().equalsIgnoreCase(Constants.TRANSFER_TYPE_BANK_TRANSFER)) {
                    ((TransactionViewHolder) holder).tvTransferType.setText("Payment by Bank Transfer to Al Ansari a/c");
                } else if (current.getTxnPayType().equalsIgnoreCase(Constants.TRANSFER_TYPE_CASH_PICK_UP)) {
                    ((TransactionViewHolder) holder).tvTransferType.setText("Payment by Cash at Al Ansari branch");
                } else if (current.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_PRIORITY_PAY)) {
                    ((TransactionViewHolder) holder).tvTransferType.setText("Payment by Debit/Credit Card");
                } else if (current.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_BRANCH_PAY)) {
                    ((TransactionViewHolder) holder).tvTransferType.setText("Payment by Cash at Al Ansari branch");
                } else if (current.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_BANK_TRANSFER)) {
                    ((TransactionViewHolder) holder).tvTransferType.setText("Payment by Bank Transfer to Al Ansari a/c");
                } else {
                    ((TransactionViewHolder) holder).tvTransferType.setText("");
                }
            }else{
                ((TransactionViewHolder) holder).tvTransferType.setText("");

            }


           /* if (current.getTxnPayType().equalsIgnoreCase(Constants.TRANSFER_TYPE_BANK_TRANSFER)) {
                ((TransactionViewHolder) holder).tvTransferType.setText("Payment by Bank Transfer to Al Ansari a/c");
            } else if (current.getTxnPayType().equalsIgnoreCase(Constants.TRANSFER_TYPE_CASH_PICK_UP)) {
                ((TransactionViewHolder) holder).tvTransferType.setText("Payment by Cash at Al Ansari branch");
            } else if (current.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_PRIORITY_PAY)) {
                ((TransactionViewHolder) holder).tvTransferType.setText("Payment by Debit/Credit Card");
            } else if (current.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_BRANCH_PAY)) {
                ((TransactionViewHolder) holder).tvTransferType.setText("Payment by Cash at Al Ansari branch");
            } else if (current.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_BANK_TRANSFER)) {
                ((TransactionViewHolder) holder).tvTransferType.setText("Payment by Bank Transfer to Al Ansari a/c");
            } else {
                ((TransactionViewHolder) holder).tvTransferType.setText("");
            }*/


        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class TransactionViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfilePic;
        View bgLayout;
        TextView tvName, tvDate, tvBankName, tvServiceType, tvTransferType, tvAmount, tvCurrencyCode;

        public TransactionViewHolder(final View itemView) {
            super(itemView);
            bgLayout = itemView.findViewById(app.alansari.R.id.bg_layout);
            ivProfilePic = (ImageView) itemView.findViewById(app.alansari.R.id.profile_pic);
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
                    String txnType = itemList.get(getAdapterPosition()).getTxnType();

                    // Log.e("vn sgchsbcvh",""+transactionType+" "+txnType);
                    if (txnType.equalsIgnoreCase(Constants.TXN_TYPE_TRAVEL_CARD)) {
                        Intent intent = new Intent(context, TransactionTravelDetailsActivity.class);
                        intent.putExtra(Constants.OBJECT, itemList.get(getAdapterPosition()));
                        context.startActivity(intent);


                    } else {
                        Intent intent = new Intent(context, TransactionDetailsActivity.class);
                        intent.putExtra(Constants.OBJECT, itemList.get(getAdapterPosition()));

                        //intent.putExtra(Constants.OBJECT, itemList.get(getAdapterPosition()));
                        //Constants.data = itemList.get(getAdapterPosition());
                        String name = itemList.get(getAdapterPosition()).getTxnType();
                        intent.putExtra(Constants.SOURCE, Constants.SOURCE_TRANSACTION_PENDING_LIST);
                        if (name.equalsIgnoreCase(Constants.TRANSACTION_TYPE_WU)) {
                            intent.putExtra(Constants.SOURCE_TYPE, Constants.TRANSACTION_TYPE_WU);
                        } else if (transactionType.equalsIgnoreCase(Constants.TXN_TYPE_INSTANT)) {
                            intent.putExtra(Constants.SOURCE_TYPE, transactionType.equalsIgnoreCase(Constants.TXN_TYPE_INSTANT) ? Constants.TYPE_CREDIT_CARD : Constants.TYPE_SEND_MONEY);

                        } else {
                            intent.putExtra(Constants.SOURCE_TYPE, transactionType.equalsIgnoreCase(Constants.TRANSACTION_TYPE_CREDIT_CARD_PAYMENT) ? Constants.TYPE_CREDIT_CARD : Constants.TYPE_SEND_MONEY);
                        }
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}