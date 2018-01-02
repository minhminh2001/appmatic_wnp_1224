package com.whitelabel.app.ui.home.fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.whitelabel.app.BaseActivity;
import com.whitelabel.app.activity.AddAddressActivity;
import com.whitelabel.app.activity.EditAddressActivity;
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
    private List<AddressBook> addressBooks=new ArrayList<>();
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
        this.addressBooks=addressBooks;
         ArrayList<AddressBook> billAndShips = new ArrayList<AddressBook>();
         ArrayList<AddressBook> otherLists = new ArrayList<AddressBook>();
        for (int i = 0; i < addressBooks.size(); i++) {
            AddressBook addressBook = addressBooks.get(i);
            if("1".equals(addressBook.getPrimaryShipping())&&"1".equals(addressBook.getPrimaryBilling())) {
                AddressBook cloneObject= (AddressBook) addressBook.clone();
                addressBook.setPrimaryShipping("0");
                billAndShips.add(0,addressBook);
                cloneObject.setPrimaryBilling("0");
                billAndShips.add(0,cloneObject);
            }else if("1".equals(addressBook.getPrimaryBilling())){
                billAndShips.add(0,addressBook);
            }else if("1".equals(addressBook.getPrimaryShipping())){
                if(billAndShips.size()>0){
                    billAndShips.add(1, addressBook);
                }else {
                    billAndShips.add(0, addressBook);
                }
            }else {
                otherLists.add(addressBook);
            }
        }
        billAndShips.addAll(otherLists);
        return billAndShips;
    }
    @Override
    public void onEditButtonClick(int postion) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", getAdapter().getData().get(postion));
        Intent intent = new Intent(getActivity(), EditAddressActivity.class);
        intent.putExtras(bundle);
        if (getParentFragment() != null) {
            getParentFragment().startActivityForResult(intent, REQUEST_EDIT_ADDRESS);
        } else {
            startActivityForResult(intent, REQUEST_EDIT_ADDRESS);
        }
        ((BaseActivity)getActivity()).startActivityTransitionAnim();
    }

    @Override
    public boolean isSwipeDelVisible() {
        return true;
    }

    @Override
    public List<Integer> getDeleteFuntionPostions() {
        List<Integer>  deleteFuntionPostions=new ArrayList<>();
        for(int i=0;i<getAdapter().getData().size();i++){
                if(!"1".equals(getAdapter().getData().get(i).getPrimaryBilling())&&
                        !"1".equals(getAdapter().getData().get(i).getPrimaryShipping())) {
                    deleteFuntionPostions.add(i);
                }
        }
        return deleteFuntionPostions;
    }
    @Override
    public void refresh(boolean isRefresh) {
        if(isRefresh) {
            requestData();

        }
    }
    @Override
    public void addAddressBtnOnClick() {
        Intent intent=new Intent(getActivity(), AddAddressActivity.class);
        intent.putExtra(AddAddressActivity.EXTRA_ADDRESS_LIST_SIZE,this.addressBooks.size());
        getParentFragment().startActivityForResult(intent,REQUEST_ADD_ADDRESS);
        ((BaseActivity)getActivity()).startActivityTransitionAnim();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode==REQUEST_ADD_ADDRESS&&resultCode==AddAddressActivity.RESULT_CODE)
                ||(requestCode==REQUEST_EDIT_ADDRESS&&resultCode==EditAddressActivity.RESULT_CODE)){
            requestData();
        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
