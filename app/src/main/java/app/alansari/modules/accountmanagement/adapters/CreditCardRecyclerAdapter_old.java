package app.alansari.modules.accountmanagement.adapters;

import app.alansari.Utils.Validation;
import app.alansari.adapters.GenericRecycleAdapter;
import app.alansari.models.BeneficiaryData;
import app.alansari.textdrawable.TextDrawable;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.core.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Parveen Dala on 19 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class CreditCardRecyclerAdapter_old extends GenericRecycleAdapter<BeneficiaryData> implements Filterable {
    private static final int VIEW_TYPE_BODY = 1;
    private static final int VIEW_TYPE_HEADER = 0;
    public int changedPosition = 0;
    public ArrayList<BeneficiaryData> beneficiaryList, originalList;
    private Context context;
    private View visibleLayout;
    private LayoutInflater inflater;

    public CreditCardRecyclerAdapter_old(Context context, ArrayList<BeneficiaryData> beneficiaryList) {
        super(context);
        this.context = context;
        this.beneficiaryList = beneficiaryList;
        this.originalList = new ArrayList<>();
        inflater = LayoutInflater.from(context);
        super.addList(this.originalList);
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

    public BeneficiaryData getItemAt(int position) {
        return beneficiaryList.get(position);
    }

    public void addArrayList(ArrayList<BeneficiaryData> beneficiaryList) {
        this.beneficiaryList.clear();
        this.originalList.clear();
        this.beneficiaryList = beneficiaryList;
        this.originalList.addAll(beneficiaryList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.beneficiaryList.clear();
        this.originalList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return beneficiaryList.size() + changedPosition;
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_BODY;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            return new BeneficiaryHeaderViewHolder(inflater.inflate(app.alansari.R.layout.item_beneficiary_header, parent, false));
        } else {
            return new BankAccountViewHolder(inflater.inflate(app.alansari.R.layout.item_credit_card, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof BeneficiaryHeaderViewHolder) {

        } else {
            BeneficiaryData current = beneficiaryList.get(position - changedPosition);
            if (position % 2 == 0) {
                ((BankAccountViewHolder) holder).bgLayout.setBackground(ContextCompat.getDrawable(context, app.alansari.R.drawable.beneficiary_card_dark_bg));
            } else {
                ((BankAccountViewHolder) holder).bgLayout.setBackground(ContextCompat.getDrawable(context, app.alansari.R.drawable.beneficiary_card_light_bg));
            }
            if (Validation.isValidString(current.getName())) {
                ((BankAccountViewHolder) holder).tvName.setText(current.getName());
                ((BankAccountViewHolder) holder).ivProfileText.setImageDrawable(TextDrawable
                        .builder()
                        .beginConfig()
                        .textColor(Color.BLACK)
                        .bold()
                        .useFont(Typeface.create("fonts/Roboto-Regular.ttf", Typeface.NORMAL))
                        .endConfig()
                        .buildRound(String.valueOf(current.getName().split(" ")[0].charAt(0) + "" + (current.getName().split(" ").length > 1 ? current.getName().split(" ")[1].charAt(0) : "")), ContextCompat.getColor(context, app.alansari.R.color.colorTransparent)));
            } else {
                ((BankAccountViewHolder) holder).tvName.setText("");
            }

            if (Validation.isValidString(current.getBankData().getBankName())) {
                ((BankAccountViewHolder) holder).tvBankName.setText(current.getBankData().getBankName());
            } else {
                ((BankAccountViewHolder) holder).tvBankName.setText("");
            }

            if (Validation.isValidString(current.getAccountNumber())) {
                ((BankAccountViewHolder) holder).tvAccountNum.setText(current.getAccountNumber());
            } else {
                ((BankAccountViewHolder) holder).tvAccountNum.setText("");
            }

            if (current.getAccountType() != null && current.getAccountType().trim().length() > 0) {
                ((BankAccountViewHolder) holder).tvAccountType.setText(current.getAccountType());
            } else {
                ((BankAccountViewHolder) holder).tvAccountType.setText("");
            }

            if (current.getBankData().getIBANNumber() != null && current.getBankData().getIBANNumber().trim().length() > 0) {
                ((BankAccountViewHolder) holder).tvIBANNum.setText(String.valueOf(current.getBankData().getIBANNumber()));
            } else {
                ((BankAccountViewHolder) holder).tvIBANNum.setText("");
            }
        }
    }

    class BeneficiaryHeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvRate, tvTime;

        public BeneficiaryHeaderViewHolder(View view) {
            super(view);
            tvRate = (TextView) itemView.findViewById(app.alansari.R.id.rate);
            tvTime = (TextView) itemView.findViewById(app.alansari.R.id.time);
        }
    }

    class BankAccountViewHolder extends RecyclerView.ViewHolder {
        View bgLayout, detailsLayout;
        ImageView ivProfilePic, ivProfileText;
        TextView tvName, tvBankName, tvAccountNum, tvIBANNum, tvAccountType;

        public BankAccountViewHolder(View itemView) {
            super(itemView);
            bgLayout = itemView.findViewById(app.alansari.R.id.bg_layout);
            detailsLayout = itemView.findViewById(app.alansari.R.id.details_layout);
            ivProfilePic = (ImageView) itemView.findViewById(app.alansari.R.id.profile_pic);
            ivProfileText = (ImageView) itemView.findViewById(app.alansari.R.id.profile_pic_text);
            tvName = (TextView) itemView.findViewById(app.alansari.R.id.name);
            tvBankName = (TextView) itemView.findViewById(app.alansari.R.id.bank_name);
            tvAccountNum = (TextView) itemView.findViewById(app.alansari.R.id.account_num);
            tvIBANNum = (TextView) itemView.findViewById(app.alansari.R.id.iban_num);
            tvAccountType = (TextView) itemView.findViewById(app.alansari.R.id.account_type);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, beneficiaryList.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}
