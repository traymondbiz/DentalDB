package ca.ucalgary.cpsc471.bridge.ui.patientMain;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ca.ucalgary.cpsc471.bridge.DatabaseAdapter;
import ca.ucalgary.cpsc471.bridge.PatientMainActivity;
import ca.ucalgary.cpsc471.bridge.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PatientAcctFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PatientAcctFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientAcctFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    DatabaseAdapter dbAdapter = null;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    public PatientAcctFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PatientAcctFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientAcctFragment newInstance(String param1, String param2) {
        PatientAcctFragment fragment = new PatientAcctFragment();
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
        View view = inflater.inflate(R.layout.fragment_patient_acct, container, false);

        populateViewWithValues(view);
        setPatientEditButton(view);
        return view;
    }

    private void setPatientEditButton(View view){
        ImageView patientEditButton = view.findViewById(R.id.patientEditButton);
        patientEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.patientAcctCL, PatientAcctEditFragment.newInstance(null, null));
                ft.commit();
            }
        });
    }


    private void populateViewWithValues(View view){
        PatientMainActivity mainActivity = (PatientMainActivity) getActivity();
        dbAdapter.open();
        Cursor patientData = dbAdapter.viewPatientInfo(mainActivity.getPatientID());
        patientData.moveToFirst();

        TextView name = (TextView) view.findViewById(R.id.patientNameEditText);
        TextView id = (TextView) view.findViewById(R.id.patientID);
        TextView streetUnit = (TextView) view.findViewById(R.id.streetUnitTextView);
        TextView cityProv = (TextView) view.findViewById(R.id.cityProvincePostalTextView);
        TextView dob = (TextView) view.findViewById(R.id.dobTextView);
        TextView insuranceID = (TextView) view.findViewById(R.id.insuranceTextView);
        TextView sex = (TextView) view.findViewById(R.id.sexTextView);

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

        id.setText("ID: " + patientData.getString(patientData.getColumnIndex("ID")));

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
