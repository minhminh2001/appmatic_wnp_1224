package com.whitelabel.app.ui.checkout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.whitelabel.app.BaseActivity;
import com.whitelabel.app.activity.AddAddressActivity;
import com.whitelabel.app.activity.EditAddressActivity;
import com.whitelabel.app.model.AddressBook;
import com.whitelabel.app.ui.common.BaseAddressFragment;

import java.util.ArrayList;
import java.util.List;

import static com.whitelabel.app.ui.checkout.CheckoutDefaultAddressFragment.SELECTED_ADDRESS_ID;

/**
 * Created by Administrator on 2017/6/12.
 */
public class CheckoutSelectAddressFragment extends BaseAddressFragment {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent();
         AddressBook  addressBook = getAdapter().getData().get(position);
         Bundle bundle=new Bundle();
         bundle.putSerializable("data",addressBook);
         intent.putExtras(bundle);
         getActivity().setResult(CheckoutSelectAddressActivity.RESULT_CODE,intent);
         getActivity().finish();
    }
    public  static  CheckoutSelectAddressFragment  newInstance(boolean useCache, String addressId){
        CheckoutSelectAddressFragment checkoutSelectAddressFragment=new CheckoutSelectAddressFragment();
        Bundle bundle=new Bundle();
        bundle.putBoolean(EXTRA_USE_CACHE,useCache);
        bundle.putString(SELECTED_ADDRESS_ID, addressId);
        checkoutSelectAddressFragment.setArguments(bundle);
         return checkoutSelectAddressFragment;
    }
    @Override
    public void addAddressBtnOnClick() {
        Intent intent=new Intent(getActivity(), AddAddressActivity.class);
        intent.putExtra(AddAddressActivity.EXTRA_USE_DEFAULT,false);
        startActivityForResult(intent,REQUEST_ADD_ADDRESS);
    }

    @Override
    public void onEditButtonClick(int postion) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", getAdapter().getData().get(postion));
        Intent intent = new Intent(getActivity(), EditAddressActivity.class);
        intent.putExtra(EditAddressActivity.EXTRA_USE_DEFAULT,false);
        intent.putExtras(bundle);
        if (getParentFragment() != null) {
            getParentFragment().startActivityForResult(intent, REQUEST_EDIT_ADDRESS);
        } else {
            startActivityForResult(intent, REQUEST_EDIT_ADDRESS);
        }
    }

    @Override
    public boolean isSwipeDelVisible() {
        return false;
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
    public List<AddressBook> handlerAddressData(List<AddressBook> addressBooks) {
        for(int i=0;i<addressBooks.size();i++){
            String addressId = addressBooks.get(i).getAddressId();
            if(addressId.equalsIgnoreCase(getSelectedAddressId())){
                addressBooks.get(i).setPrimaryBilling("1");
                addressBooks.get(i).setPrimaryShipping("1");
            }else {
                addressBooks.get(i).setPrimaryBilling("0");
                addressBooks.get(i).setPrimaryShipping("0");
            }
        }
        return addressBooks;
    }
    @Override
    public List<Integer> getDeleteFuntionPostions() {
        return new ArrayList<>();
    }

    public void onBackProcessor(){

        AddressBook addressBook = null;
        List<AddressBook> addressBooks = getAdapter().getData();
        if(addressBooks != null){
            for(int index = 0; index < addressBooks.size(); index++){
                String addressId = addressBooks.get(index).getAddressId();
                if(addressId.equalsIgnoreCase(getSelectedAddressId())) {
                    addressBook = addressBooks.get(index);
                    break;
                }
            }
        }

        Intent intent=new Intent();
        Bundle bundle=new Bundle();
        bundle.putSerializable("data",addressBook);
        intent.putExtras(bundle);
        getActivity().setResult(CheckoutSelectAddressActivity.RESULT_CODE,intent);
        getActivity().finish();
    }
}
