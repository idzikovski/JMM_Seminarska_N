package idzikovski.services.najdi.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NajdiOpenHelper extends SQLiteOpenHelper {

	
	private static final String COLUMN_ID="mesto_id";
	private static final String COLUMN_IME="ime";
	private static final String COLUMN_OPIS="opis";
	private static final String COLUMN_KATEGORIJA="kategorija";
	private static final String COLUMN_KOORDINATAX="koordinatax";
	private static final String COLUMN_KOORDINATAY="koordinatay";
	private static final String COLUMN_SLIKA="slika";
	
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME="Najdi.db";
	private static final String TABLE_NAME="Mesto";
	private static final String DATABASE_CREATE = String.format("create table %s (%s integer primary key autoincrement, "+
	"%s text not null, %s text not null, %s integer not null, %s double not null, $s double not null, %s text not null  ",
	TABLE_NAME,COLUMN_ID,COLUMN_IME,COLUMN_OPIS,COLUMN_KATEGORIJA,COLUMN_KOORDINATAX,COLUMN_KOORDINATAY,COLUMN_SLIKA);
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);		
	}
	
	NajdiOpenHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(String.format("drop table if exists %s", TABLE_NAME));
		onCreate(db);

	}

}
