package ca.ucalgary.cpsc471.bridge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class LeftoversPatBookAppt extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_book_appointments);

        // Set up the drop-down menu for appointment types.
        Spinner dropDown = findViewById(R.id.apptTypeSpinner);
        String[] apptTypes = new String[]{"Cleaning", "Other"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, apptTypes);
        dropDown.setAdapter(arrayAdapter);

    }

    public void attemptBooking(View view){
        Spinner dropDown = findViewById(R.id.apptTypeSpinner);
        TextView selectedAppt = (TextView) dropDown.getSelectedView();
        String result = selectedAppt.getText().toString();

        if (result.equals("Cleaning")){
            Toast.makeText(this, "Cleaning not available.", Toast.LENGTH_SHORT).show();
        }
        else if (result.equals("Other")){
            Toast.makeText(this, "Other not available.", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Unexpected Spinner selection.", Toast.LENGTH_SHORT).show();
        }

    }
}
