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

public class EditBoardGameActivity extends AppCompatActivity {

    private String LOG_TAG = "EditBoardGameActivity";
    private EditText mTitleInput;
    private EditText mDescriptionInput;
    private EditText mStockInput;
    private EditText mPriceInput;

    private Button mSubmitButton;

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_board_game);

        Bundle data = getIntent().getBundleExtra("data");
        final Boardgame currentGame = data.getParcelable("boardgame");

        mTitleInput = (EditText) findViewById(R.id.edit_bg_title_input);
        mTitleInput.setText(currentGame.getTitle());

        mDescriptionInput = (EditText) findViewById(R.id.edit_bg_description_input);
        mDescriptionInput.setText(currentGame.getDescription());

        mStockInput = (EditText) findViewById(R.id.edit_bg_stock_input);
        mStockInput.setText(Integer.toString(currentGame.getStock()));

        mPriceInput = (EditText) findViewById(R.id.edit_bg_price_input);
        mPriceInput.setText(Integer.toString(currentGame.getPrice()));

        mSubmitButton = (Button) findViewById(R.id.edit_bg_submit_button);
        mSubmitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String baseUrl = getResources().getString(R.string.API_boardgame_address);
                baseUrl = baseUrl + "/" + currentGame.getID();
                HttpUrl reqUrl = HttpUrl.parse(baseUrl);


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
                            Intent bgIntent = new Intent (EditBoardGameActivity.this, BoardGameActivity.class);
                            startActivity(bgIntent);
                        }
                    }
                });
            }
        });

    }
}
