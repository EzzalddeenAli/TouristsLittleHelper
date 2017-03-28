package com.insulardevelopment.touristslittlehelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.insulardevelopment.touristslittlehelper.placetype.PlaceType;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Маргарита on 27.03.2017.
 */

public class DataBaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "main.db";
    private static final int DATABASE_VERSION = 2;

    private Dao<PlaceType, Integer> placeTypeDao = null;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, PlaceType.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try{
            TableUtils.dropTable(connectionSource, PlaceType.class, true);
            onCreate(database, connectionSource);
        }
        catch (java.sql.SQLException e){
            e.printStackTrace();
        }
    }

    public Dao getTestDao() throws SQLException{
        if(placeTypeDao == null){
            placeTypeDao = getDao(PlaceType.class);
        }
        return placeTypeDao;
    }
}
