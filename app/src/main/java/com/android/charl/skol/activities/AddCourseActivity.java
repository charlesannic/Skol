package com.android.charl.skol.activities;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.charl.skol.R;
import com.android.charl.skol.adapter.ColorPickerAdapter;
import com.android.charl.skol.adapter.NotificationDialogAdapter;
import com.android.charl.skol.fragments.SchedulesBottonSheetDialogFragment;
import com.android.charl.skol.io.DatabaseHelper;
import com.android.charl.skol.java.Course;
import com.android.charl.skol.java.Schedule;
import com.android.charl.skol.java.Teacher;
import com.android.charl.skol.views.ColorPickerDialog;
import com.android.charl.skol.views.NotificationDialog;

import java.util.ArrayList;

public class AddCourseActivity extends AppCompatActivity implements View.OnClickListener,
        ColorPickerAdapter.ColorPickerDialogListener,
        NotificationDialogAdapter.NotificationDialogListener {

    private static final String TAG = "AddCourseActivity";

    private ArrayList<Schedule> schedules;
    private boolean activityAlreadyLaunch = false;
    private boolean firstTime = true;
    private com.android.charl.skol.java.Color mColor;
    private int previousColor;
    private int previousAccentColor;

    private AppBarLayout mAppBarLayout;
    private ImageView mClose;
    private FloatingActionButton mFab;
    private EditText mCourseName;
    private EditText mCourseCoefficient;
    private EditText mCourseTeacher;
    private EditText mCourseTeacherEmail;
    private TextView mColorIndicator;
    private TextView mColorName;
    private TextView mSchedulesLabel;
    private LinearLayout mCourseSchedules;
    private LinearLayout mColorPicker;
    private LinearLayout mCourseNotifications;
    private TextView mNotificationLabel;
    private ScrollView mContent;

    private SchedulesBottonSheetDialogFragment myBottomSheet;

    private EditText mScheduleLocation;

    private ColorPickerDialog mColorPickerDialog;
    private NotificationDialog mNotificationDialog;
    private int mNotificationTime = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        schedules = new ArrayList<>();
        previousColor = getColor(R.color.mariner);
        mColor = new com.android.charl.skol.java.Color(8, "Mariner", R.color.mariner);
        previousAccentColor = getColor(R.color.colorAccent);

        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mClose = (ImageView) findViewById(R.id.ic_close);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mCourseName = (EditText) findViewById(R.id.course_name);
        mCourseCoefficient = (EditText) findViewById(R.id.course_coefficient);
        mCourseTeacher = (EditText) findViewById(R.id.course_teacher);
        mCourseTeacherEmail = (EditText) findViewById(R.id.course_teacher_email);
        mColorIndicator = (TextView) findViewById(R.id.color_indicator);
        mColorName = (TextView) findViewById(R.id.color_name);
        mSchedulesLabel = (TextView) findViewById(R.id.nb_schedule);
        mColorPicker = (LinearLayout) findViewById(R.id.color_picker);
        mCourseNotifications = (LinearLayout) findViewById(R.id.course_notifications);
        mNotificationLabel = (TextView) findViewById(R.id.notification_label);
        mCourseSchedules = (LinearLayout) findViewById(R.id.modify_schedules);
        mContent = (ScrollView) findViewById(R.id.scroll_view);

        mClose.setOnClickListener(this);
        mFab.setOnClickListener(this);
        mColorPicker.setOnClickListener(this);
        mCourseNotifications.setOnClickListener(this);
        mCourseSchedules.setOnClickListener(this);

        mColorPickerDialog = new ColorPickerDialog(this, this);
        mColorPickerDialog.setTitle("Choix de la couleur");
        mNotificationDialog = new NotificationDialog(this, this);
        mNotificationDialog.setTitle("Choix des notifications");

        mCourseCoefficient.setTextSize(TypedValue.COMPLEX_UNIT_PX, mColorName.getTextSize());
        mCourseTeacher.setTextSize(TypedValue.COMPLEX_UNIT_PX, mColorName.getTextSize());
        mCourseTeacherEmail.setTextSize(TypedValue.COMPLEX_UNIT_PX, mColorName.getTextSize());

        mCourseName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mCourseName.getText().toString().length() > 0)
                    mFab.show();
                else
                    mFab.hide();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        myBottomSheet = SchedulesBottonSheetDialogFragment.newInstance(this, "Modal Bottom Sheet", mColorName.getTextSize());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if(hasFocus)
            setSchedulesText(schedules.size());
    }

    @Override
    public void onBackPressed() {
        mClose.performClick();
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick: ");
        if (v == mClose)
            new AlertDialog.Builder(this, previousAccentColor == getColor(R.color.colorAccent) ? R.style.Dialog : R.style.Dialog2)
                    .setMessage("Voulez-vous vraiment supprimer ce cours ?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            AddCourseActivity.super.onBackPressed();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        else if (v == mColorPicker) {
            mColorPickerDialog.show();
        } else if (v == mCourseSchedules) {
            firstTime = false;
            myBottomSheet.setmColorAccent(previousAccentColor);
            myBottomSheet.show(getSupportFragmentManager(), myBottomSheet.getTag());
        } else if (v == mCourseNotifications) {
            mNotificationDialog.show();
        } else if (v == mFab) {
            Course course = new Course(mCourseName.getText().toString(),
                    mColor,
                    myBottomSheet.getArrayList(),
                    new Teacher(mCourseTeacher.getText().toString(), mCourseTeacherEmail.getText().toString()),
                    mCourseCoefficient.getText().toString().length() == 0 ?
                            1. : Double.parseDouble(mCourseCoefficient.getText().toString()),
                    mNotificationTime);

            double id = DatabaseHelper.CourseHelper.addCourse(course);
            finish();
            //Log.i(TAG, "onClick2: " + id);
        }
    }

    public void tintToolbar(final int nextColor, final int nextColorAccent) {
        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            int blended = previousColor;
            int blendedAccent = previousAccentColor;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // Use animation position to blend colors.
                float position = animation.getAnimatedFraction();

                blended = blendColors(blended, nextColor, position);
                blendedAccent = blendColors(blendedAccent, nextColorAccent, position);
                mAppBarLayout.setBackgroundColor(blended);
                //mCourseSchedules.setTextColor(blendedAccent);
                mFab.setBackgroundTintList(ColorStateList.valueOf(blendedAccent));
            }
        });
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                previousColor = nextColor;
                previousAccentColor = nextColorAccent;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.setDuration(1000);
        anim.start();
    }

    private int blendColors(int from, int to, float ratio) {
        final float inverseRatio = 1f - ratio;

        final float r = Color.red(to) * ratio + Color.red(from) * inverseRatio;
        final float g = Color.green(to) * ratio + Color.green(from) * inverseRatio;
        final float b = Color.blue(to) * ratio + Color.blue(from) * inverseRatio;

        return Color.rgb((int) r, (int) g, (int) b);
    }

    @Override
    public void callback(com.android.charl.skol.java.Color color, boolean mainAccentColor, int colorIndicator) {
        mColor = color;
        mColorPickerDialog.dismiss();
        mColorIndicator.setBackgroundResource(colorIndicator);
        tintToolbar(color.getColor(),
                mainAccentColor ? getColor(R.color.colorAccent) : getColor(R.color.colorAccent2));
        mColorName.setText(color.getColorName());
        //tintAccentColor(mainAccentColor ? R.color.colorAccent : R.color.colorAccent2);
        //mAppBarLayout.setBackgroundResource(R.color.amber);
    }

    @Override
    public void callback(int time) {
        mNotificationDialog.dismiss();

        this.mNotificationTime = time;

        switch (time) {
            case 0:
                mNotificationLabel.setText("Pas de notification");
                break;
            case 10:
                mNotificationLabel.setText("10 minutes avant");
                break;
            case 30:
                mNotificationLabel.setText("30 minutes avant");
                break;
            case 60:
                mNotificationLabel.setText("1 heure avant");
                break;
        }
    }

    public ArrayList<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(ArrayList<Schedule> schedules) {
        this.schedules = schedules;
    }

    public void setSchedulesText(int nb) {
        Log.i(TAG, "setSchedulesText: AZERTY");
        if (nb > 1) {
            mSchedulesLabel.setText(nb + " horaires");
            mSchedulesLabel.setTextColor(Color.BLACK);
        } else if (nb > 0) {
            mSchedulesLabel.setText(nb + " horaire");
            mSchedulesLabel.setTextColor(Color.BLACK);
        } else {
            mSchedulesLabel.setText("Aucun horaire");
            if(!firstTime)
                mSchedulesLabel.setTextColor(getResources().getColor(R.color.error));
        }
    }
}
