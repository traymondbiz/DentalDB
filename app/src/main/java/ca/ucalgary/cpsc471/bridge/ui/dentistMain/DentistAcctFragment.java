package ca.ucalgary.cpsc471.bridge.ui.dentistMain;

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
import ca.ucalgary.cpsc471.bridge.DentistMainActivity;
import ca.ucalgary.cpsc471.bridge.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DentistAcctFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DentistAcctFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DentistAcctFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    DatabaseAdapter dbAdapter = null;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DentistAcctFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DentistAcctFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DentistAcctFragment newInstance(String param1, String param2) {
        DentistAcctFragment fragment = new DentistAcctFragment();
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
        View view = inflater.inflate(R.layout.fragment_dentist_acct, container, false);

        ConstraintLayout viewCL = view.findViewById(R.id.dentistAcctViewCL);
        ConstraintLayout editCL = view.findViewById(R.id.dentistAcctEditCL);
        viewCL.setVisibility(View.VISIBLE);
        editCL.setVisibility(View.INVISIBLE);

        populateViewWithValues(view);
        setDentistEditButton(view);
        setDentistSaveButton(view);
        setDentistCancelButton(view);

        return view;
    }

    private void setDentistEditButton(View view){
        ImageView dentistEditButton = view.findViewById(R.id.dentistAcctEditButton);
        dentistEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateEditWithValues(v.getRootView());
                ConstraintLayout viewCL = v.getRootView().findViewById(R.id.dentistAcctViewCL);
                ConstraintLayout editCL = v.getRootView().findViewById(R.id.dentistAcctEditCL);
                viewCL.setVisibility(View.INVISIBLE);
                editCL.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setDentistSaveButton(View view){
        ImageView dentistSaveButton = view.findViewById(R.id.dentistEditSave);
        dentistSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSave(v.getRootView());
                populateViewWithValues(v.getRootView());
                ConstraintLayout viewCL = v.getRootView().findViewById(R.id.dentistAcctViewCL);
                ConstraintLayout editCL = v.getRootView().findViewById(R.id.dentistAcctEditCL);
                viewCL.setVisibility(View.VISIBLE);
                editCL.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void setDentistCancelButton(View view){
        ImageView dentistCancelButton = view.findViewById(R.id.dentistEditCancel);
        dentistCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintLayout viewCL = v.getRootView().findViewById(R.id.dentistAcctViewCL);
                ConstraintLayout editCL = v.getRootView().findViewById(R.id.dentistAcctEditCL);
                viewCL.setVisibility(View.VISIBLE);
                editCL.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void performSave(View view){
        EditText firstName = (EditText) view.findViewById(R.id.dentistEditFirstName);
        EditText middleName = (EditText) view.findViewById(R.id.dentistEditMiddleName);
        EditText lastName = (EditText) view.findViewById(R.id.dentistEditLastName);
        EditText streetNumber = (EditText) view.findViewById(R.id.dentistEditStreetNumber);
        EditText unit = (EditText) view.findViewById(R.id.dentistEditUnit);
        EditText postalCode = (EditText) view.findViewById(R.id.dentistEditPostalCode);
        EditText city = (EditText) view.findViewById(R.id.dentistEditCity);
        EditText province = (EditText) view.findViewById(R.id.dentistEditProvince);
        EditText dob = (EditText) view.findViewById(R.id.dentistEditDOB);
        EditText sex = (EditText) view.findViewById(R.id.dentistEditSex);

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

        DentistMainActivity mainActivity = (DentistMainActivity) getActivity();
        String dentistID = mainActivity.getDentistID();

        dbAdapter.open();
        dbAdapter.updateDentistInfo(dentistID,
                firstName.getText().toString(),
                middleName.getText().toString(),
                lastName.getText().toString(),
                streetNumber.getText().toString(),
                unit.getText().toString(),
                postalCode.getText().toString(),
                city.getText().toString(),
                province.getText().toString(),
                dob.getText().toString(),
                sex.getText().toString());
        dbAdapter.close();
    }

    private void populateEditWithValues(View view){
        DentistMainActivity mainActivity = (DentistMainActivity) getActivity();
        dbAdapter.open();
        Cursor dentistData = dbAdapter.viewDentistInfo(mainActivity.getDentistID());
        dentistData.moveToFirst();

        EditText firstName = (EditText) view.findViewById(R.id.dentistEditFirstName);
        EditText middleName = (EditText) view.findViewById(R.id.dentistEditMiddleName);
        EditText lastName = (EditText) view.findViewById(R.id.dentistEditLastName);
        EditText streetNumber = (EditText) view.findViewById(R.id.dentistEditStreetNumber);
        EditText unit = (EditText) view.findViewById(R.id.dentistEditUnit);
        EditText postalCode = (EditText) view.findViewById(R.id.dentistEditPostalCode);
        EditText city = (EditText) view.findViewById(R.id.dentistEditCity);
        EditText province = (EditText) view.findViewById(R.id.dentistEditProvince);
        EditText dob = (EditText) view.findViewById(R.id.dentistEditDOB);
        EditText sex = (EditText) view.findViewById(R.id.dentistEditSex);

        String firstNameResult = dentistData.getString(dentistData.getColumnIndex("FirstName"));
        String middleNameResult = dentistData.getString(dentistData.getColumnIndex("MiddleName"));
        String lastNameResult = dentistData.getString(dentistData.getColumnIndex("LastName"));
        String streetNumberResult = dentistData.getString(dentistData.getColumnIndex("StreetNumber"));
        String unitResult = dentistData.getString(dentistData.getColumnIndex("Unit"));
        String postalCodeResult = dentistData.getString(dentistData.getColumnIndex("PostalCode"));
        String cityResult = dentistData.getString(dentistData.getColumnIndex("City"));
        String provinceResult = dentistData.getString(dentistData.getColumnIndex("Province"));
        String dobResult = dentistData.getString(dentistData.getColumnIndex("DateOfBirth"));
        String sexResult = dentistData.getString(dentistData.getColumnIndex("Sex"));
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
    }

    private void populateViewWithValues(View view){
        DentistMainActivity mainActivity = (DentistMainActivity) getActivity();
        dbAdapter.open();
        Cursor dentistData = dbAdapter.viewDentistInfo(mainActivity.getDentistID());
        dentistData.moveToFirst();

        TextView name = (TextView) view.findViewById(R.id.dentistAcctName);
        TextView sin = (TextView) view.findViewById(R.id.dentistAcctSIN);
        TextView streetUnit = (TextView) view.findViewById(R.id.dentistStreetUnitTextView);
        TextView cityProv = (TextView) view.findViewById(R.id.dentistCityProvincePostalTextView);
        TextView dob = (TextView) view.findViewById(R.id.dentistDOBTextView);
        TextView sex = (TextView) view.findViewById(R.id.dentistSexTextView);
        TextView clinic = (TextView) view.findViewById(R.id.dentistClinicTextView);
        TextView practice = (TextView) view.findViewById(R.id.dentistPracticeTextView);
        TextView salary = (TextView) view.findViewById(R.id.dentistSalaryLabel);

        String firstName = dentistData.getString(dentistData.getColumnIndex("FirstName"));
        String middleName = dentistData.getString(dentistData.getColumnIndex("MiddleName"));
        String lastName = dentistData.getString(dentistData.getColumnIndex("LastName"));

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

        sin.setText("SIN: " + dentistData.getString(dentistData.getColumnIndex("SIN")));

        String unit = dentistData.getString(dentistData.getColumnIndex("Unit"));
        String streetNumber = dentistData.getString(dentistData.getColumnIndex("StreetNumber"));
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

        cityProv.setText(dentistData.getString(dentistData.getColumnIndex("City")) + ", " +
                dentistData.getString(dentistData.getColumnIndex("Province")) + " - " +
                dentistData.getString(dentistData.getColumnIndex("PostalCode")));

        dob.setText(dentistData.getString(dentistData.getColumnIndex("DateOfBirth")));

        clinic.setText(dentistData.getString(dentistData.getColumnIndex("AppointedClinicName")));
        practice.setText(dentistData.getString(dentistData.getColumnIndex("TypeOfPractice")));
        salary.setText(dentistData.getString(dentistData.getColumnIndex("Salary")));
        sex.setText(dentistData.getString(dentistData.getColumnIndex("Sex")));
        dbAdapter.close();

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
