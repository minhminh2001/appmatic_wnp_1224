package com.whitelabel.app.model;

import java.io.Serializable;

public  class FacebookStoryEntity implements Serializable {
	private static final long serialVersionUID = -1368296115091129534L;
	     //link==WebURL applicationName==app名字 description==content name == title caption=facebookDesc picture=imageURL
		private String link, applicationName, description, name, caption,
				picture;

		public String getLink() {
			return link;
		}

		public void setLink(String link) {
			this.link = link;
		}

		public String getApplicationName() {
			return applicationName;
		}

		public void setApplicationName(String applicationName) {
			this.applicationName = applicationName;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCaption() {
			return caption;
		}

		public void setCaption(String caption) {
			this.caption = caption;
		}





























































		public String getPicture() {
			return picture;
		}

		public void setPicture(String picture) {
			this.picture = picture;
		}
	}