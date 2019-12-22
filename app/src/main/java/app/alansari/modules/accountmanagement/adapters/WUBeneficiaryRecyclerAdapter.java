package app.alansari.modules.accountmanagement.adapters;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.alansari.AppController;
import app.alansari.R;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.OnLoadMoreListener;
import app.alansari.customviews.MultiStateView;
import app.alansari.listeners.CustomClickListener;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.WUBeneficiaryData;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.DELETE_BENEFICIARY;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_EMPTY;

/**
 * Created by Parveen Dala on 13 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class WUBeneficiaryRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private static final int VIEW_TYPE_BODY = 1;
    private static final int VIEW_TYPE_HEADER = 0;
    public int changedPosition = 0;
    public ArrayList<WUBeneficiaryData> beneficiaryList, originalList;
    private Context context;
    private LayoutInflater inflater;
    private CustomClickListener clickListener;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean isLoading;
    private OnLoadMoreListener onLoadMoreListener;
    private MultiStateView multiStateView;
    private String promoCode;

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    public WUBeneficiaryRecyclerAdapter(Context context, ArrayList<WUBeneficiaryData> beneficiaries, CustomClickListener clickListener, RecyclerView recyclerView, MultiStateView multiStateView, String promoCode) {
        this.context = context;
        this.beneficiaryList = beneficiaries;
        this.originalList = new ArrayList<>();
        this.originalList.addAll(beneficiaryList);
        this.clickListener = clickListener;
        this.multiStateView = multiStateView;
        this.promoCode=promoCode;
        inflater = LayoutInflater.from(context);

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (beneficiaryList.size() > 0) {
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        isLoading = true;
                    }
                }

            }
        });
    }

    public void delete(int position) {
        beneficiaryList.remove(position);
        originalList.remove(position);
        notifyItemRemoved(position + changedPosition);
    }

    public void addItem(WUBeneficiaryData data) {
        this.beneficiaryList.add(data);
        this.originalList.add(data);
        notifyItemInserted(beneficiaryList.size() - 1 + changedPosition);
    }

    public void addItemAt(WUBeneficiaryData data, int position) {
        this.beneficiaryList.add(position, data);
        notifyItemInserted(position + changedPosition);
    }

    public void replaceItemAt(WUBeneficiaryData data, int position) {
        beneficiaryList.set(position, data);
        notifyItemChanged(position + changedPosition);
    }

    public void addItemAtCustomLLayout(WUBeneficiaryData data, int position) {
        this.beneficiaryList.add(position, data);
        notifyDataSetChanged();
    }

    public ArrayList<WUBeneficiaryData> getArrayList() {
        return beneficiaryList;
    }

    public ArrayList<WUBeneficiaryData> getOriginalArrayList() {
        return originalList;
    }

    public WUBeneficiaryData getItemAt(int position) {
        return beneficiaryList.get(position);
    }

    public void addArrayList(ArrayList<WUBeneficiaryData> beneficiaryList) {
        isLoading = false;
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
            return new BeneficiaryHeaderViewHolder(inflater.inflate(R.layout.item_beneficiary_header, parent, false));
        } else {
            return new BeneficiaryViewHolder(inflater.inflate(R.layout.item_select_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof BeneficiaryViewHolder) {
            if (beneficiaryList.get(position).getReceiverNameType().equalsIgnoreCase("D")) {
                ((BeneficiaryViewHolder) holder).tvName.setText(CommonUtils.getValidString(beneficiaryList.get(position).getReceiverFirstName()) + " " + CommonUtils.getValidString(beneficiaryList.get(position).getReceiverMiddleName()) + " " + CommonUtils.getValidString(beneficiaryList.get(position).getReceiverLastName()));
            } else {
                ((BeneficiaryViewHolder) holder).tvName.setText(CommonUtils.getValidString(beneficiaryList.get(position).getReceiverFirstName()) + " " + CommonUtils.getValidString(beneficiaryList.get(position).getReceiverLastName()) + " " + CommonUtils.getValidString(beneficiaryList.get(position).getReceiverMiddleName()));
            }

            ((BeneficiaryViewHolder) holder).tvDescription.setText(beneficiaryList.get(position).getReceiverCountryName() + "-" + beneficiaryList.get(position).getReceiverCurrencyCode());
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                beneficiaryList.clear();
                beneficiaryList.addAll((ArrayList<WUBeneficiaryData>) results.values);
                if (beneficiaryList != null && beneficiaryList.size() > 0) {
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
                List<WUBeneficiaryData> filteredResults = null;
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

    protected List<WUBeneficiaryData> getFilteredResults(String constraint) {
        List<WUBeneficiaryData> results = new ArrayList<>();
        for (WUBeneficiaryData item : originalList) {
            if (localFilter(item.getReceiverFirstName(), constraint) || localFilter(item.getReceiverMiddleName(), constraint) || localFilter(item.getReceiverLastName(), constraint)) {
                results.add(item);
            }
        }
        return results;
    }


    private boolean localFilter(String item, String constraint) {
        if (item != null && item.toLowerCase().contains(constraint))
            return true;
        return false;
    }

    class BeneficiaryHeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvRate, tvTime;

        public BeneficiaryHeaderViewHolder(View view) {
            super(view);
            tvRate = (TextView) itemView.findViewById(R.id.rate);
            tvTime = (TextView) itemView.findViewById(R.id.time);
        }
    }

    class BeneficiaryViewHolder extends RecyclerView.ViewHolder implements OnWebServiceResult {
        TextView tvName, tvDescription, tvDescriptionTwo;

        public BeneficiaryViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(app.alansari.R.id.name);
            tvDescription = (TextView) itemView.findViewById(app.alansari.R.id.description);
            tvDescriptionTwo = (TextView) itemView.findViewById(app.alansari.R.id.description_two);
            tvDescriptionTwo.setVisibility(View.GONE);
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    clickListener.itemClicked(v, getAdapterPosition(), beneficiaryList.get(getAdapterPosition()));
                }
            });
        }

        private void deleteBeneficiary(String deletedBeneficiaryId, String serviceType) {
            if (NetworkStatus.getInstance(context).isOnline2(context)) {
                String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
                JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().deleteBeneficiary(deletedBeneficiaryId, serviceType,CommonUtils.getUserId(),sessionTime, LogoutCalling.getDeviceID(context)), Constants.DELETE_BENEFICIARY_URL, DELETE_BENEFICIARY, Request.Method.PUT, this);
                AppController.getInstance().getRequestQueue().cancelAll(DELETE_BENEFICIARY.toString());
                AppController.getInstance().addToRequestQueue(jsonObjReq, DELETE_BENEFICIARY.toString());
                CommonUtils.showLoading(context, context.getString(R.string.please_wait), DELETE_BENEFICIARY.toString(), false);
            } else {
                Toast.makeText(context, context.getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(context,response.getString("MESSAGE"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Toast.makeText(context, context.getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, context.getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }
}
