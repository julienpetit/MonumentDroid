package com.iutbm.monumentdroid.map;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class MyItemizedOverlay extends BalloonItemizedOverlay<OverlayItem>{
	private ArrayList<OverlayItem> m_overlays = new ArrayList<OverlayItem>();
	private Context c;

	public MyItemizedOverlay(Drawable defaultMarker, MapView mapView) {
		super(boundCenter(defaultMarker), mapView);
		c= mapView.getContext();
	}

	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return m_overlays.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return m_overlays.size();

	}
	public void addOverlay(OverlayItem overlay) {
		m_overlays.add(overlay);
		populate();
	}

	void clearOverlay() {
		m_overlays.clear();
		populate();
	}

}