package com.privat.androiddev.privat.PrivatBankApi;

import com.privat.androiddev.privat.datamodel.Archive;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * Created by AndroidDev on 21.05.2017.
 */

public interface PrivatBankApi {

    @GET("p24api/exchange_rates?")
    Observable<Archive> getRateArchiveByDate(@Query("json") String json, @Query("date") String date);
}
