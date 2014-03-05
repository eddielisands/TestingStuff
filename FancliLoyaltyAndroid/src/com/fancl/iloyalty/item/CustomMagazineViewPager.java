package com.fancl.iloyalty.item;
 
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
 
public class CustomMagazineViewPager extends ViewPager {
 
	private static final String TAG = "ImageViewTouchViewPager";
	public static final String VIEW_PAGER_OBJECT_TAG = "image#";
	
	private int previousPosition;
	private boolean enabled;
	
	private OnPageSelectedListener onPageSelectedListener;
 
	public CustomMagazineViewPager(Context context) {
		super(context);
		init();
	}
 
	public CustomMagazineViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
 
	public void setOnPageSelectedListener(OnPageSelectedListener listener) {
		onPageSelectedListener = listener;
	}
	
	@Override
	protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
		if (v instanceof ImageViewTouch) {
			return ((ImageViewTouch) v).canScroll(dx);
		} else {
			return super.canScroll(v, checkV, dx, x, y);
		}
	}
	
	public interface OnPageSelectedListener {
		
		public void onPageSelected(int position);
		
	}
 
	private void init() {
		this.enabled = true;
		previousPosition = getCurrentItem();
		
		setOnPageChangeListener(new SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				if (onPageSelectedListener != null) {
					onPageSelectedListener.onPageSelected(position);
				}
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				if (state == SCROLL_STATE_SETTLING && previousPosition != getCurrentItem()) {
					try {
						ImageViewTouch imageViewTouch = (ImageViewTouch)
							findViewWithTag(VIEW_PAGER_OBJECT_TAG + getCurrentItem());
						if (imageViewTouch != null) {
							imageViewTouch.zoomTo(1f, 300);
						}
						
						previousPosition = getCurrentItem();
					} catch (ClassCastException ex) {
						Log.e(TAG, "This view pager should have only ImageViewTouch as a children.", ex);
					}
				}
			}
		});
	}
	
	@Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onTouchEvent(event);
        }
  
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onInterceptTouchEvent(event);
        }
 
        return false;
    }
 
	public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}