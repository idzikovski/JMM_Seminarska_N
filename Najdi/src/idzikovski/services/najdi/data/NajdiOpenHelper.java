package idzikovski.services.najdi.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NajdiOpenHelper extends SQLiteOpenHelper {

	
	public static final String COLUMN_ID="mesto_id";
	public static final String COLUMN_IME="ime";
	public static final String COLUMN_OPIS="opis";
	public static final String COLUMN_KATEGORIJA="kategorija";
	public static final String COLUMN_KOORDINATAX="koordinatax";
	public static final String COLUMN_KOORDINATAY="koordinatay";
	public static final String COLUMN_SLIKA="slika";
	
	private static final int DATABASE_VERSION = 3;
	private static final String DATABASE_NAME="Najdi.db";
	public static final String TABLE_NAME="Mesto";
	private static final String DATABASE_CREATE = String.format("create table %s ( %s integer primary key autoincrement, "+
	"%s text not null, %s text not null, %s integer not null, %s double not null, $s double not null, %s text not null) ",
	TABLE_NAME,COLUMN_ID,COLUMN_IME,COLUMN_OPIS,COLUMN_KATEGORIJA,COLUMN_KOORDINATAX,COLUMN_KOORDINATAY,COLUMN_SLIKA);
	String create="create table mesto (mesto_id integer primary key autoincrement, ime text not null, opis text not null, kategorija integer not null, "+
	"koordinatax real not null, koordinatay real not null, slika text not null)";
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(create);		
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
