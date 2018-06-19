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
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by eisat on 3/17/2018.
 */

public class CustomerAdapter extends ArrayAdapter<Customer> {

    private Context mContext;

    public CustomerAdapter(Activity context, ArrayList<Customer> customers)
    {
        super(context, 0, customers);
        mContext = context;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        View listItemView = convertView;
        if (listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.customer_list_item, parent, false);

        final Customer currentCustomer = getItem(position);

        TextView firstNameView = (TextView) listItemView.findViewById(R.id.customer_list_firstName);
        firstNameView.setText(currentCustomer.getFirstName());

        TextView lastNameView = (TextView) listItemView.findViewById(R.id.customer_list_lastName);
        lastNameView.setText(currentCustomer.getLastName());

        TextView moneyView = (TextView) listItemView.findViewById(R.id.customer_list_money_input);
        moneyView.setText(Integer.toString(currentCustomer.getMoney()));

        TextView capacityView = (TextView) listItemView.findViewById(R.id.customer_list_capacity_input);
        capacityView.setText(Integer.toString(currentCustomer.getCapacity()));

        TextView emptyListView = (TextView) listItemView.findViewById(R.id.customer_list_empty_bgList);
        emptyListView.setText(R.string.customer_list_empty_list);

        ListView bgListView = (ListView) listItemView.findViewById(R.id.customer_list_bgList);
        bgListView.setEmptyView(emptyListView);


        BoardGameAdapter bgAdapter = new BoardGameAdapter((Activity) mContext, currentCustomer.getInventory());
        bgListView.setAdapter(bgAdapter);

        Button editButton = (Button) listItemView.findViewById(R.id.customer_list_edit_button);
        editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Bundle data = new Bundle();
                data.putParcelable("customer", currentCustomer);
                Intent editIntent = new Intent (mContext, EditCustomerActivity.class);
                editIntent.putExtra("data", data);
                mContext.startActivity(editIntent);
            }
        });

        Button deleteButton = (Button) listItemView.findViewById(R.id.customer_list_delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String baseUrl = mContext.getResources().getString(R.string.API_customer_address);
                baseUrl = baseUrl + "/" + currentCustomer.getID();
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
                            Intent customerIntent = new Intent (mContext, CustomerActivity.class);
                            mContext.startActivity(customerIntent);
                        }
                    }
                });
            }
        });

        Button purchaseButton = (Button) listItemView.findViewById(R.id.customer_list_purchase_button);
        purchaseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // send intent to view board games with a purchase button
                // probably need a switch or an if statement inside BGAdapter to handle this case
                Bundle data = new Bundle();
                data.putString("customer_id", currentCustomer.getID());
                Intent purchaseIntent = new Intent(mContext, PurchaseBoardGameActivity.class);
                purchaseIntent.putExtra("data", data);
                mContext.startActivity(purchaseIntent);
            }
        });

        Button returnButton = (Button) listItemView.findViewById(R.id.customer_list_return_button);
        returnButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // send intent to view customers board games with a return button
                // probably need a switch or an if statement inside BGAdapter to handle this case
                Bundle data = new Bundle();
                data.putParcelableArrayList("inventory", currentCustomer.getInventory());
                data.putString("customer_id", currentCustomer.getID());
                Intent returnIntent = new Intent(mContext, ReturnBoardGameActivity.class);
                returnIntent.putExtra("data", data);
                mContext.startActivity(returnIntent);
            }
        });

        return listItemView;
    }


}
