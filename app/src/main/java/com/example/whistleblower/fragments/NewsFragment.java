package com.example.whistleblower.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whistleblower.EndlessRecyclerViewScrollListener;
import com.example.whistleblower.NewsAdapter;
import com.example.whistleblower.R;
import com.example.whistleblower.Utilities;
import com.example.whistleblower.api.ApiClient;
import com.example.whistleblower.api.ApiInterface;
import com.example.whistleblower.models.News;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFragment extends Fragment {

    public static final String TAG = "NewsActivity";
    public static final String API_KEY = "c828d95279fa4f87bae67dbcb1fc35ff";

    private RecyclerView rvNews;
    protected NewsAdapter adapter;
    protected SwipeRefreshLayout swipeContainer;
    EndlessRecyclerViewScrollListener.EndlessRVScrollListener scrollListener;
//    protected RecyclerView.LayoutManager layoutManager;

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvNews = view.findViewById(R.id.rvNews);
        Log.i(TAG, "1");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // articles = new ArrayList<>();
        adapter = new NewsAdapter(getContext());
        // 3. Set the adapter on the RV
        rvNews.setAdapter(adapter);
        Log.i(TAG, "2");
        // Set the layout manager on the RV
        rvNews.setLayoutManager(layoutManager);
        rvNews.setItemAnimator(new DefaultItemAnimator());
        rvNews.setNestedScrollingEnabled(false);

        scrollListener = new EndlessRecyclerViewScrollListener.EndlessRVScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.i(TAG, "onLoadMore: " + page);
                loadNews();
            }
        };

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                // Code to refresh the list
                loadNews();
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

        loadNews();
    }

    public void loadNews() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        String country = Utilities.getCountry();
        Call<News> call;
        call = apiInterface.getNews(country, API_KEY);

        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.isSuccessful() && response.body().getArticle() != null){
                    adapter.articles = response.body().getArticle();
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e(TAG, "No Result");
                }
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.e(TAG, "Failure");
            }
        });
    }
}