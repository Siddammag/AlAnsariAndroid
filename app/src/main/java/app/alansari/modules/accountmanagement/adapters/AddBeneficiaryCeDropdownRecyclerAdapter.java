package app.alansari.modules.accountmanagement.adapters;

import app.alansari.Utils.CommonUtils;
import app.alansari.customviews.MultiStateView;
import app.alansari.listeners.CustomClickListener;
import app.alansari.models.LoadDropDownField;
import app.alansari.models.additionalforCe.ContributionCeList;
import app.alansari.models.additionalforCe.SourcesOfFundLists;
import app.alansari.modules.accountmanagement.models.BankPurposeCeData;
import app.alansari.modules.accountmanagement.models.BeneficiaryAccountTypeCeData;
import app.alansari.modules.accountmanagement.models.BeneficiaryBranchCeData;
import app.alansari.modules.accountmanagement.models.BeneficiaryTypeCeData;
import app.alansari.modules.accountmanagement.models.BusinessActivitiesCeData;
import app.alansari.modules.accountmanagement.models.BusinessTypeCeData;
import app.alansari.modules.accountmanagement.models.CompanyTypeCeData;
import app.alansari.modules.accountmanagement.models.ContributionCeData;
import app.alansari.modules.accountmanagement.models.DistrictCeData;
import app.alansari.modules.accountmanagement.models.FundsSourceCeData;
import app.alansari.modules.accountmanagement.models.IdDateTypeCeData;
import app.alansari.modules.accountmanagement.models.IdProofCeData;
import app.alansari.modules.accountmanagement.models.ProfessionCeData;
import app.alansari.modules.accountmanagement.models.PurposeCeData;
import app.alansari.modules.accountmanagement.models.ResidentialStatusCeData;
import app.alansari.modules.accountmanagement.models.SubBusinessTypeCeData;
import app.alansari.modules.accountmanagement.models.SubPurposeCeData;
import app.alansari.modules.accountmanagement.models.TradeLicenseDateTypeCeData;
import app.alansari.modules.accountmanagement.models.TradeLicenseTypeCeData;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static app.alansari.customviews.MultiStateView.VIEW_STATE_EMPTY;


