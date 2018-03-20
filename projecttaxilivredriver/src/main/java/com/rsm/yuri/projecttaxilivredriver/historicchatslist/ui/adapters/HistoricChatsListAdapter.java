package com.rsm.yuri.projecttaxilivredriver.historicchatslist.ui.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rsm.yuri.projecttaxilivredriver.R;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.User;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.ui.OnItemClickListener;
import com.rsm.yuri.projecttaxilivredriver.lib.base.ImageLoader;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Driver;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yuri_ on 13/01/2018.
 */

public class HistoricChatsListAdapter extends RecyclerView.Adapter<HistoricChatsListAdapter.ViewHolder> {

    private List<User> historicChatsList;
    private ImageLoader imageLoader;
    private OnItemClickListener clickListener;

    public HistoricChatsListAdapter(List<User> historicChatsList, ImageLoader imageLoader, OnItemClickListener clickListener) {
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
        User user = historicChatsList.get(position);
        holder.setClickListener(user, clickListener);//chama o listener da ContactListActivity

        String email = user.getEmail();
        long online = user.getStatus();
        //Log.d("d", "adapter.driver.getStatus(): "+user.getStatus());
        String status = online == 1 ? "online" : "offline";
        int color = online == 1 ? Color.GREEN : Color.RED;

        holder.txtUser.setText(email);
        holder.txtStatus.setText(status);
        holder.txtStatus.setTextColor(color);

        String url = user.getUrlPhotoUser();
        if(url!=null) {
            if (!url.equals("Default"))
                imageLoader.load(holder.imgAvatar, user.getUrlPhotoUser());
        }
    }

    @Override
    public int getItemCount() {
        return historicChatsList.size();
    }

    public int getPositionByUsername(String username) {
        int position = 0;
        for (User user : historicChatsList) {
            if (user.getEmail().equals(username)) {
                break;
            }
            position++;
        }

        return position;
    }

    private boolean alreadyInAdapter(User newUser){
        boolean alreadyInAdapter = false;
        for (User user : this.historicChatsList) {
            if (user.getEmail().equals(newUser.getEmail())) {
                alreadyInAdapter = true;
                break;
            }
        }

        return alreadyInAdapter;
    }

    public void add(User user) {
        if (!alreadyInAdapter(user)) {
            this.historicChatsList.add(user);
            this.notifyDataSetChanged();
        }
    }

    public void update(User user) {
        int pos = getPositionByUsername(user.getEmail());
        historicChatsList.set(pos, user);
        this.notifyDataSetChanged();
    }

    public void remove(User user) {
        int pos = getPositionByUsername(user.getEmail());
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

        public void setClickListener(final User user,
                                     final OnItemClickListener listener) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(user);
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onItemLongClick(user);
                    return false;
                }
            });
        }

    }
}
