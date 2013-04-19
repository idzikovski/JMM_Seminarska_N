package idzikovski.services.najdi;

import java.util.ArrayList;

import idzikovski.services.najdi.Main.OnDownloadReceiver;
import idzikovski.services.najdi.atasks.GetAllMestaTask;
import idzikovski.services.najdi.data.NajdiDao;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Listaj extends ListActivity {

	ArrayList <Mesto> mesta;
	ProgressBar pb;
	NajdiDao db;
	MestoArrayAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listaj);
		
		pb=(ProgressBar) findViewById(R.id.pbLoading);
		pb.setVisibility(View.INVISIBLE);
		
		db=new NajdiDao(this);
		
		try {
			db.open();
			mesta=db.getAllItems();
			adapter=new MestoArrayAdapter(this, mesta);
			setListAdapter(adapter);
			db.close();
		} 
		catch (NullPointerException ex){
			Toast.makeText(this,"Null", Toast.LENGTH_LONG).show();
		}
		catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(this,e.getMessage(), Toast.LENGTH_LONG).show();
		}
		
	}
	
	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Mesto selectedMesto=mesta.get(position);
		
		Intent detali=new Intent(Listaj.this,MestoDetali.class);
		detali.putExtra("selectedMesto", selectedMesto);
		startActivity(detali);
	}



	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(1,1,1,"Refresh with service");
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		
		IntentFilter filter=new IntentFilter(GetAllMestaTask.ITEMS_DOWNLOADED_ACTION);
		registerReceiver(new OnDownloadReceiver(), filter);
		startService(new Intent(this, DownloadService.class));
		
		return super.onMenuItemSelected(featureId, item);
	}
	
	
	public class OnDownloadReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				db.open();
				mesta=db.getAllItems();
				adapter.clear();
				adapter.addAll(mesta);
				db.close();
				
				Toast.makeText(Listaj.this, "List updated", Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
	}

}
