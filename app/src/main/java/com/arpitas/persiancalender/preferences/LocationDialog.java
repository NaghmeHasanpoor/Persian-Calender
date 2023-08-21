package com.arpitas.persiancalender.preferences;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.arpitas.persiancalender.R;
import com.arpitas.persiancalender.adapter.LocationAdapter;

public class LocationDialog extends DialogFragment {

   public static final String TAG = "LocationDialog.TAG";

   private LocationSelectListener locationSelectListener;

   @Nullable
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      return inflater.inflate(R.layout.preference_location, container, false);
   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);

      RecyclerView recyclerView = view.findViewById(R.id.RecyclerView);
//      recyclerView.setHasFixedSize(true);
      recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
      recyclerView.setAdapter(new LocationAdapter(this));
   }

   @Override
   public void onDismiss(@NonNull DialogInterface dialog) {
      super.onDismiss(dialog);
      if (locationSelectListener != null) {
         locationSelectListener.onLocationSelected();
      }
   }

   public void selectItem(String city) {
      setSelected(city);
      dismiss();
   }

   public void setSelected(String selected) {
      SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
      SharedPreferences.Editor editor = prefs.edit();
      editor.putString("Location", selected);
      editor.apply();
   }

   public void setLocationSelectListener(LocationSelectListener locationSelectListener) {
      this.locationSelectListener = locationSelectListener;
   }

   public interface LocationSelectListener {

      void onLocationSelected();
   }
}
