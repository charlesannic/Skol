package com.android.charl.skol.adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.v4.widget.Space;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.charl.skol.R;
import com.android.charl.skol.activities.AddCourseActivity;
import com.android.charl.skol.fragments.SchedulesBottonSheetDialogFragment;
import com.android.charl.skol.java.Schedule;

import java.util.ArrayList;

/**
 * Created by charl on 09/11/2016.
 */

public class SchedulesAdapter extends RecyclerView.Adapter<SchedulesAdapter.ViewHolder> {

    private ArrayList<Schedule> itemsData;
    private Context mContext;
    private SchedulesBottonSheetDialogFragment mFragment;
    private int lastPosition = -1;
    private LayoutInflater layoutInflater;

    public SchedulesAdapter(Context context, ArrayList<Schedule> itemsData, SchedulesBottonSheetDialogFragment fragment) {
        mContext = context;
        mFragment = fragment;
        this.itemsData = itemsData;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_schedule, parent, false);
        ViewHolder viewHolder = new ViewHolder(/*mContext, */v, itemsData);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        String s;
        switch (itemsData.get(position).getDay()) {
            case 0:
                s = "Le lundi";
                break;
            case 1:
                s = "Le mardi";
                break;
            case 2:
                s = "Le mercredi";
                break;
            case 3:
                s = "Le jeudi";
                break;
            case 4:
                s = "Le vendredi";
                break;
            case 5:
                s = "Le samedi";
                break;
            default:
                s = "Le dimanche";
                break;
        }

        s += " de " + itemsData.get(position).getStartTime() + " à " + itemsData.get(position).getEndTime();
        s += " - " + itemsData.get(position).getLocation();

        viewHolder.label.setText(s);
    }

    @Override
    public int getItemCount() {
        return itemsData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ArrayList<Schedule> itemsData;
        LinearLayout item;
        TextView label;

        public ViewHolder(/*final Context context, */View itemLayoutView, final ArrayList<Schedule> itemsData) {
            super(itemLayoutView);

            this.itemsData = itemsData;

            item = (LinearLayout) itemLayoutView.findViewById(R.id.item);
            label = (TextView) itemLayoutView.findViewById(R.id.text);
        }
    }
}
