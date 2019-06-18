package ca.ucalgary.cpsc471.bridge;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.io.IOException;

public class DatabaseAdapter {
    private final Context mainContext;
    private SQLiteDatabase db;
    private DBManager dbManager;

    public DatabaseAdapter(Context context){
        this.mainContext = context;
        dbManager = new DBManager(mainContext);
    }

    public DatabaseAdapter createDatabase() throws SQLException {
        try {
            dbManager.createDatabase();
        }
        catch (IOException e){
            Toast.makeText(mainContext, "Error in attempting to create database.", Toast.LENGTH_LONG).show();
        }
        return this;
    }

    public DatabaseAdapter open() throws SQLException {
        dbManager.openDatabase();
        dbManager.close();
        db = dbManager.getWritableDatabase();;

        return this;
    }

    public void close(){
        dbManager.close();
    }

    // Returns true if patient ID exists in database
    public boolean signInPatient(String id){
        Cursor res = db.rawQuery("SELECT * from patient WHERE ID = ?",new String[] { id });
        if(res.getCount() == 0) {
            res.close();
            return false;
        }
        res.close();
        return true;
    }

    // Returns true if dentist SIN exists in database
    public boolean signInDentist(String SIN){
        Cursor res = db.rawQuery("SELECT * from dentist WHERE SIN = ?",new String[] { SIN });
        if(res.getCount() == 0) {
            res.close();
            return false;
        }
        res.close();
        return true;
    }

    // Returns cursor with required patient info
    public Cursor viewPatientInfo(String ID){
        return db.rawQuery("SELECT * from patient WHERE ID = ?",new String[] { ID });
    }

    // Returns cursor with required dentist info
    public Cursor viewDentistInfo(String SIN){
        return db.rawQuery("SELECT * FROM employee INNER JOIN dentist ON employee.SIN = dentist.SIN WHERE dentist.SIN = ?",new String[] { SIN });
    }

    // Returns cursor with patient's appointments
    public Cursor viewPatientAppointments(String ID){
        return db.rawQuery("SELECT * from appointment WHERE AppointPatientID = ?",new String[] { ID });
    }

    //Returns cursor with dentist's appointments
    public Cursor viewDentistAppointments(String SIN){
        return db.rawQuery("SELECT * from appointment WHERE ID IN (SELECT ID from other WHERE AttendingDentistSIN = ?)",new String[] { SIN });
    }

    //Returns true if appointment is booked
    public boolean bookAppointment(String patientID,String startTime, String appointmentType){
        if((db.rawQuery("SELECT * from patient WHERE ID = ?",new String[] { patientID })).getCount()==0){
            return false;
        }
        String assignedSIN;
        Cursor res;
        String assignedClinic;
        String roomNumber;
        if(appointmentType.equals("Cleaning")){
            res = db.rawQuery("SELECT AssignedHygienistSIN from patient WHERE ID = ?",new String[] { patientID });
            res.moveToFirst();
            assignedSIN = res.getString(0);
            res = db.rawQuery("SELECT AssignedRoom from Hygienist WHERE SIN = ?",new String[] { assignedSIN });
            res.moveToFirst();
            roomNumber = res.getString(0);
            res = db.rawQuery("SELECT AppointedClinicName from Employee WHERE SIN = ?",new String[] { assignedSIN });
            res.moveToFirst();
            assignedClinic = res.getString(0);
        }
        else{
            res = db.rawQuery("SELECT AssignedDentistSIN from patient WHERE ID = ?",new String[] { patientID });
            res.moveToFirst();
            assignedSIN = res.getString(0);
            res = db.rawQuery("SELECT AssignedRoom from Dentist WHERE SIN = ?",new String[] { assignedSIN });
            res.moveToFirst();
            roomNumber = res.getString(0);
            res = db.rawQuery("SELECT AppointedClinicName from Employee WHERE SIN = ?",new String[] { assignedSIN });
            res.moveToFirst();
            assignedClinic = res.getString(0);
        }
        if((appointmentAvailability(startTime, appointmentType, assignedSIN))==true){
            ContentValues appointmentContentValues = new ContentValues();
            appointmentContentValues.put("StartTime",startTime);
            appointmentContentValues.put("AppointmentType",appointmentType);
            appointmentContentValues.put("AppointmentClinicName",assignedClinic);
            appointmentContentValues.put("AppointRoomNumber",roomNumber);
            appointmentContentValues.put("AppointPatientID", patientID);
            db.insert("appointment", null, appointmentContentValues);
            res = db.rawQuery("SELECT ID from appointment WHERE AppointPatientID = ? AND StartTime = ?",new String[] { patientID,startTime });
            res.moveToFirst();
            String appointmentID = res.getString(0);
            if(appointmentType.equals("Cleaning")){
                ContentValues cleaningContentValues = new ContentValues();
                cleaningContentValues.put("ID",appointmentID);
                cleaningContentValues.put("AttendingHygienist",assignedSIN);
                db.insert("cleaning", null, cleaningContentValues);
            }
            else{
                ContentValues otherContentValues = new ContentValues();
                otherContentValues.put("ID",appointmentID);
                otherContentValues.put("AttendingDentistSIN",assignedSIN);
                res = db.rawQuery("SELECT SIN from dentalAssistant WHERE AssignedDentistSIN = ?",new String[] { assignedSIN });
                res.moveToFirst();
                String AttendingAssistantSIN = res.getString(0);
                otherContentValues.put("AttendingAssistantSIN",AttendingAssistantSIN);;
                db.insert("other", null, otherContentValues);
            }
            return true;
        }
        else
            return false;
    }

