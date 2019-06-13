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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PatientBookFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PatientBookFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        setApptSpinnerContent(view);
        setTimeSpinnerContent(view);
        setApptButtonListener(view);
        return view;
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

                // Get the appointment time.
                Spinner timeSpin = v.getRootView().findViewById(R.id.patientTimeSpinner);
                TextView selectedTime = (TextView) timeSpin.getSelectedView();
                String timeResult = selectedTime.getText().toString();

                if (apptResult.equals("Cleaning")){
                    Toast.makeText(getActivity(), "Cleaning @ " + timeResult, Toast.LENGTH_SHORT).show();
                    // TODO: Have the DB add a cleaning appointment.
                        // Auto-increment an appointment ID.
                        // Start time = selected time + date; End time = 1 hour from now.
                        // Clinic = Dentist's clinic; Room = Hygienist's room; Hygienist's SIN = Patient's assigned hygienist.
                        // Patient ID = The patient's ID.
                }
                else if (apptResult.equals("Other")){
                    Toast.makeText(getActivity(), "Other @ " + timeResult, Toast.LENGTH_SHORT).show();
                    // TODO: Have the DB add a cleaning appointment.
                    // Auto-increment an appointment ID.
                    // Start time = selected time + date; End time = 2 hours from now.
                    // Clinic = Dentist's clinic; Room = Dentist's room; Dentist's SIN = Patient's assigned dentist; Assistant's SIN = Dentist's assigned assistant SIN
                    // Patient ID = The patient's ID.
                }
                else {
                    // Should never happen. (A different option must've been added.)
                    Toast.makeText(getActivity(), "Unexpected Spinner selection.", Toast.LENGTH_SHORT).show();
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
