package com.android.charl.skol.views;

import android.view.View;

/**
 * Created by Charles on 13-Jan-17.
 */

public class Header {

    private View view;
    private int minPosition, maxPosition;

    public Header(View view, int minPosition, int maxPosition) {
        this.view = view;
        this.minPosition = minPosition;
        this.maxPosition = maxPosition;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getMinPosition() {
        return minPosition;
    }

    public void setMinPosition(int minPosition) {
        this.minPosition = minPosition;
    }

    public int getMaxPosition() {
        return maxPosition;
    }

    public void setMaxPosition(int maxPosition) {
        this.maxPosition = maxPosition;
    }
}
