package com.fancl.iloyalty.item;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.fancl.iloyalty.util.LogController;

public class VerticalScrollView extends ScrollView {
private float xDistance, yDistance, lastX, lastY;

	public VerticalScrollView(Context context, AttributeSet attrs) {
	    super(context, attrs);
	}
	
	public VerticalScrollView(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
	    switch (ev.getAction()) {
	        case MotionEvent.ACTION_DOWN:
	            xDistance = yDistance = 0f;
	            lastX = ev.getX();
	            lastY = ev.getY();
	            LogController.log("ACTION_DOWN");
	        	LogController.log("xDistance:" + xDistance + "|yDistance:" + yDistance);
	            LogController.log("lastX:" + lastX + "|lastY:" + lastY);
	            break;
	        case MotionEvent.ACTION_MOVE:
	            final float curX = ev.getX();
	            final float curY = ev.getY();
	            xDistance += Math.abs(curX - lastX);
	            yDistance += Math.abs(curY - lastY);
	            lastX = curX;
	            lastY = curY;
	            LogController.log("ACTION_MOVE");
	            LogController.log("xDistance:" + xDistance + "|yDistance:" + yDistance);
	            LogController.log("lastX:" + lastX + "|lastY:" + lastY); 
	            if(xDistance > yDistance)
	                return false;
	    }
	
	    return super.onInterceptTouchEvent(ev);
	}
}