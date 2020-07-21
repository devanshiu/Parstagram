package com.example.whistleblower;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.whistleblower.models.NewsArticle;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.viewHolder> {

    public List<NewsArticle> articles = new ArrayList<>();
    private Context context;
    private AdapterView.OnClickListener onClickListener;

//    public TextView tvTitle;
//    public TextView tvDescription;
//    public TextView tvAuthor;
//    public TextView tvTimestamp;
//    public TextView tvNewsSource;
//    public TextView tvTime;
//    public ImageView ivNewsStory;

    public NewsAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news, parent, false);
        return new viewHolder(view, (NewsAdapter.onClickListener) onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holders, int position) {
        final viewHolder holder = holders;
        NewsArticle model = articles.get(position);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(Utilities.getRandomDrawableColor());
        requestOptions.error(Utilities.getRandomDrawableColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();

        Glide.with(context)
            .load(model.getUrlToImage())
            .apply(requestOptions).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.pbLoadPhoto.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.pbLoadPhoto.setVisibility(View.GONE);
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.ivNewsStory);

            holder.tvTitle.setText(model.getTitle());
            holder.tvDescription.setText(model.getDescription());
            holder.tvNewsSource.setText(model.getSource().getName());
//            holder.tvTime.setText(" \u2022 " + Utilities.DateToTimeFormat(model.getPublishedAt()));
            holder.tvTimestamp.setText(Utilities.DateFormat(model.getPublishedAt()));
            holder.tvAuthor.setText(model.getAuthor());
    }


    @Override
    public int getItemCount() {
        return articles.size();
    }

    public void setOnClickListener(onClickListener onClickListener) {
        this.onClickListener = (AdapterView.OnClickListener) onClickListener;
    }

    public void clear() {
        articles.clear();
        notifyDataSetChanged();
    }

    public interface onClickListener {
        void onItemClick(View view, int position);
    }

    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final onClickListener onClickListener;
        public ProgressBar pbLoadPhoto;
        public TextView tvTitle;
        public TextView tvDescription;
        public TextView tvAuthor;
        public TextView tvTimestamp;
        public TextView tvNewsSource;
        public TextView tvTime;
        public ImageView ivNewsStory;

        public viewHolder(@NonNull View itemView, NewsAdapter.onClickListener onClickListener) {
            super(itemView);
            this.onClickListener = onClickListener;

            itemView.setOnClickListener(this);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            tvNewsSource = itemView.findViewById(R.id.tvNewsSource);
            ivNewsStory = itemView.findViewById(R.id.ivNewsStory);
            pbLoadPhoto = itemView.findViewById(R.id.pbLoadPhoto);
        }

        @Override
        public void onClick(View view) {
            onClickListener.onItemClick(view, getAdapterPosition());

        }
    }
}
