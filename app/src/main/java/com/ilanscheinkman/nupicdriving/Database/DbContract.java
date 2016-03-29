package com.ilanscheinkman.nupicdriving.Database;

import android.provider.BaseColumns;

import com.ilanscheinkman.nupicdriving.Model.ObdManager;

/**
 * Created by ilan on 3/12/16.
 */
public final class DbContract {
    public static abstract class ReadingTable implements BaseColumns{
        public static final String TABLE_NAME = "CarReadings";
        public static final String TIMESTAMP = "Timestamp";
        public static final String LAT = "Latitude";
        public static final String LONG = "Longitude";

        public static String[] GetFields(){
            String[] fields = new String[ObdManager.FIELDS.length+2];
            fields[0] = LAT;
            fields[1] = LONG;
            for (int i = 2; i<fields.length; i++){
                fields[i] = ObdManager.FIELDS[i-2].getName();
            }
            return fields;
        }

        public static String CreateTable(){
            StringBuilder builder = new StringBuilder();
            builder.append("CREATE TABLE "+TABLE_NAME+"(");
            builder.append(ReadingTable._ID+"Integer Primary Key,");
            for (String field: GetFields()){
                builder.append(field+"Long,");
            }
            builder.append(TIMESTAMP+"Long)");
            return builder.toString();
        }
    }
}
