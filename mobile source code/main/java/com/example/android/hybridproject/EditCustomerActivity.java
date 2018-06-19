package com.example.android.hybridproject;

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

public class EditCustomerActivity extends AppCompatActivity {

    private String LOG_TAG = "EditCustomerActivity";
    private EditText mFirstNameInput;
    private EditText mLastNameInput;
    private EditText mMoneyInput;
    private EditText mCapacityInput;

    private Button mSubmitButton;

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customer);

        Bundle data = getIntent().getBundleExtra("data");
        final Customer currentCustomer = data.getParcelable("customer");

        mFirstNameInput = (EditText) findViewById(R.id.edit_customer_firstName_input);
        mFirstNameInput.setText(currentCustomer.getFirstName());

        mLastNameInput = (EditText) findViewById(R.id.edit_customer_lastName_input);
        mLastNameInput.setText(currentCustomer.getLastName());

        mMoneyInput = (EditText) findViewById(R.id.edit_customer_money_input);
        mMoneyInput.setText(Integer.toString(currentCustomer.getMoney()));

        mCapacityInput = (EditText) findViewById(R.id.edit_customer_capacity_input);
        mCapacityInput.setText(Integer.toString(currentCustomer.getCapacity()));

        mSubmitButton = (Button) findViewById(R.id.edit_customer_submit_button);
        mSubmitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String baseUrl = getResources().getString(R.string.API_customer_address);
                baseUrl = baseUrl + "/" + currentCustomer.getID();
                HttpUrl reqUrl = HttpUrl.parse(baseUrl);


                // create a json string from inputs

                String firstName = mFirstNameInput.getText().toString();
                String lastName = mLastNameInput.getText().toString();
                String capacity = mCapacityInput.getText().toString();
                String money = mMoneyInput.getText().toString();
                JSONObject postData = new JSONObject();
                //String strPostData;
                try{
                    postData.put("firstName", firstName);
                    postData.put("lastName", lastName);
                    postData.put("capacity", Integer.valueOf(capacity));
                    postData.put("money", Integer.valueOf(money));

                }
                catch(JSONException exception){
                    Log.e(LOG_TAG, exception.toString());
                }


                RequestBody body = RequestBody.create(JSON, postData.toString());
                Request request = new Request.Builder()
                        .url(reqUrl)
                        .patch(body)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //String r = response.body().string();
                        //do stuff with the body of the response
                        if (Integer.toString(response.code()).matches(getResources().getString(R.string.http_status_code_204))){
                            Intent customerIntent = new Intent (EditCustomerActivity.this, CustomerActivity.class);
                            startActivity(customerIntent);
                        }
                    }
                });
            }
        });

    }
}
