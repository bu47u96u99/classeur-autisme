package ovh.polaris.classeurautisme.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.*;

public class GestionSauvegarde {
	
	
	public static void savePreference(String nom_fichier, File dossier){
		GestionFichiers.saveDonneeFichier(Boolean.toString(GestionVariables.sonActive),dossier,nom_fichier);
		GestionFichiers.saveDonneeFichier(Boolean.toString(GestionVariables.passwordActive),dossier,nom_fichier);
		GestionFichiers.saveDonneeFichier(GestionVariables.password,dossier,nom_fichier);
		GestionFichiers.saveDonneeFichier(GestionVariables.dureeCompteur,dossier,nom_fichier);
		GestionFichiers.saveDonneeFichier(GestionVariables.vitesse_elocution,dossier,nom_fichier);
		GestionFichiers.saveDonneeFichier(Boolean.toString(GestionVariables.pointagePicto),dossier,nom_fichier);
		GestionFichiers.saveDonneeFichier(Boolean.toString(GestionVariables.pointageBarre),dossier,nom_fichier);
		GestionFichiers.saveDonneeFichier(Boolean.toString(GestionVariables.zoom_actif),dossier,nom_fichier);
		GestionFichiers.saveDonneeFichier(GestionVariables.cibleTime,dossier,nom_fichier);
		GestionFichiers.saveDonneeFichier(GestionVariables.sensibilite,dossier,nom_fichier);
	}
	
	/* 
	 * -> Sauvegarde
	 */
	public static void sauvegardeInternalStorage(File dossier){

		for(int i=0;i<GestionVariables.NB_ONGLETS;i++){
			for(int j=0;j<GestionVariables.NB_PICTO;j++){
				if(GestionVariables.visibility[i][j]){
					GestionBitmap.saveBitmap(GestionVariables.drawable[i][j],dossier,"" + i +"_" + j + "_cache_bitmap.png");					
					GestionFichiers.saveDonneeFichier("" + i +"_" + j +"_"+ GestionVariables.syntaxe[i][j],dossier, "syntaxe.dat");
				}
			}
		}
		savePreference("settings.dat",dossier);
	}
	
	
	 /*
	 * -> Initialise le dossier sauvegarde
	 */
	public static File initialiserDossierSauvegarde(Activity activity){
		final File dossier = new File(activity.getBaseContext().getFilesDir()+File.separator+"save");
		if(!dossier.exists()){
			dossier.mkdir();
			GestionVariables.premierLancement = true;
		}
		return dossier;
	}
	
	/**************************
	 * Gestion de l'ouverture *
	 **************************/
	
	/* Chargement de la configuration sauvegardee lors d'un kill (et non d'une pause)
	 * -> Parcours du dossier cache de l'application
	 * -> Decodage du Bitmap en Drawable et chargement de celui-ci dans la matrice
	 * -> Chargement de la chaine dans le tableau syntaxe et mise a jour d'autres parametres
	 */
	
