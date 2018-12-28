package com.example.note.mynote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class MyHelper extends SQLiteOpenHelper {
    private ArrayList<String> arrayList1;
    private ArrayList<String> arrayList2;
    private ArrayList<String> arrayList3;
    private ArrayList<String> arrayList4;

    public MyHelper(Context context){
        super(context,"itCastes.db",null,1);
        arrayList1 = new ArrayList<String>();
        arrayList2 = new ArrayList<String>();
        arrayList3 = new ArrayList<String>();
        arrayList4 = new ArrayList<String>();
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE note(_id INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR(100), " +
                "content TEXT, time VARCHAR(20), tag VARCHAR(20))");
        db.execSQL("CREATE TABLE message(_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(20)," +
                "sexual VARCHAR(10), phone VARCHAR(100), locate VARCHAR(20), introduce TEXT)");
        db.execSQL("CREATE TABLE login(_id INTEGER PRIMARY KEY AUTOINCREMENT, account VARCHAR(30), " +
                "password VARCHAR(30))");
    }

    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }

    public void insert(String title,String content,String time, String label){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title",title);
        values.put("content",content);
        values.put("time",time);
        values.put("tag",label);
        long id = db.insert("note",null,values);
        db.close();
        return;
    }

    public void inserts(ArrayList<String> arrayList){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        if(0<arrayList.size()){
            values.put("name",arrayList.get(0));
        }
        else {
            values.put("name","暂无");
        }
        if(1<arrayList.size()){
            values.put("sexual",arrayList.get(1));
        }
        else {
            values.put("sexual","暂无");
        }
        if(2<arrayList.size()){
            values.put("phone",arrayList.get(2));
        }
        else {
            values.put("phone","暂无");
        }
        if(3<arrayList.size()){
            values.put("locate",arrayList.get(3));
        }
        else {
            values.put("locate","暂无");
        }
        if(4<arrayList.size()){
            values.put("introduce",arrayList.get(4));
        }
        else {
            values.put("introduce","暂无");
        }
        long id = db.insert("message",null,values);
        db.close();
        return;
    }

    public void insertes(String account,String password){
        ContentValues values = new ContentValues();
        values.put("account",account);
        values.put("password",password);
        ArrayList<String> arrayListes = finds();
        if(arrayListes!=null&&!arrayListes.isEmpty()){
            SQLiteDatabase db = getWritableDatabase();
            String acc = arrayListes.get(0);
            String psw = arrayListes.get(1);
            db.update("login",values,"account=? and password=?",new String[]{acc,psw});
            db.close();
        }
        else{
            SQLiteDatabase db = getWritableDatabase();
            long id = db.insert("login",null,values);
            db.close();
        }
        return;
    }

    public void replace(String title,String content,String time,String label){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title",title);
        values.put("content",content);
        boolean exist = false;
        String string1="",string2="";
        Cursor cursor = db.rawQuery("select * from note where tag='" + label + "'",null);
        while(cursor.moveToNext()){
            string1 = cursor.getString(cursor.getColumnIndex("title"));
            string2 = cursor.getString(cursor.getColumnIndex("content"));
            if(string1.equals(title)&&string2.equals(content)){
                exist = true;
            }
        }
        if(!exist){
            SQLiteDatabase dbs = getWritableDatabase();
            ContentValues value1 = new ContentValues();
            value1.put("title",title);
            value1.put("content",content);
            value1.put("time",time);
            value1.put("tag",label);
            //System.out.println("database="+title);
            long id = dbs.insert("note",null,value1);
            dbs.close();
        }
        cursor.close();
        db.close();
        return;
    }

    public void update(String lastTitle,String lastContent,String title,String content,String time,String label){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title",title);
        values.put("content",content);
        values.put("time",time);
        db.update("note",values,"title=? and content=? and tag=?",new String[]{lastTitle,lastContent,label});
        db.close();
        return;
    }

    public void updates(ArrayList<String> arrayList,String item){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        if(0<arrayList.size()){
            values.put("name",arrayList.get(0));
        }
        else {
            values.put("name","暂无");
        }
        if(1<arrayList.size()){
            values.put("sexual",arrayList.get(1));
        }
        else {
            values.put("sexual","暂无");
        }
        if(2<arrayList.size()){
            values.put("phone",arrayList.get(2));
        }
        else {
            values.put("phone","暂无");
        }
        if(3<arrayList.size()){
            values.put("locate",arrayList.get(3));
        }
        else {
            values.put("locate","暂无");
        }
        if(4<arrayList.size()){
            values.put("introduce",arrayList.get(4));
        }
        else {
            values.put("introduce","暂无");
        }
        db.update("message",values,"name=?",new String[]{item});
        db.close();
    }

    public void find(String index,String label){
        arrayList1.clear();
        arrayList2.clear();
        arrayList3.clear();
        SQLiteDatabase db = getReadableDatabase();
        String sql = "select * from note where tag='"+label+"' and title like?";
        Cursor cursor = db.rawQuery(sql,new String[]{index+"%"});
        while(cursor.moveToNext()){
            arrayList1.add(cursor.getString(cursor.getColumnIndex("title")));
            arrayList2.add(cursor.getString(cursor.getColumnIndex("content")));
            arrayList3.add(cursor.getString(cursor.getColumnIndex("time")));
        }
        cursor.close();
        db.close();
        return;
    }

    public ArrayList<String> finds(){
        ArrayList<String> arrayList5 = new ArrayList<String>();
        arrayList5.clear();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from login",null);
        while(cursor.moveToNext()){
            arrayList5.add(cursor.getString(cursor.getColumnIndex("account")));
            arrayList5.add(cursor.getString(cursor.getColumnIndex("password")));
        }
        cursor.close();
        db.close();
        return arrayList5;
    }

    public void delete(String title,String content,String label){
        SQLiteDatabase db = getReadableDatabase();
        db.delete("note","title=? and content=? and tag=?",new String[]{title,content,label});
        db.close();
        return;
    }

    public void initial(String label){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from note where tag='"+label+"'",null);
        arrayList1.clear();
        arrayList2.clear();
        arrayList3.clear();
        while(cursor.moveToNext()){
            arrayList1.add(cursor.getString(cursor.getColumnIndex("title")));
            arrayList2.add(cursor.getString(cursor.getColumnIndex("content")));
            arrayList3.add(cursor.getString(cursor.getColumnIndex("time")));
        }
        return;
    }

    public void initials(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from message",null);
        arrayList4.clear();
        while(cursor.moveToNext()){
            arrayList4.add(cursor.getString(cursor.getColumnIndex("name")));
            arrayList4.add(cursor.getString(cursor.getColumnIndex("sexual")));
            arrayList4.add(cursor.getString(cursor.getColumnIndex("phone")));
            arrayList4.add(cursor.getString(cursor.getColumnIndex("locate")));
            arrayList4.add(cursor.getString(cursor.getColumnIndex("introduce")));
        }
        return;
    }

    public ArrayList<String> backValue1(){
        return arrayList1;
    }

    public ArrayList<String> backValue2(){
        return arrayList2;
    }

    public ArrayList<String> backValue3(){
        return arrayList3;
    }

    public ArrayList<String> backValue4(){
        return arrayList4;
    }
}
