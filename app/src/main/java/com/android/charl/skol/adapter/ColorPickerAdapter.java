package com.android.charl.skol.adapter;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.charl.skol.R;
import com.android.charl.skol.activities.AddCourseActivity;
import com.android.charl.skol.activities.MainActivity;
import com.android.charl.skol.java.Color;

import java.util.ArrayList;

/**
 * Created by charl on 25/05/2016.
 */
public class ColorPickerAdapter extends RecyclerView.Adapter<ColorPickerAdapter.ViewHolder> {

    private static final String TAG = "CourseAdapter";

    private ArrayList<Color> mValues = new ArrayList<>();
    private ArrayList<Integer> mDrawables = new ArrayList<>();

    private int currentColor = 8;
    private Context context;
    private ColorPickerDialogListener listener;
    public void setListener(ColorPickerDialogListener listener) {
        this.listener = listener;
    }

    public ColorPickerAdapter(ColorPickerDialogListener listener, Context context) {
        this.listener = listener;
        this.context = context;

        int[] colors = context.getResources().getIntArray(R.array.colors);
        String[] colorsNames = context.getResources().getStringArray(R.array.colors_names);
        TypedArray imgs = context.getResources().obtainTypedArray(R.array.colors_drawable);

        for(int i=0 ; i < colors.length ; i++){
            mValues.add(new Color(i, colorsNames[i], colors[i]));
            mDrawables.add(imgs.getResourceId(i, -1));
        }
        imgs.recycle();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_color_picker, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mColorIndicator.setBackgroundResource(mDrawables.get(position));
        holder.mColorSelection.setBackgroundResource(R.drawable.circle_0_selected);
        holder.mColorName.setText(mValues.get(position).getColorName());
        holder.mItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.callback(mValues.get(position), position > 4 && position < 10, mDrawables.get(position));
                notifyItemChanged(currentColor);
                currentColor = position;
                notifyItemChanged(position);
            }
        });
        if(position == currentColor) {
            holder.mColorSelection.setVisibility(View.INVISIBLE);
            holder.mColorName.setTextColor(((AddCourseActivity)context).getColor(R.color.textSelection));
            holder.mIcCheck.setVisibility(View.VISIBLE);
        } else {
            holder.mColorSelection.setVisibility(View.VISIBLE);
            holder.mColorName.setTextColor(((AddCourseActivity)context).getColor(android.R.color.black));
            holder.mIcCheck.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mColorIndicator;
        TextView mColorSelection;
        TextView mColorName;
        ImageView mIcCheck;
        LinearLayout mItem;

        ViewHolder(View view) {
            super(view);
            mColorIndicator = (TextView) view.findViewById(R.id.color_indicator);
            mColorSelection = (TextView) view.findViewById(R.id.ic_selection);
            mColorName = (TextView) view.findViewById(R.id.color_name);
            mIcCheck = (ImageView) view.findViewById(R.id.ic_check);
            mItem = (LinearLayout) view.findViewById(R.id.layout);
        }
    }

    public interface ColorPickerDialogListener {
        void callback(Color color, boolean mainAccentColor, int colorIndicator);
    }
}