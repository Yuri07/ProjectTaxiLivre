package com.rsm.yuri.projecttaxilivre.historicchatslist.ui.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rsm.yuri.projecttaxilivre.R;
import com.rsm.yuri.projecttaxilivre.historicchatslist.ui.OnItemClickListener;
import com.rsm.yuri.projecttaxilivre.lib.base.ImageLoader;
import com.rsm.yuri.projecttaxilivre.map.entities.Driver;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yuri_ on 13/01/2018.
 */

public class HistoricChatsListAdapter extends RecyclerView.Adapter<HistoricChatsListAdapter.ViewHolder> {

    private List<Driver> historicChatsList;
    private ImageLoader imageLoader;
    private OnItemClickListener clickListener;

    public HistoricChatsListAdapter(List<Driver> historicChatsList, ImageLoader imageLoader, OnItemClickListener clickListener) {
        this.historicChatsList = historicChatsList;
        this.imageLoader = imageLoader;
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_historicchat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Driver driver = historicChatsList.get(position);
        holder.setClickListener(driver, clickListener);//chama o listener da ContactListActivity

        String email = driver.getEmail();
        long online = driver.getStatus();
        Log.d("d", "adapter.driver.getStatus(): "+driver.getStatus());
        String status = (online == Driver.OFFLINE || online==Driver.IN_TRAVEL)? "offline" : "online";
        int color = (online == Driver.OFFLINE || online==Driver.IN_TRAVEL) ? Color.RED : Color.GREEN;
        /*String status = online == 1 ? "online" : "offline";
        int color = online == 1 ? Color.GREEN : Color.RED;*/

        holder.txtUser.setText(email);
        holder.txtStatus.setText(status);
        holder.txtStatus.setTextColor(color);

        String url = driver.getUrlPhotoDriver();
        if(url!=null) {
            if (!url.equals("Default"))
                imageLoader.load(holder.imgAvatar, url);
        }
    }

    @Override
    public int getItemCount() {
        return historicChatsList.size();
    }

    public int getPositionByUsername(String username) {
        int position = 0;
        for (Driver driver : historicChatsList) {
            if (driver.getEmail().equals(username)) {
                break;
            }
            position++;
        }

        return position;
    }

    private boolean alreadyInAdapter(Driver newDriver){
        boolean alreadyInAdapter = false;
        for (Driver driver : this.historicChatsList) {
            if (driver.getEmail().equals(newDriver.getEmail())) {
                alreadyInAdapter = true;
                break;
            }
        }

        return alreadyInAdapter;
    }

    public void add(Driver driver) {
        if (!alreadyInAdapter(driver)) {
            this.historicChatsList.add(driver);
            this.notifyDataSetChanged();
        }
    }

    public void update(Driver driver) {
        int pos = getPositionByUsername(driver.getEmail());
        historicChatsList.set(pos, driver);
        this.notifyDataSetChanged();
    }

    public void remove(Driver driver) {
        int pos = getPositionByUsername(driver.getEmail());
        historicChatsList.remove(pos);
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.imgAvatar)
        CircleImageView imgAvatar;
        @BindView(R.id.txtStatus)
        TextView txtStatus;
        @BindView(R.id.txtUser)
        TextView txtUser;

        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            ButterKnife.bind(this, itemView);
        }

        public void setClickListener(final Driver driver,
                                     final OnItemClickListener listener) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(driver);
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onItemLongClick(driver);
                    return false;
                }
            });
        }

    }
}
