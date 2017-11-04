package com.katamapps.sqlitedatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDataBase extends SQLiteOpenHelper
{
    public static String eid;
    public static String ename;
    public static String ephone;
    public static String eaddress;
    public static String eimage;
    public static String tablename;
    public static String time;
    boolean exits;
    Context context;
    static Cursor cursor;
    static
    {
        tablename = "employeedetails";
        eid = "eno";
        ename = "ename";
        ephone = "ephone";
        eaddress="eaddress";
        eimage="eimage";
        time="times";
    }

    public MyDataBase(MainActivity mainActivity, String name)
    {
        super(mainActivity, name, null, 1);
        context=mainActivity;
    }

    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + tablename + "(" + eid + " INTEGER PRIMARY KEY," + ename + " TEXT," + ephone + " TEXT," +  eaddress + " TEXT," + time + " TEXT," + eimage + " TEXT"+")");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    	
    }

    public void insertRecord(String empid, String empname, String cellno, String empaddress, String empphoto, String times)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        int id = Integer.parseInt(empid);
        values.put(eid, Integer.valueOf(id));
        values.put(ename, empname);
        values.put(ephone, cellno);
        values.put(eaddress, empaddress);
        values.put(eimage, empphoto);
        values.put(time, times);
        db.insert(tablename, null, values);
        Log.e("msg", "record inserted succusfully: id:"+empid+" ename:"+empname+" ephone: "+cellno+" empadress: "+empaddress+" insrt time: "+times);
        //db.close();
    }

    public void updateRecord(String empid, String empname, String empcellno, String empaddres, String empphoto)
    {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(eid, Integer.valueOf(empid));
        values.put(ename, empname);
        values.put(ephone, empcellno);
        values.put(eaddress, empaddres);
        values.put(eimage, empphoto);
        database.update(tablename, values, eid + "=" + empid, null);
        //database.close();
    }

    public void deleteRecord(String eidss)
    {
        getReadableDatabase().execSQL("delete from " + tablename + " where " + eid + "=" + eidss);
    }
    
    
   public Cursor fetchRecords()
    {
      exits=exists(tablename);
      Log.e("msg", "exits: "+exits);
    	if(exits)
    	{
    		SQLiteDatabase db = this.getReadableDatabase();
    		cursor = db.query(tablename, null, null, null, null, null, time + " desc");
    	}
    	return cursor;		
    }
   
   public boolean exists(String table)
   {
       try
       {
       	SQLiteDatabase db = this.getReadableDatabase();
       	db.rawQuery("SELECT  * FROM " + table, null);
            return true;
       }
       catch (SQLException e)
       {
            return false;
       }
   }
}
