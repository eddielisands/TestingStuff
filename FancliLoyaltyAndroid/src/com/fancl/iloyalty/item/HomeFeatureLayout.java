package com.fancl.iloyalty.item;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.fancl.iloyalty.R;
import com.fancl.iloyalty.util.LogController;

public class HomeFeatureLayout extends HorizontalScrollView {
    private static final int SWIPE_MIN_DISTANCE = 5;
    private static final int SWIPE_THRESHOLD_VELOCITY = 300;
 
    private Context context;
    private ArrayList mItems = null;
    private GestureDetector mGestureDetector;
    private int mActiveFeature = 0;
 
    public HomeFeatureLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }
 
    public HomeFeatureLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }
 
    public HomeFeatureLayout(Context context) {
        super(context);
        this.context = context;
    }
 
    public void setFeatureItems(ArrayList<String> items){
        LinearLayout internalWrapper = new LinearLayout(getContext());
        internalWrapper.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        internalWrapper.setOrientation(LinearLayout.HORIZONTAL);
        this.addView(internalWrapper);
        this.mItems = items;
        LogController.log("0");
        for(int i = 0; i< items.size();i++){
        	LinearLayout featureLayout = new LinearLayout(context);
        	featureLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        	if (i%2 == 0) {
        		featureLayout.setBackgroundColor(this.getResources().getColor(R.color.blue));
        	} else if (i%2 == 1) {
        		featureLayout.setBackgroundColor(this.getResources().getColor(R.color.half_yellow));
        	}
//            LinearLayout featureLayout = (LinearLayout) View.inflate(this.getContext(),R.layout.homefeature,null);
            //...
          //Create the view for each screen in the scroll view
            //...
            
            
            internalWrapper.addView(featureLayout);
        }
        LogController.log("1");
        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //If the user swipes
                if (mGestureDetector.onTouchEvent(event)) {
                    return true;
                }
                else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL ){
                    int scrollX = getScrollX();
                    int featureWidth = v.getMeasuredWidth();
                    mActiveFeature = ((scrollX + (featureWidth/2))/featureWidth);
                    int scrollTo = mActiveFeature*featureWidth;
                    smoothScrollTo(scrollTo, 0);
                    return true;
                }
                else{
                    return false;
                }
            }
        });
        LogController.log("2");
        mGestureDetector = new GestureDetector(new MyGestureDetector());
        LogController.log("3");
    }
        
    class MyGestureDetector extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                //right to left
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    int featureWidth = getMeasuredWidth();
                    mActiveFeature = (mActiveFeature < (mItems.size() - 1))? mActiveFeature + 1:mItems.size() -1;
                    smoothScrollTo(mActiveFeature*featureWidth, 0);
                    return true;
                }
                //left to right
                else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    int featureWidth = getMeasuredWidth();
                    mActiveFeature = (mActiveFeature > 0)? mActiveFeature - 1:0;
                    smoothScrollTo(mActiveFeature*featureWidth, 0);
                    return true;
                }
            } catch (Exception e) {
//                    Log.e("Fling", "There was an error processing the Fling event:" + e.getMessage());
            	LogController.log("There was an error processing the Fling event:" + e.getMessage());
            }
            return false;
        }
    }
}