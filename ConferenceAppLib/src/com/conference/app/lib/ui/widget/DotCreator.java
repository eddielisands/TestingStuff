package com.conference.app.lib.ui.widget;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.conference.app.lib.R;

public final class DotCreator {
    private static final String TAG = DotCreator.class.getName();
    private static final boolean DEBUG = false;

    private DotCreator() {
    };

    public static View[] createDots(final Context ctx, final int count) {
        final View[] views = new View[count];

        for (int i = 0; i < count; i++) {
            final LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            final LinearLayout parent = new LinearLayout(ctx);
            parent.setLayoutParams(layout);
            parent.setBackgroundColor(ctx.getResources().getColor(android.R.color.transparent));

            for (int j = 0; j < count; j++) {
                if (j == i) {
                    parent.addView(getActiveDot(ctx), j);
                } else {
                    parent.addView(getInactiveDot(ctx), j);
                }
            }

            views[i] = parent;
        }

        return views;
    }

    private static View getActiveDot(final Context ctx) {
        final ImageView activeDotView = getParentDotView(ctx);
        activeDotView.setImageResource(R.drawable.pos_dot);
        return activeDotView;
    }

    private static View getInactiveDot(final Context ctx) {
        final ImageView inactiveDotView = getParentDotView(ctx);
        inactiveDotView.setImageResource(R.drawable.neg_dot);
        return inactiveDotView;
    }

    private static ImageView getParentDotView(final Context ctx) {
        final ImageView parentDotView = new ImageView(ctx);
        final float imagePXSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, ctx.getResources().getDisplayMetrics());
        final float paddingPXSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, ctx.getResources().getDisplayMetrics());
        parentDotView.setPadding((int) paddingPXSize, (int) paddingPXSize, (int) paddingPXSize, (int) paddingPXSize);
        parentDotView.setLayoutParams(new LayoutParams((int) imagePXSize, (int) imagePXSize));

        return parentDotView;
    }
}
