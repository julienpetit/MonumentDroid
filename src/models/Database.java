package models;

 
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
public class Database extends SQLiteOpenHelper {
 
	public static final int VERSION_BDD = 2;
	public static final String NAME_DATABASE = "monumentdroid.db";
	

	public Database(Context context) {
		super(context, Database.NAME_DATABASE, null, Database.VERSION_BDD);
		Log.d("sql", "construct");
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
		//on créé la table à partir de la requête écrite dans la variable CREATE_BDD

		
		db.execSQL(User.CREATE_TABLE_USERS);
		db.execSQL(Monument.CREATE_TABLE_MONUMENT);
		Log.d("sql", Comment.CREATE_TABLE_COMMENT);
		db.execSQL(Comment.CREATE_TABLE_COMMENT);
		Log.d("sql", "onCreate");
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//On peut fait ce qu'on veut ici moi j'ai décidé de supprimer la table et de la recréer
		//comme ça lorsque je change la version les id repartent de 0

		db.execSQL("DROP TABLE IF EXISTS " + User.TABLE_NAME + ";");
		db.execSQL("DROP TABLE IF EXISTS " + Monument.TABLE_NAME + ";");
		db.execSQL("DROP TABLE IF EXISTS " + Comment.TABLE_NAME + ";");
		onCreate(db);
	}
 
}