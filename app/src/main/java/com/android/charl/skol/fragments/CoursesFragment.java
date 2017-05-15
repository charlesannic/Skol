package com.android.charl.skol.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.charl.skol.R;
import com.android.charl.skol.activities.MainActivity;
import com.android.charl.skol.adapter.CourseAdapter;
import com.android.charl.skol.io.DatabaseHelper;
import com.android.charl.skol.java.Schedule;

import java.util.ArrayList;

public class CoursesFragment extends BottomNavigationFragment {
    private static final String TAG = "MemoFragment";

    private OnFragmentInteractionListener mListener;

    public RelativeLayout headerscontainer;

    public CoursesFragment() {
        // Required empty public constructor
    }

    /*public static CoursesFragment newInstance(Context context) {
        mContext = context;
        return new CoursesFragment();
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_courses, container, false);

        mContainer = (RelativeLayout) v.findViewById(R.id.container_courses);

        headerscontainer = (RelativeLayout) v.findViewById(R.id.headers_container);

        mList = (RecyclerView) v.findViewById(R.id.list);
        v.post(new Runnable() {
                   @Override
                   public void run() {
                       // code you want to run when view is visible for the first time
                       setList();
                   }
               }
        );

        mEmptyListLayout = (LinearLayout) v.findViewById(R.id.empty_list);

        TextView textView = (TextView) v.findViewById(R.id.txt_courses);
        textView.setText(Html.fromHtml(textView.getText().toString()));

        mImageView = (ImageView) v.findViewById(R.id.img_courses);
        mPictureId = R.drawable.courses;

        mHasBeenDrawn = true;

        return v;
    }

    @Override
    public void setList() {
        //if (mList != null) {
        Context context = getContext();
        ArrayList<Schedule> values = DatabaseHelper.ScheduleHelper.getAllSchedules();
        if(values.size()>0)
            mEmptyListLayout.setVisibility(View.GONE);
        mAdapter = new CourseAdapter(context, this, values);
        /*GridLayoutManager manager = new GridLayoutManager(context, 6);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position%2 == 1 ? 5 : 1;
            }
        });*/
        mList.setLayoutManager(new LinearLayoutManager(context));
        mList.setAdapter(mAdapter);
        mList.setItemAnimator(new DefaultItemAnimator());
        ((MainActivity) context).setListsListener(mList);
        //}
    }

    @Override
    public void refresh() {
        ((CourseAdapter)mAdapter).refresh();
        smoothScrollToTop();
    }

    @Override
    public void smoothScrollToTop() {
        mList.smoothScrollToPosition(0);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
