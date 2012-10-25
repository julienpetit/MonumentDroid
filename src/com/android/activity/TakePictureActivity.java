package com.android.activity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.content.ContentValues;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class TakePictureActivity extends Activity implements SurfaceHolder.Callback, PictureCallback, ShutterCallback {


	private Camera camera;
	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		// Cache la barre de titre
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main);

		surfaceView = (SurfaceView)this.findViewById(R.id.surface);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


		Button prendrePhoto = (Button)this.findViewById(R.id.takepicture);
		prendrePhoto.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				camera.takePicture(TakePictureActivity.this, null, TakePictureActivity.this);
			}
		});

	}


	/*
	 * Méthode appelée après un changement de taille ou de format de la surface.
	 * */
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub
		if(camera != null){
			camera.stopPreview();
			Camera.Parameters p = this.camera.getParameters();
			p.setPreviewSize(width, height);
			this.camera.setParameters(p);

			try {
				this.camera.setPreviewDisplay(holder);
				this.camera.startPreview();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/*
	 * Méthode appelée immédiatement après la création de la surface
	 * */
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		camera = Camera.open();
	}

	/*
	 * Méthode appelée immédiatement avant la destruction de la surface
	 * */
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if(camera != null){
			camera.stopPreview();
			camera.release();
		}
	}

	public void onPictureTaken(byte[] data, Camera arg1) {
		// TODO Auto-generated method stub
		ContentValues values = new ContentValues();
		values.put(Media.TITLE, "Mon image");
		values.put(Media.DESCRIPTION, "Image prise par le téléphone");
		Uri uri = getContentResolver().insert(Images.Media.EXTERNAL_CONTENT_URI, values);

		OutputStream os;
		try {
			os = getContentResolver().openOutputStream(uri);
			os.write(data);
			os.flush();
			os.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		camera.startPreview();
	}


	public void onShutter() {
		// TODO Auto-generated method stub
		Log.d(getClass().getSimpleName(), "Clic Clac !");
	}  
}