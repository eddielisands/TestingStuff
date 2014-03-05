package com.gt.snssharinglibrary.pojo;

import android.graphics.Bitmap;

public class SNSShareDetail {
	private String title;
	private String caption;
	private String description;
	private String link;
	private String pictureLink;
	private String friendId;
	private Bitmap bitmap;
	private String picSavePath;

	@Override
	public String toString() {
		return "SNSShareDetail [title=" + title + ", caption=" + caption + ", description=" + description + ", link=" + link + ", pictureLink=" + pictureLink + ", friendId=" + friendId + ", bitmap=" + bitmap + ", savePath=" + picSavePath + "]";
	}

	public SNSShareDetail(String title, String caption, String description,
			String link, String pictureLink, String friendId, Bitmap bitmap,
			String savePath)
	{
		super();
		this.title = title;
		this.caption = caption;
		this.description = description;
		this.link = link;
		this.pictureLink = pictureLink;
		this.friendId = friendId;
		this.bitmap = bitmap;
		this.picSavePath = savePath;
	}
	
	public SNSShareDetail(String message)
	{
		this.description = message;
	}

	public String getTitle() {
		return title;
	}

	public String getCaption() {
		return caption;
	}

	public String getDescription() {
		return description;
	}

	public String getLink() {
		return link;
	}

	public String getPictureLink() {
		return pictureLink;
	}

	public String getFriendId() {
		return friendId;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public String getPicSavePath() {
		return picSavePath;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setPictureLink(String pictureLink) {
		this.pictureLink = pictureLink;
	}

	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public void setPicSavePath(String savePath) {
		this.picSavePath = savePath;
	}

}
