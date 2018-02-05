package com.whitelabel.app.model;

import com.whitelabel.app.activity.ProductListActivity;
import com.whitelabel.app.utils.JDataUtils;

import java.util.ArrayList;

/**
 * Created by img on 2018/1/25.
 */

public class TempCategoryBean {
    public final static int TABBAR_INDEX_NONE = -1;
    public Long mGATrackTimeStart = 0L;
    public int currentProductListFragmentPosition = 0;
    private int currentFilterSortTabIndex;
    public ArrayList<SVRAppserviceProductSearchParameter> searchCategoryParameterArrayList=new ArrayList<>();
    public CategoryBaseBean.CategoryBean.ChildrenBeanX searchCategoryEntity;
    public SVRAppserviceProductSearchParameter svrAppserviceProductSearchParameter;
    public String leftMenuTitle;
    private static TempCategoryBean tempCategoryBean;
    public static TempCategoryBean getInstance(){
        if (tempCategoryBean==null){
            tempCategoryBean=new TempCategoryBean();
        }
        return tempCategoryBean;
    }

    public int getCurrentProductListFragmentPosition() {
        return currentProductListFragmentPosition;
    }

    public void setCurrentProductListFragmentPosition(int currentProductListFragmentPosition) {
        this.currentProductListFragmentPosition = currentProductListFragmentPosition;
    }

    public int getCurrentFilterSortTabIndex() {
        return currentFilterSortTabIndex;
    }

    public void resetCurrentFilterSortTabIndex() {
        currentFilterSortTabIndex = TABBAR_INDEX_NONE;
    }

    public SVRAppserviceProductSearchParameter getSVRAppserviceProductSearchParameterById(int index) {
        if (index < 0 || searchCategoryParameterArrayList.size() <= index) {
            return null;
        } else {
            return searchCategoryParameterArrayList.get(index);
        }
    }

    public CategoryBaseBean.CategoryBean.ChildrenBeanX getSearchCategoryEntity() {
        return searchCategoryEntity;
    }

    /**
     * @param type FragmentType
     */
    public SVRAppserviceProductSearchParameter getSVRAppserviceProductSearchParameterById(int type, int index) {
        if (ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_CATEGORY == type) {
            return getSVRAppserviceProductSearchParameterById(index);
        } else if (ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS == type) {
            return getSvrAppserviceProductSearchParameter();
        } else {
            return null;
        }
    }


    private SVRAppserviceProductSearchParameter getSvrAppserviceProductSearchParameter() {
        if (svrAppserviceProductSearchParameter == null) {
            svrAppserviceProductSearchParameter = new SVRAppserviceProductSearchParameter();
        }
        return svrAppserviceProductSearchParameter;
    }

    private void setSVRAppserviceProductSearchParameterCategoryId(int index, String categoryId) {
        if (index < 0) {
            return;
        }
        int arraySize = searchCategoryParameterArrayList.size();
        if (index >= arraySize) {
            for (int newInstallIndex = arraySize; newInstallIndex <= index; ++newInstallIndex) {
                SVRAppserviceProductSearchParameter parameter = new SVRAppserviceProductSearchParameter();
                if (newInstallIndex == index) {
                    parameter.setCategory_id(categoryId);
                    tempCategoryBean.searchCategoryParameterArrayList.add(parameter);
                    break;
                } else {
                    tempCategoryBean.searchCategoryParameterArrayList.add(parameter);
                    continue;
                }
            }
        } else {
            tempCategoryBean.searchCategoryParameterArrayList.get(index).setCategory_id(categoryId);
        }
    }

    private void setSVRAppserviceProductSearchParameterCategoryId(String categoryId) {
        if (tempCategoryBean.svrAppserviceProductSearchParameter == null) {
            tempCategoryBean.svrAppserviceProductSearchParameter = new SVRAppserviceProductSearchParameter();
        }

        tempCategoryBean.svrAppserviceProductSearchParameter.setCategory_id(categoryId);
    }

    private void setSVRAppserviceProductSearchParameterBrandId(int index, String brandId) {
        if (index < 0) {
            return;
        }
        int arraySize = tempCategoryBean.searchCategoryParameterArrayList.size();
        if (index >= arraySize) {
            for (int newInstallIndex = arraySize; newInstallIndex <= index; ++newInstallIndex) {
                SVRAppserviceProductSearchParameter parameter = new SVRAppserviceProductSearchParameter();
                if (newInstallIndex == index) {
                    parameter.setBrandId(brandId);
                    tempCategoryBean.searchCategoryParameterArrayList.add(parameter);
                    break;
                } else {
                    tempCategoryBean.searchCategoryParameterArrayList.add(parameter);
                    continue;
                }
            }
        } else {
            tempCategoryBean.searchCategoryParameterArrayList.get(index).setBrandId(brandId);
        }
    }

