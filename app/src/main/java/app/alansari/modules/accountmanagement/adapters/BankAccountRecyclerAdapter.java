package app.alansari.modules.accountmanagement.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.core.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.alansari.R;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Validation;
import app.alansari.customviews.MultiStateView;
import app.alansari.listeners.CustomClickListener;
import app.alansari.models.UserAccountData;
import app.alansari.textdrawable.TextDrawable;

import static app.alansari.customviews.MultiStateView.VIEW_STATE_EMPTY;

/**
 * Created by Parveen Dala on 19 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class BankAccountRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    public ArrayList<UserAccountData> itemList, originalList;
    private Context context;
    private LayoutInflater inflater;
    private MultiStateView multiStateView;
    private CustomClickListener clickListener;

    public BankAccountRecyclerAdapter(Context context, ArrayList<UserAccountData> itemList, MultiStateView multiStateView, CustomClickListener clickListener) {
        this.context = context;
        this.multiStateView = multiStateView;
        this.itemList = itemList;
        this.originalList = new ArrayList<>();
        this.originalList.addAll(itemList);
        this.clickListener = clickListener;
        inflater = LayoutInflater.from(context);
    }

    public void delete(int position) {
        itemList.remove(position);
        originalList.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(UserAccountData data) {
        this.itemList.add(data);
        this.originalList.add(data);
        notifyItemInserted(itemList.size() - 1);
    }

    public void addItemAt(UserAccountData data, int position) {
        this.itemList.add(position, data);
        this.originalList.add(position, data);
        notifyItemInserted(position);
    }

    public void replaceItemAt(UserAccountData data, int position) {
        itemList.set(position, data);
        originalList.set(position, data);
        notifyItemChanged(position);
    }

    public ArrayList<UserAccountData> getArrayList() {
        return itemList;
    }

    public ArrayList<UserAccountData> getOriginalArrayList() {
        return originalList;
    }

    public UserAccountData getItemAt(int position) {
        return itemList.get(position);
    }

    public void addArrayList(ArrayList<UserAccountData> itemList) {
        this.itemList.clear();
        this.originalList.clear();
        this.itemList = itemList;
        this.originalList.addAll(itemList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.itemList.clear();
        this.originalList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BankAccountViewHolder(inflater.inflate(R.layout.item_bank_account, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        UserAccountData current = itemList.get(position);
        if (position % 2 == 0) {
            ((BankAccountViewHolder) holder).bgLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.beneficiary_card_dark_bg));
        } else {
            ((BankAccountViewHolder) holder).bgLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.beneficiary_card_light_bg));
        }
        if (Validation.isValidString(current.getAccountName())) {
            String name = "";
            try {
                if (current.getAccountName().split(" ").length >= 1) {
                    name = String.valueOf(current.getAccountName().split(" ")[0].trim().toUpperCase().charAt(0) + "" + (current.getAccountName().split(" ").length > 1 ? current.getAccountName().split(" ")[1].toUpperCase().charAt(0) : ""));
                } else {
                    name = String.valueOf(current.getAccountName().trim().toUpperCase().charAt(0));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                name = String.valueOf(current.getAccountName().trim().toUpperCase().charAt(0));
            }
            ((BankAccountViewHolder) holder).tvName.setText(current.getAccountName());
            ((BankAccountViewHolder) holder).ivProfileText.setImageDrawable(TextDrawable
                    .builder()
                    .beginConfig()
                    .textColor(Color.BLACK)
                    .bold()
                    .useFont(Typeface.create("fonts/HelveticaNeue-Medium.ttf", Typeface.NORMAL))
                    .endConfig()
                    .buildRound(name, ContextCompat.getColor(context, R.color.colorTransparent)));
        } else {
            ((BankAccountViewHolder) holder).tvName.setText("");
        }

        if (Validation.isValidString(current.getBankName())) {
            ((BankAccountViewHolder) holder).tvBankName.setText(current.getBankName());
        } else {
            ((BankAccountViewHolder) holder).tvBankName.setText("");
        }

        if (Validation.isValidString(current.getIBANNumber())) {
            ((BankAccountViewHolder) holder).tvIBANNum.setText(current.getIBANNumber());
        } else {
            ((BankAccountViewHolder) holder).tvIBANNum.setText("");
        }

        if (Validation.isValidString(current.getBankBranchName())) {
            ((BankAccountViewHolder) holder).tvBranchName.setText(current.getBankBranchName());
        } else {
            ((BankAccountViewHolder) holder).tvBranchName.setText("");
        }
        ((BankAccountViewHolder) holder).ivProfilePic.setVisibility(View.GONE);
        ((BankAccountViewHolder) holder).ivProfileText.setVisibility(View.GONE);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                itemList.clear();
                itemList.addAll((ArrayList<UserAccountData>) results.values);
                if (itemList != null && itemList.size() > 0) {
                    multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                    multiStateView.findViewById(R.id.empty_button).setVisibility(View.VISIBLE);
                    multiStateView.findViewById(R.id.error_button).setVisibility(View.VISIBLE);
                } else {
                    CommonUtils.setViewState(multiStateView, VIEW_STATE_EMPTY, (TextView) multiStateView.findViewById(R.id.empty_textView), null, null);
                    multiStateView.findViewById(R.id.empty_button).setVisibility(View.GONE);
                    multiStateView.findViewById(R.id.error_button).setVisibility(View.GONE);
                }
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<UserAccountData> filteredResults = null;
                if (constraint.length() == 0) {
                    filteredResults = originalList;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }
                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }

    protected List<UserAccountData> getFilteredResults(String constraint) {
        List<UserAccountData> results = new ArrayList<>();
        for (UserAccountData item : originalList) {
            if (item.getAccountName() != null && item.getAccountName().toLowerCase().contains(constraint)) {
                results.add(item);
            } else if (item.getBankName() != null && item.getBankName().toLowerCase().contains(constraint)) {
                results.add(item);
            } else if (item.getBankBranchName() != null && item.getBankBranchName().toLowerCase().contains(constraint)) {
                results.add(item);
            } else if (item.getIBANNumber() != null && item.getIBANNumber().toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }
        return results;
    }

    class BankAccountViewHolder extends RecyclerView.ViewHolder {
        View bgLayout, detailsLayout;
        AppCompatImageView ivProfilePic;
        ImageView ivProfileText;
        View llBranchName, llIBANNum;
        TextView tvName, tvBankName, tvBranchName, tvIBANNum;

        public BankAccountViewHolder(View itemView) {
            super(itemView);
            bgLayout = itemView.findViewById(R.id.bg_layout);
            detailsLayout = itemView.findViewById(R.id.details_layout);
            ivProfilePic = (AppCompatImageView) itemView.findViewById(R.id.profile_pic);
            ivProfileText = (ImageView) itemView.findViewById(R.id.profile_pic_text);
            tvName = (TextView) itemView.findViewById(R.id.name);
            tvBankName = (TextView) itemView.findViewById(R.id.bank_name);
            tvBranchName = (TextView) itemView.findViewById(R.id.branch_name);
            tvIBANNum = (TextView) itemView.findViewById(R.id.iban_num);

            llBranchName = itemView.findViewById(R.id.branch_name_layout);
            llIBANNum = itemView.findViewById(R.id.iban_num_layout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.itemClicked(null, getAdapterPosition(), itemList.get(getAdapterPosition()));
                }
            });
        }
    }
}
