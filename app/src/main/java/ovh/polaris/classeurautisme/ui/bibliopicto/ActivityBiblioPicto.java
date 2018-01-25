package ovh.polaris.classeurautisme.ui.bibliopicto;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.PopupMenu;
import android.widget.Toast;
import ovh.polaris.classeurautisme.R;
import ovh.polaris.classeurautisme.ui.misc.ClassImageAdapter;
import ovh.polaris.classeurautisme.utils.GestionFichiers;
import ovh.polaris.classeurautisme.utils.GestionPicto;
import ovh.polaris.classeurautisme.utils.GestionPopUps;
import ovh.polaris.classeurautisme.utils.GestionVariables;

import java.io.File;
import java.io.IOException;

public class ActivityBiblioPicto extends AppCompatActivity {
	
	private static final String TAG = "GridViewActivity";
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int PICKFILE_RESULT_CODE = 2;


	private Camera camera;
    private File photoFile;
	private int numero_onglet,position_image;

    private static GridView gridview;
	private static ClassImageAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	Bundle extras=getIntent().getExtras();
  	  	numero_onglet = extras.getInt("numero_onglet") + 1;
  	  	
  	  	/* Creation de l'activite */
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.gridviewlayout);

        setTitle("Ajouter un pictogramme");
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	    
	    /* Recuperation des images dans le dossier /storage/projetPicto */
	    GestionFichiers.parcourirDossierPicto(this);
	    GestionFichiers.suppressionContenuDossier(GestionVariables.dossier_temp);
	    
        gridview = (GridView) findViewById(R.id.gridview);
        adapter = new ClassImageAdapter(this, GestionVariables.drawable_stockage_externe);
        gridview.setAdapter(adapter); 
        gridview.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View v,final int position, long id) {
        		position_image=position;
        		Log.v("", "position 0 : "+position_image);
        		PopupMenu popupMenu = new PopupMenu(getApplicationContext(),v);
				popupMenu.getMenuInflater().inflate(R.menu.menu_items_biblio_picto,popupMenu.getMenu());
				popupMenu.setOnMenuItemClickListener(popUpMenuItemClickListener);
				popupMenu.show();
        	}
        	});
        /* Activite lancee */
        Log.v(TAG,"activity launch");
    }

    
        private PopupMenu.OnMenuItemClickListener popUpMenuItemClickListener = new PopupMenu.OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem parent) {
					switch(parent.getItemId()){
					case R.id.item_ajouter:
						for(int i=0;i<GestionVariables.NB_ONGLETS;i++){
			            	if(numero_onglet == i+1){
			            		if(GestionVariables.nombre_picto_onglet[numero_onglet-1] < GestionVariables.NB_PICTO){
			            			Log.v("", "n="+GestionVariables.nombre_picto_onglet[numero_onglet-1]);
			            			int num_button = GestionPicto.chercherPremiereCaseLibre(i);
				            		if(num_button == -1){Toast.makeText(getBaseContext(), "Onglet plein", Toast.LENGTH_SHORT).show();} // Code mort 
				            		else{
						            	GestionVariables.drawable[i][num_button] = GestionVariables.drawable_stockage_externe.get(position_image);
						            	GestionVariables.visibility[i][num_button]=true;
						        		GestionVariables.syntaxe[i][num_button]=GestionVariables.syntaxe_stockage_externe.get(position_image);
						            	GestionVariables.nombre_picto_onglet[numero_onglet-1]++;
						            	GestionPicto.mettreJour(GestionVariables.images[i][num_button], GestionVariables.drawable_stockage_externe.get(position_image));
						            	Toast.makeText(getApplicationContext(),"Ajout du pictogramme", Toast.LENGTH_SHORT).show();
								        finish();}}
			            		else{Toast.makeText(getBaseContext(), "Onglet plein", Toast.LENGTH_SHORT).show();}
			            	}
						}
						return false;
					case R.id.item_supprimer:
						GestionPopUps.popUpComfirSupprImage(ActivityBiblioPicto.this, position_image);
						return false;
					case R.id.item_renommer:
						GestionPopUps.popUpRenommer(ActivityBiblioPicto.this,position_image);
						return false;	
					default:
						return false;	
					}
				}
			};
			


    
    /* Creation du menu */
	  @Override
	  public boolean onCreateOptionsMenu(Menu menu) {
		  super.onCreateOptionsMenu(menu);
		  MenuInflater inflater = getMenuInflater();	    
		  inflater.inflate(R.menu.menu_biblio_picto, menu);
		  return true;
	  }
	  
	  
	  /* Items de menu */
	  @Override
	  public boolean onOptionsItemSelected (MenuItem item)
	  {
		  switch(item.getItemId())
		  {
		  	case R.id.camera:
		  		GestionFichiers.suppressionContenuDossier(GestionVariables.dossier_temp);
		  		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			   	if(takePictureIntent.resolveActivity(getPackageManager()) != null) {
				   	try {
				   		photoFile = File.createTempFile("photo_result",".jpg",GestionVariables.dossier_temp); 
				   	}catch (IOException ex) {
                        Log.e(TAG, ex.getMessage());
                        ex.printStackTrace();}
				   	if(photoFile.exists()) {
				   		takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photoFile));
				   		startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);  
				   	}
			   	}
			   	break;
		  	case R.id.navigator:
		  		Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
		        fileintent.setType("image/*");
		        try {
		            startActivityForResult(fileintent, PICKFILE_RESULT_CODE);
		        } catch (ActivityNotFoundException e) {
		            Log.e(TAG, e.getMessage());
		        }
		        break;
		  }
		  return super.onOptionsItemSelected(item); 
	  } 
	  
	  
	  
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        switch (requestCode) {
		        case PICKFILE_RESULT_CODE:
		            if (resultCode == RESULT_OK) {
		            	GestionPopUps.popUpNavigateurFichier(this,new File(getRealPathFromURI(data.getData())));
		                return;
		            }else{
		            	return;
		            }
		        case REQUEST_TAKE_PHOTO:
		        	if (resultCode == RESULT_OK) {
		        		File nouvelle_photo = GestionFichiers.chercherNouvellePhoto(GestionVariables.dossier_temp);
						if(nouvelle_photo!=null) {
							if (nouvelle_photo.exists()) {
								GestionPopUps.popUpSaisieSyntaxe(this, nouvelle_photo);

							} else {
								Log.e(TAG, "Erreur : nouvelle photo non trouvee");
							}
						}
		        	}else{
		        		File nouvelle_photo = GestionFichiers.chercherNouvellePhoto(getBaseContext().getCacheDir());
                        if(nouvelle_photo!=null) {
                            if (nouvelle_photo.exists()) {
                                GestionFichiers.supprimerFichier(nouvelle_photo);
                            }
                        }
		        	}
	        	}
	        }
	  

	  /*
	   * getRealPathFromURI
	   * -> Convertir un URI en chemin en chaine String
	   */
	  public String getRealPathFromURI(Uri contentUri) {
		  String[] proj = { MediaStore.Images.Media.DATA };
		  
		  CursorLoader cursorLoader = new CursorLoader(this, contentUri, proj, null, null, null);        
		  Cursor cursor = cursorLoader.loadInBackground();
	   
		  int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		  cursor.moveToFirst();
		  return cursor.getString(column_index); 
	  }
	  


	  @Override
	  public void onSaveInstanceState(Bundle bundle)
	  {
		  super.onSaveInstanceState(bundle);
	  }
	  
	  
	  @Override
	  protected void onPause() {
		  if (camera != null) {
			  camera.release();
			  camera = null;
		  }
		  super.onPause();
	  }
	  

    
    /* Mise a jour du GridView */
    public static void miseaJour(Activity activity){
    	GestionFichiers.parcourirDossierPicto(activity);
    	adapter = new ClassImageAdapter(activity, GestionVariables.drawable_stockage_externe);
        gridview.setAdapter(adapter); 
    }
    
}
    
    




