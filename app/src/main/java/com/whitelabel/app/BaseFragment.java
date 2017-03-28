package com.whitelabel.app;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whitelabel.app.application.GemfiveApplication;
import com.whitelabel.app.ui.common.BasePresenter;
import com.whitelabel.app.ui.common.BaseView;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.widget.CustomButton;

public abstract class BaseFragment<T extends BasePresenter> extends Fragment implements BaseView {
    private View mContentView ;
    protected T mPresenter;
    private Toolbar mToolbar;
    private RelativeLayout mHomeSearchBarRL;
    private RelativeLayout mTitleRL;
    private TextView tvTitleNum,tvTitle;
    private ImageView ivTitle;

    @Override
    public void showProgressDialog() {

    }

    @Override
    public void closeProgressDialog() {

    }

    public void setToolBarColor(int colorId){
        getToolbar().setBackgroundColor(JToolUtils.getColor(colorId));
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter=getPresenter();
        if(mPresenter!=null){
            mPresenter.attachView(this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mPresenter!=null){
            mPresenter.detachView();
        }
    }
    public  T getPresenter(){return null;};

    public void setTitleNum(int num){
        if(num>0){
            tvTitleNum.setVisibility(View.VISIBLE);
            if(num<=99) {
                tvTitleNum.setText(num + "");
            }else{
                tvTitleNum.setText("99+");
            }
        }else{
            tvTitleNum.setVisibility(View.GONE);
        }
    }
    public void setTitle(String text){
        tvTitleNum.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);
        ivTitle.setVisibility(View.GONE);
        tvTitle.setText(text);
        mHomeSearchBarRL.setVisibility(View.GONE);
    }
    public void setTitleImage(int resouce){
        tvTitleNum.setVisibility(View.GONE);
        tvTitle.setVisibility(View.GONE);
        ivTitle.setVisibility(View.VISIBLE);
        ivTitle.setImageResource(resouce);
        mHomeSearchBarRL.setVisibility(View.GONE);
    }

    public void setHomeSearchBarClickListener(View.OnClickListener onClickListener ){
        showHomeSearchBar(true);
        mHomeSearchBarRL.setOnClickListener(onClickListener);
    }
    public void showHomeSearchBar(boolean isShow){
        if(isShow){
            mTitleRL.setVisibility(View.GONE);
            mHomeSearchBarRL.setVisibility(View.VISIBLE);
        }else{
            mTitleRL.setVisibility(View.VISIBLE);
            mHomeSearchBarRL.setVisibility(View.GONE);
        }
    }

    public void setLeftMenuIcon(int icon) {
        getToolbar().setNavigationIcon(icon);
    }
    public void setLeftMenuIcon(Drawable drawable){
        getToolbar().setNavigationIcon(drawable);
    }
    public void setLeftMenuClickListener(final View.OnClickListener onClickListener){
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClick(v);
                }
            }
        });
    }

    public View setRightTextMenuClickListener(MenuInflater menuInflater, int menuRes, Menu menu, int menuItemId, int actionViewRes, View.OnClickListener onClickListener){

        menuInflater.inflate(menuRes, menu);
        MenuItem menuItem= menu.findItem(menuItemId);
        MenuItemCompat.setActionView(menuItem, actionViewRes);
        View view=menuItem.getActionView();
        view.setOnClickListener(onClickListener);
        return view;
    }
    public void  updateRightIcon(int iconId,int itemId){
        MenuItem itemMenu=getToolbar().getMenu().findItem(itemId);
        if (itemMenu !=null) {
            View view = itemMenu.getActionView();
            ImageView img = (ImageView) view.findViewById(R.id.iv_img);
            img.setImageDrawable(JToolUtils.getDrawable(iconId));
        }
    }
    public void  updateRightIconNum(int itemId,long number){
        MenuItem itemMenu=getToolbar().getMenu().findItem(itemId);
        if (itemMenu !=null) {
            View view = itemMenu.getActionView();
            TextView textView = (TextView) view.findViewById(R.id.ctv_home_shoppingcart_num);
            if (number > 0) {
                if(number>99){
                    textView.setText(getResources().getString(R.string.nine));
                }else {
                    textView.setText(number + "");
                }
                textView.setVisibility(View.VISIBLE);
            } else {
                textView.setVisibility(View.INVISIBLE);
            }
        }
    }

    public Toolbar getToolbar(){
        return mToolbar;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    public void setContentView(View mContentView) {
        this.mContentView=mContentView;
        mToolbar= (Toolbar) mContentView.findViewById(R.id.toolbar);
        mHomeSearchBarRL = (RelativeLayout) mContentView.findViewById(R.id.rl_home_searchBar);
        mTitleRL = (RelativeLayout) mContentView.findViewById(R.id.rl_home_searchBar);

        tvTitleNum= (TextView) mContentView.findViewById(R.id.tv_title_num);
        ivTitle= (ImageView) mContentView.findViewById(R.id.iv_title);
        tvTitle= (TextView) mContentView.findViewById(R.id.tv_title);
        if (mToolbar != null) {
            mToolbar.setTitle("");
            ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        }
        if(mToolbar!=null){
            mToolbar.setBackgroundColor(GemfiveApplication.getAppConfiguration().getThemeConfig().getSecondaryColor());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageView ivTryAgain= (ImageView) getView().findViewById(R.id.iv_try_again);
        CustomButton btnAgain= (CustomButton)  getView().findViewById(R.id.btn_try_again);
        if(ivTryAgain!=null&&btnAgain!=null){
            ivTryAgain.setImageDrawable(JImageUtils.getThemeIcon(getActivity(),R.mipmap.connection_break_loading));
            btnAgain.setTextColor(GemfiveApplication.getAppConfiguration().getThemeConfig().getPrimaryColor());
        }
    }

    protected void onAnimationStarted () {}

    protected void onAnimationEnded () {}

    protected void onAnimationRepeated () {}

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Animation anim = super.onCreateAnimation(transit, enter, nextAnim);

        if (anim == null && nextAnim != 0) {
            anim = AnimationUtils.loadAnimation(getActivity(), nextAnim);
        }

        if(anim != null) {
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    onAnimationStarted();
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    onAnimationEnded();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    onAnimationRepeated();
                }
            });
        }

        return anim;
    }
}
