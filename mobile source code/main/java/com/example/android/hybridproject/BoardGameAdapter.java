package com.example.android.hybridproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by eisat on 3/16/2018.
 */

public class BoardGameAdapter extends ArrayAdapter<Boardgame> {

    private Context mContext;
    private String mCustomerID;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public BoardGameAdapter(Activity context, ArrayList<Boardgame> boardgames)
        {
            super(context, 0, boardgames);
            mContext = context;
        }



        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View listItemView = convertView;
            final Boardgame currentGame;
            TextView titleView;
            TextView descriptionView;
            TextView priceView;
            TextView stockView;

            switch(parent.getId()){
                case(R.id.customer_list_bgList):
                    if (listItemView == null)
                        listItemView = LayoutInflater.from(getContext()).inflate(R.layout.customer_bg_list_item, parent, false);

                    currentGame = getItem(position);

                    titleView = (TextView) listItemView.findViewById(R.id.customer_bg_list_title);
                    titleView.setText(currentGame.getTitle());
                    break;
                case(R.id.bg_list):
                    if (listItemView == null)
                        listItemView = LayoutInflater.from(getContext()).inflate(R.layout.bg_list_item, parent, false);

                    currentGame = getItem(position);

                    titleView = (TextView) listItemView.findViewById(R.id.bg_list_title);
                    titleView.setText(currentGame.getTitle());

                    descriptionView = (TextView) listItemView.findViewById(R.id.bg_list_description);
                    descriptionView.setText(currentGame.getDescription());

                    priceView = (TextView) listItemView.findViewById(R.id.bg_list_price_input);
                    priceView.setText(Integer.toString(currentGame.getPrice()));

                    stockView = (TextView) listItemView.findViewById(R.id.bg_list_stock_input);
                    stockView.setText(Integer.toString(currentGame.getStock()));

                    Button editButton = (Button) listItemView.findViewById(R.id.bg_list_edit_button);
                    editButton.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {
                            Bundle data = new Bundle();
                            data.putParcelable("boardgame", currentGame);
                            Intent editIntent = new Intent (mContext, EditBoardGameActivity.class);
                            editIntent.putExtra("data", data);
                            mContext.startActivity(editIntent);
                        }
                    });

                    Button deleteButton = (Button) listItemView.findViewById(R.id.bg_list_delete_button);
                    deleteButton.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {
                            String baseUrl = mContext.getResources().getString(R.string.API_boardgame_address);
                            baseUrl = baseUrl + "/" + currentGame.getID();
                            HttpUrl reqUrl = HttpUrl.parse(baseUrl);
                            Request request = new Request.Builder()
                                    .url(reqUrl)
                                    .delete()
                                    .build();
                            OkHttpClient client = new OkHttpClient();
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    e.printStackTrace();
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    //String r = response.body().string();
                                    //do stuff with the body of the response
                                    if (Integer.toString(response.code()).matches(mContext.getResources().getString(R.string.http_status_code_204))){
                                        Intent bgIntent = new Intent (mContext, BoardGameActivity.class);
                                        mContext.startActivity(bgIntent);
                                    }
                                }
                            });
                        }
                    });
                    break;
                case(R.id.purchase_bg_list):
                    if (listItemView == null)
                        listItemView = LayoutInflater.from(getContext()).inflate(R.layout.purchase_bg_list_item, parent, false);

                    currentGame = getItem(position);

                    titleView = (TextView) listItemView.findViewById(R.id.purchase_bg_list_title);
                    titleView.setText(currentGame.getTitle());

                    descriptionView = (TextView) listItemView.findViewById(R.id.purchase_bg_list_description);
                    descriptionView.setText(currentGame.getDescription());

                    priceView = (TextView) listItemView.findViewById(R.id.purchase_bg_list_price_input);
                    priceView.setText(Integer.toString(currentGame.getPrice()));

                    stockView = (TextView) listItemView.findViewById(R.id.purchase_bg_list_stock_input);
                    stockView.setText(Integer.toString(currentGame.getStock()));

                    Button purchaseButton = (Button) listItemView.findViewById(R.id.purchase_bg_list_purchase_button);
                    purchaseButton.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {
                            String baseUrl = mContext.getResources().getString(R.string.API_boardgame_address);
                            baseUrl = baseUrl + "/" + currentGame.getID();
                            HttpUrl reqUrl = HttpUrl.parse(baseUrl);
                            JSONObject putData = new JSONObject();
                            try{
                                putData.put("id", getmCustomerID());
                            }
                            catch(JSONException exception){
                                exception.printStackTrace();
                            }
                            RequestBody body = RequestBody.create(JSON, putData.toString());
                            Request request = new Request.Builder()
                                    .url(reqUrl)
                                    .put(body)
                                    .build();
                            OkHttpClient client = new OkHttpClient();
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    e.printStackTrace();
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    //String r = response.body().string();
                                    //do stuff with the body of the response
                                    if (Integer.toString(response.code()).matches(mContext.getResources().getString(R.string.http_status_code_204))){
                                        Intent customerIntent = new Intent (mContext, CustomerActivity.class);
                                        setmCustomerID(null);
                                        mContext.startActivity(customerIntent);
                                    }
                                }
                            });
                        }
                    });
                    break;
                case(R.id.return_bg_list):
                    if (listItemView == null)
                        listItemView = LayoutInflater.from(getContext()).inflate(R.layout.return_bg_list_item, parent, false);

                    currentGame = getItem(position);

                    titleView = (TextView) listItemView.findViewById(R.id.return_bg_list_title);
                    titleView.setText(currentGame.getTitle());

                    descriptionView = (TextView) listItemView.findViewById(R.id.return_bg_list_description);
                    descriptionView.setText(currentGame.getDescription());

                    priceView = (TextView) listItemView.findViewById(R.id.return_bg_list_price_input);
                    priceView.setText(Integer.toString(currentGame.getPrice()));

                    stockView = (TextView) listItemView.findViewById(R.id.return_bg_list_stock_input);
                    stockView.setText(Integer.toString(currentGame.getStock()));

                    Button returnButton = (Button) listItemView.findViewById(R.id.return_bg_list_return_button);
                    returnButton.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {
                            String baseUrl = mContext.getResources().getString(R.string.API_customer_address);
                            baseUrl = baseUrl + "/" + getmCustomerID() ;
                            HttpUrl reqUrl = HttpUrl.parse(baseUrl);
                            JSONObject putData = new JSONObject();
                            try{
                                putData.put("id", currentGame.getID());
                            }
                            catch(JSONException exception){
                                exception.printStackTrace();
                            }
                            RequestBody body = RequestBody.create(JSON, putData.toString());
                            Request request = new Request.Builder()
                                    .url(reqUrl)
                                    .put(body)
                                    .build();
                            OkHttpClient client = new OkHttpClient();
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    e.printStackTrace();
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    //String r = response.body().string();
                                    //do stuff with the body of the response
                                    if (Integer.toString(response.code()).matches(mContext.getResources().getString(R.string.http_status_code_204))){
                                        Intent customerIntent = new Intent (mContext, CustomerActivity.class);
                                        setmCustomerID(null);
                                        mContext.startActivity(customerIntent);
                                    }
                                }
                            });
                        }
                    });
                    break;
            }



            return listItemView;
        }


    public String getmCustomerID() {
        return mCustomerID;
    }

    public void setmCustomerID(String mCustomerID) {
        this.mCustomerID = mCustomerID;
    }
}
