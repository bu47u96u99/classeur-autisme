package ovh.polaris.classeurautisme.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class GestionBitmap {
	
	private static final String TAG = "gestionBitmap";

    private GestionBitmap() {}

    /*
         * Décoder un fichier .png en Bitmap
         */
    static public Bitmap decodeFile(File fichier_bitmap){
        Bitmap b = null;
        final int IMAGE_MAX_SIZE = 500;
        BitmapFactory.Options options_bitmap = new BitmapFactory.Options();
        options_bitmap.inJustDecodeBounds = true;
        FileInputStream fis;
		try {
			fis = new FileInputStream(fichier_bitmap);
			BitmapFactory.decodeStream(fis, null, options_bitmap);
	        fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        int scale = 1;
        if (options_bitmap.outHeight > IMAGE_MAX_SIZE || options_bitmap.outWidth > IMAGE_MAX_SIZE) {
            scale = (int)Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE / 
               (double) Math.max(options_bitmap.outHeight, options_bitmap.outWidth)) / Math.log(0.5)));
        }
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        try {
			fis = new FileInputStream(fichier_bitmap);
			b = BitmapFactory.decodeStream(fis, null, o2);
			fis.close();
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}
        return b;
    }

	
	
	/*
	 * Conversion Bitmap to Drawable 
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = null;
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable)drawable).getBitmap();
		}
		try{
			bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
		}catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
		return bitmap;
	}
	
	
	public static void saveBitmap(Drawable drawable, File dossier, String nom_fichier){
		Bitmap bitmap = GestionBitmap.drawableToBitmap(drawable);
		FileOutputStream fOut = null;
		try{
			fOut = new FileOutputStream(dossier+File.separator+nom_fichier);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
		}catch(IOException e) {
            e.printStackTrace();
        }finally {
            try{
                fOut.close();
            }catch (NullPointerException | IOException e){
                e.printStackTrace();
            }
        }
	}
}
