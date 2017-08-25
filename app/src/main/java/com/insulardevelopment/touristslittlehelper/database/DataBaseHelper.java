package com.insulardevelopment.touristslittlehelper.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.insulardevelopment.touristslittlehelper.model.PlaceType;
import com.insulardevelopment.touristslittlehelper.model.Route;
import com.insulardevelopment.touristslittlehelper.model.Place;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Класс для работы с базой данных
 */

public class DataBaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "main.db";
    private static final int DATABASE_VERSION = 7;

    private Dao<PlaceType, Integer> placeTypeDao = null;
    private Dao<Route, Integer> routeDao = null;
    private Dao<Place, Integer> placeDao = null;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, PlaceType.class);
            TableUtils.createTable(connectionSource, Route.class);
            TableUtils.createTable(connectionSource, Place.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try{
            TableUtils.dropTable(connectionSource, PlaceType.class, true);
            TableUtils.dropTable(connectionSource, Route.class, true);
            TableUtils.dropTable(connectionSource, Place.class, true);
            onCreate(database, connectionSource);
        }
        catch (java.sql.SQLException e){
            e.printStackTrace();
        }
    }

    public Dao getPlaceDao() throws SQLException{
        if(placeDao == null){
            placeDao = getDao(Place.class);
        }
        return placeDao;
    }

    public Dao getRouteDao() throws SQLException{
        if(routeDao == null){
            routeDao = getDao(Route.class);
        }
        return routeDao;
    }

    public Dao getTypeDao() throws SQLException{
        if(placeTypeDao == null){
            placeTypeDao = getDao(PlaceType.class);
        }
        return placeTypeDao;
    }
}
