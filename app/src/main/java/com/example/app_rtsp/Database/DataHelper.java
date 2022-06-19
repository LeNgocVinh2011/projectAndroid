package com.example.app_rtsp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.app_rtsp.Models.Camera_Link;

import java.util.ArrayList;
import java.util.List;

public class DataHelper extends SQLiteOpenHelper {
    private Context context;
    private static final int DATABASE_VERISON = 1;

    private static final String DATABASE_NAME = "CameraDb.db";

    private static final String TABLE_CAMERA = "Camera";

    private static final String COLUMN_CAM_ID ="Cam_Id";
    private static final String COLUMN_CAM_NAME ="Cam_Name";
    private static final String COLUMN_CAM_LINK = "Cam_Link";

    public DataHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERISON);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String script = "CREATE TABLE " + TABLE_CAMERA + "("
                + COLUMN_CAM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_CAM_NAME + " TEXT,"
                + COLUMN_CAM_LINK + " TEXT" + ")";;
        db.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAMERA);
        onCreate(db);
    }

    public ContentValues camValues(Camera_Link cameraLink){
        ContentValues values = new ContentValues();
        values.put(COLUMN_CAM_NAME, cameraLink.getCaName());
        values.put(COLUMN_CAM_LINK, cameraLink.getCaLink());
        return values;
    }

    public boolean addCam(Camera_Link cameraLink){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.insert(TABLE_CAMERA, null, camValues(cameraLink));
        db.close();
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public Boolean updateCam(Camera_Link cam) {
        SQLiteDatabase db = this.getWritableDatabase();
        long data =  db.update(TABLE_CAMERA, camValues(cam), COLUMN_CAM_ID + " = ?",
                new String[]{String.valueOf(cam.getId())});
        if(data != -1){
            return true;
        }else{
            return false;
        }
    }

    public List<Camera_Link> getListCam(){
        List<Camera_Link> listCam = new ArrayList<Camera_Link>();
        String select_query = "SELECT * FROM " + TABLE_CAMERA;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(select_query, null);

        if (cursor.moveToFirst()) {
            do {
                Camera_Link cam = new Camera_Link();
                cam.setId(Integer.parseInt(cursor.getString(0)));
                cam.setCaName(cursor.getString(1));
                cam.setCaLink(cursor.getString(2));
                listCam.add(cam);
            } while (cursor.moveToNext());
        }
        return listCam;
    }

    public Camera_Link getCamDetail(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_CAMERA + " Where " + COLUMN_CAM_ID + " = " + id;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null)
            cursor.moveToFirst();
        Camera_Link cameraLink = new Camera_Link(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        return cameraLink;
    }

    public void deleteCam(Camera_Link cam) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CAMERA, COLUMN_CAM_ID + " = ?",
                new String[] { String.valueOf(cam.getId()) });
        db.close();
    }
}
