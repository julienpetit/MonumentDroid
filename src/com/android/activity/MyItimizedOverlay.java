package com.android.activity;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MyItimizedOverlay extends ItemizedOverlay<OverlayItem>
{

	private ArrayList<OverlayItem> arrayListOverlayItem = new ArrayList<OverlayItem>();

	public MyItimizedOverlay(Drawable defaultMarker)
	{
		super(boundCenterBottom(defaultMarker));
	}

	@Override
	protected OverlayItem createItem(int i)
	{
		return arrayListOverlayItem.get(i);
	}

	@Override
	public int size()
	{
		return arrayListOverlayItem.size();
	}

	public void addOverlayItem(OverlayItem overlay)
	{
		arrayListOverlayItem.add(overlay);
		populate();
	}

}