package ca.ucalgary.cpsc471.bridge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class PatientBookAppointmentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_book_appointments);

        // Set up the drop-down menu for appointment types.
        Spinner dropDown = findViewById(R.id.spinner);
        String[] apptTypes = new String[]{"Cleaning", "Other"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, apptTypes);
        dropDown.setAdapter(arrayAdapter);

    }
}
