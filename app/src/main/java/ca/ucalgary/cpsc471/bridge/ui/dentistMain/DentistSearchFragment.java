package ca.ucalgary.cpsc471.bridge.ui.dentistMain;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ca.ucalgary.cpsc471.bridge.DatabaseAdapter;
import ca.ucalgary.cpsc471.bridge.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DentistSearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DentistSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DentistSearchFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    DatabaseAdapter dbAdapter = null;

    public DentistSearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DentistSearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DentistSearchFragment newInstance(String param1, String param2) {
        DentistSearchFragment fragment = new DentistSearchFragment();
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
        dbAdapter = new DatabaseAdapter(getActivity());
        dbAdapter.createDatabase();
        View view = inflater.inflate(R.layout.fragment_dentist_acct_search, container, false);
        initializeView(view);
        setViewButtonListener(view);
        setClearButtonListener(view);
        return view;
    }

    private void setViewButtonListener(View view){
        final ConstraintLayout searchBarCL = view.findViewById(R.id.dentistSearchBarCL);
        final ConstraintLayout startCL = view.findViewById(R.id.dentistSearchStartCL);
        final ConstraintLayout resultsCL = view.findViewById(R.id.dentistSearchResultsCL);
        final EditText searchBar = view.findViewById(R.id.dentistPatientIDEditText);
        Button viewButton = view.findViewById(R.id.dentistViewButton);
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchInput = searchBar.getText().toString();
                dbAdapter.open();
                boolean validSearch = dbAdapter.signInPatient(searchInput);
                dbAdapter.close();

                if (validSearch){
                    populateFieldsWithValues(v.getRootView(), searchInput);
                    searchBarCL.setVisibility(View.VISIBLE);
                    startCL.setVisibility(View.INVISIBLE);
                    resultsCL.setVisibility(View.VISIBLE);
                }
                else {
                    Toast.makeText(getActivity(), "Invalid ID: " + searchInput, Toast.LENGTH_SHORT).show();
                    searchBarCL.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    private void populateFieldsWithValues(View view, String patientID){
        dbAdapter.open();
        Cursor patientData = dbAdapter.viewPatientInfo(patientID);
        patientData.moveToFirst();

        TextView name = (TextView) view.findViewById(R.id.dentistSearchNameResult);
        TextView id = (TextView) view.findViewById(R.id.dentistSearchIDResult);
        TextView streetUnit = (TextView) view.findViewById(R.id.dentistSearchStreetUnitResult);
        TextView cityProv = (TextView) view.findViewById(R.id.dentistSearchCityProvincePostalResult);
        TextView dob = (TextView) view.findViewById(R.id.dentistSearchDOBResult);
        TextView insuranceID = (TextView) view.findViewById(R.id.dentistSearchInsuranceResult);
        TextView sex = (TextView) view.findViewById(R.id.dentistSearchSexResult);

        String firstName = patientData.getString(patientData.getColumnIndex("FirstName"));
        String middleName = patientData.getString(patientData.getColumnIndex("MiddleName"));
        String lastName = patientData.getString(patientData.getColumnIndex("LastName"));

        if (firstName.equals("NULL")){
            firstName = "";
        }
        else {
            firstName = firstName.concat(" ");
        }
        if (middleName.equals("NULL")){
            middleName = "";
        }
        else {
            middleName = middleName.concat(" ");
        }
        if (lastName.equals("NULL")){
            lastName = "";
        }
        name.setText(firstName + middleName + lastName);

        id.setText(patientData.getString(patientData.getColumnIndex("ID")));

        String unit = patientData.getString(patientData.getColumnIndex("Unit"));
        String streetNumber = patientData.getString(patientData.getColumnIndex("StreetNumber"));
        if (unit.equals("NULL")){
            unit = "";
        }
        else {
            unit = unit.concat(" ");
        }
        if (streetNumber.equals("NULL")){
            streetNumber = "";
        }
        streetUnit.setText(unit + streetNumber);

        cityProv.setText(patientData.getString(patientData.getColumnIndex("City")) + ", " +
                patientData.getString(patientData.getColumnIndex("Province")) + " - " +
                patientData.getString(patientData.getColumnIndex("PostalCode")));

        dob.setText(patientData.getString(patientData.getColumnIndex("DateOfBirth")));

        insuranceID.setText(patientData.getString(patientData.getColumnIndex("InsuranceNumber")));

        sex.setText(patientData.getString(patientData.getColumnIndex("Sex")));
        dbAdapter.close();
    }

    private void setClearButtonListener(View view){
        final ConstraintLayout searchBarCL = view.findViewById(R.id.dentistSearchBarCL);
        final ConstraintLayout startCL = view.findViewById(R.id.dentistSearchStartCL);
        final ConstraintLayout resultsCL = view.findViewById(R.id.dentistSearchResultsCL);
        final EditText searchBar = view.findViewById(R.id.dentistPatientIDEditText);
        Button clearButton = view.findViewById(R.id.dentistClearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBar.setText("");
                searchBarCL.setVisibility(View.VISIBLE);
                startCL.setVisibility(View.VISIBLE);
                resultsCL.setVisibility(View.INVISIBLE);
            }
        });
    }

    // Initializes the search view by hiding 'results'
    private void initializeView(View view){
        ConstraintLayout searchBarCL = view.findViewById(R.id.dentistSearchBarCL);
        ConstraintLayout startCL = view.findViewById(R.id.dentistSearchStartCL);
        ConstraintLayout resultsCL = view.findViewById(R.id.dentistSearchResultsCL);
        searchBarCL.setVisibility(View.VISIBLE);
        startCL.setVisibility(View.VISIBLE);
        resultsCL.setVisibility(View.INVISIBLE);
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
