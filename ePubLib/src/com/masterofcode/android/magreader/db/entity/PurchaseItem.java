package com.masterofcode.android.magreader.db.entity;

import org.kroz.activerecord.ActiveRecordBase;

public class PurchaseItem extends ActiveRecordBase {
	
	public String order_id;
	public String product_id;
	public String state;
	public long purchase_time;
	public String developer_payload;
	
	public PurchaseItem(){
	}
	
	public PurchaseItem(String order_id, String product_id, String state, long purchase_time, String developer_payload){
		this.order_id = order_id;
		this.product_id = product_id;
		this.state = state;
		this.purchase_time = purchase_time;
		this.developer_payload = developer_payload;
	}
}
