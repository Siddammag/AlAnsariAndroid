package app.alansari.modules.branchlocator.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.alansari.Utils.Constants;
import app.alansari.models.BranchLocatorCityData;
import app.alansari.modules.branchlocator.BranchLocatorActivity;
import app.alansari.textdrawable.ColorGenerator;
import app.alansari.textdrawable.TextDrawable;

/**
 * Created by Parveen Dala on 04 November, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class BranchLocatorCityRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<BranchLocatorCityData> itemList;

    public BranchLocatorCityRecyclerAdapter(Context context, ArrayList<BranchLocatorCityData> itemList) {
        this.context = context;
        this.itemList = itemList;
        inflater = LayoutInflater.from(context);
    }

    public void delete(int position) {
        itemList.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(BranchLocatorCityData studentClass) {
        this.itemList.add(studentClass);
        notifyItemInserted(itemList.size() - 1);
    }

    public void addItemAt(BranchLocatorCityData data, int position) {
        this.itemList.add(position, data);
        notifyItemInserted(position);
    }

    public void addItemAtCustomLLayout(BranchLocatorCityData data, int position) {
        this.itemList.add(position, data);
        notifyDataSetChanged();
    }

    public BranchLocatorCityData getItemAt(int position) {
        return itemList.get(position);
    }

    public ArrayList<BranchLocatorCityData> getArrayList() {
        return itemList;
    }

    public void addArrayList(ArrayList<BranchLocatorCityData> itemList) {
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
        return new BranchLocatorCityViewHolder(inflater.inflate(app.alansari.R.layout.item_branch_locator_city, parent, false));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BranchLocatorCityViewHolder) {
            BranchLocatorCityData current = itemList.get(position);

            if (current.getName() != null && current.getName().trim().length() > 0) {
                ((BranchLocatorCityViewHolder) holder).tvName.setText(current.getName());
                ((BranchLocatorCityViewHolder) holder).ivProfilePic.setImageDrawable(TextDrawable
                        .builder()
                        .beginConfig()
                        .textColor(Color.WHITE)
                        .bold()
                        .useFont(Typeface.create("fonts/HelveticaNeue-Medium.ttf", Typeface.NORMAL))
                        .endConfig()
                        .buildRound(String.valueOf(current.getName().charAt(0)), ColorGenerator.MATERIAL.getRandomColor()));
            } else {
                ((BranchLocatorCityViewHolder) holder).tvName.setText("");
            }

            if (position % 2 == 0) {
                ((BranchLocatorCityViewHolder) holder).tvName.setBackgroundColor(ContextCompat.getColor(context, app.alansari.R.color.colorWhite));
            } else {
                ((BranchLocatorCityViewHolder) holder).tvName.setBackgroundColor(ContextCompat.getColor(context, app.alansari.R.color.colorF8F9FA));
            }
        }
    }

    class BranchLocatorCityViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView ivProfilePic;

        public BranchLocatorCityViewHolder(View itemView) {
            super(itemView);
            {
                ivProfilePic = (ImageView) itemView.findViewById(app.alansari.R.id.profile_pic);
                tvName = (TextView) itemView.findViewById(app.alansari.R.id.name);

                itemView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (getAdapterPosition() != -1 && itemList != null && itemList.get(getAdapterPosition()) != null) {
                            Intent intent = new Intent(context, BranchLocatorActivity.class);
                            intent.putExtra(Constants.ID, itemList.get(getAdapterPosition()).getId());
                            context.startActivity(intent);
                        }

                    }
                });
            }
        }

    }
}
