package com.example.kristian.scoutingapp.Model;

import java.util.Date;

public class Model {
	private String navn;
	private int alder;
	private int nummer;
	private long id;
	private double lat, lng;
	private String dato;
	private String imagePath;

	public Model(String navn, int alder, int nummer, double lng, double lat, String dato, String imagePath){
		this.navn = navn;
		this.alder = alder;
		this.nummer = nummer;
		this.lng = lng;
		this.lat = lat;
		this.dato = dato;
		this.imagePath = imagePath;
	}
	public Model(String navn, int alder, int nummer, double lng, double lat, String dato){
		this.navn = navn;
		this.alder = alder;
		this.nummer = nummer;
		this.lng = lng;
		this.lat = lat;
		this.dato = dato;
		imagePath = "void";
	}

	public Model(long id, String navn, int alder, int nummer, double lng, double lat, String dato, String imagePath){
		this.id = id;
		this.navn = navn;
		this.alder = alder;
		this.nummer = nummer;
		this.lng = lng;
		this.lat = lat;
		this.dato = dato;
		this.imagePath = imagePath;
	}


	public String getNavn() {
		return navn;
	}

	public int getAlder() {
		return alder;
	}

	public int getNummer() {
		return nummer;
	}

	public long getId() {
		return id;
	}

	public double getLat() {
		return lat;
	}

	public double getLng() {
		return lng;
	}

	public String getDato() {
		return dato;
	}

	public String getImagePath() {
		return imagePath;
	}

	@Override
	public String toString() {
		return "ID: " + id + "\nNavn: " + navn + "\nAlder: " + alder + "\nNummer: " + nummer + "\nLongtitude: " + lng + "\nLatitude: " + lat + "\nDato: " + dato;
	}
}
