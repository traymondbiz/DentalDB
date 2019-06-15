package ca.ucalgary.cpsc471.bridge;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SelectionActivity extends AppCompatActivity {
    DatabaseAdapter dbAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        dbAdapter = new DatabaseAdapter(this);
        dbAdapter.createDatabase();
    }

    public void moveToPatientSignIn(View view){
        Intent intent = new Intent(this, PatientSignInActivity.class);
        startActivity(intent);

    }

    public void moveToDentistSignIn(View view){
        Intent intent = new Intent(this, DentistSignInActivity.class);
        startActivity(intent);
    }

    public void alertDialog(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Warning: Can't sign in?");
        builder.setMessage("Android 9+ devices with need to disable 'Automatic Restore', or perform a one-time clearing of the application's cache and storage.");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
