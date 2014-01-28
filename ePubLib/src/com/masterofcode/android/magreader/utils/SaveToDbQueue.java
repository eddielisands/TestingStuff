package com.masterofcode.android.magreader.utils;

import org.kroz.activerecord.ActiveRecordException;

import com.masterofcode.android.magreader.db.entity.FeedItem;
import com.masterofcode.android.magreader.db.entity.FeedType;

public class SaveToDbQueue {
	
	public static synchronized void saveToDbAsyncQueue(FeedItem item, FeedType typeItem) {
		if (typeItem == null) {
			try {
				item.save();
			} catch (ActiveRecordException e) {
				e.printStackTrace();
			}
		} else {
			try {
				typeItem.save();
			} catch (ActiveRecordException e) {
				e.printStackTrace();
			}
		}
	}
}
