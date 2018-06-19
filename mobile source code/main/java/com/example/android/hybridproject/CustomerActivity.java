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

public class CustomerActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Customer>> {
    private CustomerAdapter mAdapter;
    private TextView mEmptyStateTextView;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    private static final int CUSTOMER_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        mEmptyStateTextView = (TextView) findViewById(R.id.customer_empty_view);

        ListView customerListView = (ListView) findViewById(R.id.customer_list);

        mEmptyStateTextView = (TextView) findViewById(R.id.customer_empty_view);
        customerListView.setEmptyView(mEmptyStateTextView);

        mAdapter = new CustomerAdapter(this, new ArrayList<Customer>());

        customerListView.setAdapter(mAdapter);

        LoaderManager loaderManager = getLoaderManager();

        loaderManager.initLoader(CUSTOMER_LOADER_ID, null, this);
    }


    @Override
    public Loader<List<Customer>> onCreateLoader(int i, Bundle bundle)
    {
        return new CustomerLoader(this, getResources().getString(R.string.API_customer_address));
    }

    @Override
    public void onLoadFinished(Loader<List<Customer>> loader, List<Customer> customers)
    {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.customer_loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mEmptyStateTextView.setText(R.string.no_customers);

        mAdapter.clear();

        if (customers != null && !customers.isEmpty())
            mAdapter.addAll(customers);
    }

    @Override
    public void onLoaderReset(Loader<List<Customer>> loader)
    {
        mAdapter.clear();
    }
}
