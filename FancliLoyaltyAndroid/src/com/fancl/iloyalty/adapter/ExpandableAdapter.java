package com.fancl.iloyalty.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fancl.iloyalty.R;
import com.fancl.iloyalty.pojo.ShopRegion;

public class ExpandableAdapter extends BaseExpandableListAdapter{
	
	private Context context;
	List<Map<String, String>> groups;
	List<List<Map<String, String>>> childs;
	private List<ShopRegion> shopParentList = new ArrayList<ShopRegion>();
	
	public ExpandableAdapter(Context context, List<Map<String, String>> groups, List<List<Map<String, String>>> childs)
	{
	this.groups = groups;
	this.childs = childs;
	this.context = context;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childs.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		String text = ((Map<String, String>) getChild(groupPosition, childPosition)).get("child");
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


		RelativeLayout childLayout = (RelativeLayout) layoutInflater.inflate(R.layout.shop_child_row, null);
		if (childPosition%2 != 0) {
			childLayout.setBackgroundColor(context.getResources().getColor(R.color.gray));
		}else{
			childLayout.setBackgroundColor(context.getResources().getColor(R.color.LightGrey));
		}
		
		TextView tv = (TextView) childLayout.findViewById(R.id.child_row_textview);
		tv.setText(text);
		

		return childLayout;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return childs.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return groups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return groups.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		String text = groups.get(groupPosition).get("group");
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		RelativeLayout linearLayout = (RelativeLayout) layoutInflater.inflate(R.layout.shop_parent_row, null);
		TextView textView = (TextView)linearLayout.findViewById(R.id.group_row_textview);
		textView.setText(text);

		return linearLayout;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}
	
//	public void setShopParentList(List<ShopRegion> shopParentList) {
//		this.shopParentList = shopParentList;
//		
//
//	}

}