    private void setSVRAppserviceProductSearchParameterBrandId(String brandId) {
        if (tempCategoryBean.svrAppserviceProductSearchParameter == null) {
            tempCategoryBean.svrAppserviceProductSearchParameter = new SVRAppserviceProductSearchParameter();
        }
        tempCategoryBean.svrAppserviceProductSearchParameter.setBrandId(brandId);
    }

    public void setSVRAppserviceProductSearchParameterMinPriceMaxPrice(long minPrice, long maxPrice) {
        if (svrAppserviceProductSearchParameter == null) {
            svrAppserviceProductSearchParameter = new SVRAppserviceProductSearchParameter();
        }

        svrAppserviceProductSearchParameter.setPrice(minPrice + "-" + maxPrice);
    }

    private void setSVRAppserviceProductSearchParameterBrand(String brandValue) {
        if (svrAppserviceProductSearchParameter == null) {
            svrAppserviceProductSearchParameter = new SVRAppserviceProductSearchParameter();
        }

        svrAppserviceProductSearchParameter.setBrand(brandValue);
    }

    private void setSVRAppserviceProductSearchParameterType(String typeValue) {
        if (svrAppserviceProductSearchParameter == null) {
            svrAppserviceProductSearchParameter = new SVRAppserviceProductSearchParameter();
        }

        svrAppserviceProductSearchParameter.setModel_type(typeValue);
    }

    private void setSVRAppserviceProductSearchParameterSort(String sortValue) {
        if (svrAppserviceProductSearchParameter == null) {
            svrAppserviceProductSearchParameter = new SVRAppserviceProductSearchParameter();
        }

        String orderValue = null;
        String dirValue = null;

        if (!JDataUtils.isEmpty(sortValue)) {
            String[] sortValueArray = sortValue.split("__");
            if (sortValueArray != null && sortValueArray.length >= 2) {
                orderValue = sortValueArray[0];
                dirValue = sortValueArray[1];
            }
        }
        svrAppserviceProductSearchParameter.setOrder(orderValue);
        svrAppserviceProductSearchParameter.setDir(dirValue);
    }

    public void setSVRAppserviceProductSearchParameterCategoryId(int type, int index, String categoryId) {
        if (ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_CATEGORY == type) {
            setSVRAppserviceProductSearchParameterCategoryId(index, categoryId);
        } else if (ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS == type) {
            setSVRAppserviceProductSearchParameterCategoryId(categoryId);
        }
    }


    public void setSVRAppserviceProductSearchParameterBrandId(int type, int index, String brandId) {
        if (ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_CATEGORY == type) {
            setSVRAppserviceProductSearchParameterBrandId(index, brandId);
        } else if (ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS == type) {
            setSVRAppserviceProductSearchParameterBrandId(brandId);
        }
    }



