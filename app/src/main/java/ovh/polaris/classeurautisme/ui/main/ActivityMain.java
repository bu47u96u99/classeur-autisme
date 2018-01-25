package ovh.polaris.classeurautisme.ui.main;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.TabHost.TabContentFactory;
import ovh.polaris.classeurautisme.R;
import ovh.polaris.classeurautisme.ui.bibliopicto.ActivityBiblioPicto;
import ovh.polaris.classeurautisme.ui.main.fragments.*;
import ovh.polaris.classeurautisme.ui.misc.ClassDragListener;
import ovh.polaris.classeurautisme.ui.misc.ClassPagerAdapter;
import ovh.polaris.classeurautisme.ui.misc.ClassTabContent;
import ovh.polaris.classeurautisme.ui.misc.ClassTouchListener;
import ovh.polaris.classeurautisme.ui.preferences.ActivityPreferences;
import ovh.polaris.classeurautisme.utils.*;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

public class ActivityMain extends FragmentActivity implements TextToSpeech.OnInitListener, TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {

    /**************************************************************/
    /** Constantes ************************************************/
    /**************************************************************/
    /* Tag */
    private static final String TAG = "projetPecs:MainActivity";

    private static final int RATIO_SCREEN = 4;
    private static final int PERCENTAGE_ZONE = 10;
    /**************************************************************/
    /** Variables *************************************************/
    /**************************************************************/
		/* TabHost */
    public static TabHost tabHost;
    /* TextToSpeech */
    public static TextToSpeech mTts;
    /* ViewPager */
    private ViewPager mViewPager;
    /* Boutons et layouts */
    public static ImageButton bouton_clear_barre, bouton_phrase, bouton_menu;
    public static LinearLayout layoutBarreBouton;
    /* Mise en veille */
    private WakeLock wakeLock;


    /**************************************************************/
    /** Classes ***************************************************/
    /**************************************************************/
		/* Classe TabInfo */
    @SuppressWarnings("unused")
    private class TabInfo {
        private String tag;
        private Class<?> clss;

        private Bundle args;
        private Fragment fragment;

        TabInfo(String tag, Class<?> clazz, Bundle args) {
            this.tag = tag;
            this.setClss(clazz);
            this.args = args;
        }

        public void setClss(Class<?> clss) {
            this.clss = clss;
        }

    }

    /* Classe TabFactory */
    private class TabFactory implements TabContentFactory {

        private final Context mContext;

