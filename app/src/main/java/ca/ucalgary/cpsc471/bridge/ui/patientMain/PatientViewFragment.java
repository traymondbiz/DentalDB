package ca.ucalgary.cpsc471.bridge.ui.patientMain;

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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import ca.ucalgary.cpsc471.bridge.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PatientViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PatientViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LinearLayout apptList;

    private OnFragmentInteractionListener mListener;

    public PatientViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PatientViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientViewFragment newInstance(String param1, String param2) {
        PatientViewFragment fragment = new PatientViewFragment();
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
        View view = inflater.inflate(R.layout.fragment_patient_view, container, false);
        apptList = (LinearLayout) view.findViewById(R.id.patientApptList);

        // TODO: Populate list of appt by ID here, and loop to inflate. (Will have to change performTestInflation() to inflate accordingly.

        initializeInflaterButton(view);
        setFilterButtonListener(view);

        return view;
    }

    private void setFilterButtonListener(View view){
        Spinner filterSpinner = view.findViewById(R.id.patientFilterSpinner);
        String[] filterList = {"None", "Cleaning", "Other"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, filterList);
        filterSpinner.setAdapter(arrayAdapter);

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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

    private void initializeInflaterButton(View view){
        Button inflateButton = (Button) view.findViewById(R.id.patientInflateButton);
        inflateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performTestInflation(v.getRootView(), apptList.getChildCount());
            }
        });
    }

    private void performTestInflation(View view, int count){
        LayoutInflater li = getActivity().getLayoutInflater();
        final View aView = li.inflate(R.layout.layout_test, null);

        // Alternate between setting the test-inflated view as cleaning/other.
        TextView title = aView.findViewById(R.id.timeTextView);
        TextView apptType = aView.findViewById(R.id.typeTextView);
        if (count % 2 == 0){
            apptType.setText("Other");
            title.setText("12:0" + count + " - 2h");
        }
        else {
            apptType.setText("Cleaning");
            title.setText("12:0" + count + " - 1h");
        }

        Button viewCancelButton = (Button) aView.findViewById(R.id.cancelButton);
        viewCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apptList.removeView(aView);
                // TODO: Perform a DB operation to also remove the appt from the list by the ID specified in the view.
            }
        });

        apptList.addView(aView, apptList.getChildCount());

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
