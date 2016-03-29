package com.ilanscheinkman.nupicdriving.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ilanscheinkman.nupicdriving.Model.CarReading;

import rx.Observable;
import rx.Subscriber;

/**
 * Interacts with the Car Reading database.
 * Created by ilan on 3/12/16.
 */
public class DbHelper extends SQLiteOpenHelper{

    public static final int VERSION =1;
    public static final String NAME = "NupicDriving.db";

    public DbHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbContract.ReadingTable.CreateTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO: Check if we changed ObdManager.FIELDS and add/remove fields as necessary.
    }

    public boolean insertReadings(CarReading... readings){
        boolean rval = true;
        for (CarReading reading : readings){
            if (!insertReading(reading)) rval = false;
        }
        return rval;
    }
    public boolean insertReading(CarReading reading){
        ContentValues vals = new ContentValues();
        vals.put(DbContract.ReadingTable._ID, reading.getReadingMap().hashCode());
        vals.put(DbContract.ReadingTable.TIMESTAMP, reading.getTimeStamp());
        String[] fields = DbContract.ReadingTable.GetFields();
        for (String field: fields){
            vals.put(field, reading.getReading(field));
        }

        SQLiteDatabase db = getWritableDatabase();
        long newrow = db.insert(DbContract.ReadingTable.TABLE_NAME, null, vals);
        db.close();
        return newrow != -1;
    }

    public Observable<CarReading> queryReadings(String selection, String[] selectionArgs, String sortOrder){

        //Get all fields
        String[] allFields = new String[DbContract.ReadingTable.GetFields().length +1];
        allFields[0] = DbContract.ReadingTable.TIMESTAMP;
        String[] fieldsWithoutTimestamp = DbContract.ReadingTable.GetFields();
        for (int i = 1; i<allFields.length; i++){
            allFields[i] = fieldsWithoutTimestamp[i-1];
        }

        if (sortOrder == null) sortOrder = DbContract.ReadingTable.TIMESTAMP+" DESC";
        SQLiteDatabase db = getReadableDatabase();
        final Cursor cursor = db.query(DbContract.ReadingTable.TABLE_NAME, allFields, selection, selectionArgs,null,null, sortOrder);
        cursor.moveToFirst();
        Observable<CarReading> out = Observable.create(new Observable.OnSubscribe<CarReading>() {
            @Override
            public void call(Subscriber<? super CarReading> subscriber) {
                while (!cursor.isAfterLast()){
                    try {
                        subscriber.onNext(readReadingFromCursor(cursor));
                        cursor.moveToNext();
                    }catch (Exception e){
                        subscriber.onError(e);
                    }
                }
                subscriber.onCompleted();
            }
        });
        return out;
    }

    private static final CarReading readReadingFromCursor(Cursor cursor) throws Exception{
        CarReading outReading = new CarReading();
        outReading.setTimeStamp(cursor.getLong(cursor.getColumnIndexOrThrow(DbContract.ReadingTable.TIMESTAMP)));
        for (String field : DbContract.ReadingTable.GetFields()){
            outReading.putReading(field, cursor.getLong(cursor.getColumnIndexOrThrow(field)));
        }
        return outReading;
    }
}
