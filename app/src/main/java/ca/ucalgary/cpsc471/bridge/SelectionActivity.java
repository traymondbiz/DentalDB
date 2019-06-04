package ca.ucalgary.cpsc471.bridge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SelectionActivity extends AppCompatActivity {

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
}
