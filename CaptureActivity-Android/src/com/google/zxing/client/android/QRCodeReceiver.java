package com.google.zxing.client.android;

import java.util.HashMap;

public interface QRCodeReceiver {
	static public final String CARD_COLLECTION_RESULT_ALREADY_ADDED = "CARD_COLLECTION_RESULT_ALREADY_ADDED";
	static public final String CARD_COLLECTION_RESULT_IS_INVALID = "CARD_COLLECTION_RESULT_IS_INVALID";
	static public final String CARD_COLLECTION_RESULT_ADDED_SUCESSFULLY = "CARD_COLLECTION_RESULT_ADDED_SUCESSFULLY";
	
	public HashMap<String, String> qrCodeStringReceived(String qrCode);
}
