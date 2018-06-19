package com.example.android.hybridproject;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by eisat on 3/16/2018.
 */

public class BoardGameLoader extends AsyncTaskLoader<List<Boardgame>> {

    private String mUrl;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client = new OkHttpClient();

    // pass in the resource URL here
    public BoardGameLoader(Context context, String url){
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Boardgame> loadInBackground() {

        final List<Boardgame> boardgames = new ArrayList<>();
        HttpUrl reqUrl = HttpUrl.parse(mUrl);
        Request request = new Request.Builder()
                .url(reqUrl)
                .build();
        try {
            // Already off the main thread, so do not async this request
            // because we need the list to get populated before we return
            Response response = client.newCall(request).execute();
            final String r = response.body().string();
            try {
                JSONArray bgs = new JSONArray(r);
                for (int i = 0; i < bgs.length(); i++){
                    Boardgame bg = new Boardgame(bgs.getJSONObject(i).getString("title"),
                            bgs.getJSONObject(i).getString("description"),
                            bgs.getJSONObject(i).getInt("price"),
                            bgs.getJSONObject(i).getInt("stock"),
                            bgs.getJSONObject(i).getString("id"));
                    // create the list of boardgames
                    boardgames.add(bg);
                }
            }
            catch (JSONException exception){
                exception.printStackTrace();
            }
        }
        catch (IOException exception){
            Log.e("BOARDGAMELOADER", exception.toString());
        }
        return boardgames;
    }
}
