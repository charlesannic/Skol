package com.android.charl.skol.io;

import android.provider.BaseColumns;

/**
 * Created by charl on 25/05/2016.
 */
public class Database {

    // Management instructions
    private static final String CRT_TBL = " CREATE TABLE IF NOT EXISTS ";
    private static final String INT_PK = " INTEGER PRIMARY KEY AUTOINCREMENT ";
    private static final String TEXT_PK = " TEXT PRIMARY KEY ";
    private static final String REF = " REFERENCES ";
    private static final String CST = " CONSTRAINT ";
    private static final String CST_PK = " CONSTRAINT PRIMARY KEY ";
    private static final String TEXT = " TEXT ";
    private static final String INT = " INTEGER ";
    private static final String DOUBLE = " DOUBLE ";

    // Search instructions
    private static final String SELECT = " SELECT ";
    private static final String DISTINCT = " DISTINCT ";
    private static final String FROM = " FROM ";
    private static final String WHERE = " WHERE ";
    private static final String AND = " AND ";
    private static final String OR = " OR ";
    private static final String LIKE = " LIKE ";
    private static final String IN = " IN ";
    private static final String UPPER = " UPPER ";
    private static final String ORDER_BY = " ORDER BY ";

    public static class Color implements BaseColumns {
        // Table name
        public static final String TABLE_NAME = "Color";

        // FollowTest columns
        public static final String ID = "idColor";
        public static final String COLOR_NAME = "colorName";

        public static final String CREATE_TABLE = CRT_TBL + TABLE_NAME
                + " ("
                + ID + INT_PK + ", "
                + COLOR_NAME + TEXT + "); ";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + "; ";

        public static final String GET_COLOR = SELECT + "*"
                + FROM + TABLE_NAME
                + WHERE + ID + "=%s ;";

    }

    public static class Course implements BaseColumns {

        public static final String TABLE_NAME = "Course";

        public static final String ID = "idCourse";
        public static final String NAME = "nameCourse";
        public static final String ID_COLOR = "idColorCourse";
        public static final String ID_TEACHER = "idTeacherCourse";
        public static final String COEFFICIENT = "coefficientCourse";
        public static final String NOTIFICATION = "notificationCourse";

        public static final String CREATE_TABLE = CRT_TBL + TABLE_NAME
                + " ("
                + ID + INT_PK + ", "
                + NAME + TEXT + ", "
                + ID_COLOR + DOUBLE + REF + Color.TABLE_NAME + ", "
                + ID_TEACHER + DOUBLE + REF + Teacher.TABLE_NAME + ", "
                + COEFFICIENT + DOUBLE + ", "
                + NOTIFICATION + INT + "); ";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + "; ";

        public static final String GET_ALL_COURSES = SELECT + TABLE_NAME + "." + ID + ", " + NAME + ", " + ID_COLOR + ", " + CourseSchedules.ID_SCHEDULE + ", " + ID_TEACHER + ", " + COEFFICIENT + ", " + NOTIFICATION
                + FROM + TABLE_NAME + ", " + CourseSchedules.TABLE_NAME
                + WHERE + TABLE_NAME + "." + ID + " = " + CourseSchedules.TABLE_NAME + "." + CourseSchedules.ID_COURSE
                + ORDER_BY + TABLE_NAME + "." + ID + " ;";

        public static final String GET_COURSE = SELECT + "*"
                + FROM + TABLE_NAME
                + WHERE + ID + "=%s ;";

    }

    public static class Homework implements BaseColumns {

        public static final String TABLE_NAME = "Homework";

        public static final String ID = "idHomework";
        public static final String NAME = "nameHomework";
        public static final String DESCRIPTION = "descriptionHomework";
        public static final String ID_CATEGORIE = "idCategorieHomework";
        public static final String IMPORTANCE = "importanceHomework";
        public static final String DATE = "dateHomework";
        public static final String ID_NOTE = "idNoteHomework";

        public static final String CREATE_TABLE = CRT_TBL + TABLE_NAME
                + " ("
                + ID + INT_PK + ", "
                + NAME + TEXT + ", "
                + DESCRIPTION + TEXT + ", "
                + ID_CATEGORIE + DOUBLE + REF + HomeworkCategory.TABLE_NAME + ", "
                + IMPORTANCE + INT + ", "
                + DATE + DATE + ", "
                + ID_NOTE + DOUBLE + REF + Note.TABLE_NAME + "); ";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + "; ";

    }

