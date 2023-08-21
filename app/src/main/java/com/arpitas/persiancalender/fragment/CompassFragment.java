package com.arpitas.persiancalender.fragment;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.arpitas.persiancalender.CompassListener;
import com.arpitas.persiancalender.R;
import com.arpitas.persiancalender.QiblaCompassView;
import com.arpitas.persiancalender.entity.CityEntity;
import com.arpitas.persiancalender.praytimes.Coordinate;
import com.arpitas.persiancalender.util.LanguageUtil;
import com.arpitas.persiancalender.util.SharedPrefManager;
import com.arpitas.persiancalender.util.Utils;

public class CompassFragment extends Fragment {
    public QiblaCompassView compassView;
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener compassListener;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SharedPrefManager shared = new SharedPrefManager(getContext());
        if (!shared.getLanguage().equals(""))
            LanguageUtil.changeLanguage(getContext(), shared.getLanguage());

        /*----------------------------------------------------------------------------------------*/
        View view = inflater.inflate(R.layout.fragment_compass, container, false);
        Context context = getContext();
        /**
         *
         *
         * set banner ad
         */
        Utils utils = Utils.getInstance(context);
        Coordinate coordinate = utils.getCoordinate();
        if (coordinate == null) {
            utils.setActivityTitleAndSubtitle(getActivity(), getString(R.string.compass), "");
        } else {
            CityEntity cityEntity = utils.getCityFromPreference();
            String subtitle;
            if (cityEntity != null) {
                subtitle = utils.getAppLanguage().equals("en")
                    ? cityEntity.getEn()
                    : cityEntity.getFa();
            } else {
                subtitle = coordinate.getLatitude() + ", " + coordinate.getLongitude();
            }
            utils.setActivityTitleAndSubtitle(getActivity(), getString(R.string.qibla_compass), subtitle);
        }

        compassListener = new CompassListener(this);
        compassView = view.findViewById(R.id.compass_view);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        compassView.setScreenResolution(width, height - 2 * height / 8);
        if (coordinate != null) {
            compassView.setLongitude(coordinate.getLongitude());
            compassView.setLatitude(coordinate.getLatitude());
            compassView.initCompassView();
            compassView.invalidate();
        }
        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        if (sensor != null) {
            sensorManager.registerListener(compassListener, sensor,
                    SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            utils.quickToast(getString(R.string.compass_not_found));
        }
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sensor != null) {
            sensorManager.unregisterListener(compassListener);
        }
    }
}
