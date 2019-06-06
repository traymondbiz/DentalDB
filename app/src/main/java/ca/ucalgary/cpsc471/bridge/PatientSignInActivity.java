package ca.ucalgary.cpsc471.bridge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PatientSignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_sign_in);
    }

    public void attemptSignIn(View view){
        EditText inputIDField = (EditText) findViewById(R.id.editText2);
        try {
            int inputID = Integer.parseInt(inputIDField.getText().toString());
            if (inputID != 1){
                // TODO: Replace with query to DB. Should check to see if valid ID. If so, populate fields with provided ID.
                throw new NumberFormatException();
            }
            else {
                // TODO: Send the valid ID to the next activity. (View Appointments by default?) So that it can propagate that information.
                Intent intent = new Intent(this, PatientBookAppointmentsActivity.class);
                startActivity(intent);

            }
        }
        catch (NumberFormatException e){
            Toast.makeText(PatientSignInActivity.this, "ERROR: Invalid ID. (Try '1')", Toast.LENGTH_LONG).show();
        }
    }
}
