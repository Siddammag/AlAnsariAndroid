package app.alansari.modules.promotions.adapter;

import android.app.Dialog;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.alansari.R;
import app.alansari.Utils.CommonUtils;
import app.alansari.customviews.MultiStateView;
import app.alansari.models.AddsData;

/**
 * Created by FuGenX-test on 29-03-2018.
 */

public class PromotionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private LayoutInflater inflater;
    private MultiStateView multiStateView;
    public ArrayList<AddsData> addsList;

    public PromotionAdapter(Context context, MultiStateView multiStateView, ArrayList<AddsData> addsData) {
        this.multiStateView = multiStateView;
        this.mContext = context;
        this.addsList = addsData;
        inflater = LayoutInflater.from(context);
    }

    public void addArrayList(ArrayList<AddsData> itemList) {
        this.addsList.clear();
        this.addsList.addAll(itemList);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PromotionsViewHolder(inflater.inflate(R.layout.item_promotions, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (addsList.get(position).getImageURL() != null && !TextUtils.isEmpty(addsList.get(position).getImageURL()) && addsList.get(position).getImageURL().length() > 0) {
           /* final byte[] decodedBytes = Base64.decode(addsList.get(position).getImageBase64(), Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            ((PromotionsViewHolder) holder).ivPic.setImageBitmap(decodedBitmap);*/

            CommonUtils.setPromotionsImage(mContext, ((PromotionsViewHolder) holder).ivPic, addsList.get(position).getImageURL());

            ((PromotionsViewHolder) holder).ivPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog pendingTransactionDialog = new Dialog(mContext, app.alansari.R.style.CustomDialogThemeLightBg);
                    pendingTransactionDialog.setCanceledOnTouchOutside(false);
                    pendingTransactionDialog.setContentView(R.layout.adds_dialog);
                    ((TextView) pendingTransactionDialog.findViewById(app.alansari.R.id.dialog_title)).setText(addsList.get(position).getMessage());
                    pendingTransactionDialog.findViewById(app.alansari.R.id.close_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pendingTransactionDialog.dismiss();
                        }
                    });
                    pendingTransactionDialog.show();
                }
            });
        } else {
            ((PromotionsViewHolder) holder).ivPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog pendingTransactionDialog = new Dialog(mContext, app.alansari.R.style.CustomDialogThemeLightBg);
                    pendingTransactionDialog.setCanceledOnTouchOutside(false);
                    pendingTransactionDialog.setContentView(R.layout.adds_dialog);
                    ((TextView) pendingTransactionDialog.findViewById(app.alansari.R.id.dialog_title)).setText(mContext.getString(app.alansari.R.string.new_offers_coming));
                    pendingTransactionDialog.findViewById(app.alansari.R.id.close_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pendingTransactionDialog.dismiss();
                        }
                    });
                    pendingTransactionDialog.show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return addsList.size();
    }


    class PromotionsViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPic;
        TextView tvMore;

        public PromotionsViewHolder(View itemView) {
            super(itemView);
            ivPic = (ImageView) itemView.findViewById(app.alansari.R.id.iv_pic);
            tvMore = (TextView) itemView.findViewById(app.alansari.R.id.read_more);
        }
    }
}
