package ovh.polaris.classeurautisme.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.util.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.Locale;

public class GestionFichiers {

    /* Creer Dossier */
    public static File creerDossierPictos(){
        File sdcard = new File(Environment.getExternalStorageDirectory().getPath());
        String[] contenuSdCard = sdcard.list();
        int i=0;
        if(contenuSdCard!=null && GestionVariables.sdcard_exist){
            while(i<contenuSdCard.length){
                if(contenuSdCard[i].equals("Pictogrammes classeur PECS")){
                    return new File(Environment.getExternalStorageDirectory().getPath()+File.separator+"Pictogrammes classeur PECS");
                }
                i++;
            }
        }
        File repertoire_picto = new File(Environment.getExternalStorageDirectory().getPath()+File.separator+"Pictogrammes classeur PECS");
        repertoire_picto.mkdirs();

        return repertoire_picto;
    }


    public static File creerFichierNoMedia(){
        String[] contenuDossierPicto = GestionVariables.dossier_pictos.list();
        int i=0;

//		while(i<contenuDossierPicto.length){
//			if(contenuDossierPicto[i].equals(".nomedia")){
//				Log.d("Fichiers","Fichier .nomedia trouve");
//		        return new File(GestionVariables.dossier_pictos+File.separator+".nomedia");}
//			i++;
//		}

        File fichierNoMedia = new File(GestionVariables.dossier_pictos+File.separator+".nomedia");
        try {
            BufferedWriter fichier = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fichierNoMedia)));
            fichier.write("\t");
            fichier.write("\r\n");
            Log.d("Fichiers", "Fichier .nomedia crée");
            fichier.close();
        } catch (IOException e) {e.printStackTrace();}

        return fichierNoMedia;
    }


    public static boolean creerPictoJeVeux(){
        String[] contenuDossierPicto = GestionVariables.dossier_pictos.list();
        int i=0;
        if(contenuDossierPicto!=null && GestionVariables.sdcard_exist){
            while(i<contenuDossierPicto.length){
                if(contenuDossierPicto[i].equals("je_veux.png")){
                    return true;}
                i++;
            }
        }

        GestionBitmap.saveBitmap(GestionVariables.picto_jeveux, GestionVariables.dossier_pictos, "je_veux.png");

        return false;
    }


    public static File creerDossierProfils(Context context){
        boolean repDejaExistant = false;
        File sdcard = context.getFilesDir();				// catch e
        String[] contenuFileDir = sdcard.list();
        int i=0;
        if(contenuFileDir!=null){
            while(i<contenuFileDir.length){
                if(contenuFileDir[i].compareTo("profils")==0){
                    return new File(context.getFilesDir()+File.separator+"profils");}
                i++;
            }
        }
        File repertoire_profils = new File(context.getFilesDir()+File.separator+"profils");
        repertoire_profils.mkdirs();

        return repertoire_profils;
    }


    static public void suppressionContenuDossier(File dossier){
        if(dossier!=null) {
            if (dossier.exists()) {
                File[] files = dossier.listFiles();
                if (files != null) {
                    for (File file : files)
                        file.delete();
                }
            }
        }
    }


    public static void saveDonneeFichier(String data, File dossier, String nom_fichier){
        try{
            BufferedWriter fichier = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dossier+File.separator+nom_fichier,true)));
            fichier.write(data);
            fichier.write("\r\n");
            fichier.close();
        }catch (Exception e) { e.printStackTrace();}
    }


    public static void saveDonneeFichier(int data, File dossier,String nom_fichier) {
        try{
            BufferedWriter fichier = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dossier+File.separator+nom_fichier,true)));
            fichier.write(data);
            fichier.write("\r\n");
            fichier.close();
        }catch (Exception e) { e.printStackTrace();}

    }


    public static void saveDonneeFichier(float data, File dossier,String nom_fichier) {
        try{
            BufferedWriter fichier = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dossier+File.separator+nom_fichier,true)));
            fichier.write((int)data);
            fichier.write("\r\n");
            fichier.close();
        }catch (Exception e) { e.printStackTrace();}

    }



    public static boolean copierFichier(File Source, File Destination){
        boolean resultat=false;
        FileInputStream filesource=null;
        FileOutputStream fileDestination=null;
        try{
            filesource=new FileInputStream(Source);
            fileDestination=new FileOutputStream(Destination);
            byte buffer[]=new byte[512*1024];
            int nblecture;
            while((nblecture=filesource.read(buffer))!=-1){
                fileDestination.write(buffer,0,nblecture);
            }
            resultat=true;
        }catch(IOException io){
            io.printStackTrace();
        }finally{
            try{
                filesource.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                fileDestination.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return resultat;
    }

    public static File renommerFichier(File fichier,String nomFichier){
        try
        {
            File renommer = new File(GestionVariables.dossier_pictos+File.separator+nomFichier);
            fichier.renameTo(renommer);
        }
        catch (Exception e){e.printStackTrace();}
        return fichier;
    }


    /* Récupérer images */
    public static void parcourirDossierPicto(Activity activity){
        GestionVariables.drawable_stockage_externe.clear();
        GestionVariables.syntaxe_stockage_externe.clear();
        GestionVariables.files_stockage_externe.clear();

        if(GestionVariables.dossier_pictos.exists()){
            for(int i=0;i<GestionVariables.dossier_pictos.list().length;i++){
                if(GestionVariables.dossier_pictos.list()[i].endsWith(".png") || GestionVariables.dossier_pictos.list()[i].endsWith(".jpg")){
                    File image = new File(GestionVariables.dossier_pictos+File.separator+ GestionVariables.dossier_pictos.list()[i]);
                    Bitmap bitmap = GestionBitmap.decodeFile(image);
                    GestionVariables.files_stockage_externe.add(image);
                    GestionVariables.drawable_stockage_externe.add(new BitmapDrawable(activity.getBaseContext().getResources(),bitmap));
                    String syntaxe = formatageChaineSyntaxe(GestionVariables.dossier_pictos.list()[i]);
                    GestionVariables.syntaxe_stockage_externe.add(syntaxe);
                }
            }
        }
    }


    public static String formatageChaineSyntaxe(String string) {
        string = string.substring(0, string.length()-4);
        if((string.length())>2){
            for(int i=0;i<9;i++){
                if(string.substring(string.length()-2,string.length()).equals("_"+i)){
                    string=string.substring(0,string.length()-2);
                    break;
                }
            }
        }
        char[] chaine = string.toCharArray();
        for(int i=0;i<chaine.length;i++){
            if(chaine[i] =='_'){
                chaine[i]=' ';
            }
        }
        return new String(chaine);
    }

    public static boolean rechercherNomExistant(String nom){
        String[] liste = GestionVariables.dossier_pictos.list();
        for(String name : liste){
            if(nom.equals(name.substring(0, name.length() - 4))){
                return true;
            }
        }
        return false;
    }

    public static String formatageChaineFichier(String texte) {
        texte = texte.toLowerCase(Locale.FRANCE);
        char[] chaine = texte.toCharArray() ;
        for(int i=0;i<chaine.length;i++){
            if(chaine[i] ==' '){
                chaine[i]='_';
            }
        }
        return new String(chaine);
    }


    public static File chercherNouvellePhoto(File directory){
        if(directory.exists()){
            for(int i=0;i<directory.listFiles().length;i++){
                if(directory.listFiles()[i].getName().startsWith("photo_result")){

                    return directory.listFiles()[i];
                }
            }
        }
        return null;
    }


    public static File creerDossierTemp(Context context) {
        return context.getExternalCacheDir();
    }

    public static boolean supprimerFichier(File fichier){
        return fichier.delete();
    }

    public static ArrayList<File> listerFichiersDossierPicto(){
        ArrayList<File> arrayliste = new ArrayList<File>();
        if(GestionVariables.dossier_pictos.exists()){
            for(int i=0;i<GestionVariables.dossier_pictos.list().length;i++){
                if(GestionVariables.dossier_pictos.list()[i].endsWith(".png") || GestionVariables.dossier_pictos.list()[i].endsWith(".jpg")){
                    arrayliste.add(GestionVariables.dossier_pictos.listFiles()[i]);
                }
            }
        }
        return arrayliste;
    }



}
