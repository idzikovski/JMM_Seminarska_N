package idzikovski.services.najdi;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;

import idzikovski.services.najdi.R;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;


public class Main extends Activity {

	ProgressBar pb;
	HttpResponse response;
	Button btnListaj,btnDodadi;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
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
		Intent listaj=new Intent(this, Listaj.class);
		startActivity(listaj);
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
			
			String jsonResponse=Listaj.entityToString(response.getEntity());
			
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
	
}
