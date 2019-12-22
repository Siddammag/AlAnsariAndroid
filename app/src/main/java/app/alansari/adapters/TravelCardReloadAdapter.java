package app.alansari.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.alansari.TravelCardReloadActivity;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Validation;
import app.alansari.customviews.MultiStateView;
import app.alansari.listeners.OnCustomClickListenerBoth;
import app.alansari.models.TravelCardReloadModel.TravelCardInfo;

import static app.alansari.customviews.MultiStateView.VIEW_STATE_EMPTY;

public class TravelCardReloadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private ArrayList<TravelCardInfo> itemList, originalList;
    private Context context;
    private LayoutInflater inflater;
    private MultiStateView multiStateView;
    private OnCustomClickListenerBoth clickListener;

    public TravelCardReloadAdapter(Context context, ArrayList<TravelCardInfo> itemList, MultiStateView multiStateView, TravelCardReloadActivity clickListener) {
        this.context = context;
        this.multiStateView = multiStateView;
        this.itemList = itemList;
        this.originalList = new ArrayList<>();
        this.originalList.addAll(originalList);
        this.clickListener = clickListener;
        inflater = LayoutInflater.from(context);
    }


    public void delete(int position) {
        itemList.remove(position);
        originalList.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TravelCardItemViewHolder(inflater.inflate(app.alansari.R.layout.travel_card_reload_adapter, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TravelCardItemViewHolder) {
            TravelCardInfo curent = itemList.get(position);
            if (Validation.isValidString(curent.getCARDHOLDERNAME())) {
                ((TravelCardItemViewHolder) holder).tvName.setText(curent.getCARDHOLDERNAME());

            } else {
                ((TravelCardItemViewHolder) holder).tvName.setText("");
            }
            try {
                if (Validation.isValidString(curent.getCARDNUMBER())) {
                    ((TravelCardItemViewHolder) holder).cardNumLayout.setVisibility(View.VISIBLE);
                    ((TravelCardItemViewHolder) holder).tvCardNum1.setText(String.valueOf(curent.getCARDNUMBER().substring(0, 4)));
                    ((TravelCardItemViewHolder) holder).tvCardNum2.setText("****");
                    ((TravelCardItemViewHolder) holder).tvCardNum3.setText("****");
                    ((TravelCardItemViewHolder) holder).tvCardNum4.setText(curent.getCARDNUMBER().substring(15));

                    // 4000-XXXX-XXXX-5944

                } else {
                    ((TravelCardItemViewHolder) holder).cardNumLayout.setVisibility(View.INVISIBLE);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                ((TravelCardItemViewHolder) holder).cardNumLayout.setVisibility(View.INVISIBLE);
            }


        }

    }
    public TravelCardInfo getItemAt(int position) {
        return itemList.get(position);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void addArrayList(ArrayList<TravelCardInfo> itemList) {
        this.itemList.clear();
        this.originalList.clear();
        this.itemList = itemList;
        this.originalList.addAll(itemList);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                itemList.clear();
                itemList.addAll((ArrayList<TravelCardInfo>) results.values);
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
                List<TravelCardInfo> filteredResults = null;
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

    protected List<TravelCardInfo> getFilteredResults(String constraint) {
        List<TravelCardInfo> results = new ArrayList<>();
        for (TravelCardInfo item : originalList) {
            if (item.getCARDHOLDERNAME() != null && item.getCARDHOLDERNAME().toLowerCase().contains(constraint)) {
                results.add(item);
            } else if (item.getCARDNUMBER() != null && item.getCARDNUMBER().toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }
        return results;
    }


    private class TravelCardItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCardNum1, tvCardNum2, tvCardNum3, tvCardNum4;
        View cardNumLayout;

        public TravelCardItemViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(app.alansari.R.id.name);
            cardNumLayout = itemView.findViewById(app.alansari.R.id.card_num_layout);
            tvCardNum1 = (TextView) itemView.findViewById(app.alansari.R.id.card_num_1);
            tvCardNum2 = (TextView) itemView.findViewById(app.alansari.R.id.card_num_2);
            tvCardNum3 = (TextView) itemView.findViewById(app.alansari.R.id.card_num_3);
            tvCardNum4 = (TextView) itemView.findViewById(app.alansari.R.id.card_num_4);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.itemClickedListerners(null, getAdapterPosition(), itemList.get(getAdapterPosition()));
                }
            });
        }
    }
}
