package com.android.charl.skol.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.charl.skol.R;
import com.android.charl.skol.activities.MainActivity;

public class HomeworkFragment extends BottomNavigationFragment {
    private static final String TAG = "HomeworkFragment";

    private OnFragmentInteractionListener mListener;

    public HomeworkFragment() {
        // Required empty public constructor
    }

    /*public static HomeworkFragment newInstance(Context context) {
        mContext = context;
        return new HomeworkFragment();
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_homework, container, false);

        mContainer = (RelativeLayout) v.findViewById(R.id.container_homework);

        mList = (RecyclerView) v.findViewById(R.id.list);
        Log.i(TAG, "onCreateView:BBBBBBBBBBBBBBBB " + mList + "\n" + v);
        //((MainActivity)mContext).setListsListener(mList);
        mEmptyListLayout = (LinearLayout) v.findViewById(R.id.empty_list);

        TextView textView = (TextView) v.findViewById(R.id.txt_homework);
        textView.setText(Html.fromHtml(textView.getText().toString()));

        mImageView = (ImageView) v.findViewById(R.id.img_homework);
        mPictureId = R.drawable.homework;

        mHasBeenDrawn = true;
        
        return v;
    }

    @Override
    public void setList() {

    }

    @Override
    public void refresh() {

    }

    @Override
    public void smoothScrollToTop() {

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
