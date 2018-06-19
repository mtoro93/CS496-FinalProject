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

public class AddBoardGameActivity extends AppCompatActivity {

    String LOG_TAG = "AddBoardGameActivity";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    private EditText mTitleInput;
    private EditText mDescriptionInput;
    private EditText mStockInput;
    private EditText mPriceInput;

    private Button mSubmitButton;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_board_game);
        mContext = this;
        mTitleInput = (EditText) findViewById(R.id.add_bg_title_input);
        mDescriptionInput = (EditText) findViewById(R.id.add_bg_description_input);
        mStockInput = (EditText) findViewById(R.id.add_bg_stock_input);
        mPriceInput = (EditText) findViewById(R.id.add_bg_price_input);
        mSubmitButton = (Button) findViewById(R.id.add_bg_submit_button);

        mSubmitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                HttpUrl reqUrl = HttpUrl.parse(getResources().getString(R.string.API_boardgame_address));


                // create a json string from inputs

                String title = mTitleInput.getText().toString();
                String description = mDescriptionInput.getText().toString();
                String price = mPriceInput.getText().toString();
                String stock = mStockInput.getText().toString();
                JSONObject postData = new JSONObject();
                //String strPostData;
                try{
                    postData.put("title", title);
                    postData.put("description", description);
                    postData.put("price", Integer.valueOf(price));
                    postData.put("stock", Integer.valueOf(stock));

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
                            Intent bgIntent = new Intent (AddBoardGameActivity.this, BoardGameActivity.class);
                            startActivity(bgIntent);
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
