package ca.ucalgary.cpsc471.bridge;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import ca.ucalgary.cpsc471.bridge.ui.main.PatientAcctFragment;
import ca.ucalgary.cpsc471.bridge.ui.main.PatientBookFragment;
import ca.ucalgary.cpsc471.bridge.ui.main.PatientViewFragment;
import ca.ucalgary.cpsc471.bridge.ui.main.SectionsPagerAdapter;

public class PatientMainActivity extends AppCompatActivity implements PatientViewFragment.OnFragmentInteractionListener, PatientAcctFragment.OnFragmentInteractionListener, PatientBookFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    public void onFragmentInteraction(Uri uri){

    }
}