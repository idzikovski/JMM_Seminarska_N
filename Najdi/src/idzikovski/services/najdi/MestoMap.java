package idzikovski.services.najdi;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class MestoMap extends Activity {

	GoogleMap mapa;
	GoogleMapOptions mapOptions;
	MestoMapFragment mapFragment;
	LatLng koordinati;
	Mesto selectedMesto;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_layout);
		selectedMesto=getIntent().getParcelableExtra("selectedMesto");
	
		
		
		
		mapFragment=new MestoMapFragment().newInstance(selectedMesto);
		
		
		
	
		
		
		FragmentTransaction fragTrans=getFragmentManager().beginTransaction();
		fragTrans.add(R.id.mapa, mapFragment);
		fragTrans.commit();
		
		mapa=mapFragment.getMap();
	
		
	}

}
