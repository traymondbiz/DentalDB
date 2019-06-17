package ca.ucalgary.cpsc471.bridge.ui.dentistMain;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ca.ucalgary.cpsc471.bridge.DatabaseAdapter;
import ca.ucalgary.cpsc471.bridge.DentistMainActivity;
import ca.ucalgary.cpsc471.bridge.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DentistBookFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DentistBookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DentistBookFragment extends Fragment {

    // Unused parameters from template.
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    DatabaseAdapter dbAdapter = null;
    private OnFragmentInteractionListener mListener;
    private String selectedDate = "";

    public DentistBookFragment() {
        // Required empty public constructor.
    }

    // Returns a new instance of the fragment. Parameters are not used.
    public static DentistBookFragment newInstance(String param1, String param2) {
        DentistBookFragment fragment = new DentistBookFragment();
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
        View view = inflater.inflate(R.layout.fragment_dentist_book, container, false);
        dbAdapter = new DatabaseAdapter(getActivity());
        dbAdapter.createDatabase();
        setApptSpinnerContent(view);
        setTimeSpinnerContent(view);
        setApptButtonListener(view);
        //setAsstSpinnerListener(view);     // Disabled: No SQLiteDatabase query for available assistants for a given dentist, or query specification for it.
        setCalenderViewListener(view);

        // Initially hide the assistant spinner. (Since the default appt spinner option is Cleaning.)
        Spinner asstSpin = view.findViewById(R.id.dentistAsstSpinner);
        asstSpin.setVisibility(View.GONE);

        return view;
    }

    // Sets up the Calender View, which will update the selected date on interaction.
    private void setCalenderViewListener(View view){
        CalendarView calendarView = view.findViewById(R.id.dentistBookCalenderView);
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

    // Sets up the Spinner in DentistBookFragment's layout.
    // Unused.
    private void setApptSpinnerContent(View view){
        Spinner apptSpin = view.findViewById(R.id.dentistApptSpinner);
        String[] apptList = new String[]{"Cleaning", "Other"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, apptList);
        apptSpin.setAdapter(arrayAdapter);
    }

    // If 'Other' is selected, visualize the assistant's dropdown.
    private void setAsstSpinnerListener(View view){
        final Spinner apptSpin = view.findViewById(R.id.dentistApptSpinner);

        apptSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // When switching between fragments, sometimes onItemSelected() is activated without a view, resulting in NullPointerException.
                if (view == null){
                    return;
                }
                TextView selectedAppt = (TextView) view;
                String apptResult = selectedAppt.getText().toString();
                Spinner asstSpin = view.getRootView().findViewById(R.id.dentistAsstSpinner);

                if (apptResult.equals("Other")){
                    // TODO: Populate with the dentist's assistants using a DB query.
                    String[] asstList = new String[]{"Morris Code", "Jakob Schafer"};
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, asstList);
                    asstSpin.setAdapter(arrayAdapter);
                    asstSpin.setVisibility(View.VISIBLE);
                }
                else {
                    asstSpin.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing.
            }
        });
    }


    // Sets up the available times Spinner.
    private void setTimeSpinnerContent(View view){
        Spinner timeSpin = view.findViewById(R.id.dentistTimeSpinner);
        // TODO: Find the available time slots using the DB.
        String[] timeList = new String[]{"08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "18:00", "19:00", "20:00"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, timeList);
        timeSpin.setAdapter(arrayAdapter);
    }


    // Sets up a listener to handle button presses in PatientBookFragment's layout.
    private void setApptButtonListener(View view){
        Button bookApptButton = (Button) view.findViewById(R.id.dentistBookApptButton);
        bookApptButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Get the appointment type.
                Spinner apptSpin = v.getRootView().findViewById(R.id.dentistApptSpinner);
                TextView selectedAppt = (TextView) apptSpin.getSelectedView();
                String apptResult = selectedAppt.getText().toString();

                // Get the appointment start time.
                Spinner timeSpin = v.getRootView().findViewById(R.id.dentistTimeSpinner);
                TextView selectedTime = (TextView) timeSpin.getSelectedView();
                String timeResult = selectedTime.getText().toString();

                // If no date is selected (ie. The user presses Book Appointment without interacting with the CalendarView), then pick the current date.
                if (selectedDate.equals("")){
                    CalendarView calendarView = v.getRootView().findViewById(R.id.dentistBookCalenderView);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    selectedDate = simpleDateFormat.format(calendarView.getDate());
                }

                // Concatenate the selected time and date together.
                timeResult = timeResult.concat("-" + selectedDate);

                // Retrieve the ID of the patient to book the appointment for. Makes sure it exists and is valid before proceeding. Otherwise, an error toast is provided.
                EditText selectedPatient = (EditText)  v.getRootView().findViewById(R.id.dentistPatientToBook);
                String patientResult = selectedPatient.getText().toString();
                if (patientResult.equals("")) {
                    Toast.makeText(getActivity(), "No patient specified.", Toast.LENGTH_SHORT).show();
                    return;
                }
                dbAdapter.open();
                boolean validID = dbAdapter.signInPatient(patientResult);
                dbAdapter.close();
                if (!validID){
                    Toast.makeText(getActivity(), "Invalid patient ID.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Submit the query.
                // TODO: Check the resulting boolean and then release a toast. Make sure the View-Appt updates accordingly to that. Check the Dentist's view as well.
                dbAdapter.open();
                boolean successfulBookResult = dbAdapter.bookAppointment(patientResult, timeResult, apptResult);
                dbAdapter.close();

                // Create a toast declaring success or failure of the booking.
                if (successfulBookResult){
                    // TODO: Use a DB query to get the patient's name based on the ID and display it for this toast.
                    dbAdapter.open();

                    Toast.makeText(getActivity(), "Appointment " + apptResult + " successfully booked for ID: " + patientResult + " at " + timeResult, Toast.LENGTH_SHORT).show();
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


    // Can be used to facilitate interactions between the main activity and attached fragments.
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
