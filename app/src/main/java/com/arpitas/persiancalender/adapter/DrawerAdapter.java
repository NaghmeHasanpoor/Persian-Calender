package com.arpitas.persiancalender.adapter;

import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.arpitas.persiancalender.activity.MainActivity;
import com.arpitas.persiancalender.R;
import com.arpitas.persiancalender.interfaces.DrawerListener;
import com.arpitas.persiancalender.util.Utils;

public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    public int selectedItem = 1;
    private String[] drawerTitles;
    private TypedArray drawerIcon;
    private Utils utils;
    private DrawerListener listener;

    public DrawerAdapter(MainActivity mainActivity, DrawerListener listener) {
        utils = Utils.getInstance(mainActivity);
        drawerTitles = mainActivity.getResources().getStringArray(R.array.me);
        drawerIcon = mainActivity.getResources().obtainTypedArray(R.array.drawerIcons);
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView itemTitle;
        private ImageView imageView;

        public ViewHolder(View itemView, int ViewType) {
            super(itemView);
            if (ViewType == TYPE_ITEM) {
                itemView.setOnClickListener(this);
                itemTitle = itemView.findViewById(R.id.itemTitle);
                imageView = itemView.findViewById(R.id.ItemIcon);
            } else {
                imageView = itemView.findViewById(R.id.image);
            }
        }

        @Override
        public void onClick(View view) {
            if (listener != null)
                listener.onClickDrawerItem(getAdapterPosition());
            selectedItem = getAdapterPosition();
            notifyDataSetChanged();
        }
    }

    @Override
    public DrawerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drawer, parent, false);
            return new ViewHolder(v, viewType);
        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_drawer, parent, false);
            return new ViewHolder(v, viewType);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(DrawerAdapter.ViewHolder holder, int position) {
        if (!isPositionHeader(position)) {
            holder.itemTitle.setText(utils.shape(drawerTitles[position - 1]));
            holder.imageView.setImageResource(drawerIcon.getResourceId(position - 1, 0));
        } else {
            switch (utils.getSeason()) {
                case SPRING:
                    holder.imageView.setImageResource(R.drawable.spring);
                    break;
                case SUMMER:
                    holder.imageView.setImageResource(R.drawable.summer);
                    break;
                case FALL:
                    holder.imageView.setImageResource(R.drawable.fall);
                    break;
                case WINTER:
                    holder.imageView.setImageResource(R.drawable.winter);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return drawerTitles.length + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }
}