package app.alansari.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.Validation;
import app.alansari.listeners.CustomClickListener;
import app.alansari.models.QuickSendModel;
import app.alansari.modules.accountmanagement.BeneficiaryActivity;
import app.alansari.preferences.SharedPreferenceManger;
import app.alansari.textdrawable.ColorGenerator;
import app.alansari.textdrawable.TextDrawable;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Parveen Dala on 07 November, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class QuickSendRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public ArrayList<QuickSendModel> quickSendList;
    private Context context;
    private LayoutInflater inflater;
    private CustomClickListener clickListener;

    public QuickSendRecyclerAdapter(Context context, CustomClickListener clickListener) {
        this.context = context;
        this.quickSendList = SharedPreferenceManger.loadQuickSendData();
        if (this.quickSendList == null) {
            this.quickSendList = new ArrayList<>();
        }
        this.clickListener = clickListener;
        inflater = LayoutInflater.from(context);
    }

    public void delete(int position) {
        quickSendList.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(QuickSendModel data) {
        this.quickSendList.add(data);
        notifyItemInserted(quickSendList.size() - 1);
    }

    public void addItemAt(QuickSendModel data, int position) {
        this.quickSendList.add(position, data);
        notifyItemInserted(position);
    }

    public void replaceItemAt(QuickSendModel data, int position) {
        quickSendList.set(position, data);
        notifyItemChanged(position);
    }

    public void addItemAtCustomLLayout(QuickSendModel data, int position) {
        this.quickSendList.add(position, data);
        notifyDataSetChanged();
    }

    public ArrayList<QuickSendModel> getArrayList() {
        return quickSendList;
    }

    public QuickSendModel getItemAt(int position) {
        return quickSendList.get(position);
    }

    public void addArrayList(ArrayList<QuickSendModel> quickSendList) {
        this.quickSendList.clear();
        this.quickSendList = quickSendList;
        notifyDataSetChanged();
    }

    public void clear() {
        this.quickSendList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return quickSendList.size() + 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new QuickSendViewHolder(inflater.inflate(app.alansari.R.layout.item_quick_send, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == quickSendList.size()) {
            ((QuickSendViewHolder) holder).tvName.setText("Add");
            ((QuickSendViewHolder) holder).ivProfilePic.setImageResource(app.alansari.R.drawable.ic_action_add);
            ((QuickSendViewHolder) holder).ivProfilePic.setBackground(ContextCompat.getDrawable(context, app.alansari.R.drawable.quick_send_add_bg));
            CommonUtils.setLayoutFont(context, "Roboto-Medium.ttf", ((QuickSendViewHolder) holder).tvName);
        } else {
            QuickSendModel current = quickSendList.get(position);
           /* if (current.getName() != null && current.getName().trim().length() > 0) {
                ((QuickSendViewHolder) holder).tvName.setText(current.getName());
                ((QuickSendViewHolder) holder).ivProfilePic.setVisibility(View.GONE);
                ((QuickSendViewHolder) holder).profile_pic_text.setVisibility(View.VISIBLE);
                String name = "";
                try {
                    name = String.valueOf(current.getName().split(" ")[0].trim().toUpperCase().charAt(0) + "" + (current.getName().split(" ").length > 1 ? current.getName().split(" ")[1].toUpperCase().charAt(0) : ""));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    name = String.valueOf(current.getName().split(" ")[0].trim().toUpperCase().charAt(0));
                }
                ((QuickSendViewHolder) holder).profile_pic_text.setImageDrawable(TextDrawable
                        .builder()
                        .beginConfig()
                        .textColor(Color.WHITE)
                        .bold()
                        .useFont(Typeface.create("fonts/HelveticaNeue-Medium.ttf", Typeface.NORMAL))
                        .endConfig()
                        .buildRound(name, ColorGenerator.MATERIAL.getRandomColor()));
            } else {
                ((QuickSendViewHolder) holder).tvName.setText("");
            }*/
            ((QuickSendViewHolder) holder).tvName.setText(current.getName());
            if (current.getBenImage() != null && !TextUtils.isEmpty(current.getBenImage()) && current.getBenImage().length() > 0) {
                CommonUtils.setBeneficiaryImage(context, ((QuickSendViewHolder) holder).ivProfilePic, current.getBenImage());
            } else if (Validation.isValidString(current.getName())) {
                ((QuickSendViewHolder) holder).ivProfilePic.setVisibility(View.GONE);
                String name = "";
                try {
                    if (current.getName().split(" ").length >= 1) {
                        name = String.valueOf(current.getName().split(" ")[0].trim().toUpperCase().charAt(0) + "" + (current.getName().split(" ").length > 1 ? current.getName().split(" ")[1].toUpperCase().charAt(0) : ""));
                    } else {
                        name = String.valueOf(current.getName().trim().toUpperCase().charAt(0));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    name = String.valueOf(current.getName().split(" ")[0].trim().toUpperCase().charAt(0));
                }
                ((QuickSendViewHolder) holder).profile_pic_text.setImageDrawable(TextDrawable
                        .builder()
                        .beginConfig()
                        .textColor(Color.WHITE)
                        .bold()
                        .useFont(Typeface.create("fonts/HelveticaNeue-Medium.ttf", Typeface.NORMAL))
                        .endConfig()
                        .buildRound(name, ColorGenerator.MATERIAL.getRandomColor()));
                ((QuickSendViewHolder) holder).profile_pic_text.setVisibility(View.VISIBLE);
            } else {
                ((QuickSendViewHolder) holder).ivProfilePic.setVisibility(View.GONE);
                ((QuickSendViewHolder) holder).profile_pic_text.setImageDrawable(TextDrawable
                        .builder()
                        .beginConfig()
                        .textColor(Color.WHITE)
                        .bold()
                        .useFont(Typeface.create("fonts/HelveticaNeue-Medium.ttf", Typeface.NORMAL))
                        .endConfig()
                        .buildRound("", ColorGenerator.MATERIAL.getRandomColor()));
                ((QuickSendViewHolder) holder).profile_pic_text.setVisibility(View.VISIBLE);
            }

            CommonUtils.setLayoutFont(context, "Roboto-Regular.ttf", ((QuickSendViewHolder) holder).tvName);
        }
    }

    class QuickSendViewHolder extends RecyclerView.ViewHolder {
        CircleImageView ivProfilePic;
        ImageView profile_pic_text;
        TextView tvName;

        public QuickSendViewHolder(View itemView) {
            super(itemView);
            ivProfilePic = (CircleImageView) itemView.findViewById(app.alansari.R.id.profile_pic);
            profile_pic_text = (ImageView) itemView.findViewById(app.alansari.R.id.profile_pic_text);
            tvName = (TextView) itemView.findViewById(app.alansari.R.id.name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    if ((quickSendList != null) && (getAdapterPosition() == quickSendList.size())) {
                        if (quickSendList.size() > 5) {
                            Toast.makeText(context, "Max limit already reached.", Toast.LENGTH_SHORT).show();
                        } else {
                            intent = new Intent(context, BeneficiaryActivity.class);
                            intent.putExtra(Constants.INTENT_TYPE, Constants.ACTIVITY_FOR_RESULT);
                            ((AppCompatActivity) context).startActivityForResult(intent, Constants.SELECT_BENEFICIARY_FOR_QUICK_SEND);
                        }
                    } else {
                      /*  intent = new Intent(context, SendMoneyActivity.class);
                        intent.putExtra(Constants.ID, quickSendList.get(getAdapterPosition()).getBeneficiaryId());
                        intent.putExtra(Constants.SERVICE_TYPE, quickSendList.get(getAdapterPosition()).getServiceType());
                        context.startActivity(intent);*/

                        intent = new Intent(context, BeneficiaryActivity.class);
                        intent.putExtra(Constants.ID, quickSendList.get(getAdapterPosition()).getBeneficiaryId());
                        intent.putExtra(Constants.SERVICE_TYPE, quickSendList.get(getAdapterPosition()).getServiceType());
                        intent.putExtra(Constants.INTENT_TYPE, Constants.ACTIVITY_FOR_SELECTION);
                        ((AppCompatActivity) context).startActivity(intent);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if ((quickSendList != null) && (getAdapterPosition() != quickSendList.size())) {
                        final Dialog myDialog = new Dialog(context, app.alansari.R.style.CustomDialogThemeLightBg);
                        myDialog.setCanceledOnTouchOutside(true);
                        myDialog.setContentView(app.alansari.R.layout.confirm_delete_item_generic_dialog);
                        ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_title)).setText("Delete quick send?");
                        ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_text)).setText(context.getResources().getString(app.alansari.R.string.confirm_delete_quick_send));
                        myDialog.findViewById(app.alansari.R.id.dialog_btn_yes).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                clickListener.itemClicked(null, getAdapterPosition(), quickSendList.get(getAdapterPosition()));
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
                        return true;
                    }
                    return false;
                }
            });
        }
    }
}