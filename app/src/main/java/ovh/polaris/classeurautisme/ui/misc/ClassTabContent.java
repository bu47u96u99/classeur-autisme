package ovh.polaris.classeurautisme.ui.misc;

import android.content.Context;
import android.view.View;
import android.widget.TabHost.TabContentFactory;

public class ClassTabContent implements TabContentFactory{
	private Context mContext;
	
	public ClassTabContent(Context context){
		mContext = context;
	}
			
	@Override
	public View createTabContent(String tag) {
		return new View(mContext);
	}
}
