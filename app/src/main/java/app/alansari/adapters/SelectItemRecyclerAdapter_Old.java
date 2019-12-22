package app.alansari.adapters;

import app.alansari.listeners.CustomClickListener;
import app.alansari.models.SelectItemData;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Parveen Dala on 19 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class SelectItemRecyclerAdapter_Old extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public int selectedItem;
    private LayoutInflater inflater;
    private ArrayList<SelectItemData> itemList;
    private CustomClickListener customClickListener;

    public SelectItemRecyclerAdapter_Old(Context context, ArrayList<SelectItemData> itemList, CustomClickListener customClickListener) {
        this.itemList = itemList;
        inflater = LayoutInflater.from(context);
        this.customClickListener = customClickListener;
    }

    public void delete(int position) {
        itemList.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(SelectItemData studentClass) {
        this.itemList.add(studentClass);
        notifyItemInserted(itemList.size() - 1);
    }

    public void addItemAt(SelectItemData studentClass, int position) {
        this.itemList.add(position, studentClass);
        notifyItemInserted(position);
    }

    public void addItemAtCustomLLayout(SelectItemData studentClass, int position) {
        this.itemList.add(position, studentClass);
        notifyDataSetChanged();
    }

    public SelectItemData getItemAt(int position) {
        return itemList.get(position);
    }

    public ArrayList<SelectItemData> getArrayList() {
        return itemList;
    }

    public void addArrayList(ArrayList<SelectItemData> itemList) {
        this.itemList.clear();
        this.itemList = itemList;
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
        return new SelectItemViewHolder(inflater.inflate(app.alansari.R.layout.item_select_item, parent, false));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SelectItemViewHolder) {
            SelectItemData current = itemList.get(position);
            ((SelectItemViewHolder) holder).tvName.setText(current.getName());
        }
    }

    class SelectItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;

        public SelectItemViewHolder(View itemView) {
            super(itemView);
            {
                tvName = (TextView) itemView.findViewById(app.alansari.R.id.name);

                itemView.setOnClickListener(new OnClickListener() {

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
