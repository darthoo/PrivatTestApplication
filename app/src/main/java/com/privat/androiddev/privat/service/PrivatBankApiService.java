package com.privat.androiddev.privat.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;

import com.privat.androiddev.privat.PrivatBankApi.PrivatBankApi;
import com.privat.androiddev.privat.datamodel.Archive;
import com.privat.androiddev.privat.utils.Constants;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by AndroidDev on 21.05.2017.
 */

public class PrivatBankApiService extends Service {

    private static final String BASE_URL = "https://api.privatbank.ua/";
    private static final String TAG = "Privat Bank Api Service";

    private Retrofit retrofit;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"On bind");
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"Started");
        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        String dateString = intent.getStringExtra(Constants.DATE);

        if (dateString != null && !dateString.isEmpty()) {
            PrivatBankApi privatBankApi = retrofit.create(PrivatBankApi.class);
            Observable<Archive> archiveObservable = privatBankApi.getRateArchiveByDate("true", dateString);

            archiveObservable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Archive>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull Archive archive) {
                            Intent intent = new Intent(Constants.BROADCAST_PRIVAT_API_SERVICE_ACTION);
                            intent.putExtra(Constants.ARCHIVE, archive);
                            sendBroadcast(intent);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Log.e(TAG,"Error during request API");
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        } else {
            Log.d(TAG,"Invalid date");
        }
        return super.onStartCommand(intent, flags, startId);

    }

    public void onCreate() {
        super.onCreate();
        Log.d("Service: ", "created");
    }
}
