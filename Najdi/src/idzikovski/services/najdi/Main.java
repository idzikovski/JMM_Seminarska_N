package idzikovski.services.najdi;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.DateUtils;


import com.google.gson.Gson;

import org.joda.time.DateTime;
import org.joda.time.Days;

import idzikovski.services.najdi.R;
import idzikovski.services.najdi.atasks.GetAllMestaTask;
import idzikovski.services.najdi.data.NajdiDao;
import idzikovski.services.najdi.data.NajdiOpenHelper;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.media.RingtoneManager;

public class Main extends Activity {

	ProgressBar pb;
	HttpResponse response;
	Button btnListaj,btnDodadi;
	NajdiDao db;
	DateTime sega,zacuvan;
	String dateBuf;
	public static final String PREFS_NAME="Preferenci";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		sega=new DateTime().now();
		SharedPreferences settings=getSharedPreferences(PREFS_NAME, 0);
		dateBuf=settings.getString("lastUpdate", "def");
		
		IntentFilter filter=new IntentFilter(GetAllMestaTask.ITEMS_DOWNLOADED_ACTION);
		registerReceiver(new OnDownloadReceiver(), filter);
		startService(new Intent(this, DownloadService.class));
		
/*
		if (dateBuf.equals("def"))
		{
			IntentFilter filter=new IntentFilter(GetAllMestaTask.ITEMS_DOWNLOADED_ACTION);
			registerReceiver(new OnDownloadReceiver(), filter);
			startService(new Intent(this, DownloadService.class));
			
			SharedPreferences.Editor editor=settings.edit();
			editor.putString("lastUpdate", sega.toString());
			editor.commit();
		}
		else{
			zacuvan=new DateTime(dateBuf);
			int denovi=Days.daysBetween(sega, zacuvan).getDays();
			
			Log.i("Debug", Integer.toString(denovi));
			
			if (denovi>=1){
				IntentFilter filter=new IntentFilter(GetAllMestaTask.ITEMS_DOWNLOADED_ACTION);
				registerReceiver(new OnDownloadReceiver(), filter);
				startService(new Intent(this, DownloadService.class));
			}
		}
		
	*/	
		new KategoriiTask().execute();
		setContentView(R.layout.activity_main);
		btnListaj=(Button) findViewById(R.id.btnListaj);
		btnDodadi=(Button) findViewById(R.id.btnDodadi);
		
		btnListaj.setVisibility(View.INVISIBLE);
		btnDodadi.setVisibility(View.INVISIBLE);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}
	
	public void btnDodadi_Clicked(View view){
		Intent dodadi=new Intent(this, Vnes.class);
		startActivity(dodadi);
	}
	public void btnListaj_Clicked(View view){
		try {
			
			Intent listaj=new Intent(this, Listaj.class);
			startActivity(listaj);
		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
		
	}
	
	
/** Klasa za polnenje na listata so kategoriii    */
	
	private class KategoriiTask extends AsyncTask<Void, Void, Void>{

		
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			btnListaj.setVisibility(View.VISIBLE);
			btnDodadi.setVisibility(View.VISIBLE);
			
			pb=(ProgressBar) findViewById(R.id.pbLoading);
			pb.setVisibility(View.INVISIBLE);
			
			String jsonResponse=GetAllMestaTask.entityToString(response.getEntity());
			
			Kategorija.kategorii=fromJSONtoKategorija(jsonResponse);
			
			
			
		}

		

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			HttpClient httpClient = new DefaultHttpClient();
		    HttpPost httpPost = new HttpPost("http://idzikovski.heliohost.org/GetKategorija.php");
		    
		    try {
				response = httpClient.execute(httpPost);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
		
	}
	
	public static Kategorija[] fromJSONtoKategorija(String response) {
		Gson gson = new Gson();
        return gson.fromJson(response, Kategorija[].class);
	}
	
	public class OnDownloadReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			
			/* Isprati notifikacija */
			
			
			long[] vibPattern={0,200,200,500,200,200};
			Uri uri=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			
			Intent listajIntent=new Intent(Main.this, Listaj.class);
			PendingIntent pIntent=PendingIntent.getActivity(Main.this, 0, listajIntent, 0);
			
			Notification noti=new NotificationCompat.Builder(Main.this)
			.setContentText("Imate novi podatoci")
			.setContentTitle("Podatoci")
			.setLights(Color.BLUE, 1000, 2000)
			.setVibrate(vibPattern)
			.setSound(uri)
			.setSmallIcon(R.drawable.ic_launcher)
			.setContentIntent(pIntent)
			.build();
			
			NotificationManager notiManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
			noti.flags |= Notification.FLAG_AUTO_CANCEL;
			
			notiManager.notify(0, noti);
			
			
			//Toast.makeText(Main.this, "Podatocite se obnoveni", Toast.LENGTH_LONG).show();
			stopService(new Intent(Main.this,DownloadService.class));
			
			Main.this.unregisterReceiver(this);
		}
		
	}
}
