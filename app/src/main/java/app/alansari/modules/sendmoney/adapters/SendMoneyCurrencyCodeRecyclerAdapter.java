package app.alansari.modules.sendmoney.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.alansari.listeners.CustomClickListener;
import app.alansari.models.CountryData;

/**
 * Created by Parveen Dala on 19 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class SendMoneyCurrencyCodeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public int selectedItem;
    private LayoutInflater inflater;
    private ArrayList<CountryData.CurrencyData> itemList;
    private CustomClickListener customClickListener;

    public SendMoneyCurrencyCodeRecyclerAdapter(Context context, ArrayList<CountryData.CurrencyData> itemList, CustomClickListener customClickListener) {
        this.itemList = itemList;
        inflater = LayoutInflater.from(context);
        this.customClickListener = customClickListener;
    }

    public void delete(int position) {
        itemList.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(CountryData.CurrencyData data) {
        this.itemList.add(data);
        notifyItemInserted(itemList.size() - 1);
    }

    public void addItemAt(CountryData.CurrencyData data, int position) {
        this.itemList.add(position, data);
        notifyItemInserted(position);
    }

    public void addItemAtCustomLLayout(CountryData.CurrencyData data, int position) {
        this.itemList.add(position, data);
        notifyDataSetChanged();
    }

    public CountryData.CurrencyData getItemAt(int position) {
        return itemList.get(position);
    }

    public ArrayList<CountryData.CurrencyData> getArrayList() {
        return itemList;
    }

    public void addArrayList(ArrayList<CountryData.CurrencyData> itemList) {
       // this.itemList.clear();
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
        return new CurrencyCodeViewHolder(inflater.inflate(app.alansari.R.layout.item_send_money_currency_code_text_view, parent, false));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((CurrencyCodeViewHolder) holder).tvCode.setText(itemList.get(position).getName());
    }

    class CurrencyCodeViewHolder extends RecyclerView.ViewHolder {
        TextView tvCode;

        public CurrencyCodeViewHolder(View itemView) {
            super(itemView);
            {
                tvCode = (TextView) itemView.findViewById(app.alansari.R.id.text_view);

                itemView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        selectedItem = getAdapterPosition();
//                        customClickListener.itemClicked(v, getAdapterPosition(), itemList.get(getAdapterPosition()));
                    }
                });
            }
        }
    }
}
