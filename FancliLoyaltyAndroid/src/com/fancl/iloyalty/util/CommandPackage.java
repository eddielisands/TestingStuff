package com.fancl.iloyalty.util;

import java.io.Serializable;

public class CommandPackage implements Serializable {

	public static final int PACKAGE_LENGTH_SIZE = 3;

	public static final int COMMAND_SIZE = 1;

	private static final long serialVersionUID = -8030116676777996618L;

	private byte[] fullByteArray = null;

	private byte[] packageLengthInByte = null;

	private int packageLength = -1;

	private byte[] commandInByte = null;

	private byte command;

	private byte[] contentInByte = null;

	private String content = null;
	
	private String commandString = null;

	public CommandPackage() throws Exception {
		super();
	}

	public CommandPackage(byte[] packageLengthInByte, byte[] commandInByte, byte[] contentInByte) throws Exception {
		super();
		this.packageLengthInByte = packageLengthInByte;
		this.commandInByte = commandInByte;
		this.contentInByte = contentInByte;
		this.convert2Readable();
	}

	public CommandPackage(byte command, byte[] contentInByte, String type) throws Exception {
		super();
		this.command = command;
		this.contentInByte = contentInByte;

		this.convertCommand2Byte();
	}

	public CommandPackage(byte command, String content) throws Exception {
		super();
		this.command = command;
		System.out.println("content " + content);
		this.content = content;

		this.convert2Byte();
	}

	private void convert2Readable() throws Exception {
		this.packageLength = ByteOperation.byte2Int(this.packageLengthInByte);
		this.command = commandInByte[0];
		this.content = null;
		if (this.contentInByte != null && this.contentInByte.length > 0)
			this.content = new String(this.contentInByte, "UTF-8");
		this.commandString = null;
		
		if (this.commandInByte != null && this.commandInByte.length > 0){
			LogController.log("byteArrayToHexString:" + byteArrayToHexString(this.commandInByte)); 
			this.commandString = byteArrayToHexString(this.commandInByte);
		}
//		this.convert2FullByteArray(); //Eddie Li comment
	}
	
	public static String byteArrayToHexString(byte[] b)
	{
	    StringBuffer sb = new StringBuffer(b.length * 2);
	    for (int i = 0; i < b.length; i++)
	    {
	        int v = b[i] & 0xff;
	        if (v < 16)
	        {
	            sb.append('0');
	        }
	        sb.append(Integer.toHexString(v));
	    }
	    return sb.toString().toUpperCase();
	}

	private void convertCommand2Byte() throws Exception {
		this.commandInByte = ByteOperation.int2Byte(this.command, COMMAND_SIZE);
		this.packageLength = this.contentInByte.length + PACKAGE_LENGTH_SIZE + COMMAND_SIZE;
		this.packageLengthInByte = ByteOperation.int2Byte(this.packageLength, PACKAGE_LENGTH_SIZE);
		this.convert2FullByteArray();
	}

	private void convert2Byte() throws Exception {
		this.commandInByte = ByteOperation.int2Byte(this.command, COMMAND_SIZE);
		this.contentInByte = new byte[0];
		if (this.content != null && !this.content.equals("")) {
			this.contentInByte = this.content.getBytes("UTF-8");
		}
		this.packageLength = this.contentInByte.length + PACKAGE_LENGTH_SIZE + COMMAND_SIZE;
		this.packageLengthInByte = ByteOperation.int2Byte(this.packageLength, PACKAGE_LENGTH_SIZE);
		this.convert2FullByteArray();
	}
	
	

	private void convert2FullByteArray() throws Exception {
		this.fullByteArray = new byte[this.packageLength];
		System.arraycopy(this.packageLengthInByte, 0, this.fullByteArray, 0, PACKAGE_LENGTH_SIZE);
		System.arraycopy(this.commandInByte, 0, this.fullByteArray, PACKAGE_LENGTH_SIZE, COMMAND_SIZE);
		if(this.contentInByte != null)
			System.arraycopy(this.contentInByte, 0, this.fullByteArray, PACKAGE_LENGTH_SIZE + COMMAND_SIZE, this.contentInByte.length);

	}
	
	

	public byte[] getFullByteArray() {
		return fullByteArray;
	}

	public void setFullByteArray(byte[] fullByteArray) {
		this.fullByteArray = fullByteArray;
	}

	public byte[] getPackageLengthInByte() {
		return packageLengthInByte;
	}

	public void setPackageLengthInByte(byte[] packageLengthInByte) {
		this.packageLengthInByte = packageLengthInByte;
	}

	public int getPackageLength() {
		return packageLength;
	}

	public void setPackageLength(int packageLength) {
		this.packageLength = packageLength;
	}

	public byte[] getCommandInByte() {
		return commandInByte;
	}

	public void setCommandInByte(byte[] commandInByte) {
		this.commandInByte = commandInByte;
	}

	public byte getCommand() {
		return command;
	}

	public void setCommand(byte command) {
		this.command = command;
	}

	public byte[] getContentInByte() {
		return contentInByte;
	}

	public void setContentInByte(byte[] contentInByte) {
		this.contentInByte = contentInByte;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getCommandString() {
		return commandString;
	}
	
	public static String readCommandByte(byte[] byteCommand) {
		String string = byteArrayToHexString(byteCommand);
		return string;
	}
	
	public static String convertContent2Readable(byte[] byteContent) throws Exception {
		String hexString = byteArrayToHexString(byteContent);
		LogController.log("hexString: " + hexString);
		String string = new String(byteContent, "UTF-8");
		return string;
	}
	
}