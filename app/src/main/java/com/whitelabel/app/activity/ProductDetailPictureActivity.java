package com.whitelabel.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.whitelabel.app.R;
import com.whitelabel.app.application.GemfiveApplication;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.widget.photoview.PhotoView;
import com.whitelabel.app.widget.photoview.PhotoViewAttacher;

import java.util.ArrayList;


public class ProductDetailPictureActivity extends com.whitelabel.app.BaseActivity implements View.OnClickListener {

    private RelativeLayout relativeLayout;
    private LinearLayout groupTips;
    private ViewPager viewPager;
    private ArrayList<ImageView> mProductImageView;
    private ArrayList<ImageView> listGroupTips;
    private ImageView mProductdetailPictureImageView;
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail_picture);
        initView();
        if (mProductImageView != null) {
            mProductImageView.clear();
        } else {
            mProductImageView = new ArrayList<>();
        }
        listGroupTips = new ArrayList<ImageView>();

        final int destWidth = GemfiveApplication.getPhoneConfiguration().getScreenWidth();
        final int destHeight = GemfiveApplication.getPhoneConfiguration().getScreenHeigth();

        /**
         * As long as app can come here , the pictures mustn't be empty
         */
        Bundle intent = getIntent().getExtras();
        final ArrayList<String> pictures = intent.getStringArrayList("pictures");
        if (pictures.size() == 1) {
            groupTips.setVisibility(View.INVISIBLE);
        }
        int currentIndex = intent.getInt("currentIndex", 0);

        /**
         * 享元动画
         */
        setActivityImageTransition(currentIndex, pictures, destWidth);

        for (int index = 0; index < pictures.size(); index++) {
//            PhotoView
            PhotoView imageView = new PhotoView(this);
            JImageUtils.downloadImageFromServerByUrl(this, mImageLoader, imageView, pictures.get(index), destWidth, destWidth);
            imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
//                    点击图片退出
                    onBackPressed();
                }

                @Override
                public void onOutsidePhotoTap() {
//                    点击图片之外的区域退出
                    onBackPressed();
                }

                @Override
                public void onUpOrDownSlide() {
//                    上下滑动一定的距离退出
                    onBackPressed();
                }
            });
            mProductImageView.add(imageView);

            /**
             * construct imageViewTips
             */
            ImageView imageViewTips = new ImageView(this);

            try {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(20, 20);
                lp.setMargins(12, 0, 12, 0);
                imageViewTips.setLayoutParams(lp);
            } catch (Exception ex) {
                JLogUtils.e("ProductDetailPictureActivity", "initProductDetailPictures", ex);
            }
            if (index == currentIndex) {
                imageViewTips.setImageResource(R.drawable.shape_oval_productdetail_picture_checked);
            } else {
                imageViewTips.setImageResource(R.drawable.shape_oval_productdetail_picture_unchecked);
            }

            groupTips.addView(imageViewTips);
            listGroupTips.add(imageViewTips);
        }
        viewPager.setAdapter(new ProductDetailPictureAdapter(mProductImageView));
        viewPager.setCurrentItem(currentIndex);
        viewPager.setOffscreenPageLimit(mProductImageView.size());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    JLogUtils.d(currTag, "position----" + position % pictures.size() + "    url-------" + pictures.get(position % pictures.size()));
                    JImageUtils.downloadImageFromServerByUrl(ProductDetailPictureActivity.this, mImageLoader, mProductdetailPictureImageView, pictures.get(position % pictures.size()), destWidth, destWidth);
                }
                resetGroupTips(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setActivityImageTransition(int currentIndex, ArrayList<String> pictures, int destWidth) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            mProductdetailPictureImageView.setTransitionName(getResources().getString(R.string.activity_image_trans));
            JImageUtils.downloadImageFromServerByUrl(this, mImageLoader, mProductdetailPictureImageView, pictures.get(currentIndex), destWidth, destWidth);
        } else {
            mProductdetailPictureImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
//            super.onBackPressed();
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            if (viewPager.getCurrentItem() >= 0) {
                bundle.putInt("currentIndex", viewPager.getCurrentItem());
            }
            intent.putExtras(bundle);
            setResult(Activity.RESULT_OK, intent);
            viewPager.setVisibility(View.INVISIBLE);
            if (mProductdetailPictureImageView.getDrawable() == null) {
                finish();
                overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out_other);
            }
            transitionOnBackPressed();
        } else {
            finish();
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out_other);
        }
    }

    private void initView() {
        mImageLoader = new ImageLoader(this);
        relativeLayout = (RelativeLayout) findViewById(R.id.rl_productdetail_picture);
        groupTips = (LinearLayout) findViewById(R.id.ll_productdetail_picture_grouptips);
        viewPager = (ViewPager) findViewById(R.id.vp_productdetail_picture);
        mProductdetailPictureImageView = (ImageView) findViewById(R.id.iv_productdetail_picture);
        relativeLayout.setOnClickListener(this);
    }

    private void resetGroupTips(int position) {
        for (int i = 0; i < listGroupTips.size(); i++) {
            if (i == position) {
                listGroupTips.get(i).setImageResource(R.drawable.shape_oval_productdetail_picture_checked);
            } else {
                listGroupTips.get(i).setImageResource(R.drawable.shape_oval_productdetail_picture_unchecked);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_productdetail_picture:
                onBackPressed();
                break;
        }
    }

    private class ProductDetailPictureAdapter extends PagerAdapter {

        private ArrayList<ImageView> list;

        public ProductDetailPictureAdapter(ArrayList<ImageView> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            try {
                ((ViewPager) container).removeView(list.get(position % list.size()));
            } catch (Exception ex) {
                ex.getMessage();
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position %= list.size();
            ImageView imageView = list.get(position);
            ViewParent vp = imageView.getParent();
            if (vp != null) {
                ViewGroup parent = (ViewGroup) vp;
                parent.removeView(imageView);
            }
            container.addView(imageView);

            return imageView;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
//        EasyTracker.getInstance(this).activityStop(this);
//        GemfiveApplication.getRefWatcher(this).watch(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
