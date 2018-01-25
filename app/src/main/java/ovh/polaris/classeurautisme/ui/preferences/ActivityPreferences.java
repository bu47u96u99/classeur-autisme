package ovh.polaris.classeurautisme.ui.preferences;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import ovh.polaris.classeurautisme.R;
import ovh.polaris.classeurautisme.ui.preferences.fragments.FragmentPreferences;


public class ActivityPreferences extends FragmentActivity {

	@Override
	    public void onCreate(Bundle savedInstanceState) {
	    	super.onCreate(savedInstanceState);
	    	setContentView(R.layout.preferences);
	    	setTitle("Préférences");
		    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	    	getFragmentManager().beginTransaction().replace(R.id.FrameLayout,new FragmentPreferences()).commit();
	 }
	
}

