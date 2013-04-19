package idzikovski.services.najdi.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import idzikovski.services.najdi.Mesto;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class NajdiDao {
	private SQLiteDatabase db;
	private NajdiOpenHelper dbHelper;
	private String[] allColumns={NajdiOpenHelper.COLUMN_ID, NajdiOpenHelper.COLUMN_IME, NajdiOpenHelper.COLUMN_OPIS, NajdiOpenHelper.COLUMN_KATEGORIJA,
			NajdiOpenHelper.COLUMN_KOORDINATAX, NajdiOpenHelper.COLUMN_KOORDINATAY, NajdiOpenHelper.COLUMN_SLIKA};
	
	public NajdiDao(Context context){
		dbHelper=new NajdiOpenHelper(context);
	}
	
	public void open() throws SQLException{
		db=dbHelper.getWritableDatabase();
	}
	
	public void close(){
		db.close();
		dbHelper.close();
	}
	
	public boolean insert(Mesto item){
		long insertID=db.insert(NajdiOpenHelper.TABLE_NAME, null, itemToContentValues(item));
		if (insertID>0)
			return true;
		else return false;
	}
	
	public ArrayList<Mesto> getAllItems() throws Exception {
		ArrayList<Mesto> items= new ArrayList<Mesto>();
		
		Cursor cursor;
		try {
			cursor=db.query(NajdiOpenHelper.TABLE_NAME, allColumns, null, null, null, null, null);
		}
		catch (NullPointerException ex){
			throw new Exception("Moja Null pointer greska");
		}
		catch (SQLException e) {
			// TODO: handle exception
			throw new Exception("Moja SQL greska");
		}
		catch (Exception e){
			throw new Exception("Moja greska");
		
		}
		
		if(cursor.moveToFirst()){
			do{
				items.add(cursorToMesto(cursor));
			}while (cursor.moveToNext());
		}
		cursor.close();
		return items;
	}
	
	public void deleteAll(){
		db.delete(NajdiOpenHelper.TABLE_NAME, null, null);
	}
	
	protected Mesto cursorToMesto(Cursor cursor){
		Mesto item=new  Mesto();
		item.setMesto_id(cursor.getInt(cursor.getColumnIndex(NajdiOpenHelper.COLUMN_ID)));
		item.setIme(cursor.getString(cursor.getColumnIndex(NajdiOpenHelper.COLUMN_IME)));
		item.setOpis(cursor.getString(cursor.getColumnIndex(NajdiOpenHelper.COLUMN_OPIS)));
		item.setKategorija(cursor.getInt(cursor.getColumnIndex(NajdiOpenHelper.COLUMN_KATEGORIJA)));
		item.setKoordinatax(cursor.getDouble(cursor.getColumnIndex(NajdiOpenHelper.COLUMN_KOORDINATAX)));
		item.setKoordinatay(cursor.getDouble(cursor.getColumnIndex(NajdiOpenHelper.COLUMN_KOORDINATAY)));
		item.setSlika(cursor.getString(cursor.getColumnIndex(NajdiOpenHelper.COLUMN_SLIKA)));
		return item;
	}
	
	protected ContentValues itemToContentValues(Mesto item) {
		ContentValues values= new ContentValues();
		//values.put(NajdiOpenHelper.COLUMN_ID, item.getMesto_id());
		values.put(NajdiOpenHelper.COLUMN_IME, item.getIme());
		values.put(NajdiOpenHelper.COLUMN_OPIS, item.getOpis());
		values.put(NajdiOpenHelper.COLUMN_KATEGORIJA, item.getKategorija());
		values.put(NajdiOpenHelper.COLUMN_KOORDINATAX, item.getKoordinatax());
		values.put(NajdiOpenHelper.COLUMN_KOORDINATAY, item.getKoordinatay());
		values.put(NajdiOpenHelper.COLUMN_SLIKA, item.getSlika());
		
		return values;
	}

	
}
