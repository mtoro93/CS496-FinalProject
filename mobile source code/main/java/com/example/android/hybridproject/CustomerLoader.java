package com.example.android.hybridproject;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by eisat on 3/17/2018.
 */

public class CustomerLoader extends AsyncTaskLoader<List<Customer>> {

    private String mUrl;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");


    // pass in the resource URL here
    public CustomerLoader(Context context, String url){
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Customer> loadInBackground() {

        final List<Customer> customers = new ArrayList<>();
        HttpUrl reqUrl = HttpUrl.parse(mUrl);
        Request request = new Request.Builder()
                .url(reqUrl)
                .build();
        try {
            OkHttpClient client = new OkHttpClient();
            // Already off the main thread, so do not async this request
            // because we need the list to get populated before we return
            Response response = client.newCall(request).execute();
            final String r = response.body().string();
            Log.d("GET CUSTOMER RESPONSE", r);
            try {
                JSONArray custs = new JSONArray(r);

                for (int i = 0; i < custs.length(); i++){


                    JSONArray bgUrls = custs.getJSONObject(i).getJSONArray("inventory");
                    Log.d("INVENTORY", bgUrls.toString());
                    Log.d("INVENTORY LENGTH", Integer.toString(bgUrls.length()));
                    ArrayList<Boardgame> inventory = new ArrayList<>();

                    // create boardgame inventory list if applicable
                    for (int j = 0; j < bgUrls.length(); j++){
                        String baseUrl = "http://";
                        baseUrl = baseUrl + bgUrls.get(j);
                        Log.d("GET BOARDGAME URL", baseUrl);
                        HttpUrl inventoryReqUrl = HttpUrl.parse(baseUrl);
                        Request inventoryRequest = new Request.Builder()
                                .url(inventoryReqUrl)
                                .build();
                        Response inventoryResponse = client.newCall(inventoryRequest).execute();
                        final String bgResponseString = inventoryResponse.body().string();
                        Log.d("GET BOARDGAME RESPONSE", bgResponseString);
                        JSONObject jsonObject = new JSONObject(bgResponseString);
                        Boardgame inventoryBG = new Boardgame(jsonObject.getString("title"),
                                jsonObject.getString("description"),
                                jsonObject.getInt("price"),
                                jsonObject.getInt("stock"),
                                jsonObject.getString("id"));
                        inventory.add(inventoryBG);
                    }

                    Customer c = new Customer(custs.getJSONObject(i).getString("firstName"),
                            custs.getJSONObject(i).getString("lastName"),
                            custs.getJSONObject(i).getInt("money"),
                            custs.getJSONObject(i).getInt("capacity"),
                            custs.getJSONObject(i).getString("id"),
                            inventory);
                    // create the list of customers
                    customers.add(c);
                }
            }
            catch (JSONException exception){
                exception.printStackTrace();
            }
        }
        catch (IOException exception){
            Log.e("CUSTOMER LOADER", exception.toString());
        }
        return customers;
    }
}
