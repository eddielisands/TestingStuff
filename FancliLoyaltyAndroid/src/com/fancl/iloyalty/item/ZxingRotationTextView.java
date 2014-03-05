package com.fancl.iloyalty.item;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.fancl.iloyalty.R;
import com.fancl.iloyalty.util.LogController;

public class ZxingRotationTextView extends TextView {

	public enum AlignType {TOP, MIDDLE, BOTTOM};

	private Context context;
	private Paint paint;
	private String text;
	private float textSize;
	private int degree;

	private AlignType alignType;

	public ZxingRotationTextView(Context context, AlignType alignType)
	{
		super(context);

		this.context = context;
		this.alignType = alignType;
		this.setWillNotDraw(false);
		paint = new Paint();		
		paint.setColor(this.getResources().getColor(R.color.White));
		final float scale = context.getResources().getDisplayMetrics().density;
		paint.setTextSize(15 * scale + 0.5f);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		setSingleLine(false);
	}

	public void setRototeDegree(int degree) {
		this.degree = degree;
	}

	public void setText(String text) {
		this.text = text;
		this.textSize = paint.measureText(text);

		Rect rect = new Rect();
		paint.getTextBounds(text, 0, text.length(), rect);
		LayoutParams layoutParams = getLayoutParams();

		if(layoutParams == null)
		{
			layoutParams = new LayoutParams(0, 0);
		}

		layoutParams.width = (rect.bottom - rect.top) * 2;
		layoutParams.height = Math.round((rect.right - rect.left) * 1.2F);
		setLayoutParams(layoutParams);

		invalidate();
	}

	public void setTextSize(float size) {
		final float scale = context.getResources().getDisplayMetrics().density;
		paint.setTextSize(size * scale + 0.5f);
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Rect rect = new Rect();
		paint.getTextBounds(text, 0, text.length(), rect);
		float width = rect.bottom - rect.top;
		float height = rect.right - rect.left;
		canvas.save();
		canvas.rotate(degree, (getWidth() / 2F), (getHeight() / 2F));
		if (alignType == AlignType.TOP) {
			canvas.drawText(text, (getWidth() / 2F - textSize / 2F), 0, paint);
		} else if (alignType == AlignType.MIDDLE) {
			canvas.drawText(text, (getWidth() / 2F - textSize / 2F), getHeight() / 2F + width / 2F, paint);
		} else {
			canvas.drawText(text, (getWidth() / 2F - textSize / 2F), getHeight(), paint);
		}
		canvas.restore();

		LogController.log("ZxingRotationTextView onDraw");
	}
}