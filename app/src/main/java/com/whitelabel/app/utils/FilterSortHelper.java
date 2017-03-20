package com.whitelabel.app.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.whitelabel.app.R;
import com.whitelabel.app.fragment.ProductListFilterFragment;
import com.whitelabel.app.fragment.ProductListSortFragment;

/**
 * Created by Arman on 12/20/2016.
 */

public class FilterSortHelper {

    private boolean filterActive = false;
    private boolean sortActive = false;
    private boolean shouldHideFragment = false;
    private FragmentActivity context;
    private Fragment sortFragment;
    private Fragment filterFragment;
    private FrameLayout flFilterSortContainer;
    private int fragmentContainerId;

    public FilterSortHelper(FragmentActivity context, Fragment sortFragment, Fragment filterFragment, FrameLayout container, int fragmentId) {
        this.context = context;
        this.sortFragment = sortFragment;
        this.filterFragment = filterFragment;
        this.flFilterSortContainer = container;
        this.fragmentContainerId = fragmentId;
    }

    public void hideContainer(Fragment fragment) {
        if ((fragment instanceof ProductListSortFragment || fragment instanceof ProductListFilterFragment) && shouldHideFragment) {
            flFilterSortContainer.setVisibility(View.GONE);
            shouldHideFragment = false;
        }
    }

    public void hideVisibleFragments() {
        shouldHideFragment = true;
        if (filterActive && filterFragment.isAdded()) {
            FragmentTransaction fragmentTransaction = initFragmentTransaction();
            fragmentTransaction.hide(filterFragment);
            fragmentTransaction.commitAllowingStateLoss();
        } else if (sortActive && sortFragment.isAdded()) {
            FragmentTransaction fragmentTransaction = initFragmentTransaction();
            fragmentTransaction.hide(sortFragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
        filterActive = false;
        sortActive = false;
    }

    private FragmentTransaction initFragmentTransaction() {
        FragmentTransaction fragmentTransaction = context.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_bottom_top, R.anim.exit_top_bottom);
        return fragmentTransaction;
    }

    public void onSortClicked(boolean show, Bundle bundle) {
        FragmentTransaction fragmentTransaction = initFragmentTransaction();
        if (show) {
            //do a different kind of animation if filter was active
            if (filterActive && filterFragment.isAdded()) {
                fragmentTransaction.setCustomAnimations(R.anim.activity_anim1_enter1, R.anim.activity_anim1_exit1);
                fragmentTransaction.hide(filterFragment);
                filterActive = false;
            }

            if (sortFragment.isAdded()) {
                sortFragment.getArguments().putAll(bundle);
                fragmentTransaction.show(sortFragment);
            } else {
                sortFragment.setArguments(bundle);
                fragmentTransaction.add(fragmentContainerId, sortFragment);
            }

            fragmentTransaction.commitAllowingStateLoss();
            flFilterSortContainer.setVisibility(View.VISIBLE);

        } else {
            shouldHideFragment = true;
            fragmentTransaction.hide(sortFragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
        sortActive = show;
    }

    public void onFilterClicked(boolean show, Bundle bundle) {
        FragmentTransaction fragmentTransaction = initFragmentTransaction();
        if (show) {
            //do a different kind of animation if sort was active
            if (sortActive && sortFragment.isAdded()) {
                fragmentTransaction.setCustomAnimations(R.anim.anim_enter_slide_left_to_right, R.anim.anim_exit_slide_left_to_right);
                fragmentTransaction.hide(sortFragment);
                sortActive = false;
            }

            if (filterFragment.isAdded()) {
                filterFragment.getArguments().putAll(bundle);
                fragmentTransaction.show(filterFragment);
            } else {
                filterFragment.setArguments(bundle);
                fragmentTransaction.add(fragmentContainerId, filterFragment);
            }

            fragmentTransaction.commitAllowingStateLoss();
            flFilterSortContainer.setVisibility(View.VISIBLE);
        } else {
            shouldHideFragment = true;
            fragmentTransaction.hide(filterFragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
        filterActive = show;
    }

    public boolean isAnyActive() {
        return sortActive || filterActive;
    }
}
