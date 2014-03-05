package com.fancl.iloyalty.service.impl;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.pojo.TillId;
import com.fancl.iloyalty.service.PurchaseService;
import com.fancl.iloyalty.util.LogController;

public class PurchaseServiceImpl implements PurchaseService {

	@Override
	public String getSecurityCodeWithInput(String posCode) throws FanclException {
		// TODO Auto-generated method stub
		int memberNum = convertMemberId2Decimal();
	    int accessNum = Integer.valueOf(posCode);
	    int secretKey = getPredefinedSecretKey();
	    
	    String memberIdBinary = convertDecimal2Binary(memberNum, "0");
	    String accessNoBinary = convertDecimal2Binary((accessNum*10000+secretKey), "1");
	    String resultBinary = exclusiveOrTwoBinary(memberIdBinary, accessNoBinary);

	    int securityCode = convertBinary2Decimal(resultBinary);
	    
	    List<String> securityCodeStr = explode(String.valueOf(securityCode));
	    securityCodeStr.add(6, "-");
	    securityCodeStr.add(3, "-");
	    
	    String convertedStr = "";
	    for (int i = 0; i < securityCodeStr.size(); i++) {
	    	convertedStr = convertedStr + securityCodeStr.get(i);
		}
	    
	    return convertedStr;
	}
	
	private List<String> explode(String s) {
		List<String> arr = new ArrayList<String>();
	    for(int i = 0; i < s.length(); i++)
	    {
	    	arr.add(String.valueOf(s.charAt(i)));
	    }
	    return arr;
	}

	@Override
	public int convertMemberId2Decimal() throws FanclException {
		// TODO Auto-generated method stub
		String memberId = CustomServiceFactory.getAccountService().currentMemberId();
	    String middlePart = memberId.substring(2, 9);
		LogController.log("convertMemberId2Decimal");

		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM pos_offline_character_mapping;";
				c = dB.rawQuery(sql, null);

				String memberChar = "";
				String numberMapping = "";

				c.moveToFirst();
				while (!c.isAfterLast()) {
					memberChar = c.getString(c.getColumnIndex("membership_character"));
					numberMapping = c.getString(c.getColumnIndex("number_mapping"));
//					LogController.log("memberChar:"+memberChar);
//					LogController.log("numberMapping:"+numberMapping);
					middlePart = middlePart.replaceAll(memberChar, numberMapping);
					c.moveToNext();
				}

				c.close();
				c = null;
				
				return Integer.valueOf(middlePart);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			exception = e;
		}
		finally
		{
			if (c != null)
			{
				c.close();
				c = null;
			}
			if (dB != null)
			{

			}
		}

		if(exception != null)
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, exception.getMessage());
		}
		
		return -1;
	}

	@Override
	public int getPredefinedSecretKey() throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getPredefinedSecretKey");

		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM pos_secret_key;";
				c = dB.rawQuery(sql, null);

				String secretKey = "";

				c.moveToFirst();
				while (!c.isAfterLast()) {
					secretKey = c.getString(c.getColumnIndex("secret_key"));
					c.moveToNext();
				}

				c.close();
				c = null;
				
				return Integer.valueOf(secretKey);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			exception = e;
		}
		finally
		{
			if (c != null)
			{
				c.close();
				c = null;
			}
			if (dB != null)
			{

			}
		}

		if(exception != null)
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, exception.getMessage());
		}
		
		return -1;
	}

	@Override
	public String convertDecimal2Binary(int decimalNum, String headerStr) {
		// TODO Auto-generated method stub
		List<String> binaryStr = new ArrayList<String>();
	    
	    int count = 0;
	    for(int numberCopy = decimalNum; count < 28; numberCopy >>= 1)
	    {
	        // Prepend "0" or "1", depending on the bit
	        if (numberCopy == 0)
	        	binaryStr.add(0, headerStr);
	        else {
	        	
	        	int lastBit = (numberCopy & 1);
	        	binaryStr.add(0, (lastBit == 1) ? "1" : "0");
	        }
	        
	        count++;
	    }
	    
	    String convertedStr = "";
	    for (int i = 0; i < binaryStr.size(); i++) {
	    	convertedStr = convertedStr + binaryStr.get(i);
		}
	    
	    return convertedStr;
	}

	@Override
	public String exclusiveOrTwoBinary(String firstBinary, String secondBinary) {
		// TODO Auto-generated method stub
		List<String> resultStr = new ArrayList<String>();
	    for (int i = 0; i < firstBinary.length(); i++) {
	    	String firstStr = firstBinary.substring(i, i+1);
	    	String secondStr = secondBinary.substring(i, i+1);
	    	
	    	if (firstStr.equals(secondStr)) {
				resultStr.add("0");
			}
	    	else {
	    		resultStr.add("1");
	    	}
	    }
	    
	    String convertedStr = "";
	    for (int i = 0; i < resultStr.size(); i++) {
	    	convertedStr = convertedStr + resultStr.get(i);
		}
	    
	    return convertedStr;
	}

	@Override
	public int convertBinary2Decimal(String binaryNum) {
		// TODO Auto-generated method stub
		int decimalValue = 0;
	    for (int i = 0; i < binaryNum.length(); i++) {
	    	int binaryDigit = Integer.valueOf(binaryNum.substring(i, i+1));
	    	decimalValue += binaryDigit* Math.pow(2, (binaryNum.length()-i-1));
	    }
	        
	    return decimalValue;
	}

	@Override
	public List<TillId> getStoreDetail(String aPosId) throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getStoreDetail");

		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getTillIdDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM till_id WHERE pos_id = '" + aPosId + "';";
				c = dB.rawQuery(sql, null);
				
				List<TillId> tmpList = new ArrayList<TillId>();

				TillId item = null;
				
				String objectId = "";
				String storeCode = "";
				String storeName = "";
				String storeTill = "";
				String posId = "";

				c.moveToFirst();
				while (!c.isAfterLast()) {
					objectId = c.getString(c.getColumnIndex("id"));
					storeCode = c.getString(c.getColumnIndex("store_code"));
					storeName = c.getString(c.getColumnIndex("store_name"));
					storeTill = c.getString(c.getColumnIndex("store_till"));
					posId = c.getString(c.getColumnIndex("pos_id"));
					
					item = new TillId(objectId, storeCode, storeName, storeTill, posId);
					tmpList.add(item);

					c.moveToNext();
				}

				c.close();
				c = null;
				
				return tmpList;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			exception = e;
		}
		finally
		{
			if (c != null)
			{
				c.close();
				c = null;
			}
			if (dB != null)
			{

			}
		}

		if(exception != null)
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, exception.getMessage());
		}
		
		return null;
	}

}
