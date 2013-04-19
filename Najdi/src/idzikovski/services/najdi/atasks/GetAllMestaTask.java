package idzikovski.services.najdi.atasks;

import idzikovski.services.najdi.Listaj;
import idzikovski.services.najdi.Mesto;
import idzikovski.services.najdi.MestoArrayAdapter;
import idzikovski.services.najdi.R;
import idzikovski.services.najdi.data.NajdiDao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.AsyncTask;
import android.widget.Toast;


public class GetAllMestaTask extends  AsyncTask<Void, Void, Void> {
	
	public static final String ITEMS_DOWNLOADED_ACTION="idzikovski.services.najdi.ITEMS_DOWNLOADED_ACTION";
	
	ArrayList <Mesto> mesta;
	HttpResponse response;
	protected Context context;
	
	
	public GetAllMestaTask(Context context){
		this.context=context;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		
		
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
		
		String jsonResponse=entityToString(response.getEntity());
		mesta=fromJSONtoMesto(jsonResponse);
		NajdiDao db=new NajdiDao(context);
		
		try {
			db.open();
			db.deleteAll();
			for (Mesto mesto:mesta){
				db.insert(mesto);
			}
			db.close();
			
		} catch (Exception e) {
			Toast.makeText(context, "Nastana greska pri vnes", Toast.LENGTH_LONG).show();
		}

		
		Intent intent=new Intent(ITEMS_DOWNLOADED_ACTION);
		context.sendBroadcast(intent);
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
	
	/** Metod za konverzija od json odgovor vo lista od mesta */
	
	public static ArrayList <Mesto> fromJSONtoMesto(String response) {
        Gson gson = new Gson();
        Mesto [] mesta=gson.fromJson(response, Mesto[].class);
        ArrayList <Mesto> items=new ArrayList<Mesto>();
        for (Mesto item:mesta){
        	items.add(item);
        }
        return items;
    }
}
