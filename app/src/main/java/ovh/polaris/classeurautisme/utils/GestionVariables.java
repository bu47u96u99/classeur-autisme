package ovh.polaris.classeurautisme.utils;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import ovh.polaris.classeurautisme.R;

import java.io.File;
import java.util.ArrayList;

public class GestionVariables extends Application {

    /** Constantes du programme */


    /**
     * Ids du programme
     */

			/* Ids des elements XML du programme - pictogrammes*/
    public static final int[][] ids_picto = {{R.id.pictogramme1_1, R.id.pictogramme2_1, R.id.pictogramme3_1, R.id.pictogramme4_1, R.id.pictogramme5_1,
            R.id.pictogramme6_1, R.id.pictogramme7_1, R.id.pictogramme8_1},
            {R.id.pictogramme1_2, R.id.pictogramme2_2, R.id.pictogramme3_2, R.id.pictogramme4_2, R.id.pictogramme5_2,
                    R.id.pictogramme6_2, R.id.pictogramme7_2, R.id.pictogramme8_2},
            {R.id.pictogramme1_3, R.id.pictogramme2_3, R.id.pictogramme3_3, R.id.pictogramme4_3, R.id.pictogramme5_3,
                    R.id.pictogramme6_3, R.id.pictogramme7_3, R.id.pictogramme8_3},
            {R.id.pictogramme1_4, R.id.pictogramme2_4, R.id.pictogramme3_4, R.id.pictogramme4_4, R.id.pictogramme5_4,
                    R.id.pictogramme6_4, R.id.pictogramme7_4, R.id.pictogramme8_4},
            {R.id.pictogramme1_5, R.id.pictogramme2_5, R.id.pictogramme3_5, R.id.pictogramme4_5, R.id.pictogramme5_5,
                    R.id.pictogramme6_5, R.id.pictogramme7_5, R.id.pictogramme8_5},
            {R.id.pictogramme1_6, R.id.pictogramme2_6, R.id.pictogramme3_6, R.id.pictogramme4_6, R.id.pictogramme5_6,
                    R.id.pictogramme6_6, R.id.pictogramme7_6, R.id.pictogramme8_6}};

    /* Ids des elements XML du programme - bande Scotch*/
    public static final int[] ids_barre = {R.id.layoutBarre1, R.id.layoutBarre2, R.id.layoutBarre3, R.id.layoutBarre4, R.id.layoutBarre5, R.id.layoutBarre6};

    /* Ids des elements XML du programme - images barre Scotch */
    public static final int[] ids_image_barre = {R.id.imageBarre1, R.id.imageBarre2, R.id.imageBarre3, R.id.imageBarre4, R.id.imageBarre5, R.id.imageBarre6};

    /* Ids des elements XML du programme - grilles*/
    public static final int[][] ids_layoutPicto = {{R.id.layout_picto1_1, R.id.layout_picto2_1, R.id.layout_picto3_1, R.id.layout_picto4_1, R.id.layout_picto5_1, R.id.layout_picto6_1,
            R.id.layout_picto7_1, R.id.layout_picto8_1},
            {R.id.layout_picto1_2, R.id.layout_picto2_2, R.id.layout_picto3_2, R.id.layout_picto4_2, R.id.layout_picto5_2, R.id.layout_picto6_2,
                    R.id.layout_picto7_2, R.id.layout_picto8_2},
            {R.id.layout_picto1_3, R.id.layout_picto2_3, R.id.layout_picto3_3, R.id.layout_picto4_3, R.id.layout_picto5_3, R.id.layout_picto6_3,
                    R.id.layout_picto7_3, R.id.layout_picto8_3},
            {R.id.layout_picto1_4, R.id.layout_picto2_4, R.id.layout_picto3_4, R.id.layout_picto4_4, R.id.layout_picto5_4, R.id.layout_picto6_4,
                    R.id.layout_picto7_4, R.id.layout_picto8_4},
            {R.id.layout_picto1_5, R.id.layout_picto2_5, R.id.layout_picto3_5, R.id.layout_picto4_5, R.id.layout_picto5_5, R.id.layout_picto6_5,
                    R.id.layout_picto7_5, R.id.layout_picto8_5},
            {R.id.layout_picto1_6, R.id.layout_picto2_6, R.id.layout_picto3_6, R.id.layout_picto4_6, R.id.layout_picto5_6, R.id.layout_picto6_6,
                    R.id.layout_picto7_6, R.id.layout_picto8_6}};

