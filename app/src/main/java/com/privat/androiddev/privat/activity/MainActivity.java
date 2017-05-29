package com.privat.androiddev.privat.activity;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.privat.androiddev.privat.R;
import com.privat.androiddev.privat.adapter.ExchangeRateRecyclerViewAdapter;
import com.privat.androiddev.privat.datamodel.Archive;
import com.privat.androiddev.privat.datamodel.ExchangeRate;
import com.privat.androiddev.privat.service.PrivatBankApiService;
import com.privat.androiddev.privat.utils.Constants;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main Activity";

    private BroadcastReceiver broadcastReceiver;
    private IntentFilter intentFilter;

    private RecyclerView mRecyclerView;
    private ExchangeRateRecyclerViewAdapter mAdapter = new ExchangeRateRecyclerViewAdapter(new ArrayList<ExchangeRate>());
    private LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
    private EditText etDate;
    private DatePickerDialog datePickerDialog;
    private Archive currentArchive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"On create");
        setContentView(R.layout.activity_main);
        etDate = (EditText) findViewById(R.id.etDate);
        mRecyclerView = (RecyclerView) findViewById(R.id.exchangeRateRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this,mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(itemDecoration);


        broadcastReceiver = new BroadcastReceiver() {
            @Override

            public void onReceive(Context context, Intent intent) {
                Archive archive = (Archive) intent.getSerializableExtra(Constants.ARCHIVE);
                currentArchive = archive;
                mAdapter.update(archive.getExchangeRate());
                mAdapter.notifyDataSetChanged();
            }
        };

        intentFilter = new IntentFilter(Constants.BROADCAST_PRIVAT_API_SERVICE_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);

    }


    public void onGetRatesClick(View view) {
        EditText editText = (EditText) findViewById(R.id.etDate);
        String dateString = editText.getText().toString();
        startService(new Intent(this, PrivatBankApiService.class).putExtra(Constants.DATE, dateString));
    }

    public void onEditDateClick(View view) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(MainActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int month, int day) {
                        etDate.setText(day + "."
                                + (month + 1) + "." + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void onClearClick(View view) {
        if(etDate!=null){
            etDate.setText("");
        }
        mAdapter.clear();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (currentArchive != null) {
            outState.putSerializable(Constants.ARCHIVE, currentArchive);
            Log.d(TAG, "Saving current archive");
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Archive archive = (Archive) savedInstanceState.getSerializable(Constants.ARCHIVE);
        if (archive != null) {
            currentArchive = archive;
            mAdapter.update(archive.getExchangeRate());
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "On destroy.");
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }
}
