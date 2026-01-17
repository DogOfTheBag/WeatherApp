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


/*Para crear nuestra base de datos usaremos el patrón de diseño singleton
* este patron basicamente se resume en solo crear una sola (single) instancia de una clase en la app
* puesto que solo queremos una base de datos en nuestra app, lo usamos para crearla
* de esta forma optimizamos el que solo se cree una db y evitamos errores de duplicidad*/
@Database(entities = {Ciudad.class}, version = 1, exportSchema = false)
public abstract class AppDB extends RoomDatabase {
    public abstract CiudadDAO ciudadDAO();

    public static volatile AppDB INSTANCE;
//voy a explicar esto
    public static AppDB getInstance(Context context){
        //se comprueba si hay una instancia creada de la base de datos
        if(INSTANCE == null){
            /*si no la hay, usamos synchronized para que un único hilo pueda crear la base de datos
            * y no haya problemas*/
            synchronized (AppDB.class){
                //otra comprobacion de si existe por seguridad
                if(INSTANCE == null) {
                    /*si no existe, creamos la instancia de la base de datos usando databaseBuilder
                    * le pasamos el concepto de la clase, el .class de la clase actual, el nombre de la base de datos
                    * y el callback para cuando se cree la bd, y hacemos el build.*/
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDB.class, "Tiempo_db")
                            .addCallback(seedCallback(context.getApplicationContext())).build();

                }
            }
        }
        return INSTANCE;
    }
    /*El callback basicamente lo que hace es añadir un código a ejecutar a la base de datos por primera vez
    * esto lo que nos va a permitir, es que cuando creemos la base de datos, creemos un hilo
    * que se encargue de meter datos en la base de datos por primera vez.
    * Realmente esto es opcional puesto a que el usuario podrá meter sus propias ciudades pero para pruebas
    * esta bien hacerlo, asi tenemos datos desde un principio para ver que todo se genera bien*/
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
