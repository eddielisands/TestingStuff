package com.fancl.iloyalty.service;

import java.util.List;

import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.pojo.TillId;

public interface PurchaseService {
	public String getSecurityCodeWithInput(String posCode) throws FanclException;
	
	public int convertMemberId2Decimal() throws FanclException;
	
	public int getPredefinedSecretKey() throws FanclException;
	
	public String convertDecimal2Binary(int decimalNum, String headerStr);
	
	public String exclusiveOrTwoBinary(String firstBinary, String secondBinary);
	
	public int convertBinary2Decimal(String binaryNum);
	
	public List<TillId> getStoreDetail(String posId) throws FanclException;
}
