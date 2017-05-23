package com.privat.androiddev.privat.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.privat.androiddev.privat.R;
import com.privat.androiddev.privat.datamodel.ExchangeRate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AndroidDev on 23.05.2017.
 */

public class ExchangeRateRecyclerViewAdapter extends RecyclerView.Adapter<ExchangeRateRecyclerViewAdapter.ViewHolder> {

    private List<ExchangeRate> rateList = new ArrayList<>();

    public ExchangeRateRecyclerViewAdapter(List<ExchangeRate> rateList) {
        this.rateList = rateList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.exchange_rate_card_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ExchangeRate rate = rateList.get(position);
        if(rate!=null){
            String purchaseRate = "Purchase rate: "+rate.getPurchaseRateNB().toString();
            String saleRate = "Sale rate: "+rate.getSaleRateNB().toString();
            holder.lblPurchaseRate.setText(purchaseRate);
            holder.lblSaleRate.setText(saleRate);
            holder.lblCurrency.setText(rate.getCurrency());
        }

    }

    @Override
    public int getItemCount() {
        return rateList.size();
    }

    public void clear(){
        if(rateList!=null){
            rateList.clear();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView lblPurchaseRate;
        TextView lblSaleRate;
        TextView lblCurrency;
        ImageView cvImage;
        CardView rateCardView;

        public ViewHolder(View v) {
            super(v);
            lblPurchaseRate = (TextView)v.findViewById(R.id.lblPurchaseRate);
            lblSaleRate = (TextView)v.findViewById(R.id.lblSaleRate);
            lblCurrency = (TextView)v.findViewById(R.id.lblCurrency);
            cvImage = (ImageView) v.findViewById(R.id.cvImage);
            rateCardView = (CardView) v.findViewById(R.id.rateCardView);
        }
    }
}
