package Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import Database.Structure.tb_JafarStructure;


public class DatabaseManagement extends SQLiteOpenHelper {
    public static final String databaseName="MyDbJafar.db";
    public static final int databaseVersion=1;


    public DatabaseManagement(Context context){
        super(context , databaseName , null , databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tb_JafarStructure.createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
