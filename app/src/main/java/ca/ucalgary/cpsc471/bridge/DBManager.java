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
    private static String DB_PATH = "";
    private static String DB_NAME = "dental.db";
    private SQLiteDatabase db;
    private final Context mainContext;
    
    public DBManager(Context context) {
        super(context, DB_NAME, null, 1);
        DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        this.mainContext = context;
    }

    // Attempts to create the database if it doesn't exist yet.
    public void createDatabase() throws IOException {
        boolean dbExists = checkDatabase();
        if (!dbExists){
            this.getReadableDatabase();
            this.close();

            try {
                copyDatabase();
            }
            catch (IOException e){
                throw new Error("Error when trying to copy database.");
            }
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
        db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
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

    @Override
    public void onOpen(SQLiteDatabase db){
        super.onOpen(db);
        db.disableWriteAheadLogging();
    }
}
