package com.whitelabel.app.ui.productdetail;

import com.whitelabel.app.RxUnitTestTools;
import com.whitelabel.app.data.service.IAccountManager;
import com.whitelabel.app.data.service.IBaseManager;
import com.whitelabel.app.data.service.ICommodityManager;
import com.whitelabel.app.data.service.IShoppingCartManager;
import com.whitelabel.app.model.AddToWishlistEntity;
import com.whitelabel.app.model.GOUserEntity;
import com.whitelabel.app.model.ProductDetailModel;
import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.model.WishDelEntityResult;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import  static  org.mockito.Mockito.verify;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2017/7/11.
 */
public class ProductDetailPresenterTest {

    private ProductDetailPresenter  presenter;
    @Mock
    ICommodityManager iCommodityManager;
    @Mock
    IBaseManager iBaseManager;
    @Mock
    IAccountManager  iAccountManager;
    @Mock
    ProductDetailContract.View  view;
    @Mock
    IShoppingCartManager iShoppingCartManager;
    @Before
    public void setUp(){
        RxUnitTestTools.openRxTools();
        MockitoAnnotations.initMocks(this);
        presenter=new ProductDetailPresenter(iAccountManager,iCommodityManager,iBaseManager,iShoppingCartManager);
        presenter.attachView(view);
    }
    @Test
    public void loadProductDetailData() throws Exception {
        Mockito.when(iBaseManager.isSign()).thenReturn(false);
        Mockito.when(iCommodityManager.getProductDetail("","420")).
                thenReturn(rx.Observable.just(new ProductDetailModel()));
        presenter.loadProductDetailData("420");
        verify(view).showNornalProgressDialog();;
        verify(view).showContentLayout();
        presenter.setDialogType("from_product_list");
        presenter.loadProductDetailData("420");
        verify(view).showBottomProgressDialog();
    }
    @Test
    public void showVisibleProduct() throws Exception {
        ProductDetailModel  productDetailModel=new ProductDetailModel();
        productDetailModel.setVisibility("0");
        presenter.showVisibleProduct(productDetailModel);
        productDetailModel.setVisibility("1");
        productDetailModel.setIsLike(1);
        productDetailModel.setAvailability("");
        presenter.showVisibleProduct(productDetailModel);
        verify(view).hideVisibleProduct();
        verify(view).hideAvailabilityView();
        productDetailModel.setVisibility("1");
        productDetailModel.setIsLike(0);
        productDetailModel.setAvailability("0");
        presenter.showVisibleProduct(productDetailModel);
        verify(view).setLikeView(false);
        verify(view).showAvailabilityView();
    }
    @Test
    public void loadPropertyView() throws Exception {
        ProductDetailModel  productDetailModel=new ProductDetailModel();
        productDetailModel.setType(ProductDetailPresenter.TYPE_SIMPLE);
        presenter.loadPropertyView(productDetailModel);
        verify(view).loadSimpleProductView(productDetailModel,new ArrayList<String>());

        productDetailModel.setType(ProductDetailPresenter.TYPE_CONFIGURABLE);
        presenter.loadPropertyView(productDetailModel);
        verify(view).loadConfigurableProductView(productDetailModel,new ArrayList<String>());

        productDetailModel.setType(ProductDetailPresenter.TYPE_GROUP);
        ArrayList<String>  imgs=new ArrayList<>();
        imgs.add("url");
        productDetailModel.setImages(imgs);
        presenter.loadPropertyView(productDetailModel);
        verify(view).loadGroupProductView(productDetailModel,imgs);
    }

