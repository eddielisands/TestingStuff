package com.conference.app.lib.ui.adapter;

import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.conference.app.lib.ui.widget.DotCreator;

public class SchedulePagerAdapter extends PagerAdapter {
    private static final String TAG = SchedulePagerAdapter.class.getName();
    private static final boolean DEBUG = false;

    private final int maxViews;
    private final List<View> views;
    private final ViewPager parent;

    private View[] dots;
    private Toast dot;

    public SchedulePagerAdapter(final List<View> views, final ViewPager viewPager) {
        super();
        this.views = views;
        this.parent = viewPager;
        maxViews = views.size();
        initDots();
    }

    private void initDots() {
        dot = new Toast(this.parent.getContext());
        dot.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        dot.setDuration(Toast.LENGTH_SHORT);
        dots = DotCreator.createDots(this.parent.getContext(), maxViews);
    }

    public void showDots(final int pos) {
        dot.setView(dots[pos]);
        dot.show();
    }

    public void cancelDots() {
        dot.cancel();
    }

    @Override
    public void startUpdate(final View arg0) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void restoreState(final Parcelable arg0, final ClassLoader arg1) {
    }

    @Override
    public boolean isViewFromObject(final View view, final Object object) {
        return view == ((View) object);
    }

    @Override
    public void destroyItem(final View collection, final int position, final Object view) {
        ((ViewPager) collection).removeView(views.get(position));
    }

    @Override
    public void finishUpdate(final View arg0) {
    }

    @Override
    public int getCount() {
        return maxViews;
    }

    @Override
    public Object instantiateItem(final View arg0, final int arg1) {
        final View view = views.get(arg1);
        parent.addView(view, 0);
        return view;
    }
}
