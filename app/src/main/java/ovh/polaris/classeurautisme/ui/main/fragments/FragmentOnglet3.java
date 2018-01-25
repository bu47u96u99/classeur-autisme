package ovh.polaris.classeurautisme.ui.main.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import ovh.polaris.classeurautisme.R;
import ovh.polaris.classeurautisme.ui.misc.ClassDragListener;
import ovh.polaris.classeurautisme.ui.misc.ClassTouchListener;
import ovh.polaris.classeurautisme.utils.GestionOnglets;
import ovh.polaris.classeurautisme.utils.GestionVariables;

public class FragmentOnglet3 extends Fragment {
	
	private static final String TAG = "Onglet_3";
	private static final int numero_onglet= 3;
	
	 
	   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        
	    	View view = inflater.inflate(R.layout.onglet_3, container, false);

	        for(int i=0;i<GestionVariables.layout_picto[numero_onglet-1].length;i++){
	        	GestionVariables.layout_picto[numero_onglet-1][i] = (LinearLayout) view.findViewById(GestionVariables.ids_layoutPicto[numero_onglet-1][i]);
	        	GestionVariables.layout_picto[numero_onglet-1][i].setOnDragListener(new ClassDragListener());
	        }
		    
		    
		    for(int i=0; i<GestionVariables.images[numero_onglet-1].length; i++) {            		
	    	    GestionVariables.images[numero_onglet-1][i] = (ImageView) view.findViewById(GestionVariables.ids_picto[numero_onglet - 1][i]);
	    	    GestionVariables.images[numero_onglet-1][i].setOnTouchListener(new ClassTouchListener());
		    }
		    
		    GestionOnglets.initialiserOnglet(numero_onglet - 1, GestionVariables.images[numero_onglet - 1]);

		    Log.v(TAG,"activite lancee.");
		    GestionVariables.activity_start_main[numero_onglet-1]=true;
		    
		    return view;
	    }
	
}
