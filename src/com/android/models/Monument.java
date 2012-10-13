package com.android.models;

import java.security.Provider;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.text.format.Time;
import android.util.Log;

import com.android.exceptions.AttributNoValidException;
import com.android.exceptions.MonumentNotFoundException;
import com.android.exceptions.UserNotFoundException;

public class Monument {

	// Nom de la base de données
	public static final String TABLE_NAME = "monuments";

	// Champs de la table
	public static final String COL_ID 			= "_id";
	public static final int NUM_COL_ID 			= 0;
	public static final String COL_LIBELLE 		= "libelle";
	public static final int NUM_COL_LIBELLE 	= 1;
	public static final String COL_DESCRIPTION 	= "description";
	public static final int NUM_COL_DESCRIPTION = 2;
	public static final String COL_LATITUDE		= "latitude";
	public static final int NUM_COL_LATITUDE	= 3;
	public static final String COL_LONGITUDE	= "longitude";
	public static final int NUM_COL_LONGITUDE 	= 4;
	public static final String COL_ACCURACY		= "accuracy";
	public static final int NUM_COL_ACCURACY 	= 5;
	public static final String COL_ALTITUDE		= "altitude";
	public static final int NUM_COL_ALTITUDE 	= 6;
	public static final String COL_DATE			= "date";
	public static final int NUM_COL_DATE 		= 7;
	public static final String COL_IDUSER		= "idUser";
	public static final int NUM_COL_IDUSER 		= 8;

	// Création de la table
	public static final String CREATE_TABLE_USERS = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COL_LIBELLE 		+ " TEXT NOT NULL, " 
			+ COL_DESCRIPTION 	+ " TEXT, " 
			+ COL_LATITUDE 		+ " REAL, " 
			+ COL_LONGITUDE 	+ " REAL, " 
			+ COL_ACCURACY 		+ " FLOAT, " 
			+ COL_ALTITUDE 		+ " REAL, " 
			+ COL_DATE 			+ " long,"
			+ COL_IDUSER 		+ " INTEGER);";

	// Attributs
	private int id = 0;
	private String libelle;
	private String description;
	private Location location;
	private Date date;
	private int idUser;

	private Context context;
	private Database database;

	public Monument(Context context)
	{
		this.context = context;
		this.database = new Database(context);
	}

	public Monument(Context context, int idMonument) throws MonumentNotFoundException
	{
		this(context);
		this.fetchMonumentById(idUser);
	}


	private void fetchMonumentById(int idMonument) {

	}


	public void save() throws AttributNoValidException
	{
		ContentValues values = new ContentValues();

		if(!this.exist())
		{
			// Insertion du monument
			if(!this.attributesValid())
				throw new AttributNoValidException();

			values.put(COL_LIBELLE, this.libelle);
			values.put(COL_DESCRIPTION, this.description);
			values.put(COL_LATITUDE, this.location.getLatitude());
			values.put(COL_LONGITUDE, this.location.getLongitude());
			values.put(COL_ACCURACY, this.location.getAccuracy());
			values.put(COL_ALTITUDE, this.location.getAltitude());
			values.put(COL_DATE, (long) new Date().getTime());
			values.put(COL_IDUSER, this.idUser);

			this.id = (int) database.getWritableDatabase().insert(TABLE_NAME, null, values);
		}
		else
		{
//			// Modification de l'utilisateur
//			if(this.login != null)
//				values.put(COL_LOGIN, this.login);
//			if(this.mdp != null)
//				values.put(COL_MDP, this.mdp);
//
//			database.getWritableDatabase().update(TABLE_NAME, values, COL_ID + "=" + this.id, null);
		}
	}
	
	private Monument cursorToMonument(Cursor c){
		//si aucun élément n'a été retourné dans la requête, on renvoie null
		if (c.getCount() == 0)
			return null;
 
		Monument monument = new Monument(context);
		
		if(!c.isNull(NUM_COL_ID))
			monument.setId(c.getInt(NUM_COL_ID));
		
		if(!c.isNull(NUM_COL_LIBELLE))
			monument.setLibelle(c.getString(NUM_COL_LIBELLE));
		
		if(!c.isNull(NUM_COL_DESCRIPTION))
			monument.setDescription(c.getString(NUM_COL_DESCRIPTION));
		
		if(!c.isNull(NUM_COL_IDUSER))
			monument.setIdUser(c.getInt(NUM_COL_IDUSER));
		
		//On créé des coordonnees
		Location coord = new Location("bdd");
		
		//on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
		coord.setLatitude(c.getDouble(NUM_COL_LATITUDE));
		coord.setLongitude(c.getDouble(NUM_COL_LONGITUDE));
		
		if(c.isNull(NUM_COL_ALTITUDE)) 
			coord.removeAltitude();
		else 
			coord.setAltitude(c.getDouble(NUM_COL_ALTITUDE));
		
		if(c.isNull(NUM_COL_ACCURACY))
			coord.removeAccuracy();
		else
			coord.setAccuracy(c.getFloat(NUM_COL_ACCURACY));
		
		if(c.isNull(NUM_COL_DATE))
			coord.setTime((new Time()).toMillis(true));
		else
		{
			coord.setTime(c.getLong(NUM_COL_DATE));
			monument.setDate(new Date(c.getLong(NUM_COL_DATE)));
		}
			
		monument.setLocation(coord);
		
		//On retourne le monument
		return monument;
	}

	private boolean attributesValid() {
		if(this.idUser == 0 || this.libelle == null || this.location == null)
			return false;
		else
			return true;
	}

	private void setAttributeToDefault() {
		this.libelle = "pas de libellé";
		this.description = "pas de description";
		this.date = new Date();
		this.idUser = 0;
		this.location = null;
	}

	/**
	 * Renvoie vrai si le monument existe
	 * @return
	 */
	private boolean exist()
	{
		return this.id != 0;
	}

	/**
	 * Accesseurs
	 * @return
	 */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}




}
