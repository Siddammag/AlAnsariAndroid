package app.alansari.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.alansari.R;
import app.alansari.listeners.OnCustomClickListenerBoth;
import app.alansari.models.profiledetails.ProfileDetails;


public class MyProfileAdapter extends RecyclerView.Adapter<MyProfileAdapter.MyProfileHolder> {
    Context context;
    private List<ProfileDetails.TEMPLATELISTItem> itemList, originalList;
    OnCustomClickListenerBoth listener;


    public MyProfileAdapter(Context context, List<ProfileDetails.TEMPLATELISTItem> itemList, OnCustomClickListenerBoth customClickListener) {
        this.itemList = itemList;
        this.context = context;
        this.originalList = new ArrayList<>();
        this.originalList.addAll(itemList);
        this.listener = customClickListener;

    }

    @Override
    public MyProfileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyProfileHolder(LayoutInflater.from(parent.getContext()).inflate(app.alansari.R.layout.my_profile_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder( MyProfileHolder holder, int position) {
        if(holder instanceof MyProfileHolder){
            ProfileDetails.TEMPLATELISTItem current=itemList.get(position);
           // Log.e("aflcnajvchv",""+itemList.get(position).getRESULT().get(position).getCATEGORYDESC());
            ((MyProfileHolder) holder).name.setText(current.getCATEGORYDESC());
            if(current.isISDATAVALID())
                holder.ivChecked.setImageResource(R.drawable.svg_success);
            else
                holder.ivChecked.setImageResource(R.drawable.ic_attention);


        }

        //ivChecked.setImageResource(R.drawable.svg_success);

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void addArrayList(List<ProfileDetails.TEMPLATELISTItem> itemList) {
        this.itemList.clear();
        this.originalList.clear();
        this.itemList = itemList;
        this.originalList.addAll(itemList);
        notifyDataSetChanged();

    }

    public class MyProfileHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public ImageView ivChecked;

        public MyProfileHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(app.alansari.R.id.item_name);
            ivChecked = (ImageView) view.findViewById(app.alansari.R.id.accept_image);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemList.get(getAdapterPosition()).getCATEGORYID().equalsIgnoreCase("500"))
                        listener.itemClickedListerners(v,5,itemList.get(1));
                        else
                    listener.itemClickedListerners(v, getAdapterPosition(), itemList.get(getAdapterPosition()));

                }
            });
        }

    }
}