    @Test
    public void wishListBtnClick() throws Exception {
        Mockito.when(iBaseManager.isSign()).thenReturn(false);
        presenter.wishListBtnClick();
        verify(view).startLoginActivity();
        Mockito.when(iBaseManager.isSign()).thenReturn(true);

        GOUserEntity goUserEntity=  new GOUserEntity();
        Mockito.when(iBaseManager.getUser()).thenReturn(goUserEntity);

        WishDelEntityResult wishDelEntityResult=  new WishDelEntityResult();
        wishDelEntityResult.setStatus(1);
        wishDelEntityResult.setWishListItemCount(10);

        Mockito.when(iAccountManager.deleteWishlist("","123")).thenReturn(rx.Observable.just(wishDelEntityResult));

        AddToWishlistEntity addToWishlistEntity= new AddToWishlistEntity();
        addToWishlistEntity.setStatus(1);
        Mockito.when(iAccountManager.addWishlist("","123")).thenReturn(rx.Observable.just(addToWishlistEntity));

        ProductDetailModel productDetailModel=new ProductDetailModel();
        productDetailModel.setIsLike(1);
        productDetailModel.setItemId("123");
        presenter.setmProduct(productDetailModel);
        presenter.wishListBtnClick();
        verify(view).setWishIconColorToBlank();
        verify(iAccountManager).deleteWishlist("","123");
        verify(iBaseManager).saveUser(goUserEntity);
        productDetailModel.setIsLike(0);

        productDetailModel.setId("123");
        presenter.wishListBtnClick();
        verify(view).setWishIconColorToThemeColor();
        verify(iAccountManager).addWishlist("","123");
    }
    @Test
    public void productCountMinusClick() throws Exception {
        presenter.setCurrUserSelectedProductMaxStockQty(2);
        presenter.productCountMinusClick();
        ArgumentCaptor<Long> argumentCaptor=ArgumentCaptor.forClass(Long.class);
        verify(view).setProductCountView(argumentCaptor.capture());
        Assert.assertTrue(argumentCaptor.getValue()==1);


        presenter.setCurrUserSelectedProductMaxStockQty(0);
        presenter.productCountMinusClick();
        verify(view).showNoInventoryToast();
    }

    @Test
    public void productCountPlusClick() throws Exception {
        presenter.setCurrUserSelectedProductMaxStockQty(5);
        presenter.setUserSelectedProductQty(6);
        presenter.productCountPlusClick();
        ArgumentCaptor<Long> argumentCaptor=ArgumentCaptor.forClass(Long.class);
        verify(view).setProductCountView(argumentCaptor.capture());
        Assert.assertTrue(argumentCaptor.getValue()==5);


        presenter.setUserSelectedProductQty(3);
        presenter.productCountPlusClick();
        verify(view,times(2)).setProductCountView(argumentCaptor.capture());
        Assert.assertTrue(argumentCaptor.getValue()==4);
    }
    @Test
    public void addToCartClick() throws Exception {
        ProductDetailModel productDetailModel=new ProductDetailModel();
        productDetailModel.setIsLike(0);
        productDetailModel.setId("123");
        productDetailModel.setType(ProductDetailPresenter.TYPE_SIMPLE);
        AddToWishlistEntity addToWishlistEntity= new AddToWishlistEntity();
        addToWishlistEntity.setStatus(1);
        Mockito.when(iBaseManager.isSign()).thenReturn(true);
        GOUserEntity goUserEntity=  new GOUserEntity();
        Mockito.when(iBaseManager.getUser()).thenReturn(goUserEntity);
        Mockito.when(iAccountManager.addWishlist("","123")).thenReturn(rx.Observable.just(addToWishlistEntity));
        presenter.setOutOfStock(true);
        presenter.setmProduct(productDetailModel);
        presenter.addToCartClick();
        verify(iAccountManager).addWishlist("","123");


        Map<String,String> map=new HashMap<>();
        map.put("123","1");
        ResponseModel responseModel= new ResponseModel();
        responseModel.setStatus(1);
        Mockito .when(view.getConfiguationProductSimpleId()).thenReturn("123");
        Mockito.when(iShoppingCartManager.addProductToShoppingCart(any(String.class),any(String.class),any(Map.class))).thenReturn(rx.Observable.just(responseModel));
        Mockito.when(view.getGroupProductParams()).thenReturn(map);
        presenter.setOutOfStock(false);
        presenter.setUserSelectedProductQty(1);
        presenter.addToCartClick();
        verify( iShoppingCartManager).addProductToShoppingCart(any(String.class),any(String.class),any(Map.class));
    }



}