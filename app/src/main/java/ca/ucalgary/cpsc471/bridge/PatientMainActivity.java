package ca.ucalgary.cpsc471.bridge;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import ca.ucalgary.cpsc471.bridge.ui.AboutFragment;
import ca.ucalgary.cpsc471.bridge.ui.patientMain.PatientAcctFragment;
import ca.ucalgary.cpsc471.bridge.ui.patientMain.PatientBookFragment;
import ca.ucalgary.cpsc471.bridge.ui.patientMain.PatientViewFragment;
import ca.ucalgary.cpsc471.bridge.ui.patientMain.SectionsPagerAdapter;

public class PatientMainActivity extends AppCompatActivity implements PatientViewFragment.OnFragmentInteractionListener, PatientAcctFragment.OnFragmentInteractionListener, PatientBookFragment.OnFragmentInteractionListener, AboutFragment.OnFragmentInteractionListener {

    int patientID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Get the arguments from sign in.
        Bundle extras = getIntent().getExtras();
        patientID = extras.getInt("patientID");

        // Initialize the main activity and its tabs.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    @Override
    public void onFragmentInteraction(Uri uri){

    }


}