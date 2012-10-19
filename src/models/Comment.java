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
import exceptions.CommentNotFoundException;


public class Comment {

	// Nom de la base de données
	public static final String TABLE_NAME = "comments";

	// Champs de la table
	public static final String COL_ID = "_id";
	public static final int NUM_COL_ID = 0;
	public static final String COL_IDUSER= "idUser";
	public static final int NUM_COL_IDUSER = 1;
	public static final String COL_IDMONUMENT= "idMonument";
	public static final int NUM_COL_IDMONUMENT = 2;
	public static final String COL_DATE= "date";
	public static final int NUM_COL_DATE = 3;
	public static final String COL_MESSAGE= "message";
	public static final int NUM_COL_MESSAGE = 4;

	// Création de la table
	public static final String CREATE_TABLE_USERS = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" 
								+ COL_ID + " INTEGER PRIMARY KEY AUTO_INCREMENT, "
								+ COL_IDUSER + " INTEGER, " 
								+ COL_IDMONUMENT + " INTEGER, "
								+ COL_DATE + " long,"
								+ COL_MESSAGE + " TEXT NOT NULL);";

	// Attributs
	private int id = 0;
	private int idUser = 0;
	private int idMonument = 0;
	private Date date;
	private String message;

	private Context context;
	private Database database;

	public Comment(Context context)
	{
		this.context = context;
		this.database = new Database(context);
	}

	public Comment(Context context, int idComment) throws CommentNotFoundException
	{
		this(context);
		this.fetchCommentById(idComment);
	}


	/**
	 * Insertion d'un commentaire
	 * @throws AttributNoValidException 
	 */
	public void save() throws AttributNoValidException
	{
		SQLiteDatabase db = database.getWritableDatabase();
		ContentValues values = new ContentValues();

		if(!this.exist())
		{
			if(id == 0 || idUser == 0 || idMonument == 0 || message == null)
				throw new AttributNoValidException();
			
			values.put(COL_IDUSER, this.idUser);
			values.put(COL_IDMONUMENT, this.idMonument);
			values.put(COL_DATE, (long) new Date().getTime());
			values.put(COL_MESSAGE, this.message);

			this.id = (int) db.insert(TABLE_NAME, null, values);
		}
		else
		{
			if(id == 0 || idUser == 0 || idMonument == 0 || message == null)
				throw new AttributNoValidException();
			
			values.put(COL_IDUSER, this.idUser);
			values.put(COL_IDMONUMENT, this.idMonument);
			values.put(COL_DATE, (long) new Date().getTime());
			values.put(COL_MESSAGE, this.message);

			db.update(TABLE_NAME, values, COL_ID + "=" + this.id, null);
		}
		db.close();
	}

	/**
	 * Supprime le commentaire
	 * @throws CommentNotFoundException 
	 */
	public void delete() throws CommentNotFoundException
	{
		if(!this.exist())
			throw new CommentNotFoundException();
		this.database.getWritableDatabase().delete(TABLE_NAME, COL_ID + "=" + this.id, null);
	}

	/**
	 * Récupère l'ensemble des informations d'un utilisateur
	 * @param idComment
	 * @throws CommentNotFoundException 
	 */
	private void fetchCommentById(int idComment) throws CommentNotFoundException {
		SQLiteDatabase db = this.database.getReadableDatabase();
		Cursor c = db.query(TABLE_NAME, new String[]{
				COL_ID,
				COL_IDUSER,
				COL_IDMONUMENT,
				COL_DATE,
				COL_MESSAGE}, COL_ID + "=" + idComment, null, null, null, null, null);


		if(c.getCount() == 0)
			throw new CommentNotFoundException();


		c.moveToFirst();

		this.setId(c.getInt(NUM_COL_ID));
		this.setIdUser(c.getInt(NUM_COL_IDUSER));
		this.setIdMonument(c.getInt(NUM_COL_IDMONUMENT));
		this.setDate(new Date(c.getLong(NUM_COL_DATE)));
		this.setMessage(c.getString(NUM_COL_MESSAGE));

		c.close();	
		db.close();
	}

	private static Comment cursorToComment(Context context, Cursor c) throws CommentNotFoundException
	{
		if(c.getCount() == 0)
			throw new CommentNotFoundException();

		Comment comment = new Comment(context);
		comment.setId(c.getInt(NUM_COL_ID));
		comment.setIdUser(c.getInt(NUM_COL_IDUSER));
		comment.setIdMonument(c.getInt(NUM_COL_IDMONUMENT));
		comment.setDate(new Date(c.getLong(NUM_COL_DATE)));
		comment.setMessage(c.getString(NUM_COL_MESSAGE));

		return comment;
	}

	public static ArrayList<Comment> getAllCommentsOfMonument(Context context, int idMonument) throws CommentNotFoundException
	{
		ArrayList<Comment> listOfComments = new ArrayList<Comment>();

		Database database = new Database(context);
		SQLiteDatabase db = database.getReadableDatabase();

		Cursor c = db.query(TABLE_NAME, new String[]{
				COL_ID,
				COL_IDUSER,
				COL_IDMONUMENT,
				COL_DATE,
				COL_MESSAGE}, COL_IDMONUMENT + "=" + idMonument, null, null, null, null);

		c.moveToFirst();

		// Tant qu'on lit des lignes
		while(!c.isAfterLast()){
			listOfComments.add(cursorToComment(context, c));
			c.moveToNext();
		};
		c.close();
		db.close();

		return listOfComments;
	}

	public boolean exist()
	{
		return this.id != 0;
	}

	public static long getNumberOfComments(Context context)
	{
		Database database = new Database(context);
		SQLiteDatabase db = database.getReadableDatabase();
		return DatabaseUtils.queryNumEntries(db, TABLE_NAME);
	}

	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("id : " + this.id + "\n");
		builder.append("id user : " + this.idUser + "\n");
		builder.append("id monument: " + this.idMonument + "\n");
		builder.append("date : " + this.date+ "\n");
		builder.append("message : " + this.message+ "\n");

		return builder.toString();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public int getIdMonument() {
		return idMonument;
	}

	public void setIdMonument(int idMonument) {
		this.idMonument = idMonument;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}




}
