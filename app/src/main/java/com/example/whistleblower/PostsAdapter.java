package com.example.whistleblower;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import org.parceler.Parcels;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private List<Posts> posts;

    public PostsAdapter(Context context, List <Posts> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Posts posts = this.posts.get(position);
        holder.bind(posts);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void clear () {
        posts.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Posts> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvUsername;
        private ImageView ivImage;
        private TextView tvDescription;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            itemView.setOnClickListener(this);

        }

        public void bind(Posts posts) {
            // Bind the posts data to the view elements
            tvDescription.setText(posts.getDescription());
            tvUsername.setText(posts.getUser().getUsername());
            ParseFile image = posts.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivImage);
            }
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Posts posts = PostsAdapter.this.posts.get(position);
                Intent intent = new Intent(context, PostDetailsActivity.class);
                intent.putExtra("posts", Parcels.wrap(posts));
                context.startActivity(intent);
            }
        }
    }
}
