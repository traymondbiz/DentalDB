package ca.ucalgary.cpsc471.bridge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class PatientSignInActivity extends AppCompatActivity {

    DBManager dbManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_sign_in);
        setBackButtonListener();
        dbManager = new DBManager(this);
    }

    private void setBackButtonListener(){
        ImageView backButton = (ImageView) findViewById(R.id.imageView2);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void attemptSignIn(View view){
        EditText inputIDField = (EditText) findViewById(R.id.patientIDBox);
        try {
            String inputID = inputIDField.getText().toString();
            boolean successfulSignIn = dbManager.signInPatient(inputID);

            // TODO: Replace once an existing populated database can be used.
            if (/*!successfulSignIn*/ !inputID.equals("1")){
                throw new NumberFormatException();
            }
            else {
                Intent intent = new Intent(this, PatientMainActivity.class);
                intent.putExtra("patientID", inputID);
                startActivity(intent);

            }
        }
        catch (NumberFormatException e){
            Toast.makeText(PatientSignInActivity.this, "ERROR: Invalid ID. (Try '1')", Toast.LENGTH_LONG).show();
        }
    }
}
