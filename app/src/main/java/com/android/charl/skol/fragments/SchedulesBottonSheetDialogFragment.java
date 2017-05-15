package com.android.charl.skol.fragments;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.charl.skol.R;
import com.android.charl.skol.activities.AddCourseActivity;
import com.android.charl.skol.adapter.SchedulesAdapter;
import com.android.charl.skol.java.Schedule;
import com.android.charl.skol.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by charl on 08/11/2016.
 */

public class SchedulesBottonSheetDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private static final String TAG = "SchedulesBSDialogFrag";

    private static float mEdittextSize;
    private int mColorAccent;
    private static Context mContext;
    private int mStatusBarHeight;

    private View mBottomSheet;
    private LinearLayout mContent;
    private EditText mLocation;
    private Button mAddSchedule;
    private RecyclerView mSchedulesList;
    private TextView mStartHour, mEndHour;
    private ArrayList<TextView> mDaysOfTheWeek = new ArrayList<>();
    private ArrayList<Drawable> mDaysBackground = new ArrayList<>();
    private ArrayList<Boolean> mIsDayCheck = new ArrayList<>();

    private TimePickerDialog mStartHourDialogPicker;
    private TimePickerDialog mEndHourDialogPicker;
    private int mHoursGap = 1, mMinutesGap = 0;

    boolean isRunning = false;
    ArrayList<Schedule> arrayList;
    SchedulesAdapter adapter;

    public static SchedulesBottonSheetDialogFragment newInstance(Context context, String string, float edittextSize) {
        SchedulesBottonSheetDialogFragment f = new SchedulesBottonSheetDialogFragment();
        Bundle args = new Bundle();
        args.putString("string", string);
        f.setArguments(args);

        mContext = context;
        mEdittextSize = edittextSize;

        return f;
    }

    public int getYLocation() {
        int[] out = new int[2];
        //Log.i("COUCOU", "test: " + + " " + mContent.getY());
        if (mContent != null) {
            mContent.getLocationOnScreen(out);
            return out[1];
        } else
            return 0;
    }

    public void setStatusBarSpaceHeight(int height) {
        // Gets the layout params that will allow you to resize the layout
        //ViewGroup.LayoutParams params = mStatusBar.getLayoutParams();
        // Changes the height and width to the specified *pixels*

        mContent.setPadding(mContent.getPaddingLeft(), height, mContent.getPaddingRight(), mContent.getPaddingBottom());
        //params.height = 0;
        /*params.width = mStatusBar.getWidth();
        mStatusBar.setLayoutParams(params);
        Log.i("COUCOUCOUCOU", "setStatusBarSpaceHeight: " + height);*/
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            //setStateText(newState);
            if (mBottomSheet == null)
                mBottomSheet = bottomSheet;
            Log.i(TAG, "onStateChanged: COUCOU");
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            Resources r = getResources();
            //Log.i("COUCOU", "onSlide: " + dp + " " + getYLocation());
            int space = getYLocation();
            //Log.i("COUCOU", "onSlide: " + space + " " + dp + " " + (space - dp));
            //if (space < dp) {
            DisplayMetrics metrics = new DisplayMetrics();
            ((AddCourseActivity) mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);

            int height = metrics.heightPixels;

            if (slideOffset > 0 && (bottomSheet.getHeight() + mStatusBarHeight) >= metrics.heightPixels)
                setStatusBarSpaceHeight(Math.round(mStatusBarHeight * slideOffset));
            else
                setStatusBarSpaceHeight(0);
            //}


            //Log.i(TAG, "onSlide: " + slideOffset);
        }
    };


    BottomSheetBehavior mBottomSheetBehavior;

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        mBottomSheet = View.inflate(getContext(), R.layout.fragment_schedules_bottom_sheet_dialog, null);
        dialog.setContentView(mBottomSheet);

        Resources r = mContext.getResources();
        mStatusBarHeight = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, r.getDisplayMetrics()));

        mBottomSheetBehavior = BottomSheetBehavior.from(((View) mBottomSheet.getParent()));
        if (mBottomSheetBehavior != null) {
            mBottomSheetBehavior.setBottomSheetCallback(mBottomSheetBehaviorCallback);
            //mBottomSheetBehavior.setPeekHeight(peekHeight);
            mBottomSheet.requestLayout();
        }

        mContent = (LinearLayout) mBottomSheet.findViewById(R.id.bottom_sheet);
        mContent.setFocusable(true);
        mContent.setFocusableInTouchMode(true);
        mContent.requestFocus();

        mAddSchedule = (Button) mBottomSheet.findViewById(R.id.add_schedule);
        Log.i(TAG, "setupDialog: " + mColorAccent);
        mAddSchedule.setTextColor(mColorAccent);
        mAddSchedule.setOnClickListener(this);

        mLocation = (EditText) mBottomSheet.findViewById(R.id.location);
        mLocation.setTextSize(TypedValue.COMPLEX_UNIT_PX, mEdittextSize);
        mLocation.setFocusableInTouchMode(true);
        mLocation.setFocusable(true);

        mSchedulesList = (RecyclerView) mBottomSheet.findViewById(R.id.recycler_view);
        mSchedulesList.setLayoutManager(new LinearLayoutManager(mContext));
        mSchedulesList.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Log.i(TAG, "showStatusBarPadding:1 " + getYLocation() + " " + mStatusBarHeight + " " + mBottomSheet.getHeight());
            }
        });

        mStartHour = (TextView) mBottomSheet.findViewById(R.id.start_hour);
        mEndHour = (TextView) mBottomSheet.findViewById(R.id.end_hour);
        mStartHour.setOnClickListener(this);
        mEndHour.setOnClickListener(this);

        mDaysOfTheWeek = new ArrayList<>();
        mDaysOfTheWeek.add((TextView) mBottomSheet.findViewById(R.id.monday));
        mDaysOfTheWeek.add((TextView) mBottomSheet.findViewById(R.id.tuesday));
        mDaysOfTheWeek.add((TextView) mBottomSheet.findViewById(R.id.wednesday));
        mDaysOfTheWeek.add((TextView) mBottomSheet.findViewById(R.id.thursday));
        mDaysOfTheWeek.add((TextView) mBottomSheet.findViewById(R.id.friday));
        mDaysOfTheWeek.add((TextView) mBottomSheet.findViewById(R.id.saturday));
        mDaysOfTheWeek.add((TextView) mBottomSheet.findViewById(R.id.sunday));
        for (int i = 0; i < mDaysOfTheWeek.size(); i++) {
            Log.i(TAG, "setupDialog: add");
            mDaysBackground.add(mDaysOfTheWeek.get(i).getBackground());
            mDaysOfTheWeek.get(i).setOnClickListener(this);
            mIsDayCheck.add(false);
        }

        arrayList = ((AddCourseActivity) mContext).getSchedules();

        adapter = new SchedulesAdapter(mContext, arrayList, this);

        //if (arrayList.size() > 0) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallbackItemTouchHelper);

        mSchedulesList.setAdapter(adapter);
        DefaultItemAnimator animator = new DefaultItemAnimator() {
            @Override
            public void onAnimationFinished(RecyclerView.ViewHolder viewHolder) {
                if (getYLocation() > 0 && mContent.getPaddingTop() > 0 && !isRunning) {
                    isRunning = true;
                    hideStatusBarPadding();
                }
            }



            @Override
            public void onAddStarting(RecyclerView.ViewHolder item) {
                super.onAddFinished(item);

                if(getYLocation() < mStatusBarHeight && mContent.getPaddingTop() == 0)
                    showStatusBarPadding();
            }
        };
        animator.setRemoveDuration(200);
        mSchedulesList.setItemAnimator(animator);
        itemTouchHelper.attachToRecyclerView(mSchedulesList);
        /*} else {
            //mSchedulesList.setVisibility(View.GONE);
        }*/
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setStyle(STYLE_NORMAL, R.style.SchedulesFragment);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        if (v == mDaysOfTheWeek.get(0)) {
            if (!mIsDayCheck.get(0))
                mDaysOfTheWeek.get(0).setBackground(getResources().getDrawable(R.drawable.circle_selection));
            else {
                mDaysOfTheWeek.get(0).setBackground(mDaysBackground.get(0));
            }
            mIsDayCheck.set(0, !mIsDayCheck.get(0));
        } else if (v == mDaysOfTheWeek.get(1)) {
            if (!mIsDayCheck.get(1))
                mDaysOfTheWeek.get(1).setBackground(getResources().getDrawable(R.drawable.circle_selection));
            else {
                mDaysOfTheWeek.get(1).setBackground(mDaysBackground.get(1));
            }
            mIsDayCheck.set(1, !mIsDayCheck.get(1));
        } else if (v == mDaysOfTheWeek.get(2)) {
            if (!mIsDayCheck.get(2))
                mDaysOfTheWeek.get(2).setBackground(getResources().getDrawable(R.drawable.circle_selection));
            else {
                mDaysOfTheWeek.get(2).setBackground(mDaysBackground.get(2));
            }
            mIsDayCheck.set(2, !mIsDayCheck.get(2));
        } else if (v == mDaysOfTheWeek.get(3)) {
            if (!mIsDayCheck.get(3))
                mDaysOfTheWeek.get(3).setBackground(getResources().getDrawable(R.drawable.circle_selection));
            else {
                mDaysOfTheWeek.get(3).setBackground(mDaysBackground.get(3));
            }
            mIsDayCheck.set(3, !mIsDayCheck.get(3));
        } else if (v == mDaysOfTheWeek.get(4)) {
            if (!mIsDayCheck.get(4))
                mDaysOfTheWeek.get(4).setBackground(getResources().getDrawable(R.drawable.circle_selection));
            else {
                mDaysOfTheWeek.get(4).setBackground(mDaysBackground.get(4));
            }
            mIsDayCheck.set(4, !mIsDayCheck.get(4));
        } else if (v == mDaysOfTheWeek.get(5)) {
            if (!mIsDayCheck.get(5))
                mDaysOfTheWeek.get(5).setBackground(getResources().getDrawable(R.drawable.circle_selection));
            else {
                mDaysOfTheWeek.get(5).setBackground(mDaysBackground.get(5));
            }
            mIsDayCheck.set(5, !mIsDayCheck.get(5));
        } else if (v == mDaysOfTheWeek.get(6)) {
            if (!mIsDayCheck.get(6))
                mDaysOfTheWeek.get(6).setBackground(getResources().getDrawable(R.drawable.circle_selection));
            else {
                mDaysOfTheWeek.get(6).setBackground(mDaysBackground.get(6));
            }
            mIsDayCheck.set(6, !mIsDayCheck.get(6));
        }

        if (v == mStartHour) {
            int heures = getHours(mStartHour.getText().toString());
            int minutes = getMinutes(mStartHour.getText().toString());
            mStartHourDialogPicker = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String heures = String.valueOf(hourOfDay);
                    String minutes = String.valueOf(minute);

                    mStartHour.setText(formatHours(heures, minutes));

                    checkHours(true);
                }
            }, heures, minutes, true);
            mStartHourDialogPicker.show();
        } else if (v == mEndHour) {
            int hours = getHours(mEndHour.getText().toString());
            int minutes = getMinutes(mEndHour.getText().toString());
            mEndHourDialogPicker = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String hours = String.valueOf(hourOfDay);
                    String minutes = String.valueOf(minute);

                    mEndHour.setText(formatHours(hours, minutes));

                    checkHours(false);
                }
            }, hours, minutes, true);
            mEndHourDialogPicker.show();
        } else if (v == mAddSchedule) {
            Log.i(TAG, "showStatusBarPadding:1 " + getYLocation() + " " + mStatusBarHeight + " " + mBottomSheet.getHeight());

            //((AddCourseActivity)mContext).setSchedulesText(arrayList.size());
            mContent.setPadding(mContent.getPaddingLeft(),
                    mContent.getPaddingTop(),
                    mContent.getPaddingRight(),
                    0);
            ArrayList<Schedule> newItems = new ArrayList<>();
            for (int i = 0; i < mIsDayCheck.size(); i++) {
                if (mIsDayCheck.get(i))
                    newItems.add(new Schedule(i, mStartHour.getText().toString(), mEndHour.getText().toString(), mLocation.getText().toString()));
            }
            arrayList.addAll(newItems);
            Collections.sort(arrayList, new ScheduleComparator());

            for(int i = 0 ; i < newItems.size() ; i++) {
                int index = arrayList.indexOf(newItems.get(i));
                adapter.notifyItemInserted(index);
            }

            /*if (oldSize > 0)
                adapter.notifyItemChanged(oldSize - 1);
            else
                adapter.notifyItemRangeInserted(oldSize, arrayList.size());*/
            //adapter.notifyDataSetChanged();
            //adapter.notifyItemInserted(0);

            //adapter.notifyItemRangeChanged(0,arrayList.size()-1);

        }
    }

    public void checkHours(boolean startChange) {
        Float startHour = stringToFloat(mStartHour.getText().toString());
        Float endHour = stringToFloat(mEndHour.getText().toString());
        mStartHour.setTextColor(Color.BLACK);
        mAddSchedule.setEnabled(true);
        mAddSchedule.setAlpha(1);
        if (startChange) {
            if (getMinutes(mStartHour.getText().toString()) + mMinutesGap >= 60) {
                int hours = getHours(mStartHour.getText().toString()) + mHoursGap + 1,
                        minutes = getMinutes(mStartHour.getText().toString()) + mMinutesGap - 60;
                mEndHour.setText(formatHours(String.valueOf(hours), String.valueOf(minutes)));
            } else {
                int hours = getHours(mStartHour.getText().toString()) + mHoursGap,
                        minutes = getMinutes(mStartHour.getText().toString()) + mMinutesGap;
                mEndHour.setText(formatHours(String.valueOf(hours), String.valueOf(minutes)));
            }
            if (getHours(mEndHour.getText().toString()) >= 24) {
                mEndHour.setText("23:59");
            }
        } else if (startHour < endHour) {
            if (getMinutes(mEndHour.getText().toString()) < getMinutes(mStartHour.getText().toString())) {
                mHoursGap = getHours(mEndHour.getText().toString()) - getHours(mStartHour.getText().toString()) - 1;
                mMinutesGap = 60 - (getMinutes(mStartHour.getText().toString()) - getMinutes(mEndHour.getText().toString()));
            } else {
                mHoursGap = getHours(mEndHour.getText().toString()) - getHours(mStartHour.getText().toString());
                mMinutesGap = getMinutes(mEndHour.getText().toString()) - getMinutes(mStartHour.getText().toString());
            }
        } else {
            mAddSchedule.setEnabled(false);
            mAddSchedule.setAlpha(.5f);
            mStartHour.setTextColor(((AddCourseActivity) mContext).getColor(R.color.error));
        }
    }

    public int getHours(String s) {
        String hours = String.valueOf(s.charAt(0));
        hours += s.charAt(1);
        return (Integer.parseInt(hours));
    }

    public int getMinutes(String s) {
        String minutes = String.valueOf(s.charAt(3));
        minutes += s.charAt(4);
        return (Integer.parseInt(minutes));
    }

    public String formatHours(String hours, String minutes) {
        if (hours.length() < 2)
            hours = "0" + hours;
        if (minutes.length() < 2)
            minutes = "0" + minutes;
        return (hours + ":" + minutes);
    }

    public Float stringToFloat(String s) {
        String before = String.valueOf(s.charAt(0));
        before += s.charAt(1);
        String after = String.valueOf(s.charAt(3));
        after += s.charAt(4);

        return (Float.parseFloat(before + "." + after));
    }


    ItemTouchHelper.SimpleCallback simpleCallbackItemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            Toast.makeText(mContext, position + " " + arrayList.size(), Toast.LENGTH_SHORT).show();
            arrayList.remove(position);
            adapter.notifyItemRemoved(position);
            if (arrayList.size() <= 0)
                adapter.notifyDataSetChanged();

            final ValueAnimator valueAnimator = ValueAnimator.ofInt(viewHolder.itemView.getHeight(), 0);
            valueAnimator.setDuration(200);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mSchedulesList.setPadding(mSchedulesList.getPaddingLeft(),
                            mSchedulesList.getPaddingTop(),
                            mSchedulesList.getPaddingRight(),
                            (int) valueAnimator.getAnimatedValue());
                }
            });

            valueAnimator.start();
        }
    };

    private void hideStatusBarPadding() {
        final ValueAnimator valueAnimator = ValueAnimator.ofInt(mContent.getPaddingTop(), 0);
        valueAnimator.setDuration(200);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mContent.setPadding(mContent.getPaddingLeft(), (int) valueAnimator.getAnimatedValue(), mContent.getPaddingRight(), mContent.getPaddingBottom());
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isRunning = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        valueAnimator.start();
    }

    public void showStatusBarPadding() {
        final ValueAnimator valueAnimator = ValueAnimator.ofInt(0, mStatusBarHeight);
        valueAnimator.setDuration(200);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mContent.setPadding(mContent.getPaddingLeft(), (int) valueAnimator.getAnimatedValue(), mContent.getPaddingRight(), mContent.getPaddingBottom());
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isRunning = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        valueAnimator.start();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Log.i(TAG, "onConfigurationChanged: test");
        mAddSchedule.setTextColor(mColorAccent);
    }

    public class ScheduleComparator implements Comparator<Schedule> {
        @Override
        public int compare(Schedule o1, Schedule o2) {
            if (o1.getDay() < o2.getDay())
                return -1;
            else if (o1.getDay() > o2.getDay())
                return 1;
            else {
                float st1 = Float.parseFloat(String.valueOf(o1.getStartTime().charAt(0))
                        + String.valueOf(o1.getStartTime().charAt(1))
                        + "."
                        + String.valueOf(o1.getStartTime().charAt(3))
                        + String.valueOf(o1.getStartTime().charAt(4)));
                float st2 = Float.parseFloat(String.valueOf(o2.getStartTime().charAt(0))
                        + String.valueOf(o2.getStartTime().charAt(1))
                        + "."
                        + String.valueOf(o2.getStartTime().charAt(3))
                        + String.valueOf(o2.getStartTime().charAt(4)));
                if (st1 < st2)
                    return -1;
                else if (st1 > st2)
                    return 1;
                else {
                    float et1 = Float.parseFloat(String.valueOf(o1.getEndTime().charAt(0))
                            + String.valueOf(o1.getEndTime().charAt(1))
                            + "."
                            + String.valueOf(o1.getEndTime().charAt(3))
                            + String.valueOf(o1.getEndTime().charAt(4)));
                    float et2 = Float.parseFloat(String.valueOf(o2.getEndTime().charAt(0))
                            + String.valueOf(o2.getEndTime().charAt(1))
                            + "."
                            + String.valueOf(o2.getEndTime().charAt(3))
                            + String.valueOf(o2.getEndTime().charAt(4)));
                    if (et1 < et2)
                        return -1;
                    else
                        return 1;
                }
            }
            //return o1.getDay().compareTo(o2.getDay());
        }
    }

    public void setmColorAccent(int mColorAccent) {
        this.mColorAccent = mColorAccent;
    }

    public ArrayList<Schedule> getArrayList() {
        return arrayList;
    }
}