package ovh.polaris.classeurautisme.utils;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

public class GestionPicto {
	
	/* Methode pour ajout picto */
	public static void mettreJour(ImageView image, Drawable drawable){
		if(image != null){
			image.setImageDrawable(drawable);
			image.setVisibility(View.VISIBLE);}
	}
	
	
	/* Masquer une image */
	public static void masquer(ImageView image){
		if(image != null){
			image.setVisibility(View.INVISIBLE);}
	}
	
	
	
	
	/* Chercher case de libre */ 
	public static int chercherPremiereCaseLibre(int numero_onglet){
		for(int i=0;i<GestionVariables.NB_PICTO;i++){
			if(!GestionVariables.visibility[numero_onglet][i] && GestionVariables.nombre_picto_onglet[numero_onglet]!=8){
				
				return i;
			}
		}
		return -1;
	}
}
