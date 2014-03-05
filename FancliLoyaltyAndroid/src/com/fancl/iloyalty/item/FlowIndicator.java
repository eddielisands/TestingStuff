package com.fancl.iloyalty.item;

import com.fancl.iloyalty.item.ViewFlow.ViewSwitchListener;

public interface FlowIndicator extends ViewSwitchListener {  

	public void setViewFlow(ViewFlow view);  

	public void onScrolled(int h, int v, int oldh, int oldv);  
}