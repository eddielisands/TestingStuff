package com.fancl.iloyalty.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.DialogInterface;

import com.fancl.iloyalty.AndroidProjectApplication;
import com.fancl.iloyalty.ApiConstant;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.parser.FanclGeneralParser;
import com.fancl.iloyalty.service.ILoyaltyTCPSocketService;
import com.fancl.iloyalty.service.callback.ILoyaltyTCPSocketServiceCallback;
import com.fancl.iloyalty.util.Command;
import com.fancl.iloyalty.util.CommandPackage;
import com.fancl.iloyalty.util.LogController;
import com.fancl.iloyalty.util.XMLUtil;

public class ILoyaltyTCPSocketServiceImpl implements ILoyaltyTCPSocketService {
	private Socket socket;
	private Timer timer;
	private InputStream inputStream;
	private OutputStream outputStream;
	private boolean isRunning = false;

	private List<ILoyaltyTCPSocketServiceCallback> callbackList;

	public ILoyaltyTCPSocketServiceImpl()
	{
		callbackList = new ArrayList<ILoyaltyTCPSocketServiceCallback>();
	}

	@Override
	public void socketConnectRequest() {
		// TODO Auto-generated method stub
		try {
			if (socket == null) {
				socket = new Socket(ApiConstant.SOCKET_DOMAIN_NAME, ApiConstant.SOCKET_PORT);
				socket.setReceiveBufferSize(1024);
				this.inputStream = this.socket.getInputStream();
				this.outputStream = this.socket.getOutputStream();
			}
			String memberId = xmlWithTagAndElement("membershipId", CustomServiceFactory.getAccountService().currentMemberId());
			String xml = createXMLContent(memberId);
			try {
				CommandPackage commandPackage = new CommandPackage(Command.CS_CONNECT_REQUEST, xml);
				this.sendData(commandPackage);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			isRunning = true;
			addAcknowkedgeTimer();

			readData();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void socketDisconnectRequest() {
		// TODO Auto-generated method stub
		if (timer != null) {
			timer.cancel();
			timer.purge();
			timer = null;
		}

		if (socket == null) {
			return;
		}

		String memberId = xmlWithTagAndElement("membershipId", CustomServiceFactory.getAccountService().currentMemberId());
		String xml = createXMLContent(memberId);
		try {
			CommandPackage commandPackage = new CommandPackage(Command.CS_DISCONNECT_REQUEST, xml);
			this.sendData(commandPackage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		isRunning = false;
		try {
			socket.close();
			socket = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void socketAcknowledgeRequest() {
		// TODO Auto-generated method stub
		if (!isRunning) {
			return;
		}
		try {
			CommandPackage commandPackage = new CommandPackage(Command.CS_ACK_REQUEST, null);
			this.sendData(commandPackage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void socketTillIdRequest(String aTillId) {
		// TODO Auto-generated method stub
		String memberId = xmlWithTagAndElement("membershipId", CustomServiceFactory.getAccountService().currentMemberId());
		String tillId = xmlWithTagAndElement("tillId", aTillId);
		String xmlContent = memberId + tillId;
		String xml = createXMLContent(xmlContent);
		try {
			CommandPackage commandPackage = new CommandPackage(Command.CS_TILL_ID_REQUEST, xml);
			this.sendData(commandPackage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void socketPurchaseAcknowledgementResponse() {
		// TODO Auto-generated method stub
		try {
			CommandPackage commandPackage = new CommandPackage(Command.SC_PURCHASE_ACKNOWLEDGE_REQUEST, null);
			this.sendData(commandPackage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void socketCancelPurchaseRequest() {
		// TODO Auto-generated method stub
		String memberId = xmlWithTagAndElement("membershipId", CustomServiceFactory.getAccountService().currentMemberId());
		String xml = createXMLContent(memberId);
		try {
			CommandPackage commandPackage = new CommandPackage(Command.CS_CANCEL_PURCHASE_ACKNOWLEDGE_REQUEST, xml);
			this.sendData(commandPackage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void addAcknowkedgeTimer() {
		// TODO Auto-generated method stub
		if (timer != null) {
			timer.cancel();
			timer.purge();
			timer = null;
		}

		TimerTask timerTask = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				socketAcknowledgeRequest();
			}
		};
		timer = new Timer();
		timer.schedule(timerTask, 500);
//		timer.scheduleAtFixedRate(timerTask, 500, 10000);
	}

	@Override
	public void serverResponseWithCommand(String command, Object xmlData) {
		// TODO Auto-generated method stub

	}

	@Override
	public void serverNoResponse() {
		// TODO Auto-generated method stub
		GeneralServiceFactory.getAlertDialogService().makeNativeDialog(AndroidProjectApplication.application.getFrontActivity(), "", 
				"Server No Response (Socket)",
				AndroidProjectApplication.application.getString(R.string.ok_btn_title),
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
			}
		},
		"", null, false, false);
	}

	// For command with XML
	private String createXMLContent(String xmlContent) {
		String xmlString = xmlStart();
		xmlString = xmlString + xmlContent;
		xmlString = xmlEndWithXMLString(xmlString);
		LogController.log("xmlString: " + xmlString);
		return xmlString;
	}

	private void sendData(CommandPackage commandPackage) throws Exception {
		this.outputStream.write(commandPackage.getFullByteArray());
	}

	public void readData() {
		while (isRunning) {
			try {
				if (this.inputStream.available() > 0) {
					LogController.log("readData");

					byte[] tmpByte = null;
					tmpByte = toByteArray(inputStream);
					
					if (tmpByte.length > 4) {
						LogController.log("tmpByte.length > 4");
//						LogController.log("tmpByte.length > 4 - readData inputStream-->byte2:"+ tmpByte1);

						int dataLength = tmpByte.length;
						LogController.log("tmpByte.length > 4 - readData dataLength int: " + dataLength);

						byte[] packageLengthInByte = new byte[CommandPackage.PACKAGE_LENGTH_SIZE];
						System.arraycopy(tmpByte, 0, packageLengthInByte, 0, CommandPackage.PACKAGE_LENGTH_SIZE);
//						inputStream.read(packageLengthInByte);
						int packageLength = packageLengthInByte.length;
						LogController.log("tmpByte.length > 4 - readData packageLength int: " + packageLength);

						byte[] commandInByte = new byte[CommandPackage.COMMAND_SIZE];
						System.arraycopy(tmpByte, CommandPackage.PACKAGE_LENGTH_SIZE, commandInByte, 0, CommandPackage.COMMAND_SIZE);
//						inputStream.read(commandInByte);
						LogController.log("tmpByte.length > 4 - readData commandString: " + CommandPackage.readCommandByte(commandInByte));

						byte[] contentInByte = new byte[tmpByte.length - CommandPackage.COMMAND_SIZE - CommandPackage.PACKAGE_LENGTH_SIZE];
						System.arraycopy(tmpByte, (CommandPackage.COMMAND_SIZE + CommandPackage.PACKAGE_LENGTH_SIZE), contentInByte, 0, (tmpByte.length - CommandPackage.COMMAND_SIZE - CommandPackage.PACKAGE_LENGTH_SIZE));
//						inputStream.read(contentInByte);
						LogController.log("tmpByte.length > 4 - readData contentString: " + CommandPackage.convertContent2Readable(contentInByte));

						this.processDate(packageLengthInByte, commandInByte, contentInByte);
					}
					else {
						
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		try {
			this.inputStream.close();
			this.outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static byte[] toByteArray(InputStream input) throws IOException
	{
		byte[] buffer = new byte[1024];
		//	    LogController.log("readData toByteArray:111111");
		int bytesRead;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		//	    LogController.log("readData toByteArray:2222222");
		//	    while ((bytesRead = input.read(buffer)) != -1)
		//	    {
		output.write(buffer, 0, input.read(buffer));
		//	        LogController.log("readData toByteArray:3333333");
		//	        LogController.log("readData toByteArray:4444:"+(bytesRead = input.read(buffer)));
		//	    }
		//	    LogController.log("readData output.toByteArray():"+ output.toByteArray());
		return output.toByteArray();
	}
	
	public static byte[] stream2Bytes(InputStream ins) {
		byte [] availableBytes = new byte [0];

		try {
			byte [] buffer = new byte[1024];
			ByteArrayOutputStream outs = new ByteArrayOutputStream();

			int read = 0;
			while ((read = ins.read(buffer)) != -1 ) {
				outs.write(buffer, 0, read);
				LogController.log("readData toByteArray:4444:"+ (read = ins.read(buffer)));
			}

			ins.close();
			outs.close();
			availableBytes = outs.toByteArray();

		} catch (Exception e) { 
			e.printStackTrace();
		}

		return availableBytes;
	}

	public static byte[] toByteArrayUsingJava(InputStream is) throws IOException{ 
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
		int reads = is.read(); 
		while(reads != -1){ 
			baos.write(reads); reads = is.read(); 
			LogController.log("readData toByteArray:4444:"+ reads);
		} 
		return baos.toByteArray(); 
	}



	private void processDate(byte[] packageLengthInByte, byte[] commandInByte, byte[] contentInByte) throws Exception {
		CommandPackage commandPackage = new CommandPackage(packageLengthInByte, commandInByte, contentInByte);
		System.out.println("from Server = commandString:" + commandPackage.getCommandString() + ",content: " + commandPackage.getContent() +",contentInByte:"+ contentInByte);

		if(commandPackage.getCommandString().equals(ApiConstant.COMMAND_PURHCASE_ACK_RESPONSE)){

			if(contentInByte != null){

				InputStream myInputStream = new ByteArrayInputStream(contentInByte);

				LogController.log("myInputStream:" + myInputStream);

				FanclGeneralParser fanclGeneralparser = new FanclGeneralParser();
				Boolean posStatus = fanclGeneralparser.checkPosStatus(XMLUtil.toDocument(myInputStream));

				LogController.log("posStatus:" + posStatus);

				if(posStatus){
					for(ILoyaltyTCPSocketServiceCallback iLoyaltyTCPSocketServiceCallback : callbackList) {
						if(iLoyaltyTCPSocketServiceCallback != null) {
							iLoyaltyTCPSocketServiceCallback.didReceiveTCPSocketResult(commandPackage.getContent());
							iLoyaltyTCPSocketServiceCallback.didReceiveTCPSocketIsSuccess(true);
						}
					}

				}else{
					for(ILoyaltyTCPSocketServiceCallback iLoyaltyTCPSocketServiceCallback : callbackList) {
						if(iLoyaltyTCPSocketServiceCallback != null) {
							iLoyaltyTCPSocketServiceCallback.didReceiveTCPSocketResult(commandPackage.getContent());
							iLoyaltyTCPSocketServiceCallback.didReceiveTCPSocketIsSuccess(false);
						}
					}

				}

			}else{
				for(ILoyaltyTCPSocketServiceCallback iLoyaltyTCPSocketServiceCallback : callbackList) {
					if(iLoyaltyTCPSocketServiceCallback != null) {
						iLoyaltyTCPSocketServiceCallback.didReceiveTCPSocketResult(commandPackage.getContent());
						iLoyaltyTCPSocketServiceCallback.didReceiveTCPSocketIsSuccess(false);
					}
				}

			}
		}


	}

	// CreateXML
	private String xmlStart() {
		String xmlHeader = "<?xml version =\"1.0\" encoding = \"UTF-8\"?><iloyalty>";
		return xmlHeader;
	}

	private String xmlWithTagAndElement(String tag, String element) {
		String contentString = "<" + tag + ">" + element + "</" + tag + ">";
		return contentString;
	}

	private String xmlEndWithXMLString(String xmlString) {
		String returnString = xmlString + "</iloyalty>";
		LogController.log(returnString);
		return returnString;
	}

	@Override
	public void addCallbackListener(
			ILoyaltyTCPSocketServiceCallback iLoyaltyTCPSocketServiceCallback) {
		// TODO Auto-generated method stub
		if(iLoyaltyTCPSocketServiceCallback != null)
		{
			callbackList.add(iLoyaltyTCPSocketServiceCallback);
		}
	}

	@Override
	public void removeCallbackListener(
			ILoyaltyTCPSocketServiceCallback iLoyaltyTCPSocketServiceCallback) {
		// TODO Auto-generated method stub
		if(iLoyaltyTCPSocketServiceCallback != null)
		{
			int i;
			ILoyaltyTCPSocketServiceCallback callbackInList;

			for(i = 0 ; i < callbackList.size() ; i++)
			{
				callbackInList = callbackList.get(i);

				if(callbackInList != null)
				{
					if(callbackInList.equals(iLoyaltyTCPSocketServiceCallback))
					{
						callbackList.remove(i);
						i--;
					}
				}
			}
		}
	}

}
