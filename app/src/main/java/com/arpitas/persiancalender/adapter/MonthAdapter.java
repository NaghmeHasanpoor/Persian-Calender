package com.arpitas.persiancalender.adapter;

import android.content.Context;
import android.graphics.Typeface;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arpitas.persiancalender.ApplicationContexts;
import com.arpitas.persiancalender.Constants;
import com.arpitas.persiancalender.R;
import com.arpitas.persiancalender.entity.DayEntity;
import com.arpitas.persiancalender.util.SharedPrefManager;
import com.arpitas.persiancalender.util.Utils;
import com.arpitas.persiancalender.fragment.MonthFragment;

import java.io.IOException;
import java.util.List;

public class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.ViewHolder> {
    private Context context;
    private MonthFragment monthFragment;
    private final int TYPE_HEADER = 0;
    private final int TYPE_DAY = 1;
    private List<DayEntity> days;
    public int select_Day = -1;
    private Utils utils;
    private TypedValue colorHoliday = new TypedValue();
    private TypedValue colorTextHoliday = new TypedValue();
    private TypedValue colorPrimary = new TypedValue();
    private TypedValue colorDayName = new TypedValue();
    private boolean isEnglish;

    public MonthAdapter(Context context, MonthFragment monthFragment, List<DayEntity> days) {
        this.monthFragment = monthFragment;
        this.context = context;
        this.days = days;
        utils = Utils.getInstance(context);
        context.getTheme().resolveAttribute(R.attr.colorHoliday, colorHoliday, true);
        context.getTheme().resolveAttribute(R.attr.colorTextHoliday, colorTextHoliday, true);
        context.getTheme().resolveAttribute(R.attr.colorPrimary, colorPrimary, true);
        context.getTheme().resolveAttribute(R.attr.colorTextDayName, colorDayName, true);
        this.isEnglish = new SharedPrefManager(context).getLanguage().equalsIgnoreCase(SharedPrefManager.EN);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView num;
        View today, line;
        View selectDay;
        View event;

        public ViewHolder(View itemView) {
            super(itemView);
            num = itemView.findViewById(R.id.num);
            today = itemView.findViewById(R.id.today);
            selectDay = itemView.findViewById(R.id.select_day);
            line = itemView.findViewById(R.id.line);
            event = itemView.findViewById(R.id.event);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (getAdapterPosition() - 14 - days.get(0).getDayOfWeek() >= 0) {
                try {
                    monthFragment.onClickItem(days
                            .get(getAdapterPosition() - 14 - days.get(0).getDayOfWeek())
                            .getPersianDate());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                select_Day = getAdapterPosition();
                notifyDataSetChanged();
            }
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_day, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (!isPositionHeader(position)) {
            if (position - 14 - days.get(0).getDayOfWeek() >= 0) {
                String num = days.get(position - 14 - days.get(0).getDayOfWeek()).getNum();
                holder.num.setText(isEnglish? Utils.faToEn(num) : Utils.enToFa(num));
                holder.num.setVisibility(View.VISIBLE);
                holder.num.setTextSize(isEnglish ? 20 : 25);
                holder.num.setTextColor(days.get(position - 14 - days.get(0).getDayOfWeek()).isHoliday() ? ContextCompat.getColor(context, colorHoliday.resourceId) : ContextCompat.getColor(context, R.color.white));
                holder.event.setVisibility(days.get(position - 14 - days.get(0).getDayOfWeek()).isEvent() ? View.VISIBLE : View.GONE);
                holder.today.setVisibility(days.get(position - 14 - days.get(0).getDayOfWeek()).isToday() ? View.VISIBLE : View.GONE);
                if (position == select_Day) {
                    holder.selectDay.setVisibility(View.VISIBLE);
                    holder.num.setTextColor(days.get(position - 14 - days.get(0).getDayOfWeek()).isHoliday() ? ContextCompat.getColor(context, colorTextHoliday.resourceId):ContextCompat.getColor(context, R.color.white));
                } else {
                    holder.selectDay.setVisibility(View.GONE);
                }
            } else {
                holder.today.setVisibility(View.GONE);
                holder.selectDay.setVisibility(View.GONE);
                holder.num.setVisibility(View.GONE);
                holder.event.setVisibility(View.GONE);
                holder.line.setVisibility(View.GONE);
            }
            utils.setFontAndShape(holder.num);
        } else {
            if (position >= 0 && position < 7) {
                holder.num.setText(isEnglish ? Constants.FIRST_CHAR_OF_DAYS_OF_WEEK_NAME_EN[position] :Constants.FIRST_CHAR_OF_DAYS_OF_WEEK_NAME_FA[position]);
                holder.num.setTextColor(ContextCompat.getColor(context, colorDayName.resourceId));
                holder.num.setTextSize(23);
                holder.num.setTypeface(null, Typeface.BOLD);
                holder.num.setTypeface(ApplicationContexts.typeface1);
                holder.today.setVisibility(View.GONE);
                holder.selectDay.setVisibility(View.GONE);
                holder.event.setVisibility(View.GONE);
                holder.num.setVisibility(View.VISIBLE);
                holder.line.setVisibility(View.GONE);
                utils.setFont(holder.num);
            }
            if (position >= 7 && position < 14) {
                holder.today.setVisibility(View.GONE);
                holder.selectDay.setVisibility(View.GONE);
                holder.event.setVisibility(View.GONE);
                holder.num.setVisibility(View.GONE);
                holder.line.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return days.size() + days.get(0).getDayOfWeek() + 14;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        } else {
            return TYPE_DAY;
        }
    }

    private boolean isPositionHeader(int position) {
        return position < 14;
    }
}