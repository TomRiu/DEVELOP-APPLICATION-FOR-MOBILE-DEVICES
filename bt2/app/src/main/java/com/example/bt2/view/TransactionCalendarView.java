package com.example.bt2.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.bt2.R;
import com.example.bt2.model.DailyStat;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TransactionCalendarView extends View {
    private static final int DAYS_IN_WEEK = 7;
    private static final int MAX_WEEKS_IN_MONTH = 6;

    private Paint textPaint;
    private Paint incomePaint;
    private Paint outcomePaint;
    private Paint todayPaint;
    private Paint selectedPaint;

    private List<DailyStat> monthStats;
    private Calendar displayedMonth;
    private Date selectedDate;

    private float cellWidth;
    private float cellHeight;
    private CalendarViewPager.OnDateSelectedListener onDateSelectedListener;

    public TransactionCalendarView(Context context) {
        super(context);
        init(null);
    }

    public TransactionCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.calendar_text_size));
        textPaint.setTextAlign(Paint.Align.CENTER);

        incomePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        incomePaint.setColor(Color.GREEN);

        outcomePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outcomePaint.setColor(Color.RED);

        todayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        todayPaint.setColor(Color.LTGRAY);

        selectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectedPaint.setColor(Color.BLUE);
        selectedPaint.setStyle(Paint.Style.STROKE);
        selectedPaint.setStrokeWidth(4);

        displayedMonth = Calendar.getInstance();
        selectedDate = new Date();
    }

    public void setMonthStats(List<DailyStat> monthStats) {
        this.monthStats = monthStats;
        invalidate();
    }

    public void setDisplayedMonth(Calendar month) {
        this.displayedMonth = month;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int desiredHeight = (int) (width * 1.2); // Aspect ratio of 5:6
        int finalHeight = Math.min(height, desiredHeight);

        setMeasuredDimension(width, finalHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        cellWidth = w / (float) DAYS_IN_WEEK;
        cellHeight = h / (float) MAX_WEEKS_IN_MONTH;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawCalendarGrid(canvas);
        drawDayNumbers(canvas);
        drawTransactionBars(canvas);
        drawTodayIndicator(canvas);
        drawSelectedDateIndicator(canvas);
    }

    private void drawCalendarGrid(Canvas canvas) {
        for (int i = 1; i < DAYS_IN_WEEK; i++) {
            float x = i * cellWidth;
            canvas.drawLine(x, 0, x, getHeight(), textPaint);
        }

        for (int i = 1; i < MAX_WEEKS_IN_MONTH; i++) {
            float y = i * cellHeight;
            canvas.drawLine(0, y, getWidth(), y, textPaint);
        }
    }

    private void drawDayNumbers(Canvas canvas) {
        Calendar cal = (Calendar) displayedMonth.clone();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int day = 1; day <= daysInMonth; day++) {
            int row = (day + firstDayOfWeek - 1) / DAYS_IN_WEEK;
            int col = (day + firstDayOfWeek - 1) % DAYS_IN_WEEK;

            float x = col * cellWidth + cellWidth / 2;
            float y = row * cellHeight + cellHeight / 2 - (textPaint.descent() + textPaint.ascent()) / 2;

            canvas.drawText(String.valueOf(day), x, y, textPaint);
        }
    }

    private void drawTransactionBars(Canvas canvas) {
        if (monthStats == null) return;

        for (DailyStat stat : monthStats) {
            Rect dayRect = getDayRect(stat.getDay());
            if (dayRect != null) {
                float maxBarHeight = dayRect.height() / 3f;
                float maxAmount = (float) Math.max(stat.getIncome(), stat.getOutcome());

                // Vẽ thanh thu nhập
                if (stat.getIncome() > 0) {
                    float barHeight = (float) (stat.getIncome() / maxAmount * maxBarHeight);
                    // Đổi màu chữ cho tổng số tiền thu nhập
                    textPaint.setColor(Color.GREEN); // Màu chữ cho thu nhập
                    canvas.drawText(formatNumber(stat.getIncome()), dayRect.left + cellWidth / 2, dayRect.bottom - 2, textPaint);
                }

                // Vẽ thanh chi tiêu
                if (stat.getOutcome() > 0) {
                    float barHeight = (float) (stat.getOutcome() / maxAmount * maxBarHeight);
                    // Đổi màu chữ cho tổng số tiền chi tiêu
                    textPaint.setColor(Color.RED); // Màu chữ cho chi tiêu
                    canvas.drawText(formatNumber(stat.getOutcome()), dayRect.left + cellWidth / 2, dayRect.top + barHeight + 3, textPaint);
                }
            }
        }
    }

    private void drawTodayIndicator(Canvas canvas) {
        Calendar today = Calendar.getInstance();
        if (today.get(Calendar.YEAR) == displayedMonth.get(Calendar.YEAR) &&
                today.get(Calendar.MONTH) == displayedMonth.get(Calendar.MONTH)) {
            Rect todayRect = getDayRect(today.getTime());
            if (todayRect != null) {
                canvas.drawRect(todayRect, todayPaint);
            }
        }
    }

    private void drawSelectedDateIndicator(Canvas canvas) {
        Rect selectedRect = getDayRect(selectedDate);
        if (selectedRect != null) {
            canvas.drawRect(selectedRect, selectedPaint);
        }
    }

    private Rect getDayRect(Date day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(day);

        if (cal.get(Calendar.YEAR) != displayedMonth.get(Calendar.YEAR) ||
                cal.get(Calendar.MONTH) != displayedMonth.get(Calendar.MONTH)) {
            return null;
        }

        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        Calendar monthCal = (Calendar) displayedMonth.clone();
        monthCal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeek = monthCal.get(Calendar.DAY_OF_WEEK) - 1;

        int row = (dayOfMonth + firstDayOfWeek - 1) / DAYS_IN_WEEK;
        int col = (dayOfMonth + firstDayOfWeek - 1) % DAYS_IN_WEEK;

        int left = (int) (col * cellWidth);
        int top = (int) (row * cellHeight);
        int right = (int) ((col + 1) * cellWidth);
        int bottom = (int) ((row + 1) * cellHeight);

        return new Rect(left, top, right, bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            float x = event.getX();
            float y = event.getY();

            int col = (int) (x / cellWidth);
            int row = (int) (y / cellHeight);

            Calendar cal = (Calendar) displayedMonth.clone();
            cal.set(Calendar.DAY_OF_MONTH, 1);
            int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
            int dayOfMonth = row * DAYS_IN_WEEK + col - firstDayOfWeek + 1;

            if (dayOfMonth > 0 && dayOfMonth <= cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                selectedDate = cal.getTime();
                invalidate();

                if (onDateSelectedListener != null) {
                    onDateSelectedListener.onDateSelected(selectedDate);
                }
            }
        }
        return true;
    }

    public void setOnDateSelectedListener(CalendarViewPager.OnDateSelectedListener listener) {
        this.onDateSelectedListener = listener;
    }
    public static String formatNumber(double number) {
        if (number < 1000) {
            return String.valueOf(number);
        } else {
            return String.format("%.1fk", number / 1000);
        }
    }

}



