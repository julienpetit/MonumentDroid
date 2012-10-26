package map;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
// TODO Auto-generated constructor stub
// TODO Auto-generated method stub
// TODO Auto-generated constructor stub

public class MyItimizedOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> arrayListOverlayItem = new ArrayList<OverlayItem>();
	private Context context;


	public MyItimizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}

	public MyItimizedOverlay(Drawable defaultMarker, MapView mapView) {
		super(boundCenterBottom(defaultMarker));
		this.context = mapView.getContext();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return arrayListOverlayItem.get(i);
	}

	@Override
	public int size() {
		return arrayListOverlayItem.size();
	}

	public void addOverlayItem(OverlayItem overlay) {
		arrayListOverlayItem.add(overlay);
		populate();
	}

	@Override
	protected boolean onTap(int index) {
		OverlayItem item = arrayListOverlayItem.get(index);
		Toast dialog = Toast.makeText(context,arrayListOverlayItem.toString() , Toast.LENGTH_LONG);
		dialog.setText(item.getTitle() + "\n" + item.getSnippet());
		dialog.show();
		return true;
	}

}