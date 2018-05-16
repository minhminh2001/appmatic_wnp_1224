package com.whitelabel.app.adapter;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.whitelabel.app.R;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.model.ProductPropertyModel;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.widget.CustomRadioButtonImage;
import com.whitelabel.app.widget.CustomTextView;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Created by ray on 2017/5/23.
 */
public class BindProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private  List<ProductPropertyModel> mProducts;
  private ImageLoader   mImageloader;
  private  OnItemClickListener mOnItemClickListener;
  public BindProductAdapter(List<ProductPropertyModel> products, ImageLoader  imageLoader){
        this.mProducts=products;
        this.mImageloader=imageLoader;
  }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bind_product, null);
        return new ItemViewHolder(view);
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,final  int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        ProductPropertyModel svrAppserviceProductDetailResultPropertyReturnEntity = mProducts.get(position);
        if(!TextUtils.isEmpty(svrAppserviceProductDetailResultPropertyReturnEntity.getImage())) {
            JImageUtils.downloadImageFromServerByUrl(itemViewHolder.itemView.getContext(), mImageloader, itemViewHolder.ivImg,
                    svrAppserviceProductDetailResultPropertyReturnEntity.getImage(), JToolUtils.dip2px(itemViewHolder.itemView.getContext(),
                            70), JToolUtils.dip2px(itemViewHolder.itemView.getContext(), 70));
        }
        itemViewHolder.rbCheck.setSelect(svrAppserviceProductDetailResultPropertyReturnEntity.isSelected());
        itemViewHolder.tvProductName.setText(svrAppserviceProductDetailResultPropertyReturnEntity.getName());
        itemViewHolder.tvFinalPrice.setText(WhiteLabelApplication.getAppConfiguration().getCurrency().getName()+
                JDataUtils.formatDouble(svrAppserviceProductDetailResultPropertyReturnEntity.getFinalPrice()));
        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClient(v,position);
                }
            }
        });

        if(Double.valueOf(svrAppserviceProductDetailResultPropertyReturnEntity.getPrice())>Double.valueOf(svrAppserviceProductDetailResultPropertyReturnEntity.getFinalPrice())){
            itemViewHolder.tvOldPrice.setVisibility(View.VISIBLE);
            itemViewHolder.tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            itemViewHolder.tvOldPrice.setText(WhiteLabelApplication.getAppConfiguration().getCurrency().getName()+
                    JDataUtils.formatDouble(svrAppserviceProductDetailResultPropertyReturnEntity.getPrice()));
        }else{
            itemViewHolder.tvOldPrice.setVisibility(View.GONE);
        }
    }
    public void   setOnItemClickListener(OnItemClickListener  onItemClickListener){
        this.mOnItemClickListener=onItemClickListener;
    }
     public interface  OnItemClickListener{
        public void  onItemClient(View view,int position);
     }
    @Override
    public int getItemCount() {
        return mProducts.size();
    }
    static class ItemViewHolder  extends  RecyclerView.ViewHolder{
        @BindView(R.id.iv_product_img)
        ImageView ivImg;
        @BindView(R.id.tv_product_name)
        CustomTextView tvProductName;
        @BindView(R.id.tv_property)
        CustomTextView tvProperty;
        @BindView(R.id.tv_old_price)
        CustomTextView tvOldPrice;
        @BindView(R.id.tv_final_price)
        CustomTextView tvFinalPrice;
        @BindView(R.id.ivSortTitle)
        CustomRadioButtonImage rbCheck;
        ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
