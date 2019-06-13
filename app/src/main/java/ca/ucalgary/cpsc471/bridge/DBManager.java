package ca.ucalgary.cpsc471.bridge;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper {

    // Define the database name.
    public static final String DB_NAME = "dental.db";

    public DBManager(Context context) {
        super(context, DB_NAME, null, 1);

        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    // Can include at the end of 'PRIMARY KEY' the word 'AUTOINCREMENT' to generate a SIN value + 1 even without parameters.
    public void onCreate(SQLiteDatabase db) {
        createTables(db);

    }

    @Override
    // Drops any existing table and recreates it.
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
        onCreate(db);

    }

    public boolean signInPatient(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * from patient WHERE ID = ?",new String[] { id });
        if(res.getCount() == 0) {
            res.close();
            return false;
        }
        res.close();
        return true;
    }

    public boolean signInDentist(String SIN){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * from dentist WHERE SIN = ?",new String[] { SIN });
        if(res.getCount() == 0) {
            res.close();
            return false;
        }
        res.close();
        return true;
    }

    public Cursor viewPatientInfo(String ID){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * from patient WHERE ID = ?",new String[] { ID });
    }

    public Cursor viewDentistInfo(String SIN){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * from dentist WHERE SIN = ?",new String[] { SIN });
    }

    // Tables can only have one primary key. Others commented out until an alternative solution is found.
    private void createTables(SQLiteDatabase db){
        // Employee
        db.execSQL("CREATE TABLE employee (" +
                "SIN                    INTEGER     PRIMARY KEY," +
                "FirstName              TEXT," +
                "MiddleName             TEXT," +
                "LastName               TEXT," +
                "StreetNumber           INTEGER," +
                "Unit                   INTEGER," +
                "PostalCode             TEXT," +
                "City                   TEXT," +
                "Province               TEXT," +
                "DateOfBirth            TEXT," +
                "Salary                 INTEGER," +
                "Sex                    TEXT," +
                "AdminSIN               INTEGER," +                     // FK: Administration
                "AppointedClinicName    TEXT)");                        // FK: Clinic

        // Dependent
        db.execSQL("CREATE TABLE dependent (" +
                "SIN                    INTEGER     PRIMARY KEY," +     // FK: Employee
                "FirstName              TEXT        /*PRIMARY KEY*/," +
                "MiddleName             TEXT        /*PRIMARY KEY*/," +
                "LastName               TEXT        /*PRIMARY KEY*/," +
                "DateOfBirth            TEXT," +
                "Sex                    TEXT," +
                "Relationship           TEXT)");

        // Administration
        db.execSQL("CREATE TABLE administration (" +
                "SIN                    INTEGER     PRIMARY KEY," +     // FK: Employee
                "Seniority              TEXT)");

        // Hygienist
        db.execSQL("CREATE TABLE hygienist (" +
                "SIN                    INTEGER     PRIMARY KEY," +     // FK: Employee
                "YearsOfExperience      INTEGER)");

        // Dental Assistant
        db.execSQL("CREATE TABLE dentalAssistant (" +
                "SIN                    INTEGER     PRIMARY KEY," +     // FK: Employee
                "AssignedDentistSIN     INTEGER," +                     // FK: Dentist
                "YearsOfExperience      INTEGER)");

        // Dentist
        db.execSQL("CREATE TABLE dentist (" +
                "SIN                    INTEGER     PRIMARY KEY," +     // FK: Employee
                "TypeOfPractice         TEXT)");

        // Clinic
        db.execSQL("CREATE TABLE clinic (" +
                "Name                   TEXT        PRIMARY KEY," +
                "StreetNumber           INTEGER," +
                "Unit                   INTEGER," +
                "PostalCode             TEXT," +
                "City                   TEXT," +
                "Province               TEXT," +
                "AdminSIN               INTEGER)");                     // FK: Administration

        // Room
        db.execSQL("CREATE TABLE room (" +
                "ClinicName             TEXT        PRIMARY KEY," +     // FK: Clinic
                "Number                 INTEGER)");

        // Room Size
        db.execSQL("CREATE TABLE roomSize (" +
                "ClinicName             TEXT        PRIMARY KEY," +     // FK: Room
                "Number                 INTEGER     /*PRIMARY KEY*/," +     // FK: Room
                "Size                   INTEGER     /*PRIMARY KEY*/)");

        // Resource
        db.execSQL("CREATE TABLE resource (" +
                "ID                     INTEGER     PRIMARY KEY," +
                "Quantity               INTEGER," +
                "Supplier               TEXT," +
                "ClinicName             TEXT," +                        // FK: Room
                "RoomNumber             INTEGER)");                     // FK: Room

        // Patient
        db.execSQL("CREATE TABLE patient (" +
                "ID                     INTEGER     PRIMARY KEY," +
                "FirstName              TEXT," +
                "MiddleName             TEXT," +
                "LastName               TEXT," +
                "StreetNumber           INTEGER," +
                "Unit                   INTEGER," +
                "PostalCode             TEXT," +
                "City                   TEXT," +
                "Province               TEXT," +
                "DateOfBirth            TEXT," +
                "InsuranceNumber        INTEGER," +
                "Sex                    TEXT," +
                "AssignedDentistSIN     INTEGER)");                     // FK: Dentist

        // Appointment
        db.execSQL("CREATE TABLE appointment (" +
                "ID                     INTEGER     PRIMARY KEY," +
                "StartTime              TEXT," +
                "EndTime                TEXT," +
                "AppointmentType        TEXT," +
                "AppointmentClinicName  TEXT," +                        // FK: Room
                "AppointRoomNumber      INTEGER," +                     // FK: Room
                "AppointPatientID       INTEGER)");                     // FK: Patient

        // Appointment Resources
        db.execSQL("CREATE TABLE appointmentResources (" +
                "ID                     INTEGER     PRIMARY KEY," +     // FK: Appointment
                "Resource               INTEGER     /*PRIMARY KEY*/)");     // Not a foreign key. No relation added in the model. Treating it as Resource.ID

        // Other
        db.execSQL("CREATE TABLE other (" +
                "ID                     INTEGER     PRIMARY KEY," +     // FK: Appointment
                "AttendingDentistSIN    INTEGER," +                     // FK: Dentist
                "AttendingAssistantSIN  INTEGER)");                     // FK: Dental Assistant

        // Cleaning
        db.execSQL("CREATE TABLE cleaning (" +
                "ID                     INTEGER     PRIMARY KEY," +     // FK: Appointment
                "AttendingHygienistSIN  INTEGER)");                     // FK: Hygienist

    }
}