    public static class Note implements BaseColumns {

        public static final String TABLE_NAME = "Note";

        public static final String ID = "idNote";
        public static final String NOTE = "note";
        public static final String COEFFICIENT = "coefficientNote";

        public static final String CREATE_TABLE = CRT_TBL + TABLE_NAME
                + " ("
                + ID + INT_PK + ", "
                + NOTE + DOUBLE + ", "
                + COEFFICIENT + INT + "); ";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + "; ";

    }

    public static class Schedule implements BaseColumns {

        public static final String TABLE_NAME = "Schedule";

        public static final String ID = "idSchedule";
        public static final String DAY = "daySchedule";
        public static final String START_TIME = "startTimeSchedule";
        public static final String END_TIME = "endTimeSchedule";
        public static final String LOCATION = "locationSchedule";

        public static final String CREATE_TABLE = CRT_TBL + TABLE_NAME
                + " ("
                + ID + INT_PK + ", "
                + DAY + INT + ", "
                + START_TIME + TEXT + ", "
                + END_TIME + TEXT + ", "
                + LOCATION + TEXT + "); ";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + "; ";

        public static final String GET_ALL_SCHEDULES = SELECT + TABLE_NAME + "." + ID + ", " + CourseSchedules.ID_COURSE + ", " + DAY + ", " + START_TIME + ", " + END_TIME + ", " + LOCATION
                + FROM + TABLE_NAME + ", " + CourseSchedules.TABLE_NAME
                + WHERE + TABLE_NAME + "." + ID + " = " + CourseSchedules.TABLE_NAME + "." + CourseSchedules.ID_SCHEDULE
                + ORDER_BY + DAY + ", " + START_TIME + ", " + END_TIME + " ;";

    }

    public static class Teacher implements BaseColumns {

        public static final String TABLE_NAME = "Teacher";

        public static final String ID = "idTeacher";
        public static final String NAME = "nameTeacher";
        public static final String EMAIL = "emailTeacher";

        public static final String CREATE_TABLE = CRT_TBL + TABLE_NAME
                + " ("
                + ID + INT_PK + ", "
                + NAME + TEXT + ", "
                + EMAIL + TEXT + "); ";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + "; ";

        public static final String GET_TEACHER = SELECT + "*"
                + FROM + TABLE_NAME
                + WHERE + ID + "=%s ;";

    }

    public static class HomeworkCategory implements BaseColumns {

        public static final String TABLE_NAME = "HomeworkCategory";

        public static final String ID_CATEGORY = "idCategory";
        public static final String NAME_CATEGORY = "nameCategory";

        public static final String CREATE_TABLE = CRT_TBL + TABLE_NAME
                + " ("
                + ID_CATEGORY + INT_PK + ", "
                + NAME_CATEGORY + TEXT + "); ";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + "; ";

    }

    public static class CourseHomework implements BaseColumns {

        public static final String TABLE_NAME = "CourseHomework";

        public static final String ID_COURSE = "idCourse";
        public static final String ID_HOMEWORK = "idHomework";

        public static final String CREATE_TABLE = CRT_TBL + TABLE_NAME
                + " ("
                + ID_COURSE + DOUBLE + REF + Course.TABLE_NAME + ", "
                + ID_HOMEWORK + DOUBLE + REF + Homework.TABLE_NAME + "); ";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + "; ";

    }

    public static class CourseSchedules implements BaseColumns {

        public static final String TABLE_NAME = "CourseSchedules";

        public static final String ID_COURSE = "idCourse";
        public static final String ID_SCHEDULE = "idSchedule";

        public static final String CREATE_TABLE = CRT_TBL + TABLE_NAME
                + " ("
                + ID_COURSE + DOUBLE + REF + Course.TABLE_NAME + ", "
                + ID_SCHEDULE + DOUBLE + REF + Schedule.TABLE_NAME + "); ";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + "; ";

        public static final String GET_COURSE_SCHEDULE = SELECT + Schedule.TABLE_NAME + "." + Schedule.ID + ", " + Schedule.DAY + ", " + Schedule.START_TIME + ", " + Schedule.END_TIME + ", " + Schedule.LOCATION
                + FROM + TABLE_NAME + ", " + Schedule.TABLE_NAME
                + WHERE + ID_COURSE + "=%s "
                + AND + TABLE_NAME + "." + ID_SCHEDULE + " = " + Schedule.TABLE_NAME + "." + Schedule.ID + " ;";

    }

}
