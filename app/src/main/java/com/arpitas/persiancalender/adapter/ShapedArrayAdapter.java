package com.arpitas.persiancalender.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.arpitas.persiancalender.util.Utils;

import java.util.List;

public class ShapedArrayAdapter extends ArrayAdapter {
    private Utils utils;

    public ShapedArrayAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        utils = Utils.getInstance(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        if (view instanceof TextView) utils.setFontAndShape((TextView) view);
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        if (view instanceof TextView) utils.setFontAndShape((TextView) view);
        return view;
    }
}
