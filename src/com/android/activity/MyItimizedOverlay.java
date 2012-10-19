package com.android.activity;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;
// TODO Auto-generated constructor stub
// TODO Auto-generated method stub
// TODO Auto-generated constructor stub

public class MyItimizedOverlay extends ItemizedOverlay<OverlayItem>
{

	private ArrayList<OverlayItem> arrayListOverlayItem = new ArrayList<OverlayItem>();
	private Context context;

	public MyItimizedOverlay(Drawable defaultMarker, Context pContext)
	{
		super(boundCenterBottom(defaultMarker));
		this.context = pContext;
	}

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

	@Override
	protected boolean onTap(int index)
	{
		OverlayItem item = arrayListOverlayItem.get(index);
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		dialog.show();
		return true;
	}

}
