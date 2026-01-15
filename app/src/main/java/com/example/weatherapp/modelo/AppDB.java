package com.example.weatherapp.modelo;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;


/*EXPLICAR ESTO, QUE USA EL PATRÓN SINGLETON*/
@Database(entities = {Ciudad.class}, version = 1, exportSchema = false)
public abstract class AppDB extends RoomDatabase {
    public abstract CiudadDAO ciudadDAO();

    public static volatile AppDB INSTANCE;

    public static AppDB getInstance(Context context){
        if(INSTANCE == null){
            synchronized (AppDB.class){
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDB.class, "Tiempo_db")
                            .addCallback(seedCallback(context.getApplicationContext())).build();

                }
            }
        }
        return INSTANCE;
    }
    /*EXPLICAR ESTO CUANDO SE CREA LA BASE DE DATOS Y SE INSERTAN LOS datos si esta vacio*/
    private static RoomDatabase.Callback seedCallback(Context appContexto){
        return new RoomDatabase.Callback(){
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db){
                super.onCreate(db);

                Executors.newSingleThreadExecutor().execute(() -> {
                    CiudadDAO dao = INSTANCE.ciudadDAO();

                    if(dao.contarCiudades() == 0){
                        List<Ciudad> ciudades = Arrays.asList(
                                new Ciudad("Madrid", 19.0, "Soleado"),
                                new Ciudad("Barcelona", 24.0, "Soleado"),
                                new Ciudad("Oslo", -3.0, "Nieve"),
                                new Ciudad("Londres", 10.0, "Lluvia"),
                                new Ciudad("Liverpool", 12.0, "Nublado"),
                                new Ciudad("Berlín", 14.0, "Nublado"),
                                new Ciudad("Tokyo", 11.0, "Lluvia")
                        );
                        dao.insertarTodos(ciudades);
                    }
                });
            }
        };
    }

    /*creo que tengo que quitar esto y ponerlo en el adapter*/
    public static class R_drawable {
        public static int sol;
        public static int nube;
        public static int lluvia;
        public static int nieve;
    }
}
