package com.fancl.iloyalty.item;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

import com.fancl.iloyalty.R;

public class CircleFlowIndicator extends View implements FlowIndicator,
		AnimationListener {
	private static final int STYLE_STROKE = 0;
	private static final int STYLE_FILL = 1;

	private float radius = 4;
	private float circleSeparation = 2 * radius + radius;
	private float activeRadius = 0.5f;
	private int fadeOutTime = 0;
	private final Paint mPaintInactive = new Paint(Paint.ANTI_ALIAS_FLAG);
	private final Paint mPaintActive = new Paint(Paint.ANTI_ALIAS_FLAG);
	private ViewFlow viewFlow;
	private int currentScroll = 0;
	private int flowWidth = 0;
	private FadeTimer timer;
	public AnimationListener animationListener = this;
	private Animation animation;
	private boolean mCentered = false;

	public CircleFlowIndicator(Context context) {
		super(context);
		initColors(0xFFFFFFFF, 0xFFFFFFFF, STYLE_FILL, STYLE_STROKE);
	}

	public CircleFlowIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 检索风格
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.CircleFlowIndicator);

		// 变得不活跃的圆式,默认为“填补”
		int activeType = a.getInt(R.styleable.CircleFlowIndicator_activeType,
				STYLE_FILL);

		int activeDefaultColor = 0xFFFFFFFF;

		// 得到一个自定义颜色
		int activeColor = a
				.getColor(R.styleable.CircleFlowIndicator_activeColor,
						activeDefaultColor);

		// 变得不活跃的圆式
		int inactiveType = a.getInt(
				R.styleable.CircleFlowIndicator_inactiveType, STYLE_STROKE);

		int inactiveDefaultColor = 0x44FFFFFF;
		int inactiveColor = a.getColor(
				R.styleable.CircleFlowIndicator_inactiveColor,
				inactiveDefaultColor);

		//检索半径
		radius = a.getDimension(R.styleable.CircleFlowIndicator_radius_viewflow, 4.0f);

		circleSeparation = a.getDimension(
				R.styleable.CircleFlowIndicator_circleSeparation, 2 * radius
						+ radius);
		activeRadius = a.getDimension(
				R.styleable.CircleFlowIndicator_activeRadius, 0.5f);
		// 检索淡出时间
		fadeOutTime = a.getInt(R.styleable.CircleFlowIndicator_fadeOut, 0);

		mCentered = a.getBoolean(R.styleable.CircleFlowIndicator_centered_viewflow,
				false);

		initColors(activeColor, inactiveColor, activeType, inactiveType);
	}

	private void initColors(int activeColor, int inactiveColor, int activeType,
			int inactiveType) {
		// 选择涂料类型给定类型attr
		switch (inactiveType) {
		case STYLE_FILL:
			mPaintInactive.setStyle(Style.FILL);
			break;
		default:
			mPaintInactive.setStyle(Style.STROKE);
		}
		mPaintInactive.setColor(inactiveColor);

		//
		switch (activeType) {
		case STYLE_STROKE:
			mPaintActive.setStyle(Style.STROKE);
			break;
		default:
			mPaintActive.setStyle(Style.FILL);
		}
		mPaintActive.setColor(activeColor);
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int count = 3;
		if (viewFlow != null) {
			count = viewFlow.getViewsCount();
		}

		// 第一圈数量应该抵消,使得整个事情集中
		float centeringOffset = 0;

		int leftPadding = getPaddingLeft();

		// 画圆
		for (int iLoop = 0; iLoop < count; iLoop++) {
			canvas.drawCircle(leftPadding + radius + (iLoop * circleSeparation)
					+ centeringOffset, getPaddingTop() + radius, radius,
					mPaintInactive);
		}
		float cx = 0;
		if (flowWidth != 0) {
			// 画的实心圆根据当前的滚动的值
			cx = (currentScroll * circleSeparation) / flowWidth;
		}
		// 流宽度已经更新
		canvas.drawCircle(leftPadding + radius + cx + centeringOffset,
				getPaddingTop() + radius, radius + activeRadius, mPaintActive);
	}

	@Override
	public void onSwitched(View view, int position) {
	}

	@Override
	public void setViewFlow(ViewFlow view) {
		resetTimer();
		viewFlow = view;
		flowWidth = viewFlow.getWidth();
		invalidate();
	}

	@Override
	public void onScrolled(int h, int v, int oldh, int oldv) {
		setVisibility(View.VISIBLE);
		resetTimer();
		flowWidth = viewFlow.getWidth();
		if (viewFlow.getViewsCount() * flowWidth != 0) {
			currentScroll = h % (viewFlow.getViewsCount() * flowWidth);
		} else {
			currentScroll = h;
		}
		invalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(measureWidth(widthMeasureSpec),
				measureHeight(heightMeasureSpec));
	}

	private int measureWidth(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		// 设置尺寸
		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		}
		// 计算宽度根据视图计数
		else {
			int count = 3;
			if (viewFlow != null) {
				count = viewFlow.getViewsCount();
			}
			float temp = circleSeparation - 2 * radius;
			result = (int) (getPaddingLeft() + getPaddingRight()
					+ (count * 2 * radius) + (count - 1) * temp + 1);
			//
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	/**
	 * 
	 * 确定这一视图的高度
	 */
	private int measureHeight(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		}
		// 确定这一视图的高度
		else {
			result = (int) (2 * radius + getPaddingTop() + getPaddingBottom() + 1);
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	public void setFillColor(int color) {
		mPaintActive.setColor(color);
		invalidate();
	}

	public void setStrokeColor(int color) {
		mPaintInactive.setColor(color);
		invalidate();
	}

	/**
	 * 重置为0的淡出计时器
	 */
	private void resetTimer() {
		//只有设置定时器,如果我们有一个超时至少1毫秒
		if (fadeOutTime > 0) {
			// 检查是否我们需要创建一个新的计时器
			if (timer == null || timer._run == false) {
				timer = new FadeTimer();
				timer.execute();
			} else {
				timer.resetTimer();
			}
		}
	}

	private class FadeTimer extends AsyncTask<Void, Void, Void> {
		private int timer = 0;
		private boolean _run = true;

		public void resetTimer() {
			timer = 0;
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			while (_run) {
				try {
					// Wait for a millisecond
					Thread.sleep(1);
					timer++;

					if (timer == fadeOutTime) {
						_run = false;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			animation = AnimationUtils.loadAnimation(getContext(),
					android.R.anim.fade_out);
			animation.setAnimationListener(animationListener);
			startAnimation(animation);
		}
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		setVisibility(View.GONE);
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
	}

	@Override
	public void onAnimationStart(Animation animation) {
	}
}