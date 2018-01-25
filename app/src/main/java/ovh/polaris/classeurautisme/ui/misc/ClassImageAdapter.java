package ovh.polaris.classeurautisme.ui.misc;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import ovh.polaris.classeurautisme.utils.GestionVariables;

import java.util.ArrayList;

public class ClassImageAdapter extends BaseAdapter  {

	   private Context mContext;
	   private ArrayList <Drawable> images;

	   public ClassImageAdapter(Context mContext, ArrayList <Drawable> images) {
	       this.mContext = mContext;
	       this.images = images;
		   LayoutInflater.from(mContext);
	   }

	   public int getCount() {
	       return GestionVariables.drawable_stockage_externe.size();
	   }

	   public Object getItem(int position) {
	       return null;
	   }

	   public long getItemId(int position) {
		   return position;
	   }

	   public View getView(int position, View convertView, ViewGroup parent) {
	       ImageView imageView;
	       if(convertView == null){
	           imageView = new ImageView(mContext);
	           imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
	           imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	           imageView.setPadding(5, 5, 5, 5);
	       }else{
	           imageView = (ImageView) convertView;
	       }
	       imageView.setImageDrawable(images.get(position));
	       return imageView;
	   }

	


	   
	   
}