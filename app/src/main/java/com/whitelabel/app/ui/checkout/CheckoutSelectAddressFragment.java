package com.whitelabel.app.ui.checkout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.AddAddressActivity;
import com.whitelabel.app.model.AddressBook;
import com.whitelabel.app.ui.common.BaseAddressFragment;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Administrator on 2017/6/12.
 */
public class CheckoutSelectAddressFragment extends BaseAddressFragment {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent();
         AddressBook  addressBook= getAdapter().getData().get(position);
         Bundle bundle=new Bundle();
         bundle.putSerializable("data",addressBook);
         intent.putExtras(bundle);
         getActivity().setResult(CheckoutSelectAddressActivity.RESULT_CODE,intent);
         getActivity().finish();
    }
    public  static  CheckoutSelectAddressFragment  newInstance(boolean useCache){
        CheckoutSelectAddressFragment checkoutSelectAddressFragment=new CheckoutSelectAddressFragment();
        Bundle bundle=new Bundle();
        bundle.putBoolean(EXTRA_USE_CACHE,useCache);
        checkoutSelectAddressFragment.setArguments(bundle);
         return checkoutSelectAddressFragment;
    }
    @Override
    public void addAddressBtnOnClick() {
        Intent intent=new Intent(getActivity(), AddAddressActivity.class);
        intent.putExtra(AddAddressActivity.EXTRA_USE_DEFAULT,false);
        startActivityForResult(intent,REQUEST_ADD_ADDRESS);
        getActivity().overridePendingTransition(R.anim.enter_lefttoright, R.anim.exit_lefttoright);
    }

    @Override
    public List<AddressBook> handlerAddressData(List<AddressBook> addressBooks) {
        for(int i=0;i<addressBooks.size();i++){
            addressBooks.get(i).setPrimaryBilling("0");
            addressBooks.get(i).setPrimaryShipping("0");
        }
        return addressBooks;
    }
    @Override
    public List<Integer> getDeleteFuntionPostions() {
        return new ArrayList<>();
    }
}
