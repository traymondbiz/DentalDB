package ca.ucalgary.cpsc471.bridge.ui.patientMain;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    DatabaseAdapter dbAdapter = null;


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
        ConstraintLayout viewCL = view.findViewById(R.id.patientAcctViewCL);
        ConstraintLayout editCL = view.findViewById(R.id.patientAcctEditCL);
        viewCL.setVisibility(View.VISIBLE);
        editCL.setVisibility(View.INVISIBLE);

        populateViewWithValues(view);
        setPatientEditButton(view);
        setPatientSaveButton(view);
        setPatientCancelButton(view);
        return view;
    }

    private void setPatientEditButton(View view){
        ImageView patientEditButton = view.findViewById(R.id.patientAcctEditButton);
        patientEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateEditWithValues(v.getRootView());
                ConstraintLayout viewCL = v.getRootView().findViewById(R.id.patientAcctViewCL);
                ConstraintLayout editCL = v.getRootView().findViewById(R.id.patientAcctEditCL);
                viewCL.setVisibility(View.INVISIBLE);
                editCL.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setPatientSaveButton(View view){
        ImageView patientSaveButton = view.findViewById(R.id.patientEditSave);
        patientSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSave(v.getRootView());
                populateViewWithValues(v.getRootView());
                ConstraintLayout viewCL = v.getRootView().findViewById(R.id.patientAcctViewCL);
                ConstraintLayout editCL = v.getRootView().findViewById(R.id.patientAcctEditCL);
                viewCL.setVisibility(View.VISIBLE);
                editCL.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void setPatientCancelButton(View view){
        ImageView patientCancelButton = view.findViewById(R.id.patientEditCancel);
        patientCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintLayout viewCL = v.getRootView().findViewById(R.id.patientAcctViewCL);
                ConstraintLayout editCL = v.getRootView().findViewById(R.id.patientAcctEditCL);
                viewCL.setVisibility(View.VISIBLE);
                editCL.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void performSave(View view){
        EditText firstName = (EditText) view.findViewById(R.id.patientEditFirstName);
        EditText middleName = (EditText) view.findViewById(R.id.patientEditMiddleName);
        EditText lastName = (EditText) view.findViewById(R.id.patientEditLastName);
        EditText streetNumber = (EditText) view.findViewById(R.id.patientEditStreetNumber);
        EditText unit = (EditText) view.findViewById(R.id.patientEditUnit);
        EditText postalCode = (EditText) view.findViewById(R.id.patientEditPostalCode);
        EditText city = (EditText) view.findViewById(R.id.patientEditCity);
        EditText province = (EditText) view.findViewById(R.id.patientEditProvince);
        EditText dob = (EditText) view.findViewById(R.id.patientEditDOB);
        EditText sex = (EditText) view.findViewById(R.id.patientEditSex);
        EditText insuranceNumber = (EditText) view.findViewById(R.id.patientEditInsuranceNumber);

        if (firstName.getText().toString().equals("")){
            firstName.setText("NULL");
        }
        if (middleName.getText().toString().equals("")){
            middleName.setText("NULL");
        }
        if (lastName.getText().toString().equals("")){
            lastName.setText("NULL");
        }
        if (streetNumber.getText().toString().equals("")){
            streetNumber.setText("NULL");
        }
        if (unit.getText().toString().equals("")){
            unit.setText("NULL");
        }
        if (postalCode.getText().toString().equals("")){
            postalCode.setText("NULL");
        }
        if (city.getText().toString().equals("")){
            city.setText("NULL");
        }
        if (province.getText().toString().equals("")){
            province.setText("NULL");
        }
        if (dob.getText().toString().equals("")){
            dob.setText("NULL");
        }
        if (sex.getText().toString().equals("")){
            sex.setText("NULL");
        }
        if (insuranceNumber.getText().toString().equals("")){
            insuranceNumber.setText("NULL");
        }

        PatientMainActivity mainActivity = (PatientMainActivity) getActivity();
        String patientID = mainActivity.getPatientID();

        dbAdapter.open();
        dbAdapter.updatePatientInfo(patientID,
                firstName.getText().toString(),
                middleName.getText().toString(),
                lastName.getText().toString(),
                streetNumber.getText().toString(),
                unit.getText().toString(),
                postalCode.getText().toString(),
                city.getText().toString(),
                province.getText().toString(),
                dob.getText().toString(),
                sex.getText().toString(),
                insuranceNumber.getText().toString());
        dbAdapter.close();
    }

    private void populateEditWithValues(View view){
        PatientMainActivity mainActivity = (PatientMainActivity) getActivity();
        dbAdapter.open();
        Cursor patientData = dbAdapter.viewPatientInfo(mainActivity.getPatientID());
        patientData.moveToFirst();

        EditText firstName = (EditText) view.findViewById(R.id.patientEditFirstName);
        EditText middleName = (EditText) view.findViewById(R.id.patientEditMiddleName);
        EditText lastName = (EditText) view.findViewById(R.id.patientEditLastName);
        EditText streetNumber = (EditText) view.findViewById(R.id.patientEditStreetNumber);
        EditText unit = (EditText) view.findViewById(R.id.patientEditUnit);
        EditText postalCode = (EditText) view.findViewById(R.id.patientEditPostalCode);
        EditText city = (EditText) view.findViewById(R.id.patientEditCity);
        EditText province = (EditText) view.findViewById(R.id.patientEditProvince);
        EditText dob = (EditText) view.findViewById(R.id.patientEditDOB);
        EditText sex = (EditText) view.findViewById(R.id.patientEditSex);
        EditText insuranceNumber = (EditText) view.findViewById(R.id.patientEditInsuranceNumber);

        String firstNameResult = patientData.getString(patientData.getColumnIndex("FirstName"));
        String middleNameResult = patientData.getString(patientData.getColumnIndex("MiddleName"));
        String lastNameResult = patientData.getString(patientData.getColumnIndex("LastName"));
        String streetNumberResult = patientData.getString(patientData.getColumnIndex("StreetNumber"));
        String unitResult = patientData.getString(patientData.getColumnIndex("Unit"));
        String postalCodeResult = patientData.getString(patientData.getColumnIndex("PostalCode"));
        String cityResult = patientData.getString(patientData.getColumnIndex("City"));
        String provinceResult = patientData.getString(patientData.getColumnIndex("Province"));
        String dobResult = patientData.getString(patientData.getColumnIndex("DateOfBirth"));
        String sexResult = patientData.getString(patientData.getColumnIndex("Sex"));
        String insuranceNumberResult = patientData.getString(patientData.getColumnIndex("InsuranceNumber"));
        dbAdapter.close();

        if (firstNameResult.equals("NULL")){
            firstNameResult = "";
        }
        if (middleNameResult.equals("NULL")){
            middleNameResult = "";
        }
        if (lastNameResult.equals("NULL")){
            lastNameResult = "";
        }
        if (streetNumberResult.equals("NULL")){
            streetNumberResult = "";
        }
        if (unitResult.equals("NULL")){
            unitResult = "";
        }
        if (postalCodeResult.equals("NULL")){
            postalCodeResult = "";
        }
        if (cityResult.equals("NULL")){
            cityResult = "";
        }
        if (provinceResult.equals("NULL")){
            provinceResult = "";
        }
        if (dobResult.equals("NULL")){
            dobResult = "";
        }
        if (sexResult.equals("NULL")){
            sexResult = "";
        }
        if (insuranceNumberResult.equals("NULL")){
            insuranceNumberResult = "";
        }

        firstName.setText(firstNameResult);
        middleName.setText(middleNameResult);
        lastName.setText(lastNameResult);
        streetNumber.setText(streetNumberResult);
        unit.setText(unitResult);
        postalCode.setText(postalCodeResult);
        city.setText(cityResult);
        province.setText(provinceResult);
        dob.setText(dobResult);
        sex.setText(sexResult);
        insuranceNumber.setText(insuranceNumberResult);
    }


    private void populateViewWithValues(View view){
        PatientMainActivity mainActivity = (PatientMainActivity) getActivity();
        dbAdapter.open();
        Cursor patientData = dbAdapter.viewPatientInfo(mainActivity.getPatientID());
        patientData.moveToFirst();

        TextView name = (TextView) view.findViewById(R.id.patientAcctName);
        TextView id = (TextView) view.findViewById(R.id.patientAcctID);
        TextView streetUnit = (TextView) view.findViewById(R.id.patientStreetUnitTextView);
        TextView cityProv = (TextView) view.findViewById(R.id.patientCityProvincePostalTextView);
        TextView dob = (TextView) view.findViewById(R.id.patientDOBTextView);
        TextView insuranceID = (TextView) view.findViewById(R.id.patientInsuranceTextView);
        TextView sex = (TextView) view.findViewById(R.id.patientSexTextView);

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
