package idzikovski.services.najdi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import com.google.gson.Gson;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.location.*;
import android.provider.Settings;

public class Vnes extends Activity {
	
	EditText et;
	TextView status;
	ImageView iv;
	Location lokacija;
	Uri imageUri;
	ProgressBar pb;
	Spinner sp;
	Bitmap slika, slikaNamalena;
	
	
	private static final int MAX_HEIGHT=200;
	private static final int MAX_HEIGHT_WEB=500;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
		
		setContentView(R.layout.activity_vnes);
		
		
		// Inicajilzacija na listata so kategorii
		
		sp=(Spinner) findViewById(R.id.spKategorija);
		KategorijaArrayAdapter adapter=new KategorijaArrayAdapter(Vnes.this, Kategorija.kategorii);
		sp.setAdapter(adapter);
		
		
		
		pb=(ProgressBar) findViewById(R.id.pbLoading);
		pb.setVisibility(View.INVISIBLE);
		
		if (savedInstanceState!=null)
		{
			slikaNamalena=savedInstanceState.getParcelable("slika");
			iv=(ImageView)findViewById(R.id.imgPrikaz);
			iv.setImageBitmap(slikaNamalena);
		}
		
		/*Toast.makeText(this, "Please turn on gps", Toast.LENGTH_LONG).show();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(myIntent);*/
		
