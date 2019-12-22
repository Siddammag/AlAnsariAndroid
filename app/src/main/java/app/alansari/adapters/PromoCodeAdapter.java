package app.alansari.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.alansari.R;
import app.alansari.models.PromoCodeData;
import app.alansari.modules.sendmoney.AdditionalInfoActivity;
import app.alansari.modules.sendmoney.AdditionalInfoCeActivity;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by FuGenX-test on 23-03-2018.
 */

public class PromoCodeAdapter extends RecyclerView.Adapter<PromoCodeAdapter.PromoViewHolder> {
    private ArrayList<PromoCodeData> promoCodeList = new ArrayList<>();
    private int selected = 0;
    private Context mContext;

    public PromoCodeAdapter(Context context) {
        mContext = context;
    }

    @Override
    public PromoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.promocode_item, parent, false);

        return new PromoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PromoViewHolder holder, int position) {
        holder.title.setText(promoCodeList.get(position).getPROMOVALUE());
        if (selected == position) {
            updatePromoMessage(position);
            holder.imageView.setImageResource(R.drawable.icn_radion_on);
        } else {
            holder.imageView.setImageResource(R.drawable.icn_radio_off);
        }

    }

    @Override
    public int getItemCount() {
        return promoCodeList.size();
    }

    public void addArrayList(ArrayList<PromoCodeData> promoCodeList) {
        this.promoCodeList.clear();
        this.promoCodeList = promoCodeList;
        notifyDataSetChanged();
    }

    public String getCode() {
        return promoCodeList.get(selected).getPROMOVALUE();
    }

    public String getPromoCode() {
        return promoCodeList.get(selected).getPROMOCODE();
    }

    public class PromoViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public CircleImageView imageView;

        public PromoViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.name);
            imageView = (CircleImageView) view.findViewById(R.id.radio);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }

    private void updatePromoMessage(int adapterPosition) {
        if (mContext instanceof AdditionalInfoActivity && (AdditionalInfoActivity) mContext != null) {
            ((AdditionalInfoActivity) mContext).updateMessage(promoCodeList.get(adapterPosition).getPromoMessage());
        } else if (mContext instanceof AdditionalInfoCeActivity && (AdditionalInfoCeActivity) mContext != null) {
            ((AdditionalInfoCeActivity) mContext).updateMessage(promoCodeList.get(adapterPosition).getPromoMessage());
        }
    }
}
