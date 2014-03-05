package com.fancl.iloyalty.service.callback;

public interface ILoyaltyTCPSocketServiceCallback {
	
	public void didReceiveTCPSocketResult(String string);
	
	public void didReceiveTCPSocketIsSuccess(Boolean isSuccess);
}
