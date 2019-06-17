package ca.ucalgary.cpsc471.bridge.ui.patientMain;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ca.ucalgary.cpsc471.bridge.DatabaseAdapter;
import ca.ucalgary.cpsc471.bridge.PatientMainActivity;
import ca.ucalgary.cpsc471.bridge.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PatientBookFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PatientBookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientBookFragment extends Fragment {
    // Unused parameters from template.
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    DatabaseAdapter dbAdapter = null;
    private OnFragmentInteractionListener mListener;
    private String selectedDate = "";

    public PatientBookFragment() {
        // Required empty public constructor
    }

    // Returns a new instance of the fragment. Parameters are not used.
    public static PatientBookFragment newInstance(String param1, String param2) {
        PatientBookFragment fragment = new PatientBookFragment();
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
        View view = inflater.inflate(R.layout.fragment_patient_book, container, false);
        dbAdapter = new DatabaseAdapter(getActivity());
        dbAdapter.createDatabase();
        setApptSpinnerContent(view);
        setTimeSpinnerContent(view);
        setApptButtonListener(view);
        setCalenderViewListener(view);
        return view;
    }

    // Sets up the Calender View, which will update the selected date on interaction.
    private void setCalenderViewListener(View view){
        CalendarView calendarView = view.findViewById(R.id.patientBookCalenderView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                long dateInMillis = calendar.getTimeInMillis();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                selectedDate = simpleDateFormat.format(dateInMillis);
            }
        });
    }

    // Sets up the Spinner in PatientBookFragment's layout.
    private void setApptSpinnerContent(View view){
        Spinner apptSpin = view.findViewById(R.id.patientApptSpinner);
        String[] apptList = new String[]{"Cleaning", "Other"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, apptList);
        apptSpin.setAdapter(arrayAdapter);
    }

    // Sets up the available times Spinner.
    private void setTimeSpinnerContent(View view){
        Spinner timeSpin = view.findViewById(R.id.patientTimeSpinner);
        // TODO: Find the available time slots using the DB.
        String[] timeList = new String[]{"08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "18:00", "19:00", "20:00"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, timeList);
        timeSpin.setAdapter(arrayAdapter);
    }

    // Sets up a listener to handle button presses in PatientBookFragment's layout.
    private void setApptButtonListener(View view){
        Button bookApptButton = (Button) view.findViewById(R.id.patientBookApptButton);
        bookApptButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Get the appointment type.
                Spinner apptSpin = v.getRootView().findViewById(R.id.patientApptSpinner);
                TextView selectedAppt = (TextView) apptSpin.getSelectedView();
                String apptResult = selectedAppt.getText().toString();

                // Get the appointment start time.
                Spinner timeSpin = v.getRootView().findViewById(R.id.patientTimeSpinner);
                TextView selectedTime = (TextView) timeSpin.getSelectedView();
                String timeResult = selectedTime.getText().toString();

                // If no date is selected (ie. The user presses Book Appointment without interacting with the CalendarView), then pick the current date.
                if (selectedDate.equals("")){
                    CalendarView calendarView = v.getRootView().findViewById(R.id.patientBookCalenderView);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    selectedDate = simpleDateFormat.format(calendarView.getDate());
                }

                // Concatenate the selected time and date together.
                PatientMainActivity patientMainActivity = (PatientMainActivity)getActivity();
                String patientID = patientMainActivity.getPatientID();
                timeResult = timeResult.concat("-" + selectedDate);

                // Submit the query.
                // TODO: Check the resulting boolean and then release a toast. Make sure the View-Appt updates accordingly to that. Check the Dentist's view as well.
                dbAdapter.open();
                boolean successfulBookResult = dbAdapter.bookAppointment(patientID, timeResult, apptResult, null);
                dbAdapter.close();

                // Create a toast declaring success or failure of the booking.
                if (successfulBookResult){
                    Toast.makeText(getActivity(), "Appointment " + apptResult + " successfully booked for: " + timeResult, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), "Failed to book appointment. (Schedule Overlap.)", Toast.LENGTH_SHORT).show();
                }
            }
        });
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


//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
}