			/* Ids de GridView de chaque onglets */
//			static public final int[] ids_gridLayout = {R.id.layoutGrid1,R.id.layoutGrid2,R.id.layoutGrid3,R.id.layoutGrid4,R.id.layoutGrid5,R.id.layoutGrid6};

    /* Id du bouton de clear */
    public static final int id_bouton_clear_barre = R.id.bouton_clear_barre;

    /* Id du layout mode suppression */
    public static final int id_layoutBarreBouton = R.id.layoutBarreBouton;

    /**
     * Constantes
     */
			
			/* Nombre de pictogrammes */
    public static final int NB_PICTO = 8;

    /* Nombre d'onglets */
    public static final int NB_ONGLETS = 6;

    /* Nombre de pictogrammes dans la barre */
    public static final int NB_CASES = 6;


    public static final int MODE_SUPPR = 0;
    public static final int MODE_AJOUT = 1;
    public static final int MODE_PREFS = 2;
    public static final int MODE_QUIT = 3;
    public static final long SILENCE_PHRASE = 35;
    public static final int MODE_CHARGEMENT = 0;
    public static final int MODE_PROFIL = 1;

    /**
     * Variables du programme
     */

		/* Vitesse d'élocution de la synthèse vocale */
    public static float vitesse_elocution = 0.85f;

    /* Mot de passe */
    public static String password = "0000";

    /* Durée compteur pour dialogue mot de passe */
    public static int dureeCompteur = 5;

    /********************************************/
    /**
     * Barre
     ***********************************/
		/*
			/* Tableau des images barre */
    public static ImageView[] images_barre;

    /* Tableau visibilité barre*/
    public static boolean[] visibility_barre;

    /* Tableau des drawable barre */
    public static Drawable[] drawable_barre;

    /* Tableau des drawable barre */
    public static String[] syntaxe_barre;

    /* Tableau layout barre */
    public static LinearLayout[] layout_barre;

    /* Tableau sauvegarde emplacement de depart picto */
    public static int[] sauvegarde_picto_barre;
    public static int[] sauvegarde_onglet_barre;

    /**
     * Onglets
     */
			/* Tableau des ImageView */
    public static ImageView[][] images;

    /* Tableau ressources drawable */
    public static Drawable[][] drawable;  // Dessin que l'image doit afficher

    /* Tableau visibilité */
    public static boolean[][] visibility;    // Visibilité de l'image

    /* Tableau syntaxe */
    public static String[][] syntaxe;    // Chaine de caractères

    /* Tableau layout */
    public static LinearLayout[][] layout_picto;

    /**************************************/

    public static long startTime = 0;
    public static long endTime = 0;

    public static boolean fenetrePopIm = false;
    public static int[] nombre_picto_onglet;

    /* Fichier des profils */
    public static File dossier_profils;
    public static File dossier_pictos;
    public static File dossier_temp;
    public static File dossier_save;

    /* Tableau activites en cours */
    public static boolean[] activity_start_main;

    /* Mot de passe necessaire */
    public static boolean passwordActive = false;

    /* Mode suppression */
    public static boolean mode_suppression = false;

    /* Son active */
    public static boolean sonActive = false;

    public static boolean pointagePicto = false, pointageBarre = true;

    /* SD card presente */
    public static boolean sdcard_exist = false;

    /* Pictogrammes de base */
    public static Drawable picto_vide;
//		static Drawable je_veux;

