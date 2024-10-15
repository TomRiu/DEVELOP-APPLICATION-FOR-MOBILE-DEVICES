package com.example.bt2.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.bt2.dao.DBHelper;
import com.example.bt2.dao.DailyStatDAO;
import com.example.bt2.model.DailyStat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarViewPager extends ViewPager {
    private static final int MONTHS_TO_SHOW = 12 * 10; // 10 years

    private List<TransactionCalendarView> calendarViews;
    private DailyStatDAO dailyStatDAO;
    private OnDateSelectedListener onDateSelectedListener;
    private OnMonthChangedListener onMonthChangedListener;

    public CalendarViewPager(Context context) {
        super(context);
        init(context);
    }

    public CalendarViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        calendarViews = new ArrayList<>();
        dailyStatDAO = new DailyStatDAO(new DBHelper(context).getReadableDatabase());

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -MONTHS_TO_SHOW / 2);

        for (int i = 0; i < MONTHS_TO_SHOW; i++) {
            TransactionCalendarView calendarView = new TransactionCalendarView(context);
            calendarView.setDisplayedMonth((Calendar) cal.clone());
            calendarViews.add(calendarView);
            cal.add(Calendar.MONTH, 1);
        }

        setAdapter(new CalendarPagerAdapter());
        setCurrentItem(MONTHS_TO_SHOW / 2);

        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                updateMonthYear(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    public void setOnDateSelectedListener(OnDateSelectedListener listener) {
        this.onDateSelectedListener = listener;
        for (TransactionCalendarView calendarView : calendarViews) {
            calendarView.setOnDateSelectedListener(listener);
        }
    }

    public void setOnMonthChangedListener(OnMonthChangedListener listener) {
        this.onMonthChangedListener = listener;
    }

    private class CalendarPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return MONTHS_TO_SHOW;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            TransactionCalendarView calendarView = calendarViews.get(position);
            container.addView(calendarView);

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, position - MONTHS_TO_SHOW / 2);
            List<DailyStat> monthStats = dailyStatDAO.getMonth(cal.getTime());
            calendarView.setMonthStats(monthStats);

            return calendarView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
        updateMonthYear(item);
    }

    private void updateMonthYear(int position) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, position - MONTHS_TO_SHOW / 2);
        if (onMonthChangedListener != null) {
            onMonthChangedListener.onMonthChanged(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
        }
    }

    public interface OnDateSelectedListener {
        void onDateSelected(Date date);
    }

    public interface OnMonthChangedListener {
        void onMonthChanged(int year, int month);
    }
}
