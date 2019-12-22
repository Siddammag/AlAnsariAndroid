package app.alansari.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.alansari.listeners.CustomClickListener;
import app.alansari.models.LeftMenu;

/**
 * Created by Teja on 13-10-2016.
 */

public class LeftMenuAdapter extends RecyclerView.Adapter<LeftMenuAdapter.MyViewHolder> {
    Context context;
    ArrayList<LeftMenu> leftMenuArrayList;
    CustomClickListener listener;

    public LeftMenuAdapter(Context context, ArrayList<LeftMenu> leftMenuArrayList, CustomClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.leftMenuArrayList = leftMenuArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(app.alansari.R.layout.item_left_menu, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (leftMenuArrayList.get(position).getPicId() != 0) {
            holder.name.setText(leftMenuArrayList.get(position).getName());
            holder.icon.setImageResource(leftMenuArrayList.get(position).getPicId());
        } else {
            holder.appVersion.setText(leftMenuArrayList.get(position).getName());
            holder.icon.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return leftMenuArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, appVersion;
        public ImageView icon;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(app.alansari.R.id.left_menu_item_name);
            appVersion = (TextView) view.findViewById(app.alansari.R.id.left_menu_item_app_version);
            icon = (ImageView) view.findViewById(app.alansari.R.id.iv_icon);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.itemClicked(v, getAdapterPosition(), leftMenuArrayList.get(getAdapterPosition()));
                }
            });
        }
    }
}
