package ovh.polaris.classeurautisme.utils;

import java.io.File;

public class GestionProfils {
	
    public static File creerDossierProfil(String nom_profil){
		final File dossier = new File(GestionVariables.dossier_profils+File.separator+nom_profil);
		if(!dossier.exists()){
			dossier.mkdir();
		}
		return dossier;
	}

    
    public static void supprimerProfil(String nom_profil){
    	File dossier = new File(GestionVariables.dossier_profils+File.separator+nom_profil);
    	GestionFichiers.suppressionContenuDossier(dossier);
    	dossier.delete();
    }
}
