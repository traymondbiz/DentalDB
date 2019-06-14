package ca.ucalgary.cpsc471.bridge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class DentistSignInActivity extends AppCompatActivity {

    DBManager dbManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dentist_sign_in);
        setBackButtonListener();
        dbManager = new DBManager(this);
    }

    private void setBackButtonListener(){
        ImageView backButton = (ImageView) findViewById(R.id.imageView6);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void attemptSignIn(View view){
        EditText inputSINField = (EditText) findViewById(R.id.dentistSINBox);
        try {
            String inputID = inputSINField.getText().toString();
            boolean successfulSignIn = dbManager.signInPatient(inputID);

            // TODO: Replace once an existing populated database can be used.
            if (/*!successfulSignIn*/ !inputID.equals("2")){
                throw new NumberFormatException();
            }
            else {
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
