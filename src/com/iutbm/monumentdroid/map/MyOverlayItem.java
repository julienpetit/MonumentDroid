package com.iutbm.monumentdroid.map;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class MyOverlayItem extends OverlayItem {

	private int idMonument;

	public MyOverlayItem(GeoPoint point, String title, String snippet, int idMonument) {
		super(point, title, snippet);
		this.idMonument = idMonument;
	}

	public int getIdMonument() {
		return idMonument;
	}

	public void setIdMonument(int idMonument) {
		this.idMonument = idMonument;
	}


}