/**
 * Created by Parveen Dala on 12 July, 2017
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class AddBeneficiaryCeDropdownRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    public int selectedItem;
    private LayoutInflater inflater;
    private MultiStateView multiStateView;
    private ArrayList<T> itemList, originalList;
    private CustomClickListener customClickListener;

    public AddBeneficiaryCeDropdownRecyclerAdapter(Context context, ArrayList<T> itemList, CustomClickListener customClickListener, MultiStateView multiStateView) {
        this.itemList = itemList;
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

    public void addItem(T data) {
        this.itemList.add(data);
        this.originalList.add(data);
        notifyItemInserted(itemList.size() - 1);
    }

    public void addItemAt(T data, int position) {
        this.itemList.add(position, data);
        notifyItemInserted(position);
    }

    public void addItemAtCustomLLayout(T data, int position) {
        this.itemList.add(position, data);
        notifyDataSetChanged();
    }

    public T getItemAt(int position) {
        return itemList.get(position);
    }

    public ArrayList<T> getArrayList() {
        return itemList;
    }

    public void addArrayList(ArrayList<T> itemList) {
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
        return new SelectItemViewHolder(inflater.inflate(app.alansari.R.layout.item_select_item, parent, false));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SelectItemViewHolder) {
            T current = itemList.get(position);
            ((SelectItemViewHolder) holder).tvDescription.setVisibility(View.GONE);
            if (current instanceof FundsSourceCeData)
                ((SelectItemViewHolder) holder).tvName.setText(((FundsSourceCeData) current).getName());
            else if (current instanceof PurposeCeData)
                ((SelectItemViewHolder) holder).tvName.setText(((PurposeCeData) current).getName());
            else if (current instanceof IdProofCeData)
                ((SelectItemViewHolder) holder).tvName.setText(((IdProofCeData) current).getIdTypeDesc());
            else if (current instanceof IdDateTypeCeData)
                ((SelectItemViewHolder) holder).tvName.setText(((IdDateTypeCeData) current).getName());
            else if (current instanceof ResidentialStatusCeData)
                ((SelectItemViewHolder) holder).tvName.setText(((ResidentialStatusCeData) current).getName());
            else if (current instanceof SubPurposeCeData)
                ((SelectItemViewHolder) holder).tvName.setText(((SubPurposeCeData) current).getName());
            else if (current instanceof ProfessionCeData)
                ((SelectItemViewHolder) holder).tvName.setText(((ProfessionCeData) current).getName());
            else if (current instanceof CompanyTypeCeData)
                ((SelectItemViewHolder) holder).tvName.setText(((CompanyTypeCeData) current).getName());
            else if (current instanceof TradeLicenseTypeCeData)
                ((SelectItemViewHolder) holder).tvName.setText(((TradeLicenseTypeCeData) current).getIdTypeDesc());
            else if (current instanceof TradeLicenseDateTypeCeData)
                ((SelectItemViewHolder) holder).tvName.setText(((TradeLicenseDateTypeCeData) current).getName());
            else if (current instanceof BusinessActivitiesCeData)
                ((SelectItemViewHolder) holder).tvName.setText(((BusinessActivitiesCeData) current).getName());
            else if (current instanceof BeneficiaryBranchCeData) {
                ((SelectItemViewHolder) holder).tvName.setText(((BeneficiaryBranchCeData) current).getName());

                ((SelectItemViewHolder) holder).tvDescription.setText(((BeneficiaryBranchCeData) current).getBranchAddress());
                ((SelectItemViewHolder) holder).tvDescription.setVisibility(View.VISIBLE);
            } else if (current instanceof BeneficiaryAccountTypeCeData)
                ((SelectItemViewHolder) holder).tvName.setText(((BeneficiaryAccountTypeCeData) current).getAccountType());
            else if (current instanceof BankPurposeCeData)
                ((SelectItemViewHolder) holder).tvName.setText(((BankPurposeCeData) current).getPurposeDesc());
            else if (current instanceof DistrictCeData)
                ((SelectItemViewHolder) holder).tvName.setText(((DistrictCeData) current).getName());
            else if (current instanceof BeneficiaryTypeCeData)
                ((SelectItemViewHolder) holder).tvName.setText(((BeneficiaryTypeCeData) current).getBeneficiaryTypeDesc());
            else if (current instanceof ContributionCeData)
                ((SelectItemViewHolder) holder).tvName.setText(((ContributionCeData) current).getContributionAmount());
            else if (current instanceof BusinessTypeCeData)
                ((SelectItemViewHolder) holder).tvName.setText(((BusinessTypeCeData) current).getBusinessTypeDesc());
            else if (current instanceof SubBusinessTypeCeData)
                ((SelectItemViewHolder) holder).tvName.setText(((SubBusinessTypeCeData) current).getName());


            else if (current instanceof LoadDropDownField)
                ((SelectItemViewHolder) holder).tvName.setText(((LoadDropDownField) current).getDisplay_key());

           else if(current instanceof SourcesOfFundLists)
                ((SelectItemViewHolder) holder).tvName.setText(((SourcesOfFundLists) current).getDisplayValue());
            else if(current instanceof ContributionCeList)
                ((SelectItemViewHolder) holder).tvName.setText(((ContributionCeList) current).getDisplayValue());
            else if (current instanceof String)
                ((SelectItemViewHolder) holder).tvName.setText((String) current);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                itemList.clear();
                itemList.addAll((ArrayList<T>) results.values);
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
                List<T> filteredResults = null;
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

    private boolean localFilter(String item, String constraint) {
        if (item != null && item.toLowerCase().contains(constraint))
            return true;
        return false;
    }

    protected List<T> getFilteredResults(String constraint) {
        List<T> results = new ArrayList<>();
        for (T item : originalList) {
            if (item instanceof FundsSourceCeData) {
                if (localFilter(((FundsSourceCeData) item).getName(), constraint))
                    results.add(item);
            } else if (item instanceof PurposeCeData) {
                if (localFilter(((PurposeCeData) item).getName(), constraint))
                    results.add(item);
            } else if (item instanceof IdProofCeData) {
                if (localFilter(((IdProofCeData) item).getIdTypeDesc(), constraint))
                    results.add(item);
            } else if (item instanceof IdDateTypeCeData) {
                if (localFilter(((IdDateTypeCeData) item).getName(), constraint))
                    results.add(item);
            } else if (item instanceof ResidentialStatusCeData) {
                if (localFilter(((ResidentialStatusCeData) item).getName(), constraint))
                    results.add(item);
            } else if (item instanceof SubPurposeCeData) {
                if (localFilter(((SubPurposeCeData) item).getName(), constraint))
                    results.add(item);
            } else if (item instanceof ProfessionCeData) {
                if (localFilter(((ProfessionCeData) item).getName(), constraint))
                    results.add(item);
            } else if (item instanceof CompanyTypeCeData) {
                if (localFilter(((CompanyTypeCeData) item).getName(), constraint))
                    results.add(item);
            } else if (item instanceof TradeLicenseTypeCeData) {
                if (localFilter(((TradeLicenseTypeCeData) item).getIdTypeDesc(), constraint))
                    results.add(item);
            } else if (item instanceof TradeLicenseDateTypeCeData) {
                if (localFilter(((TradeLicenseDateTypeCeData) item).getName(), constraint))
                    results.add(item);
            } else if (item instanceof BusinessActivitiesCeData) {
                if (localFilter(((BusinessActivitiesCeData) item).getName(), constraint))
                    results.add(item);
            } else if (item instanceof BeneficiaryBranchCeData) {
                if (localFilter(((BeneficiaryBranchCeData) item).getName(), constraint))
                    results.add(item);
                else if (localFilter(((BeneficiaryBranchCeData) item).getBranchAddress(), constraint))
                    results.add(item);
            } else if (item instanceof BeneficiaryAccountTypeCeData) {
                if (localFilter(((BeneficiaryAccountTypeCeData) item).getAccountType(), constraint))
                    results.add(item);
            } else if (item instanceof BankPurposeCeData) {
                if (localFilter(((BankPurposeCeData) item).getPurposeDesc(), constraint))
                    results.add(item);
            } else if (item instanceof DistrictCeData) {
                if (localFilter(((DistrictCeData) item).getName(), constraint))
                    results.add(item);
            } else if (item instanceof BeneficiaryTypeCeData) {
                if (localFilter(((BeneficiaryTypeCeData) item).getBeneficiaryTypeDesc(), constraint))
                    results.add(item);
            } else if (item instanceof ContributionCeData) {
                if (localFilter(((ContributionCeData) item).getContributionAmount(), constraint))
                    results.add(item);
            } else if (item instanceof BusinessTypeCeData) {
                if (localFilter(((BusinessTypeCeData) item).getBusinessTypeDesc(), constraint))
                    results.add(item);
            } else if (item instanceof SubBusinessTypeCeData) {
                if (localFilter(((SubBusinessTypeCeData) item).getName(), constraint))
                    results.add(item);
            } else if (item instanceof LoadDropDownField) {
                    if (localFilter(((LoadDropDownField) item).getDisplay_key(), constraint))
                        results.add(item);

            }else if (item instanceof SourcesOfFundLists) {
                if (localFilter(((SourcesOfFundLists) item).getDisplayValue(), constraint))
                    results.add(item);

            }else if (item instanceof ContributionCeList){
                if (localFilter(((ContributionCeList) item).getDisplayValue(), constraint))
                    results.add(item);

            } else if (item instanceof String) {
                if (item != null && ((String) item).toLowerCase().contains(constraint))
                    results.add(item);
            }
        }
        return results;
    }

    class SelectItemViewHolder<T> extends RecyclerView.ViewHolder {
        TextView tvName, tvDescription;

        public SelectItemViewHolder(View itemView) {
            super(itemView);
            {
                tvName = (TextView) itemView.findViewById(app.alansari.R.id.name);
                tvDescription = (TextView) itemView.findViewById(app.alansari.R.id.description);
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