	public static boolean chargementConfigurationExterne(File dossier,Context context,final int MODE){
		boolean erreur_initialisation = false;
	     if(dossier.exists()){
	    	 for(int i=0;i<dossier.list().length;i++){
	    		 	if(dossier.list()[i].endsWith(".png") && !dossier.list()[i].startsWith("b")){
		        		File image = new File(dossier.getAbsolutePath()+File.separator+dossier.list()[i]);
		        		Bitmap bitmap = GestionBitmap.decodeFile(image);
		        		Drawable drawable = new BitmapDrawable(context.getResources(),bitmap);
		        		int indice_picto = recupererIndices(dossier.list()[i],2);
		        		int indice_onglet = recupererIndices(dossier.list()[i],0);
		                GestionVariables.drawable[indice_onglet][indice_picto] = drawable;
		                GestionVariables.visibility[indice_onglet][indice_picto] = true;
		                GestionPicto.mettreJour(GestionVariables.images[indice_onglet][indice_picto], GestionVariables.drawable[indice_onglet][indice_picto]);		                
	    		 	}
		        }
	    	 File fichier_syntaxe = new File(dossier.getAbsolutePath()+File.separator+"syntaxe.dat");
	    	 if(fichier_syntaxe.exists()){
	    		 try{
	    				InputStream ips=new FileInputStream(fichier_syntaxe); 
	    				InputStreamReader ipsr=new InputStreamReader(ips);
	    				BufferedReader br=new BufferedReader(ipsr);
	    				String ligne;
	    				while ((ligne=br.readLine())!=null){
	    					int indice_onglet = recupererIndices(ligne,0);
	    					int indice_picto = recupererIndices(ligne,2);
	    					GestionVariables.syntaxe[indice_onglet][indice_picto] = ligne.substring(4);
	    				}
	    				br.close(); 
	    			}		
	    			catch (Exception e){
	    				System.out.println(e.toString());
	    			}
	    	 }
	    	 File fichier_settings = new File(dossier.getAbsolutePath()+File.separator+"settings.dat");
	    	 if(fichier_settings.exists()){
	    		 try{
	    				InputStream ips=new FileInputStream(fichier_settings); 
	    				InputStreamReader ipsr=new InputStreamReader(ips);
	    				BufferedReader br=new BufferedReader(ipsr);
	    				String ligne;
	    				ligne=br.readLine();
	    				if(ligne!=null){
	    					
	    					GestionVariables.sonActive = Boolean.valueOf(ligne);
	    					
	    				}
	    				ligne=br.readLine();
	    				if(ligne!=null){
	    					GestionVariables.passwordActive = Boolean.valueOf(ligne);
	    				}
	    				ligne=br.readLine();
	    				if(ligne!=null){
	    					GestionVariables.password = (ligne);
	    				}
	    				ligne=br.readLine();
	    				if(ligne!=null){
	    					try{
	    					GestionVariables.dureeCompteur = Integer.parseInt(ligne);
	    					}catch(NumberFormatException e){System.out.println(e.toString());}
	    				}
	    				ligne=br.readLine();
	    				if(ligne!=null){
	    					try{
	    					GestionVariables.vitesse_elocution = Float.parseFloat(ligne);
	    					}catch(NumberFormatException e){System.out.println(e.toString());}
	    				}
	    				ligne=br.readLine();
	    				if(ligne!=null){
	    					GestionVariables.pointagePicto = Boolean.valueOf(ligne);
	    				}
	    				ligne=br.readLine();
	    				if(ligne!=null){
	    					GestionVariables.pointageBarre = Boolean.valueOf(ligne);
	    				}
	    				ligne=br.readLine();
	    				if(ligne!=null){
	    					GestionVariables.zoom_actif = Boolean.valueOf(ligne);
	    				}
	    				ligne=br.readLine();
	    				if(ligne!=null){
	    					try{
	    					GestionVariables.cibleTime = Integer.valueOf(ligne);
	    					}catch(NumberFormatException e){System.out.println(e.toString());}
	    				}
	    				ligne=br.readLine();
	    				if(ligne!=null){
	    					try{
	    					GestionVariables.sensibilite = Integer.valueOf(ligne);
	    					}catch(NumberFormatException e){System.out.println(e.toString());}
	    				}
	    				br.close(); 	    				
	    			}		
	    			catch (Exception e){
	    				System.out.println(e.toString());
	    			}
	    	 }else{
	    		 erreur_initialisation = true;
	    	 }
	    	 if(MODE==GestionVariables.MODE_CHARGEMENT)
	    		 GestionFichiers.suppressionContenuDossier(dossier);
	     }else{
	    	 erreur_initialisation = true;
	     }
	     
	     return erreur_initialisation;
	}
	

	
	/*
	 * Recupere les indices
	 */
	private static int recupererIndices(String string,int position) {
		return Character.getNumericValue(string.charAt(position));
	}

}
