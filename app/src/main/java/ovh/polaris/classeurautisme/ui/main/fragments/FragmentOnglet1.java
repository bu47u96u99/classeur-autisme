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

public class FragmentOnglet1 extends Fragment{
	
	/** Attributs */	
		/* Constantes */
		private static final int numeroOnglet = 1;
		private static final String TAG = "Onglet_1";

		
	/** Methodes */
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    	View view = inflater.inflate(R.layout.onglet_1, container, false);

	        for(int i=0;i<GestionVariables.layout_picto[numeroOnglet -1].length;i++){
	        	GestionVariables.layout_picto[numeroOnglet -1][i] = (LinearLayout) view.findViewById(GestionVariables.ids_layoutPicto[numeroOnglet -1][i]);
	        	GestionVariables.layout_picto[numeroOnglet -1][i].setOnDragListener(new ClassDragListener());
	        	
	        }
		    
		    
		    for(int i=0; i<GestionVariables.images[numeroOnglet -1].length; i++) {
	    	    GestionVariables.images[numeroOnglet -1][i] = (ImageView) view.findViewById(GestionVariables.ids_picto[numeroOnglet - 1][i]);
	    	    GestionVariables.images[numeroOnglet -1][i].setOnTouchListener(new ClassTouchListener());
		    }
		   
		    GestionOnglets.initialiserOnglet(numeroOnglet - 1, GestionVariables.images[numeroOnglet - 1]);

		    /* Activite lancee */
			Log.v(TAG,"activity start.");
		    GestionVariables.activity_start_main[numeroOnglet -1]=true;
		    
		    return view;
	    }
	    


}	

