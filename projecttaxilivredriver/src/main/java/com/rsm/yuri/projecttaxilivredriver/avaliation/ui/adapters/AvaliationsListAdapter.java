package com.rsm.yuri.projecttaxilivredriver.avaliation.ui.adapters;

//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rsm.yuri.projecttaxilivredriver.R;
import com.rsm.yuri.projecttaxilivredriver.avaliation.entities.Rating;
import com.rsm.yuri.projecttaxilivredriver.lib.base.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yuri_ on 07/03/2018.
 */

public class AvaliationsListAdapter extends RecyclerView.Adapter<AvaliationsListAdapter.ViewHolder> {

    private List<Rating> avaliationsList;
    private ImageLoader imageLoader;

    public AvaliationsListAdapter(List<Rating> avaliationsList, ImageLoader imageLoader) {
        this.avaliationsList = avaliationsList;
        this.imageLoader = imageLoader;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_avaliation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Rating rating = avaliationsList.get(position);

        holder.txtComment.setText(rating.getComment());
        holder.txtNomeUser.setText(rating.getNome());
        holder.txtData.setText(rating.getDate());
        String urlPhotoUser = rating.getUrlPhotoUser();
        if ((!urlPhotoUser.equals("default"))&&!(urlPhotoUser.equals("url_sh_pr"))&&!(urlPhotoUser.isEmpty()))
            imageLoader.load(holder.imgAvatar, urlPhotoUser);
        switch ((int) rating.getVote()) {
            case 1:
                break;
            case 2:
                holder.star2.setImageResource(R.drawable.icons8_estrela_preenchida_16);
                break;
            case 3:
                holder.star2.setImageResource(R.drawable.icons8_estrela_preenchida_16);
                holder.star3.setImageResource(R.drawable.icons8_estrela_preenchida_16);
                break;
            case 4:
                holder.star2.setImageResource(R.drawable.icons8_estrela_preenchida_16);
                holder.star3.setImageResource(R.drawable.icons8_estrela_preenchida_16);
                holder.star4.setImageResource(R.drawable.icons8_estrela_preenchida_16);
                break;
            case 5:
                holder.star2.setImageResource(R.drawable.icons8_estrela_preenchida_16);
                holder.star3.setImageResource(R.drawable.icons8_estrela_preenchida_16);
                holder.star4.setImageResource(R.drawable.icons8_estrela_preenchida_16);
                holder.star5.setImageResource(R.drawable.icons8_estrela_preenchida_16);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return avaliationsList.size();
    }

    public void add(Rating rating) {
        if (!alreadyInAdapter(rating)) {
            this.avaliationsList.add(rating);
            this.notifyDataSetChanged();
        }
    }

    private boolean alreadyInAdapter(Rating newRating) {
        boolean alreadyInAdapter = false;
        for (Rating rating : this.avaliationsList) {
            if (rating.getEmail().equals(newRating.getEmail())) {
                alreadyInAdapter = true;
                break;
            }
        }

        return alreadyInAdapter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imgAvatar)
        CircleImageView imgAvatar;
        @BindView(R.id.txtNomeUser)
        TextView txtNomeUser;
        @BindView(R.id.star1)
        ImageView star1;
        @BindView(R.id.star2)
        ImageView star2;
        @BindView(R.id.star3)
        ImageView star3;
        @BindView(R.id.star4)
        ImageView star4;
        @BindView(R.id.star5)
        ImageView star5;
        @BindView(R.id.txtData)
        TextView txtData;
        @BindView(R.id.txtComment)
        TextView txtComment;

        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

}
