package com.android.charl.skol.views;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.android.charl.skol.R;
import com.android.charl.skol.adapter.ColorPickerAdapter;
import com.android.charl.skol.adapter.NotificationDialogAdapter;

/**
 * Created by charl on 07/11/2016.
 */

public class NotificationDialog extends Dialog {

    public NotificationDialog(final Context context, NotificationDialogAdapter.NotificationDialogListener listener) {
        super(context);

        this.setContentView(R.layout.dialog_notification);

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        //getWindow().

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new NotificationDialogAdapter(listener, context));
    }

}