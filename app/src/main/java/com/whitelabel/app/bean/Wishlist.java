package com.whitelabel.app.bean;

public class Wishlist {
	   private String id;
	    private String brand;
	    private String productName;
	private String smallType;
	    private String newPrice;
	private String bigType;
	private String soldBy;
	    private int imageCode;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getBrand() {
			return brand;
		}
		public void setBrand(String brand) {
			this.brand = brand;
		}
		public String getProductName() {
			return productName;
		}
		public void setProductName(String productName) {
			this.productName = productName;
		}
	    public String getSmallType() {
		return smallType;
	   }

	   public void setSmallType(String smallType) {
		this.smallType = smallType;
	   }
		public String getNewPrice() {
			return newPrice;
		}
		public void setNewPrice(String newPrice) {
			this.newPrice = newPrice;
		}
	public String getBigType() {
		return bigType;
	}

	public void setBigType(String bigType) {
		this.bigType = bigType;
	}
	public String getSoldBy() {
		return soldBy;
	}

	public void setSoldBy(String soldBy) {
		this.soldBy = soldBy;
	}
		public int getImageCode() {
			return imageCode;
		}
		public void setImageCode(int imageCode) {
			this.imageCode = imageCode;
		}

}
