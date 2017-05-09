package com.whitelabel.app.ui.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.HomeActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link HomeHomeFragmentV4#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeHomeFragmentV4 extends Fragment  implements HomeActivity.HomeFragmentCallback{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public HomeHomeFragmentV4() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeHomeFragmentV4.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeHomeFragmentV4 newInstance(String param1, String param2) {
        HomeHomeFragmentV4 fragment = new HomeHomeFragmentV4();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void requestData() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_home_fragment_v4, container, false);
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }
}
