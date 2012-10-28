package com.iutbm.monumentdroid.utilities;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

/*
 * Classe outil pour faciliter la gestion de fichier
 */
public class FileUtilities {

	/*
	 * Convertie un byte[] en image et l'enregistre sur la carte SD dans le dossier image
	 */
	public static boolean StoreByteImage(byte[] imageData, String folder, String pictureName, String format) {

		File sdImageMainDirectory = new File(folder);
		FileOutputStream fileOutputStream = null;
		try {

			BitmapFactory.Options options=new BitmapFactory.Options();
			options.inSampleSize = 5;

			Bitmap myImage = BitmapFactory.decodeByteArray(imageData, 0,
					imageData.length,options);

			fileOutputStream = new FileOutputStream(
					sdImageMainDirectory.toString() +"/" + pictureName + "." + format);

			BufferedOutputStream bos = new BufferedOutputStream(
					fileOutputStream);

			if(format.equals("PNG"))
			{
				myImage.compress(CompressFormat.PNG, 0, bos);
			}
			else
			{
				myImage.compress(CompressFormat.JPEG, 50, bos);
			}

			bos.flush();
			bos.close();

		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	public static boolean deleteDirectory(File file) {
		if( file.exists() ) {
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for(int i=0; i<files.length; i++) {
					if(files[i].isDirectory()) {
						deleteDirectory(files[i]);
					} else {
						files[i].delete();
					}
				}
			}
		}
		return(file.delete());
	}
}