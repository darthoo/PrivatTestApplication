package com.privat.androiddev.privat.activity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.privat.androiddev.privat.R;
import com.privat.androiddev.privat.adapter.ExchangeRateRecyclerViewAdapter;
import com.privat.androiddev.privat.datamodel.Archive;
import com.privat.androiddev.privat.service.PrivatBankApiService;
import com.privat.androiddev.privat.utils.Constants;

public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;
    private IntentFilter intentFilter;

    private RecyclerView mRecyclerView;
    private ExchangeRateRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLayoutManager = new LinearLayoutManager(this);

        broadcastReceiver = new BroadcastReceiver() {
            @Override

            public void onReceive(Context context, Intent intent) {
                Archive archive = (Archive) intent.getSerializableExtra(Constants.ARCHIVE);
                mRecyclerView = (RecyclerView) findViewById(R.id.exchangeRateRecyclerView);
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setLayoutManager(mLayoutManager);

                mAdapter = new ExchangeRateRecyclerViewAdapter(archive.getExchangeRate());
                mRecyclerView.setAdapter(mAdapter);
                System.out.println();
            }
        };

        intentFilter = new IntentFilter(Constants.BROADCAST_PRIVAT_API_SERVICE_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);


    }


    public void onGetRatesClick(View view) {
        EditText editText = (EditText) findViewById(R.id.dateSelector);
        String dateString = editText.getText().toString();
        startService(new Intent(this, PrivatBankApiService.class).putExtra(Constants.DATE, dateString));
    }

    public void onClearClick(View view){
        mAdapter.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }
}
