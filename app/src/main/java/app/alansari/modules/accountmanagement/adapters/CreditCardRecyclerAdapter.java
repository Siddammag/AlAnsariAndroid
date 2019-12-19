package app.alansari.modules.accountmanagement.adapters;

import app.alansari.Utils.CardType;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Validation;
import app.alansari.customviews.MultiStateView;
import app.alansari.listeners.CustomClickListener;
import app.alansari.models.CreditCardData;
import android.content.Context;
import android.support.v4.content.ContextCompat;
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

import static app.alansari.customviews.MultiStateView.VIEW_STATE_EMPTY;

/**
 * Created by Parveen Dala on 19 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class CreditCardRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    public ArrayList<CreditCardData> itemList, originalList;
    private Context context;
    private LayoutInflater inflater;
    private MultiStateView multiStateView;
    private CustomClickListener clickListener;

    public CreditCardRecyclerAdapter(Context context, ArrayList<CreditCardData> itemList, MultiStateView multiStateView, CustomClickListener clickListener) {
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

   /* public void addItem(CreditCardData data) {
        this.itemList.add(data);
        this.originalList.add(data);
        notifyItemInserted(itemList.size() - 1);
    }*/

    public void addItemAt(CreditCardData data, int position) {
        this.itemList.add(position, data);
        this.originalList.add(position, data);
        notifyItemInserted(position);
    }

    public void replaceItemAt(CreditCardData data, int position) {
        itemList.set(position, data);
        originalList.set(position, data);
        notifyItemChanged(position);
    }

    public ArrayList<CreditCardData> getArrayList() {
        return itemList;
    }

    public ArrayList<CreditCardData> getOriginalArrayList() {
        return originalList;
    }

    public CreditCardData getItemAt(int position) {
        return itemList.get(position);
    }



    public void addArrayList(ArrayList<CreditCardData> itemList) {
        this.itemList.clear();
        this.originalList.clear();
        this.itemList = itemList;
        this.originalList.addAll(itemList);
        notifyDataSetChanged();
    }

   /* public void clear() {
        this.itemList.clear();
        this.originalList.clear();
        notifyDataSetChanged();
    }*/




    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CreditCardViewHolder(inflater.inflate(app.alansari.R.layout.item_credit_card, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof CreditCardViewHolder) {
            CreditCardData current = itemList.get(position);
            if ((position + 1) % 4 == 0) {
                ((CreditCardViewHolder) holder).bgLayout.setBackground(ContextCompat.getDrawable(context, app.alansari.R.drawable.credit_card_grey_bg));
            } else if ((position + 1) % 3 == 0) {
                ((CreditCardViewHolder) holder).bgLayout.setBackground(ContextCompat.getDrawable(context, app.alansari.R.drawable.credit_card_blue_dark_bg));
            } else if ((position + 1) % 2 == 0) {
                ((CreditCardViewHolder) holder).bgLayout.setBackground(ContextCompat.getDrawable(context, app.alansari.R.drawable.credit_card_blue_light_bg));
            } else {
                ((CreditCardViewHolder) holder).bgLayout.setBackground(ContextCompat.getDrawable(context, app.alansari.R.drawable.credit_card_black_bg));
            }

            if (Validation.isValidString(current.getName())) {
                ((CreditCardViewHolder) holder).tvName.setText(current.getName());
            } else {
                ((CreditCardViewHolder) holder).tvName.setText("");
            }

            if (Validation.isValidString(current.getBankName())) {
                ((CreditCardViewHolder) holder).tvBankName.setText(current.getBankName());
            } else {
                ((CreditCardViewHolder) holder).tvBankName.setText("");
            }

            try {
                if (Validation.isValidString(current.getCardNumber())) {
                    ((CreditCardViewHolder) holder).cardNumLayout.setVisibility(View.VISIBLE);
                    ((CreditCardViewHolder) holder).tvCardNum1.setText(String.valueOf(current.getCardNumber().substring(0, 4)));
                    ((CreditCardViewHolder) holder).tvCardNum2.setText("****");
                    ((CreditCardViewHolder) holder).tvCardNum3.setText("****");
                    ((CreditCardViewHolder) holder).tvCardNum4.setText(String.valueOf(current.getCardNumber().substring(12)));
                } else {
                    ((CreditCardViewHolder) holder).cardNumLayout.setVisibility(View.INVISIBLE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                ((CreditCardViewHolder) holder).cardNumLayout.setVisibility(View.INVISIBLE);
            }

            if (Validation.isValidString(current.getCardTypeName())) {
                /*if (current.getCardTypeName().equalsIgnoreCase("MASTER")) {
                    ((CreditCardViewHolder) holder).ivCardType.setImageResource(app.alansari.R.drawable.svg_card_master);
                    ((CreditCardViewHolder) holder).tvCardTypeName.setVisibility(View.VISIBLE);
                    ((CreditCardViewHolder) holder).tvCardTypeName.setText("mastercard");
                } else if (current.getCardTypeName().equalsIgnoreCase("VISA")) {
                    ((CreditCardViewHolder) holder).ivCardType.setImageResource(app.alansari.R.drawable.icn_card_visa);
                    ((CreditCardViewHolder) holder).tvCardTypeName.setVisibility(View.GONE);
                } else if (current.getCardTypeName().equalsIgnoreCase("MAESTRO")) {
                    ((CreditCardViewHolder) holder).ivCardType.setImageResource(app.alansari.R.drawable.svg_card_maestro);
                    ((CreditCardViewHolder) holder).tvCardTypeName.setVisibility(View.VISIBLE);
                    ((CreditCardViewHolder) holder).tvCardTypeName.setText("maestro");
                } else if (current.getCardTypeName().equalsIgnoreCase("JCB")) {
                    ((CreditCardViewHolder) holder).ivCardType.setImageResource(app.alansari.R.drawable.icn_card_jcb);
                    ((CreditCardViewHolder) holder).tvCardTypeName.setVisibility(View.GONE);
                }*/
                ((CreditCardViewHolder) holder).tvCardTypeName.setText(current.getCardTypeName());
                String cardType = CardType.getCardType(String.valueOf(current.getCardNumber()));
                if (cardType != null) {
                    ((CreditCardViewHolder) holder).ivCardType.setImageResource(CardType.getCardLogo(cardType));
                }
            } else {
                ((CreditCardViewHolder) holder).ivCardType.setImageResource(0);
                ((CreditCardViewHolder) holder).tvCardTypeName.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                itemList.clear();
                itemList.addAll((ArrayList<CreditCardData>) results.values);
                if (itemList != null && itemList.size() > 0) {
                    multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                    multiStateView.findViewById(app.alansari.R.id.empty_button).setVisibility(View.VISIBLE);
                    multiStateView.findViewById(app.alansari.R.id.error_button).setVisibility(View.VISIBLE);
                } else {
                    CommonUtils.setViewState(multiStateView, VIEW_STATE_EMPTY, (TextView) multiStateView.findViewById(app.alansari.R.id.empty_textView), null, null);
                    multiStateView.findViewById(app.alansari.R.id.empty_button).setVisibility(View.GONE);
                    multiStateView.findViewById(app.alansari.R.id.error_button).setVisibility(View.GONE);
                }
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<CreditCardData> filteredResults = null;
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

    protected List<CreditCardData> getFilteredResults(String constraint) {
        List<CreditCardData> results = new ArrayList<>();
        for (CreditCardData item : originalList) {
            if (item.getName() != null && item.getName().toLowerCase().contains(constraint)) {
                results.add(item);
            } else if (item.getBankName() != null && item.getBankName().toLowerCase().contains(constraint)) {
                results.add(item);
            } else if (item.getCardNumber() != null && item.getCardNumber().toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }
        return results;
    }

    class CreditCardViewHolder extends RecyclerView.ViewHolder {
        View bgLayout, cardNumLayout;
        ImageView ivCardType;
        TextView tvName, tvBankName, tvCardNum1, tvCardNum2, tvCardNum3, tvCardNum4, tvCardTypeName;

        public CreditCardViewHolder(View itemView) {
            super(itemView);
            bgLayout = itemView.findViewById(app.alansari.R.id.bg_layout);
            ivCardType = (ImageView) itemView.findViewById(app.alansari.R.id.card_type);
            tvName = (TextView) itemView.findViewById(app.alansari.R.id.name);
            tvBankName = (TextView) itemView.findViewById(app.alansari.R.id.bank_name);
            cardNumLayout = itemView.findViewById(app.alansari.R.id.card_num_layout);
            tvCardNum1 = (TextView) itemView.findViewById(app.alansari.R.id.card_num_1);
            tvCardNum2 = (TextView) itemView.findViewById(app.alansari.R.id.card_num_2);
            tvCardNum3 = (TextView) itemView.findViewById(app.alansari.R.id.card_num_3);
            tvCardNum4 = (TextView) itemView.findViewById(app.alansari.R.id.card_num_4);
            tvCardTypeName = (TextView) itemView.findViewById(app.alansari.R.id.card_type_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.itemClicked(null, getAdapterPosition(), itemList.get(getAdapterPosition()));
                }
            });
        }
    }

}
