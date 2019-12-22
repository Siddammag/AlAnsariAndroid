package app.alansari.adapters;

import app.alansari.models.BeneficiaryData;
import app.alansari.modules.accountmanagement.adapters.CreditCardRecyclerAdapter_old;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Parveen Dala on 14 October, 2016
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public abstract class GenericRecycleAdapter<E> extends RecyclerView.Adapter implements Filterable {
    protected List<E> list;
    protected List<E> originalList;
    protected Context context;

    public GenericRecycleAdapter(Context context) {
        this.context = context;
    }

    public GenericRecycleAdapter(Context context, List<E> list) {
        this.originalList = list;
        this.list = list;
        this.context = context;
    }

    public void addList(List<E> list) {
        this.originalList = list;
        this.list = list;
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (List<E>) results.values;
                if (GenericRecycleAdapter.this instanceof CreditCardRecyclerAdapter_old) {
                    ((CreditCardRecyclerAdapter_old) GenericRecycleAdapter.this).beneficiaryList.clear();
                    ((CreditCardRecyclerAdapter_old) GenericRecycleAdapter.this).beneficiaryList.addAll((ArrayList<BeneficiaryData>) list);
                }

                GenericRecycleAdapter.this.notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<E> filteredResults = null;
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

    protected List<E> getFilteredResults(String constraint) {
        List<E> results = new ArrayList<>();
        for (E item : originalList) {
            if (GenericRecycleAdapter.this instanceof CreditCardRecyclerAdapter_old) {
                if (((BeneficiaryData) item).getName().toLowerCase().contains(constraint)) {
                    results.add(item);
                } else if (((BeneficiaryData) item).getBankData().getBankName().toLowerCase().contains(constraint)) {
                    results.add(item);
                } else if (((BeneficiaryData) item).getAccountType().toLowerCase().contains(constraint)) {
                    results.add(item);
                }
            }
        }
        return results;
    }
}