    //Returns true if appointment available
    public boolean appointmentAvailability(String startTime, String appointmentType, String assignedSIN){
        Cursor res;
        if(appointmentType.equals("Cleaning")){
            res = db.rawQuery("SELECT * from appointment INNER JOIN cleaning ON appointment.ID = cleaning.ID WHERE StartTime = ? AND AttendingHygienistSIN = ?",new String[] { startTime, assignedSIN });
        }
        else {
            res = db.rawQuery("SELECT * from appointment INNER JOIN other ON appointment.ID = other.ID WHERE StartTime = ? AND AttendingDentistSIN = ?",new String[] { startTime, assignedSIN });
        }
        if (res.getCount()==0)
            return true;
        return false;
    }
        //Returns true if appointment is cancelled
    public boolean cancelAppointment(String appointmentID,String appointmentType){
        if(appointmentType.equals("Cleaning")){
            db.delete("cleaning", "ID = ?",new String[] {appointmentID});
        }
        else{
            db.delete("other", "ID = ?",new String[] {appointmentID});
        }
        db.delete("appointment", "ID = ?",new String[] {appointmentID});
        return true;
    }
    
    // Returns true once patient info is updated
    public boolean updatePatientInfo(String ID, String FName, String MName, String LName, String StNo, String unit, String postalCode,String city, String province, String DOB, String sex, String insurance){
        ContentValues contentValues = new ContentValues();
        contentValues.put("FirstName",FName);
        contentValues.put("MiddleName",MName);
        contentValues.put("LastName",LName);
        contentValues.put("StreetNumber",StNo);
        contentValues.put("Unit",unit);
        contentValues.put("PostalCode",postalCode);
        contentValues.put("City",city);
        contentValues.put("Province",province);
        contentValues.put("DateOfBirth",DOB);
        contentValues.put("Sex",sex);
        contentValues.put("InsuranceNumber",insurance);
        db.update("patient", contentValues, "ID = ?",new String[] { ID });
        return true;
    }

    // Returns true once dentist info is updated
    public boolean updateDentistInfo(String SIN, String FName, String MName, String LName, String StNo, String unit, String postalCode,String city, String province, String DOB, String sex){
        ContentValues contentValues = new ContentValues();
        contentValues.put("FirstName",FName);
        contentValues.put("MiddleName",MName);
        contentValues.put("LastName",LName);
        contentValues.put("StreetNumber",StNo);
        contentValues.put("Unit",unit);
        contentValues.put("PostalCode",postalCode);
        contentValues.put("City",city);
        contentValues.put("Province",province);
        contentValues.put("DateOfBirth",DOB);
        contentValues.put("Sex",sex);
        db.update("dentist", contentValues, "SIN = ?",new String[] { SIN });
        return true;
    }

}