        public TabFactory(Context context) {
            mContext = context;
        }

        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }

    }

    /**************************************************************/
    /** Methodes***************************************************/
    /**************************************************************/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        /* Create */
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "wakeLock");

        /* Chargement du layout */
        this.initialiserTailleEcran();
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /* Init */
        this.initialisation(savedInstanceState);

        /* Mise en place des Drag Listeners */
        for (int i = 0; i < GestionVariables.layout_barre.length; i++) {
            GestionVariables.layout_barre[i] = (LinearLayout) findViewById(GestionVariables.ids_barre[i]);
            GestionVariables.layout_barre[i].setOnDragListener(new ClassDragListener());
        }

        for (int i = 0; i < GestionVariables.images_barre.length; i++) {
            GestionVariables.images_barre[i] = (ImageView) findViewById(GestionVariables.ids_image_barre[i]);
            GestionVariables.images_barre[i].setOnTouchListener(new ClassTouchListener());
        }

        /* Listener sur le layout */
        layoutBarreBouton = (LinearLayout) findViewById(R.id.layoutBarreBouton);
        layoutBarreBouton.setOnDragListener(new ClassDragListener());
        layoutBarreBouton.setOnClickListener(clickListenerLayoutBarreBouton);

        /* Listener sur le bouton */
        bouton_clear_barre = (ImageButton) findViewById(GestionVariables.id_bouton_clear_barre);
        bouton_clear_barre.setOnClickListener(clickListenerClearBouton);

        bouton_phrase = (ImageButton) findViewById(R.id.bouton_phrase);
        bouton_phrase.setOnClickListener(clickListenerBoutonPhrase);

        bouton_menu = (ImageButton) findViewById(R.id.bouton_menu);
        bouton_menu.setOnClickListener(clickListenerBoutonMenu);
        /* Activité lancée */
        Log.v(TAG, "activity launch");
    }


    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("tab", tabHost.getCurrentTabTag());
        super.onSaveInstanceState(outState);
    }


    private void intialiseViewPager() {

        List<Fragment> fragments = new Vector<>();
        fragments.add(Fragment.instantiate(this, FragmentOnglet1.class.getName()));
        fragments.add(Fragment.instantiate(this, FragmentOnglet2.class.getName()));
        fragments.add(Fragment.instantiate(this, FragmentOnglet3.class.getName()));
        fragments.add(Fragment.instantiate(this, FragmentOnglet4.class.getName()));
        fragments.add(Fragment.instantiate(this, FragmentOnglet5.class.getName()));
        fragments.add(Fragment.instantiate(this, FragmentOnglet6.class.getName()));

        ClassPagerAdapter mPagerAdapter = new ClassPagerAdapter(super.getSupportFragmentManager(), fragments);

        this.mViewPager = (ViewPager) super.findViewById(R.id.viewpager);
        this.mViewPager.setAdapter(mPagerAdapter);
        this.mViewPager.setOnPageChangeListener(this);
    }


    private void initialiseTabHost(Bundle args) {
        /* TabInfo */
        HashMap<String, TabInfo> mapTabInfo = new HashMap<>();
        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();

        TabInfo tabInfo;
        TabHost.TabSpec tSpecOnglet_1 = tabHost.newTabSpec("onglet_1");
        tSpecOnglet_1.setIndicator(createTabView(1, getBaseContext()));
        tSpecOnglet_1.setContent(new ClassTabContent(getBaseContext()));
        ActivityMain.AddTab(this, tabHost, tSpecOnglet_1);
        tabInfo = new TabInfo("Tab1", FragmentOnglet1.class, args);
        mapTabInfo.put(tabInfo.tag, tabInfo);

        TabHost.TabSpec tSpecOnglet_2 = tabHost.newTabSpec("onglet_2");
        tSpecOnglet_2.setIndicator(createTabView(2, getBaseContext()));
        tSpecOnglet_2.setContent(new ClassTabContent(getBaseContext()));
        ActivityMain.AddTab(this, tabHost, tSpecOnglet_2);
        tabInfo = new TabInfo("Tab2", FragmentOnglet2.class, args);
        mapTabInfo.put(tabInfo.tag, tabInfo);

        TabHost.TabSpec tSpecOnglet_3 = tabHost.newTabSpec("onglet_3");
        tSpecOnglet_3.setIndicator(createTabView(3, getBaseContext()));
        tSpecOnglet_3.setContent(new ClassTabContent(getBaseContext()));
        ActivityMain.AddTab(this, tabHost, tSpecOnglet_3);
        tabInfo = new TabInfo("Tab3", FragmentOnglet3.class, args);
        mapTabInfo.put(tabInfo.tag, tabInfo);

        TabHost.TabSpec tSpecOnglet_4 = tabHost.newTabSpec("onglet_4");
        tSpecOnglet_4.setIndicator(createTabView(4, getBaseContext()));
        tSpecOnglet_4.setContent(new ClassTabContent(getBaseContext()));
        ActivityMain.AddTab(this, tabHost, tSpecOnglet_4);
        tabInfo = new TabInfo("Tab4", FragmentOnglet4.class, args);
        mapTabInfo.put(tabInfo.tag, tabInfo);

        TabHost.TabSpec tSpecOnglet_5 = tabHost.newTabSpec("onglet_5");
        tSpecOnglet_5.setIndicator(createTabView(5, getBaseContext()));
        tSpecOnglet_5.setContent(new ClassTabContent(getBaseContext()));
        ActivityMain.AddTab(this, tabHost, tSpecOnglet_5);
        tabInfo = new TabInfo("Tab5", FragmentOnglet5.class, args);
        mapTabInfo.put(tabInfo.tag, tabInfo);

        TabHost.TabSpec tSpecOnglet_6 = tabHost.newTabSpec("onglet_6");
        tSpecOnglet_6.setIndicator(createTabView(6, getBaseContext()));
        tSpecOnglet_6.setContent(new ClassTabContent(getBaseContext()));
        ActivityMain.AddTab(this, tabHost, tSpecOnglet_6);
        tabInfo = new TabInfo("Tab6", FragmentOnglet6.class, args);
        mapTabInfo.put(tabInfo.tag, tabInfo);

        tabHost.setOnTabChangedListener(this);
    }


    private static void AddTab(ActivityMain activity, TabHost tabHost, TabHost.TabSpec tabSpec) {
        tabSpec.setContent(activity.new TabFactory(activity));
        tabHost.addTab(tabSpec);
    }


    public void onTabChanged(String tag) {
        int pos = tabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }


    @Override
    public void onPageSelected(int position) {
        tabHost.setCurrentTab(position);
    }


    @Override
    public void onPageScrollStateChanged(int state) {
    }

    /* Mise en place du TabView avec décoration XML */
    private View createTabView(int n_onglet, final Context context) {
        View view;
        switch (n_onglet) {
            case 1:
                //view = LayoutInflater.from(context).inflate(R.layout.tab_item_1, null);
                view = View.inflate(context, R.layout.tab_item_1, null);
                break;
            case 2:
                view = View.inflate(context, R.layout.tab_item_2, null);
                break;
            case 3:
                view = View.inflate(context, R.layout.tab_item_3, null);
                break;
            case 4:
                view = View.inflate(context, R.layout.tab_item_4, null);
                break;
            case 5:
                view = View.inflate(context, R.layout.tab_item_5, null);
                break;
            case 6:
                view = View.inflate(context, R.layout.tab_item_6, null);
                break;
            default:
                view = null;
        }
        return view;
    }

    public void onBackPressed() {
    }

    protected void onPause() {
        super.onPause();
        wakeLock.release();
    }

    protected void onResume() {
        super.onResume();
        wakeLock.acquire();
    }

    /*
       * Lancer activité BiblioPicto pour ajouter des pictogrammes
	*/
    public static void lancerBiblioPicto(Activity activity) {
        Intent intent = new Intent(activity, ActivityBiblioPicto.class);
        intent.putExtra("numero_onglet", tabHost.getCurrentTab());
        activity.startActivity(intent);
    }


    /*
       * Lancer mode préférences
    */
    public static void lancerPreferences(Activity activity, Context context) {
        Intent intent = new Intent(context, ActivityPreferences.class);
        activity.startActivity(intent);
    }


    /***************************
     * Gestion de la fermeture *
     ***************************/

	/*
		 * Fermeture de l'application
		 */
    public static void fermetureApplication(Activity activity) {
        GestionBandePhrase.viderBandePhrase();
        GestionSauvegarde.sauvegardeInternalStorage(GestionVariables.dossier_save);
        Log.i(TAG, "activité stoppée.");
        mTts.shutdown();
        activity.finish();
    }


    /* Initialisation et configuration du TextToSpeech */
    @Override
    public void onInit(int arg0) {
			/* Verification de la disponibilite  de la synthese vocale.*/
        if (arg0 == TextToSpeech.SUCCESS) {
            int result = mTts.setLanguage(Locale.FRANCE);
				/* verification ici si cette langue est supporte par le terminal et si elle existe */
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(TAG, "Langue non disponible.");
            } else {
                mTts.setSpeechRate(GestionVariables.vitesse_elocution);
                Log.v(TAG, "Tts initialise - vitesse " + GestionVariables.vitesse_elocution);
            }
        } else {
            Log.e(TAG, "Impossible d'initialiser TextToSpeech.");
        }
    }


    public void initialiserTailleEcran() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int screenWidth = metrics.widthPixels / RATIO_SCREEN;
        GestionVariables.swipeZone = screenWidth - (screenWidth*PERCENTAGE_ZONE/100);
        GestionVariables.densite = (int) (getBaseContext().getResources().getDisplayMetrics().density);
    }

    /* Initialisation globale */
    public void initialisation(Bundle savedInstanceState) {

        GestionVariables.picto_vide = getResources().getDrawable(R.drawable.picto_vide);
        GestionVariables.picto_jeveux = getResources().getDrawable(R.drawable.picto_jeveux);
        GestionVariables.dossier_pictos = GestionFichiers.creerDossierPictos();
        GestionFichiers.creerPictoJeVeux();
        GestionFichiers.creerFichierNoMedia();
        GestionVariables.dossier_save = GestionSauvegarde.initialiserDossierSauvegarde(this);
        GestionVariables.dossier_profils = GestionFichiers.creerDossierProfils(getBaseContext());
        GestionVariables.dossier_temp = GestionFichiers.creerDossierTemp(getBaseContext());
        GestionFichiers.suppressionContenuDossier(GestionVariables.dossier_temp);
        GestionVariables.sdcard_exist = Environment.getExternalStorageDirectory().exists();
			
		    /* Proprietes de l'activite */
        setTitle("projetPicto");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		    
		    /* Reset tableaux */
        GestionVariables.resetTableaux();
		    
		    /* Reset activity_start_main et ajoutPicto */
        GestionVariables.resetActivityStart();

        boolean result = GestionSauvegarde.chargementConfigurationExterne(GestionSauvegarde.initialiserDossierSauvegarde(this), getBaseContext(), GestionVariables.MODE_CHARGEMENT);

        if (GestionVariables.premierLancement || result) {
            GestionOnglets.remiseDefautGrille();
        }

        this.initialiseTabHost(savedInstanceState);
        if (savedInstanceState != null) {
            tabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }
			/* Initialise ViewPager */
        this.intialiseViewPager();
		    
		    /* Text To Speech */
        mTts = new TextToSpeech(this, this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Intent.ACTION_MAIN.equals(intent.getAction())) {
            Log.i("MyLauncher", "onNewIntent: HOME Key");

        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (!hasFocus) {
            windowCloseHandler.postDelayed(windowCloserRunnable, 250);
        }
    }

    private void toggleRecents() {
        Intent closeRecents = new Intent("com.android.systemui.recent.action.TOGGLE_RECENTS");
        closeRecents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        ComponentName recents = new ComponentName("com.android.systemui", "com.android.systemui.recent.RecentsActivity");
        closeRecents.setComponent(recents);
        this.startActivity(closeRecents);
    }

    private Handler windowCloseHandler = new Handler();
    private Runnable windowCloserRunnable = new Runnable() {
        @Override
        public void run() {
            ActivityManager am = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
            ComponentName cn = am.getRunningTasks(1).get(0).topActivity;

            if (cn != null && cn.getClassName().equals("com.android.systemui.recent.RecentsActivity")) {
                toggleRecents();
            }
        }
    };

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if ((event.getKeyCode() == KeyEvent.KEYCODE_HOME)) {
            Log.v("clac", "clac");
            return false;
        } else
            return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            return false;
        } else if (keyCode == KeyEvent.KEYCODE_HOME) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    private OnClickListener clickListenerClearBouton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            GestionBandePhrase.viderBandePhrase();
        }
    };

    private OnClickListener clickListenerBoutonPhrase = new OnClickListener() {
        @Override
        public void onClick(View v) {
            pointageParlant_barre();
        }

    };

    private OnClickListener clickListenerBoutonMenu = new OnClickListener() {
        @Override
        public void onClick(View v) {
            PopupMenu popupMenu = new PopupMenu(getApplicationContext(), v);
            popupMenu.getMenuInflater().inflate(R.menu.menu_main_activity, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(popUpMenuItemClickListener);
            popupMenu.show();
        }

    };

    private OnClickListener clickListenerLayoutBarreBouton = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (GestionVariables.mode_suppression) {
                GestionBandePhrase.modeNormal(bouton_clear_barre, bouton_phrase, bouton_menu, layoutBarreBouton);
            }
        }

    };

    private PopupMenu.OnMenuItemClickListener popUpMenuItemClickListener = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem parent) {
            switch (parent.getItemId()) {
                case R.id.menu_prefs:
                    if (GestionVariables.passwordActive) {
                        GestionPopUps.popUpMotDePasse(ActivityMain.this, getBaseContext(), GestionVariables.MODE_PREFS);
                    } else {
                        GestionBandePhrase.modeNormal(bouton_clear_barre, bouton_phrase, bouton_menu, layoutBarreBouton);
                        lancerPreferences(ActivityMain.this, getBaseContext());
                    }
                    return true;

                case R.id.menu_quitter:
                    if (GestionVariables.passwordActive) {
                        GestionPopUps.popUpMotDePasse(ActivityMain.this, getBaseContext(), GestionVariables.MODE_QUIT);
                    } else {
                        GestionPopUps.popUpComfirmationQuitter(ActivityMain.this);
                    }
                    return true;


                case R.id.menu_ajout_pict:
                    if (GestionVariables.passwordActive) {
                        GestionPopUps.popUpMotDePasse(ActivityMain.this, getBaseContext(), GestionVariables.MODE_AJOUT);
                    } else {
                        GestionBandePhrase.modeNormal(bouton_clear_barre, bouton_phrase, bouton_menu, layoutBarreBouton);
                        lancerBiblioPicto(ActivityMain.this);
                    }
                    return true;

                case R.id.menu_suppr_pict:
                    if (GestionVariables.passwordActive) {
                        if (!GestionVariables.mode_suppression) {
                            GestionPopUps.popUpMotDePasse(ActivityMain.this, getBaseContext(), GestionVariables.MODE_SUPPR);
                        } else {
                            GestionBandePhrase.modeNormal(bouton_clear_barre, bouton_phrase, bouton_menu, layoutBarreBouton);
                        }
                    } else {
                        if (!GestionVariables.mode_suppression) {
                            GestionBandePhrase.modeSuppression(getBaseContext(), bouton_clear_barre, bouton_phrase, bouton_menu, layoutBarreBouton);

                        } else {
                            GestionBandePhrase.modeNormal(bouton_clear_barre, bouton_phrase, bouton_menu, layoutBarreBouton);
                        }

                    }
                    return true;
            }
            return false;
        }
    };


    /**************************************************************/
    /** Statics****************************************************/
    /**************************************************************/
		
		


		/*                */
    public static void pointageParlant(String texte) {
        if (mTts != null && GestionVariables.sonActive)
            mTts.speak(texte, TextToSpeech.QUEUE_FLUSH, null);
    }

    public static void pointageParlant_barre() {
        if (mTts != null && GestionVariables.sonActive && GestionVariables.pointageBarre && !mTts.isSpeaking()) {
            for (int i = 0; i < GestionVariables.syntaxe_barre.length; i++) {
                if (!GestionVariables.syntaxe_barre[i].equals("") && GestionVariables.visibility_barre[i] && !GestionVariables.mode_suppression) {
                    mTts.speak(GestionVariables.syntaxe_barre[i], TextToSpeech.QUEUE_ADD, null);
                    mTts.playSilence(GestionVariables.SILENCE_PHRASE, TextToSpeech.QUEUE_ADD, null);
                }
            }
        }
    }

    public static void reglageVitesseElocution(float v) {
        mTts.setSpeechRate(v);
    }

}

