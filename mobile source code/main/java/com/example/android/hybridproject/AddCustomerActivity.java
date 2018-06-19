package com.example.android.hybridproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddCustomerActivity extends AppCompatActivity {

    String LOG_TAG = "AddBoardGameActivity";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    private EditText mFirstNameInput;
    private EditText mLastNameInput;
    private EditText mCapacityInput;
    private EditText mMoneyInput;

    private Button mSubmitButton;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        mContext = this;
        mFirstNameInput = (EditText) findViewById(R.id.add_customer_firstName_input);
        mLastNameInput = (EditText) findViewById(R.id.add_customer_lastName_input);
        mCapacityInput = (EditText) findViewById(R.id.add_customer_capacity_input);
        mMoneyInput = (EditText) findViewById(R.id.add_customer_money_input);
        mSubmitButton = (Button) findViewById(R.id.add_customer_submit_button);

        mSubmitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                HttpUrl reqUrl = HttpUrl.parse(getResources().getString(R.string.API_customer_address));


                // create a json string from inputs

                String firstName = mFirstNameInput.getText().toString();
                String lastName = mLastNameInput.getText().toString();
                String money = mMoneyInput.getText().toString();
                String capacity = mCapacityInput.getText().toString();
                JSONObject postData = new JSONObject();
                //String strPostData;
                try{
                    postData.put("firstName", firstName);
                    postData.put("lastName", lastName);
                    postData.put("money", Integer.valueOf(money));
                    postData.put("capacity", Integer.valueOf(capacity));

                }
                catch(JSONException exception){
                    Log.e(LOG_TAG, exception.toString());
                }


                RequestBody body = RequestBody.create(JSON, postData.toString());
                Request request = new Request.Builder()
                        .url(reqUrl)
                        .post(body)
                        .build();
                //String response = post(reqUrl, postData.toString());
                // Response response = client.newCall(request).execute();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //String r = response.body().string();
                        //do stuff with the body of the response
                        if (Integer.toString(response.code()).matches(getResources().getString(R.string.http_status_code_201))){
                            Intent customerIntent = new Intent (AddCustomerActivity.this, CustomerActivity.class);
                            startActivity(customerIntent);
                        }
                    }
                });
            }
        });

    }

    /*
    String post(okhttp3.HttpUrl url, String json) throws IOException {

        try (Response response = client.newCall(request).execute()) {
            return response.message();
        }
    }
    */


}

