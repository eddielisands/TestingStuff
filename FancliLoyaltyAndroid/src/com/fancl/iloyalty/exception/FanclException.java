package com.fancl.iloyalty.exception;

public class FanclException extends GeneralException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FanclException(String status) {
		super(status);
	}
	
	public FanclException(String status, String errMsg) {
		super(status, errMsg);
	}
	
	public FanclException(String status, String errMsgEn, String errMsgTc, String errMsgSc) {
		super(status, errMsgEn, errMsgTc, errMsgSc);
	}
	
}
