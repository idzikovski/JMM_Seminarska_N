package idzikovski.services.najdi;

import com.google.gson.annotations.SerializedName;

public class Kategorija {
	@SerializedName("kategorija_id")
	int kategorija_id;
	@SerializedName("ime")
	String ime;
	
	public static Kategorija kategorii[];
	
	public Kategorija(int kategorija_id, String ime) {
		this.kategorija_id = kategorija_id;
		this.ime = ime;
	}

	public int getKategorija_id() {
		return kategorija_id;
	}

	public void setKategorija_id(int kategorija_id) {
		this.kategorija_id = kategorija_id;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}
	
	
}
