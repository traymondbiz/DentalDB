package ca.ucalgary.cpsc471.bridge;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBManager extends SQLiteOpenHelper {

    // Define the database name.
    public static final String DB_NAME = "dental.db";
    private static String DB_PATH = "";
    private SQLiteDatabase db;
    private final Context mainContext;
    
    public DBManager(Context context) {
        super(context, DB_NAME, null, 1);
        DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        this.mainContext = context;

        db = this.getWritableDatabase();
    }

    // Attempts to create the database if it doesn't exist yet.
    public void createDatabase() throws IOException {
        boolean dbExists = checkDatabase();
        if (!dbExists){
            this.getReadableDatabase();
            this.close();

            try {
                copyDatabase();
                Toast.makeText(mainContext, "Created a new copy of the database using assets.", Toast.LENGTH_LONG).show();
            }
            catch (IOException e){
                Toast.makeText(mainContext, "Error in attempting to copy database from assets.", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(mainContext, "Using existing database.", Toast.LENGTH_LONG).show();
        }
    }

    // Determines if the database file exists or not.
    private boolean checkDatabase(){
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    // Reads from assets to create a copy of the database for use.
    private void copyDatabase() throws IOException {
        InputStream input = mainContext.getAssets().open(DB_NAME);
        String outputFileName = DB_PATH + DB_NAME;
        OutputStream output = new FileOutputStream(outputFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = input.read(buffer)) > 0){
            output.write(buffer, 0, length);
        }
        output.flush();
        output.close();
        input.close();
    }

    // Returns true if database successfully opened.
    public boolean openDatabase() throws SQLException{
        String path = DB_PATH + DB_NAME;
        db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        return db != null;
    }

    @Override
    public synchronized  void close(){
        if (db != null){
            db.close();
            super.close();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Do nothing.
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Do nothing.
    }

//    // Returns true if patient ID exists in database
//    public boolean signInPatient(String id){
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor res = db.rawQuery("SELECT * from patient WHERE ID = ?",new String[] { id });
//        if(res.getCount() == 0) {
//            res.close();
//            return false;
//        }
//        res.close();
//        return true;
//    }
//
//    // Returns true if dentist SIN exists in database
//    public boolean signInDentist(String SIN){
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor res = db.rawQuery("SELECT * from dentist WHERE SIN = ?",new String[] { SIN });
//        if(res.getCount() == 0) {
//            res.close();
//            return false;
//        }
//        res.close();
//        return true;
//    }
//
//    // Returns cursor with required patient info
//    public Cursor viewPatientInfo(String ID){
//        SQLiteDatabase db = this.getWritableDatabase();
//        return db.rawQuery("SELECT * from patient WHERE ID = ?",new String[] { ID });
//    }
//
//    // Returns cursor with required dentist info
//    public Cursor viewDentistInfo(String SIN){
//        SQLiteDatabase db = this.getWritableDatabase();
//        return db.rawQuery("SELECT * from dentist WHERE SIN = ?",new String[] { SIN });
//    }
//
//    // Returns cursor with patient's appointments
//    public Cursor viewPatientAppointments(String ID){
//        return db.rawQuery("SELECT * from appointment WHERE AppointPatientID = ?",new String[] { ID });
//    }
//
//    //Returns cursor with dentist's appointments
//    public Cursor viewDentistAppointments(String SIN){
//        SQLiteDatabase db = this.getWritableDatabase();
//        return db.rawQuery("SELECT * from appointment WHERE ID IN (SELECT ID from other WHERE AttendingDentistSIN = ?)",new String[] { SIN });
//    }
//
//    //Returns true if appointment is booked
//    public boolean bookAppointment(String patientID,String startTime, String appointmentType,String appointmentClinicName){
//        String assignedSIN;
//        String assignedClinic;
//        String roomNumber;
//        SQLiteDatabase db = this.getWritableDatabase();
//        if(appointmentType == "cleaning"){
//            assignedSIN = (db.rawQuery("SELECT AssignedHygienistSIN from patient WHERE ID = ?",new String[] { patientID })).getString(0);
//            roomNumber = (db.rawQuery("SELECT AssignedRoom from Hygienist WHERE SIN = ?",new String[] { assignedSIN })).getString(0);
//            assignedClinic = (db.rawQuery("SELECT AppointedClinicName from Employee WHERE SIN = ?",new String[] { assignedSIN })).getString(0);
//        }
//        else{
//            assignedSIN = (db.rawQuery("SELECT AssignedDentistSIN from patient WHERE ID = ?",new String[] { patientID })).getString(0);
//            roomNumber = (db.rawQuery("SELECT AssignedRoom from Dentist WHERE SIN = ?",new String[] { assignedSIN })).getString(0);
//            assignedClinic = (db.rawQuery("SELECT AppointedClinicName from Employee WHERE SIN = ?",new String[] { assignedSIN })).getString(0);
//        }
//        if((appointmentAvailability(startTime, appointmentType, assignedSIN))==true){
//            ContentValues appointmentContentValues = new ContentValues();
//            appointmentContentValues.put("StartTime",startTime);
//            appointmentContentValues.put("AppointmentType",appointmentType);
//            appointmentContentValues.put("AppointmentClinicName",assignedClinic);
//            appointmentContentValues.put("AppointRoomNumber",roomNumber);
//            appointmentContentValues.put("AppointPatientID", patientID);
//            db.insert("appointment", null, appointmentContentValues);
//            if(appointmentType=="cleaning"){
//                ContentValues cleaningContentValues = new ContentValues();
//                cleaningContentValues.put("ID",(db.rawQuery("SELECT ID from appointment WHERE AppointPatientID = ? AND StartTime = ?",new String[] { patientID,startTime })).getString(0));
//                cleaningContentValues.put("AttendingHygienist",assignedSIN);
//                db.insert("cleaning", null, cleaningContentValues);
//            }
//            else{
//                ContentValues otherContentValues = new ContentValues();
//                otherContentValues.put("ID",(db.rawQuery("SELECT ID from appointment WHERE AppointPatientID = ? AND StartTime = ?",new String[] { patientID,startTime })).getString(0));
//                otherContentValues.put("AttendingDentistSIN",assignedSIN);
//                otherContentValues.put("AttendingAssistantSIN",(db.rawQuery("SELECT SIN from dentalAssistant WHERE AssignedDentistSIN = ?",new String[] { assignedSIN })).getString(0));;
//                db.insert("other", null, otherContentValues);
//            }
//            return true;
//        }
//        else
//            return false;
//    }
//
//    //Returns true if appointment available
//    public boolean appointmentAvailability(String startTime, String appointmentType, String assignedSIN){
//        Cursor res;
//        if (appointmentType == "cleaning"){
//            SQLiteDatabase db = this.getWritableDatabase();
//            res = db.rawQuery("SELECT * from appointment INNER JOIN cleaning ON appointment.ID = cleaning.ID WHERE StartTime = ? AND AttendingHygienistSIN = ?",new String[] { startTime, assignedSIN });
//        }
//        else {
//            SQLiteDatabase db = this.getWritableDatabase();
//            res = db.rawQuery("SELECT * from appointment INNER JOIN other ON appointment.ID = other.ID WHERE StartTime = ? AND AttendingDentistSIN = ?",new String[] { startTime, assignedSIN });
//        }
//        if (res.getCount()==0)
//            return true;
//        return false;
//    }
//
//    // Returns true once patient info is updated
//    public boolean updatePatientInfo(String ID, String FName, String MName, String LName, String StNo, String unit, String postalCode,String city, String province, String DOB, String sex, String insurance){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("FirstName",FName);
//        contentValues.put("MiddleName",MName);
//        contentValues.put("LastName",LName);
//        contentValues.put("StreetNumber",StNo);
//        contentValues.put("Unit",unit);
//        contentValues.put("PostalCode",postalCode);
//        contentValues.put("City",city);
//        contentValues.put("Province",province);
//        contentValues.put("DateOfBirth",DOB);
//        contentValues.put("Sex",sex);
//        contentValues.put("InsuranceNumber",insurance);
//        db.update("patient", contentValues, "ID = ?",new String[] { ID });
//        return true;
//    }
//
//    // Returns true once dentist info is updated
//    public boolean updateDentistInfo(String SIN, String FName, String MName, String LName, String StNo, String unit, String postalCode,String city, String province, String DOB, String sex){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("FirstName",FName);
//        contentValues.put("MiddleName",MName);
//        contentValues.put("LastName",LName);
//        contentValues.put("StreetNumber",StNo);
//        contentValues.put("Unit",unit);
//        contentValues.put("PostalCode",postalCode);
//        contentValues.put("City",city);
//        contentValues.put("Province",province);
//        contentValues.put("DateOfBirth",DOB);
//        contentValues.put("Sex",sex);
//        db.update("dentist", contentValues, "SIN = ?",new String[] { SIN });
//        return true;
//    }
    
    // Tables can only have one primary key. Others commented out until an alternative solution is found.
//    private void createTables(SQLiteDatabase db){
//        // Employee
//        db.execSQL("CREATE TABLE employee (" +
//                "SIN                    INTEGER     PRIMARY KEY," +
//                "FirstName              TEXT," +
//                "MiddleName             TEXT," +
//                "LastName               TEXT," +
//                "StreetNumber           INTEGER," +
//                "Unit                   INTEGER," +
//                "PostalCode             TEXT," +
//                "City                   TEXT," +
//                "Province               TEXT," +
//                "DateOfBirth            TEXT," +
//                "Salary                 INTEGER," +
//                "Sex                    TEXT," +
//                "AdminSIN               INTEGER," +                     // FK: Administration
//                "AppointedClinicName    TEXT)");                        // FK: Clinic
//
//        // Dependent
//        db.execSQL("CREATE TABLE dependent (" +
//                "SIN                    INTEGER     PRIMARY KEY," +     // FK: Employee
//                "FirstName              TEXT        /*PRIMARY KEY*/," +
//                "MiddleName             TEXT        /*PRIMARY KEY*/," +
//                "LastName               TEXT        /*PRIMARY KEY*/," +
//                "DateOfBirth            TEXT," +
//                "Sex                    TEXT," +
//                "Relationship           TEXT)");
//
//        // Administration
//        db.execSQL("CREATE TABLE administration (" +
//                "SIN                    INTEGER     PRIMARY KEY," +     // FK: Employee
//                "Seniority              TEXT)");
//
//        // Hygienist
//        db.execSQL("CREATE TABLE hygienist (" +
//                "SIN                    INTEGER     PRIMARY KEY," +     // FK: Employee
//                "YearsOfExperience      INTEGER)");
//
//        // Dental Assistant
//        db.execSQL("CREATE TABLE dentalAssistant (" +
//                "SIN                    INTEGER     PRIMARY KEY," +     // FK: Employee
//                "AssignedDentistSIN     INTEGER," +                     // FK: Dentist
//                "YearsOfExperience      INTEGER)");
//
//        // Dentist
//        db.execSQL("CREATE TABLE dentist (" +
//                "SIN                    INTEGER     PRIMARY KEY," +     // FK: Employee
//                "TypeOfPractice         TEXT)");
//
//        // Clinic
//        db.execSQL("CREATE TABLE clinic (" +
//                "Name                   TEXT        PRIMARY KEY," +
//                "StreetNumber           INTEGER," +
//                "Unit                   INTEGER," +
//                "PostalCode             TEXT," +
//                "City                   TEXT," +
//                "Province               TEXT," +
//                "AdminSIN               INTEGER)");                     // FK: Administration
//
//        // Room
//        db.execSQL("CREATE TABLE room (" +
//                "ClinicName             TEXT        PRIMARY KEY," +     // FK: Clinic
//                "Number                 INTEGER)");
//
//        // Room Size
//        db.execSQL("CREATE TABLE roomSize (" +
//                "ClinicName             TEXT        PRIMARY KEY," +     // FK: Room
//                "Number                 INTEGER     /*PRIMARY KEY*/," +     // FK: Room
//                "Size                   INTEGER     /*PRIMARY KEY*/)");
//
//        // Resource
//        db.execSQL("CREATE TABLE resource (" +
//                "ID                     INTEGER     PRIMARY KEY," +
//                "Quantity               INTEGER," +
//                "Supplier               TEXT," +
//                "ClinicName             TEXT," +                        // FK: Room
//                "RoomNumber             INTEGER)");                     // FK: Room
//
//        // Patient
//        db.execSQL("CREATE TABLE patient (" +
//                "ID                     INTEGER     PRIMARY KEY," +
//                "FirstName              TEXT," +
//                "MiddleName             TEXT," +
//                "LastName               TEXT," +
//                "StreetNumber           INTEGER," +
//                "Unit                   INTEGER," +
//                "PostalCode             TEXT," +
//                "City                   TEXT," +
//                "Province               TEXT," +
//                "DateOfBirth            TEXT," +
//                "InsuranceNumber        INTEGER," +
//                "Sex                    TEXT," +
//                "AssignedDentistSIN     INTEGER)");                     // FK: Dentist
//
//        // Appointment
//        db.execSQL("CREATE TABLE appointment (" +
//                "ID                     INTEGER     PRIMARY KEY," +
//                "StartTime              TEXT," +
//                "EndTime                TEXT," +
//                "AppointmentType        TEXT," +
//                "AppointmentClinicName  TEXT," +                        // FK: Room
//                "AppointRoomNumber      INTEGER," +                     // FK: Room
//                "AppointPatientID       INTEGER)");                     // FK: Patient
//
//        // Appointment Resources
//        db.execSQL("CREATE TABLE appointmentResources (" +
//                "ID                     INTEGER     PRIMARY KEY," +     // FK: Appointment
//                "Resource               INTEGER     /*PRIMARY KEY*/)");     // Not a foreign key. No relation added in the model. Treating it as Resource.ID
//
//        // Other
//        db.execSQL("CREATE TABLE other (" +
//                "ID                     INTEGER     PRIMARY KEY," +     // FK: Appointment
//                "AttendingDentistSIN    INTEGER," +                     // FK: Dentist
//                "AttendingAssistantSIN  INTEGER)");                     // FK: Dental Assistant
//
//        // Cleaning
//        db.execSQL("CREATE TABLE cleaning (" +
//                "ID                     INTEGER     PRIMARY KEY," +     // FK: Appointment
//                "AttendingHygienistSIN  INTEGER)");                     // FK: Hygienist
//
//    }
}
