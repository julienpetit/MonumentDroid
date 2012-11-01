package com.iutbm.monumentdroid.models;

import java.util.ArrayList;
import java.util.Date;

import com.iutbm.monumentdroid.exceptions.AttributNoValidException;
import com.iutbm.monumentdroid.exceptions.CommentNotFoundException;
import com.iutbm.monumentdroid.exceptions.MonumentNotFoundException;
import com.iutbm.monumentdroid.exceptions.UserNotFoundException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.text.format.Time;
import android.util.Log;

public class Monument {

	@SuppressWarnings("unused")
	private final String LOG_ID = this.getClass().getName();
	
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
	public static final String CREATE_TABLE_MONUMENT = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COL_LIBELLE 		+ " TEXT NOT NULL, " 
			+ COL_DESCRIPTION 	+ " TEXT, " 
			+ COL_LATITUDE 		+ " REAL, " 
			+ COL_LONGITUDE 	+ " REAL, " 
			+ COL_ACCURACY 		+ " FLOAT, " 
			+ COL_ALTITUDE 		+ " REAL, " 
			+ COL_DATE 			+ " long,"
			+ COL_IDUSER 		+ " INTEGER);";

	public static final String[] ADD_DEFAULT_VALUES = {"INSERT INTO " + TABLE_NAME + " VALUES( null, 'Lion de Belfort', 'Le Lion de Belfort est une sculpture monumentale en haut-relief du sculpteur alsacien Auguste Bartholdi, située à Belfort en France au pied de la falaise de la citadelle.', '47.636633', '6.864519', '3', '420', '1351787659492', '1');", 
														"INSERT INTO " + TABLE_NAME + " VALUES( null, 'Théâtre Gallo-Romain de Mandeure','Le théâtre gallo-romain de Mandeure se situe dans le pays de Montbéliard (département du Doubs, France). Il date du ier siècle, et est donc contemporain du Colisée à Rome. Il a été découvert en 1819, fouillé en 1820 puis à partir de 19461. Il est protégé au titre des monuments historiques.', '47.44901', '6.79622', '13', '389', '1351787659492', '2');",
														"INSERT INTO " + TABLE_NAME + " VALUES( null, 'La tour Eiffel','La tour Eiffel est une tour de fer puddlé de 324 mètres de hauteur située à Paris. ce monument est devenu le symbole de la capitale française, et un site touristique de premier plan : il s agit du second site culturel français payant le plus visité en 2011, avec 7,1 millions de visiteurs dont 75% d étrangers en 2011', '48.85825', '2.2945', '4', '200', '1351787659492', '2');",
														"INSERT INTO " + TABLE_NAME + " VALUES( null, 'Les volcans d auvergne','Le volcanisme du Massif central a été actif sur les périodes tertiaire et quaternaire, l âge des volcans s étageant de 65 Millions d années pour les plus anciens à seulement 7000 ans (voire moins)1 pour la chaîne des Puys. Le point culminant de l Auvergne, 1 886 m, se situe au Puy de Sancy dans le massif des monts Dore datant du tertiaire.', '45.664566', '2.950859', '8', '600', '1351787659492', '1');",
														"INSERT INTO " + TABLE_NAME + " VALUES( null, 'Stonehenge','	Stonehenge, dont le nom signifie « les pierres suspendues », est un grand monument mégalithique composé d un ensemble de structures circulaires concentriques, érigé entre -2800 et -11001, du Néolithique à l âge du bronze. Il est situé à treize kilomètres au nord de Salisbury, et à quatre kilomètres à l ouest d Amesbury', '51.178889', '-1.826389', '8', '600', '1351787659492', '1');"


														};

	
	// Attributs
	private int id = 0;
	private String libelle;
	private String description;
	private Location location;
	private Date date;
	private int idUser;
	private ArrayList<Comment> listeDeCommentaires;
	private ArrayList<Photo> listeDePhotos;
 
	private Context context;
	private Database database;

	public Monument(Context context)
	{
		this.context 				= context;
		this.database 				= new Database(context);
		this.listeDeCommentaires 	= new ArrayList<Comment>();
		this.listeDePhotos 			= new ArrayList<Photo>();
	}

	public Monument(Context context, int _idMonument) throws MonumentNotFoundException, CommentNotFoundException, UserNotFoundException
	{
		this(context);
		this.fetchMonumentById(_idMonument);
		this.listeDeCommentaires 	= Comment.getAllCommentsOfMonument(context, _idMonument);
		this.listeDePhotos			= new ArrayList<Photo>();
	}


	private void fetchMonumentById(int _idMonument) throws MonumentNotFoundException {
		SQLiteDatabase db = this.database.getReadableDatabase();
		Cursor c = db.query(TABLE_NAME, new String[]{
				COL_ID,
				COL_LIBELLE,
				COL_DESCRIPTION,
				COL_LATITUDE,
				COL_LONGITUDE,
				COL_ACCURACY,
				COL_ALTITUDE,
				COL_DATE,
				COL_IDUSER}, COL_ID + "=" + _idMonument, null, null, null, null, null);

		if(c.getCount() == 0)
			throw new MonumentNotFoundException();

		c.moveToFirst();
		Monument monument = this.cursorToMonument(c);
		c.close();	
		db.close();
	
		this.id = monument.getId();
		this.idUser = monument.getIdUser();
		this.date = monument.getDate();
		this.libelle = monument.getLibelle();
		this.description = monument.getDescription();
		this.location = monument.getLocation();
	}


	public void save() throws AttributNoValidException
	{
		SQLiteDatabase db = database.getWritableDatabase();
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
			values.put(COL_DATE, new Date().getTime());
			values.put(COL_IDUSER, this.idUser);

			this.id = (int) db.insert(TABLE_NAME, null, values);
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
		db.close();
		
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

	public static ArrayList<Monument> getAllMonuments(Context context, long distance, Location loc) throws MonumentNotFoundException, CommentNotFoundException, UserNotFoundException
	{
		ArrayList<Monument> listOfMonuments = new ArrayList<Monument>();

		Database database = new Database(context);
		SQLiteDatabase db = database.getReadableDatabase();

		Cursor c = db.query(TABLE_NAME, new String[]{COL_ID}, null, null, null, null, null);

		c.moveToFirst();

		// Tant qu'on lit des lignes
		while(!c.isAfterLast()){
			if(!c.isNull(NUM_COL_ID))
			{
				Log.d("Monument Model", "noMonument : " + c.getInt(NUM_COL_ID));
				Monument monument = new Monument(context, c.getInt(NUM_COL_ID));
				
				if(loc != null && distance > 0)
				{
					if(loc.distanceTo(monument.getLocation()) <= distance)
						listOfMonuments.add(monument);
				}
				else 
				{
					listOfMonuments.add(monument);	
				}
				
			}
				
			c.moveToNext();
		};
		c.close();
		db.close();

		return listOfMonuments;
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

	public ArrayList<Comment> getListeDeCommentaires() {
		return listeDeCommentaires;
	}

	public void setListeDeCommentaires(ArrayList<Comment> listeDeCommentaires) {
		this.listeDeCommentaires = listeDeCommentaires;
	}

	public ArrayList<Photo> getListeDePhotos() {
		return listeDePhotos;
	}

	public void setListeDePhotos(ArrayList<Photo> listeDePhotos) {
		this.listeDePhotos = listeDePhotos;
	}




}
