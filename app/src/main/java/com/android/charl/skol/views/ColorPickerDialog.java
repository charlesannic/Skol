package com.android.charl.skol.views;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.android.charl.skol.R;
import com.android.charl.skol.adapter.ColorPickerAdapter;

/**
 * Created by charl on 07/11/2016.
 */

public class ColorPickerDialog extends Dialog {

    public ColorPickerDialog(final Context context, ColorPickerAdapter.ColorPickerDialogListener listener) {
        super(context);

        this.setContentView(R.layout.dialog_color_picker);

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        //getWindow().

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new ColorPickerAdapter(listener, context));
    }

    public static DisplayMetrics getDeviceMetrics(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(metrics);
        return metrics;
    }

}