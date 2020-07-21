package com.example.whistleblower.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whistleblower.EndlessRecyclerViewScrollListener;
import com.example.whistleblower.Posts;
import com.example.whistleblower.PostsAdapter;
import com.example.whistleblower.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class PostsFragment extends Fragment {
    public static final String TAG = "PostsFragment";
    private RecyclerView rvPosts;
    protected PostsAdapter adapter;
    protected List<Posts> allPosts;
    protected SwipeRefreshLayout swipeContainer;
    EndlessRecyclerViewScrollListener.EndlessRVScrollListener scrollListener;



    public PostsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts = view.findViewById(R.id.rvPosts);
        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // 1. Create the adapter
        allPosts = new ArrayList<>();
        adapter = new PostsAdapter(getContext(), allPosts);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        // Steps to use recycler view:
        // 0. Create layout for one row in the list
        // need to fix item_post layout
        // 2. Create the data source
        // 3. Set the adapter on the RV
        rvPosts.setAdapter(adapter);
        // 4. Set the layout manager on the RV
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        scrollListener = new EndlessRecyclerViewScrollListener.EndlessRVScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.i(TAG, "onLoadMore: " + page);
                queryPosts();
            }
        };

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                // Code to refresh the list
                queryPosts();
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
//                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        queryPosts();
    }

    protected void queryPosts() {
        ParseQuery<Posts> query = ParseQuery.getQuery(Posts.class);
        query.include(Posts.KEY_USER);
        query.include(Posts.KEY_IMAGE);
        query.setLimit(20);
        query.addDescendingOrder(Posts.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Posts>() {
            @Override
            public void done(List<Posts> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for (Posts post: posts) {
                    Log.i(TAG, "Posts: " + post.getDescription()+ ", username: " + post.getUser().getUsername());

                    swipeContainer.setRefreshing(false);
                }
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }


}