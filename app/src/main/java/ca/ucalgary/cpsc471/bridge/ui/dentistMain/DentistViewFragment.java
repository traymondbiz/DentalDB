package ca.ucalgary.cpsc471.bridge.ui.dentistMain;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormatSymbols;

import ca.ucalgary.cpsc471.bridge.DatabaseAdapter;
import ca.ucalgary.cpsc471.bridge.DentistMainActivity;
import ca.ucalgary.cpsc471.bridge.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DentistViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DentistViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DentistViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private LinearLayout apptList;
    DatabaseAdapter dbAdapter = null;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DentistViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DentistViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DentistViewFragment newInstance(String param1, String param2) {
        DentistViewFragment fragment = new DentistViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dentist_view, container, false);
        apptList = (LinearLayout) view.findViewById(R.id.dentistApptList);

        dbAdapter = new DatabaseAdapter(getActivity());
        dbAdapter.createDatabase();
        dbAdapter.open();

        populateFields(view);
        setFilterButtonListener(view);
        setRefreshButtonListener(view);

        return view;
    }

    private void setRefreshButtonListener(View view){
        Button refreshButton = view.findViewById(R.id.dentistViewRefreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apptList.removeAllViews();
                populateFields(v.getRootView());
            }
        });
    }

    private void populateFields(View view){
        DentistMainActivity mainActivity = (DentistMainActivity) getActivity();
        Cursor dentistAppts = dbAdapter.viewDentistAppointments(mainActivity.getDentistID());
        dentistAppts.moveToFirst();

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        int numOfAppts = dentistAppts.getCount();

        for (int i = 0; i < numOfAppts; i++){
            final View aView = layoutInflater.inflate(R.layout.layout_test, null);

            String rawStartDate = dentistAppts.getString(dentistAppts.getColumnIndex("StartTime"));
            String startTime = rawStartDate.substring(0, 5);    // HH:MM
            String startDay = rawStartDate.substring(6, 8);
            String startMonthNum = rawStartDate.substring(9, 11);
            String startMonthTxt = getMonth(Integer.parseInt(startMonthNum));
            String startYear = rawStartDate.substring(12, 16);

            TextView title = aView.findViewById(R.id.timeTextView);
            title.setText(startTime);

            TextView date = aView.findViewById(R.id.dateTextView);
            date.setText(startMonthTxt + " " + startDay + ", " + startYear);

            TextView type = aView.findViewById(R.id.typeTextView);
            type.setText(dentistAppts.getString(dentistAppts.getColumnIndex("AppointmentType")));

            TextView id = aView.findViewById(R.id.idTextView);
            id.setText(dentistAppts.getString(dentistAppts.getColumnIndex("ID")));

            TextView loc = aView.findViewById(R.id.clinicRoomTextView);
            String clinicName = dentistAppts.getString(dentistAppts.getColumnIndex("AppointmentClinicName"));
            String roomNumber = dentistAppts.getString(dentistAppts.getColumnIndex("AppointRoomNumber"));
            loc.setText(clinicName + " - Room " + roomNumber);

            Button viewCancelButton = (Button) aView.findViewById(R.id.cancelButton);
            TextView aViewApptID = (TextView) aView.findViewById(R.id.idTextView);
            final String aViewApptIDResult = aViewApptID.getText().toString();
            TextView aViewApptType = (TextView) aView.findViewById(R.id.typeTextView);
            final String aViewApptTypeResult = aViewApptType.getText().toString();
            viewCancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dbAdapter.open();
                    dbAdapter.cancelAppointment(aViewApptIDResult, aViewApptTypeResult);
                    dbAdapter.close();
                    apptList.removeView(aView);
                    Toast.makeText(getActivity(), "Appointment cancelled.", Toast.LENGTH_SHORT).show();
                }
            });

            apptList.addView(aView, apptList.getChildCount());

            dentistAppts.moveToNext();
        }
    }

    public String getMonth(int month){
        return new DateFormatSymbols().getMonths()[month-1];
    }

    private void setFilterButtonListener(View view){
        Spinner filterSpinner = view.findViewById(R.id.dentistFilterSpinner);
        String[] filterList = {"None", "Cleaning", "Other"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, filterList);
        filterSpinner.setAdapter(arrayAdapter);

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // When switching between fragments, sometimes onItemSelected() is activated without a view, resulting in NullPointerException.
                if (view == null){
                    return;
                }
                TextView selectedFilter = (TextView) view;
                String filterResult = selectedFilter.getText().toString();

                for (int i = 0; i < apptList.getChildCount(); i++){
                    View aView = apptList.getChildAt(i);
                    TextView apptType = aView.findViewById(R.id.typeTextView);
                    String apptResult = apptType.getText().toString();
                    if (!apptResult.equals("Cleaning") && filterResult.equals("Cleaning")){
                        aView.setVisibility(View.GONE);
                    }
                    else if (!apptResult.equals("Other") && filterResult.equals("Other")){
                        aView.setVisibility(View.GONE);
                    }
                    else {
                        aView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
