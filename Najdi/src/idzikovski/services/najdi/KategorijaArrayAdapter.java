package idzikovski.services.najdi;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class KategorijaArrayAdapter extends ArrayAdapter<Kategorija> {
	
	private Activity context;
	private Kategorija[] values;
	
	public KategorijaArrayAdapter(Activity context, Kategorija[] values){
		super(context, R.layout.rowlayout, values);
		this.context=context;
		this.values=values;
	}
	
	
	
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View rowView=convertView;
		if (rowView==null)
		{
			LayoutInflater inflater=context.getLayoutInflater();
			rowView=inflater.inflate(R.layout.rowlayout, null);
			ListViewHolder viewHolder=new ListViewHolder();
			viewHolder.ime=(TextView)rowView.findViewById(R.id.tvImeList);
			viewHolder.slika=(ImageView)rowView.findViewById(R.id.imgListItem);
			rowView.setTag(viewHolder);
		}
		
		
		ListViewHolder holder=(ListViewHolder)rowView.getTag();
		String ime=values[position].getIme();
		holder.ime.setText(ime);
		
		return rowView;
		
	}




	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View rowView=convertView;
		if (rowView==null)
		{
			LayoutInflater inflater=context.getLayoutInflater();
			rowView=inflater.inflate(R.layout.rowlayout, null);
			ListViewHolder viewHolder=new ListViewHolder();
			viewHolder.ime=(TextView)rowView.findViewById(R.id.tvImeList);
			viewHolder.slika=(ImageView)rowView.findViewById(R.id.imgListItem);
			rowView.setTag(viewHolder);
		}
		
		
		ListViewHolder holder=(ListViewHolder)rowView.getTag();
		String ime=values[position].getIme();
		holder.ime.setText(ime);
		
		return rowView;
	};
}
