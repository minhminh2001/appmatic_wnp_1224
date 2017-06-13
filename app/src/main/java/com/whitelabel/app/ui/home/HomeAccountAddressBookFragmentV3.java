package com.whitelabel.app.ui.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.AddAddressActivity;
import com.whitelabel.app.fragment.MyAccountFragmentRefresh;
import com.whitelabel.app.model.AddressBook;
import com.whitelabel.app.ui.common.BaseAddressFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link HomeAccountAddressBookFragmentV3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeAccountAddressBookFragmentV3 extends BaseAddressFragment implements MyAccountFragmentRefresh {
    // TODO: Rename parameter arguments, choose names that match
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeAccountAddressBookFragmentV3.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeAccountAddressBookFragmentV3 newInstance(boolean useCache) {
        HomeAccountAddressBookFragmentV3 fragment = new HomeAccountAddressBookFragmentV3();
        Bundle args = new Bundle();
        args.putBoolean(BaseAddressFragment.EXTRA_USE_CACHE,useCache);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public List<AddressBook> handlerAddressData(List<AddressBook> addressBooks) {
         ArrayList<AddressBook> mBeans = new ArrayList<AddressBook>();
        for (int i = 0; i < addressBooks.size(); i++) {
            AddressBook addressBook = addressBooks.get(i);
            if("1".equals(addressBook.getPrimaryShipping())&&"1".equals(addressBook.getPrimaryBilling())) {
                AddressBook cloneObject= (AddressBook) addressBook.clone();
                addressBook.setPrimaryBilling("0");
                mBeans.add(0,addressBook);
                cloneObject.setPrimaryShipping("0");
                mBeans.add(0,cloneObject);
            }else if("1".equals(addressBook.getPrimaryBilling())){
                mBeans.add(0,addressBook);
            }else if("1".equals(addressBook.getPrimaryShipping())){
                mBeans.add(0,addressBook);
            } else{
                mBeans.add(addressBook);
            }
        }
        return mBeans;
    }
    @Override
    public List<Integer> getDeleteFuntionPostions() {
        List<Integer>  deleteFuntionPostions=new ArrayList<>();
        for(int i=0;i<getAdapter().getData().size();i++){
                if("1".equals(getAdapter().getData().get(i).getPrimaryBilling())||
                        "1".equals(getAdapter().getData().get(i).getPrimaryShipping())) {
                    deleteFuntionPostions.add(i);
                }
        }
        return null;
    }
    @Override
    public void refresh(boolean isRefresh) {
        requestData();
    }

    @Override
    public void addAddressBtnOnClick() {
        Intent intent=new Intent(getActivity(), AddAddressActivity.class);
        intent.putExtra(AddAddressActivity.EXTRA_USE_DEFAULT,true);
        getParentFragment().startActivityForResult(intent,REQUEST_ADD_ADDRESS);
        getActivity().overridePendingTransition(R.anim.enter_lefttoright, R.anim.exit_lefttoright);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
