package Database.DataSource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import Database.DatabaseManagement;
import Database.Structure.tb_JafarStructure;
import Database.Table.tb_Jafar;
import iran.com.jafar.SearchActivityJafar;


public class tb_JafarDataSource {
    private SQLiteDatabase database;
    private DatabaseManagement dbManagement;

    private String[] allColumns = {
            tb_JafarStructure.colPK_Jafar,
            tb_JafarStructure.colCompanyName,
            tb_JafarStructure.colproductName,
            tb_JafarStructure.colUrlImg,
            tb_JafarStructure.colYear,
            tb_JafarStructure.colMonth,
            tb_JafarStructure.colDays,
            tb_JafarStructure.colPayCash,
            tb_JafarStructure.colBaqi,
            tb_JafarStructure.colCheckNumber,
            tb_JafarStructure.colCheckYear,
            tb_JafarStructure.colCheckMonth,
            tb_JafarStructure.colCheckDays,
            tb_JafarStructure.colKeywords
    };

    public tb_JafarDataSource(Context context) {
        dbManagement = new DatabaseManagement(context);
    }

    public void Open() throws SQLException {
        database = dbManagement.getWritableDatabase();
    }

    public void Close() {
        dbManagement.close();
        database.close();
    }

    public long Add(tb_Jafar data) {
        ContentValues values = new ContentValues();

        values.put(tb_JafarStructure.colCompanyName, data.CompanyName);
        values.put(tb_JafarStructure.colproductName, data.productName);
        values.put(tb_JafarStructure.colUrlImg, data.UrlImg);
        values.put(tb_JafarStructure.colYear, data.Year);
        values.put(tb_JafarStructure.colMonth, data.Month);
        values.put(tb_JafarStructure.colDays, data.Days);
        values.put(tb_JafarStructure.colPayCash, data.PayCash);
        values.put(tb_JafarStructure.colBaqi, data.Baqi);
        values.put(tb_JafarStructure.colCheckNumber, data.CheckNumber);
        values.put(tb_JafarStructure.colCheckYear, data.CheckYear);
        values.put(tb_JafarStructure.colCheckMonth, data.CheckMonth);
        values.put(tb_JafarStructure.colCheckDays, data.CheckDays);
        values.put(tb_JafarStructure.colKeywords, data.Keywords);

        return database.insert(tb_JafarStructure.tableName, null, values);
    }


    public void DeleteAll() {
        database.delete(tb_JafarStructure.tableName, null, null);
    }

    public void Delete(int id) {
        database.delete(tb_JafarStructure.tableName,
                tb_JafarStructure.colPK_Jafar + "=" + id,
                null);
    }


    public long QueryNumEntries() {
        return DatabaseUtils.queryNumEntries(database, tb_JafarStructure.tableName);
    }

    public tb_Jafar GetRecord() {
        Cursor cursor = database.query(tb_JafarStructure.tableName, allColumns,
                null, null, null, null, null);
        cursor.moveToFirst();

        if (cursor.getCount() == 0) {
            return null;
        }

        tb_Jafar data = ConvertToRecord(cursor);
        cursor.close();
        return data;
    }

    public tb_Jafar GetRecord(int id) {
        Cursor cursor = database.query(tb_JafarStructure.tableName, allColumns,
                tb_JafarStructure.colPK_Jafar + "=" + id,
                null, null, null, null);

        cursor.moveToFirst();

        if (cursor.getCount() == 0) {
            return null;
        }

        tb_Jafar data = ConvertToRecord(cursor);
        cursor.close();
        return data;
    }


    public List<tb_Jafar> GetRecordByCompanyName(String com, String pro, String year, String month, String day, String keys) {

        List<tb_Jafar> lstData = new ArrayList<tb_Jafar>();

        Cursor cursor = database.query(true, tb_JafarStructure.tableName, allColumns,
                tb_JafarStructure.colCompanyName + " LIKE" + " '%" + com + "%' OR "
                        + tb_JafarStructure.colproductName + " LIKE" + " '%" + pro + "%' OR "
                        + tb_JafarStructure.colKeywords + " LIKE" + " '%" + keys + "%' OR "
                        + tb_JafarStructure.colYear + " LIKE" + " '%" + year + "%' OR "
                        + tb_JafarStructure.colMonth + " LIKE" + " '%" + month + "%' OR "
                        + tb_JafarStructure.colDays + " LIKE" + " '%" + day + "%'"
                ,
                null, tb_JafarStructure.colKeywords , null, null, null);

        cursor.moveToFirst();


        while (!cursor.isAfterLast()) {
            tb_Jafar tmpInfo = ConvertToRecord(cursor);
            lstData.add(tmpInfo);
            cursor.moveToNext();
        }

        if (cursor.getCount() == 0) {
            return null;
        }


        cursor.close();
        return lstData;

    }

    public List<tb_Jafar> GetRecordByCompanyName1(String com, String pro, String year, String month, String day, String keys) {

        List<tb_Jafar> lstData = new ArrayList<tb_Jafar>();

        Cursor cursor = database.rawQuery("SELECT * FROM "+ tb_JafarStructure.tableName + " WHERE "+ tb_JafarStructure.colCompanyName + " IN ('"+ com + "' , '" + pro + "')" +
                        "UNION SELECT * FROM "+ tb_JafarStructure.tableName + " WHERE " + tb_JafarStructure.colKeywords + " LIKE '%" + keys + "%'"

                , null);

        cursor.moveToFirst();


        while (!cursor.isAfterLast()) {
            tb_Jafar tmpInfo = ConvertToRecord(cursor);
            lstData.add(tmpInfo);
            cursor.moveToNext();
        }

        if (cursor.getCount() == 0) {
            return null;
        }


        cursor.close();
        return lstData;

    }



    public List<tb_Jafar> GetList() {
        List<tb_Jafar> lstData = new ArrayList<tb_Jafar>();

        Cursor cursor = database.query(tb_JafarStructure.tableName,
                allColumns,
                null, null, null, null,
                tb_JafarStructure.colPK_Jafar + " DESC");
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            tb_Jafar tmpInfo = ConvertToRecord(cursor);
            lstData.add(tmpInfo);
            cursor.moveToNext();
        }

        cursor.close();
        return lstData;
    }


    private tb_Jafar ConvertToRecord(Cursor cursor) {
        tb_Jafar data = new tb_Jafar();


        data.PK_Jafar = cursor.getInt(0);
        data.CompanyName = cursor.getString(1);
        data.productName = cursor.getString(2);
        data.UrlImg = cursor.getString(3);
        data.Year = cursor.getString(4);
        data.Month = cursor.getString(5);
        data.Days = cursor.getString(6);
        data.PayCash = cursor.getString(7);
        data.Baqi = cursor.getString(8);
        data.CheckNumber = cursor.getString(9);
        data.CheckYear = cursor.getString(10);
        data.CheckMonth = cursor.getString(11);
        data.CheckDays = cursor.getString(12);
        data.Keywords = cursor.getString(13);


        return data;
    }

}
