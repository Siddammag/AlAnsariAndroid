package app.alansari.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.alansari.R;
import app.alansari.Utils.Constants;
import app.alansari.models.travelcardreloadcurrency.TravelCardCurrencyModel;

public class TravelCardCurrencyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<TravelCardCurrencyModel> itemList,originalList;

    public TravelCardCurrencyAdapter(Context context, ArrayList<TravelCardCurrencyModel> travelCardCurrencyModels) {
        this.context=context;
        this.itemList=travelCardCurrencyModels;
        this.originalList = new ArrayList<>();
        this.originalList.addAll(originalList);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TravelCardItemViewHolder(LayoutInflater.from(context).inflate(app.alansari.R.layout.travel_card_currency_adapter, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TravelCardItemViewHolder curent= (TravelCardItemViewHolder) holder;
        curent.name.setText(itemList.get(position).getISOCCYCODE());
        curent.rate.setText(itemList.get(position).getBALANCE());
        Picasso.get()
                .load(Constants.COUNTRY_FLAG_IMAGE_BASE_URL + itemList.get(position).getFLAG())
                .placeholder(app.alansari.R.drawable.flag_placeholder)
                .error(app.alansari.R.drawable.flag_placeholder)
                .into(curent.imageViewFlag);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void addArrayList(ArrayList<TravelCardCurrencyModel> itemList) {
        this.itemList.clear();
        this.originalList.clear();
        this.itemList = itemList;
        this.originalList.addAll(itemList);
        notifyDataSetChanged();
    }

    private class TravelCardItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewFlag;
        TextView name,rate;
        public TravelCardItemViewHolder(View inflate) {
            super(inflate);
            imageViewFlag=(ImageView)inflate.findViewById(R.id.flag_image);
            name=(TextView)inflate.findViewById(R.id.title);
            rate=(TextView)inflate.findViewById(R.id.rate);

        }
    }
}
