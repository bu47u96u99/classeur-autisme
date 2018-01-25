package ovh.polaris.classeurautisme.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import ovh.polaris.classeurautisme.R;
import ovh.polaris.classeurautisme.ui.bibliopicto.ActivityBiblioPicto;
import ovh.polaris.classeurautisme.ui.main.ActivityMain;
import ovh.polaris.classeurautisme.ui.preferences.fragments.FragmentPreferences;

import java.io.File;
import java.util.ArrayList;

public class GestionPopUps {

    /*
     *
     */
    static Dialog boite_saisie_profil;

    public static void popUpSaisieProfil(final Activity activity) {
        boite_saisie_profil = new Dialog(activity);
        boite_saisie_profil.setContentView(R.layout.boite_saisie_profil);
        boite_saisie_profil.setTitle("Saisie du profil");
        boite_saisie_profil.setCancelable(false);

        Button bouton_ok = (Button) boite_saisie_profil.findViewById(R.id.bouton_ok);
        bouton_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText champ_profil = (EditText) boite_saisie_profil.findViewById(R.id.EditText_boite_saisie_profil);
                String profil = champ_profil.getText().toString();
                if (profil.equals("")) {
                    Toast.makeText(activity.getApplicationContext(), "Entrez un nom d'au moins un caractère", Toast.LENGTH_SHORT).show();
                    return;
                }
                String[] nomProfils = GestionVariables.dossier_profils.list();
                if (nomProfils != null) {
                    for (String nomProfile : nomProfils) {
                        if (profil.equals(nomProfile)) {
                            popUpComfirmEraseProfil(activity, profil);
                            champ_profil.setText("");
                            return;
                        }
                    }
                }
                GestionBandePhrase.viderBandePhrase();
                GestionSauvegarde.sauvegardeInternalStorage(GestionProfils.creerDossierProfil(profil));
                Toast.makeText(activity, "Profil sauvergardé sous le nom : " + profil, Toast.LENGTH_SHORT).show();
                FragmentPreferences.miseJourListes(GestionVariables.dossier_profils.list());
                boite_saisie_profil.dismiss();
            }
        });

        Button bouton_annuler= (Button) boite_saisie_profil.findViewById(R.id.bouton_annuler);
        bouton_annuler.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boite_saisie_profil.dismiss();
            }

        });
        boite_saisie_profil.show();
    }


    public static void popUpComfirmEraseProfil(final Activity activity, final String profil) {
        final AlertDialog.Builder comfirmErase = new AlertDialog.Builder(activity);
        comfirmErase.setTitle("Ecraser le profil");
        comfirmErase.setMessage("Voulez-vous écraser le profil existant ?");
        comfirmErase.setCancelable(false);
        comfirmErase.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                GestionBandePhrase.viderBandePhrase();
                GestionProfils.supprimerProfil(profil);
                GestionSauvegarde.sauvegardeInternalStorage(GestionProfils.creerDossierProfil(profil));
                FragmentPreferences.miseJourListes(GestionVariables.dossier_profils.list());
                Toast.makeText(activity, "Profil " + profil + " sauvergardé", Toast.LENGTH_SHORT).show();
                boite_saisie_profil.dismiss();
            }
        });
        comfirmErase.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        comfirmErase.setIcon(android.R.drawable.ic_dialog_alert);
        comfirmErase.show();
    }

    /*
     * Pop-up
     */
    public static void popUpComfirmSupprProfil(final Activity activity, final String profil) {
        AlertDialog.Builder comfirSuppr = new AlertDialog.Builder(activity);
        comfirSuppr.setTitle("Supprimer");
        comfirSuppr.setMessage("Supprimer le profil ?");
        comfirSuppr.setCancelable(false);
        comfirSuppr.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                GestionProfils.supprimerProfil(profil);
                Toast.makeText(activity, "Profil " + profil + " supprimé", Toast.LENGTH_SHORT).show();
                FragmentPreferences.miseJourListes(GestionVariables.dossier_profils.list());
            }
        });
        comfirSuppr.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        comfirSuppr.setIcon(android.R.drawable.ic_dialog_alert);
        comfirSuppr.show();
    }

    /*
     *
     */
    public static void popUpComfirmLoadProfil(final Activity activity, final String profil) {
        AlertDialog.Builder comfirLoad = new AlertDialog.Builder(activity);
        comfirLoad.setTitle("Charger un profil");
        comfirLoad.setMessage("Charger le profil sélectionné ?");
        comfirLoad.setCancelable(false);
        comfirLoad.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                GestionOnglets.remiseDefautGrille();
                GestionSauvegarde.chargementConfigurationExterne(GestionProfils.creerDossierProfil(profil), activity.getBaseContext(), GestionVariables.MODE_PROFIL);
                FragmentPreferences.initialiserCheckBox(FragmentPreferences.SON, FragmentPreferences.checkboxSon);
                FragmentPreferences.initialiserCheckBox(FragmentPreferences.PASSWORD, FragmentPreferences.checkboxMdp);
                FragmentPreferences.initialiserEditMdp(FragmentPreferences.editTextMdp);
                FragmentPreferences.initialiserCheckBox(FragmentPreferences.ZOOM, FragmentPreferences.checkboxZoom);
                FragmentPreferences.initialiserTempsAffichage();
                FragmentPreferences.initialiserTempsZoom();
                FragmentPreferences.initialiserSens();
                Toast.makeText(activity, "Profil " + profil + " chargé", Toast.LENGTH_SHORT).show();
            }
        });
        comfirLoad.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        comfirLoad.setIcon(android.R.drawable.ic_dialog_alert);
        comfirLoad.show();
    }


    public static void popUpComfirmationQuitter(final Activity activity) {
        AlertDialog.Builder comfirQuitter = new AlertDialog.Builder(activity);
        comfirQuitter.setTitle("Quitter");
        comfirQuitter.setMessage("Quitter l'application ?");
        comfirQuitter.setCancelable(true);
        comfirQuitter.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ActivityMain.fermetureApplication(activity);
            }
        });
        comfirQuitter.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        comfirQuitter.setIcon(android.R.drawable.ic_dialog_alert);
        comfirQuitter.show();
    }


    public static void popUpMotDePasse(final Activity activity, final Context context, final int MODE) {
        final Dialog boite_saisie_mot_de_passe = new Dialog(activity);
        final CountDownTimer timer = new CountDownTimer(GestionVariables.dureeCompteur * 1000, 1000) {
            @Override
            public void onFinish() {
                boite_saisie_mot_de_passe.dismiss();
            }

            @Override
            public void onTick(long millisUntilFinished) {
            }
        };


        boite_saisie_mot_de_passe.setContentView(R.layout.boite_saisie_mot_de_passe);
        boite_saisie_mot_de_passe.setTitle("Saisir le mot de passe");
        boite_saisie_mot_de_passe.setCancelable(true);

        Button bouton_ok = (Button) boite_saisie_mot_de_passe.findViewById(R.id.bouton_ok);
        bouton_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText passField = (EditText) boite_saisie_mot_de_passe.findViewById(R.id.passwordField);
                if (passField.getText().toString().equals(GestionVariables.password)) {
                    switch (MODE) {
                        case GestionVariables.MODE_AJOUT:
                            GestionBandePhrase.modeNormal(ActivityMain.bouton_clear_barre, ActivityMain.bouton_phrase, ActivityMain.bouton_menu, ActivityMain.layoutBarreBouton);
                            ActivityMain.lancerBiblioPicto(activity);
                            break;
                        case GestionVariables.MODE_SUPPR:
                            GestionBandePhrase.modeSuppression(activity.getBaseContext(), ActivityMain.bouton_clear_barre, ActivityMain.bouton_phrase, ActivityMain.bouton_menu, ActivityMain.layoutBarreBouton);
                            break;
                        case GestionVariables.MODE_PREFS:
                            GestionBandePhrase.modeNormal(ActivityMain.bouton_clear_barre, ActivityMain.bouton_phrase, ActivityMain.bouton_menu, ActivityMain.layoutBarreBouton);
                            ActivityMain.lancerPreferences(activity, context);
                            break;
                        case GestionVariables.MODE_QUIT:
                            GestionPopUps.popUpComfirmationQuitter(activity);
                            break;
                        default:
                            break;
                    }
                    boite_saisie_mot_de_passe.dismiss();
                    timer.cancel();
                    return;
                } else {
                    Toast.makeText(activity.getApplicationContext(), "Mauvais mot de passe", Toast.LENGTH_SHORT).show();
                    passField.setText("");
                }
                timer.cancel();
            }
        });

        Button bouton_annuler = (Button) boite_saisie_mot_de_passe.findViewById(R.id.bouton_annuler);
        bouton_annuler.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boite_saisie_mot_de_passe.dismiss();
                timer.cancel();
            }

        });
        boite_saisie_mot_de_passe.show();
        timer.start();
    }


    public static void popUpCreerMotDePasse(final Activity activity, final Context context) {
        final Dialog boite_saisie_mot_de_passe = new Dialog(activity);

        boite_saisie_mot_de_passe.setContentView(R.layout.boite_creer_mot_de_passe);
        boite_saisie_mot_de_passe.setTitle("Entrez le mot de passe (moins de 8 caractères)");
        boite_saisie_mot_de_passe.setCancelable(false);

        Button bouton_ok = (Button) boite_saisie_mot_de_passe.findViewById(R.id.bouton_ok);
        bouton_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText passField = (EditText) boite_saisie_mot_de_passe.findViewById(R.id.passwordField);
                String password = passField.getText().toString();
                if (password.equals("")) {
                    Toast.makeText(activity.getApplicationContext(), "Entrez un nom d'au moins un caractère", Toast.LENGTH_SHORT).show();
                    passField.setText("");
                } else if (password.length() > 8) {
                    Toast.makeText(activity.getApplicationContext(), "Mot de passe trop long !", Toast.LENGTH_SHORT).show();
                    passField.setText("");
                } else {
                    Toast.makeText(activity.getApplicationContext(), "Mot de passe : " + password, Toast.LENGTH_SHORT).show();
                    GestionVariables.password = password;
                    GestionVariables.passwordActive = true;
                    FragmentPreferences.initialiserCheckBox(FragmentPreferences.PASSWORD, FragmentPreferences.checkboxMdp);
                    FragmentPreferences.initialiserEditMdp(FragmentPreferences.editTextMdp);
                    FragmentPreferences.initialiserTempsAffichage();
                    boite_saisie_mot_de_passe.dismiss();
                }
            }

        });

        Button bouton_annuler= (Button) boite_saisie_mot_de_passe.findViewById(R.id.bouton_annuler);
        bouton_annuler.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boite_saisie_mot_de_passe.dismiss();
                FragmentPreferences.initialiserCheckBox(FragmentPreferences.PASSWORD, FragmentPreferences.checkboxMdp);
            }

        });
        boite_saisie_mot_de_passe.show();

    }

    public static void popUpRenommer(final Activity activity, final int position) {
        if (GestionVariables.dossier_pictos.exists()) {
            Log.v("pos", "" + position);

            final Dialog boite_changer_syntaxe = new Dialog(activity);
            boite_changer_syntaxe.setContentView(R.layout.boite_changer_syntaxe_picto);
            boite_changer_syntaxe.setTitle("Renommer");
            boite_changer_syntaxe.setCancelable(false);

            TextView texte_ancienne_syntaxe = (TextView) boite_changer_syntaxe.findViewById(R.id.TextView_boite_changer_syntaxe_ancienne_syntaxe);
            texte_ancienne_syntaxe.setText(GestionVariables.syntaxe_stockage_externe.get(position));

            Button bouton_ok = (Button) boite_changer_syntaxe.findViewById(R.id.bouton_ok);
            bouton_ok.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText editText = (EditText) boite_changer_syntaxe.findViewById(R.id.EditText_boite_changer_syntaxe);
                    String texte_boite_dialogue = editText.getText().toString();
                    if (texte_boite_dialogue.equals("")) {
                        Toast.makeText(activity.getApplicationContext(), "Entrez un nom d'au moins un caractère", Toast.LENGTH_SHORT).show();
                        editText.setText("");
                        return;
                    }

                    if (GestionFichiers.rechercherNomExistant(GestionFichiers.formatageChaineFichier(texte_boite_dialogue))) {
                        String nom_fichier = texte_boite_dialogue;
                        int i = 2;
                        nom_fichier = nom_fichier + "_" + i;
                        while (GestionFichiers.rechercherNomExistant(GestionFichiers.formatageChaineFichier(nom_fichier)) && i < 100) {
                            nom_fichier = nom_fichier.substring(0, nom_fichier.length() - 2) + "_" + i;
                            i++;
                        }
                        if (GestionFichiers.rechercherNomExistant(GestionFichiers.formatageChaineFichier(nom_fichier))) {
                            Toast.makeText(activity.getApplicationContext(), "Echec 100 noms ...", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        texte_boite_dialogue = nom_fichier;
                    }

                    GestionFichiers.renommerFichier(GestionVariables.files_stockage_externe.get(position), GestionFichiers.formatageChaineFichier(texte_boite_dialogue) + ".jpg");
                    ActivityBiblioPicto.miseaJour(activity);
                    boite_changer_syntaxe.dismiss();
                }
            });

            Button bouton_annuler = (Button) boite_changer_syntaxe.findViewById(R.id.bouton_annuler);
            bouton_annuler.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    boite_changer_syntaxe.dismiss();
                }
            });
            boite_changer_syntaxe.show();

        }
    }

    public static void popUpSaisieSyntaxe(final Activity activity, final File photo) {


        final Dialog boite_saisie_syntaxe = new Dialog(activity);
        boite_saisie_syntaxe.setContentView(R.layout.boite_saisie_syntaxe_picto);
        boite_saisie_syntaxe.setTitle("Signification du pictogramme");
        boite_saisie_syntaxe.setCancelable(false);

        Button bouton_ok = (Button) boite_saisie_syntaxe.findViewById(R.id.bouton_ok);
        bouton_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText texte = (EditText) boite_saisie_syntaxe.findViewById(R.id.EditText_boite_saisie_syntaxe);
                String texte_boite_dialogue = texte.getText().toString();
                if (texte_boite_dialogue.equals("")) {
                    Toast.makeText(activity.getApplicationContext(), "Entrez un nom d'au moins un caractère", Toast.LENGTH_SHORT).show();
                    texte.setText("");
                    return;
                }
                if (GestionFichiers.rechercherNomExistant(GestionFichiers.formatageChaineFichier(texte_boite_dialogue))) {
                    String nom_fichier = texte_boite_dialogue;
                    int i = 2;
                    nom_fichier = nom_fichier + "_" + i;
                    while (GestionFichiers.rechercherNomExistant(GestionFichiers.formatageChaineFichier(nom_fichier)) && i < 100) {
                        nom_fichier = nom_fichier.substring(0, nom_fichier.length() - 2) + "_" + i;
                        i++;
                    }
                    if (GestionFichiers.rechercherNomExistant(GestionFichiers.formatageChaineFichier(nom_fichier))) {
                        Toast.makeText(activity.getApplicationContext(), "Echec 100 noms ...", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    texte_boite_dialogue = nom_fichier;
                }


                GestionFichiers.copierFichier(photo, new File(GestionVariables.dossier_pictos + File.separator + GestionFichiers.formatageChaineFichier(texte_boite_dialogue) + ".jpg"));
                GestionFichiers.suppressionContenuDossier(GestionVariables.dossier_temp);
                ActivityBiblioPicto.miseaJour(activity);
                boite_saisie_syntaxe.dismiss();
            }
        });

        Button bouton_annuler = (Button) boite_saisie_syntaxe.findViewById(R.id.bouton_annuler);
        bouton_annuler.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boite_saisie_syntaxe.dismiss();
            }

        });
        boite_saisie_syntaxe.show();
    }


    public static void popUpNavigateurFichier(final Activity activity, final File source) {
        final Dialog boite_saisie_syntaxe = new Dialog(activity);
        boite_saisie_syntaxe.setContentView(R.layout.boite_saisie_syntaxe_picto);
        boite_saisie_syntaxe.setTitle("Saisir le nom du pictogramme");
        boite_saisie_syntaxe.setCancelable(false);

        Button bouton_ok = (Button) boite_saisie_syntaxe.findViewById(R.id.bouton_ok);
        bouton_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText texte = (EditText) boite_saisie_syntaxe.findViewById(R.id.EditText_boite_saisie_syntaxe);
                String texte_boite_dialogue = texte.getText().toString();

                if (texte_boite_dialogue.equals("")) {
                    Toast.makeText(activity.getApplicationContext(), "Entrez un nom d'au moins un caractère", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (GestionFichiers.rechercherNomExistant(GestionFichiers.formatageChaineFichier(texte_boite_dialogue))) {
                    String nom_fichier = texte_boite_dialogue;
                    int i = 2;
                    nom_fichier = nom_fichier + "_" + i;
                    while (GestionFichiers.rechercherNomExistant(GestionFichiers.formatageChaineFichier(nom_fichier)) && i < 100) {
                        nom_fichier = nom_fichier.substring(0, nom_fichier.length() - 2) + "_" + i;
                        i++;
                    }
                    if (GestionFichiers.rechercherNomExistant(GestionFichiers.formatageChaineFichier(nom_fichier))) {
                        Toast.makeText(activity.getApplicationContext(), "Echec 100 noms ...", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    texte_boite_dialogue = nom_fichier;
                }
                GestionFichiers.copierFichier(source, new File(GestionVariables.dossier_pictos + File.separator + GestionFichiers.formatageChaineFichier(texte_boite_dialogue) + ".jpg"));
                ActivityBiblioPicto.miseaJour(activity);
                boite_saisie_syntaxe.dismiss();
            }

        });

        Button bouton_annuler = (Button) boite_saisie_syntaxe.findViewById(R.id.bouton_annuler);
        bouton_annuler.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boite_saisie_syntaxe.dismiss();
            }

        });
        boite_saisie_syntaxe.show();
    }

    public static void popUpComfirSupprImage(final Activity activity, final int position) {
        AlertDialog.Builder comfirSuppr = new AlertDialog.Builder(activity);
        comfirSuppr.setTitle("Suppression");
        comfirSuppr.setMessage("Supprimer le pictogramme ?");
        comfirSuppr.setPositiveButton("oui", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (GestionVariables.dossier_pictos != null) {
                    ArrayList<File> liste = GestionFichiers.listerFichiersDossierPicto();
                    File image = liste.get(position);
                    if (image.delete()) {
                        ActivityBiblioPicto.miseaJour(activity);
                    } else {
                        Toast.makeText(activity.getApplicationContext(), "Erreur durant la suppression du pictogramme", Toast.LENGTH_SHORT).show();
                    }
                }
                Toast.makeText(activity.getApplicationContext(), "Suppression du pictogramme", Toast.LENGTH_SHORT).show();
            }
        });
        comfirSuppr.setNegativeButton("non", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        comfirSuppr.setIcon(android.R.drawable.ic_dialog_alert);
        comfirSuppr.show();
    }


    public static void popUpApropos(Activity activity) {
        AlertDialog.Builder comfirSuppr = new AlertDialog.Builder(activity);
        comfirSuppr.setTitle("A propos");
        comfirSuppr.setMessage(activity.getResources().getString(R.string.apropos));
        comfirSuppr.setPositiveButton("Fermer", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        comfirSuppr.setIcon(R.drawable.logo);
        comfirSuppr.show();
    }
}
