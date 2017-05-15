package com.android.charl.skol.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.charl.skol.R;
import com.android.charl.skol.activities.AddCourseActivity;
import com.android.charl.skol.java.Color;
import com.android.charl.skol.views.NotificationDialog;

import java.util.ArrayList;

/**
 * Created by charl on 25/05/2016.
 */
public class NotificationDialogAdapter extends RecyclerView.Adapter<NotificationDialogAdapter.ViewHolder> {

    private static final String TAG = "CourseAdapter";

    private ArrayList<String> mValues = new ArrayList<>();

    private int currentItem = 0;
    private Context context;
    private NotificationDialogListener listener;
    public void setListener(NotificationDialogListener listener) {
        this.listener = listener;
    }

    public NotificationDialogAdapter(NotificationDialogListener listener, Context context) {
        this.listener = listener;
        this.context = context;

        mValues.add("Pas de notification");
        mValues.add("10 minutes avant");
        mValues.add("30 minutes avant");
        mValues.add("1 heure avant");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification_dialog, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mColorName.setText(mValues.get(position));
        holder.mItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position) {
                    case 0 :
                        listener.callback(0);
                        break;
                    case 1 :
                        listener.callback(10);
                        break;
                    case 2 :
                        listener.callback(30);
                        break;
                    case 3 :
                        listener.callback(60);
                        break;
                    default:
                        listener.callback(0);
                        break;
                }
                notifyItemChanged(currentItem);
                currentItem = position;
                notifyItemChanged(position);
            }
        });
        if(position == currentItem) {
            holder.mColorName.setTextColor(((AddCourseActivity)context).getColor(R.color.textSelection));
            holder.mIcCheck.setVisibility(View.VISIBLE);
        } else {
            holder.mColorName.setTextColor(((AddCourseActivity)context).getColor(android.R.color.black));
            holder.mIcCheck.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mColorName;
        ImageView mIcCheck;
        LinearLayout mItem;

        ViewHolder(View view) {
            super(view);
            mColorName = (TextView) view.findViewById(R.id.notification);
            mIcCheck = (ImageView) view.findViewById(R.id.ic_check);
            mItem = (LinearLayout) view.findViewById(R.id.layout);
        }
    }

    public interface NotificationDialogListener {
        void callback(int time);
    }
}