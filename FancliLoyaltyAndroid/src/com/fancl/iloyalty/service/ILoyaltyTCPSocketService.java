package com.fancl.iloyalty.service;

import com.fancl.iloyalty.service.callback.ILoyaltyTCPSocketServiceCallback;

public interface ILoyaltyTCPSocketService {
	public void socketConnectRequest();

	public void socketDisconnectRequest();

	public void socketAcknowledgeRequest();

	public void socketTillIdRequest(String tillId);

	public void socketPurchaseAcknowledgementResponse();

	public void socketCancelPurchaseRequest();

	public void addAcknowkedgeTimer();

	public void serverResponseWithCommand(String command, Object xmlData);

	public void serverNoResponse();

	public void addCallbackListener(ILoyaltyTCPSocketServiceCallback iLoyaltyTCPSocketServiceCallback);

	public void removeCallbackListener(ILoyaltyTCPSocketServiceCallback iLoyaltyTCPSocketServiceCallback); 
}
