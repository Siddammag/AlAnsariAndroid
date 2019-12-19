package app.alansari.adapters;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.demono.adapter.InfinitePagerAdapter;

import java.util.ArrayList;

import app.alansari.R;
import app.alansari.Utils.CommonUtils;
import app.alansari.models.AddsData;

/**
 * Created by FuGenX-test on 23-02-2018.
 */

public class AddsAdapter extends InfinitePagerAdapter {
    private Context mContext;
    public ArrayList<AddsData> addsList;
    private LayoutInflater inflater;

    public AddsAdapter(Context context, ArrayList<AddsData> addsList) {
        this.mContext = context;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.addsList = addsList;
    }


    @Override
    public int getItemCount() {
        return addsList == null ? 0 : addsList.size();
    }

    @Override
    public View getItemView(final int position, View convertView, ViewGroup container) {
        View itemView = inflater.inflate(R.layout.item_landing_add, container, false);

        ImageView ivPic = (ImageView) itemView.findViewById(app.alansari.R.id.iv_pic);
        TextView tvMore = (TextView) itemView.findViewById(app.alansari.R.id.read_more);

        /*if (addsList.get(position).getImageBase64() != null && !TextUtils.isEmpty(addsList.get(position).getImageBase64()) && addsList.get(position).getImageBase64().length() > 0) {
            final byte[] decodedBytes = Base64.decode(addsList.get(position).getImageBase64(), Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            ivPic.setImageBitmap(decodedBitmap);

            ivPic.setOnClickListener(new View.OnClickListener() {
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
        }*/

        if (addsList.get(position).getImageURL() != null && !TextUtils.isEmpty(addsList.get(position).getImageURL()) && addsList.get(position).getImageURL().length() > 0) {
            CommonUtils.setPromotionsImage(mContext, ivPic, addsList.get(position).getImageURL());
            ivPic.setOnClickListener(new View.OnClickListener() {
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
            ivPic.setOnClickListener(new View.OnClickListener() {
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
        return itemView;
    }
}
