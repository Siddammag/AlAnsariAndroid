package app.alansari.modules.branchlocator.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.core.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.customviews.MultiStateView;
import app.alansari.models.BranchLocatorData;
import app.alansari.modules.branchlocator.BranchLocatorMapActivity;

/**
 * Created by Parveen Dala on 04 November, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class BranchLocatorRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private Context context;
    private LayoutInflater inflater;
    private MultiStateView multiStateView;
    private ArrayList<BranchLocatorData> branchList, originalList;

    public BranchLocatorRecyclerAdapter(Context context, ArrayList<BranchLocatorData> branchList, MultiStateView multiStateView) {
        this.context = context;
        this.multiStateView = multiStateView;
        this.branchList = branchList;
        this.originalList = new ArrayList<>();
        this.originalList.addAll(branchList);
        inflater = LayoutInflater.from(context);
    }

    public void delete(int position) {
        branchList.remove(position);
        originalList.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(BranchLocatorData data) {
        this.branchList.add(data);
        this.originalList.add(data);
        notifyItemInserted(branchList.size() - 1);
    }

    public void addItemAt(BranchLocatorData data, int position) {
        this.branchList.add(position, data);
        notifyItemInserted(position);
    }

    public void replaceItemAt(BranchLocatorData data, int position) {
        branchList.set(position, data);
        notifyItemChanged(position);
    }

    public void addItemAtCustomLLayout(BranchLocatorData data, int position) {
        this.branchList.add(position, data);
        notifyDataSetChanged();
    }

    public BranchLocatorData getItemAt(int position) {
        return branchList.get(position);
    }

    public ArrayList<BranchLocatorData> getArrayList() {
        return branchList;
    }

    public void addArrayList(ArrayList<BranchLocatorData> branchList) {
        this.branchList.clear();
        this.originalList.clear();
        this.branchList = branchList;
        this.originalList.addAll(branchList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.branchList.clear();
        this.originalList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return branchList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MemberViewHolder(inflater.inflate(app.alansari.R.layout.item_branch_locator, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        BranchLocatorData current = branchList.get(position);

        if (current.getName() != null && current.getName().trim().length() > 0) {
            ((MemberViewHolder) holder).tvName.setText(current.getName());
        } else {
            ((MemberViewHolder) holder).tvName.setText("");
        }

        if (CommonUtils.getBranchStatus(current.getOpeningHours())) {
            ((MemberViewHolder) holder).tvOpenStatus.setText("Open Now");
            ((MemberViewHolder) holder).tvOpenStatus.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.color62A930));
            ((MemberViewHolder) holder).ivOpenStatus.setImageResource(app.alansari.R.drawable.svg_branch_open);
        } else {
            ((MemberViewHolder) holder).tvOpenStatus.setText("Closed Now");
            ((MemberViewHolder) holder).tvOpenStatus.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.colorE86768));
            ((MemberViewHolder) holder).ivOpenStatus.setImageResource(app.alansari.R.drawable.svg_branch_close);
        }
        try {
            ((MemberViewHolder) holder).tvTimings.setText(CommonUtils.getTimings(current.getOpeningHours()));
        } catch (Exception e) {

        }
        ((MemberViewHolder) holder).tvPhone.setText(current.getMainPhone());

        if (current.getAddressLine1() != null && current.getAddressLine1().trim().length() > 0) {
            if (current.getAddressLine2() != null && current.getAddressLine2().trim().length() > 0)
                ((MemberViewHolder) holder).tvAddress.setText(current.getAddressLine1() + " " + current.getAddressLine2());
            else
                ((MemberViewHolder) holder).tvAddress.setText(current.getAddressLine1());
        } else {
            ((MemberViewHolder) holder).tvAddress.setText("");
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                branchList.clear();
                branchList.addAll((ArrayList<BranchLocatorData>) results.values);
                if (branchList != null && branchList.size() > 0) {
                    multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                    multiStateView.findViewById(app.alansari.R.id.empty_button).setVisibility(View.VISIBLE);
                    multiStateView.findViewById(app.alansari.R.id.error_button).setVisibility(View.VISIBLE);
                } else {
                    CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_EMPTY, (TextView) multiStateView.findViewById(app.alansari.R.id.empty_textView), null, null);
                    multiStateView.findViewById(app.alansari.R.id.empty_button).setVisibility(View.GONE);
                    multiStateView.findViewById(app.alansari.R.id.error_button).setVisibility(View.GONE);
                }
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<BranchLocatorData> filteredResults = null;
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

    protected List<BranchLocatorData> getFilteredResults(String constraint) {
        List<BranchLocatorData> results = new ArrayList<>();
        for (BranchLocatorData item : originalList) {
            if (item.getName() != null && item.getName().toLowerCase().contains(constraint)) {
                results.add(item);
            } else if (item.getAddressLine1() != null && item.getAddressLine1().toLowerCase().contains(constraint)) {
                results.add(item);
            } else if (item.getAddressLine2() != null && item.getAddressLine2().toLowerCase().contains(constraint)) {
                results.add(item);
            } else if (item.getCity() != null && item.getCity().toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }
        return results;
    }

    class MemberViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView ivProfilePic;
        AppCompatImageView ivOpenStatus;
        AppCompatImageView ivMap;
        TextView tvName, tvOpenStatus, tvAddress, tvTimings, tvCall, tvPhone;

        public MemberViewHolder(View itemView) {
            super(itemView);
            {
                ivProfilePic = (AppCompatImageView) itemView.findViewById(app.alansari.R.id.profile_pic);
                ivOpenStatus = (AppCompatImageView) itemView.findViewById(app.alansari.R.id.open_status_icon);
                ivMap = (AppCompatImageView) itemView.findViewById(app.alansari.R.id.map_icon);
                tvName = (TextView) itemView.findViewById(app.alansari.R.id.name);
                tvOpenStatus = (TextView) itemView.findViewById(app.alansari.R.id.open_status);
                tvAddress = (TextView) itemView.findViewById(app.alansari.R.id.address);
                tvTimings = (TextView) itemView.findViewById(app.alansari.R.id.timings);
                tvPhone = (TextView) itemView.findViewById(app.alansari.R.id.phone);
                tvCall = (TextView) itemView.findViewById(app.alansari.R.id.call);

                itemView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, BranchLocatorMapActivity.class);
                        intent.putExtra(Constants.OBJECT, branchList.get(getAdapterPosition()));
                        context.startActivity(intent);
                    }
                });
                tvCall.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + branchList.get(getAdapterPosition()).getMainPhone()));
                        context.startActivity(intent);
                    }
                });
            }
        }
    }
}