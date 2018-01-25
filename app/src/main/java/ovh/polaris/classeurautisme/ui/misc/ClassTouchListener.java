package ovh.polaris.classeurautisme.ui.misc;

import android.content.ClipData;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnTouchListener;
import ovh.polaris.classeurautisme.utils.GestionVariables;

public final class ClassTouchListener implements OnTouchListener {

	public boolean onTouch(View view, MotionEvent motionEvent) {
		View view_parent = (View) view.getParent();
		if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
			GestionVariables.startTime = System.currentTimeMillis();

			boolean onglet = false;

			for(int k=0;k<GestionVariables.NB_ONGLETS;k++){
				for(int l=0;l<GestionVariables.NB_PICTO;l++){												// kl parent mn cible
					if(view_parent.getId() == (GestionVariables.ids_layoutPicto[k][l])){
						GestionVariables.location_departX = (int)(motionEvent.getX()*GestionVariables.densite + 60);
						GestionVariables.location_departY = (int)(motionEvent.getY()*GestionVariables.densite + 30 );
						onglet = true;
					}
				}
			}

			if(!onglet){
				GestionVariables.location_departX = (int)(motionEvent.getX()*GestionVariables.densite + 10);
				GestionVariables.location_departY = (int)(motionEvent.getY()*GestionVariables.densite + 10);
			}

			ClipData data = ClipData.newPlainText("", "");
			DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
			view.startDrag(data, shadowBuilder, view, 0);
			view.setVisibility(View.VISIBLE);
			GestionVariables.fenetrePopIm = false;
			GestionVariables.fuite = false;
			GestionVariables.touchCount = 0;
		}
		return false;
	}
}