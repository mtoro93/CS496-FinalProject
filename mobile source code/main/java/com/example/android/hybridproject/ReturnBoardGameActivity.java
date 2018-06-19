package com.example.android.hybridproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class ReturnBoardGameActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_return_board_game);
        mEmptyStateTextView = (TextView) findViewById(R.id.return_bg_empty_view);

        // get customer ID from bundle
        Bundle data = getIntent().getBundleExtra("data");
        ArrayList<Boardgame> inventory = data.getParcelableArrayList("inventory");
        mCustomerId = data.getString("customer_id");
        ListView boardgameListView = (ListView) findViewById(R.id.return_bg_list);

        boardgameListView.setEmptyView(mEmptyStateTextView);

        mAdapter = new BoardGameAdapter(this, inventory);
        mAdapter.setmCustomerID(mCustomerId);
        boardgameListView.setAdapter(mAdapter);
    }

    public String getCustomerId(){
        return mCustomerId;
    }
}
