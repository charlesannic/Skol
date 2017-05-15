package com.android.charl.skol.adapter;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.charl.skol.R;
import com.android.charl.skol.io.DatabaseHelper;
import com.android.charl.skol.java.Course;
import com.android.charl.skol.java.Schedule;

import java.util.ArrayList;

import static android.text.TextUtils.isEmpty;
import static android.view.View.GONE;

/**
 * Created by charl on 25/05/2016.
 */
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    private static final String TAG = "CourseAdapter";

    private Context mContext;
    private CourseAdapterListener listener;
    private ArrayList<Schedule> mValues;
    private ArrayList<Integer> heights = new ArrayList<>();
    private String[] mDaysOfWeek;
    //private ArrayList<View> mHeaders;
    //private Integer[] array= new Integer[]{0,0,0,0,0,0,0};

    public CourseAdapter(Context context, CourseAdapterListener listener, ArrayList<Schedule> values) {
        mContext = context;
        this.listener = listener;
        mValues = values;
        /*mHeaders = new ArrayList<>();
        for (int i = mValues.size() - 1; i >= 0; i--) {
            //array[mValues.get(i).getDay()] = array[mValues.get(i).getDay()] ++;
            mValues.add(i, null);
        }*/
        //for (int i = 0 ; i < mValues.size()- ; i++)
        mDaysOfWeek = context.getResources().getStringArray(R.array.days_of_week);

        for (int i = 0; i < 7; i++)
            heights.add(0);

        Log.i(TAG, "CourseAdapter: " + mValues.size());
    }

    /*public ArrayList<View> getmHeaders() {
        return mHeaders;
    }*/

    public ArrayList<Schedule> getmValues() {
        return mValues;
    }

    public int update() {
        mValues.clear();
        notifyDataSetChanged();

        return mValues.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Schedule mItem = this.mValues.get(position);

        heights.set(this.mValues.get(position).getDay(), heights.get(this.mValues.get(position).getDay()) + 1);
        //Log.i(TAG, "onBindViewHolder: " + position + " " + (getItemCount()-1));
        if (position == getItemCount() - 1) {
            holder.itemView.post(new Runnable() {
                @Override
                public void run() {

                    int cellHeight = holder.itemView.getHeight();// this will give you cell height dynamically
                    for (int i = 0; i < heights.size(); i++) {
                        heights.set(mValues.get(position).getDay(), heights.get(mValues.get(position).getDay()) * cellHeight);
                    }
                }
            });
        }

        holder.setIsRecyclable(false);

        holder.mItem.setTag(mValues.get(position).getDay());

        String day = String.valueOf(mDaysOfWeek[mValues.get(position).getDay()]);
        if (position > 0
                && mValues.get(position - 1).getDay() != mValues.get(position).getDay())
            holder.mHeader.setText(String.valueOf(day.charAt(0))
                    + String.valueOf(day.charAt(1)));
        else
            holder.mHeader.setVisibility(View.INVISIBLE);

        Course course = DatabaseHelper.CourseHelper.getCourse(mItem.getItsCourse());
        holder.mTitleView.setText(course.getName());
        holder.mDescription.setText(mItem.getStartTime() + " à " + mItem.getEndTime() + (mItem.getLocation().length() == 0 ? "" : " en " + mItem.getLocation()));
        //Log.i(TAG, "onBindViewHolder: " + mItem.getColor().getColorId() + " " + mValues.get(position).getColor().getColorName() + " " + mValues.get(position).getColor().getId());

        holder.mCard.setCardBackgroundColor(course.getColor().getColor());

        holder.mCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //holder.mTitleView.setY(holder.mTitleView.getY() + 20);
            }
        });

        if (position < mValues.size() - 1 && mValues.get(position + 1).getDay() == mItem.getDay()) {
            holder.mNewDay.setVisibility(GONE);
        }
    }

    public void refresh() {
        mValues = DatabaseHelper.ScheduleHelper.getAllSchedules();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //public final View mView;

        LinearLayout mItem;
        TextView mHeader;
        CardView mCard;
        TextView mTitleView;
        TextView mDescription;
        View mNewDay;

        public ViewHolder(View view) {
            super(view);
            //mView = view.findViewById(R.id.view);
            mItem = (LinearLayout) view.findViewById(R.id.item);
            mHeader = (TextView) view.findViewById(R.id.header);
            mCard = (CardView) view.findViewById(R.id.card);
            mTitleView = (TextView) view.findViewById(R.id.title);
            mDescription = (TextView) view.findViewById(R.id.description);
            mNewDay = (View) view.findViewById(R.id.its_a_new_day);
        }
    }

    public interface CourseAdapterListener {
        void callback(ArrayList<Integer> heights);
    }
}