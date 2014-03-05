package it.sephiroth.android.library.imagezoom.test;


import it.sephiroth.android.library.imagezoom.test.ImageViewTouch.OnImageViewTouchDoubleTapListener;
import it.sephiroth.android.library.imagezoom.test.ImageViewTouch.OnImageViewTouchSingleTapListener;
import it.sephiroth.android.library.imagezoom.test.ImageViewTouchBase.DisplayType;
import it.sephiroth.android.library.imagezoom.test.ImageViewTouchBase.OnDrawableChangeListener;
import it.sephiroth.android.library.imagezoom.test.factory.GeneralServiceFactory;
import it.sephiroth.android.library.imagezoom.test.util.LogController;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class ImageViewTestActivity extends Activity {

	private static final String LOG_TAG = "image-test";

	ImageViewTouch mImage;
	Button mButton1;
	static int displayTypeCount = 0; 
	boolean needFullScreen = true;

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		GeneralServiceFactory.getImageService().prepareFileStructure();
		GeneralServiceFactory.getThreadService().startImageExecutor();

		requestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView( R.layout.main );
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		LogController.log("onContentChanged");
		mImage = (ImageViewTouch) findViewById( R.id.image );

		// set the default image display type
		mImage.setDisplayType( DisplayType.FIT_IF_BIGGER );

		mButton1 = (Button) findViewById( R.id.button );

		mButton1.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick( View v ) {
				selectRandomImage();
			}
		} );

		mImage.setSingleTapListener( new OnImageViewTouchSingleTapListener() {

			@Override
			public void onSingleTapConfirmed() {
				Log.d( LOG_TAG, "onSingleTapConfirmed" );
				toggleFullscreen(needFullScreen);
			}
		} );

		mImage.setDoubleTapListener( new OnImageViewTouchDoubleTapListener() {

			@Override
			public void onDoubleTap() {
				Log.d( LOG_TAG, "onDoubleTap" );
			}
		} );

		mImage.setOnDrawableChangedListener( new OnDrawableChangeListener() {

			@Override
			public void onDrawableChanged( Drawable drawable ) {
				Log.i( LOG_TAG, "onBitmapChanged: " + drawable );
			}
		} );
	}

	@Override
	public void onConfigurationChanged( Configuration newConfig ) {
		super.onConfigurationChanged( newConfig );
	}

	public void selectRandomImage() {
		mImage.setRequestingUrl(new Handler(), "http://eofdreams.com/data_images/dreams/car/car-02.jpg", Constants.IMAGE_FOLDER);

	}

	private void toggleFullscreen(boolean fullscreen)
	{
		WindowManager.LayoutParams attrs = getWindow().getAttributes();
		if (fullscreen)
		{
			attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
			mButton1.setVisibility(View.GONE);
			needFullScreen = false;
		}
		else
		{
			attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
			mButton1.setVisibility(View.VISIBLE);
			needFullScreen = true;
		}
		getWindow().setAttributes(attrs);
	}
}
