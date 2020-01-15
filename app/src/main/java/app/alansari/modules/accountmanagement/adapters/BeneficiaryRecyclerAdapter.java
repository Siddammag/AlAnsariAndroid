package app.alansari.modules.accountmanagement.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import app.alansari.AppController;
import app.alansari.R;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.RoundedImageView;
import app.alansari.Utils.Validation;
import app.alansari.listeners.CustomClickListener;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.BeneficiaryData;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import app.alansari.textdrawable.TextDrawable;
import de.hdodenhof.circleimageview.CircleImageView;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.DELETE_BENEFICIARY;

/**
 * Created by Parveen Dala on 13 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class BeneficiaryRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_BODY = 1;
    private static final int VIEW_TYPE_HEADER = 0;
    public int changedPosition = 0;
    public ArrayList<BeneficiaryData> beneficiaryList, originalList;
    private Context context;
    private String intentType;
    private LayoutInflater inflater;
    private CustomClickListener clickListener;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public BeneficiaryRecyclerAdapter(Context context, ArrayList<BeneficiaryData> beneficiaryList, CustomClickListener clickListener, String intentType) {
        this.context = context;
        this.intentType = intentType;
        this.beneficiaryList = beneficiaryList;
        this.originalList = new ArrayList<>();
        this.originalList.addAll(beneficiaryList);
        this.clickListener = clickListener;
        inflater = LayoutInflater.from(context);
    }

    public void delete(int position) {
        beneficiaryList.remove(position);
        originalList.remove(position);
        notifyItemRemoved(position + changedPosition);
    }

    public void addItem(BeneficiaryData data) {
        this.beneficiaryList.add(data);
        this.originalList.add(data);
        notifyItemInserted(beneficiaryList.size() - 1 + changedPosition);
    }

    public void addItemAt(BeneficiaryData data, int position) {
        this.beneficiaryList.add(position, data);
        notifyItemInserted(position + changedPosition);
    }

    public void replaceItemAt(BeneficiaryData data, int position) {
        beneficiaryList.set(position, data);
        notifyItemChanged(position + changedPosition);
    }

    public void addItemAtCustomLLayout(BeneficiaryData data, int position) {
        this.beneficiaryList.add(position, data);
        notifyDataSetChanged();
    }

    public ArrayList<BeneficiaryData> getArrayList() {
        return beneficiaryList;
    }

    public ArrayList<BeneficiaryData> getOriginalArrayList() {
        return originalList;
    }

    public BeneficiaryData getItemAt(int position) {
        return beneficiaryList.get(position);
    }

    public void addArrayList(ArrayList<BeneficiaryData> beneficiaryList) {
        this.beneficiaryList.addAll(beneficiaryList);
        this.originalList.addAll(beneficiaryList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.beneficiaryList.clear();
        this.originalList.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            return new BeneficiaryHeaderViewHolder(inflater.inflate(R.layout.item_beneficiary_cash_express, parent, false));
        } else {
            return new BeneficiaryViewHolder(inflater.inflate(app.alansari.R.layout.item_beneficiary, parent, false));
        }
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof BeneficiaryViewHolder) {
            BeneficiaryData current = beneficiaryList.get(position - changedPosition);
            //BeneficiaryData current = beneficiaryList.get(position);
           /* if (current.getModuleName().equalsIgnoreCase("AREX")) {
                ((BeneficiaryViewHolder) holder).bgLayout.setBackground(ContextCompat.getDrawable(context, app.alansari.R.drawable.beneficiary_card_dark_bg_image));
            } else {
                ((BeneficiaryViewHolder) holder).bgLayout.setBackground(ContextCompat.getDrawable(context, app.alansari.R.drawable.beneficiary_card_dark_bg_white));
            }*/

             ((BeneficiaryViewHolder) holder).bgLayout.setBackground(ContextCompat.getDrawable(context, app.alansari.R.drawable.beneficiary_card_dark_bg_image));

            if (Validation.isValidString(current.getName())) {
                ((BeneficiaryViewHolder) holder).tvName.setText(current.getName());

            }else if(Validation.isValidString(current.getArabicName())){
                ((BeneficiaryViewHolder) holder).tvName.setText(current.getArabicName().trim());
            }else{
                ((BeneficiaryViewHolder) holder).tvName.setText("NA");
            }


            if (current.getBenImage() != null && !TextUtils.isEmpty(current.getBenImage().trim()) && current.getBenImage().trim().length() > 0) {
               // CommonUtils.setBeneficiaryImage(context, ((BeneficiaryViewHolder) holder).ivProfilePic, current.getBenImage());
                ((BeneficiaryViewHolder) holder).ivProfilePic.setVisibility(View.VISIBLE);
                ((BeneficiaryViewHolder) holder).ivProfileText.setVisibility(View.GONE);
            } else {
                /*if (Validation.isValidString(current.getName())) {
                    ((BeneficiaryViewHolder) holder).tvName.setText(current.getName());
                    String name = "";
                    try {
                        if (current.getName().split(" ").length >= 1) {
                            name = String.valueOf(current.getName().split(" ")[0].trim().toUpperCase().charAt(0) + "" + (current.getName().split(" ").length > 1 ? current.getName().split(" ")[1].toUpperCase().charAt(0) : ""));
                        } else {
                            name = String.valueOf(current.getName().trim().toUpperCase().charAt(0));
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        name = String.valueOf(current.getName().trim().toUpperCase().charAt(0));
                    }

                    ((BeneficiaryViewHolder) holder).ivProfileText.setImageDrawable(TextDrawable
                            .builder()
                            .beginConfig()
                            .textColor(Color.BLACK)
                            .bold()
                            .useFont(Typeface.create("fonts/HelveticaNeue-Medium.ttf", Typeface.NORMAL))
                            .endConfig()
                            .buildRound(name, ContextCompat.getColor(context, R.color.white)));
                    ((BeneficiaryViewHolder) holder).ivProfilePic.setVisibility(View.GONE);
                    ((BeneficiaryViewHolder) holder).ivProfileText.setVisibility(View.VISIBLE);
                } else if (Validation.isValidString(current.getArabicName())) {
                    ((BeneficiaryViewHolder) holder).ivProfileText.setImageDrawable(TextDrawable
                            .builder()
                            .beginConfig()
                            .textColor(Color.BLACK)
                            .bold()
                            .useFont(Typeface.create("fonts/HelveticaNeue-Medium.ttf", Typeface.NORMAL))
                            .endConfig()
                            .buildRound("", ContextCompat.getColor(context, R.color.white)));
                    ((BeneficiaryViewHolder) holder).ivProfilePic.setVisibility(View.GONE);
                    ((BeneficiaryViewHolder) holder).ivProfileText.setVisibility(View.VISIBLE);
                    ((BeneficiaryViewHolder) holder).tvName.setText(current.getArabicName().trim());
                } else {
                    ((BeneficiaryViewHolder) holder).ivProfilePic.setVisibility(View.GONE);
                    ((BeneficiaryViewHolder) holder).ivProfileText.setVisibility(View.INVISIBLE);
                    ((BeneficiaryViewHolder) holder).tvName.setText("NA");
                }*/
                //CommonUtils.setBeneficiaryImage(context, ((BeneficiaryViewHolder) holder).ivProfilePic, current.getCountryData().getFlag());
                Picasso.get()
                        .load("https://mobileapp.eexchange.ae:9106/ProjectGatewayImage/Flag/" + current.getCountryData().getFlag())
                        .into(((BeneficiaryViewHolder) holder).ivProfilePic);
                ((BeneficiaryViewHolder) holder).ivProfilePic.setVisibility(View.VISIBLE);
                ((BeneficiaryViewHolder) holder).ivProfileText.setVisibility(View.GONE);

            }


            if (current.getBankData() != null && Validation.isValidString(current.getBankData().getBankName())) {
                ((BeneficiaryViewHolder) holder).tvBankName.setText(current.getBankData().getBankName());
            } else {
                ((BeneficiaryViewHolder) holder).tvBankName.setText("NA");
            }

            if (Validation.isValidString(current.getAccountNumber())) {
                ((BeneficiaryViewHolder) holder).tvAccountNum.setText(current.getAccountNumber());
            } else {
                if (current.getBankData() != null && Validation.isValidString(current.getBankData().getIBANNumber())) {
                    ((BeneficiaryViewHolder) holder).tvAccountNum.setText(current.getBankData().getIBANNumber());
                } else {
                    ((BeneficiaryViewHolder) holder).tvAccountNum.setText("NA");
                }
            }

            if (Validation.isValidString(current.getAccountType())) {
                if (current.getAccountType().equalsIgnoreCase("Savings")) {
                    ((BeneficiaryViewHolder) holder).tvAccountType.setText(current.getAccountType());
                } else if (current.getAccountType().equalsIgnoreCase("Current")) {
                    ((BeneficiaryViewHolder) holder).tvAccountType.setText(current.getAccountType());
                } else {
                    ((BeneficiaryViewHolder) holder).tvAccountType.setText("NA");
                }
                //((BeneficiaryViewHolder) holder).tvAccountType.setText(current.getAccountType().equalsIgnoreCase("S") ? "Savings" : "Current");
                //((BeneficiaryViewHolder) holder).tvAccountType.setText(current.getAccountType());

            } else {
                ((BeneficiaryViewHolder) holder).tvAccountType.setText("NA");
            }

            if (Validation.isValidString(current.getBranchName())) {
                ((BeneficiaryViewHolder) holder).tvBranchName.setText(current.getBranchName());
            } else {
                ((BeneficiaryViewHolder) holder).tvBranchName.setText("NA");
            }

            if (Validation.isValidString(current.getMobile())) {
                ((BeneficiaryViewHolder) holder).tvMobileNum.setText(current.getMobile());
            } else {
                ((BeneficiaryViewHolder) holder).tvMobileNum.setText("NA");
            }
            ((BeneficiaryViewHolder) holder).picLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            if (intentType.equalsIgnoreCase(Constants.ACTIVITY_FOR_RESULT)) {
                ((BeneficiaryViewHolder) holder).btnSend.setText("Select");
                ((BeneficiaryViewHolder) holder).btnDelete.setVisibility(View.GONE);
                ((BeneficiaryViewHolder) holder).btnEdit.setVisibility(View.GONE);
            } else if (intentType.equalsIgnoreCase(Constants.ACTIVITY_FOR_SELECTION)) {
                ((BeneficiaryViewHolder) holder).btnSend.setText("Select");
                ((BeneficiaryViewHolder) holder).btnDelete.setVisibility(View.GONE);
                ((BeneficiaryViewHolder) holder).btnEdit.setVisibility(View.GONE);
            } else if(intentType.equalsIgnoreCase(Constants.ACTIVITY_FOR_SELECTION)){
                ((BeneficiaryViewHolder) holder).btnSend.setText("Select");
                ((BeneficiaryViewHolder) holder).btnDelete.setVisibility(View.VISIBLE);
                ((BeneficiaryViewHolder) holder).btnEdit.setVisibility(View.VISIBLE);

                ((BeneficiaryViewHolder) holder).picLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickListener.itemClicked(v, position, beneficiaryList.get(position));
                    }
                });
            }else {
                ((BeneficiaryViewHolder) holder).btnSend.setText("Select");
                ((BeneficiaryViewHolder) holder).btnDelete.setVisibility(View.VISIBLE);
                ((BeneficiaryViewHolder) holder).btnEdit.setVisibility(View.VISIBLE);

                ((BeneficiaryViewHolder) holder).picLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickListener.itemClicked(v, position, beneficiaryList.get(position));
                    }
                });
            }
            ((BeneficiaryViewHolder) holder).btnEdit.setVisibility(View.GONE);


        } else if (holder instanceof BeneficiaryHeaderViewHolder) {
            BeneficiaryData current = beneficiaryList.get(position - changedPosition);
            ((BeneficiaryHeaderViewHolder) holder).bgLayout.setBackground(ContextCompat.getDrawable(context, app.alansari.R.drawable.beneficiary_card_dark_bg_white));


            if (Validation.isValidString(current.getName())) {
                ((BeneficiaryHeaderViewHolder) holder).tvName.setText(current.getName());
            }else if(Validation.isValidString(current.getArabicName())){
                ((BeneficiaryHeaderViewHolder) holder).tvName.setText(current.getArabicName().trim());
            }else{
                ((BeneficiaryHeaderViewHolder) holder).tvName.setText("NA");
            }

            if (current.getBenImage() != null && !TextUtils.isEmpty(current.getBenImage().trim()) && current.getBenImage().trim().length() > 0) {
               // CommonUtils.setBeneficiaryImage(context, ((BeneficiaryHeaderViewHolder) holder).ivProfilePic, current.getBenImage());
                ((BeneficiaryHeaderViewHolder) holder).ivProfilePic.setVisibility(View.VISIBLE);
                ((BeneficiaryHeaderViewHolder) holder).ivProfileText.setVisibility(View.GONE);
            } else {
                Picasso.get()
                        .load("https://mobileapp.eexchange.ae:9106/ProjectGatewayImage/Flag/" + current.getCountryData().getFlag())
                        .into(((BeneficiaryHeaderViewHolder) holder).ivProfilePic);
                /*Glide.with(context)
                        .load("https://mobileapp.eexchange.ae:9106/ProjectGatewayImage/Flag/" + current.getCountryData().getFlag())
                        .apply(RequestOptions.circleCropTransform())
                        .into(((BeneficiaryHeaderViewHolder) holder).ivProfilePic);
*/
                ((BeneficiaryHeaderViewHolder) holder).ivProfilePic.setVisibility(View.VISIBLE);
                ((BeneficiaryHeaderViewHolder) holder).ivProfileText.setVisibility(View.GONE);

            }


            if (current.getBankData() != null && Validation.isValidString(current.getBankData().getBankName())) {
                ((BeneficiaryHeaderViewHolder) holder).tvBankName.setText(current.getBankData().getBankName());
            } else {
                ((BeneficiaryHeaderViewHolder) holder).tvBankName.setText("NA");
            }

            if (Validation.isValidString(current.getAccountNumber())) {
                ((BeneficiaryHeaderViewHolder) holder).tvAccountNum.setText(current.getAccountNumber());
            } else {
                if (current.getBankData() != null && Validation.isValidString(current.getBankData().getIBANNumber())) {
                    ((BeneficiaryHeaderViewHolder) holder).tvAccountNum.setText(current.getBankData().getIBANNumber());
                } else {
                    ((BeneficiaryHeaderViewHolder) holder).tvAccountNum.setText("NA");
                }
            }

            if (Validation.isValidString(current.getAccountType())) {
                if (current.getAccountType().equalsIgnoreCase("Savings")) {
                    ((BeneficiaryHeaderViewHolder) holder).tvAccountType.setText(current.getAccountType());
                } else if (current.getAccountType().equalsIgnoreCase("Current")) {
                    ((BeneficiaryHeaderViewHolder) holder).tvAccountType.setText(current.getAccountType());
                } else {
                    ((BeneficiaryHeaderViewHolder) holder).tvAccountType.setText("NA");
                }

            } else {
                ((BeneficiaryHeaderViewHolder) holder).tvAccountType.setText("NA");
            }

            if (Validation.isValidString(current.getBranchName())) {
                ((BeneficiaryHeaderViewHolder) holder).tvBranchName.setText(current.getBranchName());
            } else {
                ((BeneficiaryHeaderViewHolder) holder).tvBranchName.setText("NA");
            }

            if (Validation.isValidString(current.getMobile())) {
                ((BeneficiaryHeaderViewHolder) holder).tvMobileNum.setText(current.getMobile());
            } else {
                ((BeneficiaryHeaderViewHolder) holder).tvMobileNum.setText("NA");
            }
            ((BeneficiaryHeaderViewHolder) holder).picLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            if (intentType.equalsIgnoreCase(Constants.ACTIVITY_FOR_RESULT)) {
                ((BeneficiaryHeaderViewHolder) holder).btnSend.setText("Select");
                ((BeneficiaryHeaderViewHolder) holder).btnDelete.setVisibility(View.GONE);
                ((BeneficiaryHeaderViewHolder) holder).btnEdit.setVisibility(View.GONE);
            } else if (intentType.equalsIgnoreCase(Constants.ACTIVITY_FOR_SELECTION)) {
                ((BeneficiaryHeaderViewHolder) holder).btnSend.setText("Select");
                ((BeneficiaryHeaderViewHolder) holder).btnDelete.setVisibility(View.GONE);
                ((BeneficiaryHeaderViewHolder) holder).btnEdit.setVisibility(View.GONE);
            } else if(intentType.equalsIgnoreCase(Constants.ACTIVITY_FOR_SELECTION)){
                ((BeneficiaryHeaderViewHolder) holder).btnSend.setText("Select");
                ((BeneficiaryHeaderViewHolder) holder).btnDelete.setVisibility(View.VISIBLE);
                ((BeneficiaryHeaderViewHolder) holder).btnEdit.setVisibility(View.VISIBLE);

                ((BeneficiaryHeaderViewHolder) holder).picLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickListener.itemClicked(v, position, beneficiaryList.get(position));
                    }
                });
            }else {
                ((BeneficiaryHeaderViewHolder) holder).btnSend.setText("Select");
                ((BeneficiaryHeaderViewHolder) holder).btnDelete.setVisibility(View.VISIBLE);
                ((BeneficiaryHeaderViewHolder) holder).btnEdit.setVisibility(View.VISIBLE);

                ((BeneficiaryHeaderViewHolder) holder).picLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickListener.itemClicked(v, position, beneficiaryList.get(position));
                    }
                });
            }
            ((BeneficiaryHeaderViewHolder) holder).btnEdit.setVisibility(View.GONE);


        }


    }


    @Override
    public int getItemViewType(int position) {
        BeneficiaryData current = beneficiaryList.get(position - changedPosition);
        if (current.getModuleName().equalsIgnoreCase("AREX")) {
            return VIEW_TYPE_BODY;
        } else {
            return VIEW_TYPE_HEADER;
        }
        //return VIEW_TYPE_BODY;

    }

    @Override
    public int getItemCount() {
        return beneficiaryList.size() + changedPosition;
    }


    class BeneficiaryViewHolder extends RecyclerView.ViewHolder implements OnWebServiceResult {
        View bgLayout, detailsLayout;
        TextView btnSend;
        AppCompatImageView btnDelete, btnEdit;
        //CircleImageView ivProfilePic;
        ImageView ivProfileText,ivProfilePic;
        View llAccountNum, llAccountType, llBranchName, llMobileNum;
        TextView tvName, tvBankName, tvAccountNum, tvBranchName, tvAccountType, tvMobileNum;
        RelativeLayout picLayout;

        public BeneficiaryViewHolder(View itemView) {
            super(itemView);
            bgLayout = itemView.findViewById(app.alansari.R.id.bg_layout);
            detailsLayout = itemView.findViewById(app.alansari.R.id.details_layout);
            ivProfilePic = (ImageView) itemView.findViewById(app.alansari.R.id.profile_pic);
            ivProfileText = (ImageView) itemView.findViewById(app.alansari.R.id.profile_pic_text);
            tvName = (TextView) itemView.findViewById(app.alansari.R.id.name);
            tvBankName = (TextView) itemView.findViewById(app.alansari.R.id.bank_name);
            tvAccountNum = (TextView) itemView.findViewById(app.alansari.R.id.account_num);
            tvBranchName = (TextView) itemView.findViewById(app.alansari.R.id.branch_name);
            tvAccountType = (TextView) itemView.findViewById(app.alansari.R.id.account_type);
            tvMobileNum = (TextView) itemView.findViewById(app.alansari.R.id.mobile_num);
            llAccountNum = itemView.findViewById(app.alansari.R.id.account_num_layout);
            llAccountType = itemView.findViewById(app.alansari.R.id.account_type_layout);
            llBranchName = itemView.findViewById(app.alansari.R.id.branch_name_layout);
            llMobileNum = itemView.findViewById(app.alansari.R.id.mobile_num_layout);
            btnDelete = (AppCompatImageView) itemView.findViewById(app.alansari.R.id.delete_btn);
            btnEdit = (AppCompatImageView) itemView.findViewById(app.alansari.R.id.edit_btn);
            btnSend = (TextView) itemView.findViewById(app.alansari.R.id.send_btn);
            picLayout = (RelativeLayout) itemView.findViewById(app.alansari.R.id.pic_layout);

            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.itemClicked(v, getAdapterPosition(), beneficiaryList.get(getAdapterPosition()));
                }
            });

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.itemClicked(v, getAdapterPosition(), beneficiaryList.get(getAdapterPosition()));
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((beneficiaryList != null) && beneficiaryList.size() > 0) {
                        final Dialog myDialog = new Dialog(context, app.alansari.R.style.CustomDialogThemeLightBg);
                        myDialog.setCanceledOnTouchOutside(true);
                        myDialog.setContentView(app.alansari.R.layout.confirm_delete_item_generic_dialog);
                        ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_title)).setText("Delete beneficiary?");
                        ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_text)).setText(context.getResources().getString(app.alansari.R.string.confirm_delete_beneficiary));
                        myDialog.findViewById(app.alansari.R.id.dialog_btn_yes).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteBeneficiary(beneficiaryList.get(getAdapterPosition() - changedPosition).getBeneficiaryId(), beneficiaryList.get(getAdapterPosition() - changedPosition).getServiceTypeData().getMapping());
                                myDialog.dismiss();
                            }
                        });

                        myDialog.findViewById(app.alansari.R.id.dialog_btn_no).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myDialog.dismiss();
                            }
                        });
                        myDialog.show();
                    }
                }
            });
        }

        public void deleteBeneficiary(String deletedBeneficiaryId, String serviceType) {
            if (NetworkStatus.getInstance(context).isOnline2(context)) {
                String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
                JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().deleteBeneficiary(deletedBeneficiaryId, serviceType, CommonUtils.getUserId(), sessionTime, LogoutCalling.getDeviceID(context)), Constants.DELETE_BENEFICIARY_URL, DELETE_BENEFICIARY, Request.Method.PUT, this);
                AppController.getInstance().getRequestQueue().cancelAll(DELETE_BENEFICIARY.toString());
                AppController.getInstance().addToRequestQueue(jsonObjReq, DELETE_BENEFICIARY.toString());
                CommonUtils.showLoading(context, context.getString(app.alansari.R.string.please_wait), DELETE_BENEFICIARY.toString(), false);
            } else {
                Toast.makeText(context, context.getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
            switch (sType) {
                case DELETE_BENEFICIARY:
                    CommonUtils.hideLoading();
                    if (status == 1) {
                        try {
                            if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                                if (context != null && this != null)
                                    delete(getAdapterPosition());
                            } else {
                                Toast.makeText(context, response.getString("MESSAGE"), Toast.LENGTH_SHORT).show();

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Toast.makeText(context, context.getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, context.getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }

    }



    class BeneficiaryHeaderViewHolder extends RecyclerView.ViewHolder implements OnWebServiceResult {
        View bgLayout, detailsLayout;
        TextView btnSend;
        AppCompatImageView btnDelete, btnEdit;
       // CircleImageView ivProfilePic;
        ImageView ivProfileText,ivProfilePic;
        View llAccountNum, llAccountType, llBranchName, llMobileNum;
        TextView tvName, tvBankName, tvAccountNum, tvBranchName, tvAccountType, tvMobileNum;
        RelativeLayout picLayout;

        public BeneficiaryHeaderViewHolder(View itemView) {
            super(itemView);
            bgLayout = itemView.findViewById(app.alansari.R.id.bg_layout);
            detailsLayout = itemView.findViewById(app.alansari.R.id.details_layout);
            ivProfilePic = (ImageView) itemView.findViewById(app.alansari.R.id.profile_pic);
            ivProfileText = (ImageView) itemView.findViewById(app.alansari.R.id.profile_pic_text);
            tvName = (TextView) itemView.findViewById(app.alansari.R.id.name);
            tvBankName = (TextView) itemView.findViewById(app.alansari.R.id.bank_name);
            tvAccountNum = (TextView) itemView.findViewById(app.alansari.R.id.account_num);
            tvBranchName = (TextView) itemView.findViewById(app.alansari.R.id.branch_name);
            tvAccountType = (TextView) itemView.findViewById(app.alansari.R.id.account_type);
            tvMobileNum = (TextView) itemView.findViewById(app.alansari.R.id.mobile_num);
            llAccountNum = itemView.findViewById(app.alansari.R.id.account_num_layout);
            llAccountType = itemView.findViewById(app.alansari.R.id.account_type_layout);
            llBranchName = itemView.findViewById(app.alansari.R.id.branch_name_layout);
            llMobileNum = itemView.findViewById(app.alansari.R.id.mobile_num_layout);
            btnDelete = (AppCompatImageView) itemView.findViewById(app.alansari.R.id.delete_btn);
            btnEdit = (AppCompatImageView) itemView.findViewById(app.alansari.R.id.edit_btn);
            btnSend = (TextView) itemView.findViewById(app.alansari.R.id.send_btn);
            picLayout = (RelativeLayout) itemView.findViewById(app.alansari.R.id.pic_layout);

            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.itemClicked(v, getAdapterPosition(), beneficiaryList.get(getAdapterPosition()));
                }
            });

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.itemClicked(v, getAdapterPosition(), beneficiaryList.get(getAdapterPosition()));
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((beneficiaryList != null) && beneficiaryList.size() > 0) {
                        final Dialog myDialog = new Dialog(context, app.alansari.R.style.CustomDialogThemeLightBg);
                        myDialog.setCanceledOnTouchOutside(true);
                        myDialog.setContentView(app.alansari.R.layout.confirm_delete_item_generic_dialog);
                        ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_title)).setText("Delete beneficiary?");
                        ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_text)).setText(context.getResources().getString(app.alansari.R.string.confirm_delete_beneficiary));
                        myDialog.findViewById(app.alansari.R.id.dialog_btn_yes).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteBeneficiary(beneficiaryList.get(getAdapterPosition() - changedPosition).getBeneficiaryId(), beneficiaryList.get(getAdapterPosition() - changedPosition).getServiceTypeData().getMapping());
                                myDialog.dismiss();
                            }
                        });

                        myDialog.findViewById(app.alansari.R.id.dialog_btn_no).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myDialog.dismiss();
                            }
                        });
                        myDialog.show();
                    }
                }
            });
        }
        public void deleteBeneficiary(String deletedBeneficiaryId, String serviceType) {
            if (NetworkStatus.getInstance(context).isOnline2(context)) {
                String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
                JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().deleteBeneficiary(deletedBeneficiaryId, serviceType, CommonUtils.getUserId(), sessionTime, LogoutCalling.getDeviceID(context)), Constants.DELETE_BENEFICIARY_URL, DELETE_BENEFICIARY, Request.Method.PUT, this);
                AppController.getInstance().getRequestQueue().cancelAll(DELETE_BENEFICIARY.toString());
                AppController.getInstance().addToRequestQueue(jsonObjReq, DELETE_BENEFICIARY.toString());
                CommonUtils.showLoading(context, context.getString(app.alansari.R.string.please_wait), DELETE_BENEFICIARY.toString(), false);
            } else {
                Toast.makeText(context, context.getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
            switch (sType) {
                case DELETE_BENEFICIARY:
                    CommonUtils.hideLoading();
                    if (status == 1) {
                        try {
                            if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                                if (context != null && this != null)
                                    delete(getAdapterPosition());
                            } else {
                                Toast.makeText(context, response.getString("MESSAGE"), Toast.LENGTH_SHORT).show();

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Toast.makeText(context, context.getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, context.getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }


}
}
