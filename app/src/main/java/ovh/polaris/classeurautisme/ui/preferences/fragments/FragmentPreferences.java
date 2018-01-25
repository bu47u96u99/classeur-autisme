package ovh.polaris.classeurautisme.ui.preferences.fragments;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.widget.Toast;
import ovh.polaris.classeurautisme.R;
import ovh.polaris.classeurautisme.ui.main.ActivityMain;
import ovh.polaris.classeurautisme.ui.widgets.SeekBarDialogPreference;
import ovh.polaris.classeurautisme.utils.GestionOnglets;
import ovh.polaris.classeurautisme.utils.GestionPopUps;
import ovh.polaris.classeurautisme.utils.GestionVariables;

public class FragmentPreferences extends PreferenceFragment {

    public static final int PASSWORD = 0;
    public static final int SON = 1;
    public static final int ZOOM = 2;
    public static CheckBoxPreference checkboxSon;
    public static Preference editTextMdp;
    public static CheckBoxPreference checkboxMdp;
    public static CheckBoxPreference checkboxZoom;

    static private ListPreference list_profils_ajout;
    static private ListPreference list_profils_suppr;
    static private ListPreference list_mode_son;
    private static SeekBarDialogPreference numberPickerDialogPreference_mdp;
    private static SeekBarDialogPreference numberPickerDialogPreference_zoom;
    private static SeekBarDialogPreference numberPickerDialogPreference_sens;
    private SeekBarDialogPreference seekBarDialogPreference;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        checkboxSon = (CheckBoxPreference) getPreferenceManager().findPreference("checkbox_son");
        editTextMdp = getPreferenceManager().findPreference("edit_text_mot_de_passe");
        checkboxMdp = (CheckBoxPreference) getPreferenceManager().findPreference("checkbox_mot_de_passe");
        checkboxZoom = (CheckBoxPreference) getPreferenceManager().findPreference("checkbox_zoom");
        Preference button_raz = getPreferenceManager().findPreference("bouton_raz");
        list_profils_ajout = (ListPreference) getPreferenceManager().findPreference("liste_profils");
        list_profils_suppr = (ListPreference) getPreferenceManager().findPreference("liste_profils_suppr");
        list_mode_son = (ListPreference) getPreferenceManager().findPreference("liste_mode_son");
        Preference button_save = getPreferenceManager().findPreference("bouton_save");
        numberPickerDialogPreference_mdp = (SeekBarDialogPreference) getPreferenceManager().findPreference("numberPickerDialogPreference_mdp");
        numberPickerDialogPreference_zoom = (SeekBarDialogPreference) getPreferenceManager().findPreference("numberPickerDialogPreference_zoom");
        numberPickerDialogPreference_sens = (SeekBarDialogPreference) getPreferenceManager().findPreference("numberPickerDialogPreference_sens");
        seekBarDialogPreference = (SeekBarDialogPreference) getPreferenceManager().findPreference("SeekBarDialogPreference");
        Preference aPropos = getPreferenceManager().findPreference("a_propos");


        initialiserCheckBox(SON, checkboxSon);
        initialiserCheckBox(PASSWORD, checkboxMdp);
        initialiserCheckBox(ZOOM, checkboxZoom);
        initialiserListModeSon();
        initialiserTempsZoom();
        initialiserEditMdp(editTextMdp);
        list_profils_ajout.setDialogTitle("Choix du profil");
        list_profils_suppr.setDialogTitle("Supprimer le profil");
        miseJourListes(GestionVariables.dossier_profils.list());
        initialiserVitesseElocution();
        initialiserTempsAffichage();
        initialiserSens();

