package com.android.charl.bottomnavigation;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by charl on 01/07/2016.
 */
public class BottomNavigation extends RelativeLayout implements View.OnClickListener {

    private static final String TAG = "BottomNavigation";

    private final int LOW_WEIGHT = 3;
    private final int HIGH_WEIGHT = 4;

    private BottomNavigationListener listener;

    private boolean isShown = true, initialize = false, mIsLow = false;
    private int selectedItem = 1;
    private int initialSpaceHeight;

    private ArrayList<Integer> colors = new ArrayList<>();

    private RelativeLayout mBackground;
    private View mCircularRevealView;

    private View mItemIdeas;
    private View mItemHomework;
    private View mItemCourses;
    private View mItemGrades;

    private ImageView mIconIdeas;
    private ImageView mIconHomework;
    private ImageView mIconCourses;
    private ImageView mIconGrades;

    private TextView mLabelIdeas;
    private TextView mLabelHomework;
    private TextView mLabelCourses;
    private TextView mLabelGrades;

    public BottomNavigation(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs);
    }

    public void setListener(BottomNavigationListener listener) {
        this.listener = listener;
    }

    private void initViews(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.bottom_navigation_bar, this);

        mBackground = (RelativeLayout) findViewById(R.id.background);
        mCircularRevealView = findViewById(R.id.circular_reveal);

        mItemIdeas = findViewById(R.id.item_ideas);
        mItemHomework = findViewById(R.id.item_homework);
        mItemCourses = findViewById(R.id.item_courses);
        mItemGrades = findViewById(R.id.item_grades);


        mIconIdeas = (ImageView) findViewById(R.id.ic_ideas);
        mIconHomework = (ImageView) findViewById(R.id.ic_homework);
        mIconCourses = (ImageView) findViewById(R.id.ic_courses);
        mIconGrades = (ImageView) findViewById(R.id.ic_grades);

        mLabelIdeas = (TextView) findViewById(R.id.label_ideas);
        mLabelHomework = (TextView) findViewById(R.id.label_homework);
        mLabelCourses = (TextView) findViewById(R.id.label_courses);
        mLabelGrades = (TextView) findViewById(R.id.label_grades);

        mItemIdeas.setOnClickListener(this);
        mItemHomework.setOnClickListener(this);
        mItemCourses.setOnClickListener(this);
        mItemGrades.setOnClickListener(this);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mItemIdeas.getLayoutParams();
        params.weight = LOW_WEIGHT;
        mItemIdeas.setLayoutParams(params);

        params = (LinearLayout.LayoutParams) mItemHomework.getLayoutParams();
        params.weight = HIGH_WEIGHT;
        mItemHomework.setLayoutParams(params);

        params = (LinearLayout.LayoutParams) mItemCourses.getLayoutParams();
        params.weight = LOW_WEIGHT;
        mItemCourses.setLayoutParams(params);

        params = (LinearLayout.LayoutParams) mItemGrades.getLayoutParams();
        params.weight = LOW_WEIGHT;
        mItemGrades.setLayoutParams(params);

        Space space = (Space) findViewById(R.id.space);
        initialSpaceHeight = space.getHeight();

        /*colors.add(mContext.getResources().getColor(R.color.colorFirstItem));
        colors.add(mContext.getResources().getColor(R.color.colorSecondItem));
        colors.add(mContext.getResources().getColor(R.color.colorThirdItem));
        colors.add(mContext.getResources().getColor(R.color.colorFourthItem));*/
    }

    @Override
    public void onClick(View v) {
        if (!initialize && getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            ViewGroup.LayoutParams params =
                    mCircularRevealView.getLayoutParams();
            if (findViewById(R.id.space).getHeight() == 0) {
                params.height = getRealSize();
            } else {
                params.height = mBackground.getHeight();
            }

            mCircularRevealView.setLayoutParams(params);

            initialize = true;
        }

        if (Integer.parseInt(v.getTag().toString()) == selectedItem) {
            listener.bnReselectedItem();
        }
        else if (v == mItemIdeas) {
            animateBar(0, true);
            listener.bnSelectedItem(0);
        } else if (v == mItemHomework) {
            animateBar(1, true);
            listener.bnSelectedItem(1);
        } else if (v == mItemCourses) {
            animateBar(2, true);
            listener.bnSelectedItem(2);
        } else if (v == mItemGrades) {
            animateBar(3, true);
            listener.bnSelectedItem(3);
        }
    }

    public void animateBar(int index, boolean animate) {
        unselectCurrentItem(animate);
        switch (index) {
            case 0:
                animateSelectWeight(mItemIdeas, animate);
                animateSelectIconAndLabel(mIconIdeas, mLabelIdeas, animate);
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                    animateSelectPaddingTop(mItemIdeas, animate);
                circularRevealAnimation(mItemIdeas, animate);

                selectedItem = 0;
                break;
            case 1:
                animateSelectWeight(mItemHomework, animate);
                animateSelectIconAndLabel(mIconHomework, mLabelHomework, animate);
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                    animateSelectPaddingTop(mItemHomework, animate);
                circularRevealAnimation(mItemHomework, animate);

                selectedItem = 1;
                break;
            case 2:
                animateSelectWeight(mItemCourses, animate);
                animateSelectIconAndLabel(mIconCourses, mLabelCourses, animate);
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                    animateSelectPaddingTop(mItemCourses, animate);
                circularRevealAnimation(mItemCourses, animate);

                selectedItem = 2;
                break;
            case 3:
                animateSelectWeight(mItemGrades, animate);
                animateSelectIconAndLabel(mIconGrades, mLabelGrades, animate);
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                    animateSelectPaddingTop(mItemGrades, animate);
                circularRevealAnimation(mItemGrades, animate);

                selectedItem = 3;
                break;
        }
    }

    public void setColors(ArrayList<Integer> colors) {
        this.colors = colors;
    }

    private void circularRevealAnimation(final View view, boolean animate) {
        if (animate) {

            // get the center for the clipping circle
            int x, y;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                x = Math.round(view.getX() + view.getWidth() / 2);
                y = Math.round(view.getHeight() / 2);
            } else {
                x = Math.round(view.getWidth() / 2);
                y = Math.round(view.getY() + view.getHeight() / 2);
            }

            mCircularRevealView.setBackgroundColor(colors.get(Integer.parseInt(view.getTag().toString())));

            // create the animator for this view (the start radius is zero)
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(mCircularRevealView,
                            x,
                            y,
                            0,
                            getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ?
                                    mCircularRevealView.getWidth() :
                                    mCircularRevealView.getHeight());

            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    mBackground.setBackgroundColor(colors.get(Integer.parseInt(view.getTag().toString())));
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            // make the view visible and start the animation
            anim.start();
        } else {
            mBackground.setBackgroundColor(colors.get(Integer.parseInt(view.getTag().toString())));
        }
    }

    private void unselectCurrentItem(boolean animate) {
        View item;
        ImageView icon;
        TextView label;

        switch (selectedItem) {
            case 0:
                item = mItemIdeas;
                icon = mIconIdeas;
                label = mLabelIdeas;
                break;
            case 1:
                item = mItemHomework;
                icon = mIconHomework;
                label = mLabelHomework;
                break;
            case 2:
                item = mItemCourses;
                icon = mIconCourses;
                label = mLabelCourses;
                break;
            case 3:
                item = mItemGrades;
                icon = mIconGrades;
                label = mLabelGrades;
                break;
            default:
                return;
        }

        animateUnselectWeight(item, animate);
        animateUnselectIconAndLabel(icon, label, animate);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            animateUnselectPaddingTop(item, icon, animate);
    }

    private void animateUnselectWeight(final View item, boolean animate) {
        final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) item.getLayoutParams();

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(4, 3);
        //valueAnimator.setInterpolator(null);
        if (animate)
            valueAnimator.setDuration(200);
        else
            valueAnimator.setDuration(0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Float value = (Float) valueAnimator.getAnimatedValue();

                params.weight = value;
                item.setLayoutParams(params);
                //Log.i(TAG, "onAnimationUpdate: coucou" + value);

            }
        });
        valueAnimator.start();
    }

    private void animateSelectWeight(final View item, boolean animate) {
        final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) item.getLayoutParams();

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(3, 4);
        if (animate)
            valueAnimator.setDuration(200);
        else
            valueAnimator.setDuration(0);
        //valueAnimator.setInterpolator(null);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Float value = (Float) valueAnimator.getAnimatedValue();

                params.weight = value;
                item.setLayoutParams(params);
                //Log.i(TAG, "onAnimationUpdate: coucou" + value);
            }
        });
        valueAnimator.start();
    }

    private void animateUnselectIconAndLabel(final ImageView icon, final TextView label, boolean animate) {
        /*final LinearLayout.LayoutParams iconParams = (LinearLayout.LayoutParams) icon.getLayoutParams();
        final LinearLayout.LayoutParams labelParams = (LinearLayout.LayoutParams) label.getLayoutParams();*/

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1, 0);
        if (animate)
            valueAnimator.setDuration(200);
        else
            valueAnimator.setDuration(0);
        //valueAnimator.setInterpolator(null);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Float value = (Float) valueAnimator.getAnimatedValue();

                icon.setAlpha(0.5f + value / 2);
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                    label.setAlpha(value);
            }
        });
        valueAnimator.start();
    }

    private void animateSelectIconAndLabel(final ImageView icon, final TextView label, boolean animate) {
        /*final LinearLayout.LayoutParams iconParams = (LinearLayout.LayoutParams) icon.getLayoutParams();
        final LinearLayout.LayoutParams labelParams = (LinearLayout.LayoutParams) label.getLayoutParams();*/

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        if (animate)
            valueAnimator.setDuration(200);
        else
            valueAnimator.setDuration(0);
        //valueAnimator.setInterpolator(null);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Float value = (Float) valueAnimator.getAnimatedValue();

                icon.setAlpha(0.5f + value / 2);
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                    label.setAlpha(value);
            }
        });
        valueAnimator.start();
    }

    private void animateUnselectPaddingTop(final View item, final View icon, boolean animate) {
        LinearLayout ll = (LinearLayout) findViewById(R.id.real_bar);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(item.getPaddingTop(), ll.getHeight() / 2 - icon.getHeight() / 2);
        if (animate)
            valueAnimator.setDuration(200);
        else
            valueAnimator.setDuration(0);
        //valueAnimator.setInterpolator(null);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Float value = (Float) valueAnimator.getAnimatedValue();

                item.setPadding(item.getPaddingLeft(), Math.round(value), item.getPaddingRight(), item.getPaddingBottom());
            }
        });
        valueAnimator.start();
    }

    private void animateSelectPaddingTop(final View item, boolean animate) {
        LinearLayout ll = (LinearLayout) findViewById(R.id.real_bar);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(item.getPaddingTop(), ll.getHeight() / 8);
        if (animate)
            valueAnimator.setDuration(200);
        else
            valueAnimator.setDuration(0);
        //valueAnimator.setInterpolator(null);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Float value = (Float) valueAnimator.getAnimatedValue();

                item.setPadding(item.getPaddingLeft(), Math.round(value), item.getPaddingRight(), item.getPaddingBottom());
            }
        });
        valueAnimator.start();
    }

    public void removeSpace() {
        /*Space space = (Space) findViewById(R.id.space);

        ViewGroup.LayoutParams params = space.getLayoutParams();
        params.height = 0;
        space.setLayoutParams(params);*/
        Space space = (Space) findViewById(R.id.space);

        if (!mIsLow && getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setY(getY() + space.getHeight());
            mIsLow = true;
        }
    }

    public void addSpace() {
        Space space = (Space) findViewById(R.id.space);

        if (mIsLow) {
            setY(getY() - space.getHeight());
            mIsLow = false;
        }
    }

    public int getRealSize() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.real_bar);
        return ll.getHeight();
    }

    public int getBottomPadding() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.real_bar);
        return ll.getPaddingBottom();
    }

    public void setAlpha(float alpha) {
        LinearLayout ll = (LinearLayout) findViewById(R.id.real_bar);
        ll.setAlpha(alpha);
    }

    public void show() {
        if (!isShown) {
            final LinearLayout ll = (LinearLayout) findViewById(R.id.real_bar);
            AlphaAnimation animation1 = new AlphaAnimation(0f, 1f);
            animation1.setDuration(200);
            ll.startAnimation(animation1);
            ll.setAlpha(1f);
            isShown = true;
        }
    }

    public void hide() {
        if (isShown) {
            final LinearLayout ll = (LinearLayout) findViewById(R.id.real_bar);
            AlphaAnimation animation1 = new AlphaAnimation(1f, 0f);
            animation1.setDuration(200);
            animation1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    ll.setAlpha(0f);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            ll.startAnimation(animation1);
            isShown = false;
        }
    }

    public void setmBackgroundColor(int index) {
        mBackground.setBackgroundColor(colors.get(index));
    }

    public int getSelectedItem() {
        return selectedItem;
    }

    public interface BottomNavigationListener {
        // you can define any parameter as per your requirement
        void bnSelectedItem(int index);

        void bnReselectedItem();
    }
}
