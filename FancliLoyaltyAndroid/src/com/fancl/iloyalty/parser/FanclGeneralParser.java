package com.fancl.iloyalty.parser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.exception.GeneralException;
import com.fancl.iloyalty.util.StringUtil;


public class FanclGeneralParser {
	public boolean checkPosStatus(Document document) throws GeneralException
	{
		NodeList nodeList = document.getElementsByTagName("iloyalty");
		Node node;
		NodeList insideList;
		Element insideElement;

		if (nodeList != null)
		{
			String status = null;
			
			Element iloyaltyElement = null;
			
			for (int i = 0; i < nodeList.getLength(); i++)
			{
				node = nodeList.item(i);

				if (node.getNodeType() == Node.ELEMENT_NODE)
				{
					iloyaltyElement = (Element) node;

					try
					{
						//Get <status>
						insideList = iloyaltyElement.getElementsByTagName("status");
						insideElement = (Element) insideList.item(0);
						status = ((Node)insideElement.getChildNodes().item(0)).getNodeValue();
					}
					catch (Exception e) {
						
					}
					
					
					if(!StringUtil.isStringEmpty(status))
					{
						if(status.trim().equals(Constants.STATUS_CODE_FAIL))
						{
							return false;
						}
					}
					
				}
			}
		}

		return true;
	}
	

}
