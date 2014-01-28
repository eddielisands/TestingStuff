package com.masterofcode.android.magreader.inapp;

import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.ActiveRecordException;
import org.kroz.activerecord.EntitiesHelper;

import android.content.Context;
import android.util.Log;

import com.masterofcode.android.magreader.application.JtjApplication;
import com.masterofcode.android.magreader.db.entity.IssueItem;
import com.masterofcode.android.magreader.db.entity.PurchaseItem;
import com.masterofcode.android.magreader.inapp.Consts.PurchaseState;

public class PurchasedManager {

	private static PurchasedManager			instance = null;

	private ActiveRecordBase				dbPurchase;

	public PurchasedManager()
	{
		super();
		this.dbPurchase = JtjApplication.getInstance().getPurchaseDatabase();
	}

	static public PurchasedManager GetInstance()
	{
		if(instance==null)
		{
			instance = new PurchasedManager();
		}
		return instance;
	}

	public void insertOrder(Context context, String orderId, String productId,
			PurchaseState purchaseState, long purchaseTime, String developerPayload){
		PurchaseItem purchasedItem = new PurchaseItem(orderId, productId, purchaseState.name(), purchaseTime, developerPayload);
		if (Consts.DEBUG) {
			Log.d("Billing", "InsertOrder..." + orderId + " productID: " + productId + " dbPurchase= " + dbPurchase);
		}
		try {
			PurchaseItem moveItem = dbPurchase.newEntity(PurchaseItem.class);
			EntitiesHelper.copyFieldsWithoutID(moveItem, purchasedItem);
			if (Consts.DEBUG) {
				Log.d("Billing", "InsertingOrder...moveItem: " + moveItem + " dbPurchase= " + dbPurchase);
			}
			if(!dbPurchase.isOpen()) dbPurchase.open();
			if (Consts.DEBUG) {
				Log.d("Billing", "InsertingOrder...dbPurchase.isOpen(): " + dbPurchase.isOpen());
			}
			moveItem.save();
			//dbPurchase.close();
			if (Consts.DEBUG) {
				Log.d("Billing", "InsertingOrder...was saved: " + moveItem.product_id  );
			}
			if (Consts.DEBUG) {
				Log.d("Billing", "InsertingOrder...dbPurchase.isOpen(): " + dbPurchase.isOpen());
			}
		} catch (ActiveRecordException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
