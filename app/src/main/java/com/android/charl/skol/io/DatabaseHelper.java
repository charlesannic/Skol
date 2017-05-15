package com.android.charl.skol.io;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.android.charl.skol.R;
import com.android.charl.skol.java.Color;
import com.android.charl.skol.java.Course;
import com.android.charl.skol.java.Schedule;
import com.android.charl.skol.java.Teacher;

import java.util.ArrayList;

/**
 * Created by charl on 25/05/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String DATABASE_NAME = "skol_database";
    private static final int DATABASE_VERSION = 1;

    private static Context context;
    private SQLiteDatabase database;
    private static DatabaseHelper helper;

    public CourseHelper courseHelper;
    public HomeworkHelper homeworkHelper;
    public NoteHelper noteHelper;
    public ScheduleHelper scheduleHelper;
    public TeacherHelper teacherHelper;
    public HomeworkCategoryHelper homeworkCategoryHelper;
    public CourseHomeworkHelper courseHomeworkHelper;
    public CourseSchedulesHelper courseSchedulesHelper;

    private DatabaseHelper(Context c) {
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
        context = c;
        database = getWritableDatabase();

        initHelpers();
    }

    public static DatabaseHelper getInstance(Context context) {
        if (helper == null) {
            helper = new DatabaseHelper(context);
        }
        return helper;
    }

    private void initHelpers() {
        Log.i(TAG, "initHelpers: ");
        if (courseHelper == null)
            courseHelper = new CourseHelper(this, database);
        if (homeworkHelper == null)
            homeworkHelper = new HomeworkHelper(this, database);
        if (noteHelper == null)
            noteHelper = new NoteHelper(this, database);
        if (scheduleHelper == null)
            scheduleHelper = new ScheduleHelper(this, database);
        if (teacherHelper == null)
            teacherHelper = new TeacherHelper(this, database);
        if (courseHomeworkHelper == null)
            courseHomeworkHelper = new CourseHomeworkHelper(this, database);
        if (homeworkCategoryHelper == null)
            homeworkCategoryHelper = new HomeworkCategoryHelper(this, database);
        if (courseSchedulesHelper == null)
            courseSchedulesHelper = new CourseSchedulesHelper(this, database);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Création de la Base de données
        if (database == null)
            database = db;
        //Création des tables
        db.execSQL(Database.Color.CREATE_TABLE);
        db.execSQL(Database.Course.CREATE_TABLE);
        db.execSQL(Database.Homework.CREATE_TABLE);
        db.execSQL(Database.Note.CREATE_TABLE);
        db.execSQL(Database.Schedule.CREATE_TABLE);
        db.execSQL(Database.Teacher.CREATE_TABLE);
        db.execSQL(Database.HomeworkCategory.CREATE_TABLE);
        db.execSQL(Database.CourseHomework.CREATE_TABLE);
        db.execSQL(Database.CourseSchedules.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Database.Color.DROP_TABLE);
        db.execSQL(Database.Course.DROP_TABLE);
        db.execSQL(Database.Homework.DROP_TABLE);
        db.execSQL(Database.Note.DROP_TABLE);
        db.execSQL(Database.Schedule.DROP_TABLE);
        db.execSQL(Database.Teacher.DROP_TABLE);
        db.execSQL(Database.HomeworkCategory.DROP_TABLE);
        db.execSQL(Database.CourseHomework.DROP_TABLE);
        db.execSQL(Database.CourseSchedules.DROP_TABLE);

        onCreate(db);
    }

    /*public static class ColorHelper {

        private DatabaseHelper databaseHelper;
        private static SQLiteDatabase database;

        ColorHelper(DatabaseHelper databaseHelper, SQLiteDatabase database) {
            this.databaseHelper = databaseHelper;
            this.database = database;
        }

        public static long addColor(Color color) {
            ContentValues values = new ContentValues();
            values.put(Database.Color.ID, color.getId());
            values.put(Database.Color.COLOR_NAME, color.getColorName());

            return database.insert(Database.Color.TABLE_NAME, null, values);
        }

        public static Color getColor(long id) {

            Cursor c = database.rawQuery(String.format(Database.Color.GET_COLOR, id), null);

            if (c.moveToFirst()) {
                int[] colors = getResources().getIntArray(R.array.colors);

                Color color = new Color(c.getInt(1),
                        c.getString(2));
                c.close();

                return color;
            }

            c.close();

            return null;
        }


    }*/

    public static class CourseHelper {

        private DatabaseHelper databaseHelper;
        private static SQLiteDatabase database;

        CourseHelper(DatabaseHelper databaseHelper, SQLiteDatabase database) {
            this.databaseHelper = databaseHelper;
            this.database = database;
        }

        public static long addCourse(Course course) {
            ContentValues values = new ContentValues();
            values.put(Database.Course.NAME, course.getName());
            values.put(Database.Course.ID_COLOR, course.getColor().getId());
            values.put(Database.Course.ID_TEACHER, TeacherHelper.addTeacher(course.getTeacher()));
            values.put(Database.Course.COEFFICIENT, course.getCoefficient());
            values.put(Database.Course.NOTIFICATION, course.getNotification());

            long idCourse = database.insert(Database.Course.TABLE_NAME, null, values);

            CourseSchedulesHelper.addCourseSchedules(idCourse, course.getSchedules());

            return idCourse;
        }

        public static ArrayList<Course> getAllCourses() {
            ArrayList<Course> courses = new ArrayList<>();

            Log.i(TAG, "getAllCourses: " + Database.Course.GET_ALL_COURSES);
            Cursor c = database.rawQuery(Database.Course.GET_ALL_COURSES, null);


            if (c.moveToFirst()) {
                int[] colors = context.getResources().getIntArray(R.array.colors);
                String[] colorsNames = context.getResources().getStringArray(R.array.colors_names);
                do {
                    Log.i(TAG, "getAllCourses: loop");

                    Course course = new Course(c.getLong(0),
                            c.getString(1),
                            new Color(colors[c.getInt(2)], colorsNames[c.getInt(2)], colors[c.getInt(2)]),
                            CourseSchedulesHelper.getCourseSchedules(c.getLong(3)),
                            TeacherHelper.getTeacher(c.getLong(4)),
                            c.getDouble(5),
                            c.getInt(6));

                    courses.add(course);
                } while (c.moveToNext());
            }

            c.close();

            return courses;
        }

        public static Course getCourse(long id) {

            Cursor c = database.rawQuery(String.format(Database.Course.GET_COURSE, id), null);

            if (c.moveToFirst()) {

                int[] colors = context.getResources().getIntArray(R.array.colors);
                String[] colorsNames = context.getResources().getStringArray(R.array.colors_names);

                Course course = new Course(c.getLong(0),
                        c.getString(1),
                        new Color(
                                colors[c.getInt(2)],
                                colorsNames[c.getInt(2)],
                                colors[c.getInt(2)]),
                        null,
                        TeacherHelper.getTeacher(c.getLong(3)),
                        c.getDouble(4),
                        c.getInt(5));

                c.close();

                return course;
            }

            c.close();

            return null;
        }

    }

    public static class HomeworkHelper {

        private DatabaseHelper databaseHelper;
        private SQLiteDatabase database;

        HomeworkHelper(DatabaseHelper databaseHelper, SQLiteDatabase database) {
            this.databaseHelper = databaseHelper;
            this.database = database;
        }

    }

    public static class NoteHelper {

        private DatabaseHelper databaseHelper;
        private SQLiteDatabase database;

        NoteHelper(DatabaseHelper databaseHelper, SQLiteDatabase database) {
            this.databaseHelper = databaseHelper;
            this.database = database;
        }

    }

    public static class ScheduleHelper {

        private DatabaseHelper databaseHelper;
        private static SQLiteDatabase database;

        ScheduleHelper(DatabaseHelper databaseHelper, SQLiteDatabase database) {
            this.databaseHelper = databaseHelper;
            this.database = database;
        }

        public static long addSchedule(Schedule schedule) {
            ContentValues values = new ContentValues();
            values.put(Database.Schedule.DAY, schedule.getDay());
            values.put(Database.Schedule.START_TIME, schedule.getStartTime());
            values.put(Database.Schedule.END_TIME, schedule.getEndTime());
            values.put(Database.Schedule.LOCATION, schedule.getLocation());

            return database.insert(Database.Schedule.TABLE_NAME, null, values);
        }

        public static ArrayList<Schedule> getAllSchedules() {
            ArrayList<Schedule> schedules = new ArrayList<>();

            Cursor c = database.rawQuery(Database.Schedule.GET_ALL_SCHEDULES, null);


            if (c.moveToFirst()) {
                do {
                    Log.i(TAG, "getAllCourses: loop");
                    Schedule schedule = new Schedule(c.getLong(0),
                            c.getLong(1),
                            c.getInt(2),
                            c.getString(3),
                            c.getString(4),
                            c.getString(5));

                    schedules.add(schedule);
                } while (c.moveToNext());
            }

            c.close();

            return schedules;
        }

    }

    public static class TeacherHelper {

        private DatabaseHelper databaseHelper;
        private static SQLiteDatabase database;

        TeacherHelper(DatabaseHelper databaseHelper, SQLiteDatabase database) {
            this.databaseHelper = databaseHelper;
            this.database = database;
        }

        public static long addTeacher(Teacher teacher) {
            ContentValues values = new ContentValues();
            values.put(Database.Teacher.NAME, teacher.getName());
            values.put(Database.Teacher.EMAIL, teacher.getEmail());

            return database.insert(Database.Teacher.TABLE_NAME, null, values);
        }

        public static Teacher getTeacher(long id) {

            Cursor c = database.rawQuery(String.format(Database.Teacher.GET_TEACHER, id), null);

            if (c.moveToFirst()) {
                Teacher teacher = new Teacher(c.getLong(0),
                        c.getString(1),
                        c.getString(2));
                c.close();

                return teacher;
            }

            c.close();

            return null;
        }

    }

    public static class HomeworkCategoryHelper {

        private DatabaseHelper databaseHelper;
        private static SQLiteDatabase database;

        HomeworkCategoryHelper(DatabaseHelper databaseHelper, SQLiteDatabase database) {
            this.databaseHelper = databaseHelper;
            this.database = database;
        }

    }

    public static class CourseHomeworkHelper {

        private DatabaseHelper databaseHelper;
        private static SQLiteDatabase database;

        CourseHomeworkHelper(DatabaseHelper databaseHelper, SQLiteDatabase database) {
            this.databaseHelper = databaseHelper;
            this.database = database;
        }

    }

    public static class CourseSchedulesHelper {

        private DatabaseHelper databaseHelper;
        private static SQLiteDatabase database;

        CourseSchedulesHelper(DatabaseHelper databaseHelper, SQLiteDatabase database) {
            this.databaseHelper = databaseHelper;
            this.database = database;
        }

        public static void addCourseSchedules(double idCourse, ArrayList<Schedule> schedules) {
            for (int i = 0; i < schedules.size(); i++) {
                ContentValues values = new ContentValues();
                values.put(Database.CourseSchedules.ID_COURSE, idCourse);
                values.put(Database.CourseSchedules.ID_SCHEDULE, ScheduleHelper.addSchedule(schedules.get(i)));

                database.insert(Database.CourseSchedules.TABLE_NAME, null, values);
            }
        }


        public static ArrayList<Schedule> getCourseSchedules(long id) {
            ArrayList<Schedule> schedules = new ArrayList<>();

            Cursor c = database.rawQuery(String.format(Database.CourseSchedules.GET_COURSE_SCHEDULE, id), null);

            if (c.moveToFirst()) {
                do {
                    Schedule schedule = new Schedule(c.getLong(0),
                            id,
                            c.getInt(1),
                            c.getString(2),
                            c.getString(3),
                            c.getString(4));
                    Log.i(TAG, "getCourseSchedules: LAAAAAAA " + id + " " + schedule.getId());

                    schedules.add(schedule);
                } while (c.moveToNext());
            }

            c.close();

            return schedules;
        }

    }

}
