package app.alansari.adapters;

import android.content.Context;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.customviews.MultiStateView;
import app.alansari.listeners.CustomClickListener;
import app.alansari.models.CurrencyData;
import app.alansari.models.travalcardvalidateflag.TravelCardFlag;

import static app.alansari.customviews.MultiStateView.VIEW_STATE_EMPTY;

public class SelectCurrencyTravelCardRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    public int selectedItem = 0, selectionType;
    public boolean isKeyboardVisible;
    private Context context;
    private LayoutInflater inflater;
    private MultiStateView multiStateView;
    private ArrayList<TravelCardFlag> itemList, originalList;
    private CustomClickListener customClickListener;

    public SelectCurrencyTravelCardRecyclerAdapter(Context context, ArrayList<TravelCardFlag> itemList, CustomClickListener customClickListener, MultiStateView multiStateView, int selectionType) {
        this.itemList = itemList;
        this.context = context;
        this.selectionType = selectionType;
        this.originalList = new ArrayList<>();
        this.originalList.addAll(itemList);
        this.multiStateView = multiStateView;
        inflater = LayoutInflater.from(context);
        this.customClickListener = customClickListener;
    }

    public void delete(int position) {
        itemList.remove(position);
        originalList.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(TravelCardFlag data) {
        this.itemList.add(data);
        this.originalList.add(data);
        notifyItemInserted(itemList.size() - 1);
    }

    public void addItemAt(TravelCardFlag data, int position) {
        this.itemList.add(position, data);
        notifyItemInserted(position);
    }

    public void addItemAtCustomLLayout(TravelCardFlag data, int position) {
        this.itemList.add(position, data);
        notifyDataSetChanged();
    }


    public TravelCardFlag getItemAt(int position) {
        return itemList.get(position);
    }

    public ArrayList<TravelCardFlag> getArrayList() {
        return itemList;
    }

    public void addArrayList(ArrayList<TravelCardFlag> itemList) {
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
        return new SelectItemViewHolder(inflater.inflate(app.alansari.R.layout.item_select_country_flag, parent, false));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SelectItemViewHolder) {
            TravelCardFlag current = itemList.get(position);

            CommonUtils.setCountryFlagImage(context, ((SelectItemViewHolder) holder).ivFlag, current.getFLAG());
            ((SelectItemViewHolder) holder).tvName.setText(current.getCCYDESC());

        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                itemList.clear();
                itemList.addAll((ArrayList<TravelCardFlag>) results.values);
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
                List<TravelCardFlag> filteredResults = null;
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

    protected List<TravelCardFlag> getFilteredResults(String constraint) {
        List<TravelCardFlag> results = new ArrayList<>();
        for (TravelCardFlag item : originalList) {
            if (item.getCCYCODE() != null && item.getCCYCODE().toLowerCase().contains(constraint.toLowerCase())) {
                results.add(item);
            } else if (item.getCCYDESC() != null && item.getCCYDESC().toLowerCase().contains(constraint.toLowerCase())) {
                results.add(item);
            }
        }
        return results;
    }

    class SelectItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        AppCompatImageView ivFlag;

        public SelectItemViewHolder(View itemView) {
            super(itemView);
            {
                ivFlag = (AppCompatImageView) itemView.findViewById(app.alansari.R.id.flag);
                tvName = (TextView) itemView.findViewById(app.alansari.R.id.name);

                itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        selectedItem = getAdapterPosition();
                        customClickListener.itemClicked(v, getAdapterPosition(), itemList.get(getAdapterPosition()));
                    }
                });
            }
        }
    }
}
