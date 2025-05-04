package com.example.collegemanagement;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "CollegeManagement.db";
    public static final int DATABASE_VERSION = 1;

    // User Table
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD = "password";
    public static final String COLUMN_USER_ROLE = "role"; // Admin, Student, Teacher

    // Documents Table
    public static final String TABLE_DOCUMENTS = "documents";
    public static final String COLUMN_DOC_ID = "id";
    public static final String COLUMN_DOC_TITLE = "title";
    public static final String COLUMN_DOC_DESCRIPTION = "description";
    public static final String COLUMN_DOC_USER_ID = "user_id";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Reports Table
        String CREATE_REPORTS_TABLE = "CREATE TABLE reports (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "user_role TEXT, " +
                "title TEXT, " +
                "description TEXT, " +
                "status TEXT" +
                ");";
        db.execSQL(CREATE_REPORTS_TABLE);

        // Create Users Table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " ("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USER_NAME + " TEXT, "
                + COLUMN_USER_EMAIL + " TEXT UNIQUE, "
                + COLUMN_USER_PASSWORD + " TEXT, "
                + COLUMN_USER_ROLE + " TEXT"
                + ");";

        // Create Documents Table
        String CREATE_DOCUMENTS_TABLE = "CREATE TABLE " + TABLE_DOCUMENTS + " ("
                + COLUMN_DOC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_DOC_TITLE + " TEXT, "
                + COLUMN_DOC_DESCRIPTION + " TEXT, "
                + COLUMN_DOC_USER_ID + " INTEGER, "
                + "FOREIGN KEY(" + COLUMN_DOC_USER_ID + ") REFERENCES "
                + TABLE_USERS + "(" + COLUMN_USER_ID + "))";

        // Create Teachers Table
        String CREATE_TEACHERS_TABLE = "CREATE TABLE teachers ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT, "
                + "address TEXT, "
                + "dob TEXT, "
                + "salary TEXT, "
                + "email TEXT, "
                + "password TEXT)";

        // Create Students Table
        String CREATE_STUDENTS_TABLE = "CREATE TABLE students ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT, "
                + "address TEXT, "
                + "dob TEXT, "
                + "course TEXT, "
                + "roll_number TEXT, "
                + "email TEXT, "
                + "password TEXT)";

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_DOCUMENTS_TABLE);
        db.execSQL(CREATE_TEACHERS_TABLE);
        db.execSQL(CREATE_STUDENTS_TABLE);

        // Insert default Admin user
        ContentValues admin = new ContentValues();
        admin.put(COLUMN_USER_NAME, "Admin");
        admin.put(COLUMN_USER_EMAIL, "admin@admin.com");
        admin.put(COLUMN_USER_PASSWORD, "Admin@123");
        admin.put(COLUMN_USER_ROLE, "admin");

        db.insert(TABLE_USERS, null, admin); //

        // Inside onCreate() method of DBHelper.java
        String CREATE_MARKS_TABLE = "CREATE TABLE marks (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "student_id INTEGER, " +
                "module_name TEXT, " +
                "td_mark REAL, " +
                "tp_mark REAL, " +
                "exam_mark REAL, " +
                "coefficient INTEGER" +
                ");";

        db.execSQL(CREATE_MARKS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCUMENTS);
        db.execSQL("DROP TABLE IF EXISTS teachers");
        db.execSQL("DROP TABLE IF EXISTS students");
        db.execSQL("DROP TABLE IF EXISTS marks");

        onCreate(db);
    }

    // User login
    public Cursor checkUserCredentials(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USERS, null, COLUMN_USER_EMAIL + "=? AND " + COLUMN_USER_PASSWORD + "=?",
                new String[]{email, password}, null, null, null);
    }

    // Insert new user
    public boolean insertUser(String name, String email, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USER_NAME, name);
        contentValues.put(COLUMN_USER_EMAIL, email);
        contentValues.put(COLUMN_USER_PASSWORD, password);
        contentValues.put(COLUMN_USER_ROLE, role);

        long result = db.insert(TABLE_USERS, null, contentValues);
        return result != -1;
    }

    // Insert teacher
    public boolean insertTeacher(String name, String address, String dob, String salary, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("address", address);
        values.put("dob", dob);
        values.put("salary", salary);
        values.put("email", email);
        values.put("password", password);
        long result = db.insert("teachers", null, values);
        return result != -1;
    }

    // Insert student
    public boolean insertStudent(String name, String address, String dob, String course, String rollNumber, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("address", address);
        values.put("dob", dob);
        values.put("course", course);
        values.put("roll_number", rollNumber);
        values.put("email", email);
        values.put("password", password);
        long result = db.insert("students", null, values);
        return result != -1;
    }

    // Get all teachers
    public Cursor getAllTeachers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM teachers", null);
    }

    // Get all students
    public Cursor getAllStudents() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM students", null);
    }

    // Delete teacher by ID
    public boolean deleteTeacher(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("teachers", "id=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    // Delete student by ID
    public boolean deleteStudent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("students", "id=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    // Get single teacher by ID
    public Cursor getTeacherById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM teachers WHERE id = ?", new String[]{String.valueOf(id)});
    }

    // Get single student by ID
    public Cursor getStudentById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM students WHERE id = ?", new String[]{String.valueOf(id)});
    }

    // Update teacher
    public boolean updateTeacher(int id, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.update("teachers", values, "id=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    // Update student
    public boolean updateStudent(int id, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.update("students", values, "id=?", new String[]{String.valueOf(id)});
        return result > 0;
    }
    public Cursor checkStudentCredentials(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query("students", null, "email=? AND password=?", new String[]{email, password}, null, null, null);
    }
    // Check if teacher exists (based on email and password)
    public Cursor checkTeacherCredentials(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query("teachers", null, "email=? AND password=?", new String[]{email, password}, null, null, null);
    }

    // Insert new marks for a student
    public boolean insertMark(int studentId, String moduleName, double td, double tp, double exam, int coefficient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("student_id", studentId);
        values.put("module_name", moduleName);
        values.put("td_mark", td);
        values.put("tp_mark", tp);
        values.put("exam_mark", exam);
        values.put("coefficient", coefficient);

        long result = db.insert("marks", null, values);
        return result != -1;
    }

    // Get all marks for a student
    public Cursor getMarksForStudent(int studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM marks WHERE student_id=?", new String[]{String.valueOf(studentId)});
    }
    // Get student by email
    // Get student by email
    public Cursor getStudentByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM students WHERE email = ?", new String[]{email});
    }

    // Get teacher by email
    public Cursor getTeacherByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM teachers WHERE email = ?", new String[]{email});
    }
// Insert report (already handled in SendReportActivity, optional here if needed later)

    // Get all reports (for Admin)
    public Cursor getAllReports() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM reports", null);
    }

    // Update report status
    public boolean updateReportStatus(int reportId, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", newStatus);

        int rows = db.update("reports", values, "id=?", new String[]{String.valueOf(reportId)});
        return rows > 0;
    }

    // Insert a new report
    public boolean insertReport(int userId, String userRole, String title, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("user_role", userRole);
        values.put("title", title);
        values.put("description", description);
        values.put("status", "Pending"); // Always start with Pending

        long result = db.insert("reports", null, values);
        return result != -1;
    }


}
