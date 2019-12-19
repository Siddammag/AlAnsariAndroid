package app.alansari.adapters;

import android.content.Context;
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
import app.alansari.customviews.MultiStateView;
import app.alansari.listeners.CustomClickListener;
import app.alansari.models.AccountTypeData;
import app.alansari.models.AdditionalInfoData;
import app.alansari.models.BankData;
import app.alansari.models.BranchData;
import app.alansari.models.BranchLocatorCityData;
import app.alansari.models.CreditCardBankData;
import app.alansari.models.CurrencyData;
import app.alansari.models.ProfessionalLoadList;
import app.alansari.models.ServiceTypeData;
import app.alansari.models.TransactionModeData;
import app.alansari.models.UserAccountBankData;
import app.alansari.models.UserAccountData;
import app.alansari.models.WuCurrencyData;
import app.alansari.models.additioninfowc.RESULTItem;
import app.alansari.models.gender.GenderSelection;
import app.alansari.models.servicetype.RESULTDTO;

import static app.alansari.customviews.MultiStateView.VIEW_STATE_EMPTY;


/**
 * Created by Parveen Dala on 19 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class SelectItemRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    public int selectedItem;
    private LayoutInflater inflater;
    private MultiStateView multiStateView;
    private ArrayList<T> itemList, originalList;
    private CustomClickListener customClickListener;

    public SelectItemRecyclerAdapter(Context context, ArrayList<T> itemList, CustomClickListener customClickListener, MultiStateView multiStateView) {
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
            if (current instanceof BankData)
                ((SelectItemViewHolder) holder).tvName.setText(((BankData) current).getBankName());
            else if (current instanceof AdditionalInfoData.ValuesData)
                ((SelectItemViewHolder) holder).tvName.setText(((AdditionalInfoData.ValuesData) current).getTitle());
            else if (current instanceof BranchData) {
                ((SelectItemViewHolder) holder).tvName.setText(((BranchData) current).getBranchName());
                ((SelectItemViewHolder) holder).tvDescription.setText(((BranchData) current).getBranchAddress());
                ((SelectItemViewHolder) holder).tvDescription.setVisibility(View.VISIBLE);
            } else if (current instanceof UserAccountData) {
                ((SelectItemViewHolder) holder).tvName.setText("Bank Name :" + ((UserAccountData) current).getBankName());
                ((SelectItemViewHolder) holder).tvDescription.setText("Account Name :" + ((UserAccountData) current).getAccountName());
                ((SelectItemViewHolder) holder).tvDescriptionTwo.setText("IBAN :" + ((UserAccountData) current).getIBANNumber());
                ((SelectItemViewHolder) holder).tvDescriptionTwo.setVisibility(View.VISIBLE);
                ((SelectItemViewHolder) holder).tvDescription.setVisibility(View.VISIBLE);
            } else if (current instanceof UserAccountBankData)
                ((SelectItemViewHolder) holder).tvName.setText(((UserAccountBankData) current).getBankName());
            else if (current instanceof CreditCardBankData)
                ((SelectItemViewHolder) holder).tvName.setText(((CreditCardBankData) current).getBankName());
            else if (current instanceof TransactionModeData)
                ((SelectItemViewHolder) holder).tvName.setText(((TransactionModeData) current).getName());
            else if (current instanceof ServiceTypeData)
                ((SelectItemViewHolder) holder).tvName.setText(((ServiceTypeData) current).getName());
            else if (current instanceof AccountTypeData)
                ((SelectItemViewHolder) holder).tvName.setText(((AccountTypeData) current).getName());
            else if (current instanceof String)
                ((SelectItemViewHolder) holder).tvName.setText((String) current);
            else if (current instanceof CurrencyData)
                ((SelectItemViewHolder) holder).tvName.setText(((CurrencyData) current).getNAME());
            else if(current instanceof RESULTItem.TXNPURPOSELISTItem)
                ((SelectItemViewHolder) holder).tvName.setText(((RESULTItem.TXNPURPOSELISTItem) current).getDisplayValue());
            else if(current instanceof RESULTItem.SOURCEOFFUNDItem)
                ((SelectItemViewHolder) holder).tvName.setText(((RESULTItem.SOURCEOFFUNDItem) current).getDisplayValue());
            else if(current instanceof RESULTDTO)
                ((SelectItemViewHolder) holder).tvName.setText(((RESULTDTO) current).getDisplayValue());
            else if (current instanceof GenderSelection)
                ((SelectItemViewHolder) holder).tvName.setText(((GenderSelection) current).getName());

            else if (current instanceof BranchLocatorCityData)
                ((SelectItemViewHolder) holder).tvName.setText((((BranchLocatorCityData) current).getName()));
            else if(current instanceof ProfessionalLoadList)
                ((SelectItemViewHolder) holder).tvName.setText(((ProfessionalLoadList) current).getDisplayValue());


            else if (current instanceof WuCurrencyData) {
                if (((WuCurrencyData) current).getName() == null) {
                    ((SelectItemViewHolder) holder).tvName.setText(((WuCurrencyData) current).getDisplayValue());
                } else {
                    ((SelectItemViewHolder) holder).tvName.setText(((WuCurrencyData) current).getName());
                }

            }

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

    protected List<T> getFilteredResults(String constraint) {
        List<T> results = new ArrayList<>();
        for (T item : originalList) {
            if (item instanceof BankData) {
                if (((BankData) item).getBankName() != null && ((BankData) item).getBankName().toLowerCase().contains(constraint)) {
                    results.add(item);
                }
            } else if (item instanceof AdditionalInfoData.ValuesData) {
                if (((AdditionalInfoData.ValuesData) item).getTitle() != null && ((AdditionalInfoData.ValuesData) item).getTitle().toLowerCase().contains(constraint))
                    results.add(item);
            } else if (item instanceof BranchData) {
                if (((BranchData) item).getBranchName() != null && ((BranchData) item).getBranchName().toLowerCase().contains(constraint))
                    results.add(item);
                else if (((BranchData) item).getBranchAddress() != null && ((BranchData) item).getBranchAddress().toLowerCase().contains(constraint))
                    results.add(item);
            } else if (item instanceof UserAccountData) {
                if (((UserAccountData) item).getAccountName() != null && ((UserAccountData) item).getAccountName().toLowerCase().contains(constraint))
                    results.add(item);
                else if (((UserAccountData) item).getBankName() != null && ((UserAccountData) item).getBankName().toLowerCase().contains(constraint))
                    results.add(item);
                else if (((UserAccountData) item).getBankBranchName() != null && ((UserAccountData) item).getBankBranchName().toLowerCase().contains(constraint))
                    results.add(item);
            } else if (item instanceof UserAccountBankData) {
                if (((UserAccountBankData) item).getBankName() != null && ((UserAccountBankData) item).getBankName().toLowerCase().contains(constraint))
                    results.add(item);
            } else if (item instanceof CreditCardBankData) {
                if (((CreditCardBankData) item).getBankName() != null && ((CreditCardBankData) item).getBankName().toLowerCase().contains(constraint))
                    results.add(item);
            } else if (item instanceof TransactionModeData) {
                if (((TransactionModeData) item).getName() != null && ((TransactionModeData) item).getName().toLowerCase().contains(constraint))
                    results.add(item);
            } else if (item instanceof ServiceTypeData) {
                if (((ServiceTypeData) item).getName() != null && ((ServiceTypeData) item).getName().toLowerCase().contains(constraint))
                    results.add(item);
            } else if (item instanceof AccountTypeData) {
                if (((AccountTypeData) item).getName() != null && ((AccountTypeData) item).getName().toLowerCase().contains(constraint))
                    results.add(item);
            } else if (item instanceof WuCurrencyData) {
                if (((WuCurrencyData) item).getDisplayValue() != null && ((WuCurrencyData) item).getDisplayValue().toLowerCase().contains(constraint))
                    results.add(item);
            } else if (item instanceof String) {
                if (item != null && ((String) item).toLowerCase().contains(constraint))
                    results.add(item);
            } else if (item instanceof CurrencyData) {
                if (item != null && (((CurrencyData) item).getNAME()).toLowerCase().contains(constraint))
                    results.add(item);
            }
        }
        return results;
    }

    class SelectItemViewHolder<T> extends RecyclerView.ViewHolder {
        TextView tvName, tvDescription, tvDescriptionTwo;

        public SelectItemViewHolder(View itemView) {
            super(itemView);
            {
                tvName = (TextView) itemView.findViewById(app.alansari.R.id.name);
                tvDescription = (TextView) itemView.findViewById(app.alansari.R.id.description);
                tvDescriptionTwo = (TextView) itemView.findViewById(app.alansari.R.id.description_two);
                tvDescriptionTwo.setVisibility(View.GONE);
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
