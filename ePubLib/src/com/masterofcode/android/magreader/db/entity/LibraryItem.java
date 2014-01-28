package com.masterofcode.android.magreader.db.entity;

import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.ActiveRecordException;

import com.masterofcode.android.magreader.utils.constants.Constants;

public class LibraryItem extends ActiveRecordBase {
	public String			magazine_filepath;
	public String			magazine_cover_filepath;
	public int				magazine_type = Constants.MAGAZINE_TYPE_NONE;
	public String			magazine_title;
	public String			magazine_url;
	public String			magazine_id;
	public String 			googlecheckoutid;
	
	public boolean			isDownloaded;

	public LibraryItem()
	{
		
	}

	public LibraryItem(String magazineFilePath, String magazineCoverFilePath, String magazineTitle, String magazine_url, boolean isDownloaded, int magazineType, String magazine_id, String googlecheckoutid)
	{
		this.magazine_filepath = magazineFilePath;
		this.magazine_title = magazineTitle;
		this.magazine_cover_filepath = magazineCoverFilePath;
		this.magazine_url = magazine_url;
		this.isDownloaded = isDownloaded;
		magazine_type = magazineType;
		this.magazine_id = magazine_id;
		this.googlecheckoutid = googlecheckoutid;
	}
	
	@Override
	public int update() throws ActiveRecordException {
		if(magazine_type==Constants.MAGAZINE_TYPE_NONE) return 0;
		return super.update();
	}

	@Override
	public boolean delete() throws ActiveRecordException {
		if(magazine_type==Constants.MAGAZINE_TYPE_NONE) return false;
		return super.delete();
	}

	@Override
	public long save() throws ActiveRecordException {
		if(magazine_type==Constants.MAGAZINE_TYPE_NONE) return -1;
		return super.save();
	}

	
}
