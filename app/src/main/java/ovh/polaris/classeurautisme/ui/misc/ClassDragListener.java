package ovh.polaris.classeurautisme.ui.misc;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.Window;
import android.widget.ImageView;
import ovh.polaris.classeurautisme.R;
import ovh.polaris.classeurautisme.ui.main.ActivityMain;
import ovh.polaris.classeurautisme.utils.GestionBandePhrase;
import ovh.polaris.classeurautisme.utils.GestionPicto;
import ovh.polaris.classeurautisme.utils.GestionVariables;

public class ClassDragListener extends Activity implements OnDragListener  {

    private static final String TAG = "MyDragListener";

    @Override
    public boolean onDrag(View view_cible, DragEvent event) {

        View view_picto = (View) event.getLocalState();                    // view pictogramme
        View view_parent = (View) view_picto.getParent();                // view parent

        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:


                /** Pointage parlant */
                  /* Si on est sur la grille */
                for (int k = 0; k < GestionVariables.NB_ONGLETS; k++) {
                    for (int l = 0; l < GestionVariables.NB_PICTO; l++) {
                        if (view_picto.getId() == GestionVariables.ids_picto[k][l]) {
                            if (GestionVariables.pointagePicto)
                                ActivityMain.pointageParlant(GestionVariables.syntaxe[k][l]);
                            break;
                        }
                    }
                }

		    	  
		    	  /* Si on est sur la barre */
                for (int k = 0; k < GestionVariables.ids_image_barre.length; k++) {
                    if (view_picto.getId() == GestionVariables.ids_image_barre[k]) {
                        if (GestionVariables.pointagePicto)
                            ActivityMain.pointageParlant(GestionVariables.syntaxe_barre[k]);
                        break;
                    }


                }
                break;

            case DragEvent.ACTION_DRAG_LOCATION:

                if (view_parent != view_cible) {
                    GestionVariables.fuite = true;
                }


                int x = ((int) event.getX());
                int y = ((int) event.getY());

                for (int i = 0; i < 6; i++) {
                    for (int j = 0; j < 2; j++) {
                        if (view_cible.getId() == GestionVariables.ids_layoutPicto[i][j]) {
                            if (x < 15) {
                                if (ActivityMain.tabHost.getCurrentTab() != 0) {
                                    if (GestionVariables.compteur_intercalaire_slidees_gauche) {
                                        int tab_cible = ActivityMain.tabHost.getCurrentTab() - 1;
                                        if (tab_cible <= 6 && tab_cible >= 0) {
                                            ActivityMain.tabHost.setCurrentTab(tab_cible);
                                        }
                                        GestionVariables.compteur_intercalaire_slidees_gauche = false;
                                    }
                                }
                            }
                        }
                    }
                }
                for (int i = 0; i < 6; i++) {
                    for (int j = 6; j < 8; j++) {
                        if (view_cible.getId() == GestionVariables.ids_layoutPicto[i][j]) {
                            if (x > GestionVariables.swipeZone) {
                                if (ActivityMain.tabHost.getCurrentTab() != 5) {
                                    if (GestionVariables.compteur_intercalaire_slidees_droite) {
                                        int tab_cible = ActivityMain.tabHost.getCurrentTab() + 1;
                                        if (tab_cible <= 6 && tab_cible >= 0) {
                                            ActivityMain.tabHost.setCurrentTab(tab_cible);
                                        }
                                        GestionVariables.compteur_intercalaire_slidees_droite = false;
                                    }
                                }
                            }
                        }
                    }
                }

                /**************************************************/
                /**  Machin pop up ********************************/
                /**************************************************/

                GestionVariables.endTime = System.currentTimeMillis();
                GestionVariables.touchCount++;
                int deltaX = Math.abs(GestionVariables.location_departX - x);
                int deltaY = Math.abs(GestionVariables.location_departY - y);

                if (deltaX > GestionVariables.sensibilite + 5 || deltaY > GestionVariables.sensibilite + 5) {
                    GestionVariables.fuite = true;
                }


                /** View */
                if (GestionVariables.endTime - GestionVariables.startTime > (GestionVariables.cibleTime * 1000 - 500)
                        && GestionVariables.endTime - GestionVariables.startTime < ((GestionVariables.cibleTime + 1) * 1000 - 850)
                        && !GestionVariables.fuite && view_cible == view_parent
                        && GestionVariables.zoom_actif && !GestionVariables.fenetrePopIm
                        && deltaX < GestionVariables.sensibilite && deltaY < GestionVariables.sensibilite
                        && GestionVariables.touchCount > 15) {

                    Context context = view_cible.getContext();
                    final Dialog settingsDialog = new Dialog(context);
                    settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
                    //settingsDialog.setContentView(inflater.inflate(R.layout.pop_up_image, null));
                    setContentView(View.inflate(context, R.layout.pop_up_image, null));
                    ImageView image = (ImageView) settingsDialog.findViewById(R.id.popup_image);
                    image.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            settingsDialog.dismiss();
                        }
                    });

                    /***************************************************************************************/

		    		/* Si on est sur la grille */
                    for (int k = 0; k < GestionVariables.NB_ONGLETS; k++) {
                        for (int l = 0; l < GestionVariables.NB_PICTO; l++) {
                            if (view_picto.getId() == GestionVariables.ids_picto[k][l]) {
                                image.setImageDrawable(GestionVariables.drawable[k][l]);
                                break;
                            }
                        }
                    }
                    for (int j = 0; j < GestionVariables.ids_image_barre.length; j++) {
                        if (view_picto.getId() == GestionVariables.ids_image_barre[j]) {
                            image.setImageDrawable(GestionVariables.drawable_barre[j]);
                            break;
                        }
                    }
                    settingsDialog.show();
                    GestionVariables.startTime = 0;
                    GestionVariables.endTime = 0;
                    GestionVariables.fenetrePopIm = true;
                    GestionVariables.location_departX = 0;
                    GestionVariables.location_departY = 0;
                    GestionVariables.touchCount = 0;
                }


            case DragEvent.ACTION_DRAG_ENTERED:
                break;

            case DragEvent.ACTION_DRAG_EXITED:
                break;

            case DragEvent.ACTION_DROP:
                /** Deblocage du slide */
                GestionVariables.compteur_intercalaire_slidees_gauche = true;
                GestionVariables.compteur_intercalaire_slidees_droite = true;


                /***********************************************************************************************/
                /** Deplacement picto **************************************************************************/
                /***********************************************************************************************/

			    	  
		    	  /* Si la vue parent est differente de la vue cible */
                if (view_parent != view_cible) {
                    Log.v(TAG, "Drag&Drop::start");
                      /*--------------------------------------------------------------------------------*/
		    		  /* Si la vue parente est un layoutPicto ------------------------------------------*/
		    		  /*--------------------------------------------------------------------------------*/
                    for (int k = 0; k < GestionVariables.NB_ONGLETS; k++) {
                        for (int l = 0; l < GestionVariables.NB_PICTO; l++) {                                                // kl parent mn cible
                            if (view_parent.getId() == (GestionVariables.ids_layoutPicto[k][l])) {

                                /**************************************************************************************/
                                /** Vue parente - layoutPicto *********************************************************/
                                /**************************************************************************************/


                                for (int j = 0; j < GestionVariables.ids_barre.length; j++) {
                                    if (view_cible.getId() == GestionVariables.ids_barre[j]) {
                                        if (!GestionVariables.visibility_barre[j] && !GestionVariables.mode_suppression) {
                                              /*----------------------------------------------------------------------------------------------*/
                                                /* Si la vue cible est la barre inferieure -----------------------------------------------------*/
			  			        			  /*----------------------------------------------------------------------------------------------*/
                                            Log.v(TAG, "parent:layoutPicto:cible:barreinf");
                                            GestionVariables.sauvegarde_onglet_barre[j] = k;
                                            GestionVariables.sauvegarde_picto_barre[j] = l;
                                            GestionVariables.drawable_barre[j] = GestionVariables.drawable[k][l];
                                            GestionVariables.drawable[k][l] = GestionVariables.picto_vide;
                                            GestionVariables.visibility[k][l] = false;
                                            GestionVariables.visibility_barre[j] = true;
                                            GestionVariables.syntaxe_barre[j] = GestionVariables.syntaxe[k][l];
                                            GestionVariables.syntaxe[k][l] = "";
                                            GestionPicto.masquer(GestionVariables.images[k][l]);
                                            GestionBandePhrase.mettreJourBandePhrase(j);
                                            return true;
                                        }
                                    }
                                }
                                              /*----------------------------------------------------------------------------------------------*/
                                for (int m = 0; m < GestionVariables.NB_ONGLETS; m++) {
                                    for (int n = 0; n < GestionVariables.NB_PICTO; n++) {
                                        if (view_cible.getId() == GestionVariables.ids_layoutPicto[m][n]) {
                                            if (!GestionVariables.visibility[m][n]) {
                                              /*----------------------------------------------------------------------------------------------*/
		    			    				  /* Si la vue cible est un autre layout picto ---------------------------------------------------*/
		    				    			  /*----------------------------------------------------------------------------------------------*/
                                                Log.v(TAG, "parent:layoutPicto:cible:layoutPictovide");
                                              /* La vue cible est une case picto et est vide */
                                                if (m != k) {
                                                 /* if(GestionVariables.nombre_picto_onglet[m] == 8){
		    		    							  return false;
		    		    						  }*/
                                                    GestionVariables.nombre_picto_onglet[k]--;
                                                    GestionVariables.nombre_picto_onglet[m]++;
                                                }

                                                GestionVariables.drawable[m][n] = GestionVariables.drawable[k][l];
                                                GestionVariables.drawable[k][l] = GestionVariables.picto_vide;
                                                GestionVariables.visibility[m][n] = true;
                                                GestionVariables.visibility[k][l] = false;
                                                GestionVariables.syntaxe[m][n] = GestionVariables.syntaxe[k][l];
                                                GestionVariables.syntaxe[k][l] = "";
                                                GestionPicto.mettreJour(GestionVariables.images[m][n], GestionVariables.drawable[m][n]);
                                                GestionPicto.masquer(GestionVariables.images[k][l]);
                                                return true;
                                            }
                                        }
                                    }
                                }
                                              /*--------------------------------------------------------------------------------*/
						    				/*-------------------------------------------------------------------------------------*/
                                            /* Si la vue cible est le layout du mode suppression (active) -------------------------*/
						    				/*-------------------------------------------------------------------------------------*/
                                if (view_cible.getId() == GestionVariables.id_layoutBarreBouton && GestionVariables.mode_suppression) {
                                    Log.v(TAG, "parent:layoutPicto:cible:layoutSuppr");
                                    GestionVariables.nombre_picto_onglet[k]--;
                                    GestionVariables.drawable[k][l] = GestionVariables.picto_vide;
                                    GestionVariables.visibility[k][l] = false;
                                    GestionVariables.syntaxe[k][l] = "";
                                    GestionPicto.masquer(GestionVariables.images[k][l]);
                                    return true;
                                }
                            }
                        }
                    }

		    		  
		    		  	/*----------------------------------------------------------------------------------*/
                          /* Si la vue parente est la barre inferieure ---------------------------------------*/
		    		    /*----------------------------------------------------------------------------------*/
                    for (int i = 0; i < GestionVariables.ids_barre.length; i++) {                                        // i-> zone source
                        if (view_parent.getId() == (GestionVariables.ids_barre[i]) && GestionVariables.visibility_barre[i]) {

                            /**************************************************************************************************/
                            /** Vue parente barre *****************************************************************************/
                            /**************************************************************************************************/


                            for (int j = 0; j < GestionVariables.ids_barre.length; j++) {                                // j-> zone cible
                                if (view_cible.getId() == (GestionVariables.ids_barre[j]) && i != j) {
                                    if (!GestionVariables.visibility_barre[j]) {
                                                  /*----------------------------------------------------------------------------------------*/
                                                  /* Si la vue cible est aussi une case differente de la barre inferieure ------------------*/
							      				/*----------------------------------------------------------------------------------------*/
                                        Log.v(TAG, "parent:barre:cible:case_barre");
                                                  /*Si la vue cible est vide de pictogrammes */
                                        GestionVariables.sauvegarde_onglet_barre[j] = GestionVariables.sauvegarde_onglet_barre[i];
                                        GestionVariables.sauvegarde_picto_barre[j] = GestionVariables.sauvegarde_picto_barre[i];
                                        GestionVariables.sauvegarde_onglet_barre[i] = 0;
                                        GestionVariables.sauvegarde_picto_barre[i] = 0;
                                        GestionVariables.visibility_barre[j] = GestionVariables.visibility_barre[i];
                                        GestionVariables.visibility_barre[i] = false;
                                        GestionVariables.drawable_barre[j] = GestionVariables.drawable_barre[i];
                                        GestionVariables.drawable_barre[i] = GestionVariables.picto_vide;
                                        GestionVariables.syntaxe_barre[j] = GestionVariables.syntaxe_barre[i];
                                        GestionVariables.syntaxe_barre[i] = "";
                                        GestionBandePhrase.mettreJourBandePhrase(j);
                                        GestionPicto.masquer(GestionVariables.images_barre[i]);
                                        return true;
                                    }
                                }
                            }


                            for (int u = 0; u < GestionVariables.NB_ONGLETS; u++) {                            //uv grille onglets
                                for (int v = 0; v < GestionVariables.NB_PICTO; v++) {
                                    if ((view_cible.getId() == GestionVariables.ids_layoutPicto[u][v]) && (!GestionVariables.visibility[u][v])) {            //wx picto stocké sur mem
                                                  /*-------------------------------------------------------------------------------------------*/
                                                  /* Si la vue cible est la grille a onglets --------------------------------------------------*/
							      				/*-------------------------------------------------------------------------------------------*/
                                        Log.v(TAG, "parent:barre:cible:casePicto");

                                        if (GestionVariables.sauvegarde_onglet_barre[i] != u) {
                                                      /*if(GestionVariables.nombre_picto_onglet[i]==GestionVariables.NB_PICTO){
				      									return false;}*/

                                            GestionVariables.nombre_picto_onglet[GestionVariables.sauvegarde_onglet_barre[i]]--;
                                            GestionVariables.nombre_picto_onglet[u]++;
                                        }


                                        GestionVariables.sauvegarde_onglet_barre[i] = 0;
                                        GestionVariables.sauvegarde_picto_barre[i] = 0;
                                        GestionVariables.visibility[u][v] = true;
                                        GestionVariables.visibility_barre[i] = false;
                                        GestionVariables.drawable[u][v] = GestionVariables.drawable_barre[i];
                                        GestionVariables.drawable_barre[i] = GestionVariables.picto_vide;
                                        GestionVariables.syntaxe[u][v] = GestionVariables.syntaxe_barre[i];
                                        GestionVariables.syntaxe_barre[i] = "";
                                        GestionPicto.mettreJour(GestionVariables.images[u][v], GestionVariables.drawable[u][v]);
                                        GestionPicto.masquer(GestionVariables.images_barre[i]);
                                        return true;
                                    }
                                }
                            }
                                      /*------------------------------------------------------------------------------------------------*/
                        }
                    }


                    return false;
                } else {
                    Log.v(TAG, "parent==cible");
                    return false;
                }

            case DragEvent.ACTION_DRAG_ENDED:
            default:
                break;
        }
        return true;
    } // methode on drag
}	// classe
	    
 