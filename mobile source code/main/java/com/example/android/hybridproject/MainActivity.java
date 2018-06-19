package com.example.android.hybridproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity  {

    private Context mContext;
    private int mPermissionsRequestCallback = 17;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE}, mPermissionsRequestCallback);
        }

        Button boardGameButton = (Button) findViewById(R.id.main_boardgame_button);

        boardGameButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent boardGameIntent = new Intent(MainActivity.this, BoardGameActivity.class);
                startActivity(boardGameIntent);
            }
        });

        Button addBoardGameButton = (Button) findViewById(R.id.main_add_boardgame_button);

        addBoardGameButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent addBGIntent = new Intent(MainActivity.this, AddBoardGameActivity.class);
                startActivity(addBGIntent);
            }
        });

        Button customersButton = (Button) findViewById(R.id.main_customers_button);


        customersButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent customerIntent = new Intent(MainActivity.this, CustomerActivity.class);
                startActivity(customerIntent);
            }
        });

        Button addCustomerButton = (Button) findViewById(R.id.main_add_customer_button);
        addCustomerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent addCustomerIntent = new Intent(MainActivity.this, AddCustomerActivity.class);
                startActivity(addCustomerIntent);
            }
        });

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == mPermissionsRequestCallback){
            if (grantResults.length > 0 && grantResults[0] == mPermissionsRequestCallback){
                Toast.makeText(this, "INTERNET Permission Granted", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
