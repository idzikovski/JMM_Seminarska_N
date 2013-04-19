package idzikovski.services.najdi;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class Mesto implements Parcelable {
	@SerializedName("mesto_id")
	private int mesto_id;
	
	@SerializedName("ime")
	private String ime;
	
	@SerializedName("opis")
	private String opis;
	
	@SerializedName("kategorija")
	private int kategorija;
	
	@SerializedName("koordinatax")
	private double koordinatax;
	
	@SerializedName("koordinatay")
	private double koordinatay;
	
	@SerializedName("slika")
	private String slika;
	
	
	public Mesto(int mesto_id, String ime, String opis, int kategorija, double koordinatax, double koordinatay, String slika){
		this.mesto_id=mesto_id;
		this.ime=ime;
		this.opis=opis;
		this.kategorija=kategorija;
		this.koordinatax=koordinatax;
		this.koordinatay=koordinatay;
		this.slika=slika;
	}
	
	public Mesto(){
		this.mesto_id=0;
		this.ime="";
		this.opis="";
		this.kategorija=0;
		this.koordinatax=0;
		this.koordinatay=0;
		this.slika="";
	}

	public Mesto(Parcel source) {
		// TODO Auto-generated constructor stub
		mesto_id=source.readInt();
		ime=source.readString();
		opis=source.readString();
		kategorija=source.readInt();
		koordinatax=source.readDouble();
		koordinatay=source.readDouble();
		slika=source.readString();
	}


	public int getMesto_id() {
		return mesto_id;
	}

	public void setMesto_id(int mesto_id) {
		this.mesto_id = mesto_id;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public int getKategorija() {
		return kategorija;
	}

	public void setKategorija(int kategorija) {
		this.kategorija = kategorija;
	}

	public double getKoordinatax() {
		return koordinatax;
	}

	public void setKoordinatax(double koordinatax) {
		this.koordinatax = koordinatax;
	}

	public double getKoordinatay() {
		return koordinatay;
	}

	public void setKoordinatay(double koordinatay) {
		this.koordinatay = koordinatay;
	}

	public String getSlika() {
		return slika;
	}

	public void setSlika(String slika) {
		this.slika = slika;
	}


	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(mesto_id);
		dest.writeString(ime);
		dest.writeString(opis);
		dest.writeInt(kategorija);
		dest.writeDouble(koordinatax);
		dest.writeDouble(koordinatay);
		dest.writeString(slika);
	}
	
	public static final Parcelable.Creator<Mesto> CREATOR
		= new Parcelable.Creator<Mesto>() {

			@Override
			public Mesto createFromParcel(Parcel source) {
				// TODO Auto-generated method stub
				return new Mesto(source);
			}

			@Override
			public Mesto[] newArray(int size) {
				// TODO Auto-generated method stub
				return new Mesto[size];
			}
			
		};
	 
}
