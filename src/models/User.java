package models;

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import exceptions.AttributNoValidException;
import exceptions.UserNotFoundException;


public class User {

	// Nom de la base de données
	public static final String TABLE_NAME = "users";

	// Champs de la table
	public static final String COL_ID = "_id";
	public static final int NUM_COL_ID = 0;
	public static final String COL_LOGIN= "login";
	public static final int NUM_COL_LOGIN = 1;
	public static final String COL_MDP= "mdp";
	public static final int NUM_COL_MDP = 2;
	public static final String COL_DATE= "date_inscription";
	public static final int NUM_COL_DATE = 3;

	// Création de la table
	public static final String CREATE_TABLE_USERS = "CREATE TABLE IF NOT EXISTS " + User.TABLE_NAME + " (" + User.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ User.COL_LOGIN + " TEXT NOT NULL, " 
			+ User.COL_MDP + " TEXT NOT NULL, " 
			+ User.COL_DATE + " long);";

	// Attributs
	private int id = 0;
	private String login;
	private String mdp;
	private Date dateInscription;

	private Context context;
	private Database database;

	public User(Context context)
	{
		this.context = context;
		this.database = new Database(context);
	}

	public User(Context context, int idUser) throws UserNotFoundException
	{
		this(context);
		this.fetchUserById(idUser);
	}


	/**
	 * Insert un utilisateur si il n'existe pas, sinon Met à jour un utilisateur
	 */
	public void save()
	{
		SQLiteDatabase db = database.getWritableDatabase();
		ContentValues values = new ContentValues();

		if(!this.exist())
		{
			// Insersion de l'utilisateur
			if(this.login == null || this.mdp == null)
				this.setAttributeToDefault();

			values.put(COL_LOGIN, this.login);
			values.put(COL_MDP, this.mdp);
			values.put(COL_DATE, (long) new Date().getTime());

			this.id = (int) db.insert(TABLE_NAME, null, values);
		}
		else
		{
			// Modification de l'utilisateur
			if(this.login != null)
				values.put(COL_LOGIN, this.login);
			if(this.mdp != null)
				values.put(COL_MDP, this.mdp);

			db.update(TABLE_NAME, values, COL_ID + "=" + this.id, null);
		}
		db.close();
	}

	/**
	 * Supprime l'utilisateur
	 * @throws UserNotFoundException 
	 */
	public void delete() throws UserNotFoundException
	{
		if(!this.exist())
			throw new UserNotFoundException();
		this.database.getWritableDatabase().delete(TABLE_NAME, COL_ID + "=" + this.id, null);
	}

	/**
	 * Récupère l'ensemble des informations d'un utilisateur
	 * @param idUser
	 * @throws UserNotFoundException 
	 */
	private void fetchUserById(int idUser) throws UserNotFoundException {
		SQLiteDatabase db = this.database.getReadableDatabase();
		Cursor c = db.query(TABLE_NAME, new String[]{
				COL_ID,
				COL_LOGIN,
				COL_MDP,
				COL_DATE}, COL_ID + "=" + idUser, null, null, null, null, null);


		if(c.getCount() == 0)
			throw new UserNotFoundException();


		c.moveToFirst();

		this.setId(c.getInt(NUM_COL_ID));
		this.setLogin(c.getString(NUM_COL_LOGIN));
		this.setMdp(c.getString(NUM_COL_MDP));
		this.setDateInscription(new Date(c.getLong(NUM_COL_DATE)));

		c.close();	
		db.close();
	}


	public void uploadMonument(String libelle, String description, Location location, int idUser) throws AttributNoValidException
	{
		Monument monument = new Monument(context);
		monument.setDate(new Date());
		monument.setDescription(description);
		monument.setLibelle(libelle);
		monument.setLocation(location);
		monument.setIdUser(idUser);
		monument.save();
	}

	public void uploadMonument(String libelle, Location location, int idUser) throws AttributNoValidException
	{
		this.uploadMonument(libelle, "pas de description", location, idUser);
	}

	private void setAttributeToDefault()
	{
		this.login = "inconnu";
		this.mdp = "inconnu";
		this.dateInscription = new Date();
	}


	private static User cursorToUser(Context context, Cursor c) throws UserNotFoundException
	{
		if(c.getCount() == 0)
			throw new UserNotFoundException();

		User user = new User(context);
		user.setId(c.getInt(NUM_COL_ID));
		user.setLogin(c.getString(NUM_COL_LOGIN));
		user.setMdp(c.getString(NUM_COL_MDP));
		user.setDateInscription(new Date(c.getLong(NUM_COL_DATE)));

		return user;
	}

	public static ArrayList<User> getAllUsers(Context context) throws UserNotFoundException
	{
		ArrayList<User> listOfUsers = new ArrayList<User>();

		Database database = new Database(context);
		SQLiteDatabase db = database.getReadableDatabase();

		Cursor c = db.query(TABLE_NAME, new String[]{
				COL_ID,
				COL_LOGIN,
				COL_MDP,
				COL_DATE}, null, null, null, null, null);

		c.moveToFirst();

		// Tant qu'on lit des lignes
		while(!c.isAfterLast()){
			listOfUsers.add(cursorToUser(context, c));
			c.moveToNext();
		};
		c.close();
		db.close();

		return listOfUsers;
	}

	public boolean exist()
	{
		return this.id != 0;
	}


	public static long getNumberOfUsers(Context context)
	{
		Database database = new Database(context);
		SQLiteDatabase db = database.getReadableDatabase();
		return DatabaseUtils.queryNumEntries(db, TABLE_NAME);
	}

	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("id : " + this.id + "\n");
		builder.append("login : " + this.login + "\n");
		builder.append("mdp : " + this.mdp + "\n");
		builder.append("date : " + this.dateInscription + "\n");

		return builder.toString();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getMdp() {
		return mdp;
	}

	public void setMdp(String mdp) {
		this.mdp = mdp;
	}

	public Date getDateInscription() {
		return dateInscription;
	}

	public void setDateInscription(Date dateInscription) {
		this.dateInscription = dateInscription;
	}


}
