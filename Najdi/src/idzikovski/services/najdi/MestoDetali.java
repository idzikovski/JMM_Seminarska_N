package idzikovski.services.najdi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MestoDetali extends Activity {

	TextView tv;
	ImageView iv;
	ProgressBar pb;
	Mesto selectedMesto;
	Bitmap slika;
	Button btn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mesto_detali);
		
		btn=(Button) findViewById(R.id.btnMapa);
		btn.setVisibility(View.INVISIBLE);
		
		selectedMesto=getIntent().getParcelableExtra("selectedMesto");
		tv=(TextView)findViewById(R.id.tvImeDetali);
		
		new LoadImageTask().execute();
		
	
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}
	
	private class LoadImageTask extends AsyncTask<Void, Void, Void>{

		
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			iv=(ImageView)findViewById(R.id.imgSlikaDetali);
			iv.setImageBitmap(slika);
			tv.setText(selectedMesto.getIme());
			tv=(TextView)findViewById(R.id.tvOpisDetali);
			tv.setText(selectedMesto.getOpis());
			pb=(ProgressBar) findViewById(R.id.pbLoading);
			pb.setVisibility(View.INVISIBLE);
			btn.setVisibility(View.VISIBLE);
			
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			URL req;
			try {
				req = new URL("http://idzikovski.heliohost.org/Sliki/IMG_"+Integer.toString(selectedMesto.getMesto_id())+".jpg");
				slika = BitmapFactory.decodeStream(req.openConnection().getInputStream());
				
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
		
	}
	
	public void btnMapa_Clicked(View view){
		
		Intent detali=new Intent(MestoDetali.this,MestoMap.class);
		detali.putExtra("selectedMesto", selectedMesto);
		startActivity(detali);
		
	}

}