    /* Stockage des ressources externes */
    public static ArrayList<Drawable> drawable_stockage_externe;
    public static ArrayList<String> syntaxe_stockage_externe;
    public static ArrayList<File> files_stockage_externe;

    /* Taille ecran */
    public static int swipeZone;
    //		static public int hauteur_ecran;
    public static int densite;

    /* Compteur intercalaires slidées */
    public static boolean compteur_intercalaire_slidees_gauche = true;
    public static boolean compteur_intercalaire_slidees_droite = true;
    /*******************************************************************************************/

    public static int cibleTime = 1;

    public static boolean fuite = false;

    public static Drawable picto_jeveux;

    public static boolean premierLancement = false;

    public static boolean zoom_actif = true;

    public static int location_departX = 0;
    public static int location_departY = 0;

    public static int sensibilite = 30;

    public static int touchCount = 0;


    /* Reset des tableaux  */
    static public void resetTableaux() {
        /** Onglets */
        GestionVariables.layout_picto = new LinearLayout[GestionVariables.NB_ONGLETS][GestionVariables.NB_PICTO];
        GestionVariables.images = new ImageView[GestionVariables.NB_ONGLETS][GestionVariables.NB_PICTO];
        GestionVariables.drawable = new Drawable[GestionVariables.NB_ONGLETS][GestionVariables.NB_PICTO];
        GestionVariables.visibility = new boolean[GestionVariables.NB_ONGLETS][GestionVariables.NB_PICTO];
        GestionVariables.syntaxe = new String[GestionVariables.NB_ONGLETS][GestionVariables.NB_PICTO];
        GestionVariables.nombre_picto_onglet = new int[NB_ONGLETS];

        /** Barre */
        GestionVariables.layout_barre = new LinearLayout[GestionVariables.NB_CASES];
        GestionVariables.images_barre = new ImageView[GestionVariables.NB_CASES];
        GestionVariables.drawable_barre = new Drawable[GestionVariables.NB_CASES];
        GestionVariables.visibility_barre = new boolean[GestionVariables.NB_CASES];
        GestionVariables.syntaxe_barre = new String[GestionVariables.NB_CASES];
        GestionVariables.sauvegarde_onglet_barre = new int[GestionVariables.NB_CASES];
        GestionVariables.sauvegarde_picto_barre = new int[GestionVariables.NB_CASES];

        /** Bibliotheque */
        GestionVariables.drawable_stockage_externe = new ArrayList<>();
        GestionVariables.syntaxe_stockage_externe = new ArrayList<>();
        GestionVariables.files_stockage_externe = new ArrayList<>();
			
			
			/* Remplissage par defaut des grilles*/
        for (int i = 0; i < GestionVariables.NB_ONGLETS; i++) {
            for (int j = 1; j < GestionVariables.NB_PICTO; j++) {
                GestionVariables.visibility[i][j] = false;
                GestionVariables.drawable[i][j] = GestionVariables.picto_vide;
                GestionVariables.syntaxe[i][j] = "";
            }
            GestionVariables.nombre_picto_onglet[i] = 0;
        }
			
			/* Remplissage par defaut de la barre*/
        for (int i = 0; i < GestionVariables.NB_CASES; i++) {
            GestionVariables.visibility_barre[i] = false;
            GestionVariables.drawable_barre[i] = GestionVariables.picto_vide;
            GestionVariables.syntaxe_barre[i] = "";
            GestionVariables.sauvegarde_onglet_barre[i] = 0;
            GestionVariables.sauvegarde_picto_barre[i] = 0;
        }
    }


    /* Reset des tableaux activity_start */
    static public void resetActivityStart() {
			/* Reset de activity_start_main */
        GestionVariables.activity_start_main = new boolean[GestionVariables.NB_ONGLETS];
        for (int i = 0; i < activity_start_main.length; i++)
            GestionVariables.activity_start_main[i] = false;
    }

}
		

		
		
		
		

	

