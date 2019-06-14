package ca.ucalgary.cpsc471.bridge;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import ca.ucalgary.cpsc471.bridge.ui.AboutFragment;
import ca.ucalgary.cpsc471.bridge.ui.dentistMain.DentistAcctFragment;
import ca.ucalgary.cpsc471.bridge.ui.dentistMain.DentistPatientAcctSearchFragment;
import ca.ucalgary.cpsc471.bridge.ui.dentistMain.DentistBookFragment;
import ca.ucalgary.cpsc471.bridge.ui.dentistMain.DentistViewFragment;
import ca.ucalgary.cpsc471.bridge.ui.dentistMain.SectionsPagerAdapter;

public class DentistMainActivity extends AppCompatActivity implements DentistAcctFragment.OnFragmentInteractionListener, DentistPatientAcctSearchFragment.OnFragmentInteractionListener, DentistViewFragment.OnFragmentInteractionListener, DentistBookFragment.OnFragmentInteractionListener, AboutFragment.OnFragmentInteractionListener {

    String dentistID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Get the arguments from sign in.
        Bundle extras = getIntent().getExtras();
        dentistID = extras.getString("dentistSIN");

        // Initialize the main activity and its tabs.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dentist_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    public String getDentistID(){
        return dentistID;
    }

    @Override
    public void onFragmentInteraction(Uri uri){

    }
}