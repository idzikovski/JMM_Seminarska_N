package idzikovski.services.najdi;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.view.LayoutInflater;

public class MestoArrayAdapter extends ArrayAdapter<Mesto> {
	
	private Activity context;
	private ArrayList <Mesto> values;
	
	public MestoArrayAdapter(Activity context, ArrayList <Mesto> values){
		super(context, R.layout.rowlayout, values);
		this.context=context;
		this.values=values;
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
		String ime=values.get(position).getIme();
		holder.ime.setText(ime);
		
		return rowView;
	};
	
	
}