    public void setSVRAppserviceProductSearchParameterMinPriceMaxPrice(int index, long minPrice, long maxPrice) {
        if (index < 0) {
            return;
        }

        int arraySize = tempCategoryBean.searchCategoryParameterArrayList.size();
        if (index >= arraySize) {
            for (int newInstallIndex = arraySize; newInstallIndex <= index; ++newInstallIndex) {
                SVRAppserviceProductSearchParameter parameter = new SVRAppserviceProductSearchParameter();
                if (newInstallIndex == index) {
                    parameter.setPrice(minPrice + "-" + maxPrice);
                    tempCategoryBean.searchCategoryParameterArrayList.add(parameter);
                    break;
                } else {
                    tempCategoryBean.searchCategoryParameterArrayList.add(parameter);
                    continue;
                }
            }
        } else {
            tempCategoryBean.searchCategoryParameterArrayList.get(index).setPrice(minPrice + "-" + maxPrice);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public void setSVRAppserviceProductSearchParameterBrand(int type, int index, String brandValue) {
        if (ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_CATEGORY == type) {
            setSVRAppserviceProductSearchParameterBrand(index, brandValue);
        } else if (ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS == type) {
            setSVRAppserviceProductSearchParameterBrand(brandValue);
        }
    }

    private void setSVRAppserviceProductSearchParameterBrand(int index, String brandValue) {
        if (index < 0) {
            return;
        }
        int arraySize = tempCategoryBean.searchCategoryParameterArrayList.size();

        if (index >= arraySize) {
            for (int newInstallIndex = arraySize; newInstallIndex <= index; ++newInstallIndex) {
                SVRAppserviceProductSearchParameter parameter = new SVRAppserviceProductSearchParameter();
                if (newInstallIndex == index) {
                    parameter.setBrand(brandValue);
                    tempCategoryBean.searchCategoryParameterArrayList.add(parameter);
                    break;
                } else {
                    tempCategoryBean.searchCategoryParameterArrayList.add(parameter);
                    continue;
                }
            }
        } else {
            tempCategoryBean.searchCategoryParameterArrayList.get(index).setBrand(brandValue);
        }
    }

    private void setSVRAppserviceProductSearchParameterType(int index, String typeValue) {
        if (index < 0) {
            return;
        }

        int arraySize = tempCategoryBean.searchCategoryParameterArrayList.size();
        if (index >= arraySize) {
            for (int newInstallIndex = arraySize; newInstallIndex <= index; ++newInstallIndex) {
                SVRAppserviceProductSearchParameter parameter = new SVRAppserviceProductSearchParameter();
                if (newInstallIndex == index) {
                    parameter.setModel_type(typeValue);
                    tempCategoryBean.searchCategoryParameterArrayList.add(parameter);
                    break;
                } else {
                    tempCategoryBean.searchCategoryParameterArrayList.add(parameter);
                    continue;
                }
            }
        } else {
            tempCategoryBean.searchCategoryParameterArrayList.get(index).setModel_type(typeValue);
        }
    }

    public void setSVRAppserviceProductSearchParameterType(int type, int index, String typeValue) {
        if (ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_CATEGORY == type) {
            setSVRAppserviceProductSearchParameterType(index, typeValue);
        } else if (ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS == type) {
            setSVRAppserviceProductSearchParameterType(typeValue);
        }
    }

    private void setSVRAppserviceProductSearchParameterSort(int index, String sortValue) {
        if (index < 0) {
            return;
        }

        int arraySize = tempCategoryBean.searchCategoryParameterArrayList.size();
        if (index >= arraySize) {
            for (int newInstallIndex = arraySize; newInstallIndex <= index; ++newInstallIndex) {
                SVRAppserviceProductSearchParameter parameter = new SVRAppserviceProductSearchParameter();
                if (newInstallIndex == index) {
                    String orderValue = null;
                    String dirValue = null;

                    if (!JDataUtils.isEmpty(sortValue)) {
                        String[] sortValueArray = sortValue.split("__");
                        if (sortValueArray != null && sortValueArray.length >= 2) {
                            orderValue = sortValueArray[0];
                            dirValue = sortValueArray[1];
                        }
                    }

                    parameter.setOrder(orderValue);
                    parameter.setDir(dirValue);

                    tempCategoryBean.searchCategoryParameterArrayList.add(parameter);
                    break;
                } else {
                    tempCategoryBean.searchCategoryParameterArrayList.add(parameter);
                    continue;
                }
            }
        } else {
            String orderValue = null;
            String dirValue = null;

            if (!JDataUtils.isEmpty(sortValue)) {
                String[] sortValueArray = sortValue.split("__");
                if (sortValueArray != null && sortValueArray.length >= 2) {
                    orderValue = sortValueArray[0];
                    dirValue = sortValueArray[1];
                }
            }
            tempCategoryBean.searchCategoryParameterArrayList.get(index).setOrder(orderValue);
            tempCategoryBean.searchCategoryParameterArrayList.get(index).setDir(dirValue);
        }
    }

    public void setSVRAppserviceProductSearchParameterSort(int type, int index, String sortValue) {
        if (ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_CATEGORY == type) {
            setSVRAppserviceProductSearchParameterSort(index, sortValue);
        } else if (ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS == type) {
            setSVRAppserviceProductSearchParameterSort(sortValue);
        }
    }

    public void setSVRAppserviceProductSearchParameterBrandName(int index, String brandValue) {
        if (index < 0) {
            return;
        }
        int arraySize = tempCategoryBean.searchCategoryParameterArrayList.size();

        if (index >= arraySize) {
            for (int newInstallIndex = arraySize; newInstallIndex <= index; ++newInstallIndex) {
                SVRAppserviceProductSearchParameter parameter = new SVRAppserviceProductSearchParameter();
                if (newInstallIndex == index) {
                    parameter.setBrandName(brandValue);
                    tempCategoryBean.searchCategoryParameterArrayList.add(parameter);
                    break;
                } else {
                    tempCategoryBean.searchCategoryParameterArrayList.add(parameter);
                    continue;
                }
            }
        } else {
            tempCategoryBean.searchCategoryParameterArrayList.get(index).setBrandName(brandValue);
        }
    }

}
