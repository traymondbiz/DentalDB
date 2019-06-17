package ca.ucalgary.cpsc471.bridge.ui.dentistMain;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ca.ucalgary.cpsc471.bridge.DatabaseAdapter;
import ca.ucalgary.cpsc471.bridge.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DentistPatientAcctSearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DentistPatientAcctSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DentistPatientAcctSearchFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    DatabaseAdapter dbAdapter = null;

    public DentistPatientAcctSearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DentistPatientAcctSearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DentistPatientAcctSearchFragment newInstance(String param1, String param2) {
        DentistPatientAcctSearchFragment fragment = new DentistPatientAcctSearchFragment();
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
        setViewButtonListener(view);
        return view;
    }

    private void setViewButtonListener(View view){
        final Button viewButton = view.findViewById(R.id.dentistViewButton);
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView patientIDSearch = v.getRootView().findViewById(R.id.dentistPatientIDEditText);
                String patientIDResult = patientIDSearch.getText().toString();
                dbAdapter.open();
                boolean validPatientID = dbAdapter.signInPatient(patientIDResult);
                dbAdapter.close();

                if (validPatientID){
                    viewButton.setVisibility(View.INVISIBLE);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.dentistAcctSearchCL, DentistPatientAcctSearchResultFragment.newInstance(patientIDResult, null));
                    ft.commitAllowingStateLoss();
                }
                else {
                    Toast.makeText(getActivity(), "Invalid patient ID.", Toast.LENGTH_SHORT).show();
                }
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