	// Kreiranje na objekti potrebni za lokacijata
		
		
		LocationManager  locationManager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		lokacija=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		
		LocationListener locationListener=new LocationListener()
		{
			public void onLocationChanged(Location location)
			{
				lokacija=location;
				//Toast.makeText(Vnes.this, String.valueOf(lokacija.getLatitude()), Toast.LENGTH_LONG).show();
			}
			 public void onStatusChanged(String provider, int status, Bundle extras) {}

			    public void onProviderEnabled(String provider) {}

			    public void onProviderDisabled(String provider) {}
		};
		
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.vnes, menu);
		return true;
	}
	
	public void btnIsprati_Clicked(View view)
	{
		new NovoMestoTask().execute();
	}
	
	
	
	
	/** Metoda za zacuvuvanje na sostojbata na aktivnosta pri unistuvanje na aktivnosta */
	
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		if (slikaNamalena!=null)
			outState.putParcelable("slika", slikaNamalena);
		
		
	}





	/** Klasa za asinhron http post za zapisuvanje na novo mesto vo bazata*/

	private class NovoMestoTask extends AsyncTask<Void, Void, Void>
	{

		
		
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			//setProgressBarIndeterminateVisibility(true);
			pb=(ProgressBar) findViewById(R.id.pbLoading);
			pb.setVisibility(View.VISIBLE);
		}


		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			
			
			//Isprakanje na http baranje za zapisuvanje na novo mesto vo bazata

		    
		    File mediaStorageDir=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"Najdi");
		    File mediaFile=null;
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
			
			et=(EditText)findViewById(R.id.etIme);
	        nameValuePairs.add(new BasicNameValuePair("ime", et.getText().toString()));
	        
	        et=(EditText)findViewById(R.id.etOpis);
	        nameValuePairs.add(new BasicNameValuePair("opis", et.getText().toString()));
			
	        sp=(Spinner)findViewById(R.id.spKategorija);
	        nameValuePairs.add(new BasicNameValuePair("kategorija", Integer.toString(sp.getSelectedItemPosition()+1)));
	        
	        nameValuePairs.add(new BasicNameValuePair("slika", mediaStorageDir.getPath()+File.separator+"IMG_R.jpg"));
	        
	        nameValuePairs.add(new BasicNameValuePair("koordinatax", Double.toString(lokacija.getLatitude())));
	        
	        nameValuePairs.add(new BasicNameValuePair("koordinatay", Double.toString(lokacija.getLongitude())));
	        
	        HttpClient httpClient = new DefaultHttpClient();
	        HttpContext localContext = new BasicHttpContext();
		    HttpPost httpPost = new HttpPost("http://idzikovski.heliohost.org/SQL_Connect.php");
	        
			
			try {
				
				MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
				for(int i=0; i < nameValuePairs.size(); i++)
				{
					if(nameValuePairs.get(i).getName().equalsIgnoreCase("slika"))
					{
						mediaFile=new File (nameValuePairs.get(i).getValue());
						entity.addPart(nameValuePairs.get(i).getName(), new FileBody(mediaFile));
					}
					else
					{
						entity.addPart(nameValuePairs.get(i).getName(), new StringBody(nameValuePairs.get(i).getValue()));
					}
				}
				
				httpPost.setEntity(entity);
				HttpResponse response = httpClient.execute(httpPost,localContext);
				
				if (mediaFile!=null)
					mediaFile.delete();  // Izbrisi ja slikata od karticka
				
			} catch (ClientProtocolException e) {
				// TODO: handle exception
			}catch (IOException e) {
				// TODO: handle exception
			}catch (Exception e) {
				Log.i("Ivan Dzikovski", "Faten bese isklucok "+e.getMessage());
		       //Toast.makeText(Vnes.this, e.toString(), Toast.LENGTH_LONG);
		    }
			
			
			
			
			return null;
		}
		

		/** Metod za obrabotuvanje na rezultatot od postot */

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			//setProgressBarIndeterminateVisibility(false);
			pb=(ProgressBar) findViewById(R.id.pbLoading);
			pb.setVisibility(View.INVISIBLE);
			Toast.makeText(Vnes.this, "Успешен внес", Toast.LENGTH_LONG).show();
			finish();
		}
		
	}
	
	
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE=100;
	private Uri fileUri;
	
	/** Metod koj se povikuva koga e pritisnato kopceto btnIsprati */
	
	public void btnSlikaj_CLicked(View view)
	{
		Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		
		fileUri=getOutputMediaFileUri();
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		
		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
		
	}
	
	
	
	/** Metod koj se izvrsuva po vrakanje na rezultat od Image Capture intentot */
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		//super.onActivityResult(requestCode, resultCode, data);
		
		
		File mediaStorageDir=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"Najdi");
		File mediaFile=new File(mediaStorageDir.getPath()+File.separator+"IMG.jpg");
		if (mediaFile.exists())
		{
		
			/*float scaleFactor;
			slika= BitmapFactory.decodeFile(mediaFile.getAbsolutePath());
			scaleFactor=MAX_HEIGHT/slika.getHeight();
			slikaNamalena=Bitmap.createScaledBitmap(slika, Math.round(scaleFactor*slika.getWidth()), Math.round(scaleFactor*slika.getHeight()), true);*/
			
			slikaNamalena=decodeSampledBitmapFromFile(mediaFile.getAbsoluteFile(), MAX_HEIGHT);
			slika=decodeSampledBitmapFromFile(mediaFile.getAbsoluteFile(), MAX_HEIGHT_WEB);
			
			
			iv=(ImageView)findViewById(R.id.imgPrikaz);
			iv.setImageBitmap(slikaNamalena);
			mediaFile.delete();
			
			//Zacuvaj ja namalenata slika za upload
			
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			slika.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
			
			

			File file=new File(mediaStorageDir.getPath()+File.separator+"IMG_R.jpg");
			
			
			try {
				file.createNewFile();
				FileOutputStream fo = new FileOutputStream(file);
				fo.write(bytes.toByteArray());
				fo.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			slika.recycle();
			
			
			
			//Toast.makeText(this, String.valueOf(slika.getWidth())+"x"+String.valueOf(slika.getHeight()), Toast.LENGTH_LONG).show();
			//Toast.makeText(Vnes.this, String.valueOf(scaleFactor), Toast.LENGTH_LONG).show();
		}
		
		/*Bitmap photo = (Bitmap) data.getExtras().get("data"); 
        iv=(ImageView)findViewById(R.id.imgSlika);
        iv.setImageBitmap(photo);*/
     
        //Toast.makeText(this, String.valueOf(photo.getHeight())+String.valueOf(photo.getWidth()),Toast.LENGTH_LONG).show();
		
	}
	
	/** Metod sto vcituva namalena slika */
	
	public static Bitmap decodeSampledBitmapFromFile(File file, int maxHeight)
	{
		final BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=true;
		BitmapFactory.decodeFile(file.getAbsolutePath(), options);
		
		options.inSampleSize=calculateInSampleSize(options, maxHeight);
		
		options.inJustDecodeBounds=false;
		return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
	}
	
	
	/** Pomosen metod za premetuvanje na odnos na golemini na sliki */
	
	public static int calculateInSampleSize(BitmapFactory.Options options, int maxHeight)
	{
		final int height=options.outHeight;
		int inSampleSize=1;
		
		if (height>maxHeight)
		{
			final int heightRatio=Math.round((float)height/(float)maxHeight);
			inSampleSize=heightRatio;
		}
		
		return inSampleSize;
	}
	

	/** Pomosni metodi za formiranje na pateka za slikata */
	
	private static Uri getOutputMediaFileUri()
	{
		return Uri.fromFile(getOutputMediaFile());
	}
	
	private static File getOutputMediaFile()
	{
		File mediaStorageDir=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"Najdi");
		
		if (!mediaStorageDir.exists())
			if(!mediaStorageDir.mkdirs())
				Log.d("Najdi", "Failed to create directory");
	
	
	//String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	
	File mediaFile=new File(mediaStorageDir.getPath()+File.separator+"IMG.jpg");
	
	return mediaFile;
	
	}
	
	
}
