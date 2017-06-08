package com.example.hope.movieapp_stage2.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.hope.movieapp_stage2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hope on 6/4/2017.
 */

public class MovieGridAdapter extends RecyclerView.Adapter<MovieGridAdapter.PosterViewHolder> {

    private List<String> posterURL = new ArrayList();
    private Context context;
    private GridItemClickListener mGridItemClickListener;

    public MovieGridAdapter(Context c, List posterURL, GridItemClickListener clickListener){
        this.posterURL = posterURL;
        this.context = c;
        this.mGridItemClickListener = clickListener;
    }

    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_grid_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        PosterViewHolder viewHolder = new PosterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PosterViewHolder viewHolder, int position) {
        Picasso.with(context).load(posterURL.get(position)).into(viewHolder.gridItemPoster);
    }

    @Override
    public int getItemCount() {
        return posterURL.size();
    }

    public class PosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView gridItemPoster;

        public PosterViewHolder(View itemView) {
            super(itemView);
            gridItemPoster = (ImageView) itemView.findViewById(R.id.imageview_movie_grid_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedItemPosition = getAdapterPosition();
            mGridItemClickListener.onGridItemClick(clickedItemPosition);
        }
    }

    public interface GridItemClickListener{void onGridItemClick(int itemIndex);}
}
