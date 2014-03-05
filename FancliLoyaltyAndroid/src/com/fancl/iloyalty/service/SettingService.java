package com.fancl.iloyalty.service;

import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.pojo.Version;
import com.fancl.iloyalty.responseimpl.FanclGeneralResult;

public interface SettingService {
	public Object getUserReceiptSetting() throws FanclException;

	public FanclGeneralResult setUserReceiptSetting(String printBool, String emailBool) throws FanclException;

	public Version currentDatabaseVersion() throws FanclException;

	public void addUserLogWithSection(String aSection, String aSectionSubCat,
			String aType, String aContentId, String aContentName, String aEvent, String aDetail) throws FanclException;

	public void clearUserLog() throws FanclException;

	public void submitUserLog() throws FanclException;

	public void countAdHitRateWithBannerId(String bannerId) throws FanclException;

	public void updateReadContentToDatabase(String type, String itemId) throws FanclException;
	
	public void updateDatabaseContentAfterUpdating(String type, String itemId) throws FanclException;

	public Version currentTillIdDatabaseVersion() throws FanclException;
}
