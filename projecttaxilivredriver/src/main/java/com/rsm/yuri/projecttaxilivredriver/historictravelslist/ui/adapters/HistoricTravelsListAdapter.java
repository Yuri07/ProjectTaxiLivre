package com.rsm.yuri.projecttaxilivredriver.historictravelslist.ui.adapters;

import android.graphics.Color;
//import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rsm.yuri.projecttaxilivredriver.R;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.User;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.ui.OnItemClickListener;
import com.rsm.yuri.projecttaxilivredriver.historictravelslist.entities.HistoricTravelItem;
import com.rsm.yuri.projecttaxilivredriver.historictravelslist.ui.OnHistoricTravelItemClickListener;
import com.rsm.yuri.projecttaxilivredriver.lib.base.ImageLoader;
import com.rsm.yuri.projecttaxilivredriver.main.entities.Travel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class HistoricTravelsListAdapter extends RecyclerView.Adapter<HistoricTravelsListAdapter.ViewHolder> {

    private List<HistoricTravelItem> historicTravelsList;
    private ImageLoader imageLoader;
    private OnHistoricTravelItemClickListener clickListener;

    public HistoricTravelsListAdapter(List<HistoricTravelItem> historicTravelsList, ImageLoader imageLoader, OnHistoricTravelItemClickListener clickListener) {
        this.historicTravelsList = historicTravelsList;
        this.imageLoader = imageLoader;
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_historic_travel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HistoricTravelItem travelItem = historicTravelsList.get(position);
        holder.setClickListener(travelItem, clickListener);//chama o listener da HistoricTravelListActivity

        //String email = travel.getRequesterEmail();
        //String travelId = travel.getTravelId();
        String date = travelItem.getTravelDate();
        String modelo = travelItem.getModelo();
        double price = travelItem.getTravelPrice();
        holder.tvDate.setText("01/01/0001 às 01:01");
        SimpleDateFormat spf = new SimpleDateFormat("DDD MMM dd hh:mm:ss aaa-HH:ZZ yyyy");
        try {
            Date newDate = spf.parse(date);
            spf = new SimpleDateFormat("dd/MMM/yyyy");
            date = spf.format(newDate);
            SimpleDateFormat spf2 = new SimpleDateFormat("hh:mm");
            date = date +" às " + spf2.format(newDate);
            Log.d("d", "HistoricTravelListAdapter - onBindViewHolder - date: " + date);
            holder.tvDate.setText(date);
            //"Mon Dec 03 17:20:13 GMT-03:00 2018"
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.tvCarModel.setText(modelo);
        holder.tvPriceTravel.setText("R$" + String.format("%.2f", price));

        String urlPhotoMap = travelItem.getUrlPhotoMap();
        if(urlPhotoMap!=null) {
            if (!urlPhotoMap.equals("Default"))
                imageLoader.load(holder.imgMap, urlPhotoMap);
        }
    }

    @Override
    public int getItemCount() {
        return historicTravelsList.size();
    }

    public void add(HistoricTravelItem travelItem) {
        if (!alreadyInAdapter(travelItem)) {
            this.historicTravelsList.add(travelItem);
            this.notifyDataSetChanged();
        }
    }

    private boolean alreadyInAdapter(HistoricTravelItem newTravelItem){
        boolean alreadyInAdapter = false;
        for (HistoricTravelItem travelItem : this.historicTravelsList) {
            if (travelItem.getTravelId().equals(newTravelItem.getTravelId())) {
                alreadyInAdapter = true;
                break;
            }
        }

        return alreadyInAdapter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.cht_iv_map_image)
        ImageView imgMap;
        @BindView(R.id.cht_tv_date_travel)
        TextView tvDate;
        @BindView(R.id.cht_tv_car_travel)
        TextView tvCarModel;
        @BindView(R.id.cht_tv_value_travel)
        TextView tvPriceTravel;

        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            ButterKnife.bind(this, itemView);
        }

        public void setClickListener(final HistoricTravelItem travelItem,
                                     final OnHistoricTravelItemClickListener listener) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(travelItem);
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onItemLongClick(travelItem);
                    return false;
                }
            });
        }

    }

}
