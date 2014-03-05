package com.fancl.iloyalty.util;

public class ByteOperation {

	private static final String HEXES = "0123456789ABCDEF";

	public static int byte2Int(byte[] bytes) {
		int result = 0;
		for (int i = 0; i < bytes.length; i++)
			result += ((bytes[i] & 0xFF) << ((bytes.length - i - 1) * 8));
		return result;
	}

	public static byte[] int2Byte(int source, int byteArraySize) {
		byte[] bytes = new byte[byteArraySize];
		for (int i = 0; i < byteArraySize; i++)
			bytes[i] = (byte) (source >>> ((byteArraySize - i - 1) * 8));
		return bytes;
	}

	public static String toHex(byte[] raw) {
		if (raw == null)
			return null;
		StringBuilder hex = new StringBuilder(2 * raw.length);
		for (byte b : raw)
			hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F))).append(" ");
		return hex.toString();
	}

}
