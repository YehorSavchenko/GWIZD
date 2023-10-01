package com.centarius.gwizd.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.centarius.gwizd.R;
import com.centarius.gwizd.activity.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubmitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubmitFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public SubmitFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubmitFragment.
     */
    public static SubmitFragment newInstance(String param1, String param2) {
        SubmitFragment fragment = new SubmitFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_submit, container, false);

        // Add a delay of 2 seconds
        new Handler().postDelayed(() -> {
            // Start the MainActivity
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);

            // Finish the current activity
            if (getActivity() != null) {
                getActivity().finish();
            }
        }, 2000); // 2 seconds (2000 milliseconds)
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).hideBottomNavigation();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showBottomNavigation();
        }
    }
}
