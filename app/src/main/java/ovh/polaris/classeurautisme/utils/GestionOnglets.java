package ovh.polaris.classeurautisme.utils;

import android.widget.ImageView;

public class GestionOnglets {
	
	static public void initialiserTousOnglets(){
		for(int i=0;i<GestionVariables.NB_ONGLETS;i++){
			initialiserOnglet(i,GestionVariables.images[i]);
		}
	}
	
	
	/* Initialiser onglet */
	static public void initialiserOnglet(int onglet, ImageView[] im){
		for(int i=0; i<im.length; i++) {    
			if(im[i]!=null){
        	    if((GestionVariables.visibility[onglet][i]))
        	    	GestionPicto.mettreJour(im[i],GestionVariables.drawable[onglet][i]);
        	    else
        	    	GestionPicto.masquer(im[i]);
			}
		}
    }
	
	public static void remiseDefautGrille(){
		for(int i=0;i<GestionVariables.NB_ONGLETS;i++){
			for(int j=1;j<GestionVariables.NB_PICTO;j++){
				GestionVariables.drawable[i][j] = GestionVariables.picto_vide;
				GestionVariables.visibility[i][j] = false;
				GestionVariables.syntaxe[i][j] = "";
			}
			GestionVariables.drawable[i][0] = GestionVariables.picto_jeveux;
			GestionVariables.visibility[i][0] = true;
			GestionVariables.syntaxe[i][0] = "Je veux";
			GestionVariables.nombre_picto_onglet[i]=1;
		}
		initialiserTousOnglets();
		GestionBandePhrase.detruireBandePhrase();
	}
	
	
}
