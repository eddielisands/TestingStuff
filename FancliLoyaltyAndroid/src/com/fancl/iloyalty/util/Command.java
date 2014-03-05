package com.fancl.iloyalty.util;

public class Command {

	/*******************************/
	
	// 0000 0000, Connect Request (Client -> Server)
	public static final byte CS_CONNECT_REQUEST = (byte) 0x00;

	// 1000 0000, Connect Response (Server -> Client)
	public static final byte SC_CONNECT_RESPONSE = (byte) 0x80;
	
	/*******************************/
	
	// 0000 0001, Disconnect Request (Client -> Server)
	public static final byte CS_DISCONNECT_REQUEST = (byte) 0x01;

	// 1000 0001, Disconnect Response (Server -> Client)
	public static final byte SC_DISCONNECT_RESPONSE = (byte) 0x81;
	
	/*******************************/
	
	// 0000 0010, Ack Request (Client -> Server)
	public static final byte CS_ACK_REQUEST = (byte) 0x02;

	// 1000 0010, Ack Response (Server -> Client)
	public static final byte SC_ACK_RESPONSE = (byte) 0x82;
	
	/*******************************/
	
	// 0000 0011, Send TillID Request (Client -> Server)
	public static final byte CS_TILL_ID_REQUEST = (byte) 0x03;

	// 1000 0011, Send TillID Response (Server -> Client)
	public static final byte SC_TILL_ID_RESPONSE = (byte) 0x83;
	
	/*******************************/
	
	// 0000 0100, Purchase Acknowledge Request (Server -> Client)
	public static final byte SC_PURCHASE_ACKNOWLEDGE_REQUEST = (byte) 0x04;

	// 1000 0111, Purchase Acknowledge Response (Client -> Server)
	public static final byte CS_PURCHASE_ACKNOWLEDGE_RESPONSE = (byte) 0x84;
	
	/*******************************/
	
	// 0000 0101, Cancel Purchase Acknowledge Request (Client -> Server)
	public static final byte CS_CANCEL_PURCHASE_ACKNOWLEDGE_REQUEST = (byte) 0x05;

	// 1000 0101, Cancel Purchase Acknowledge Response (Server -> Client)
	public static final byte SC_CANCEL_PURCHASE_ACKNOWLEDGE_RESPONSE = (byte) 0x85;
	
		
}