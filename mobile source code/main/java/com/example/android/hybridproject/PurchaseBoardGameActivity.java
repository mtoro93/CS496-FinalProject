package com.example.android.hybridproject;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class PurchaseBoardGameActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Boardgame>> {
    private BoardGameAdapter mAdapter;
    private TextView mEmptyStateTextView;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    private static final int BOARDGAME_LOADER_ID = 1;
    private String mCustomerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_board_game);
        mEmptyStateTextView = (TextView) findViewById(R.id.purchase_bg_empty_view);

        // get customer ID from bundle
        Bundle data = getIntent().getBundleExtra("data");
        mCustomerId = data.getString("customer_id");

        ListView boardgameListView = (ListView) findViewById(R.id.purchase_bg_list);

        boardgameListView.setEmptyView(mEmptyStateTextView);

        mAdapter = new BoardGameAdapter(this, new ArrayList<Boardgame>());
        mAdapter.setmCustomerID(mCustomerId);
        boardgameListView.setAdapter(mAdapter);

        LoaderManager loaderManager = getLoaderManager();

        loaderManager.initLoader(BOARDGAME_LOADER_ID, null, this);
    }


    @Override
    public Loader<List<Boardgame>> onCreateLoader(int i, Bundle bundle)
    {
        return new BoardGameLoader(this, getResources().getString(R.string.API_boardgame_address));
    }

    @Override
    public void onLoadFinished(Loader<List<Boardgame>> loader, List<Boardgame> boardgames)
    {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.purchase_bg_loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mEmptyStateTextView.setText(R.string.no_boardgames);

        mAdapter.clear();

        if (boardgames != null && !boardgames.isEmpty())
            mAdapter.addAll(boardgames);
    }

    @Override
    public void onLoaderReset(Loader<List<Boardgame>> loader)
    {
        mAdapter.clear();
    }

    public String getCustomerId(){
        return mCustomerId;
    }
}
