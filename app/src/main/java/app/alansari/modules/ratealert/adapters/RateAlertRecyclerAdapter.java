package app.alansari.modules.ratealert.adapters;

import android.app.Dialog;
import android.content.Context;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.alansari.Utils.CommonUtils;
import app.alansari.listeners.CustomClickListener;
import app.alansari.models.RateAlertModel;

/**
 * Created by Parveen Dala on 21 February, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class RateAlertRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public ArrayList<RateAlertModel> itemList;
    private Context context;
    private LayoutInflater inflater;
    private CustomClickListener clickListener;

    public RateAlertRecyclerAdapter(Context context, ArrayList<RateAlertModel> itemList, CustomClickListener clickListener) {
        this.context = context;
        this.itemList = itemList;
        this.clickListener = clickListener;
        inflater = LayoutInflater.from(context);
    }

    public void delete(int position) {
        itemList.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(RateAlertModel data) {
        this.itemList.add(data);
        notifyItemInserted(itemList.size() - 1);
    }

    public void addItemAt(RateAlertModel data, int position) {
        this.itemList.add(position, data);
        notifyItemInserted(position);
    }

    public void replaceItemAt(RateAlertModel data, int position) {
        itemList.set(position, data);
        notifyItemChanged(position);
    }

    public void addItemAtCustomLLayout(RateAlertModel data, int position) {
        this.itemList.add(position, data);
        notifyDataSetChanged();
    }

    public ArrayList<RateAlertModel> getArrayList() {
        return itemList;
    }

    public RateAlertModel getItemAt(int position) {
        return itemList.get(position);
    }

    public void addArrayList(ArrayList<RateAlertModel> itemList) {
        this.itemList.clear();
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
        return new QuickSendViewHolder(inflater.inflate(app.alansari.R.layout.item_rate_alert, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RateAlertModel current = itemList.get(position);
        ((QuickSendViewHolder) holder).tvAmount.setText(current.getFromCcyName() + " 1 = " + current.getDescCcyName() + " " + CommonUtils.addCommaToString(current.getAmount()));
    }

    class QuickSendViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView btnDelete;
        TextView tvAmount;

        public QuickSendViewHolder(View itemView) {
            super(itemView);
            btnDelete = (AppCompatImageView) itemView.findViewById(app.alansari.R.id.delete_btn);
            tvAmount = (TextView) itemView.findViewById(app.alansari.R.id.amount);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((itemList != null) && (getAdapterPosition() != itemList.size())) {
                        final Dialog myDialog = new Dialog(context, app.alansari.R.style.CustomDialogThemeLightBg);
                        myDialog.setCanceledOnTouchOutside(true);
                        myDialog.setContentView(app.alansari.R.layout.confirm_delete_item_generic_dialog);
                        ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_title)).setText("Delete rate alert?");
                        ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_text)).setText("Are you sure to delete 'AED 1 = " + itemList.get(getAdapterPosition()).getDescCcyName() + " " + itemList.get(getAdapterPosition()).getAmount() + "' Rate Alert?");
                        myDialog.findViewById(app.alansari.R.id.dialog_btn_yes).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                clickListener.itemClicked(null, getAdapterPosition(), itemList.get(getAdapterPosition()));
                                myDialog.dismiss();
                            }
                        });

                        myDialog.findViewById(app.alansari.R.id.dialog_btn_no).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myDialog.dismiss();
                            }
                        });
                        myDialog.show();
                    }
                }
            });
        }
    }
}