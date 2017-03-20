package com.whitelabel.app.model;

import com.whitelabel.app.adapter.CustomFooterRecyclerAdapter;

import java.io.Serializable;

public class Wishlist extends CustomFooterRecyclerAdapter.RecyclerEntity implements Serializable {
	private String itemId;
	private String productId;
	private String name;
	private String comment;
	private String sku;
	private String price;
	private String priceFormatted;
	private String image;
	private String category;
	private String finalPrice;
	private String brand;
	private String canViewPdp;
	private String availability;
	private String visibility;
	private String vendorDisplayName;
	private String vendor_id;
	private String brandId;

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getVendor_id() {
		return vendor_id;
	}

	public void setVendor_id(String vendor_id) {
		this.vendor_id = vendor_id;
	}

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

	public String getCanViewPdp() {
		return canViewPdp;
	}

	public void setCanViewPdp(String canViewPdp) {
		this.canViewPdp = canViewPdp;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public String getVendorDisplayName() {
		return vendorDisplayName;
	}

	public void setVendorDisplayName(String vendorDisplayName) {
		this.vendorDisplayName = vendorDisplayName;
	}

	public String getBrand() {return brand;}

	public void setBrand(String brand) {this.brand = brand;}

	public String getFinalPrice() {return finalPrice;}

	public void setFinalPrice(String finalPrice) {this.finalPrice = finalPrice;}

	public String getCategory() {return category;}

	public void setCategory(String category) {this.category = category;}

	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}


	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPriceFormatted() {
		return priceFormatted;
	}

	public void setPriceFormatted(String priceFormatted) {
		this.priceFormatted = priceFormatted;
	}



}