        checkboxZoom.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                GestionVariables.zoom_actif = Boolean.parseBoolean(newValue.toString());
                initialiserTempsZoom();
                initialiserSens();
                return true;
            }
        });

        checkboxSon.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue.toString().equals("true")) {
                    GestionVariables.sonActive = true;
                    initialiserListModeSon();
                    initialiserVitesseElocution();
                } else {
                    GestionVariables.sonActive = false;
                    initialiserListModeSon();
                    initialiserVitesseElocution();
                }
                return true;
            }
        });

        numberPickerDialogPreference_mdp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                GestionVariables.dureeCompteur = Integer.parseInt(newValue.toString());
                Log.v("", "" + GestionVariables.dureeCompteur);
                Toast.makeText(getActivity(), "Délai mot de passe : " + newValue.toString() + " s", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        numberPickerDialogPreference_zoom.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                GestionVariables.cibleTime = Integer.parseInt(newValue.toString());
                Log.v("", "" + GestionVariables.cibleTime);
                Toast.makeText(getActivity(), "Délai : " + newValue.toString() + " s", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        numberPickerDialogPreference_sens.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                switch (Integer.parseInt(newValue.toString())) {
                    case 1:
                        GestionVariables.sensibilite = 50;
                        break;
                    case 2:
                        GestionVariables.sensibilite = 40;
                        break;
                    case 3:
                        GestionVariables.sensibilite = 30;
                        break;
                    case 4:
                        GestionVariables.sensibilite = 20;
                        break;
                    case 5:
                        GestionVariables.sensibilite = 15;
                        break;
                }
                Log.v("", "" + GestionVariables.sensibilite);
                Toast.makeText(getActivity(), "Sensibilité : " + newValue.toString(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        seekBarDialogPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                GestionVariables.vitesse_elocution = (float) Integer.parseInt(newValue.toString()) / 100;
                ActivityMain.reglageVitesseElocution(GestionVariables.vitesse_elocution);

                return true;
            }

        });

        checkboxMdp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue.toString().equals("true")) {
                    GestionPopUps.popUpCreerMotDePasse(getActivity(), getActivity());
                } else {
                    GestionVariables.passwordActive = false;
                    initialiserEditMdp(editTextMdp);
                    initialiserTempsAffichage();
                }
                return true;
            }
        });

        editTextMdp.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference arg0) {
                GestionPopUps.popUpCreerMotDePasse(getActivity(), getActivity());
                return true;
            }
        });

        aPropos.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference arg0) {
                GestionPopUps.popUpApropos(getActivity());
                return true;
            }
        });

        button_raz.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference arg0) {
                GestionOnglets.remiseDefautGrille();
                Toast.makeText(getActivity(), "Grille supprimée", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        button_save.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference arg0) {
                GestionPopUps.popUpSaisieProfil(getActivity());
                miseJourListes(GestionVariables.dossier_profils.list());
                return true;
            }
        });

        list_profils_ajout.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                GestionPopUps.popUpComfirmLoadProfil(getActivity(), newValue.toString());
                return false;
            }
        });

        list_profils_suppr.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                GestionPopUps.popUpComfirmSupprProfil(getActivity(), newValue.toString());
                return false;
            }
        });

        list_mode_son.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                if (newValue.toString().equals("1")) {
                    GestionVariables.pointagePicto = false;
                    GestionVariables.pointageBarre = true;
                } else if (newValue.toString().equals("2")) {
                    GestionVariables.pointagePicto = true;
                    GestionVariables.pointageBarre = false;
                } else if (newValue.toString().equals("3")) {
                    GestionVariables.pointagePicto = true;
                    GestionVariables.pointageBarre = true;
                }
                initialiserListModeSon();
                return false;
            }
        });
    }


    public static void miseJourListes(String[] profils){
        list_profils_ajout.setPersistent(false);

        list_profils_ajout.setEntries(profils);
        list_profils_ajout.setEntryValues(profils);
        list_profils_suppr.setEntries(profils);
        list_profils_suppr.setEntryValues(profils);
        list_profils_suppr.setPersistent(false);
        list_profils_ajout.setPersistent(false);
    }


    public static void initialiserCheckBox(final int MODE, CheckBoxPreference checkbox){
        checkbox.setPersistent(false);
        switch (MODE) {
            case SON:
                checkbox.setChecked(GestionVariables.sonActive);
                break;
            case PASSWORD:
                checkbox.setChecked(GestionVariables.passwordActive);
                break;
            case ZOOM:
                checkbox.setChecked(GestionVariables.zoom_actif);
            default:
                break;
        }
    }


    public static void initialiserEditMdp(Preference edit_text) {
        edit_text.setEnabled(GestionVariables.passwordActive);
    }


    public void initialiserVitesseElocution() {
        seekBarDialogPreference.setPersistent(false);
        seekBarDialogPreference.setEnabled(GestionVariables.sonActive);
        seekBarDialogPreference.setProgress((int) (GestionVariables.vitesse_elocution * 100));
    }


    public static void initialiserTempsAffichage(){
        numberPickerDialogPreference_mdp.setPersistent(false);
        numberPickerDialogPreference_mdp.setMinProgress(3);
        numberPickerDialogPreference_mdp.setEnabled(GestionVariables.passwordActive);
        numberPickerDialogPreference_mdp.setProgress(GestionVariables.dureeCompteur);
    }

    public static void initialiserTempsZoom(){
        numberPickerDialogPreference_zoom.setMinProgress(1);
        numberPickerDialogPreference_zoom.setPersistent(false);
        numberPickerDialogPreference_zoom.setEnabled(GestionVariables.zoom_actif);
        numberPickerDialogPreference_zoom.setProgress(GestionVariables.cibleTime);
    }

    public static void initialiserSens(){
        numberPickerDialogPreference_sens.setPersistent(false);
        numberPickerDialogPreference_sens.setMinProgress(1);
        numberPickerDialogPreference_sens.setEnabled(GestionVariables.zoom_actif);
        switch (GestionVariables.sensibilite) {
            case 50:
                numberPickerDialogPreference_sens.setProgress(1);
                break;
            case 40:
                numberPickerDialogPreference_sens.setProgress(2);
                break;
            case 30:
                numberPickerDialogPreference_sens.setProgress(3);
                break;
            case 20:
                numberPickerDialogPreference_sens.setProgress(4);
                break;
            case 15:
                numberPickerDialogPreference_sens.setProgress(5);
                break;
            default:
                numberPickerDialogPreference_sens.setProgress(3);
                break;
        }

    }

    public void initialiserListModeSon(){
        list_mode_son.setPersistent(false);
        list_mode_son.setEnabled(GestionVariables.sonActive);

        if (!GestionVariables.pointagePicto && GestionVariables.pointageBarre)
            list_mode_son.setValueIndex(0);
        else if(GestionVariables.pointagePicto && !GestionVariables.pointageBarre)
            list_mode_son.setValueIndex(1);
        else if(GestionVariables.pointagePicto && GestionVariables.pointageBarre)
            list_mode_son.setValueIndex(2);


    }
}
