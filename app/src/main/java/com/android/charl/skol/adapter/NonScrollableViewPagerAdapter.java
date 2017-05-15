package com.android.charl.skol.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.charl.skol.R;
import com.android.charl.skol.fragments.HomeworkFragment;
import com.android.charl.skol.fragments.MemoFragment;

import java.util.ArrayList;

/**
 * Created by charl on 07/09/2016.
 */
public class NonScrollableViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private Fragment currentFragment;

    public NonScrollableViewPagerAdapter(FragmentManager fm) {
        super(fm);

        fragments.clear();
    }

    public void add(Fragment fragment) {
        fragments.add(fragment);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            currentFragment = ((Fragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }

    /**
     * Get the current fragment
     */
    public Fragment getCurrentFragment() {
        return currentFragment;
    }
}