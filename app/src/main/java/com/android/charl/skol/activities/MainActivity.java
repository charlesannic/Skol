package com.android.charl.skol.activities;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.charl.bottomnavigation.BottomNavigation;
import com.android.charl.searchbar.SearchBar;
import com.android.charl.skol.R;
import com.android.charl.skol.adapter.CourseAdapter;
import com.android.charl.skol.adapter.NonScrollableViewPagerAdapter;
import com.android.charl.skol.fragments.CoursesFragment;
import com.android.charl.skol.fragments.GradesFragment;
import com.android.charl.skol.fragments.HomeworkFragment;
import com.android.charl.skol.fragments.MemoFragment;
import com.android.charl.skol.io.DatabaseHelper;
import com.android.charl.skol.utils.Utils;
import com.android.charl.skol.views.NonScrollableViewPager;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        BottomNavigation.BottomNavigationListener,
        HomeworkFragment.OnFragmentInteractionListener,
        MemoFragment.OnFragmentInteractionListener,
        CoursesFragment.OnFragmentInteractionListener,
        GradesFragment.OnFragmentInteractionListener {

    private static final String TAG = "MainActivity";

    private NonScrollableViewPager mViewPager;
    private NonScrollableViewPagerAdapter mViewPagerAdapter;
    private HomeworkFragment mHomeworkFragment;
    private MemoFragment mMemoFragment;
    private CoursesFragment mCoursesFragment;
    private GradesFragment mGradesFragment;

    private RelativeLayout mRlSearchLayout;
    private EditText mEtSearch;
    private TextView mTvSearch;
    private ImageView mIbMore;
    private LinearLayout mLlSearchBackground;
    private LinearLayout mLlSearchBackground2;
    private FloatingActionButton mFab;

    private TransitionDrawable mSearchBackgroundTranstion;

    private int scrollValue = 0;
    private int mCurrentPage;
    private BottomNavigation mBottomNavigation;
    private SearchBar mAppBarLayout;
    private CourseAdapter mAdapter;

    private BottomSheetBehavior behavior;

    private HidingScrollListener hidingScrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: ");
        super.onCreate(null);
        setContentView(R.layout.activity_main);

        DatabaseHelper.getInstance(this);

        mViewPager = (NonScrollableViewPager) findViewById(R.id.view_pager);
        mViewPagerAdapter = new NonScrollableViewPagerAdapter(getSupportFragmentManager());
        mMemoFragment = new MemoFragment();
        //mMemoFragment.setmContext(this);
        mViewPagerAdapter.add(mMemoFragment);
        mHomeworkFragment = new HomeworkFragment();
        //mHomeworkFragment.setmContext(this);
        mViewPagerAdapter.add(mHomeworkFragment);
        mCoursesFragment = new CoursesFragment();
        //mCoursesFragment.setmContext(this);
        mViewPagerAdapter.add(mCoursesFragment);
        mGradesFragment = new GradesFragment();
        //mGradesFragment.setmContext(this);
        mViewPagerAdapter.add(mGradesFragment);
        mViewPager.setAdapter(mViewPagerAdapter);
        if (savedInstanceState != null) {
            mCurrentPage = savedInstanceState.getInt("CURRENT_PAGE");
        } else {
            mCurrentPage = 1;
        }

        mViewPager.setCurrentItem(mCurrentPage, false);

        //mRlSearchLayout = (RelativeLayout) findViewById(R.id.search_layout);
        mEtSearch = (EditText) findViewById(R.id.search_bar);
        mTvSearch = (TextView) findViewById(R.id.title_search_bar);
        mIbMore = (ImageView) findViewById(R.id.ic_more);
        mIbMore.bringToFront();
        //mLlSearchBackground = (LinearLayout) findViewById(R.id.search_background);
        //mLlSearchBackground2 = (LinearLayout) findViewById(R.id.search_background_2);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        if (android.os.Build.VERSION.SDK_INT >= 21)
            mFab.setTransitionName("fabtransition");

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        View bottomSheet = coordinatorLayout.findViewById(R.id.bottom_sheet);
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                Log.i(TAG, "onStateChanged: " + newState);
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                mIbMore.setImageDrawable(getResources().getDrawable(R.drawable.more_to_close_animation_full));
                AnimationDrawable frameAnimation = (AnimationDrawable) mIbMore.getDrawable();
                frameAnimation.selectDrawable(Math.round(47 * slideOffset));
            }
        });

        mEtSearch.setVisibility(View.INVISIBLE);

        //mRlSearchLayout.bringToFront();

        //mSearchBackgroundTranstion = (TransitionDrawable) mLlSearchBackground.getBackground();


        mTvSearch.setOnClickListener(this);
        mIbMore.setOnClickListener(this);
        //mLlSearchBackground.setOnClickListener(this);
        //mLlSearchBackground2.setOnClickListener(this);
        mFab.setOnClickListener(this);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        //mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        /*mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);*/

        /*TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);*/


        mAppBarLayout = (SearchBar) findViewById(R.id.sb);
        mAppBarLayout.post(new Runnable() {
            @Override
            public void run() {
                setHidingScrollListener();
            }
        });
        mBottomNavigation = (BottomNavigation) findViewById(R.id.bottom_navigation);
        mBottomNavigation.setListener(this);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.colorFirstItem));
        colors.add(getResources().getColor(R.color.colorSecondItem));
        colors.add(getResources().getColor(R.color.colorThirdItem));
        colors.add(getResources().getColor(R.color.colorFourthItem));
        mBottomNavigation.setColors(colors);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //Log.i(TAG, "onPageSelected: ");
                cropImages();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: " + requestCode + " " + resultCode);
        switch (requestCode) {
            case 1:
                mCoursesFragment.refresh();
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("CURRENT_PAGE", mCurrentPage);

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        Log.i(TAG, "onWindowFocusChanged: " + " " + mCurrentPage + " " + getResources().getConfiguration().orientation + " " + (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE));

        mBottomNavigation.animateBar(mCurrentPage, false);

        if (!Utils.hasNavBar(this) || getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mBottomNavigation.removeSpace();
        }

        if (hasFocus) {
            //mHomeworkFragment.setList();
        }
    }

    private void setHeaders(RecyclerView recyclerView) {
        //if (mCoursesFragment.headerscontainer != null) {

        mCoursesFragment.headerscontainer.removeAllViews();
        String[] daysOfWeek = MainActivity.this.getResources().getStringArray(R.array.days_of_week);
        ArrayList<TextView> headers = new ArrayList<>();
        int statusBarHeight = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));

        View referenceView = recyclerView.getChildAt(0).findViewById(R.id.card);
        LinearLayout.LayoutParams referenceParams = (LinearLayout.LayoutParams) referenceView.getLayoutParams();

        for (int i = 0; i < mCoursesFragment.getmAdapter().getItemCount(); i++) {
            if (recyclerView.getChildAt(i) == null)
                break;

            int thisone = (Integer) recyclerView.getChildAt(i).getTag();
            int previous = i == 0 ? -1 : (Integer) recyclerView.getChildAt(i - 1).getTag();

            View child = recyclerView.getChildAt(i);

            if (previous != thisone) {
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                TextView tile = (TextView) inflater.inflate(R.layout.header_recyclerview, null, false);

                tile.setText(String.valueOf(daysOfWeek[(Integer) child.getTag()].charAt(0) +
                        String.valueOf(daysOfWeek[(Integer) child.getTag()].charAt(1))));
                tile.setTag(child.getTag());

                if (child.getY() < statusBarHeight
                        || child.getY() < (mAppBarLayout.getY() + mAppBarLayout.getHeight())) {
                    float appbarY = (mAppBarLayout.getY() + mAppBarLayout.getHeight());
                    tile.setY(statusBarHeight > appbarY ? statusBarHeight : appbarY);
                } else
                    tile.setY(child.getY());

                mCoursesFragment.headerscontainer.addView(tile);

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tile.getLayoutParams();
                Log.i(TAG, "setHeaders: " + referenceParams.height + " " + referenceParams.topMargin);
                params.height = referenceView.getHeight() + referenceParams.topMargin * 2;
                params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                tile.setLayoutParams(params);

                headers.add(tile);
            }
        }

        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        //Log.i(TAG, "setHeaders: currentDay " + currentDay + " " + ((currentDay + 1) % 7) + 1);
        for (int i = 0; i < headers.size() - 1; i++) {
            //Log.i(TAG, "setHeaders: " + headers.get(i).getY() + " " + recyclerView.getChildAt(0).getHeight() + " " + headers.get(i + 1).getY());
            if ((Integer) headers.get(i).getTag() == ((currentDay - 1) % 7) - 1)
                headers.get(i).setTextColor(getResources().getColor(R.color.textSelection));
            if (headers.get(i).getY() + referenceView.getHeight() + referenceParams.topMargin * 2 > headers.get(i + 1).getY())
                headers.get(i).setY(headers.get(i + 1).getY() - (referenceView.getHeight() + referenceParams.topMargin * 2));
        }
        //if(currentDay == ((mValues.get(position+1).getDay() + 1)%7)+1)
        if ((Integer) headers.get(headers.size() - 1).getTag() == ((currentDay - 1) % 7) - 1)
            headers.get(headers.size() - 1).setTextColor(getResources().getColor(R.color.textSelection));
        //}
    }

    private void setHidingScrollListener() {
        hidingScrollListener = new HidingScrollListener(this, mAppBarLayout) {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //Log.i(TAG, "onScrollStateChanged: " + newState);
            }

            @Override
            public void onMoved(RecyclerView recyclerView, int distance) {
                //Log.i(TAG, "onMoved: " + distance);
                mAppBarLayout.setTranslationY(-distance);
                if (/*getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT*/ false) {
                    int bottomNavigationDistance = (int) (((float) mBottomNavigation.getHeight() / (float) mAppBarLayout.getHeight()) * distance);
                    mBottomNavigation.setTranslationY(bottomNavigationDistance);
                    if (bottomNavigationDistance <= mBottomNavigation.getRealSize())
                        mFab.setTranslationY(bottomNavigationDistance);
                    else
                        mFab.setTranslationY(mBottomNavigation.getRealSize());

                    //Log.i(TAG, "onMoved: " + mList.computeVerticalScrollOffset());
                    if (recyclerView.computeVerticalScrollOffset() > 0)
                        mAppBarLayout.setSmoothElevation(true);
                    else
                        mAppBarLayout.setSmoothElevation(false);

                    if (bottomNavigationDistance > mBottomNavigation.getBottomPadding())
                        mBottomNavigation.hide();
                    else if (bottomNavigationDistance < mBottomNavigation.getBottomPadding() || recyclerView.computeVerticalScrollOffset() == 0)
                        mBottomNavigation.show();

                }
            }

            @Override
            public void onShow() {
                mAppBarLayout.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        setHeaders(mCoursesFragment.getmList());
                    }
                }).start();
                if (/*getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT*/ false) {
                    mBottomNavigation.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                    mFab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                    mBottomNavigation.show();
                }
            }

            @Override
            public void onHide() {
                mAppBarLayout.animate().translationY(-mAppBarLayout.getHeight()).setInterpolator(new AccelerateInterpolator(2)).setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        setHeaders(mCoursesFragment.getmList());
                    }
                }).start();
                if (/*getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT*/ false) {
                    mBottomNavigation.animate().translationY(mBottomNavigation.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();
                    mFab.animate().translationY(mBottomNavigation.getRealSize()).setInterpolator(new AccelerateInterpolator(2)).start();
                    mBottomNavigation.hide();
                }
            }
        };
    }

    public void setListsListener(final RecyclerView list) {
        list.setOnScrollListener(hidingScrollListener);
    }

    private void cropImages() {
        if (mCurrentPage == 0)
            mMemoFragment.cropPicture();
        if (mCurrentPage == 1)
            mHomeworkFragment.cropPicture();
        if (mCurrentPage == 2)
            mCoursesFragment.cropPicture();
        if (mCurrentPage == 3)
            mGradesFragment.cropPicture();
    }

    @Override
    public void onClick(View v) {
        if (v == mTvSearch) {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else if (v == mIbMore) {
            if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                showPopup(mIbMore);
            } else {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                //mSearchBarAnimations.closeSearchBar();
                mEtSearch.setText(null);
            }
        } else if (v == mLlSearchBackground || v == mLlSearchBackground2) {
            mEtSearch.setText(null);
        } else if (v == mFab) {
            switch (mCurrentPage) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    startActivityForResult(new Intent(MainActivity.this, AddCourseActivity.class), 1);
                    break;
                case 3:
                    break;
            }
        }
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_main, popup.getMenu());
        popup.setGravity(Gravity.RIGHT);
        popup.show();
    }

    @Override
    public void onBackPressed() {
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
            //mIbMore.performClick();
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        else
            super.onBackPressed();
    }

    @Override
    protected void onPause() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEtSearch.getWindowToken(), 0);

        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public float getTopPosition() {
        int statusBarHeight = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));

        return mAppBarLayout.getY() < statusBarHeight ? statusBarHeight : mAppBarLayout.getY();
    }

    public static int mToolbarOffset = 0;

    public abstract class HidingScrollListener extends RecyclerView.OnScrollListener {

        private static final float HIDE_THRESHOLD = 10;
        private static final float SHOW_THRESHOLD = 70;

        private int mBBOffset = 0;
        private boolean mControlsVisible = true;
        private int mToolbarHeight;
        private int mBBHeight;

        public HidingScrollListener(Context context, SearchBar appBarLayout) {
            //mToolbarHeight = Utils.getToolbarHeight(context);
            mToolbarHeight = appBarLayout.getHeight();
            mBBHeight = mBottomNavigation.getHeight();
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                //Log.i(TAG, "onScrollStateChanged: " + mControlsVisible + " " + mToolbarOffset +" " +  HIDE_THRESHOLD +" " +  recyclerView.computeVerticalScrollOffset() +" " +  mList.getPaddingTop());
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager.findLastCompletelyVisibleItemPosition() == recyclerView.getAdapter().getItemCount())
                    setInvisible();
                else if (mControlsVisible) {
                    if (mToolbarOffset > HIDE_THRESHOLD
                            && recyclerView.computeVerticalScrollOffset() > recyclerView.getPaddingTop()) {
                        Log.i(TAG, "onScrollStateChanged: 1");
                        setInvisible();
                    } else {
                        Log.i(TAG, "onScrollStateChanged: 2");
                        setVisible();
                    }
                } else {
                    //Log.i(TAG, "onScrollStateChanged: " + recyclerView.computeHorizontalScrollOffset() + " " + mList.getPaddingTop());
                    if (recyclerView.computeVerticalScrollOffset() < recyclerView.getPaddingTop()) {
                        Log.i(TAG, "onScrollStateChanged: 3");
                        setVisible();
                    } else if ((mToolbarHeight - mToolbarOffset) > SHOW_THRESHOLD) {
                        Log.i(TAG, "onScrollStateChanged: 4");
                        setVisible();
                    } else {
                        Log.i(TAG, "onScrollStateChanged: 5");
                        setInvisible();
                    }
                }
            }

        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            setHeaders(recyclerView);
            //Log.i(TAG, "onScrolled: " + /*mCoursesFragment.getmAdapter().getmValues().get(*//*recyclerView.getChildAdapterPosition(*/recyclerView.getChildAt(0).getTag().toString()/*)*//*).getDay()*/);
            //recyclerView.getChildAt(0).getY();
            /*if (mCoursesFragment.getmAdapter() != null) {
                Log.i(TAG, "onScrolled: " + mCoursesFragment.getmAdapter().getmHeaders().get(0).getY());
                int statusBarHeight = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));

                //Log.i(TAG, "onScrolled: " + mCoursesFragment.getmList().getChildAdapterPosition(mCoursesFragment.getmAdapter().getmHeaders().get(1)));
                Log.i(TAG, "onScrolled: " + mCoursesFragment.getmAdapter().getmHeaders().size());
                for (int i = 0; i < mCoursesFragment.getmAdapter().getmHeaders().size(); i++) {
                    /*if (mCoursesFragment.getmAdapter().getmHeaders().get(i) != null) {
                        Float min = recyclerView.getChildAt(recyclerView.getChildAdapterPosition(mCoursesFragment.getmAdapter().getmHeaders().get(i)) + 1).getY();
                        Float max = null;
                        if (i < mCoursesFragment.getmAdapter().getmHeaders().size() - 1)
                            max = recyclerView.getChildAt(recyclerView.getChildAdapterPosition(mCoursesFragment.getmAdapter().getmHeaders().get(i + 1)) - 1).getY();
                        else
                            max = min; //recyclerView.getChildAt(mCoursesFragment.getmAdapter().getmValues().size()).getY();
                        if (min > statusBarHeight)
                            mCoursesFragment.getmAdapter().getmHeaders().get(i).setY(min);
                        else {
                            if (max < statusBarHeight)
                                mCoursesFragment.getmAdapter().getmHeaders().get(i).setY(max);
                            else
                                mCoursesFragment.getmAdapter().getmHeaders().get(i).setY(0);
                        }
                    }*/
                    /*if(i < mCoursesFragment.getmAdapter().getmHeaders().size()-1 &&
                            mCoursesFragment.getmAdapter().getmHeaders().get(i) != null && mCoursesFragment.getmAdapter().getmHeaders().get(i+1) != null &&
                            mCoursesFragment.getmAdapter().getmHeaders().get(i).getY() + mCoursesFragment.getmAdapter().getmHeaders().get(i).getHeight() <
                            mCoursesFragment.getmAdapter().getmHeaders().get(i+1).getY())
                        mCoursesFragment.getmAdapter().getmHeaders().get(i).setY(statusBarHeight);

                }*/
                /*if(mCoursesFragment.getmAdapter().getmHeaders().get(0).getY() < statusBarHeight)
                    mCoursesFragment.getmAdapter().getmHeaders().get(0).setY(statusBarHeight);*/
            //}

            int offset = recyclerView.computeVerticalScrollOffset();
            int extent = recyclerView.computeVerticalScrollExtent();
            int range = recyclerView.computeVerticalScrollRange();

            int percentage = (int) (100.0 * offset / (float) (range - extent));

            clipToolbarOffset();
            onMoved(recyclerView, mToolbarOffset);

            if ((mToolbarOffset < mToolbarHeight && dy > 0) || (mToolbarOffset > 0 && dy < 0)) {
                mToolbarOffset += dy;
            }

        }

        private void clipToolbarOffset() {
            if (mToolbarOffset > mToolbarHeight) {
                mToolbarOffset = mToolbarHeight;
            } else if (mToolbarOffset < 0) {
                mToolbarOffset = 0;
            }
        }

        private void setVisible() {
            if (mToolbarOffset >= 0) {
                onShow();
                mToolbarOffset = 0;
            }
            mControlsVisible = true;
        }

        private void setInvisible() {
            if (mToolbarOffset <= mToolbarHeight) {
                onHide();
                mToolbarOffset = mToolbarHeight;
            }
            mControlsVisible = false;
        }

        public abstract void onMoved(RecyclerView recyclerView, int distance);

        public abstract void onShow();

        public abstract void onHide();
    }

    @Override
    public void bnSelectedItem(int index) {

        if (index == 0) {
            willBeDisplayed(0);
        } else if (index == 1) {
            willBeDisplayed(1);
        } else if (index == 2) {
            willBeDisplayed(2);
        } else if (index == 3) {
            willBeDisplayed(3);
        }

    }

    @Override
    public void bnReselectedItem() {

        if (mCurrentPage == 0) {
            mMemoFragment.smoothScrollToTop();
        } else if (mCurrentPage == 1) {
            mHomeworkFragment.smoothScrollToTop();
        } else if (mCurrentPage == 2) {
            mCoursesFragment.smoothScrollToTop();
        } else if (mCurrentPage == 3) {
            mGradesFragment.smoothScrollToTop();
        }

    }

    /**
     * Called when a fragment will be displayed
     */
    public void willBeDisplayed(final int index) {
        // Do what you want here, for example animate the content
        //if (fragmentContainer != null) {
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mCurrentPage = index;
                Log.i(TAG, "onAnimationEnd: " + mCurrentPage);
                mViewPager.setCurrentItem(mCurrentPage, false);
                new AsyncTask<Void, Void, Void>() {

                    Animation fadeIn;

                    @Override
                    protected Void doInBackground(Void... voids) {
                        fadeIn = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in);

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);

                        mViewPager.startAnimation(fadeIn);
                    }
                }.execute();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mViewPager.startAnimation(fadeOut);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
