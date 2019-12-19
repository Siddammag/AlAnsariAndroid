package app.alansari.modules.feedback.adapters;

import app.alansari.Utils.CommonUtils;
import app.alansari.customviews.MultiStateView;
import app.alansari.models.FaqData;
import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static app.alansari.customviews.MultiStateView.VIEW_STATE_EMPTY;

/**
 * Created by Parveen Dala on 20 February, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class FaqRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    public ArrayList<FaqData> itemList, originalList;
    private int selectedPosition;
    private LayoutInflater inflater;
    private MultiStateView multiStateView;

    public FaqRecyclerAdapter(Context context, ArrayList<FaqData> itemList, MultiStateView multiStateView) {
        this.multiStateView = multiStateView;
        this.itemList = itemList;
        this.originalList = new ArrayList<>();
        this.originalList.addAll(itemList);
        inflater = LayoutInflater.from(context);
    }

    public void delete(int position) {
        itemList.remove(position);
        originalList.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(FaqData data) {
        this.itemList.add(data);
        this.originalList.add(data);
        notifyItemInserted(itemList.size() - 1);
    }

    public void addItemAt(FaqData data, int position) {
        this.itemList.add(position, data);
        this.originalList.add(position, data);
        notifyItemInserted(position);
    }

    public void replaceItemAt(FaqData data, int position) {
        itemList.set(position, data);
        originalList.set(position, data);
        notifyItemChanged(position);
    }

    public ArrayList<FaqData> getArrayList() {
        return itemList;
    }

    public ArrayList<FaqData> getOriginalArrayList() {
        return originalList;
    }

    public FaqData getItemAt(int position) {
        return itemList.get(position);
    }

    public void addArrayList(ArrayList<FaqData> itemList) {
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
        return new BankAccountViewHolder(inflater.inflate(app.alansari.R.layout.item_faq, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        FaqData current = itemList.get(position);
        if (current.getIsReading() == 1) {
            selectedPosition = position;
            ((BankAccountViewHolder) holder).ivArrow.setImageResource(app.alansari.R.drawable.ic_up_arrow);
            ((BankAccountViewHolder) holder).tvAnswer.setVisibility(View.VISIBLE);
        } else {
            ((BankAccountViewHolder) holder).ivArrow.setImageResource(app.alansari.R.drawable.ic_down_arrow);
            ((BankAccountViewHolder) holder).tvAnswer.setVisibility(View.GONE);
        }

        if (current.getQuestion() != null && current.getQuestion().trim().length() > 0) {
            ((BankAccountViewHolder) holder).tvQuestion.setText(current.getQuestion());
        } else {
            ((BankAccountViewHolder) holder).tvQuestion.setText("");
        }

        if (current.getAnswer() != null && current.getAnswer().trim().length() > 0) {
            ((BankAccountViewHolder) holder).tvAnswer.setText(String.valueOf(current.getAnswer()));
        } else {
            ((BankAccountViewHolder) holder).tvAnswer.setText("");
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                itemList.clear();
                itemList.addAll((ArrayList<FaqData>) results.values);
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
                List<FaqData> filteredResults = null;
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

    protected List<FaqData> getFilteredResults(String constraint) {
        List<FaqData> results = new ArrayList<>();
        for (FaqData item : originalList) {
            if (item.getQuestion() != null && item.getQuestion().toLowerCase().contains(constraint)) {
                results.add(item);
            } else if (item.getAnswer() != null && item.getAnswer().toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }
        return results;
    }

    class BankAccountViewHolder extends RecyclerView.ViewHolder {
        View questionLayout;
        AppCompatImageView ivArrow;
        TextView tvQuestion, tvAnswer;

        public BankAccountViewHolder(View itemView) {
            super(itemView);
            questionLayout = itemView.findViewById(app.alansari.R.id.question_layout);
            ivArrow = (AppCompatImageView) itemView.findViewById(app.alansari.R.id.question_arrow);
            tvQuestion = (TextView) itemView.findViewById(app.alansari.R.id.question);
            tvAnswer = (TextView) itemView.findViewById(app.alansari.R.id.answer);

            questionLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedPosition == getAdapterPosition()) {
                        itemList.get(getAdapterPosition()).setIsReading(itemList.get(getAdapterPosition()).getIsReading() == 1 ? 0 : 1);
                    } else {
                        itemList.get(selectedPosition).setIsReading(0);
                        itemList.get(getAdapterPosition()).setIsReading(1);
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }
}
