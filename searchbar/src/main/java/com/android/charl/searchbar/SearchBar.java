package com.android.charl.searchbar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by charl on 01/07/2016.
 */
public class SearchBar extends RelativeLayout {
    
    private static final String TAG = "SearchBar";

    private Context mContext;

    private View mPendingUserContentView;

    private int mPrimaryColor;
    private int mInActiveColor;
    private int mDarkBackgroundColor;
    private int mWhiteColor;

    private float twoDp;
    private boolean isCardElevated = false;

    public SearchBar(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.search_bar, this);
    }

    public SearchBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs);
    }

    public SearchBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews(context, attrs);
    }

    private void initViews(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.search_bar, this);

        final CardView cardView = (CardView) findViewById(R.id.search_card_view);
        twoDp = cardView.getCardElevation();
    }

    public void setSmoothElevation(boolean isRecyclerViewScrolled) {
        final CardView cardView = (CardView) findViewById(R.id.search_card_view);
        if(isRecyclerViewScrolled && !isCardElevated) {
            AnimateElevation animateElevation = new AnimateElevation ();
            animateElevation.setParams(cardView, twoDp, twoDp / 2f * 3f, 200);
            animateElevation.execute("");
            isCardElevated = true;
        } else if(!isRecyclerViewScrolled && isCardElevated) {
            AnimateElevation animateElevation = new AnimateElevation ();
            animateElevation.setParams(cardView, twoDp / 2f * 3f, twoDp, 200);
            animateElevation.execute("");
            isCardElevated = false;
        }
    }

    public void setCardElevation(float elevation) {
        final CardView cardView = (CardView) findViewById(R.id.search_card_view);

        cardView.setElevation(elevation);
    }

    private class AnimateElevation extends AsyncTask<String, Integer, Long>
    {
        CardView view;
        float startElevation, endElevation, currentElevation, elevationDelta;
        long duration;

        public void setParams(CardView v, float startElevation, float endElevation, long duration)
        {
            this.view = v;
            this.startElevation = startElevation;
            this.endElevation = endElevation;
            this.currentElevation = startElevation;
            this.elevationDelta = endElevation - startElevation;
            this.duration = duration;
        }


        protected Long doInBackground(String... params)
        {
            long startTime = System.currentTimeMillis();
            long endTime = startTime + duration;
            long now = startTime;
            float percentage = 0;
            while (now <= endTime)
            {
                now = System.currentTimeMillis();
                percentage = (100f / duration * (now - startTime)) / 100;
                currentElevation = startElevation + (elevationDelta * percentage);
                publishProgress(0);
                try { Thread.sleep(50); }catch(Exception e) {};
            }
            currentElevation = endElevation;
            publishProgress(0);
            return new Long (0);
        }

        protected void onProgressUpdate(Integer... progress)
        {
            view.setCardElevation(currentElevation);
        }

        protected void onPostExecute(Long result)
        {
        }
    }

}
