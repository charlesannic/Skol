package com.android.charl.skol.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.charl.skol.activities.MainActivity;
import com.android.charl.skol.adapter.CourseAdapter;
import com.android.charl.skol.io.DatabaseHelper;
import com.android.charl.skol.java.Schedule;

import java.util.ArrayList;

public abstract class BottomNavigationFragment extends Fragment implements CourseAdapter.CourseAdapterListener {
    private static final String TAG = "BottomNavigationF";

    private Bitmap image;

    protected boolean mHasBeenDrawn = false;
    protected boolean mHasBeenCroped = false;

    public boolean ismHasBeenCroped() {
        return mHasBeenCroped;
    }

    public void setmHasBeenCroped(boolean mHasBeenCroped) {
        this.mHasBeenCroped = mHasBeenCroped;
    }

    protected RelativeLayout mContainer;
    protected RecyclerView mList;
    protected LinearLayout mEmptyListLayout;
    protected ImageView mImageView;
    protected int mPictureId;
    protected RecyclerView.Adapter mAdapter;

    private Bitmap croppedBmp;

    public abstract void setList();

    public abstract void refresh();

    public abstract void smoothScrollToTop();

    public RecyclerView.Adapter getmAdapter() {
        return mAdapter;
    }

    /*@Override
    public void onHiddenChanged (final boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setList();
        }
    }*/

    public void cropPicture() {
        //if(mHasBeenDrawn && !mHasBeenCroped) {
        /*getView().post(new Runnable() {
                           @Override
                           public void run() {
                               final int imageViewHeight = mImageView.getHeight();

                               new AsyncTask<Void, Void, Void>() {
                                   @Override
                                   protected Void doInBackground(Void... voids) {
                                       /*Bitmap image = BitmapFactory.decodeResource(mContext.getResources(), mPictureId);
                                       double ratioHeader = imageViewWidth / imageViewHeight;
                                       double ratioImage = image.getWidth() / image.getHeight();
                                       double width, height;

                                       if (ratioHeader > ratioImage) {
                                           width = imageViewWidth;
                                           height = ((float) imageViewWidth / (float) image.getWidth()) * (float) image.getHeight();
                                       } else if (ratioHeader < ratioImage) {
                                           width = (imageViewHeight / image.getHeight()) * image.getWidth();
                                           height = imageViewHeight;
                                       } else {
                                           width = imageViewWidth;
                                           height = imageViewHeight;
                                       }*/

                                       /*croppedBmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                                               mPictureId),
                                               (int) Math.round(imageViewHeight),
                                               (int) Math.round(imageViewHeight),
                                               true);

                                       /*Bitmap original = BitmapFactory.decodeResource(mContext.getResources(), mPictureId);
                                       ByteArrayOutputStream out = new ByteArrayOutputStream();
                                       original.compress(Bitmap.CompressFormat.PNG, 100, out);
                                       image = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));*/
        // = BitmapFactory.decodeResource(mContext.getResources(), mPictureId);


                                       /*return null;
                                   }

                                   @Override
                                   protected void onPostExecute(Void aVoid) {
                                       super.onPostExecute(aVoid);
                                       mImageView.setImageBitmap(croppedBmp);
                                   }
                               }.execute();

                               mHasBeenCroped = true;
                           }
                       }
        );*/

        //}
        //mImageView.setVisibility(View.VISIBLE);
    }

    public RecyclerView getmList() {
        return mList;
    }

    public RelativeLayout getmContainer() {
        return mContainer;
    }

    @Override
    public void callback(ArrayList<Integer> heights) {
        Log.i(TAG, "bnSelectedItem: " + heights.size());
    }
}
