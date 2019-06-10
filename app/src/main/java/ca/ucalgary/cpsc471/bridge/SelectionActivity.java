package ca.ucalgary.cpsc471.bridge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SelectionActivity extends AppCompatActivity {

    DBManager dentalDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
    }

    public void moveToPatientSignIn(View view){
        Intent intent = new Intent(this, PatientSignInActivity.class);
        startActivity(intent);

    }

    public void moveToDentistSignIn(View view){
        Intent intent = new Intent(this, DentistSignInActivity.class);
        startActivity(intent);
    }

    public void jumpstartDatabase(View view){
        dentalDB = new DBManager(this);
        Toast.makeText(this, "Successfully jumpstarted database!", Toast.LENGTH_SHORT).show();
    }
}
