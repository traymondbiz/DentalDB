package ca.ucalgary.cpsc471.bridge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class DentistSignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dentist_sign_in);
    }

    public void attemptSignIn(View view){
        EditText inputSINField = (EditText) findViewById(R.id.dentistSINBox);
        try {
            int inputID = Integer.parseInt(inputSINField.getText().toString());
            if (inputID != 2){
                // TODO: Replace with query to DB. Should check to see if valid ID. If so, populate fields with provided ID.
                throw new NumberFormatException();
            }
            else {
                // TODO: Send the valid ID to the next activity. (View Appointments by default?) So that it can propagate that information.
                Intent intent = new Intent(this, DentistMainActivity.class);
                intent.putExtra("dentistSIN", inputID);
                startActivity(intent);

            }
        }
        catch (NumberFormatException e){
            Toast.makeText(DentistSignInActivity.this, "ERROR: Invalid ID. (Try '2')", Toast.LENGTH_LONG).show();
        }
    }
}
