package com.arpitas.persiancalender.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.arpitas.persiancalender.R;
import com.arpitas.persiancalender.entity.CityEntity;
import com.arpitas.persiancalender.preferences.LocationDialog;
import com.arpitas.persiancalender.util.SharedPrefManager;
import com.arpitas.persiancalender.util.Utils;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

   private List<CityEntity> cities;
   private Utils utils;
   //    LocationPreferenceDialog locationPreferenceDialog;
   LocationDialog locationDialog;
   LayoutInflater layoutInflater;
   private boolean isEnglish;

//    public LocationAdapter(LocationPreferenceDialog locationPreferenceDialog) {
//        Context context = locationPreferenceDialog.getContext();
//        utils = Utils.getInstance(locationPreferenceDialog.getContext());
//        this.layoutInflater = LayoutInflater.from(context);
//        this.locationPreferenceDialog = locationPreferenceDialog;
//        this.cities = utils.getAllCities(true);
//        this.isEnglish = new SharedPrefManager(context).getLanguage().equalsIgnoreCase(SharedPrefManager.EN);
//    }

   public LocationAdapter(LocationDialog locationDialog) {
      Context context = locationDialog.getContext();
      utils = Utils.getInstance(locationDialog.getContext());
      this.layoutInflater = LayoutInflater.from(context);
      this.locationDialog = locationDialog;
      this.cities = utils.getAllCities(true);
      this.isEnglish = new SharedPrefManager(context).getLanguage().equalsIgnoreCase(SharedPrefManager.EN);
   }

   public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

      private TextView country;
      private TextView city;

      public ViewHolder(View itemView) {
         super(itemView);
         itemView.setOnClickListener(this);
         city = itemView.findViewById(R.id.text1);
         country = itemView.findViewById(R.id.text2);
      }

      @Override
      public void onClick(View view) {
         locationDialog.selectItem(cities.get(getAdapterPosition()).getKey());
      }
   }

   @Override
   public LocationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new ViewHolder(layoutInflater.inflate(R.layout.list_item_city_name, parent, false));
   }

   @Override
   public void onBindViewHolder(ViewHolder holder, int position) {
      utils.setFont(holder.city);
      holder.city.setText(isEnglish
            ? cities.get(position).getEn()
            : utils.shape(cities.get(position).getFa()));

      utils.setFont(holder.country);
      holder.country.setText(isEnglish
            ? cities.get(position).getCountryEn()
            : utils.shape(cities.get(position).getCountryFa()));
   }

   @Override
   public int getItemCount() {
      return cities.size();
   }
}