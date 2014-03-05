package com.fancl.iloyalty.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.adapter.CustomSpinnerAdapter;
import com.fancl.iloyalty.util.LogController;

public class CustomSpinnerActivity extends AndroidProjectFrameworkActivity {
	// WRT <<iloyalty_flow_V1.7_20120713.pdf>> screen 1.2.1, 1.2.2
		
	private ListView spinnerView;
	
	private RelativeLayout customSpinnerLayout;
	
	private CustomSpinnerAdapter customSpinnerAdapter;
	
	private String[] chooses;
	
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.custom_spinner_page);
        
        spinnerView = (ListView)findViewById(R.id.custom_spinner_list);
        
        chooses = (String[]) this.getIntent().getExtras().getSerializable(Constants.CUSTOM_SPINNER_STRING_ARRAY);
        
        this.setupSpaceLayout();
	}
	
	private void setupSpaceLayout() {		
		// List View		
		customSpinnerAdapter = new CustomSpinnerAdapter(this, this, handler, chooses);
		ListView customSpinnerListView = (ListView)spinnerView.findViewById(R.id.custom_spinner_list);
		customSpinnerListView.setAdapter(customSpinnerAdapter);
		
//		final Intent intent = new Intent(this, WhatsHotDetailActivity.class);
		
		customSpinnerListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			    // When clicked, show a toast with the TextView text
				LogController.log("position : " + position + "| id : " + id);
				
//				HotItem hotItem = whatsHotList.get(position);
				
//				intent.putExtra(Constants.CUSTOM_SPINNER_KEY, hotItem);
//				startActivity(intent);/
				
				Intent resultIntent = new Intent();
				resultIntent.putExtra(Constants.CUSTOM_SPINNER_RETURN_KEY, position);
				setResult(CustomSpinnerActivity.RESULT_OK, resultIntent);
				finish();
				
			}
		});
	}
}
