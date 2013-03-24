package idzikovski.services.najdi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MestoMapFragment extends MapFragment {

	private LatLng pozicija;
	private Mesto mesto;
	

	public MestoMapFragment(){
		super();
	}
	
	public static MestoMapFragment newInstance(Mesto mesto){
		MestoMapFragment frag=new MestoMapFragment();
		frag.pozicija=new LatLng(mesto.getKoordinatax(),mesto.getKoordinatay());
		frag.mesto=mesto;
		return frag;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view= super.onCreateView(inflater, container, savedInstanceState);
		
		UiSettings settings = getMap().getUiSettings();
	    settings.setAllGesturesEnabled(true);
	    settings.setMyLocationButtonEnabled(false);
	    
	    getMap().setMapType(GoogleMap.MAP_TYPE_HYBRID);
	    
	    getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(pozicija, 16));
	    
	    getMap().addMarker(new MarkerOptions().position(pozicija).title(mesto.getIme())).showInfoWindow();
		
	    
	 
	    
		return view;
		
	}

	
}
