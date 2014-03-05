package com.fancl.iloyalty.activity.shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.activity.MainTabFragmentActivity;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.exception.GeneralException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.pojo.Shop;
import com.fancl.iloyalty.service.LocaleService;
import com.fancl.iloyalty.util.DataUtil;
import com.fancl.iloyalty.util.LogController;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ShopMapActivity extends MainTabFragmentActivity implements LocationSource, LocationListener {

	final int RQS_GooglePlayServices = 1;

	private View shopMapLayout;
	private GoogleMap googleMap;
	private List<Shop> shopList;
	private HashMap<Marker, Shop> shopMarkerMap;
	private List<Marker> markerList;

	SupportMapFragment mapFragment;
	LocationManager myLocationManager = null;
	OnLocationChangedListener myLocationListener = null;
	LocationListener locationListener = null;

	LocaleService localeService;

	private Location currentLocation;

	private boolean isShowArrow = false;
	private Shop selectedShop;

	// WRT <<iloyalty_flow_V1.7_20120713.pdf>> screen 6.3.1, 6.3.5

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = this.getIntent();
		isShowArrow = intent.getBooleanExtra("SHOW_ARROW", false);
		selectedShop = (Shop) intent.getExtras().getSerializable(Constants.SELECTED_SHOP_ITEM_KEY);

		localeService = GeneralServiceFactory.getLocaleService();
		headerTitleTextView.setText(this.getResources().getString(R.string.shop_map));

		this.setupSpaceLayout();

		this.setupMenuButtonListener(4, true);
		
		navigationBarLeftBtn.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onResume() {
		super.onResume();

		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

		if (resultCode == ConnectionResult.SUCCESS){
			//			Toast.makeText(getApplicationContext(), 
			//					"isGooglePlayServicesAvailable SUCCESS", 
			//					Toast.LENGTH_LONG).show();

			myLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
					1000, 1, this);

			//Replaces the location source of the my-location layer.
			googleMap.setLocationSource(this);

		}else{
			GooglePlayServicesUtil.getErrorDialog(resultCode, this, RQS_GooglePlayServices); 
		}

	}

	@Override
	protected void onPause() {
		googleMap.setLocationSource(null);
		myLocationManager.removeUpdates(this);

		super.onPause();
	}

	private void setupSpaceLayout() {
		// TODO Auto-generated method stub
		shopMapLayout = (RelativeLayout) this.getLayoutInflater().inflate(
				R.layout.shop_map, null);
		spaceLayout.addView(shopMapLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		locationListener = this;
		FragmentManager myFragmentManager = getSupportFragmentManager();
		mapFragment = (SupportMapFragment)myFragmentManager.findFragmentById(R.id.map);
		googleMap = mapFragment.getMap();

		googleMap.setMyLocationEnabled(true);
		googleMap.getUiSettings().setMyLocationButtonEnabled(false);
		googleMap.getUiSettings().setCompassEnabled(true);

		myLocationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

		final View mapView = getSupportFragmentManager().findFragmentById(R.id.map).getView();
		if (mapView.getViewTreeObserver().isAlive()) {
			mapView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				@SuppressLint("NewApi") // We check which build version we are using.
				@Override
				public void onGlobalLayout() {
					final LatLng latLng = new LatLng(22.3000, 114.1667);
					LatLngBounds bounds = new LatLngBounds.Builder()
					.include(latLng)
					.build();

					if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
						mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this); 
					} else {
						mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this); 
					}
					googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, Math.round(DataUtil.dip2px(application, 100))));
				}});    
		}
		this.loadAllShopsFromDatabase();
	}

	private void loadAllShopsFromDatabase() {
		markerList = new ArrayList<Marker>();
		shopMarkerMap = new HashMap<Marker, Shop>();

		try {
			shopList = CustomServiceFactory.getAboutFanclService()
					.getFullShopList();

			if (shopList.size() > 0) {
				new Thread(new Runnable(){

					@Override
					public void run() {


						for (int i = 0; i < shopList.size(); i++) {
							final Shop shop = (Shop) shopList.get(i);

							handler.post(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									addMarker(shop);
								}
							});
						}

						if (!isShowArrow) {
							handler.postDelayed(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									moveToSelectedShop();
								}
							}, 50);
						}
					}
				}).start();
			}

		} catch (GeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void moveToNearestShop() {
		if (currentLocation == null) {
			return;
		}
		if (shopList != null) {
			if (shopList.size() == 0) {
				return;
			}
			int nearestShop = 0;
			double distance = Double.MAX_VALUE;
			for (int i = 0; i < shopList.size(); i++) {
				Shop shop = shopList.get(i);
				if (shop.getLatitude().length() != 0 && shop.getLongitude().length() != 0) {
					Location shopLocation = new Location("Shop");
					shopLocation.setLatitude(Double.valueOf(shop.getLatitude()));
					shopLocation.setLongitude(Double.valueOf(shop.getLongitude()));

					double checkDistance = Double.MAX_VALUE;
					checkDistance = currentLocation.distanceTo(shopLocation);

					if (checkDistance < distance ) {
						distance = checkDistance;
						nearestShop = i;
					}
				}
			}

			try {
				Shop shop = shopList.get(nearestShop);
				Marker marker = markerList.get(nearestShop);

				LatLng nearLatLng = new LatLng(Double.valueOf(shop.getLatitude()), Double.valueOf(shop.getLongitude()));
				LatLng userLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
				LatLngBounds bounds = new LatLngBounds.Builder()
				.include(nearLatLng)
				.include(userLatLng)
				.build();

				googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, Math.round(DataUtil.dip2px(this, 150))));
				marker.showInfoWindow();

			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	private void moveToSelectedShop() {
		if (selectedShop != null) {
			int shopLocation = -1;
			boolean isFound = false;
			for (int i = 0; i < shopList.size(); i++) {
				Shop shop = shopList.get(i);

				if (shop.getObjectId().equals(selectedShop.getObjectId())) {
					shopLocation = i;
					isFound = true;
					break;
				}
			}
			if ((isFound == true) && (shopLocation != -1)) {
				Shop shop = shopList.get(shopLocation);
				Marker marker = markerList.get(shopLocation);
				LatLng shopLatLng = new LatLng(Double.valueOf(shop.getLatitude()), Double.valueOf(shop.getLongitude()));

				googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(shopLatLng, 15));
				marker.showInfoWindow();
			}
		}
	}

	private void addMarker(Shop shop) {
//		LogController.log("shopName:"+shop.getTitleZh()+",shopMap:"+shop.getLatitude()+","+shop.getLongitude());
		if (shop.getLatitude().length() == 0 || shop.getLongitude().length() == 0) {

			final LatLng latLng = new LatLng(0, 0);
			Marker marker = googleMap.addMarker(new MarkerOptions()
			.position(latLng)
			.icon(BitmapDescriptorFactory.fromResource(R.drawable.sub_cat_on)));
			shopMarkerMap.put(marker, shop);
			markerList.add(marker);
			
			return;
		}

		
		double latValue = Double.parseDouble(shop.getLatitude());
		double longValue = Double.parseDouble(shop.getLongitude());
		final LatLng latLng = new LatLng(latValue, longValue);

		if (shop.getType().equals("fancl")) {
			Marker marker = googleMap.addMarker(new MarkerOptions()
			.position(latLng)
			.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_blue)));
			shopMarkerMap.put(marker, shop);
			markerList.add(marker);
		}
		else {
			Marker marker = googleMap.addMarker(new MarkerOptions()
			.position(latLng)
			.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_green)));
			shopMarkerMap.put(marker, shop);
			markerList.add(marker);
		}

		googleMap.setInfoWindowAdapter(new InfoWindowAdapter() {

			@Override
			public View getInfoWindow(Marker arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public View getInfoContents(Marker arg0) {
				// TODO Auto-generated method stub
				Shop shop = shopMarkerMap.get(arg0);
				final String address = localeService.textByLangaugeChooser(application, shop.getAddressEn(), shop.getAddressZh(), shop.getAddressSc());

				View myContentsView = getLayoutInflater().inflate(R.layout.map_info_box, null);
				ImageView tvImage = ((ImageView)myContentsView.findViewById(R.id.map_info_image));
				if (shop.getType().equals("fancl")) {
					tvImage.setImageDrawable(application.getResources().getDrawable(R.drawable.dummy_map));
				}
				else {
					tvImage.setImageDrawable(application.getResources().getDrawable(R.drawable.dummy_map_fnh));
				}
				TextView tvTitle = ((TextView)myContentsView.findViewById(R.id.map_info_title));
				tvTitle.setText(address);
				TextView tvSnippet = ((TextView)myContentsView.findViewById(R.id.map_info_snippet));
				ImageView arrowImage = ((ImageView)myContentsView.findViewById(R.id.map_info_arrow));
				if (isShowArrow) {
					tvSnippet.setVisibility(View.VISIBLE);
					tvSnippet.setText(application.getResources().getString(R.string.distance) + getDistance(shop) + application.getResources().getString(R.string.meter));
					arrowImage.setVisibility(View.VISIBLE);
				}
				else {
					tvSnippet.setVisibility(View.GONE);
					arrowImage.setVisibility(View.GONE);
				}
				return myContentsView;
			}
		});
		if (isShowArrow) {
			googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

				@Override
				public void onInfoWindowClick(Marker arg0) {
					// TODO Auto-generated method stub
					Shop shop = shopMarkerMap.get(arg0);
					startActivity(CustomServiceFactory.getDetailContentService().getShopDetailActivity(shop, ShopMapActivity.this, 4));
					
					try {
						CustomServiceFactory.getSettingService().addUserLogWithSection("Shop", "Store Detail", "",shop.getObjectId() , shop.getTitleEn(), "View", "");
					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
	}

	private int getDistance(Shop shop) {
		Location shopLocation = new Location("Shop");
		shopLocation.setLatitude(Double.valueOf(shop.getLatitude()));
		shopLocation.setLongitude(Double.valueOf(shop.getLongitude()));

		double checkDistance = Double.MAX_VALUE;
		checkDistance = currentLocation.distanceTo(shopLocation);
		return (int)(checkDistance + 0.5);
	}

	@Override
	public void activate(OnLocationChangedListener listener) {
		myLocationListener = listener;
	}

	@Override
	public void deactivate() {
		myLocationListener = null;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		if (myLocationListener != null) {
			currentLocation = location;
			myLocationListener.onLocationChanged(location);

			if (isShowArrow) {
				moveToNearestShop();
			}
			myLocationListener = null;
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
}
