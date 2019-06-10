package ca.ucalgary.cpsc471.bridge;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ca.ucalgary.cpsc471.bridge.ui.AboutFragment;
import ca.ucalgary.cpsc471.bridge.ui.dentistMain.DentistAcctSearchFragment;
import ca.ucalgary.cpsc471.bridge.ui.dentistMain.DentistBookFragment;
import ca.ucalgary.cpsc471.bridge.ui.dentistMain.DentistViewFragment;
import ca.ucalgary.cpsc471.bridge.ui.dentistMain.SectionsPagerAdapter;

public class DentistMainActivity extends AppCompatActivity implements DentistAcctSearchFragment.OnFragmentInteractionListener, DentistViewFragment.OnFragmentInteractionListener, DentistBookFragment.OnFragmentInteractionListener, AboutFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dentist_main);
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