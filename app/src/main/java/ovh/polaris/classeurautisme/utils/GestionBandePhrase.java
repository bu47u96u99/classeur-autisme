package ovh.polaris.classeurautisme.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import ovh.polaris.classeurautisme.R;


public class GestionBandePhrase {
	
	public static void viderBandePhrase(){
		for(int i=0;i<GestionVariables.images_barre.length;i++){
			if(GestionVariables.visibility_barre[i]){
				int onglet = GestionVariables.sauvegarde_onglet_barre[i];
				int num_case = GestionVariables.sauvegarde_picto_barre[i];
				
				if(GestionVariables.visibility[onglet][num_case]){
					num_case = GestionPicto.chercherPremiereCaseLibre(onglet);					
					while(num_case==-1){					
						onglet=onglet+1;
						if(onglet==6){onglet=0;}
						num_case = GestionPicto.chercherPremiereCaseLibre(onglet);				
					}
				}
				
				GestionVariables.drawable[onglet][num_case]=GestionVariables.drawable_barre[i];
				GestionVariables.syntaxe[onglet][num_case]=GestionVariables.syntaxe_barre[i];
				GestionVariables.visibility[onglet][num_case]=true;
				GestionPicto.mettreJour(GestionVariables.images[onglet][num_case],GestionVariables.drawable_barre[i]);
				
				miseDefautCaseBandePhrase(i);
				GestionPicto.masquer(GestionVariables.images_barre[i]);
			}
		}	
	}
	
	public static void detruireBandePhrase(){
		for(int i=0;i<GestionVariables.images_barre.length;i++){
			miseDefautCaseBandePhrase(i);
			GestionPicto.masquer(GestionVariables.images_barre[i]);
		}
	}
	
	public static void miseDefautCaseBandePhrase(final int i){
		GestionVariables.drawable_barre[i]=GestionVariables.picto_vide;
		GestionVariables.syntaxe_barre[i]="";
		GestionVariables.visibility_barre[i]=false;
	}
	

	

	public static void modeSuppression(Context context, ImageButton bouton_clear_barre, ImageButton bouton_phrase, ImageButton bouton_menu, ViewGroup layoutBarreBouton){
		if(!GestionVariables.mode_suppression) {
			bouton_clear_barre.setVisibility(View.GONE);
			bouton_phrase.setVisibility(View.GONE);
			bouton_menu.setVisibility(View.GONE);
			layoutBarreBouton.setBackgroundResource(R.drawable.ic_suppr);
			viderBandePhrase();
			GestionVariables.mode_suppression = true;
			Toast.makeText(context, "Glissez le pictogramme sur la croix rouge", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	public static void modeNormal(ImageButton bouton_clear_barre, ImageButton bouton_phrase, ImageButton bouton_menu ,ViewGroup layoutBarreBouton){
		bouton_clear_barre.setVisibility(View.VISIBLE);
		bouton_phrase.setVisibility(View.VISIBLE);
		bouton_menu.setVisibility(View.VISIBLE);
		layoutBarreBouton.setBackgroundColor(Color.parseColor("#00000000"));
		GestionVariables.mode_suppression = false;
	}
	
	
	public static void mettreJourBandePhrase(int indice) {
		if(GestionVariables.images_barre[indice]!=null){
			GestionVariables.images_barre[indice].setImageDrawable(GestionVariables.drawable_barre[indice]);
			GestionVariables.images_barre[indice].setVisibility(View.VISIBLE);
		}
	}
	
}
