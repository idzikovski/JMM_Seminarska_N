package idzikovski.services.najdi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import com.google.gson.Gson;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

public class Listaj extends ListActivity {

	Mesto mesta[];
	HttpResponse response;
	ProgressBar pb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listaj);
		
		pb=(ProgressBar) findViewById(R.id.pbLoading);
		pb.setVisibility(View.INVISIBLE);
		
		new HttpRequestTask().execute();
		

	}
	
	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Mesto selectedMesto=mesta[position];
		
		Intent detali=new Intent(Listaj.this,MestoDetali.class);
		detali.putExtra("selectedMesto", selectedMesto);
		startActivity(detali);
	}



	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//pb=(ProgressBar) findViewById(R.id.pbLoading);
		//pb.setVisibility(View.VISIBLE);
	}



	private class HttpRequestTask extends AsyncTask<Void, Void, Void>{

		
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pb=(ProgressBar) findViewById(R.id.pbLoading);
			pb.setVisibility(View.VISIBLE);
			
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			
			HttpClient httpClient = new DefaultHttpClient();
		    HttpPost httpPost = new HttpPost("http://idzikovski.heliohost.org/SelectAll.php");
		    
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

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			
			super.onPostExecute(result);
			
			pb=(ProgressBar) findViewById(R.id.pbLoading);
			pb.setVisibility(View.INVISIBLE);
			
			String jsonResponse=entityToString(response.getEntity());
			
			mesta=fromJSONtoMesto(jsonResponse);
			
			MestoArrayAdapter adapter=new MestoArrayAdapter(Listaj.this, mesta);
			setListAdapter(adapter);
			
			
			
		}
		
	}
	
	/** Metod za konverzija od http entity vo string */
	
	public static String entityToString(HttpEntity entity) {
		  InputStream is;
		  StringBuilder str = new StringBuilder();
		try {
			is = entity.getContent();
		
		  BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
		  

		  String line = null;
		  try {
		    while ((line = bufferedReader.readLine()) != null) {
		      str.append(line + "\n");
		    }
		  } catch (IOException e) {
		    throw new RuntimeException(e);
		  } finally {
		    try {
		      is.close();
		    } catch (IOException e) {
		      //tough luck...
		    }
		  }
		  
		} catch (IllegalStateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		  return str.toString();
		}
	
	/** Metod za konverzija od json odgovor vo pole od mesta */
	
	public static Mesto[] fromJSONtoMesto(String response) {
        Gson gson = new Gson();
        return gson.fromJson(response, Mesto[].class);
    }
}
